package com.zsyc.order.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.order.entity.OrderGoods;
import com.zsyc.order.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.order.po.OrderInfoPo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单主表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-14
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfoPo> {

    IPage<OrderInfoPo> getOrderinfos(IPage<OrderInfoPo> page, @Param("createUserId") Long createUserId);

    /**
     * 获取会员所有订单状态对应的数量
     *
     * @param memberId
     * @param status
     * @return
     */
    int getAllOrderStatusCount(@Param("memberId") Long memberId, @Param("status") String status);


    /**
     * 更新主订单状态
     *
     * @param memberId
     * @param status
     * @param now
     */
    void updateOrderInfoStatusById(@Param("orderId") Long orderId, @Param("memberId") Object memberId, @Param("status") String status, @Param("now") LocalDateTime now);


    /**
     * 删除主订单
     */
    void delOrderInfo(@Param("orderId") Long orderId, @Param("memberId") Long memberId, @Param("now") LocalDateTime now);//删除主订单


    /**
     * 修改主订单数据
     * @param orderNo
     * @param amount
     * @param actualAmount
     * @param now
     */
    void updateOrderByOldforNew(@Param("orderNo") String orderNo,@Param("amount") int amount,@Param("actualAmount") int actualAmount,@Param("now") LocalDateTime now);


    /**
     * 支付回调修改订单数据
     * @param orderInfoPo
     */
    void callback(@Param("orderInfoPo") OrderInfoPo orderInfoPo);
}
