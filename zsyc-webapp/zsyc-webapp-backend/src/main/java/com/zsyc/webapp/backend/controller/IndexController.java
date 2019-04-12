package com.zsyc.webapp.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * Created by lcs on 2018/11/17.
 */
@Controller
public class IndexController  {

	@RequestMapping(value = {"","/","/index"})
	public String index(){
		return "redirect:/page/main/index";
	}

	@RequestMapping(value = {"getTime"})
	public Date getTime(String time){
//		Assert.hasText(time,"no time");
		if(!StringUtils.hasText(time)){
			throw new IllegalArgumentException("no time");
		}
		return new Date();
	}
}
