package com.zsyc.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(description = "商品自定义价格列表查询")
public class GoodsCustomPriceSearchVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 地址ID
     */
    private Long addressId;

    /**
     * 商品sku码
     */
    @ApiModelProperty(value = "商品sku码",required = false)
    private String sku;

    /**
     * beforePrice
     */
    @ApiModelProperty(value = "商品价格beforePrice",required = false)
    private BigDecimal beforePrice;

    /**
     * afterPrice
     */
    @ApiModelProperty(value = "商品价格afterPrice",required = false)
    private BigDecimal afterPrice;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间",required = false)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beforeTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间",required = false)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime afterTime;
}
