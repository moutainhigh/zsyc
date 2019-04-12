package com.zsyc.warehouse.po;

import com.zsyc.order.entity.OrderGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class WarehouseGoods extends OrderGoods {

    /**
     * 备货订单编号
     */
    private String warehouseOrderNo;
    private String img;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 预订配送开始时间
     */
    @ApiModelProperty(value = "预订配送开始时间")
    private LocalDateTime bookStartTime;

    /**
     * 收货人地址
     */
    @ApiModelProperty(value = "收货人地址")
    private String consigneeAddress;
    private BigDecimal TotalPrice;
    List<OrderGoodsKuai> kuaijie=new ArrayList<>();


}
