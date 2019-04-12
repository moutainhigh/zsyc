package com.zsyc.webapp.h5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lcs on 2018/9/27.
 */
@SpringBootApplication
@RestController
public class H5WebApplication {
	public static void main(String[] args) {
		SpringApplication.run(H5WebApplication.class, args);
	}
	@GetMapping("hi")
	public String hi(){
		return "hi";}
}
