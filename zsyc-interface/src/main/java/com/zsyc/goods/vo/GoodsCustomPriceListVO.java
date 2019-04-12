package com.zsyc.goods.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 商品自定义价格
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Data
@Accessors(chain = true)
public class GoodsCustomPriceListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 门店名，不可重复
     */
    private String storeName;

    /**
     * 商品sku码
     */
    private String sku;

    /**
     * 地址ID
     */
    private Long addressId;

    /**
     * 商品自定义价格
     */
    private Integer price;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 1.普通商品，2.组合商品
     */
    private Integer goodsType;

    /**
     * 是否删除
     */
    private Integer isDel;


}
