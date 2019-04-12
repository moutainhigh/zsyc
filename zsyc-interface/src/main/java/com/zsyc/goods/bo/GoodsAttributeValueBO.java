package com.zsyc.goods.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class GoodsAttributeValueBO implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商品sku码
     */
    private String sku;

    /**
     * 属性key
     */
    private Long attrKey;

    /**
     * 属性值key
     */
    private Long attrValueKey;

    /**
     * 属性key名称
     */
    private String attrKeyName;

    /**
     * 属性值名称
     */
    private String attrValueName;

    /**
     * 是否售卖单位(0非售卖单位,1售卖单位)
     */
    private Integer isSale;
}
