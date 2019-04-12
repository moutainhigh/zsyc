package com.zsyc.warehouse.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsyc.goods.entity.GoodsInfo;
import com.zsyc.order.entity.OrderGoods;
import com.zsyc.order.entity.OrderInfo;
import com.zsyc.order.entity.OrderSubInfo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: zsyc-parent
 * @description: 订单Vo
 * @author: Mr.Ning
 * @create: 2019-01-23 17:29
 **/

@Data
public class StockOrderPo extends OrderGoods {
    /**
     * 商品名称
     */
    private String goodsName;


    /**
     * 备货子表id
     */
    private Long orderSubStockId;

    /**
     * 备货订单编号
     */
    private String stockNo;

    /**
     * 备货订单编号
     */
    private String warehouseOrderNo;

    /**
     * 备货主表id
     */
    private Long warehouseOrderId;





}
