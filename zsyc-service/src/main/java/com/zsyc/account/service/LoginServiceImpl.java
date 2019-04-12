package com.zsyc.account.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.kevinsawicki.http.HttpRequest;
import com.zsyc.account.entity.Account;
import com.zsyc.account.entity.AccountBind;
import com.zsyc.account.mapper.AccountBindMapper;
import com.zsyc.account.mapper.AccountMapper;
import com.zsyc.account.vo.AccountVo;
import com.zsyc.admin.mapper.UserMapper;
import com.zsyc.common.AESCBCUtil;
import com.zsyc.common.AssertExt;
import com.zsyc.common.CommonConstant;
import com.zsyc.common.StringUtil;
import com.zsyc.framework.base.OAuthException;
import com.zsyc.member.entity.MemberAddress;
import com.zsyc.member.entity.MemberInfo;
import com.zsyc.member.mapper.MemberAddressMapper;
import com.zsyc.member.mapper.MemberAddressStoreMapper;
import com.zsyc.member.mapper.MemberInfoMapper;
import com.zsyc.member.vo.MemberAddressStoreVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.zsyc.common.WxPhoneUtil.descrptPhone;


/**
 * Created by lcs on 2018-12-27.
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private AccountBindMapper accountBindMapper;

	@Autowired
	private MemberAddressMapper memberAddressMapper;

	@Autowired
	private MemberInfoMapper memberInfoMapper;

	@Resource
	private RedisTemplate redisTemplate;

	@Autowired
	private MemberAddressStoreMapper memberAddressStoreMapper;

	@Autowired
	private UserMapper userMapper;

	@Value("${zsyc.smallProgramAppId}")
	private String smallProgramAppId;//小程序appid

	@Value("${zsyc.smallProgramSECRET}")
	private String smallProgramSECRET;//小程序密钥

	@Value("${zsyc.publicAppId}")
	private String publicAppId;//公众号appid

	@Value("${zsyc.publicSECRET}")
	private String publicSECRET;//公众号密钥

	@Value("${zsyc.accessKeyId}")
	private String accessKeyId;//短信accessKeyId

	@Value("${zsyc.accessKeySecret}")
	private String accessKeySecret;//短信accessKeySecret

	@Value("${zsyc.templateCode}")
	private String TemplateCode;//短信模板

	@Override
	public Account registry(AccountVo accountVo) {
		AssertExt.notBlank(accountVo.getAccount(), "no account");
		AssertExt.notBlank(accountVo.getPassword(), "no password");
		AssertExt.isTrue(accountVo.getPassword().equals(accountVo.getPasswordConfirm()), "password not equal");
		AssertExt.matches("^[A-Za-z0-9][A-Za-z0-9_\\-]{6,18}[A-Za-z0-9]$", accountVo.getAccount(),
				"账号是数字、字母、下划线、中横线(不能以下划线、中横线开头结尾)；长度8到20位");
		AssertExt.matches(".{6,20}",accountVo.getPassword(),"密码长度8到20位");
		Account accountDB = this.accountMapper.selectOne(new QueryWrapper<Account>().eq("account", accountVo.getAccount()));
		AssertExt.isTrue(accountDB == null, "account[%s] exists", accountVo.getAccount());

		Account account = new Account();
		BeanUtils.copyProperties(accountVo, account);

		account.setCreateTime(new Date());
		account.setUpdateTime(account.getCreateTime());
		account.setSalt(RandomStringUtils.random(10,true,true));
		account.setPassword(DigestUtils.sha1Hex(accountVo.getPassword() + account.getSalt()));
		account.setId(null);
		account.setStatus(1);

		this.accountMapper.insert(account);
		return account;
	}

	@Override
	public Account login(Account account) {
		AssertExt.notBlank(account.getAccount(), "no account");
		AssertExt.notBlank(account.getPassword(), "no password");


		Account accountDB = this.accountMapper.selectOne(new QueryWrapper<Account>().eq("account", account.getAccount()));
		AssertExt.notNull(accountDB, "account[%s] not exist", account.getAccount());

		AssertExt.isTrue(accountDB.getStatus().equals(1), "account not available");

		boolean check = DigestUtils.sha1Hex(account.getPassword() + accountDB.getSalt()).equals(accountDB.getPassword());
		AssertExt.isTrue(check, "password error");

		accountDB.setPassword(null);
		accountDB.setSalt(null);

		return accountDB;

	}

	@Override
	public Account get(String account) {
		AssertExt.notBlank(account, "无效 account");
		QueryWrapper<Account> wrapper = new QueryWrapper<>();
		wrapper.eq("account", account);
		Account accountDB = this.accountMapper.selectOne(wrapper);

		if(accountDB==null) return null;

		accountDB.setPassword(null);
		accountDB.setSalt(null);

		return accountDB;
	}

	@Override
	public List<Account> testApi(Integer num) {
		AssertExt.notNull(num, "无效num");
		return this.accountMapper.getAccounts(num);
	}

	@Override
	@Transactional
	public Account weixinLogin2(String code,String cacheKey) {
		if(org.apache.commons.lang3.StringUtils.isBlank(cacheKey)){
			cacheKey = UUID.randomUUID().toString();
		}

		String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+smallProgramAppId+"&secret="+smallProgramSECRET+"&js_code="+code+"&grant_type=authorization_code";

		HttpRequest httpRequest = HttpRequest.get(url.trim());
		JSONObject jsonObject = JSON.parseObject(httpRequest.body());
		log.debug("jscode2session response {}", jsonObject.toJSONString());

		//判断openid数据有没有

		String openId = jsonObject.getString("openid");

		AssertExt.notBlank(openId, "jscode2session error");

		String unionId = jsonObject.getString("unionid");

		boolean isFromPublic = redisTemplate.opsForValue().get(String.format("public:%s", cacheKey)) != null;

		redisTemplate.opsForValue().set(String.format("wechat:%s", cacheKey), jsonObject.toJSONString(), 24, TimeUnit.HOURS);

		//如果查询的openid数据不存在
		QueryWrapper<AccountBind> wrapper = new QueryWrapper<>();
		wrapper.eq("bind_account", openId);
		wrapper.eq("type", AccountBind.AccountBindType.PROGRAM_OPENID.name());
		AccountBind accountBind = accountBindMapper.selectOne(wrapper);

		if (accountBind == null) {
			//查询unionid数据存不存在
			QueryWrapper<AccountBind> queryWrapper2 = new QueryWrapper<>();
			queryWrapper2.eq("bind_account", unionId);
			queryWrapper2.eq("type", AccountBind.AccountBindType.UNIONID.name());
			AccountBind accountBind2 = accountBindMapper.selectOne(queryWrapper2);

			if(accountBind2 == null){
				if (isFromPublic) {
					throw new OAuthException("公众号跳来，跳注册页面", "0", cacheKey);
				} else {
					throw new OAuthException("openid和unionid数据都没有，跳公众号", "1", cacheKey);
				}
			}else {
				AccountBind openIdAccountBind = new AccountBind();
				openIdAccountBind.setAccountId(accountBind2.getAccountId());
				openIdAccountBind.setBindAccount(openId);
				openIdAccountBind.setType(AccountBind.AccountBindType.PROGRAM_OPENID.name());
				openIdAccountBind.setCreateTime(LocalDateTime.now());
				this.accountBindMapper.insert(openIdAccountBind);
				return accountMapper.selectById(accountBind2.getAccountId());
			}
		}else {
			//openid数据有，直接跳首页
			return accountMapper.selectById(accountBind.getAccountId());
		}


	}

	@Override
	@Transactional
	public Object weixinAccept(String encryptedData, String iv, String cacheKey) {

		AssertExt.notNull(encryptedData, "encryptedData为空");
		AssertExt.notNull(iv, "iv为空");
		AssertExt.notNull(cacheKey, "cacheKey为空");

		Map<String, String> map = new HashMap<>();

		//把授权信息取出
		JSONObject jsonObject = JSONObject.parseObject(redisTemplate.opsForValue().get(String.format("wechat:%s", cacheKey)).toString());
		String sessionKey = jsonObject.getString("session_key");

		//对encryptedData加密数据进行AES解密
		String result = null;
		try {
			result = AESCBCUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
		} catch (Exception e) {
//			e.printStackTrace();
			map.put("msg", "解密失败");
			map.put("code", "2");
			return map;
		}

		if (null != result && result.length() > 0) {
			JSONObject userInfoJSON = JSONObject.parseObject(result);
			jsonObject.put("unionid", userInfoJSON.getString("unionId"));
			redisTemplate.opsForValue().set(String.format("wechat:%s", cacheKey), jsonObject.toJSONString(), 24, TimeUnit.HOURS);

			//跳注册
			map.put("msg", "解密成功");
			map.put("code", "1");
		}else {
			map.put("msg", "解密失败");
			map.put("code", "2");
		}
		return map;
	}


	@Override
	@Transactional
	public Map weixinRegister(String phone, Map<String, Object> userInfo, String phoneCode, String cacheKey, Long memberId){


		AssertExt.notNull(phone, "phone为空");
		AssertExt.notNull(phoneCode, "phoneCode为空");
		AssertExt.notNull(cacheKey, "cacheKey为空");
		AssertExt.isFalse(userInfo == null, "userInfo为空");
//		AssertExt.notNull(memberId, "memberId为空");

		Map<String, Object> map = new HashMap<>();
		LocalDateTime now = LocalDateTime.now();

		JSONObject jsonObject = JSONObject.parseObject(redisTemplate.opsForValue().get(String.format("wechat:%s", cacheKey)).toString());
		String openId = jsonObject.getString("openid");
		String unionId = jsonObject.getString("unionid");
		//从缓存拿code, key为:code + phone
		String vcode = redisTemplate.opsForValue().get("code" + phone).toString();
		AssertExt.isTrue(vcode.equals(phoneCode),"短信验证码错误");


		//查询account是否存在
		QueryWrapper<Account> wrapper2 = new QueryWrapper<>();
		wrapper2.eq("account", phone);
		Account account2 = accountMapper.selectOne(wrapper2);
		Long accountId = 0l;

		if(account2 != null){
			//查询member
			QueryWrapper<MemberInfo> wrapper3 = new QueryWrapper<>();
			wrapper3.eq("account_id", account2.getId());
			MemberInfo memberInfo = memberInfoMapper.selectOne(wrapper3);
			accountId = account2.getId();

			if(memberInfo == null){
				//判断是不是后台下单过的
				getBackEndOrderUser(phone, userInfo, account2, map, memberId);
			}else {
				//'更新member
				UpdateWrapper<MemberInfo> updateWrapper = new UpdateWrapper<>();
				updateWrapper.eq("account_id", memberInfo.getAccountId());
				updateWrapper.set("nick_name", userInfo.get("nickName").toString());
				updateWrapper.set("img", userInfo.get("avatarUrl").toString());
				//gender：0不知，1男2女
				updateWrapper.set("sex", Integer.valueOf(userInfo.get("gender").toString()));
				memberInfoMapper.update(memberInfo, updateWrapper);

				memberInfo = memberInfoMapper.selectOne(wrapper3);
				map.put("memberInfo", memberInfo);
			}
		}else {
			//account数据不存在,  没有就生存account
			Account account = getAccountByPhone(phone);

			if(account == null){
				account = new Account();
				account.setAccount(phone);
				account.setPassword("");
				account.setSalt("");
				account.setStatus(1);
				account.setCreateTime(new Date());
				account.setUpdateTime(new Date());
				accountMapper.insert(account);
			}else {
				UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
				updateWrapper.eq("id", account.getId());
				updateWrapper.set("account", phone);
				updateWrapper.set("status", 1);
				updateWrapper.set("create_time", now);
				updateWrapper.set("update_time", now);
				accountMapper.update(account, updateWrapper);
			}
			accountId = account.getId();

			//判断是不是后台下单过的
			getBackEndOrderUser(phone, userInfo, account, map, memberId);
		}

		//判断有没uid数据
		if(!StringUtils.isBlank(unionId)){
			QueryWrapper<AccountBind> wrapper = new QueryWrapper<>();
			wrapper.eq("bind_account", unionId);
			wrapper.eq("type", AccountBind.AccountBindType.UNIONID.name());
			AccountBind accountBind = accountBindMapper.selectOne(wrapper);

			if(accountBind == null){
				//插入uid数据
				AccountBind accountBind2 = new AccountBind();
				accountBind2.setAccountId(accountId);
				accountBind2.setBindAccount(unionId);
				accountBind2.setType(AccountBind.AccountBindType.UNIONID.name());
				accountBind2.setStatus(1);
				accountBind2.setCreateTime(now);
				accountBind2.setUpdateTime(now);
				accountBindMapper.insert(accountBind2);
			}
		}

		//如果查询的openid数据不存在
		QueryWrapper<AccountBind> wrapper3 = new QueryWrapper<>();
		wrapper3.eq("bind_account", openId);
		wrapper3.eq("type", AccountBind.AccountBindType.PROGRAM_OPENID.name());
		AccountBind accountBind3 = accountBindMapper.selectOne(wrapper3);
		if(accountBind3 == null){
			//插入一条有openid的数据
			AccountBind accountBind1 = new AccountBind();
			accountBind1.setAccountId(accountId);
			accountBind1.setBindAccount(openId);
			accountBind1.setType(AccountBind.AccountBindType.PROGRAM_OPENID.name());
			accountBind1.setStatus(1);
			accountBind1.setCreateTime(now);
			accountBind1.setUpdateTime(now);
			accountBindMapper.insert(accountBind1);
		}
		map.put("code","0");
		map.put("msg","注册成功");
		return map;
	}


	@Override
	@Transactional
	public Map weixinQuickRegister(String encrypData, String iv, Map<String, Object> userInfo, String cacheKey, Long memberId ) {

		AssertExt.notNull(encrypData, "encrypData为空");
		AssertExt.notNull(iv, "iv为空");
		AssertExt.notNull(cacheKey, "cacheKey为空");
		AssertExt.isFalse(userInfo == null, "userInfo为空");
//		AssertExt.notNull(memberId, "memberId为空");

		Map<String, Object> map = new HashMap<>();
		JSONObject jsonObject = JSONObject.parseObject(redisTemplate.opsForValue().get(String.format("wechat:%s", cacheKey)).toString());
		String openId = jsonObject.getString("openid");
		String sessionkey = jsonObject.getString("session_key");
		String unionId = jsonObject.getString("unionid");

		String data = null;
		try {
			data = descrptPhone(sessionkey, iv, encrypData);
		} catch (Exception e) {
			map.put("code","1");
			map.put("mag","解密失败");
			return map;
		}

		if(data != null){
			//获取手机号码等信息
			JSONObject userInfoJSON = JSONObject.parseObject(data);
			String phone = userInfoJSON.get("phoneNumber").toString();
			LocalDateTime now = LocalDateTime.now();

			//查询account是否存在
			QueryWrapper<Account> wrapper2 = new QueryWrapper<>();
			wrapper2.eq("account", phone);
			Account account2 = accountMapper.selectOne(wrapper2);
			Long accountId = 0l;

			if(account2 != null){
				//查询member
				QueryWrapper<MemberInfo> wrapper3 = new QueryWrapper<>();
				wrapper3.eq("account_id", account2.getId());
				MemberInfo memberInfo = memberInfoMapper.selectOne(wrapper3);
				accountId = account2.getId();

				if(memberInfo == null){
					//判断是不是后台下单过的
					getBackEndOrderUser(phone, userInfo, account2, map, memberId);
				}else {
					//'更新member
					UpdateWrapper<MemberInfo> updateWrapper = new UpdateWrapper<>();
					updateWrapper.eq("account_id", memberInfo.getAccountId());
					updateWrapper.set("nick_name", userInfo.get("nickName").toString());
					updateWrapper.set("img", userInfo.get("avatarUrl").toString());
					updateWrapper.set("sex", Integer.valueOf(userInfo.get("gender").toString()));
					memberInfoMapper.update(memberInfo, updateWrapper);
					map.put("memberInfo", memberInfoMapper.selectOne(wrapper3));
				}
			}else {
				//account数据不存在, 没有就生存account
				Account account = getAccountByPhone(phone);

				if(account == null){
					account = new Account();
					account.setAccount(phone);
					account.setPassword("");
					account.setSalt("");
					account.setStatus(1);
					account.setCreateTime(new Date());
					account.setUpdateTime(new Date());
					accountMapper.insert(account);

				}else {
					UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
					updateWrapper.eq("id", account.getId());
					updateWrapper.set("account", phone);
					updateWrapper.set("status", 1);
					updateWrapper.set("create_time", now);
					updateWrapper.set("update_time", now);
					accountMapper.update(account, updateWrapper);
				}
				accountId = account.getId();

				//判断是不是后台下单过的
				getBackEndOrderUser(phone, userInfo, account, map, memberId);
			}

			//判断有没uid数据
			if(!StringUtils.isBlank(unionId)) {
				QueryWrapper<AccountBind> wrapper = new QueryWrapper<>();
				wrapper.eq("bind_account", unionId);
				wrapper.eq("type", AccountBind.AccountBindType.UNIONID.name());
				AccountBind accountBind = accountBindMapper.selectOne(wrapper);

				if (accountBind == null) {
					//插入uid数据
					AccountBind accountBind2 = new AccountBind();
					accountBind2.setAccountId(accountId);
					accountBind2.setBindAccount(unionId);
					accountBind2.setType(AccountBind.AccountBindType.UNIONID.name());
					accountBind2.setStatus(1);
					accountBind2.setCreateTime(now);
					accountBind2.setUpdateTime(now);
					accountBindMapper.insert(accountBind2);
				}
			}

			//如果查询的openid数据不存在
			QueryWrapper<AccountBind> wrapper3 = new QueryWrapper<>();
			wrapper3.eq("bind_account", openId);
			wrapper3.eq("type", AccountBind.AccountBindType.PROGRAM_OPENID.name());
			AccountBind accountBind3 = accountBindMapper.selectOne(wrapper3);
			if(accountBind3 == null){
				//插入一条有openid的数据
				AccountBind accountBind1 = new AccountBind();
				accountBind1.setAccountId(accountId);
				accountBind1.setBindAccount(openId);
				accountBind1.setType(AccountBind.AccountBindType.PROGRAM_OPENID.name());
				accountBind1.setStatus(1);
				accountBind1.setCreateTime(now);
				accountBind1.setUpdateTime(now);
				accountBindMapper.insert(accountBind1);
			}
			map.put("code","0");
			map.put("msg","登陆成功");
		}
		return map;
	}


	@Override
	@Transactional
	public void getPublicUserInfo(String code, String cacheKey) {
		AssertExt.notBlank("code", "no code");
		AssertExt.notBlank("cacheKey", "no cacheKey");

		LocalDateTime now = LocalDateTime.now();

		//获取token
		String openId = this.getOpenid(code);
		AssertExt.notBlank(openId, "公众号授权失败");
		String unionId = this.getunionid(openId);


		QueryWrapper<AccountBind> wrapper3 = new QueryWrapper<>();
		wrapper3.eq("bind_account", openId);
		wrapper3.eq("type", AccountBind.AccountBindType.PUBLIC_OPENID.name());
		AccountBind accountBind3 = accountBindMapper.selectOne(wrapper3);

		if (accountBind3 == null) {
			redisTemplate.opsForValue().set(String.format("public:%s", cacheKey), "wechat", 1, TimeUnit.MINUTES);
			return;
		} else {
			//判断unionid数据有没有
			QueryWrapper<AccountBind> wrapper4 = new QueryWrapper<>();
			wrapper4.eq("type", AccountBind.AccountBindType.UNIONID.name());
			wrapper4.eq("bind_account", unionId);
			AccountBind accountBind4 = accountBindMapper.selectOne(wrapper4);
			if (accountBind4 == null) {
				//插入unionid数据
				AccountBind accountBind = new AccountBind();
				accountBind.setAccountId(accountBind3.getAccountId());
				accountBind.setBindAccount(unionId);
				accountBind.setType(AccountBind.AccountBindType.UNIONID.name());
				accountBind.setStatus(1);
				accountBind.setCreateTime(now);
				accountBind.setUpdateTime(now);
				accountBindMapper.insert(accountBind);
			}
			redisTemplate.opsForValue().set(String.format("public:%s", cacheKey), "wechat", 1, TimeUnit.MINUTES);
		}
	}

	/**
	 * 获取公众号的access_token
	 * @return
	 */
	private String getWechatAccessToken(){
		final String redisKey = String.format("wechat:access_token:%s", this.publicAppId);
		Object accessToken = this.redisTemplate.opsForValue().get(redisKey);

		if(accessToken != null) {
			log.debug("get access token from cache ");
			return accessToken.toString();
		}
		String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
				this.publicAppId,
				this.publicSECRET);

		JSONObject jsonObject = JSON.parseObject(HttpRequest.get(url.trim()).body());
		this.redisTemplate.opsForValue().set(redisKey, jsonObject.getString("access_token"), jsonObject.getLong("expires_in") - 60L, TimeUnit.SECONDS);
		return jsonObject.getString("access_token");
	}

	/**
	 * 获取用户的openid
	 *
	 * @param code
	 * @return
	 */
	private String getOpenid(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + publicAppId + "&secret=" + publicSECRET + "&code=" + code + "&grant_type=authorization_code";
		HttpRequest httpRequest = HttpRequest.get(url.trim());
		JSONObject jsonObject = JSON.parseObject(httpRequest.body());
		log.debug("oauth2 response {}", jsonObject.toJSONString());
		AssertExt.isTrue(jsonObject.getString("errmsg") == null, "公众号授权失败");
		return jsonObject.getString("openid");
	}


	/**
	 * 获取用户的unionid
	 *
	 * @param openId
	 * @return
	 */
	private String getunionid(String openId) {
		String infoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + this.getWechatAccessToken() + "&openid=" + openId + "&lang=zh_CN";

		HttpRequest httpRequest = HttpRequest.get(infoUrl.trim());
		JSONObject jsonObject = JSON.parseObject(httpRequest.body());
		String unionId = jsonObject.getString("unionid");
		log.debug("unionid response {}", jsonObject.toJSONString());
		AssertExt.isTrue(jsonObject.getString("errmsg") == null, "获取用户的unionid失败");
		return unionId;
	}


	@Override
	public Map getPhoneCode(String phone) {

		Map<String, Object> map = new HashMap<>();

		// 设置超时时间-可自行调整
		String defaultConnectTimeout  = "sun.net.client.defaultConnectTimeout";
		String defaultReadTimeout = "sun.net.client.defaultReadTimeout";
		String Timeout = "10000";

		// 初始化ascClient需要的几个参数
		String product = "Dysmsapi";// 短信API产品名称（短信产品名固定，无需修改）
		String domain = "dysmsapi.aliyuncs.com";// 短信API产品域名（接口地址固定，无需修改）

		// 替换成你的AK (产品密)
//		String accessKeyId = "";// 你的accessKeyId,填你自己的 上文配置所得  自行配置
//		String accessKeySecret = "";// 你的accessKeySecret,填你自己的 上文配置所得 自行配置

		// 必填:短信签名-可在短信控制台中找到
		String SignName = "志盛云创"; // 阿里云配置你自己的短信签名填入
		// 必填:短信模板-可在短信控制台中找到
//		String TemplateCode = ""; // 阿里云配置你自己的短信模板填入


		// 设置超时时间-可自行调整
		System.setProperty(defaultConnectTimeout, Timeout);
		System.setProperty(defaultReadTimeout, Timeout);

		// 初始化ascClient,暂时不支持多region
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		} catch (ClientException e1) {
			e1.printStackTrace();
		}

		//获取验证码
		String code = vcode(phone);

		IAcsClient acsClient = new DefaultAcsClient(profile);
		// 组装请求对象
		SendSmsRequest request = new SendSmsRequest();
		// 使用post提交
		request.setMethod(MethodType.POST);
		// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
		request.setPhoneNumbers(phone);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(SignName);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(TemplateCode);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		request.setTemplateParam("{ \"code\":\""+code+"\"}");
		// 可选-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");
		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		request.setOutId("yourOutId");
		// 请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
			if (sendSmsResponse.getCode() != null
					&& sendSmsResponse.getCode().equals("OK")) {
				// 请求成功
				System.out.println("获取验证码成功！！！");
			} else {
				//如果验证码出错，会输出错误码告诉你具体原因
				System.out.println(sendSmsResponse.getCode());
				System.out.println("获取验证码失败...");
			}
		} catch (ServerException e) {
			e.printStackTrace();
			map.put("code", 1);
			map.put("msg", "由于系统维护，暂时无法注册！！！");
			return map;
		} catch (ClientException e) {
			e.printStackTrace();
			map.put("code", 1);
			map.put("msg", "由于系统维护，暂时无法注册！！！");
			return map;
		}
		map.put("code", 0);
		map.put("msg", "验证码获取成功");
		map.put("phoneCode",code);
		return map;
	}


	/**
	 * 生成6位随机数验证码
	 * @return
	 */
	public String vcode(String phone){
		String vcode = RandomStringUtils.random(6, false, true);
		//放入缓存，时间5分钟，key为:code + phone
		redisTemplate.opsForValue().set("code"+ phone , vcode,5, TimeUnit.MINUTES);
		return vcode;
	}



	/**
	 * 判断有没有openid的account
	 */
	public Account getAccountByPhone(String phone){
		Account accountDB = accountMapper.selectOne(new QueryWrapper<Account>().eq("account", phone));
		return accountDB;
	}

	/**
	 * 获取登陆人的openId
	 */
//	public String getLoginOpenIdorUnionId(Long accountId, String type){
//
//		//拿出openId
//		Account account = accountMapper.selectById(accountId);
//		QueryWrapper<AccountBind> queryWrapper1= new QueryWrapper<>();
//		queryWrapper1.eq("account_id", account.getId());
//		queryWrapper1.eq("type", type);
//
//		AccountBind accountBind = accountBindMapper.selectOne(queryWrapper1);
//
//		return accountBind.getBindAccount();
//	}

	/**
	 * 判断有没后台下单的用户
	 */
	private Map getBackEndOrderUser(String phone, Map<String, Object> userInfo, Account account2, Map<String, Object> map, Long memberId){

		LocalDateTime now = LocalDateTime.now();

		//先判断有没有后台下单生成的member
		QueryWrapper<MemberInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("telephone", phone);
		MemberInfo memberInfo2 = memberInfoMapper.selectOne(queryWrapper);

		if(memberInfo2 == null){
			//新增member
			MemberInfo memberInfo1 = new MemberInfo();
			memberInfo1.setNickName(userInfo.get("nickName").toString());
			memberInfo1.setSex(Integer.valueOf(userInfo.get("gender").toString()));
			memberInfo1.setAccountId(account2.getId());
			memberInfo1.setImg(userInfo.get("avatarUrl").toString());
			memberInfo1.setBirthday(null);
			memberInfo1.setTelephone(phone);
			memberInfo1.setEmail(null);
			memberInfo1.setStatus("NORMAL");
			memberInfo1.setUpdateTime(now);
			memberInfo1.setUpdateUserId(0l);
			memberInfo1.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
			memberInfo1.setCreateTime(now);
			memberInfo1.setParentId(memberId == null ? 0l : memberId);
			memberInfoMapper.insert(memberInfo1);
			map.put("memberInfo", memberInfo1);
		}else {
			//更新
			UpdateWrapper<MemberInfo> wrapper = new UpdateWrapper<>();
			wrapper.set("nick_name", userInfo.get("nickName").toString());
			wrapper.set("sex", Integer.valueOf(userInfo.get("gender").toString()));
			wrapper.set("account_id", account2.getId());
			wrapper.set("img", userInfo.get("avatarUrl").toString());
			wrapper.set("birthday", null);
			wrapper.set("telephone", phone);
			wrapper.set("status", "NORMAL");
			wrapper.set("update_time", now);
			wrapper.set("update_user_id", 0l);
			wrapper.set("is_del", CommonConstant.IsDel.IS_NOT_DEL);
			wrapper.set("create_time", now);
			wrapper.set("parent_id", memberId == null ? 0l : memberId);
			memberInfoMapper.update(memberInfo2, wrapper);
			map.put("memberInfo", memberInfoMapper.selectOne(queryWrapper));
		}

		return map;
	}


	/**
	 * 获取门店和类型
	 * @param addressId
	 * @param map
	 * @return
	 */
	public Map getAddressStore(Long addressId, Map<String, Object> map){

		List<MemberAddressStoreVo> memberAddressStoreList = memberAddressStoreMapper.getMemberAddressStoreAndSoreType(addressId);
		Map<String, Object> store = new HashMap<>();
		Map<String, Object> gas = null;
		Map<String, Object> market = null;
		Map<String, Object> fastfood = null;
		Map<String, Object> water = null;
		Map<String, Object> marketAndFastfood = null;

		if(memberAddressStoreList != null){
			for (MemberAddressStoreVo memberAddressStore: memberAddressStoreList){
				if(memberAddressStore.getStoreTypeId().longValue() == 0){
					gas = new HashMap<>();
					gas.put("id", memberAddressStore.getStoreId());
					gas.put("storeName", memberAddressStore.getStoreName());
					gas.put("phone", memberAddressStore.getPhone());
					gas.put("address",memberAddressStore.getAddress());
				}else if (memberAddressStore.getStoreTypeId().longValue() == 3){
					market = new HashMap<>();
					market.put("id", memberAddressStore.getStoreId());
					market.put("storeName", memberAddressStore.getStoreName());
					market.put("phone", memberAddressStore.getPhone());
					market.put("address",memberAddressStore.getAddress());
				}else if(memberAddressStore.getStoreTypeId() == 2){
					fastfood = new HashMap<>();
					fastfood.put("id", memberAddressStore.getStoreId());
					fastfood.put("storeName", memberAddressStore.getStoreName());
					fastfood.put("phone", memberAddressStore.getPhone());
					fastfood.put("address",memberAddressStore.getAddress());
				}else if(memberAddressStore.getStoreTypeId() == 1){
					water = new HashMap<>();
					water.put("id", memberAddressStore.getStoreId());
					water.put("storeName", memberAddressStore.getStoreName());
					water.put("phone", memberAddressStore.getPhone());
					water.put("address",memberAddressStore.getAddress());
				}else if(memberAddressStore.getStoreTypeId() == 4){
					marketAndFastfood = new HashMap<>();
					marketAndFastfood.put("id", memberAddressStore.getStoreId());
					marketAndFastfood.put("storeName", memberAddressStore.getStoreName());
					marketAndFastfood.put("phone", memberAddressStore.getPhone());
					marketAndFastfood.put("address",memberAddressStore.getAddress());
				}
			}
		}

		if(gas != null)store.put("gas", gas);
		if(market != null)store.put("market", market);
		if(fastfood != null)store.put("fastfood", fastfood);
		if(water != null)store.put("water", water);
		if(marketAndFastfood != null)store.put("marketAndFastfood", marketAndFastfood);

		if(!store.isEmpty())map.put("store", store);
		return map;
	}
}
