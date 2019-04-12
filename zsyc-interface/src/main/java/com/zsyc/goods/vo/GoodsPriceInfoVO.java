package com.zsyc.goods.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(description = "商品价格信息")
public class GoodsPriceInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品sku码 唯一商品
     */
    @ApiModelProperty(value = "商品sku码 唯一商品")
    private String sku;

    /**
     * 多张图片逗号分割
     */
    @ApiModelProperty(value = "多张图片逗号分割")
    private String picture;

    /**
     * 1.普通商品，2.组合商品
     */
    @ApiModelProperty(value = "1.普通商品，2.组合商品")
    private Integer goodsType;

    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述")
    private String description;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private Double price;

    /**
     * 红包价格
     */
    @ApiModelProperty(value = "红包价格")
    private Double bouns;

    /**
     * 商品类型
     */
    @ApiModelProperty(value = "商品类型")
    private Integer goodsStyle;

    /**
     * 属性值名称
     */
    @ApiModelProperty(value = "属性值名称")
    private List<GoodsAttributeValueVO> goodsAttributeValueVOS;

    /**
     * 子商品
     */
    @ApiModelProperty(value = "子商品")
    private List<GoodsPriceInfoVO> goodsPriceInfoVOS;


}
