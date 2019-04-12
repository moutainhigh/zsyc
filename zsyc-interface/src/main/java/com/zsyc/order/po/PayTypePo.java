package com.zsyc.order.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: zsyc-parent
 * @description: 支付类型
 * @author: Mr.Ning
 * @create: 2019-03-07 10:23
 **/
@Data
@ApiModel(description = "支付类型")
public class PayTypePo implements Serializable {
    @ApiModelProperty(value = "支付名称")
    private String name;

    @ApiModelProperty(value = "支付值")
    private String value;
}
