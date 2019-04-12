package com.zsyc.aftersale.service;

import com.zsyc.aftersale.po.PayServicePo;
import com.zsyc.order.entity.OrderSubInfo;

import java.util.List;

public interface AfterSaleOrderService {
    Integer  warehouseOrderPARTIAL(Long warehouseOrderPARTIAL);
    Integer outStock(String afterSaleNo1,String staffPhone,String staffName,Long WarehousePackOrderID, Long ordersubId, Long addressId);
    List<OrderSubInfo> selectAfterSaleOrderAll();
    Integer afterUpdateOrderSubStauts(Long subId);
    Object selectAfterSaleOrderGoods(PayServicePo payServicePo);


}
