package com.zsyc.goods.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class GoodsNameAndAttributeVO implements Serializable {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品sku码 唯一商品
     */
    private String sku;

    /**
     * 多张图片逗号分割
     */
    private String picture;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 属性值名称
     */
    private List<GoodsAttributeValueVO> goodsAttributeValueVOS;

    /**
     * 1.子商品,2.配料
     */
    private Integer type;
}
