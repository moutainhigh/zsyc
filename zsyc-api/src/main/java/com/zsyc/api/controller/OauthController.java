package com.zsyc.api.controller;

import io.swagger.annotations.*;
import lombok.Data;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lcs on 2019-01-30.
 * api doc for TokenEndpoint
 * @see org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
 */
@Api
@RestController
public class OauthController {
	@ApiOperation("oauth2.0 , get token , 这个接口还要 http basic 授权")
	@PostMapping("oauth/token")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "grant_type", value = "grant_type", required = true, example = "account_token/refresh_token/wechat_token"),
			@ApiImplicitParam(name = "【wechat_token】code", value = "微信授权code", required = true, example = ""),
			@ApiImplicitParam(name = "【wechat_token】cacheKey", value = "cacheKey", required = false, example = ""),
			@ApiImplicitParam(name = "【account_token】account", value = "帐号", required = true, example = "admin"),
			@ApiImplicitParam(name = "【account_token】password", value = "密码", required = true, example = "zsyc2018"),
			@ApiImplicitParam(name = "【refresh_token】refresh_token", value = "refresh_token", required = true, example = "11e7b669-6e81-4e0b-90e6-aedaed9cb648")
	})
	public Res token(){
		throw new NotImplementedException("");
	}

	@Data
	@ApiModel
	private static class Res {
		private String access_token;
		private String token_type;
		private String refresh_token;
		private Long expires_in;
		private String scope;
	}
}
