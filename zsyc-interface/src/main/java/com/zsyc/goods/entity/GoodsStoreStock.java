package com.zsyc.goods.entity;

import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 商品库存表

 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(description = "商品属性表")
public class GoodsStoreStock extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键",hidden = true)
    private Long id;

    /**
     * 商品sku码
     */
    @ApiModelProperty(value = "商品sku码")
    private String sku;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 库存总数
     */
    @ApiModelProperty(value = "库存总数")
    private Long total;

    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id",hidden = true)
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间",hidden = true)
    private LocalDateTime createTime;


}
