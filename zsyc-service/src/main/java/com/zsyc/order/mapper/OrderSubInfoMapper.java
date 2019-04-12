package com.zsyc.order.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.order.entity.OrderSubInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.order.po.OrderSubInfoPo;
import com.zsyc.order.vo.OrderSubInfoVo;
import com.zsyc.store.entity.StoreDeliveryRelation;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单子表  Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-14
 */
public interface OrderSubInfoMapper extends BaseMapper<OrderSubInfoPo> {

    /**
     * 创建子订单
     * @param orderSubInfoVos
     * @return List<OrderSubInfo> 返回批量插入数据的自增id
     */
    void createOrderSub(List<OrderSubInfoVo> orderSubInfoVos);

    /**
     * 在返回批量插入数据的自增id里，根据店铺id获取子订单id
     * @param map
     * @return
     */
    Long getOrderSubIdByStoreId(Map map);


    /**
     * 删除子订单
     * @return
     */
    int delOrderSub(Map map);

    /**
     * 取消子订单
     * @return
     */
    int updateOrderSubStatusByIds(Map map);

    /**
     * 更新子订单状态
     */
    void updateOrderSubStatus(Map<String,Object> map);

    /**
     * 催单
     */
    void orderReminder(Map map);

    /**
     * 催单(后台)
     */
    void orderReminderBack(Map map);


    /**
     * 根据时间段获取主订单（给店铺提供的接口）
     * @param orderSubInfoVo
     * @return
     */
    List<OrderSubInfoPo> getOrderinfosByTimeSlot(OrderSubInfoVo orderSubInfoVo);

    /**
     * 全部子订单(已分页)
     * @param page
     * @param createUserId
     * @param type
     * @return
     */
    IPage<OrderSubInfoPo> getOrderSubinfos(IPage<OrderSubInfoPo> page, @Param("createUserId") Long createUserId,@Param("type") String type);

    /**
     * 根据子订单Id获取数据(子订单详情)
     * @param id
     * @return
     */
    OrderSubInfoPo getOrderSubById(Long id);


    /**
     * 获取会员所有订单状态对应的数量
     * @param memberId
     * @param status
     * @return
     */
    int getAllOrderStatusCount(@Param("memberId") Long memberId,@Param("status") String status);


    /**
     * 获取店铺旧换新的订单(后台)
     * @param page
     * @param orderSubInfoVo
     * @return
     */
    IPage<OrderSubInfoPo> getOldforNewOrderByStoreId(IPage<OrderSubInfoPo> page,@Param("orderSubInfoVo") OrderSubInfoVo orderSubInfoVo);


    /**
     * 修改子订单数据 ==>旧换新
     * @param orderSubInfoVo
     */
    void updateOrderSubByOldforNew(@Param("orderSubInfoVo") OrderSubInfoVo orderSubInfoVo);


    /**
     * 从订单中获取生成订单结算报表需要的数据
     * @return
     */
    List<OrderSubInfoPo> getOrderDataForReport(@Param("now") LocalDateTime now);


    /**
     * 修改子订单数据(后台)
     * @param orderSubInfoVo
     */
    void updateOrdersubData(@Param("orderSubInfoVo") OrderSubInfoVo orderSubInfoVo);


    /**
     * 接单列表（配送端）
     * @param page
     * @param storeDeliveryRelations
     * @return
     */
    IPage<OrderSubInfoPo> getOrderTakingList(IPage<OrderSubInfoPo> page,@Param("storeDeliveryRelations") List<StoreDeliveryRelation> storeDeliveryRelations);


    /**
     * 配送员接单（配送端）
     * @param orderSubInfoVo
     */
    void orderTaking(@Param("orderSubInfoVo") OrderSubInfoVo orderSubInfoVo);


    /**
     * 送单列表和完成列表（配送端）
     * @param orderSubInfoVo
     * @return
     */
    IPage<OrderSubInfoPo> getOrderByPostmanIdAndStatus(IPage<OrderSubInfoPo> page,@Param("orderSubInfoVo") OrderSubInfoVo orderSubInfoVo);


    /**
     * 把协议号insert进子订单表
     */
    void updateProtocolNoOfOrderSub(@Param("protocolNo") String protocolNo,@Param("orderId") Long orderId);


    /**
     * 获取新租瓶的订单列表（后台）
     * @return
     */
    IPage<OrderSubInfoPo> getNewOrderList(IPage<OrderSubInfoPo> page,@Param("storeId") Long storeId);
}
