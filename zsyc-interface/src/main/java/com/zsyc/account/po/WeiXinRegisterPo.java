package com.zsyc.account.po;

import lombok.Data;

import java.util.Map;

@Data
public class WeiXinRegisterPo {
    private String phone;

    private String phoneCode;

    private Map<String, Object> userInfo;

    private String cacheKey;

    private Long memberId;
}
