package com.zsyc.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zsyc.order.entity.OrderSubGoods;
import com.zsyc.order.mapper.OrderSubGoodsMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 订单子商品快照
 * @author: Mr.Ning
 * @create: 2019-02-11 09:21
 **/
@Service
public class OrderSubGoodsServiceImpl implements OrderSubGoodsService {

    @Resource
    private OrderSubGoodsMapper orderSubGoodsMapper;

    /**
     * 创建子商品快照
     * @param ls
     */
    @Override
    public void createOrderSubGoods(List<OrderSubGoods> ls) {
        orderSubGoodsMapper.createOrderSubGoods(ls);
    }
}
