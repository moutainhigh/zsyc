package com.zsyc.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.order.entity.OrderInfo;
import com.zsyc.order.po.OrderInfoPo;
import com.zsyc.order.vo.OrderInfoVo;
import com.zsyc.order.vo.OrderSubInfoVo;
import com.zsyc.order.vo.OrderVo;
import com.zsyc.pay.vo.PayOrderVo;
import org.apache.ibatis.annotations.Param;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @program: zsyc-parent
 * @description: 订单
 * @author: Mr.Ning
 * @create: 2019-01-15 14:28
 **/

public interface OrderInfoService {

    /**
     * 创建主订单
     * @param orderVo
     */
    String createOrderInfo(OrderVo orderVo);


    /**
     * 后台下单
     * @param orderVo
     */
    Long createOrderInfoBack(OrderVo orderVo);

    List<OrderInfo> test();


    /**
     * 根据主订单状态获取数据(已分页)
     * @param orderInfoVo
     * @return
     */
    IPage<OrderInfoPo> getOrderInfosByStatus(OrderInfoVo orderInfoVo);

    /**
     * 全部主订单(已分页)
     * @param orderInfoVo
     * @return
     */
    IPage<OrderInfoPo> getOrderinfos(OrderInfoVo orderInfoVo);


    /**
     * 根据主订单Id获取数据(主订单详情)
     * @param map
     * @return
     */
    OrderInfoPo getOrderInfoById(Map map);


    /**
     * 根据订单号获取订单实体内容
     * @param orderNo
     * @return
     */
    OrderInfoPo getOrderInfoByNo(String orderNo);


    /**
     * 删除主订单
     * @return
     */
    void delOrder(Map params);

    /**
     * 更新主订单状态
     * @param params
     * @return
     */
    void updateOrderStatus(Map params);


    /**
     * 获取会员所有订单状态对应的数量
     * @param memberId
     * @param status
     * @return
     */
    int getAllOrderStatusCount(Long memberId,String status);


    /**
     * 修改主订单数据
     * @param orderNo
     * @param amount
     * @param actualAmount
     * @param now
     */
    void updateOrderByOldforNew(String orderNo,int amount,int actualAmount,LocalDateTime now);

    /**
     * 支付回调修改订单数据
     * @param orderInfoPo
     */
    void callback(OrderInfoPo orderInfoPo);

    /**
     * 检测该订单是否是气订单
     * @param orderInfoPo
     * @return
     */
    boolean checkOrderIsGas(OrderInfoPo orderInfoPo);

    /**
     * 订单支付
     * @param payOrder
     * @param accountId
     * @param ip
     * @return
     */
    PayOrderVo payOrder(PayOrderVo payOrder,Long accountId,String ip);
}
