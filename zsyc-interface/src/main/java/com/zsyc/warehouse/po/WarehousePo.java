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
public class WarehousePo extends OrderGoods {
    /**
     * 备货员员名称
     */
    private String name;

    /**
     * 备货员id
     */
    private Long warehouseStaffId;

    /**
     * warehousePackOrder
     */
    private Long warehousePackOrderId;
    /**
     * 商品名称
     */
    private String goodsName;
/**
 * 主备货单id
 */
   private Long warehouseOrderId;
    /**
     * 备货子表id
     */
    private Long orderSubStockId;

    /**
     * 备货订单编号
     */
    private String stockNo;
    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    /**
     * 备货信息（多少斤，多少个，多少单）
     */
    private String message;


}
