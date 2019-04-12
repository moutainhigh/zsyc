package com.zsyc.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.order.entity.OrderProtocolInfo;
import com.zsyc.order.po.OrderGoodsPo;
import com.zsyc.order.po.OrderProtocolInfoPo;
import com.zsyc.order.po.OrderSubInfoPo;
import com.zsyc.order.vo.OrderProtocolInfoVo;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @program: zsyc-parent
 * @description: 协议
 * @author: Mr.Ning
 * @create: 2019-02-26 17:31
 **/

public interface OrderProtocolInfoService {

    /**
     * 查询会员协议(已分页)
     * @return
     */
    IPage<OrderProtocolInfoPo> getOrderProtocolInfo(Map map);


    /**
     * 查询会员的全部协议（不分页）
     * @param memberId
     * @return
     */
    List<OrderProtocolInfoPo> getOrderProtocolInfo(Long memberId);


    /**
     * 协议详情
     * @param map
     * @return
     */
    OrderProtocolInfoPo protocolDetails(Map map);


    /**
     * 协议详情（获取某个协议的退瓶订单列表）
     * @param map
     * @return
     */
    IPage<OrderSubInfoPo> getProtocolRefundOrder(Map map);


    /**
     * 更新协议当前押金
     * @param protocolNo
     * @param amount
     */
    void updateDepositCurrent(String protocolNo,int amount);


    /**
     * 根据协议号查找协议信息
     * @return
     */
    OrderProtocolInfoPo getOrderProtocolInfoPoByProtocolNo(String protocolNo);

    /**
     *查看店铺所有用户的协议(后台)
     * @param orderProtocolInfoVo
     * @return
     */
    IPage<OrderProtocolInfoPo> getAllProtocolByStoreId(OrderProtocolInfoVo orderProtocolInfoVo);


    /**
     * 更新最后一次缴纳租金时间
     * @param protocolNo
     * @param now
     */
    void updateOrderProtocolLastRentPaymentTime(String protocolNo, LocalDateTime now);

    /**
     * 添加协议（后台）
     * @param orderProtocolInfoVo
     */
    void addOrderProtocol(OrderProtocolInfoVo orderProtocolInfoVo);
}
