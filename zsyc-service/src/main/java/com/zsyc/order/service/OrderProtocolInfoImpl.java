package com.zsyc.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.common.AssertExt;
import com.zsyc.delivery.service.DeliveryService;
import com.zsyc.goods.service.GoodsStorePriceService;
import com.zsyc.goods.vo.GoodsPriceInfoVO;
import com.zsyc.member.entity.MemberAddress;
import com.zsyc.member.mapper.MemberAddressMapper;
import com.zsyc.member.service.MemberInfoService;
import com.zsyc.order.entity.OrderProtocolInfo;
import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.order.mapper.OrderProtocolGoodsMapper;
import com.zsyc.order.mapper.OrderProtocolInfoMapper;
import com.zsyc.order.mapper.OrderSubGoodsMapper;
import com.zsyc.order.mapper.OrderSubInfoMapper;
import com.zsyc.order.po.*;
import com.zsyc.order.utils.OrderUtil;
import com.zsyc.order.vo.OrderProtocolInfoVo;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @program: zsyc-parent
 * @description: 协议
 * @author: Mr.Ning
 * @create: 2019-02-26 17:37
 **/
@Service
public class OrderProtocolInfoImpl implements OrderProtocolInfoService {

    @Resource
    private OrderProtocolInfoMapper orderProtocolInfoMapper;

    @Resource
    private OrderProtocolGoodsService orderProtocolGoodsService;

    @Resource
    private MemberInfoService memberInfoService;

    @Resource
    private OrderSubInfoService orderSubInfoService;

    @Resource
    private OrderGoodsService orderGoodsService;

    @Resource
    private DeliveryService deliveryService;

    @Resource
    private GoodsStorePriceService goodsStorePriceService;

    @Resource
    private OrderProtocolGoodsMapper orderProtocolGoodsMapper;

    @Resource
    private OrderSubInfoMapper orderSubInfoMapper;

    @Resource
    private OrderSubGoodsMapper orderSubGoodsMapper;


    /**
     * 查询会员协议（已分页）
     *
     * @return
     */
    @Override
    public IPage<OrderProtocolInfoPo> getOrderProtocolInfo(Map map) {
        AssertExt.isFalse(map.get("memberId") == null || "".equals(map.get("memberId")), "会员id不能为空");
        AssertExt.isFalse("".equals(map.get("current")), "当前页码为空");
        AssertExt.isFalse("".equals(map.get("size")), "页码大小为空");
        String memberId = String.valueOf(map.get("memberId"));
        Object currentObject = map.get("current");
        Object sizeObject = map.get("size");
        if(currentObject == null){
            currentObject = 1;
        }
        if(sizeObject == null){
            sizeObject = 10;
        }
        List<MemberAddress> memberAddressList = memberInfoService.getMemberAddressList(Long.parseLong(memberId));
        IPage<OrderProtocolInfoPo> page = new Page(Long.parseLong(currentObject.toString()), Long.parseLong(sizeObject.toString()));
        IPage<OrderProtocolInfoPo> orderProtocolInfoPoIPage = orderProtocolInfoMapper.getOrderProtocolInfo(page, memberAddressList);
        List<OrderProtocolInfoPo> orderProtocolInfoPos = orderProtocolInfoPoIPage.getRecords();
        Map params = new HashMap();
        MemberAddress memberAddress;
        for (OrderProtocolInfoPo orderProtocolInfoPo : orderProtocolInfoPos) {
            memberAddress = memberInfoService.getMemberAddressById(orderProtocolInfoPo.getMemberAddressId());
            OrderSubInfoPo orderSubInfoPo = orderSubInfoService.getOrderSubByProtocolNo(orderProtocolInfoPo.getProtocolNo());
            orderProtocolInfoPo.setOrderProtocolTime(orderSubInfoPo.getCreateTime());
            orderProtocolInfoPo.setMemberAddress(memberAddress.getAddress());   //地址
            params.put("protocolNo", orderProtocolInfoPo.getProtocolNo());
            List<OrderProtocolGoodsPo> orderProtocolGoods = orderProtocolGoodsService.getOrderProtocolGoods(params);
            //获取该协议当前瓶数
            int currentBottleNum = 0;
            for (OrderProtocolGoodsPo orderProtocolGoodsPo : orderProtocolGoods) {
                currentBottleNum += orderProtocolGoodsPo.getNum();
            }
            orderProtocolInfoPo.setCurrentBottleNum(currentBottleNum);
            orderProtocolInfoPo.setDepositAmount(orderProtocolInfoPo.getDepositAmount() / 100);
            orderProtocolInfoPo.setDepositCurrent(orderProtocolInfoPo.getDepositCurrent() / 100);
            if(currentBottleNum == 0){
                orderProtocolInfoPo.setStatus("已完成");
            }else {
                orderProtocolInfoPo.setStatus("使用中");
            }
            orderProtocolInfoPo.setOrderProtocolGoodsPos(orderProtocolGoods);
        }
        return orderProtocolInfoPoIPage;

    }


    /**
     * 查询会员的全部协议（不分页）
     * @param memberId
     * @return
     */
    @Override
    public List<OrderProtocolInfoPo> getOrderProtocolInfo(Long memberId) {
        List<MemberAddress> memberAddressList = memberInfoService.getMemberAddressList(memberId);
        List<OrderProtocolInfoPo> orderProtocolInfoPos = orderProtocolInfoMapper.getOrderProtocolInfo(memberAddressList);
        Map params = new HashMap();
        MemberAddress memberAddress;
        for (OrderProtocolInfoPo orderProtocolInfoPo : orderProtocolInfoPos) {
            memberAddress = memberInfoService.getMemberAddressById(orderProtocolInfoPo.getMemberAddressId());
            orderProtocolInfoPo.setMemberAddress(memberAddress.getAddress());   //地址
            params.put("protocolNo", orderProtocolInfoPo.getProtocolNo());
            List<OrderProtocolGoodsPo> orderProtocolGoods = orderProtocolGoodsService.getOrderProtocolGoods(params);
            //获取该协议当前瓶数
            int currentBottleNum = 0;
            for (OrderProtocolGoodsPo orderProtocolGoodsPo : orderProtocolGoods) {
                currentBottleNum += orderProtocolGoodsPo.getNum();
            }
            orderProtocolInfoPo.setCurrentBottleNum(currentBottleNum);
            orderProtocolInfoPo.setOrderProtocolGoodsPos(orderProtocolGoods);
        }
        return orderProtocolInfoPos;
    }


    /**
     * 协议详情
     * @param map
     * @return
     */
    @Override
    public OrderProtocolInfoPo protocolDetails(Map map) {
        AssertExt.isFalse(map.get("protocolNo") == null || "".equals(map.get("protocolNo")), "协议号不能为空");
        String protocolNo = String.valueOf(map.get("protocolNo"));
        OrderProtocolInfoPo orderProtocolInfoPo = orderProtocolInfoMapper.selectOne(new QueryWrapper<OrderProtocolInfoPo>()
                .eq("protocol_no", protocolNo));
        OrderSubInfoPo orderSubInfoPo = orderSubInfoService.getOrderSubByProtocolNo(orderProtocolInfoPo.getProtocolNo());
        orderProtocolInfoPo.setOrderProtocolTime(orderSubInfoPo.getCreateTime());
        MemberAddress memberAddress = memberInfoService.getMemberAddressById(orderProtocolInfoPo.getMemberAddressId());
        orderProtocolInfoPo.setMemberAddress(memberAddress.getAddress());   //地址
        map.clear();
        map.put("protocolNo", orderProtocolInfoPo.getProtocolNo());
        List<OrderProtocolGoodsPo> orderProtocolGoodsPos = orderProtocolGoodsService.getOrderProtocolGoods(map);
        GoodsPriceInfoVO goodsPriceInfoVO;
        int currentBottleNum = 0;
        for(OrderProtocolGoodsPo orderProtocolGoodsPo : orderProtocolGoodsPos){
            //商品模块提供接口：根据sku返回商品信息
            goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderProtocolInfoPo.getStoreId(), orderProtocolInfoPo.getMemberAddressId(), orderProtocolGoodsPo.getSku());

            orderProtocolGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);
            orderProtocolGoodsPo.setPrice(orderProtocolGoodsPo.getPrice() / 100);

            currentBottleNum += orderProtocolGoodsPo.getNum();
        }
        orderProtocolInfoPo.setCurrentBottleNum(currentBottleNum);
        orderProtocolInfoPo.setOrderProtocolGoodsPos(orderProtocolGoodsPos);
        orderProtocolInfoPo.setDepositAmount(orderProtocolInfoPo.getDepositAmount() / 100);
        orderProtocolInfoPo.setDepositCurrent(orderProtocolInfoPo.getDepositCurrent() / 100);
        return orderProtocolInfoPo;
    }


    /**
     * 协议详情（获取某个协议的退瓶订单列表）
     *
     * @param map
     * @return
     */
    @Override
    public IPage<OrderSubInfoPo> getProtocolRefundOrder(Map map) {
        AssertExt.isFalse(map.get("protocolNo") == null || "".equals(map.get("protocolNo")), "协议号不能为空");
        AssertExt.isFalse("".equals(map.get("current")), "当前页码为空");
        AssertExt.isFalse("".equals(map.get("size")), "页码大小为空");
        String protocolNo = String.valueOf(map.get("protocolNo"));
        Object currentObject = map.get("current");
        Object sizeObject = map.get("size");
        if(currentObject == null){
            currentObject = 1;
        }
        if(sizeObject == null){
            sizeObject = 10;
        }
        IPage<OrderSubInfoPo> page = orderSubInfoService.getProtocolRefundOrder(Long.parseLong(currentObject.toString()), Long.parseLong(sizeObject.toString()), protocolNo);
        List<OrderSubInfoPo> orderSubInfoPos = page.getRecords();
        List<OrderGoodsPo> orderGoodsPos;
        GoodsPriceInfoVO goodsPriceInfoVO;
        for (OrderSubInfoPo orderSubInfoPo : orderSubInfoPos) {

            orderSubInfoPo.setDeliveryStaff(deliveryService.getDeliveryById(orderSubInfoPo.getPostmanId()));

            //拿到每个订单商品列表
            orderGoodsPos = orderGoodsService.getOrderGoodsByOrderSubId(orderSubInfoPo.getId());
            int bottleNum = 0;
            //为每个订单商品快照取到具体的商品信息
            for (OrderGoodsPo orderGoodsPo : orderGoodsPos) {
                //商品模块提供接口：根据sku返回商品信息
                goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());

                orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);

                bottleNum += orderGoodsPo.getNum();

                //金额：单位分转元
                orderGoodsPo.setPrice(orderGoodsPo.getPrice() / 100);
                orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
                orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
                orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);

            }
            orderSubInfoPo.setReturnBottleNum(bottleNum);
            orderSubInfoPo.setOrderStatus(OrderSubInfo.EOrderSubStatus.valueOf(orderSubInfoPo.getOrderStatus()).desc());

            //金额：单位分转元
            orderSubInfoPo.setAmount(orderSubInfoPo.getAmount() / 100);
            orderSubInfoPo.setExpressAmount(orderSubInfoPo.getExpressAmount() / 100);
            orderSubInfoPo.setRentAmount(orderSubInfoPo.getRentAmount() / 100);
            orderSubInfoPo.setRefundAmount(orderSubInfoPo.getRefundAmount() / 100);
            orderSubInfoPo.setDiscountAmount(orderSubInfoPo.getDiscountAmount() / 100);
            orderSubInfoPo.setActualAmount(orderSubInfoPo.getActualAmount() / 100);

            orderSubInfoPo.setOrderGoodsPos(orderGoodsPos); //把商品快照集合设置到子订单
        }
        return page;
    }

    /**
     * 更新协议当前押金
     * @param protocolNo
     * @param amount
     */
    @Override
    public void updateDepositCurrent(String protocolNo, int amount) {
        orderProtocolInfoMapper.updateDepositCurrent(protocolNo,amount );
    }


    /**
     * 根据协议号查找协议信息
     * @param protocolNo
     * @return
     */
    @Override
    public OrderProtocolInfoPo getOrderProtocolInfoPoByProtocolNo(String protocolNo) {
        OrderProtocolInfoPo orderProtocolInfoPo = orderProtocolInfoMapper.selectOne(new QueryWrapper<OrderProtocolInfoPo>().eq("protocol_no", protocolNo));
        Map map = new HashMap();
        map.put("protocolNo", orderProtocolInfoPo.getProtocolNo());
        orderProtocolInfoPo.setOrderProtocolGoodsPos(orderProtocolGoodsService.getOrderProtocolGoods(map));
        return orderProtocolInfoPo;
    }

    /**
     * 查看店铺所有用户的协议
     * @param orderProtocolInfoVo
     * @return
     */
    @Override
    public IPage<OrderProtocolInfoPo> getAllProtocolByStoreId(OrderProtocolInfoVo orderProtocolInfoVo) {
        AssertExt.notNull(orderProtocolInfoVo.getStoreId(), "店铺id为空");
        IPage<OrderProtocolInfoPo> page = new Page<>(orderProtocolInfoVo.getCurrent(),orderProtocolInfoVo.getSize());
        IPage<OrderProtocolInfoPo> orderProtocolInfoPoIPage = orderProtocolInfoMapper.getAllProtocolByStoreId(page, orderProtocolInfoVo);
        List<OrderProtocolInfoPo> orderProtocolInfoPos = orderProtocolInfoPoIPage.getRecords();

        Map params = new HashMap();
        MemberAddress memberAddress;
        for (OrderProtocolInfoPo orderProtocolInfoPo : orderProtocolInfoPos) {
            memberAddress = memberInfoService.getMemberAddressById(orderProtocolInfoPo.getMemberAddressId());
            OrderSubInfoPo orderSubInfoPo = orderSubInfoService.getOrderSubByProtocolNo(orderProtocolInfoPo.getProtocolNo());
            orderProtocolInfoPo.setOrderProtocolTime(orderSubInfoPo.getCreateTime());
            orderProtocolInfoPo.setMemberAddress(memberAddress.getAddress());   //地址
            params.put("protocolNo", orderProtocolInfoPo.getProtocolNo());
            List<OrderProtocolGoodsPo> orderProtocolGoods = orderProtocolGoodsService.getProtocolGoods(params);
            //获取该协议当前瓶数
            int currentBottleNum = 0;
            for (OrderProtocolGoodsPo orderProtocolGoodsPo : orderProtocolGoods) {
                currentBottleNum += orderProtocolGoodsPo.getNum();
            }
            orderProtocolInfoPo.setCurrentBottleNum(currentBottleNum);
            orderProtocolInfoPo.setDepositAmount(orderProtocolInfoPo.getDepositAmount() / 100);
            orderProtocolInfoPo.setDepositCurrent(orderProtocolInfoPo.getDepositCurrent() / 100);
            if(currentBottleNum == 0){
                orderProtocolInfoPo.setStatus("已完成");
            }else {
                orderProtocolInfoPo.setStatus("使用中");
            }
            orderProtocolInfoPo.setOrderProtocolGoodsPos(orderProtocolGoods);
        }
        return orderProtocolInfoPoIPage;
    }


    /**
     * 更新最后一次缴纳租金时间
     * @param protocolNo
     * @param now
     */
    @Override
    public void updateOrderProtocolLastRentPaymentTime(String protocolNo, LocalDateTime now) {
        orderProtocolInfoMapper.updateOrderProtocolLastRentPaymentTime(protocolNo,now);
    }


    /**
     * 添加协议（后台）
     * @param orderProtocolInfoVo
     */
    @Transactional
    @Override
    public void addOrderProtocol(OrderProtocolInfoVo orderProtocolInfoVo) {
        AssertExt.notNull(orderProtocolInfoVo.getOrderId(),"子订单id为空" );
        AssertExt.notBlank(orderProtocolInfoVo.getProtocolName(),"协议名称为空" );
        AssertExt.notBlank(orderProtocolInfoVo.getType(),"协议类型为空" );
        AssertExt.checkEnum(OrderProtocolInfo.ProtocolType.class, orderProtocolInfoVo.getType(),"协议类型有误" );

        OrderSubInfoPo orderSubInfoPo = orderSubInfoMapper.selectById(orderProtocolInfoVo.getOrderId());

        //协议号
        String protocolNo = OrderUtil.getOrderNo();

        /**
         * 生成协议
         */
        OrderProtocolInfoPo orderProtocolInfoPo = new OrderProtocolInfoPo();
        orderProtocolInfoPo.setCreateUserId(orderSubInfoPo.getCreateUserId());
        orderProtocolInfoPo.setCreateTime(orderSubInfoPo.getCreateTime());
        orderProtocolInfoPo.setUpdateUserId(orderSubInfoPo.getCreateUserId());
        orderProtocolInfoPo.setUpdateTime(orderSubInfoPo.getCreateTime());
        orderProtocolInfoPo.setMemberAddressId(orderSubInfoPo.getAddressId());
        orderProtocolInfoPo.setStoreId(orderSubInfoPo.getStoreId());
        orderProtocolInfoPo.setProtocolName(orderProtocolInfoVo.getProtocolName());
        orderProtocolInfoPo.setProtocolNo(protocolNo);
        orderProtocolInfoPo.setType(orderProtocolInfoVo.getType());
        orderProtocolInfoPo.setLastRentPaymentTime(orderSubInfoPo.getCreateTime());


        /**
         * 把协议号insert进子订单表
         */
        orderSubInfoMapper.updateProtocolNoOfOrderSub(protocolNo,orderSubInfoPo.getId());

        int num = 0;
        int depositAmount = 0;
        OrderProtocolGoodsPo orderProtocolGoodsPo;


        List<OrderGoodsPo> orderGoodsPos = orderGoodsService.getOrderGoodsByOrderSubId(orderSubInfoPo.getId());
        //为每个订单商品快照取到具体的商品信息
        for (OrderGoodsPo orderGoodsPo : orderGoodsPos) {

            num += orderGoodsPo.getNum();   //总瓶数

            /**
             * 子商品信息
             */
            List<OrderSubGoodsPo> orderSubGoodsPos = orderSubGoodsMapper.selectList(new QueryWrapper<OrderSubGoodsPo>().eq("order_sub_id", orderSubInfoPo.getId()).eq("sku", orderGoodsPo.getSku()));

            for (OrderSubGoodsPo orderSubGoodsPo : orderSubGoodsPos) {
                //瓶和桶
                if(orderSubGoodsPo.getGoodsStyle() == 1 || orderSubGoodsPo.getGoodsStyle() == 5){

                    orderProtocolGoodsPo = new OrderProtocolGoodsPo();
                    orderProtocolGoodsPo.setCreateUserId(orderProtocolInfoPo.getCreateUserId());
                    orderProtocolGoodsPo.setCreateTime(orderProtocolInfoPo.getCreateTime());
                    orderProtocolGoodsPo.setUpdateUserId(orderProtocolInfoPo.getUpdateUserId());
                    orderProtocolGoodsPo.setUpdateTime(orderProtocolInfoPo.getUpdateTime());
                    orderProtocolGoodsPo.setProtocolNo(protocolNo);
                    orderProtocolGoodsPo.setNum(orderSubGoodsPo.getNum());
                    orderProtocolGoodsPo.setPrice(orderSubGoodsPo.getPrice());
                    orderProtocolGoodsPo.setSku(orderSubGoodsPo.getSkuSub());

                    orderProtocolGoodsMapper.insert(orderProtocolGoodsPo);

                    depositAmount += orderSubGoodsPo.getPrice() * orderSubGoodsPo.getNum(); //计算总押金
                }
            }
        }

        orderProtocolInfoPo.setNum(num);
        orderProtocolInfoPo.setDepositAmount(depositAmount); //总押金
        orderProtocolInfoPo.setDepositCurrent(depositAmount);    //当前押金
        orderProtocolInfoMapper.insert(orderProtocolInfoPo);
    }
}
