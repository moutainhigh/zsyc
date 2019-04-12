package com.zsyc.webapp.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by lcs on 2019-01-13.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZSYCUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String username;

	private String password;

	private Long accountId;

	private Collection<SimpleGrantedAuthority> authorities;


	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
