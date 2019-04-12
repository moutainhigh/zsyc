package com.zsyc.goods.vo;

import com.zsyc.goods.dto.GoodsAttributeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(description = "商品修改")
public class GoodsInfoUpdateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 多张图片逗号分割
     */
    @ApiModelProperty(value = "图片")
    private String picture;

    /**
     * 商品sku码 唯一商品
     */
    @ApiModelProperty(value = "sku")
    private String sku;

    /**
     * 品牌编码
     */
    @ApiModelProperty(value = "品牌编码")
    private String brandCode;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
    private Long categoryId;

    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述")
    private String description;

    /**
     * 子商品sku编码
     */
    @ApiModelProperty(value = "子商品sku编码",required = false)
    private List<GoodsGroupInsertVO> goodsGroupInsertVOS;

    /**
     * 配料sku编码
     */
    @ApiModelProperty(value = "配料sku编码",required = false)
    private List<GoodsGroupInsertVO> goodsIngredientVOs;

    /**
     * 商品属性
     */
    @ApiModelProperty(value = "商品属性")
    private List<GoodsAttributeDTO> goodsAttributeDTOS;

}
