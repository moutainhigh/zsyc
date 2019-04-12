package com.zsyc.oauth.wechat;

import lombok.Data;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Created by lcs on 2019-04-03.
 */
@Data
public class WechatAuthenticationToken extends AbstractAuthenticationToken {

	/**
	 * wechat code
	 * https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842
	 */
	private String code;
	private String cacheKey;

	private String type;
	private Object principal;

	public WechatAuthenticationToken(String code,String cacheKey,String type) {
		super(null);
		this.code = code;
		this.type = type;
		this.cacheKey = cacheKey;
		this.setAuthenticated(false);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}
}
