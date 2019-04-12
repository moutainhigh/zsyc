package com.zsyc.order.service;

import com.zsyc.order.entity.OrderSubGoods;

import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 订单子商品快照
 * @author: Mr.Ning
 * @create: 2019-02-11 09:20
 **/

public interface OrderSubGoodsService {

    /**
     * 创建子商品快照
     */
    void createOrderSubGoods(List<OrderSubGoods> ls);
}
