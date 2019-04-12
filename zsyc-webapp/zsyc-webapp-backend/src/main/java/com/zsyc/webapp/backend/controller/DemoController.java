package com.zsyc.webapp.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lcs on 2018/9/27.
 */
@Controller
public class DemoController {


	@RequestMapping("add/{name}")
	public Object add(@PathVariable("name")String name){
		//return this.demoService.add(Demo.builder().createTime(new Date()).name(name).build());
		return null;
	}

	@RequestMapping("get/{page}/{pageSize}")
	public Object get(@PathVariable("page") int page,@PathVariable("pageSize") int pageSize){
		return null;
	}
}
