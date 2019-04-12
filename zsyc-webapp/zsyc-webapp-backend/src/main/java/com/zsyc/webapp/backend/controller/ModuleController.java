package com.zsyc.webapp.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lcs on 2018/9/27.
 */
@RequestMapping("page")
@Controller
public class ModuleController {
	@GetMapping("{module}/{page}")
	public String page(@PathVariable("module") String module, @PathVariable("page") String page) {
		return String.format("%s/%s", module, page);
	}
}
