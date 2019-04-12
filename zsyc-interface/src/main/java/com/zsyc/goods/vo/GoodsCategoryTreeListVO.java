package com.zsyc.goods.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品分类表
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "商品分类表")
public class GoodsCategoryTreeListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类目名称
     */
    private String categoryName;

    /**
     * 多张图片逗号分割
     */
    private String picture;

    /**
     * 父类Id
     */
    private Long parentId;

    /**
     * 是否底层分类
     */
    private Integer isLeaf;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否删除
     */
    private Integer isDel;

    /**
     * tree path
     */
    private String treePath;

    /**
     * 分类类型
     */
    private String categoryType;

    /**
     * 子分类列表
     */
    @ApiModelProperty(value = " 子分类列表")
    private List<GoodsCategoryTreeListVO> goodsCategoryTreeListVOS;
}
