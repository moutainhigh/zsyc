package com.zsyc.aftersale.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AterSaleOrderPo extends BaseBean {
    private Long id;
    private LocalDateTime createTime;
    private String orderNo;
    private String consignee;
    private String consigneePhone;
    private List<AterSaleOrderVo> list;
}
