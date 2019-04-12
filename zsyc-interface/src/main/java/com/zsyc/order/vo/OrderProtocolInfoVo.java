package com.zsyc.order.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zsyc.order.entity.OrderProtocolInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: zsyc-parent
 * @description: 协议
 * @author: Mr.Ning
 * @create: 2019-02-26 17:46
 **/
@Data
public class OrderProtocolInfoVo extends OrderProtocolInfo {

    /**
     * 当前页码
     */
    private Integer current = 1;

    /**
     * 页码大小
     */
    private Integer size = 10;

    private Long orderId;

}
