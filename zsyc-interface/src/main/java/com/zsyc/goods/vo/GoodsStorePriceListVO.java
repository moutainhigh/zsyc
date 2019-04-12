package com.zsyc.goods.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class GoodsStorePriceListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * vip价格
     */
    private BigDecimal vipPrice;

    /**
     * 成本价格
     */
    private BigDecimal costPrice;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 多张图片逗号分割
     */
    private String picture;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 商品sku码 唯一商品
     */
    private String sku;

    /**
     * 是否特价
     */
    private Integer isBargainPrice;

    /**
     * 门店上下架
     */
    private String status;

    /**
     * 是否推荐 1推荐 0不推荐
     */
    private Integer isRecommend;

    /**
     * 商品类型
     */
    private Integer goodsStyle;
}
