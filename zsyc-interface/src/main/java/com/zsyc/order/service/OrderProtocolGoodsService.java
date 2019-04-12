package com.zsyc.order.service;

import com.zsyc.order.entity.OrderProtocolGoods;
import com.zsyc.order.po.OrderGoodsPo;
import com.zsyc.order.po.OrderProtocolGoodsPo;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

/**
 * @program: zsyc-parent
 * @description: 协议商品
 * @author: Mr.Ning
 * @create: 2019-02-26 17:32
 **/

public interface OrderProtocolGoodsService {

    /**
     * 获取协议商品
     * @param map
     * @return
     */
    List<OrderProtocolGoodsPo> getOrderProtocolGoods(Map map);


    /**
     * 退瓶时更新商品数量
     * @param id
     * @param num
     */
    void updateGoodsNum(Long id, Integer num);


    /**
     * 根据id查找
     * @param id
     * @return
     */
    OrderProtocolGoodsPo getOrderProtocolGoodsById(Long id);

    /**
     * 获取协议的商品列表(如果退瓶为0的排除)
     * @param map
     * @return
     */
    List<OrderProtocolGoodsPo> getProtocolGoods(Map map);
}
