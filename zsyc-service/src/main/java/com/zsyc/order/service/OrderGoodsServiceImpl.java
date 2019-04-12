package com.zsyc.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.common.AssertExt;
import com.zsyc.goods.service.GoodsStorePriceService;
import com.zsyc.goods.vo.GoodsPriceInfoVO;
import com.zsyc.order.entity.OrderGoods;
import com.zsyc.order.entity.OrderSubGoods;
import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.order.mapper.OrderGoodsMapper;
import com.zsyc.order.mapper.OrderSubGoodsMapper;
import com.zsyc.order.po.OrderGoodsPo;
import com.zsyc.order.vo.OrderGoodsVo;
import com.zsyc.order.vo.OrderSubInfoVo;
import com.zsyc.order.vo.OrderVo;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: zsyc-parent
 * @description: 订单商品快照
 * @author: Mr.Ning
 * @create: 2019-02-11 09:21
 **/
@Service
public class OrderGoodsServiceImpl implements OrderGoodsService {

    @Resource
    private OrderGoodsMapper orderGoodsMapper;

    @Resource
    private OrderSubGoodsService orderSubGoodsService;

    @Resource
    private OrderSubInfoService orderSubInfoService;

    @Resource
    private GoodsStorePriceService goodsStorePriceService;

    /**
     * 创建商品快照
     * @param orderVo
     * @param ids   新增子订单的自增id
     */
    @Override
    public void createOrderGoods(OrderVo orderVo, List<Long> ids) {

        /**
         * 取订单商品快照
         */
        List<OrderGoods> ls = new ArrayList<>();
        List<OrderSubGoods> orderSubGoodsList = new ArrayList<>();
        Map map = new HashMap();
        map.put("ls", ids);

        Long orderSubId;
        OrderSubGoods orderSubGoods;
        for(OrderSubInfoVo orderSubInfoVo : orderVo.getOrderSubInfoVos()){
            map.put("storeId", orderSubInfoVo.getStoreId());    //店铺id
            orderSubId = orderSubInfoService.getOrderSubIdByStoreId(map);   //在返回批量插入数据的自增id里，根据店铺id获取子订单id
           for(OrderGoodsVo orderGoodsVo : orderSubInfoVo.getOrderGoodsVos()){
               orderGoodsVo.setOrderSubId(orderSubId);//设置子订单id
               orderGoodsVo.setCreateUserId(orderVo.getMemberAddress().getMemberId());  //创建人id
               orderGoodsVo.setCreateTime(LocalDateTime.now()); //创建时间
               orderGoodsVo.setIsDel(0);    //是否删除
               ls.add(orderGoodsVo);

               if(orderGoodsVo.getGoodsPriceInfoVO().getGoodsType() == 2 && orderGoodsVo.getGoodsPriceInfoVO().getGoodsPriceInfoVOS() != null){
                   for(GoodsPriceInfoVO goodsPriceInfoVO : orderGoodsVo.getGoodsPriceInfoVO().getGoodsPriceInfoVOS()){
                       /**
                        * 子商品快照信息
                        */
                       orderSubGoods = new OrderSubGoods();
                       orderSubGoods.setOrderSubId(orderSubId);
                       orderSubGoods.setSku(orderGoodsVo.getGoodsPriceInfoVO().getSku());
                       orderSubGoods.setSkuSub(goodsPriceInfoVO.getSku());
                       orderSubGoods.setNum(orderGoodsVo.getNum());
                       orderSubGoods.setPrice((int)(double)(goodsPriceInfoVO.getPrice() * 100));
                       orderSubGoods.setGoodsStyle(goodsPriceInfoVO.getGoodsStyle());
                       orderSubGoods.setCreateUserId(orderVo.getMemberAddress().getMemberId());
                       orderSubGoods.setCreateTime(LocalDateTime.now());
                       orderSubGoods.setUpdateUserId(orderVo.getMemberAddress().getMemberId());
                       orderSubGoods.setUpdateTime(LocalDateTime.now());
                       orderSubGoodsList.add(orderSubGoods);
                   }
               }
           }
        }
        if(ls.size() != 0){
            orderGoodsMapper.createOrderGoods(ls);  //主商品快照insert
            if(orderSubGoodsList.size() != 0){     //子商品快照insert
                orderSubGoodsService.createOrderSubGoods(orderSubGoodsList);
            }
        }
    }

    /**
     * 创建商品快照(用于退瓶订单)
     * @param ls
     */
    @Override
    public void createProtocolRefundOrderGoods(List<OrderGoods> ls) {
        orderGoodsMapper.createOrderGoods(ls);
    }


    /**
     * 根据子订单id获取订单商品快照
     * @return
     */
    @Override
    public List<OrderGoodsPo> getOrderGoodsByOrderSubId(Long id) {
        return orderGoodsMapper.selectList(new QueryWrapper<OrderGoodsPo>().eq("order_sub_id", id).eq("is_del",0 ));
    }

    /**
     * 删除商品快照
     * @param ids
     * @return
     */
    @Override
    public int delOrderGoods(List<String> ids) {
        Map map = new HashMap();
        map.put("ids",ids);
        map.put("updateUserId", 1);
        map.put("updateTime", LocalDateTime.now());
        return orderGoodsMapper.delOrderGoods(map);
    }


    /**
     * 更改商品快照状态(确认、缺货)
     * @param map
     */
    @Override
    public void updateOrderGoodsStatus(Map map) {
        AssertExt.isFalse(map.get("orderGoodsId") == null || map.get("orderGoodsId").equals(""), "订单快照id不能为空");
        AssertExt.isFalse(map.get("status") == null || map.get("status").equals(""), "订单快照状态不能为空");
        Long orderGoodsId = Long.parseLong(map.get("orderGoodsId").toString());
        String status = String.valueOf(map.get("status"));
        AssertExt.checkEnum(OrderSubInfo.EOrderSubStatus.class, status, "子订单类型错误");

        orderGoodsMapper.updateStatusById(orderGoodsId,status);
    }


    /**
     * 根据sku集合查询子订单id（会进行排重）
     * @param ls
     * @return
     */
    @Override
    public List<Long> getOrderSubIdsBySkus(List<String> ls) {
        return orderGoodsMapper.getOrderSubIdsBySkus(ls);
    }


    /**
     * 会员最近购买的商品
     * @param orderGoodsVo
     * @return
     */
    @Override
    public IPage<OrderGoodsPo> getLatelyGoodsByMember(OrderGoodsVo orderGoodsVo) {
        AssertExt.isFalse(orderGoodsVo.getCurrent() == null, "当前页码为空");
        AssertExt.isFalse(orderGoodsVo.getSize() == null, "页码大小为空");
        AssertExt.isFalse(orderGoodsVo.getCreateUserId() == null, "会员id为空");

        IPage<OrderGoodsPo> page = new Page<>(orderGoodsVo.getCurrent(), orderGoodsVo.getSize());
        IPage<OrderGoodsPo> latelyGoodsByMember = orderGoodsMapper.getLatelyGoodsByMember(page, orderGoodsVo);
        GoodsPriceInfoVO goodsPriceInfoVO;
        for(OrderGoodsPo orderGoodsPo : latelyGoodsByMember.getRecords()){
            goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderGoodsPo.getStoreId(), orderGoodsPo.getAddressId(), orderGoodsPo.getSku());

            if(goodsPriceInfoVO.getGoodsType() == 2 && goodsPriceInfoVO.getGoodsPriceInfoVOS() != null){
                double amount1 = 0;
                for (GoodsPriceInfoVO goodsPriceInfoVO1 : goodsPriceInfoVO.getGoodsPriceInfoVOS()){
                    amount1 += goodsPriceInfoVO1.getPrice();
                }
                goodsPriceInfoVO.setPrice(amount1);
            }
            orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);
            //金额：单位分转元
            orderGoodsPo.setPrice(orderGoodsPo.getPrice() / 100);
            orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
            orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
            orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);
        }
        return latelyGoodsByMember;
    }


    /**
     * 从订单快照中获取生成订单结算报表需要的数据
     * @return
     */
    @Override
    public List<OrderGoodsPo> getOrderGoodsDataForReport(long storeId,LocalDateTime now) {
        return orderGoodsMapper.getOrderGoodsDataForReport(storeId,now);
    }
}
