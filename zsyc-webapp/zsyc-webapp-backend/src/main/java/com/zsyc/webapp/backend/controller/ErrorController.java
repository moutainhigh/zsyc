package com.zsyc.webapp.backend.controller;

import com.zsyc.webapp.config.ResponseJson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by lcs on 2018/11/17.
 */

@ControllerAdvice(basePackages ="com.zsyc")
public class ErrorController {

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public Object handleControllerException(IllegalArgumentException e) {
		return new ResponseEntity<>(ResponseJson.builder().errorCode("600").errorMessage(e.getMessage()).build(), HttpStatus.OK);
	}
}
