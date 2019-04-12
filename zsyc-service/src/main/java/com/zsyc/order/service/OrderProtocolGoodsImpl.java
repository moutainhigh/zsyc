package com.zsyc.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsyc.common.AssertExt;
import com.zsyc.goods.service.GoodsStorePriceService;
import com.zsyc.goods.vo.GoodsPriceInfoVO;
import com.zsyc.order.mapper.OrderProtocolGoodsMapper;
import com.zsyc.order.po.OrderProtocolGoodsPo;
import com.zsyc.order.po.OrderProtocolInfoPo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: zsyc-parent
 * @description: 协议商品
 * @author: Mr.Ning
 * @create: 2019-02-26 17:37
 **/
@Service
public class OrderProtocolGoodsImpl implements OrderProtocolGoodsService {

    @Resource
    private OrderProtocolGoodsMapper orderProtocolGoodsMapper;

    @Resource
    private OrderProtocolInfoService orderProtocolInfoService;

    @Resource
    private GoodsStorePriceService goodsStorePriceService;

    /**
     * 获取协议商品
     * @param map
     * @return
     */
    @Override
    public List<OrderProtocolGoodsPo> getOrderProtocolGoods(Map map) {
        String protocolNo = String.valueOf(map.get("protocolNo"));
        AssertExt.notBlank(protocolNo, "协议号不能为空");
        List<OrderProtocolGoodsPo> orderProtocolGoodsPos = orderProtocolGoodsMapper.selectList(new QueryWrapper<OrderProtocolGoodsPo>()
                .eq("protocol_no", protocolNo));
        return orderProtocolGoodsPos;
    }


    /**
     * 退瓶时更新商品数量
     * @param id
     * @param num
     */
    @Override
    public void updateGoodsNum(Long id, Integer num) {
        orderProtocolGoodsMapper.updateGoodsNum(id,num );
    }

    /**
     * 根据id查找
     * @param id
     * @return
     */
    @Override
    public OrderProtocolGoodsPo getOrderProtocolGoodsById(Long id) {
        return orderProtocolGoodsMapper.selectById(id);
    }


    /**
     * 获取协议的商品列表(如果退瓶为0的排除)
     * @param map
     * @return
     */
    @Override
    public List<OrderProtocolGoodsPo> getProtocolGoods(Map map) {
        AssertExt.isFalse(map.get("protocolNo") == null || map.get("protocolNo").equals(""),"协议号不能为空" );
        String protocolNo = String.valueOf(map.get("protocolNo"));
        GoodsPriceInfoVO goodsPriceInfo;
        OrderProtocolInfoPo orderProtocolInfoPo = orderProtocolInfoService.getOrderProtocolInfoPoByProtocolNo(protocolNo);
        List<OrderProtocolGoodsPo> orderProtocolGoodsPos = orderProtocolGoodsMapper.selectList(new QueryWrapper<OrderProtocolGoodsPo>()
                .eq("protocol_no", protocolNo).ne("num", 0));
        for(OrderProtocolGoodsPo orderProtocolGoodsPo : orderProtocolGoodsPos){
            goodsPriceInfo = goodsStorePriceService.getGoodsPriceInfo(orderProtocolInfoPo.getStoreId(),orderProtocolInfoPo.getMemberAddressId(), orderProtocolGoodsPo.getSku());
            orderProtocolGoodsPo.setGoodsPriceInfoVO(goodsPriceInfo);

            orderProtocolGoodsPo.setPrice(orderProtocolGoodsPo.getPrice() / 100);
        }
        return orderProtocolGoodsPos;
    }
}
