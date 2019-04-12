package com.zsyc.goods.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class GoodsInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品sku码 唯一商品
     */
    private String sku;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 品牌编码
     */
    private String brandCode;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 类目名称
     */
    private String categoryName;

    /**
     * 分类类型
     */
    private String categoryType;

    /**
     * 1.普通商品，2.组合商品
     */
    private Integer goodsType;

    /**
     * 属性值名称
     */
    private List<GoodsAttributeValueVO> goodsAttributeValueVOS;

    /**
     * sku编码
     */
    private List<GoodsGroupSkuNameVO> goodsGroupSkuNameVOS;

    /**
     * 状态
     */
    private String status;
}
