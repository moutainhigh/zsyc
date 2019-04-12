package com.zsyc.order.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: zsyc-parent
 * @description: 库存Vo
 * @author: Mr.Ning
 * @create: 2019-03-08 09:42
 **/
@Data
public class StockVo implements Serializable {

    /**
     * 商品sku
     */
    private String sku;

    /**
     * 扣库存的数量
     */
    private Integer num;

    /**
     * 门店id
     */
    private Long storeId;
}
