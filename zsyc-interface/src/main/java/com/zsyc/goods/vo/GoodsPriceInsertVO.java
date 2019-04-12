package com.zsyc.goods.vo;

import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(description = "店铺商品价格")
public class GoodsPriceInsertVO extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键",hidden = true)
    private Long id;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID,1、新增2、修改")
    private Long storeId;

    /**
     * 商品sku码
     */
    @ApiModelProperty(value = "商品sku码,1、新增2、修改")
    private String sku;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格,1、新增2、修改")
    private BigDecimal price;

    /**
     * vip价格
     */
    @ApiModelProperty(value = "vip价格,1、新增2、修改")
    private BigDecimal vipPrice;

    /**
     * 成本价格
     */
    @ApiModelProperty(value = "成本价格,1、新增2、修改")
    private BigDecimal costPrice;

    /**
     * 是否特价
     */
    @ApiModelProperty(value = "是否特价,1、新增")
    private Integer isBargainPrice;

    /**
     * 库存总数
     */
    @ApiModelProperty(value = "库存总数,1、新增2、修改")
    private Long total;

    /**
     * 是否推荐 1推荐 0不推荐
     */
    @ApiModelProperty(value = "是否推荐 1推荐 0不推荐,1、新增")
    private Integer isRecommend;

    /**
     * 红包价格
     */
    @ApiModelProperty(value = "vip价格,1、新增")
    private BigDecimal bouns;
}
