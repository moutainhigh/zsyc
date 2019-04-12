package com.zsyc.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by lcs on 2019-01-02.
 */
@SpringBootApplication(scanBasePackages = "com.zsyc")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableAuthorizationServer
@RestController
@CrossOrigin(origins = "*", allowedHeaders = {"authorization", "content-type"}, methods = {GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE})
public class ZSYCOAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZSYCOAuthServiceApplication.class, args);
	}

	@Bean
	public AuthenticationManager getAuthenticationManagerBean(AuthenticationProvider[] authenticationProviders) {
		return new ProviderManager(Arrays.asList(authenticationProviders));
	}
}
