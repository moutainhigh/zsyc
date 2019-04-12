package com.zsyc.goods.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class GoodsStorePriceVO extends GoodsInfoListVO{

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
     * 品牌名称
     */
    private String brandName;

    /**
     * 属性值名称
     */
    private List<GoodsAttributeValueVO> goodsAttributeValueVOS;

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
     * 库存总数
     */
    private Long total;
}
