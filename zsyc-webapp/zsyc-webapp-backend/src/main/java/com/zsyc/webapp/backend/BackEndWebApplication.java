package com.zsyc.webapp.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lcs on 2018/9/27.
 */
@SpringBootApplication(scanBasePackages = "com.zsyc")
@RestController
public class BackEndWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackEndWebApplication.class, args);
	}
	@GetMapping("hi")
	public String hi(){
		return "hi";}
}
