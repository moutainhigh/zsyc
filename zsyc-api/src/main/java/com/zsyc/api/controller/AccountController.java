package com.zsyc.api.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sun.javafx.collections.MappingChange;
import com.zsyc.account.entity.Account;
import com.zsyc.account.po.WeiXinAcceptPo;
import com.zsyc.account.po.WeiXinQuickRegisterPo;
import com.zsyc.account.po.WeiXinRegisterPo;
import com.zsyc.account.service.LoginService;
import com.zsyc.account.vo.AccountVo;
import com.zsyc.admin.entity.Role;
import com.zsyc.admin.entity.User;
import com.zsyc.admin.po.UserPo;
import com.zsyc.api.AccountHelper;
import com.zsyc.member.entity.MemberInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lcs on 2019-01-09.
 */
@RestController
@RequestMapping("api/weChat/account")
@Api
public class AccountController {

	@Reference
	private LoginService loginService;

	@Autowired
	private AccountHelper accountHelper;

	@Value("${zsyc.publicAppId}")
	private String publicAppId;//公众号appid

//	@ApiOperation("用户登录")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "account", value = "帐号", defaultValue = "abs", allowableValues = "rangs[1-3]", example = "example", type = "type"),
//			@ApiImplicitParam(name = "password", value = "密码")
//	})
//	@ApiResponse(code = 200, message = "", response = Account.class)
//	@PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
//	public Account login(@ApiIgnore Account account) {
//		return this.loginService.login(account);
//	}
//
//	@ApiOperation("这个测试接口")
//	@Authorization("authorization")
//	@ApiImplicitParam(name = "query",value = "一个数据")
//	@PostMapping(value = "testApi",produces = MediaType.APPLICATION_JSON_VALUE)
//	public Object login(Integer num) {
//		return this.loginService.testApi(num);
//	}


	@ApiOperation("小程序登陆---测试---接口")
	@GetMapping(value = "weixinLoginTest", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(name = "code", value = "微信code", required = true, dataType = "string")
	public Account weixinLoginTest(String code) {
        return loginService.weixinLogin2(code, "sss");
	}

	@ApiOperation("小程序授权接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cacheKey", value = "缓存key", required = true, dataType = "string"),
			@ApiImplicitParam(name = "encryptedData", value = "加密数据", required = true, dataType = "string"),
			@ApiImplicitParam(name = "iv", value = "偏移量", required = true, dataType = "string")})
	@ApiResponses(value = {
			@ApiResponse(code = 1, message = "授权成功，跳到公众号"),
			@ApiResponse(code = 2, message = "解密失败")})
	@PostMapping(value = "weixinAccept",produces = MediaType.APPLICATION_JSON_VALUE)
	public Object weixinAccept(@RequestBody WeiXinAcceptPo weiXinAcceptPo) {
        return  loginService.weixinAccept(weiXinAcceptPo.getEncryptedData(), weiXinAcceptPo.getIv(), weiXinAcceptPo.getCacheKey());
	}

	@ApiOperation("获取短信验证码接口")
	@GetMapping(value = "getPhoneCode",produces = MediaType.APPLICATION_JSON_VALUE)
	public Map getPhoneCode(String phone){
		return loginService.getPhoneCode(phone);
	}


	@ApiOperation("微信注册接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cacheKey", value = "缓存key", required = true, dataType = "string"),
			@ApiImplicitParam(name = "phone", value = "手机号码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "phoneCode", value = "手机验证码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "memberId", value = "父类id", required = true, dataType = "long"),
			@ApiImplicitParam(name = "userInfo", value = "用户信息", required = true)})
	@ApiResponses(value = {
			@ApiResponse(code = 1, message = "短信验证码错误"),
			@ApiResponse(code = 0, message = "注册成功")})
	@PostMapping(value = "weixinRegister",produces = MediaType.APPLICATION_JSON_VALUE)
	public Map weixinRegister(@RequestBody WeiXinRegisterPo weiXinRegisterPo) {
		return  loginService.weixinRegister(weiXinRegisterPo.getPhone(), weiXinRegisterPo.getUserInfo(), weiXinRegisterPo.getPhoneCode(),
				weiXinRegisterPo.getCacheKey(), weiXinRegisterPo.getMemberId());
	}

	@ApiOperation("小程序快速登陆接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cacheKey", value = "缓存key", required = true, dataType = "string"),
			@ApiImplicitParam(name = "encryptedData", value = "加密手机号数据", required = true, dataType = "string"),
			@ApiImplicitParam(name = "iv", value = "偏移量", required = true, dataType = "string"),
			@ApiImplicitParam(name = "memberId", value = "父类id", required = true, dataType = "long"),
			@ApiImplicitParam(name = "userInfo", value = "用户信息", required = true)})
	@ApiResponses(value = {
			@ApiResponse(code = 0, message = "登陆成功"),
			@ApiResponse(code = 1, message = "解密失败")})
	@PostMapping(value = "weixinQuickRegister",produces = MediaType.APPLICATION_JSON_VALUE)
	public Map weixinQuickRegister(@RequestBody WeiXinQuickRegisterPo weiXinQuickRegisterPo) {
		return  loginService.weixinQuickRegister(weiXinQuickRegisterPo.getDecryptData(), weiXinQuickRegisterPo.getIv(),
				weiXinQuickRegisterPo.getUserInfo(), weiXinQuickRegisterPo.getCacheKey(), weiXinQuickRegisterPo.getMemberId());
	}


	@ApiOperation("公众号获取用户信息接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "code", value = "微信code", required = true, dataType = "string"),
			@ApiImplicitParam(name = "cacheKey", value = "cacheKey", required = true, dataType = "string")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "公众号授权成功")})
	@GetMapping(value = "getPublicUserInfo",produces = MediaType.APPLICATION_JSON_VALUE)
	public String getPublicUserInfo(String code,String cacheKey) {
		loginService.getPublicUserInfo(code, cacheKey);
		return "公众号授权...";
	}


	@ApiOperation("公众号获取用户信息---测试---接口")
	@ApiImplicitParam(name = "code", value = "微信code", required = true, dataType = "string")
	@GetMapping(value = "getPublicUserInfo2",produces = MediaType.APPLICATION_JSON_VALUE)
	public String getPublicUserInfo2(String code) {
		return code;
	}

	@ApiOperation("公众号重定向请求授权")
	@GetMapping(value = "weixinRedirectAccept",produces = MediaType.APPLICATION_JSON_VALUE)
	public String  weixinRedirectAccept(HttpServletResponse httpServletResponse,String cacheKey) {

		/**
		 * &connect_redirect=1不加会出现微信重定向两次请求的情况，该参数在公众平台文档没有出现，狗比微信
		 * //snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo 需要点击授权
		 * **/

		String redirect_uri = "http://jessicababy.cn/api/weChat/account/getPublicUserInfo?cacheKey=" + cacheKey;

		try {
			String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+publicAppId
					+"&redirect_uri="+java.net.URLEncoder.encode(redirect_uri, "utf-8")
					+"&response_type=code"
					+"&scope=snsapi_base"
					+"&state=STATE&connect_redirect=1#wechat_redirect";

			httpServletResponse.setStatus(301);
			httpServletResponse.sendRedirect(url);
		} catch (IOException e) {
			return "请求发生错误";
		}

		return "请求成功";
	}

}
