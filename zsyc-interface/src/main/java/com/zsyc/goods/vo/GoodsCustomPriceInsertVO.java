package com.zsyc.goods.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

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
@ApiModel(description = "商品属性表")
public class GoodsCustomPriceInsertVO implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键",hidden = true)
    private Long id;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID1、新增2、修改3、删除")
    private Long storeId;

    /**
     * 商品sku码
     */
    @ApiModelProperty(value = "商品sku码,1、新增2、修改3、删除")
    private String sku;

    /**
     * 地址ID
     */
    @ApiModelProperty(value = "地址ID,1、新增2、修改3、删除")
    private Long addressId;

    /**
     * 商品自定义价格
     */
    @ApiModelProperty(value = "商品自定义价格,1、新增2、修改")
    private BigDecimal price;

}
