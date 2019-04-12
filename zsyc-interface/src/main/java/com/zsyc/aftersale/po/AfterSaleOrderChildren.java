package com.zsyc.aftersale.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AfterSaleOrderChildren extends BaseBean {
    private Long id;
    private LocalDateTime createTime;
    /**
     * 商品编码
     */
    private String sku;

    /**
     * 现有的数量
     */
    private Integer numIng;

    /**
     * 原始数量
     */
    private Integer num;
    private List<AfterSaleOrderChildren> quick=new ArrayList<>();




}
