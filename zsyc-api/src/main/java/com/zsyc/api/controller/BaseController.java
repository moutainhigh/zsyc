package com.zsyc.api.controller;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lcs on 2019-01-19.
 */
public class BaseController {

	protected String getIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip != null && ip.contains(",")) {
			ip = ip.substring(0, ip.indexOf(","));
		}
		return ip;
	}
}
