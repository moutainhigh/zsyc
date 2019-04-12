package com.zsyc.api.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zsyc.admin.entity.Permission;
import com.zsyc.admin.entity.User;
import com.zsyc.admin.service.AdminService;
import com.zsyc.admin.service.PermissionService;
import com.zsyc.webapp.oauth.ZSYCUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

/**
 * Created by lcs on 2019-01-30.
 */
@Service
@Slf4j
public class ZSYCAccessDecisionManager implements AccessDecisionManager {

	@Value("${zsyc.oauth2.request.white-list}")
	private String[] whiteList;

	private AntPathMatcher antPathMatcher = new AntPathMatcher();
	@Reference
	private PermissionService permissionService;
	@Reference
	private AdminService adminService;

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		String uri = ((FilterInvocation) object).getHttpRequest().getRequestURI();

		if (this.isPass(uri)) {
			return;
		}

		if (!(authentication instanceof OAuth2Authentication)) {
			throw new AccessDeniedException("no OAuth2Authentication");
		}
		authentication = ((OAuth2Authentication) authentication).getUserAuthentication();

		log.info("check permission {},{}", authentication.getPrincipal(), uri);

		Permission permission = this.permissionService.getPermission(uri, Permission.PermissionType.API);
		if (permission == null) {
			log.debug("permission[{}],not exist in db ; pass", uri);
			return;
		}

		User user = this.adminService.getUserByAccount(((ZSYCUserDetails)authentication.getDetails()).getAccountId());
		boolean match = this.adminService.getPermissionByUserId(user.getId(), Permission.PermissionType.API).stream().anyMatch(permission1 -> permission1.getId().equals(permission.getId()));

		if(match) {
			log.debug("permission[{}],has match width user ; pass", uri);
			return;
		}
		throw new AccessDeniedException("no right");
	}

	/**
	 * 是否过滤权限判断
	 * @param uri
	 * @return
	 */
	private boolean isPass(String uri){
		for (String path:this.whiteList){
			if(this.antPathMatcher.match(path,uri))
				return true;
		}
		return false;
	}
	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
