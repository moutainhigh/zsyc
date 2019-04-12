package com.zsyc.webapp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by lcs on 2018/11/17.
 */

@ControllerAdvice
@Slf4j
public class ErrorController {

	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public Object handleControllerException(RuntimeException e) {
		log.error("RuntimeException", e);
		return new ResponseEntity<>(ResponseJson.builder().errorCode("600").errorMessage(e.getMessage()).build(), HttpStatus.OK);
	}
}
