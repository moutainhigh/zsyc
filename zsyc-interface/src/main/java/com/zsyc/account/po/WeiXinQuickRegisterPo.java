package com.zsyc.account.po;

import lombok.Data;

import java.util.Map;

@Data
public class WeiXinQuickRegisterPo {

    private String decryptData;

    private String iv;

    private Map<String, Object> userInfo;

    private String cacheKey;

    private Long memberId;
}
