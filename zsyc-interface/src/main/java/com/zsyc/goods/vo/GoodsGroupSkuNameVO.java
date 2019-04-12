package com.zsyc.goods.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class GoodsGroupSkuNameVO implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * sku编码
     */
    private String sku;

    /**
     * 子商品排序
     */
    private Integer sort;

    /**
     * 商品类型
     */
    private Integer goodsStyle;
}
