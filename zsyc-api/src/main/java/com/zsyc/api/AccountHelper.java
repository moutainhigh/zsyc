package com.zsyc.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zsyc.admin.entity.User;
import com.zsyc.admin.service.AdminService;
import com.zsyc.member.entity.MemberInfo;
import com.zsyc.member.service.MemberInfoService;
import com.zsyc.webapp.oauth.ZSYCUserDetails;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

/**
 * Created by lcs on 2019-01-19.
 */
@Component
public class AccountHelper {
	@Reference
	private AdminService adminService;
	@Reference
	private MemberInfoService memberInfoService;

	private static Long getAccountId() {
		if (SecurityContextHolder.getContext() == null) {
			return null;
		}

		if (SecurityContextHolder.getContext()
				.getAuthentication() == null) {
			return null;
		}
		if( !(SecurityContextHolder.getContext()
				.getAuthentication() instanceof OAuth2Authentication)){
			return null;
		}
		OAuth2Authentication auth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
		ZSYCUserDetails userDetails = (ZSYCUserDetails) auth2Authentication.getUserAuthentication().getDetails();
		return userDetails.getAccountId();
	}

	/**
	 * 获取当前登录人（管理后台）
	 * @return
	 */
	public User getUser(){
		return this.adminService.getUserByAccount(getAccountId());
	}

	/**
	 * 获取客户（小程序）
	 * @return
	 */
	public MemberInfo getMember(){
		return this.memberInfoService.getmemberByAccount(getAccountId());
	}
}
