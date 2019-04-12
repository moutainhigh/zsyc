package com.zsyc.delivery.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliVeryStaffAo extends BaseBean {
    private String newMasterName;
    private String phone;
    private String papers;
    private Integer isLeader;
    private Long id;
    private LocalDateTime createTime;
}
