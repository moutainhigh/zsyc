package com.zsyc.oauth.wechat;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zsyc.account.entity.Account;
import com.zsyc.account.service.LoginService;
import com.zsyc.common.AssertExt;
import com.zsyc.framework.base.OAuthException;
import com.zsyc.member.entity.MemberInfo;
import com.zsyc.member.service.MemberInfoService;
import com.zsyc.oauth.account.AccountAuthenticationToken;
import com.zsyc.oauth.config.ZSYCAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

/**
 * Created by lcs on 2019-01-13.
 */
@Component
@Slf4j
public class WecahtAuthenticationProvider extends ZSYCAuthenticationProvider<WechatAuthenticationToken> {

	@Reference
	private LoginService loginService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public UserDetails subAuthenticate(WechatAuthenticationToken authentication) throws AuthenticationException {
		try {
			Account account = this.loginService.weixinLogin2(authentication.getCode(), authentication.getCacheKey());
			return this.userDetailsService.loadUserByUsername(account.getAccount());
		}catch (OAuthException e){
			log.error("subAuthenticate OAuthException", e);
			OAuth2AccessDeniedException exception = new OAuth2AccessDeniedException(e.getMessage()) {
				@Override
				public String getOAuth2ErrorCode() {
					return e.getCode();
				}
			};

			exception.addAdditionalInformation("cache_key", e.getCacheKey());
			throw exception;

		} catch (Exception e){
			log.error("subAuthenticate error", e);
			throw new OAuth2Exception(e.getMessage(), e);
		}
	}
}
