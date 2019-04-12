package com.zsyc.aftersale.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AterSaleOrderVo extends BaseBean {
    private Long id;
    private LocalDateTime createTime;
    private String img;
    private String goodsName;
    private Long price;
    private Long num;
    private Long priceAll;
    private String status;
    private LocalDateTime bookStarTime;



}
