package com.zsyc.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by lcs on 2019-01-09.
 */
@SpringBootApplication(scanBasePackages = {"com.zsyc"})
public class ZSYCApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZSYCApiApplication.class, args);
    }
}
