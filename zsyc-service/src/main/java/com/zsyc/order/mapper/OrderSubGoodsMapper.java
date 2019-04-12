package com.zsyc.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.order.entity.OrderSubGoods;
import com.zsyc.order.po.OrderSubGoodsPo;

import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 订单子商品快照
 * @author: Mr.Ning
 * @create: 2019-02-11 09:21
 **/

public interface OrderSubGoodsMapper extends BaseMapper<OrderSubGoodsPo> {

    /**
     * 创建商品快照
     */
    void createOrderSubGoods(List<OrderSubGoods> ls);
}
