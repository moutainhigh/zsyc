package com.zsyc.goods.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(description = "小程序商品分类表")
public class GoodsCategoryListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 类目名称
     */
    @ApiModelProperty(value = "类目名称")
    private String categoryName;

    /**
     * 多张图片逗号分割
     */
    @ApiModelProperty(value = "图片")
    private String picture;

    /**
     * 父类Id
     */
    @ApiModelProperty(value = "父类Id")
    private Long parentId;

    /**
     * 是否底层分类
     */
    @ApiModelProperty(value = "是否底层分类")
    private Integer isLeaf;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 分类类型
     */
    @ApiModelProperty(value = "分类类型")
    private String categoryType;

    /**
     * 子分类列表
     */
    @ApiModelProperty(value = " 子分类列表")
    private List<GoodsCategoryListVO> goodsCategoryListVOS;
}
