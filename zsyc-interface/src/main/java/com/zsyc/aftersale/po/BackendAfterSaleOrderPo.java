package com.zsyc.aftersale.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BackendAfterSaleOrderPo extends BaseBean {
    private Long id;
    private LocalDateTime createTime;
    private Long orderSubId;
    private String afterSaleOrderStatus;
    private String afterSaleNo;
    private String staffPhone;
    private String staffName;
    private String remark;
    private String sku;
    private Long original;
    private Long num;
    private BigDecimal refundAmount;
    private String goodsName;
    private String img;
    private String orderNo;
    private Long price;
    private List<BackendAfterSaleOrderPo> list=new ArrayList<>();

}
