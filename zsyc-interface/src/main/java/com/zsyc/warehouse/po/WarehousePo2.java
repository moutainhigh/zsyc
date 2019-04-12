package com.zsyc.warehouse.po;

import com.zsyc.order.entity.OrderGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: zsyc-parent
 * @description: 订单Vo
 * @author: Mr.Ning
 * @create: 2019-01-23 17:29
 **/

@Data
public class WarehousePo2 {
    /**
     * 备货员员名称
     */
    private String stockName;

    /**
     * 备货员id
     */
    private Long id;

    private String phone;

    private String idCard;

    private String idCardImg;

    private Long storeId;

    private String stockType;

    private Integer currentPage;

    private Integer pageSize;
}
