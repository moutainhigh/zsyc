package com.zsyc.warehouse.po;



import com.zsyc.order.entity.OrderGoods;
import com.zsyc.warehouse.entity.WarehouseOrderGoods;
import com.zsyc.warehouse.entity.WarehousePackOrderGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 小程序显示备货界面po
 * @author: Mr.Ning
 * @create: 2019-01-23 17:29
 **/

@Data
public class SortingOrderPo extends WarehouseOrderGoods {
    private Long storeId;

    private Long WarehousePackOrderGoodsId;


    private Long warehousePackOrderId;
    /**
     * 备货订单编号
     */
    private String warehouseOrderNo;

    /**
     * 订单子表ID
     */
    @ApiModelProperty(value = "订单子ID")
    private Long orderSubId;


    private String goodsName;

    private String num;
    private String unit;
    private String orderNo;
    private String address;
    private Long addressId;
    private String img;
    private BigDecimal totalPrice;
    private List<OrderGoodsKuai> kuaijie=new ArrayList();
}
