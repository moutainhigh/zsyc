package com.zsyc.account.po;

import lombok.Data;

@Data
public class WeiXinAcceptPo {

    private String encryptedData;

    private String iv;

    private String cacheKey;
}
