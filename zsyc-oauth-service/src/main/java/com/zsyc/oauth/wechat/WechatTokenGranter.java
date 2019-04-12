package com.zsyc.oauth.wechat;

import com.zsyc.oauth.config.ZSYCTokenGranter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.stereotype.Component;

/**
 * Created by lcs on 2019-04-03.
 */
@Component
@Lazy
public class WechatTokenGranter extends ZSYCTokenGranter {
	private final static String GRANT_TYPE = "wechat_token";

	@Autowired
	private AuthenticationManager authenticationManager;

	public WechatTokenGranter() {
		super(GRANT_TYPE);
	}

	@Override
	public OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
		String code = tokenRequest.getRequestParameters().get("code");
		String cacheKey = tokenRequest.getRequestParameters().get("cache_key");

		Authentication authenticate = authenticationManager.authenticate(new WechatAuthenticationToken(code, cacheKey, "mini"));

		if (authenticate == null || !authenticate.isAuthenticated()) {
			throw new InvalidGrantException("Could not authenticate user: " + code);
		}

		OAuth2Request storedOAuth2Request = super.auth2RequestFactory.createOAuth2Request(client, tokenRequest);
		return new OAuth2Authentication(storedOAuth2Request, authenticate);
	}

}
