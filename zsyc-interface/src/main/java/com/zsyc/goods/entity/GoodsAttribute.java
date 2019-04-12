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
 * 商品属性表
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(description = "商品属性表")
public class GoodsAttribute extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键,2、修改")
    private Long id;

    /**
     * 属性key
     */
    @ApiModelProperty(value = "属性key",hidden = true)
    private Long attrKey;

    /**
     * 属性key名称
     */
    @ApiModelProperty(value = "属性key名称,1、新增2、修改")
    private String attrKeyName;

    /**
     * 是否售卖单位(0非售卖单位,1售卖单位)
     */
    @ApiModelProperty(value = "是否售卖单位(0非售卖单位,1售卖单位),1、新增")
    private Integer isSale;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间",hidden = true)
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "创建人ID",hidden = true)
    private Long createUserId;

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除",hidden = true)
    private Integer isDel;
}
