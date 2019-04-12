package com.zsyc.webapp.config;

import lombok.Builder;
import lombok.Data;

/**
 * Created by lcs on 2018/11/17.
 */
@Data
@Builder
public class ResponseJson {
	private String errorCode;
	private String errorMessage;
	private Object bizContent;
}
