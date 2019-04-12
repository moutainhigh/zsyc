package com.zsyc.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 商品品牌表
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(description = "商品品牌表")
public class GoodsBrand extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键,2、修改")
    private Long id;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称,1、新增2、修改")
    private String brandName;

    /**
     * 品牌编码
     */
    @ApiModelProperty(value = "品牌编码,1、新增")
    private String brandCode;

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除",hidden = true)
    private Integer isDel;

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


}
