package com.zsyc.oauth;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zsyc.account.entity.Account;
import com.zsyc.account.service.LoginService;
import com.zsyc.common.AssertExt;
import com.zsyc.webapp.oauth.ZSYCUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Created by lcs on 2019-01-13.
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	@Reference
	private LoginService loginService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = this.loginService.get(username);
		AssertExt.notNull(account, "account[%s] not exist");
		return ZSYCUserDetails.builder()
				.accountId(account.getId())
				.username(username)
				.authorities(Collections.singletonList(new SimpleGrantedAuthority("USER")))
				.build();

	}
}
