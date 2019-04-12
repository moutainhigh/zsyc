package com.zsyc.admin.po;

import lombok.Data;

@Data
public class UserPo {

    private String accountPassword;

    private String userName;

    private Long userId;

    private String telphone;

    private String email;

    private String name;

    private String remark;

    private Long accountId;

    private String status;

    private Integer currentPage;

    private Integer pageSize;
}
