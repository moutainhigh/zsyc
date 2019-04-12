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
 * 商品分类表
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(description = "商品分类表")
public class GoodsCategory extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键2、修改")
    private Long id;

    /**
     * 类目名称
     */
    @ApiModelProperty(value = "类目名称,1、新增2、修改")
    private String categoryName;

    /**
     * 多张图片逗号分割
     */
    @ApiModelProperty(value = "图片,1、新增2、修改")
    private String picture;

    /**
     * 父类Id
     */
    @ApiModelProperty(value = "父类Id,1、新增",required = false)
    private Long parentId;

    /**
     * 是否底层分类
     */
    @ApiModelProperty(value = "是否底层分类",hidden = true)
    private Integer isLeaf;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序",hidden = true)
    private Integer sort;

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
     * 创建用户ID
     */
    @ApiModelProperty(value = "创建用户ID",hidden = true)
    private Long createUserId;

    /**
     * tree path
     */
    @ApiModelProperty(value = "tree path",hidden = true)
    private String treePath;

    /**
     * 分类类型
     */
    @ApiModelProperty(value = "分类类型",hidden = true)
    private String categoryType;

    /**
     * 是否展示商品(0不展示,1展示)
     */
    @ApiModelProperty(value = "是否展示商品(0不展示,1展示)",required = false)
    private Integer display;
}
