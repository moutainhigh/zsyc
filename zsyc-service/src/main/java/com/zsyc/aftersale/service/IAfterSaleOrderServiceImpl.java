package com.zsyc.aftersale.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zsyc.aftersale.entity.AfterSaleOrder;
import com.zsyc.aftersale.entity.AfterSaleOrderGoods;
import com.zsyc.aftersale.mapper.AfterSaleOrderMapper;
import com.zsyc.aftersale.po.PayServicePo;
import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.pay.service.PayOrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class IAfterSaleOrderServiceImpl implements AfterSaleOrderService {
    @Autowired
    public AfterSaleOrderMapper aftSalMapper;
    @Autowired
    public AfterSaleOrderGoodsService afterSaleOrderGoodsService;
    @Autowired
    public PayOrderService payOrderService;

    @Override
    public Integer warehouseOrderPARTIAL(Long warehouseOrderPARTIAL) {
        return aftSalMapper.warehouseOrderPARTIAL(warehouseOrderPARTIAL);
    }

    @Override
    public Integer outStock(String afterSaleNo1,String staffPhone,String staffName,Long WarehousePackOrderID, Long ordersubId, Long addressId) {
        AfterSaleOrder afso =new AfterSaleOrder();
        afso.setAfterSaleNo(afterSaleNo1);
        afso.setStaffPhone(staffPhone);
        afso.setStaffName(staffName);
        afso.setWarehousePackOrderId(WarehousePackOrderID);
        afso.setOrderSubId(ordersubId);
        afso.setMemberAddressId(addressId);
        afso.setStatus("FOLLOWUP");
        return aftSalMapper.insert(afso);
    }


//    查询待处理的客服表的子订单信息
    @Override
    public List<OrderSubInfo> selectAfterSaleOrderAll() {
        List<AfterSaleOrder>afterSaleOrders=aftSalMapper.selectAfterSaleOrderSubId();
        List<OrderSubInfo> orderSubInfos=new ArrayList<>();
        for(AfterSaleOrder f:afterSaleOrders){
            Long subIdd=f.getOrderSubId();
            List<OrderSubInfo> afterSaleOrderAll=aftSalMapper.selectAfterSaleOrderAll(subIdd);
            orderSubInfos.addAll(afterSaleOrderAll);
        }
        return orderSubInfos;
    }

    @Override
    public Integer afterUpdateOrderSubStauts(Long subId) {
        return aftSalMapper.afterUpdateOrderSubStauts(subId);
    }

    @Override
    public Object selectAfterSaleOrderGoods(PayServicePo payServicePo) {
        Integer refundFree=0;
        String orderNo=payServicePo.getOrderNo();
        for(int i=0;i<payServicePo.getAfterSaleOrderGoodsId().length;i++){
          List<AfterSaleOrderGoods> afterSaleOrderGoods =aftSalMapper.selectAfterSaleOrderGoods(payServicePo.getAfterSaleOrderGoodsId()[i]);

          for(AfterSaleOrderGoods afterSaleOrderGoods1:afterSaleOrderGoods){
              refundFree+=afterSaleOrderGoods1.getRefundAmount();
          }
}

        return payOrderService.refund(orderNo,refundFree);
    }
}
