package com.zsyc.account.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.account.entity.Account;
import com.zsyc.account.vo.AccountVo;
import com.zsyc.admin.entity.User;
import com.zsyc.member.entity.MemberInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by lcs on 2018-12-27.
 */
public interface LoginService {

	/**
	 * 注册
	 * @param account
	 * @return
	 */
	Account registry(AccountVo account);

	/**
	 * 登录
	 * @param account
	 * @return
	 */
	Account login(Account account);

	/**
	 * 微信登陆
	 * @param code
	 * @return
	 */
	Account weixinLogin2(String code, String cacheKey);

	/**
	 * 获取一个account
	 * @param account
	 * @return
	 */
	Account get(String account);

	/**
	 * demo测试用
	 * @param num
	 * @return
	 */
	List<Account> testApi(Integer num);


	/**
	 * 微信授权
	 */
	Object weixinAccept(String encryptedData, String iv, String cacheKey);

	/**
	 * 微信注册
	 */
	Map weixinRegister(String phone, Map<String, Object> userInfo, String phoneCode, String cacheKey, Long memberId );

	/**
	 * 微信一键注册
	 */
	Map weixinQuickRegister(String decryptData,  String iv,  Map<String, Object> userInfo, String cacheKey , Long memberId );

	/**
	 * 获取公众号用户信息
	 * @return
	 */
	void getPublicUserInfo(String code,String cacheKey);


	/**
	 * 获取短信验证码
	 * @param phone
	 * @return
	 */
	Map getPhoneCode(String phone);




}
