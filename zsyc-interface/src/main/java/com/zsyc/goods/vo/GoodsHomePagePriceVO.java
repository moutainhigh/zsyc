package com.zsyc.goods.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class GoodsHomePagePriceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品sku码 唯一商品
     */
    private String sku;

    /**
     * 商品spu码 同款商品
     */
    private String spu;

    /**
     * 1.普通商品，2.组合商品
     */
    private Integer goodsType;

    /**
     * 多张图片逗号分割
     */
    private String picture;

    /**
     * 商品类型
     */
    private Integer goodsStyle;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品详情展示类型
     */
    private Integer type;
}
