package com.zsyc.order.vo;

import com.zsyc.order.entity.OrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 主订单Vo
 * @author: Mr.Ning
 * @create: 2019-02-15 10:18
 **/

@Data
public class OrderInfoVo extends OrderInfo {

    List<OrderSubInfoVo> orderSubInfoVoList;

    private Integer current = 1;

    private Integer size = 10;
}
