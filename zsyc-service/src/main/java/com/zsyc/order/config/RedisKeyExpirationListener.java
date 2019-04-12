package com.zsyc.order.config;

/**
 * @program: zsyc-parent
 * @description: 定义监听器，实现KeyExpirationEventMessageListener接口
 * @author: Mr.Ning
 * @create: 2019-02-27 10:02
 **/

import com.alibaba.dubbo.config.annotation.Reference;
import com.zsyc.goods.service.GoodsStorePriceService;
import com.zsyc.goods.vo.GoodsPriceInfoVO;
import com.zsyc.order.entity.OrderInfo;
import com.zsyc.order.po.OrderGoodsPo;
import com.zsyc.order.po.OrderInfoPo;
import com.zsyc.order.po.OrderSubInfoPo;
import com.zsyc.order.service.OrderInfoService;
import com.zsyc.order.service.OrderSubInfoService;
import com.zsyc.order.vo.StockVo;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.store.service.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 监听所有db的过期事件__keyevent@*__:expired"
 */
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private GoodsStorePriceService goodsStorePriceService;

    @Resource
    private StoreInfoService storeInfoService;

    @Resource
    private RedisTemplate redisTemplate;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }


    /**
     * 针对redis数据失效事件，进行数据处理
     * @param message
     * @param pattern
     */
    @Override
    @Transactional
    public void onMessage(Message message, byte[] pattern) {
        // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
        String expiredKey = message.toString();
        if(expiredKey.startsWith("orderInfoId:")){
            String orderId = expiredKey.substring(12); //去掉orderId
            Map map = new HashMap();
            map.put("orderId",orderId);
            OrderInfoPo orderInfoPo = orderInfoService.getOrderInfoById(map);
            //时间到期尚未支付
            if(orderInfoPo.getOrderStatus().equals(OrderInfo.EOrderStatus.UNPAID.toString())){
                redisTemplate.opsForValue().set("orderId_unpaid_operation:" + orderId,orderId);
            }
        }
    }
}
