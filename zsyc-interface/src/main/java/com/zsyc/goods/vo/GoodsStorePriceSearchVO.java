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
@ApiModel(description = "商品vip价格列表查询")
public class GoodsStorePriceSearchVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID",required = false)
    private Long storeId;

    /**
     * 1.普通商品，2.组合商品
     */
    @ApiModelProperty(value = "1.普通商品，2.组合商品",required = true)
    private Integer goodsType;

    /**
     * 商品sku码
     */
    @ApiModelProperty(value = "商品sku码",required = false)
    private String sku;

    /**
     * 门店上下架
     */
    @ApiModelProperty(value = "上下架,ON_SALE,OFF_SALE",required = false)
    private String status;

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
     * beforePrice
     */
    @ApiModelProperty(value = "商品价格beforePrice",required = false,hidden = true)
    private Integer changeBeforePrice;

    /**
     * afterPrice
     */
    @ApiModelProperty(value = "商品价格afterPrice",required = false,hidden = true)
    private Integer changeAfterPrice;

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

    /**
     * 是否特价 1特价 0不特价
     */
    @ApiModelProperty(value = "是否特价 1特价 0不特价",required = false)
    private Integer isBargainPrice;

    /**
     * 是否推荐 1推荐 0不推荐
     */
    @ApiModelProperty(value = "是否推荐 1推荐 0不推荐",required = false)
    private Integer isRecommend;
}
