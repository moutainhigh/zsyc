package com.zsyc.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.order.entity.OrderGoods;
import com.zsyc.order.po.OrderGoodsPo;
import com.zsyc.order.po.OrderSubInfoPo;
import com.zsyc.order.vo.OrderGoodsVo;
import com.zsyc.order.vo.OrderVo;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @program: zsyc-parent
 * @description: 订单商品快照
 * @author: Mr.Ning
 * @create: 2019-02-11 09:20
 **/

public interface OrderGoodsService {

    /**
     * 创建商品快照
     * @param orderVo
     * @param ids
     */
    void createOrderGoods(OrderVo orderVo, List<Long> ids);

    /**
     * 创建商品快照(用于退瓶订单)
     * @param ls
     */
    void createProtocolRefundOrderGoods(List<OrderGoods> ls);

    /**
     * 根据子订单id获取订单商品快照
     * @return
     */
    List<OrderGoodsPo> getOrderGoodsByOrderSubId(Long id);

    /**
     * 删除商品快照
     * @param ids
     * @return
     */
    int delOrderGoods(List<String> ids);


    /**
     * 更改商品快照状态(确认、缺货)
     * @param map
     */
    void updateOrderGoodsStatus(Map map);


    /**
     * 根据sku集合查询子订单id（会进行排重）
     * @param ls
     * @return
     */
    List<Long> getOrderSubIdsBySkus(List<String> ls);


    /**
     * 会员最近购买的商品
     * @param orderGoodsVo
     * @return
     */
    IPage<OrderGoodsPo> getLatelyGoodsByMember(OrderGoodsVo orderGoodsVo);


    /**
     * 从订单快照中获取生成订单结算报表需要的数据
     * @param storeId
     * @return
     */
    List<OrderGoodsPo> getOrderGoodsDataForReport(long storeId, LocalDateTime now);
}
