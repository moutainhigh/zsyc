package com.zsyc.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.order.po.OrderInfoPo;
import com.zsyc.order.po.OrderSubInfoPo;
import com.zsyc.order.vo.OrderGoodsVo;
import com.zsyc.order.vo.OrderInfoVo;
import com.zsyc.order.vo.OrderSubInfoVo;
import com.zsyc.order.vo.OrderVo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @program: zsyc-parent
 * @description: 子订单
 * @author: Mr.Ning
 * @create: 2019-02-02 09:40
 **/

public interface OrderSubInfoService {

    /**
     * 创建子订单
     * @param orderVo
     * @return map.List<OrderSubInfo> 返回批量插入数据的自增id
     */
    Map<String,Object> createOrderSub(OrderVo orderVo,String orderInfoNo);

    /**
     * 后台下单
     * @param orderVo
     * @return map.List<OrderSubInfo> 返回批量插入数据的自增id
     */
    Map<String,Object> createOrderSubBack(OrderVo orderVo,String orderInfoNo);

    /**
     * 在返回批量插入数据的自增id里，根据店铺id获取子订单id
     * @param map
     * @return
     */
    Long getOrderSubIdByStoreId(Map map);


    /**
     * 根据店铺id获取子订单信息（给店铺提供的接口所依赖）
     * @param storeId
     * @return
     */
    List<OrderSubInfoPo> getOrderSubByStoreId(Long storeId);

    /**
     * 根据id查询
     * @return
     */
    OrderSubInfoPo getOrderSubById(Long id);


    /**
     * 删除子订单
     * @param ids
     * @return
     */
    int delOrderSub(List<String> ids);


    /**
     * 更新子订单状态
     */
    void updateOrderSubStatus(Map<String,Object> params);


    /**
     * 根据子订单状态获取数据(已分页)
     * @param orderSubInfoVo
     * @return
     */
    IPage<OrderSubInfoPo> getOrderSubInfosByStatus(OrderSubInfoVo orderSubInfoVo);


    /**
     * 全部子订单(已分页)
     * @param orderSubInfoVo
     * @return
     */
    IPage<OrderSubInfoPo> getOrderSubinfos(OrderSubInfoVo orderSubInfoVo);

    /**
     *根据子订单Id获取数据(子订单详情)
     * @param map
     * @return
     */
    OrderSubInfoPo getOrderSubInfoById(Map map);


    /**
     * 获取会员所有订单状态对应的数量
     * @param memberId
     * @return
     */
    Map<String,Integer> getAllOrderStatusCount(Long memberId);


    /**
     *根据子订单号获取数据
     * @param map
     * @return
     */
    OrderSubInfoPo getOrderSubInfoByOrderNo(Map map);

    /**
     *计算气订单的租金、押金、运费、优惠、总金额等(确认订单接口)
     * @param orderInfoVo
     * @return
     */
    OrderInfoPo getGasCalculatedAmount(OrderInfoVo orderInfoVo);

    /**
     * 计算菜市场订单的运费、优惠、总金额等(确认订单接口)
     * @param orderInfoVo
     * @return
     */
    OrderInfoPo getCalculatedAmount(OrderInfoVo orderInfoVo);


    /**
     * 是否可以催单
     * @return
     */
    boolean orderIsAbleReminder(Map params);

    /**
     * 催单
     * @param params
     */
    void orderReminder(Map params);


    /**
     * 获取店铺的订单(后台)
     * @param orderSubInfoVo
     * @return
     */
    IPage<OrderSubInfoPo> getOrderSubInfosByStoreId(OrderSubInfoVo orderSubInfoVo);


    /**
     * 获取某个协议的退瓶订单列表
     * @param current
     * @param size
     * @param protocolNo
     * @return
     */
    IPage<OrderSubInfoPo> getProtocolRefundOrder(Long current,Long size,String protocolNo);


    /**
     * 生成退瓶订单
     * @param orderSubInfoVo
     */
    void createProtocolRefundOrder(OrderSubInfoVo orderSubInfoVo);


    /**
     * 根据时间段获取子订单（给店铺提供的接口）
     * @param orderSubInfoVo
     * @return
     */
    List<OrderSubInfoPo> getOrderinfosByTimeSlot(OrderSubInfoVo orderSubInfoVo);


    /**
     * 获取租金和运费信息
     * @param params
     * @return
     */
    Map<String, Object> getRentAmountAndExpressInfo(Map params);

    /**
     * 根据协议号查找对应子订单信息（是协议主单，不是退瓶订单）
     * @param protocolNo
     * @return
     */
    OrderSubInfoPo getOrderSubByProtocolNo(String protocolNo);


    /**
     * 获取店铺旧换新的订单(后台)
     * @param orderSubInfoVo
     * @return
     */
    IPage<OrderSubInfoPo> getOldforNewOrderByStoreId(OrderSubInfoVo orderSubInfoVo);


    /**
     * 旧换新订单绑定商品(后台)
     * @param orderSubInfoVo
     */
    void orderOldforNewBoundGoods(OrderSubInfoVo orderSubInfoVo);


    /**
     * 从订单中获取生成订单结算报表需要的数据
     * @return
     */
    List<OrderSubInfoPo> getOrderDataForReport(LocalDateTime now);


    /**
     * 修改子订单数据(后台)
     * @param orderSubInfoVo
     */
    void updateOrdersubData(OrderSubInfoVo orderSubInfoVo);


    /**
     * 催单(后台)
     * @param params
     * @return
     */
    void orderOeminderBack(Map params);


    /**
     * 接单列表（配送端）
     * @param orderSubInfoVo
     * @return
     */
    IPage<OrderSubInfoPo> getOrderTakingList(OrderSubInfoVo orderSubInfoVo);


    /**
     * 配送员接单（配送端）
     * @param orderSubInfoVo
     * @return
     */
    boolean orderTaking(OrderSubInfoVo orderSubInfoVo);

    /**
     * 送单列表和完成列表（配送端）
     * @param orderSubInfoVo
     * @return
     */
    IPage<OrderSubInfoPo> getOrderByPostmanIdAndStatus(OrderSubInfoVo orderSubInfoVo);

    /**
     * 获取新租瓶的订单列表（后台）
     * @param orderSubInfoVo
     * @return
     */
    IPage<OrderSubInfoPo> getNewOrderList(OrderSubInfoVo orderSubInfoVo);


    /**
     * 确认订单(后台)
     * @param orderId
     */
    void firmOrder(Long orderId);


    /**
     * 获取需要确认订单的订单列表(已分页)
     * @param orderSubInfoVo
     * @return
     */
    IPage<OrderSubInfoPo> firmOrderList(OrderSubInfoVo orderSubInfoVo);
}
