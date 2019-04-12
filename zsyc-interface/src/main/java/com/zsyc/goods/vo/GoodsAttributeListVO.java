package com.zsyc.goods.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class GoodsAttributeListVO implements Serializable {

    /**
     * 属性key
     */
    @ApiModelProperty(value = " 属性key")
    private Long attrKey;

    /**
     * 属性值keys
     */
    @ApiModelProperty(value = "属性值keys")
    private List<Long> attrValueKeys;
}
