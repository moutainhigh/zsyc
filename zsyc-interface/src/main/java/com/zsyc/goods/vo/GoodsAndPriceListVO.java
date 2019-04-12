package com.zsyc.goods.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class GoodsAndPriceListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 商品sku码 唯一商品
     */
    private String sku;

    /**
     * 商品spu码 同款商品
     */
    private String spu;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 多张图片逗号分割
     */
    private String picture;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品气价格价格
     */
    private BigDecimal groupPrice;

    /**
     * 商品押金
     */
    private BigDecimal deposit;

    /**
     * 属性值名称
     */
    private List<GoodsAttributeValueVO> goodsAttributeValueVOS;

    /**
     * 商品详情展示类型
     */
    private Integer type;
}
