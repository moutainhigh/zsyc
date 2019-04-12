package com.zsyc.fs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by lcs on 2019-02-01.
 */
@SpringBootApplication(scanBasePackages = {"com.zsyc"})
public class ZSYCFSApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZSYCFSApplication.class, args);
	}
}
