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
 * 商品自定义价格
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(description = "商品自定义价格")
public class GoodsCustomPrice extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(name = "id1",value = "id")
    private Long id;

    /**
     * 店铺ID
     */
    @ApiModelProperty(name = "storeId1",value = "storeId")
    private Long storeId;

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 是否删除
     */
    private Integer isDel;


}
