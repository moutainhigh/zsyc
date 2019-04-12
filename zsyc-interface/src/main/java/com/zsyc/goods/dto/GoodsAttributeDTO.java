package com.zsyc.goods.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class GoodsAttributeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 属性key
     */
    @ApiModelProperty(value = "属性key")
    private Long attrKey;

    /**
     * 属性值key
     */
    @ApiModelProperty(value = "属性值key")
    private Long attrValueKey;
}
