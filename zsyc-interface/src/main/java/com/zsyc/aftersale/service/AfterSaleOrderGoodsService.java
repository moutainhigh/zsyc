package com.zsyc.aftersale.service;

import com.zsyc.aftersale.entity.AfterSaleOrder;
import com.zsyc.aftersale.po.AfterSaleOrderAccept;
import com.zsyc.aftersale.po.AfterSalePo;
import com.zsyc.order.entity.OrderGoods;

public interface AfterSaleOrderGoodsService {
    Integer afterSaleOrderGoods(String afteraleSNo, Integer original,String sku,Long ordersubId,Integer num,Integer refundAmount,String parentSku);
    OrderGoods selectGoodsPrice(Long ordersubId, String sku);
    Integer updateOrderSub(Long ordersubId);
    Integer updateOrderGoods(Long ordersubId);
    Object selectAfterSaleOrderGoods();
    Integer updateSubOrderemark(String backendRemark,Long subId);
    Object outOcket(AfterSaleOrderAccept afterSaleOrderAccept);
    Object selectAfterSaleOrderOrderNo(String afterSaleNo);
    Object selectAfterSaleOrderSubId(Long subId);
    Object selectAfterSaleStaffName(String staffName);
    Object selectAfterSalePhone(String phone);
    Object selectAfterSaleAll(AfterSalePo afterSalePo);
    Integer updateAfterStatus(Long afterId, String status);
    Integer updateAfterSaleOrderRemark(AfterSaleOrder afterSaleOrder);
    Integer  doneService(Long id);

}
