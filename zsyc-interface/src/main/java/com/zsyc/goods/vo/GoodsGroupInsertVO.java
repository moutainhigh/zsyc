package com.zsyc.goods.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class GoodsGroupInsertVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * sku编码 子商品
     */
    @ApiModelProperty(value = "sku编码 子商品")
    private String subSku;

    /**
     * 子商品排序
     */
    @ApiModelProperty(value = "子商品排序")
    private Integer sort;
}
