package com.zsyc.admin.po;

import lombok.Data;

@Data
public class RoleStorePo {

    /**
     * 用户
     */
    private Long userId;

    /**
     * 地区编码
     */
    private Integer adcode;

    private Integer currentPage;

    private Integer pageSize;

    private int storeTypeId;
}
