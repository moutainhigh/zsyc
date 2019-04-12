package com.zsyc.goods.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class GoodsStoreStockListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 商品编码
     */
    private String sku;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 门店名，不可重复
     */
    private String storeName;

    /**
     * 多张图片逗号分割
     */
    private String picture;

    /**
     * 1.普通商品，2.组合商品
     */
    private Integer goodsType;

    /**
     * 库存总数
     */
    private Long total;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 商品类型
     */
    private Integer goodsStyle;
}
