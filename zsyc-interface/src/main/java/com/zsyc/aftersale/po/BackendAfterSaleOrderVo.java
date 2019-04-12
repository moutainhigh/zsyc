package com.zsyc.aftersale.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BackendAfterSaleOrderVo extends BaseBean {
    private Long id;
    private LocalDateTime createTime;
    private String afterSaleNo;
    private String staffPhone;
    private String staffName;
    private String orderNo;
    private String status;
    private String phone;
    private String remark;
    private List<BackendAfterSaleOrderPo> list =new ArrayList<>();


}
