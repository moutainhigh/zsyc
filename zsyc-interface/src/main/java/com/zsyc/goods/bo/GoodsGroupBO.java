package com.zsyc.goods.bo;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsGroupBO extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * sku编码 组合商品
     */
    private String sku;

    /**
     * sku编码 子商品
     */
    private String subSku;

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

    /**
     * 商品类型
     */
    private Integer goodsStyle;
}
