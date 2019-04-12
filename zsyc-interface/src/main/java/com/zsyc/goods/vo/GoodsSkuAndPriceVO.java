package com.zsyc.goods.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class GoodsSkuAndPriceVO implements Serializable {

    /**
     * 商品sku码 唯一商品
     */
    private String sku;

    /**
     * 商品价格
     */
    private BigDecimal price;
}
