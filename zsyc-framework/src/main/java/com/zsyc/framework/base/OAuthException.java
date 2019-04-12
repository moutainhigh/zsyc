package com.zsyc.framework.base;

import lombok.Data;

@Data
public class OAuthException extends RuntimeException{
    private String code;
    private String cacheKey;

    public OAuthException(String message,String code,String cacheKey){
        super(message);
        this.code = code;
        this.cacheKey = cacheKey;
    }
}
