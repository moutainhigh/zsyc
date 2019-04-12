package com.zsyc.pay.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zsyc.order.entity.OrderInfo;
import com.zsyc.order.entity.OrderProtocolInfo;
import com.zsyc.order.po.OrderInfoPo;
import com.zsyc.order.po.OrderProtocolInfoPo;
import com.zsyc.order.service.OrderInfoService;
import com.zsyc.order.service.OrderProtocolInfoService;
import com.zsyc.pay.entity.PayOrder;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: zsyc-parent
 * @description: 支付内部回调
 * @author: Mr.Ning
 * @create: 2019-04-09 09:49
 **/
@Service
public class InnerCallbackServiceImpl implements InnerCallbackService{

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private OrderProtocolInfoService orderProtocolInfoService;

    /**
     * 此方法是成功后才回调，非成功不回调
     * @param payOrder
     */
    @Override
    public void payCallback(PayOrder payOrder) {
        OrderInfoPo orderInfoPo = orderInfoService.getOrderInfoByNo(payOrder.getOrderNo());
        /**
         * 更新订单信息
         * @param payOrder
         */
        updateOrderInfo(payOrder,orderInfoPo);

        /**
         * 更新最后缴纳租金时间
         * @param orderInfoPo
         */
        updateRentTime(orderInfoPo);
    }

    /**
     * 更新订单信息
     * @param payOrder
     */
    public void updateOrderInfo(PayOrder payOrder,OrderInfoPo orderInfoPo){
        /**
         * 订单需要更新的数据
         */
        OrderInfoPo params = new OrderInfoPo();
        params.setPayFlowId(payOrder.getId().toString());  //交易流水id
        params.setPayFlowNo(payOrder.getOrderNo());    //交易流水号
        params.setPayTime(payOrder.getTimeEnd());      //交易完成时间

        /**
         * 订单进行更新(不包含订单状态)
         */
        orderInfoService.callback(params);

        /**
         * 更新订单状态（主，子订单）
         */
        Map<String,Object> map = new HashMap();
        map.put("status", OrderInfo.EOrderStatus.SUCCESS.val());
        map.put("orderId",orderInfoPo.getId());
        orderInfoService.updateOrderStatus(map);
    }


    /**
     * 更新最后缴纳租金时间
     * @param orderInfoPo
     */
    public void updateRentTime(OrderInfoPo orderInfoPo){
        /**
         * 当店铺是气店时才更新租金
         */
        if (!orderInfoService.checkOrderIsGas(orderInfoPo)){
            return;
        }

        //该子订单的租金
        List<OrderProtocolInfoPo> orderProtocolInfoPos = orderProtocolInfoService.getOrderProtocolInfo(orderInfoPo.getCreateUserId());
        int month = 0; //月份

        for (OrderProtocolInfoPo orderProtocolInfoPo : orderProtocolInfoPos) {
            //如果是借的话，则不计算租金
            if (orderProtocolInfoPo.getType().equals(OrderProtocolInfo.ProtocolType.BORROW.val())) continue;
            month = LocalDateTime.now().getMonthValue() - orderProtocolInfoPo.getLastRentPaymentTime().getMonthValue();
            if (month != 0) {

                /**
                 * 更新最后一次缴纳租金时间
                 */
                orderProtocolInfoService.updateOrderProtocolLastRentPaymentTime(orderProtocolInfoPo.getProtocolNo(), LocalDateTime.now());
            }
        }
    }
}
