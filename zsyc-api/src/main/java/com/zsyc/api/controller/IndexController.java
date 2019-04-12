package com.zsyc.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lcs on 2019-01-13.
 */
@RestController
public class IndexController {
	@GetMapping({"index", "", "/"})
	public Object index(){
		return String.format("ZSYC API IS RUNNING");
	}
}