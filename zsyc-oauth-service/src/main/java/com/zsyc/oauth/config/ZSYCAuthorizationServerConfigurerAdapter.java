package com.zsyc.oauth.config;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

/**
 * Created by lcs on 2019-01-02.
 */
@Component
@Slf4j
public class ZSYCAuthorizationServerConfigurerAdapter extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Autowired
	private TokenGranter[] tokenGranters;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private ClientProperties clientProperties;


	private AuthorizationServerEndpointsConfigurer endpointsConfigurer;


	@PostConstruct
	public void init( ){
		initTokenGranters(endpointsConfigurer);
	}
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()")
				.passwordEncoder(new PasswordEncoder() {
					@Override
					public String encode(CharSequence charSequence) {
						return charSequence.toString();
					}

					@Override
					public boolean matches(CharSequence charSequence, String s) {
						return s.equals(charSequence);
					}
				})
				.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
		this.clientProperties.getClients().entrySet().stream().forEach(item->{
			val client = item.getValue();

			builder.withClient(client.getClientId())
					.secret(client.getClientSecret())
					.resourceIds(client.getResourceIds().toArray(new String[0]))
					.authorizedGrantTypes(client.getAuthorizedGrantTypes().toArray(new String[0]))
					.scopes(client.getScope().toArray(new String[0]))
					.accessTokenValiditySeconds(client.getAccessTokenValiditySeconds())
					.refreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds())
					.autoApprove(true);

		});
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(getTokenStore())
				.userDetailsService(this.userDetailsService)
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
		initTokenGranters(endpoints);
		this.endpointsConfigurer = endpoints;
	}

	private TokenStore getTokenStore(){
		RedisTokenStore tokenStore = new RedisTokenStore(this.redisConnectionFactory);
		tokenStore.setPrefix("spring_oauth2:");
		return tokenStore;
	}

	private void initTokenGranters(AuthorizationServerEndpointsConfigurer endpoints) {
		if (this.tokenGranters == null) return;
		List<TokenGranter> list = new ArrayList<>();
		list.add(endpoints.getTokenGranter());
		list.addAll(Arrays.asList(this.tokenGranters));
		endpoints.tokenGranter(new CompositeTokenGranter(list));
	}
}
