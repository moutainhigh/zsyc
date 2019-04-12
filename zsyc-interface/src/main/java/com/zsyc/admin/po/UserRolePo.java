package com.zsyc.admin.po;

import lombok.Data;

import java.util.List;

@Data
public class UserRolePo {

    private Long userId;

    private List<Long> roleIds;

    private Long roleId;

    private List<Long> permissionIds;

    private List<Long> storeIds;
}
