package com.zsyc.oauth.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.ParameterizedType;

/**
 * Created by lcs on 2019-01-13.
 */
public abstract class ZSYCAuthenticationProvider<T extends Authentication> implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserDetails userDetails = this.subAuthenticate((T) authentication);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails, userDetails.getAuthorities());
		usernamePasswordAuthenticationToken.setDetails(userDetails);
		return usernamePasswordAuthenticationToken;
	}

	public abstract UserDetails subAuthenticate(T authentication) throws AuthenticationException;

	@Override
	public boolean supports(Class<?> authentication) {
		Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return clazz.isAssignableFrom(authentication);
	}
}
