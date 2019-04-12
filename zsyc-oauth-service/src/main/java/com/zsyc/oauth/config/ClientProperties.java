package com.zsyc.oauth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lcs on 2019-01-13.
 */
@ConfigurationProperties(prefix = "zsyc.oauth2")
@Component
@Data
public class ClientProperties {

	private final Map<String, BaseClientDetails> clients = new HashMap<>();
}
