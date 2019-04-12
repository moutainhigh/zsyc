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
@ApiModel(description = "小程序商品分类表")
public class GoodsCategoryBaseTreeListVO implements Serializable {

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
     * 子分类列表
     */
    @ApiModelProperty(value = " 子分类列表")
    private List<GoodsCategoryBaseTreeListVO> goodsCategoryBaseTreeListVOS;
}
