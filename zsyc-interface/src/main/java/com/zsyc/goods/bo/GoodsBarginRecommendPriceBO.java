package com.zsyc.goods.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class GoodsBarginRecommendPriceBO implements Serializable {

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
    private Integer price;

    /**
     * vip价格
     */
    private Integer vipPrice;

    /**
     * 成本价格
     */
    private Integer costPrice;
}
