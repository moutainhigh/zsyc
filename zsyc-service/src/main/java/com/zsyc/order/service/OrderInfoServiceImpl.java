package com.zsyc.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.account.entity.AccountBind;
import com.zsyc.account.service.AccountService;
import com.zsyc.common.AssertExt;

import com.zsyc.goods.service.GoodsStorePriceService;
import com.zsyc.goods.vo.GoodsAttributeValueVO;
import com.zsyc.goods.vo.GoodsPriceInfoVO;
import com.zsyc.member.entity.MemberAddress;
import com.zsyc.member.service.MemberInfoService;
import com.zsyc.order.entity.OrderInfo;
import com.zsyc.order.entity.OrderProtocolInfo;
import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.order.mapper.OrderInfoMapper;
import com.zsyc.order.po.OrderGoodsPo;
import com.zsyc.order.po.OrderInfoPo;
import com.zsyc.order.po.OrderProtocolInfoPo;
import com.zsyc.order.po.OrderSubInfoPo;
import com.zsyc.order.utils.OrderUtil;
import com.zsyc.order.vo.*;
import com.zsyc.pay.service.PayOrderService;
import com.zsyc.pay.vo.PayOrderVo;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.store.service.StoreInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @program: zsyc-parent
 * @description: 订单
 * @author: Mr.Ning
 * @create: 2019-01-15 14:30
 **/
@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    private static final Logger logger = LoggerFactory.getLogger(OrderInfoServiceImpl.class);

    //#待支付时间,若时间到期但还未支付，则取消订单
    @Value("${order.unpaid.time}")
    private Long orderUnpaidTime;

    @Value("${pay.dataSource}")
    private String dataSource;

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private OrderSubInfoService orderSubInfoService;

    @Resource
    private OrderGoodsService orderGoodsService;

    @Resource
    private OrderProtocolInfoService orderProtocolInfoService;

    @Resource
    private StoreInfoService storeInfoService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private MemberInfoService memberInfoService;

    @Resource
    private GoodsStorePriceService goodsStorePriceService;

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private AccountService accountService;


    /**
     * 定时扫描redis，对未支付的订单进行处理
     */
    @Transactional
    @Scheduled(cron = "${order.unpaid.operation}")
    public void orderUnpaidOperation() {
        Set keys = redisTemplate.keys("orderId_unpaid_operation*");
        Iterator iterator = keys.iterator();
        GoodsPriceInfoVO goodsPriceInfoVO;
        List<StockVo> stockVos;
        StockVo stockVo;
        StoreInfo storeInfo;
        Map map;
        String orderId;
        boolean isGas = true;
        OrderInfoPo orderInfoPo;
        while (iterator.hasNext()) {
            orderId = iterator.next().toString().substring(25);
            stockVos = new ArrayList<>();
            //从redis删除已处理的orderId
            redisTemplate.delete("orderId_unpaid_operation:" + orderId);
            map = new HashMap();
            map.put("orderId", orderId);
            orderInfoPo = getOrderInfoById(map);
            for (OrderSubInfoPo orderSubInfoPo : orderInfoPo.getOrderSubInfoPos()) {
                storeInfo = storeInfoService.getStoreById(orderSubInfoPo.getStoreId());
                if (storeInfo.getStoreTypeId() == 0) isGas = false;
                for (OrderGoodsPo orderGoodsPo : orderSubInfoPo.getOrderGoodsPos()) {
                    goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());
                    if (goodsPriceInfoVO.getGoodsType() == 1) {
                        stockVo = new StockVo();
                        stockVo.setSku(goodsPriceInfoVO.getSku());
                        stockVo.setNum(orderGoodsPo.getNum());
                        stockVo.setStoreId(orderSubInfoPo.getStoreId());
                        stockVos.add(stockVo);
                    }

                    if (goodsPriceInfoVO.getGoodsType() == 2 && goodsPriceInfoVO.getGoodsPriceInfoVOS() == null) {
                        stockVo = new StockVo();
                        stockVo.setSku(goodsPriceInfoVO.getSku());
                        stockVo.setNum(orderGoodsPo.getNum());
                        stockVo.setStoreId(orderSubInfoPo.getStoreId());
                        stockVos.add(stockVo);
                    }

                    if (goodsPriceInfoVO.getGoodsType() == 2 && goodsPriceInfoVO.getGoodsPriceInfoVOS() != null) {
                        for (GoodsPriceInfoVO goodsPriceInfoVO2 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                            stockVo = new StockVo();
                            stockVo.setSku(goodsPriceInfoVO2.getSku());
                            stockVo.setNum(orderGoodsPo.getNum());
                            stockVo.setStoreId(orderSubInfoPo.getStoreId());
                            stockVos.add(stockVo);
                        }
                    }
                }
            }

            //时间到期尚未支付
            if (orderInfoPo.getOrderStatus().equals(OrderInfo.EOrderStatus.UNPAID.toString())) {
                Map params = new HashMap();
                params.put("orderId", orderId);
                params.put("memberId", orderInfoPo.getCreateUserId());
                params.put("status", OrderInfo.EOrderStatus.CANCEL.toString());
                updateOrderStatus(params);

                /**
                 * 恢复库存
                 */
                if (isGas) {
                    //不是气店才会扣库存
                    storeInfoService.storeGoodStock(stockVos, 1, 0l);
                }
            }
        }
    }


    /**
     * 创建主订单
     */
    @Override
    @Transactional      //事务
    public String createOrderInfo(OrderVo orderVo) {
        AssertExt.notNull(orderVo.getAddressId(), "用户地址id不能为空");
        AssertExt.notNull(orderVo.getOrderCategory(), "订单类型不能为空");
        AssertExt.notNull(orderVo.getGasOrderCategory(), "气订单类型不能为空");
        AssertExt.notEmpty(orderVo.getOrderSubInfoVos(), "子订单为空&不能没有子订单");
        if (orderVo.getGasOrderCategory() != 3) {
            AssertExt.checkEnum(OrderInfo.EPayType.class, orderVo.getPayType(), "支付方式不正确");
        }

        for (OrderSubInfoVo orderSubInfoVo : orderVo.getOrderSubInfoVos()) {
            AssertExt.notNull(orderSubInfoVo.getStoreId(), "店铺id不能为空");
            AssertExt.checkEnum(OrderSubInfo.EDeliveryType.class, orderSubInfoVo.getDeliveryType(), "配送方式不明确");
            if (orderSubInfoVo.getDeliveryType().equals(OrderSubInfo.EDeliveryType.SELF.toString())) {     //如果是自提则需要时间，非自提不需要
                AssertExt.notNull(orderSubInfoVo.getBookStartTime(), "自提时间段不能为空");
                AssertExt.notNull(orderSubInfoVo.getBookEndTime(), "自提时间段不能为空");
            }

            AssertExt.notNull(orderSubInfoVo.getOrderGoodsVos(), "商品不能为null");
        }

        MemberAddress memberAddress = memberInfoService.getMemberAddressById(orderVo.getAddressId());

        orderVo.setMemberAddress(memberAddress);
        StoreInfo storeInfo = new StoreInfo();
        GoodsPriceInfoVO goodsPriceInfoVO;
        List<StockVo> stockVos = new ArrayList<>();
        StockVo stockVo;
        for (OrderSubInfoVo orderSubInfoVo : orderVo.getOrderSubInfoVos()) {
            storeInfo = storeInfoService.getStoreById(orderSubInfoVo.getStoreId());
            orderSubInfoVo.setStoreInfo(storeInfo);
            for (OrderGoodsVo orderGoodsVo : orderSubInfoVo.getOrderGoodsVos()) {
                /**
                 * 商品模块提供接口:根据sku取到商品
                 */
                goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoVo.getStoreId(), orderVo.getAddressId(), orderGoodsVo.getSku());
                if (goodsPriceInfoVO.getGoodsType() == 2 && goodsPriceInfoVO.getGoodsPriceInfoVOS() != null) {
                    double amount1 = 0;
                    for (GoodsPriceInfoVO goodsPriceInfoVO1 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                        amount1 += goodsPriceInfoVO1.getPrice();
                    }
                    goodsPriceInfoVO.setPrice(amount1);
                }

                orderGoodsVo.setGoodsPriceInfoVO(goodsPriceInfoVO);

                //如果是非组合商品，则设置冗余单位
                if (goodsPriceInfoVO.getGoodsType() == 1) {
                    for (GoodsAttributeValueVO goodsAttributeValueVO : goodsPriceInfoVO.getGoodsAttributeValueVOS()) {
                        if (goodsAttributeValueVO.getIsSale() == 1) {
                            orderGoodsVo.setUnit(goodsAttributeValueVO.getAttrKeyName());
                        }
                    }
                }
                orderGoodsVo.setPrice((int) (double) (goodsPriceInfoVO.getPrice() * 100));

                /**
                 * 应减库存数据
                 */
                if (goodsPriceInfoVO.getGoodsType() == 1) {
                    stockVo = new StockVo();
                    stockVo.setSku(goodsPriceInfoVO.getSku());
                    stockVo.setNum(orderGoodsVo.getNum());
                    stockVo.setStoreId(orderSubInfoVo.getStoreId());
                    stockVos.add(stockVo);
                }

                if (goodsPriceInfoVO.getGoodsType() == 2 && goodsPriceInfoVO.getGoodsPriceInfoVOS() == null) {
                    stockVo = new StockVo();
                    stockVo.setSku(goodsPriceInfoVO.getSku());
                    stockVo.setNum(orderGoodsVo.getNum());
                    stockVo.setStoreId(orderSubInfoVo.getStoreId());
                    stockVos.add(stockVo);
                }

                if (goodsPriceInfoVO.getGoodsType() == 2 && goodsPriceInfoVO.getGoodsPriceInfoVOS() != null) {
                    for (GoodsPriceInfoVO goodsPriceInfoVO2 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                        stockVo = new StockVo();
                        stockVo.setSku(goodsPriceInfoVO2.getSku());
                        stockVo.setNum(orderGoodsVo.getNum());
                        stockVo.setStoreId(orderSubInfoVo.getStoreId());
                        stockVos.add(stockVo);
                    }
                }
            }
        }

        //主订单号
        String orderInfoNo = OrderUtil.getOrderNo();

        //先生成子订单
        //orderSubInfoVos：批量插入数据的自增id
        Map<String, Object> result = orderSubInfoService.createOrderSub(orderVo, orderInfoNo);

        List<OrderSubInfoVo> ls = (List<OrderSubInfoVo>) result.get("orderSubInfoVos");
        //将id取出
        List<Long> idList = new ArrayList<>();
        for (OrderSubInfoVo vo : ls) {
            idList.add(vo.getId());
        }


        //接着才生成订单商品快照
        orderGoodsService.createOrderGoods(orderVo, idList);

        //创建支付订单
        //支付订单数据
        OrderInfoPo orderInfoPo = new OrderInfoPo();
        orderInfoPo.setOrderNo(orderInfoNo); //订单号
        orderInfoPo.setAddressId(orderVo.getAddressId()); //地址
        orderInfoPo.setAmount(Integer.parseInt(result.get("amount").toString())); //总金额
        orderInfoPo.setActualAmount(Integer.parseInt(result.get("actualAmount").toString())); //实付金额
        orderInfoPo.setPayType(orderVo.getPayType());    //支付方式
        orderInfoPo.setSubOrderIds(StringUtils.join(idList.toArray(), ","));  //支付订单中的子订单ids
        if (OrderInfo.EPayType.OFFLINE.val().equals(orderVo.getPayType())) {
            //货到付款，直接支付成功
            orderInfoPo.setOrderStatus(OrderInfo.EOrderStatus.SUCCESS.toString());     //支付状态初始化
        } else {
            orderInfoPo.setOrderStatus(OrderInfo.EOrderStatus.UNPAID.toString());     //支付状态初始化
        }
        orderInfoPo.setCreateUserId(orderVo.getMemberAddress().getMemberId());    //创建人id
        orderInfoPo.setCreateTime(LocalDateTime.now());   //创建时间
        orderInfoPo.setUpdateUserId(orderVo.getMemberAddress().getMemberId());  //更新人id
        orderInfoPo.setUpdateTime(orderInfoPo.getCreateTime());     //更新时间（订单初始化时，更新时间==创建时间）
        orderInfoPo.setIsDel(0);  //是否删除
        orderInfoMapper.insert(orderInfoPo);

        /**
         * 减库存  ===> 参数1代表增库存，2代表减库存
         */
        if (storeInfo.getStoreTypeId() != 0 && orderVo.getGasOrderCategory() != 3) {
            storeInfoService.storeGoodStock(stockVos, 2, 0l);
        }

        //将订单号插入缓存，设置30分钟过期，30分钟后若时间到期但还未支付，则取消订单
        //旧换新不用取消
        if (orderVo.getGasOrderCategory() != 3) {
            redisTemplate.opsForValue().set("orderInfoId:" + orderInfoPo.getId(), orderInfoPo.getId(), orderUnpaidTime, TimeUnit.SECONDS);
        }

        return orderInfoPo.getOrderNo();
    }


    public List<OrderInfo> test() {
        List<OrderInfo> orderInfos = new ArrayList<>(10);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(10L);
        orderInfo.setCreateTime(LocalDateTime.now());
        OrderInfo orderInfo2 = new OrderInfo();
        orderInfo2.setId(100L);
        orderInfo2.setCreateTime(LocalDateTime.now());
        orderInfos.add(orderInfo);
        orderInfos.add(orderInfo2);
        redisTemplate.opsForList().leftPushAll("list", orderInfos);
        Long size = redisTemplate.opsForList().size("list");
        List<OrderInfo> list = redisTemplate.opsForList().range("list", 0, -1);
        return list;
    }


    /**
     * 后台下单
     */
    @Override
    @Transactional      //事务
    public Long createOrderInfoBack(OrderVo orderVo) {
        AssertExt.notNull(orderVo.getGasOrderCategory(), "气订单类型为空");
        AssertExt.notNull(orderVo.getAddressId(), "地址为空");

        if (orderVo.getGasOrderCategory() == 3) {
            AssertExt.notBlank(orderVo.getPhoto(), "旧换新订单上传的图片地址为空");
        }

        AssertExt.isFalse(orderVo.getOrderSubInfoVos() == null || orderVo.getOrderSubInfoVos().size() == 0, "子订单为空");

        for (OrderSubInfoVo orderSubInfoVo : orderVo.getOrderSubInfoVos()) {
            AssertExt.notNull(orderSubInfoVo.getStoreId(), "店铺id为空");
            if (orderVo.getGasOrderCategory() != 3) {
                AssertExt.isFalse(orderSubInfoVo.getOrderGoodsVos() == null || orderSubInfoVo.getOrderGoodsVos().size() == 0, "商品为空");
                for (OrderGoodsVo orderGoodsVo : orderSubInfoVo.getOrderGoodsVos()) {
                    AssertExt.notBlank(orderGoodsVo.getSku(), "sku为空");
                    AssertExt.notNull(orderGoodsVo.getNum(), "数量为空");
                }
            }
        }

        MemberAddress memberAddress = memberInfoService.getMemberAddressById(orderVo.getAddressId());

        orderVo.setMemberAddress(memberAddress);

        orderVo.setPayType(OrderInfo.EPayType.OFFLINE.val());   //后台下单只能货到付款

        StoreInfo storeInfo = new StoreInfo();
        GoodsPriceInfoVO goodsPriceInfoVO;
        List<StockVo> stockVos = new ArrayList<>();
        StockVo stockVo;
        for (OrderSubInfoVo orderSubInfoVo : orderVo.getOrderSubInfoVos()) {
            storeInfo = storeInfoService.getStoreById(orderSubInfoVo.getStoreId());
            orderSubInfoVo.setDeliveryType(OrderSubInfo.EDeliveryType.EXPRESS.val());   //后台下单固定配送方式
            orderSubInfoVo.setStoreInfo(storeInfo);
            for (OrderGoodsVo orderGoodsVo : orderSubInfoVo.getOrderGoodsVos()) {
                /**
                 * 商品模块提供接口:根据sku取到商品
                 */
                goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoVo.getStoreId(), orderVo.getAddressId(), orderGoodsVo.getSku());
                if (goodsPriceInfoVO.getGoodsType() == 2 && goodsPriceInfoVO.getGoodsPriceInfoVOS() != null) {
                    double amount1 = 0;
                    for (GoodsPriceInfoVO goodsPriceInfoVO1 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                        amount1 += goodsPriceInfoVO1.getPrice();
                    }
                    goodsPriceInfoVO.setPrice(amount1);
                }

                orderGoodsVo.setGoodsPriceInfoVO(goodsPriceInfoVO);

                //如果是非组合商品，则设置冗余单位
                if (goodsPriceInfoVO.getGoodsType() == 1) {
                    for (GoodsAttributeValueVO goodsAttributeValueVO : goodsPriceInfoVO.getGoodsAttributeValueVOS()) {
                        if (goodsAttributeValueVO.getIsSale() == 1) {
                            orderGoodsVo.setUnit(goodsAttributeValueVO.getAttrKeyName());
                        }
                    }
                }
                orderGoodsVo.setPrice((int) (double) (goodsPriceInfoVO.getPrice() * 100));

                /**
                 * 应减库存数据
                 */
                if (goodsPriceInfoVO.getGoodsType() == 1) {
                    stockVo = new StockVo();
                    stockVo.setSku(goodsPriceInfoVO.getSku());
                    stockVo.setNum(orderGoodsVo.getNum());
                    stockVo.setStoreId(orderSubInfoVo.getStoreId());
                    stockVos.add(stockVo);
                }

                if (goodsPriceInfoVO.getGoodsType() == 2 && goodsPriceInfoVO.getGoodsPriceInfoVOS() == null) {
                    stockVo = new StockVo();
                    stockVo.setSku(goodsPriceInfoVO.getSku());
                    stockVo.setNum(orderGoodsVo.getNum());
                    stockVo.setStoreId(orderSubInfoVo.getStoreId());
                    stockVos.add(stockVo);
                }

                if (goodsPriceInfoVO.getGoodsType() == 2 && goodsPriceInfoVO.getGoodsPriceInfoVOS() != null) {
                    for (GoodsPriceInfoVO goodsPriceInfoVO2 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                        stockVo = new StockVo();
                        stockVo.setSku(goodsPriceInfoVO2.getSku());
                        stockVo.setNum(orderGoodsVo.getNum());
                        stockVo.setStoreId(orderSubInfoVo.getStoreId());
                        stockVos.add(stockVo);
                    }
                }
            }
        }

        //主订单号
        String orderInfoNo = OrderUtil.getOrderNo();

        //先生成子订单
        //orderSubInfoVos：批量插入数据的自增id
        Map<String, Object> result = orderSubInfoService.createOrderSubBack(orderVo, orderInfoNo);

        List<OrderSubInfoVo> ls = (List<OrderSubInfoVo>) result.get("orderSubInfoVos");
        //将id取出
        List<Long> idList = new ArrayList<>();
        for (OrderSubInfoVo vo : ls) {
            idList.add(vo.getId());
        }


        //接着才生成订单商品快照
        orderGoodsService.createOrderGoods(orderVo, idList);

        //创建支付订单
        //支付订单数据
        OrderInfoPo orderInfoPo = new OrderInfoPo();
        orderInfoPo.setOrderNo(orderInfoNo); //订单号
        orderInfoPo.setAddressId(orderVo.getAddressId()); //地址
        orderInfoPo.setAmount(Integer.parseInt(result.get("amount").toString())); //总金额
        orderInfoPo.setActualAmount(Integer.parseInt(result.get("actualAmount").toString())); //实付金额
        orderInfoPo.setPayType(orderVo.getPayType());    //支付方式(后台下单只能货到付款)
        orderInfoPo.setSubOrderIds(StringUtils.join(idList.toArray(), ","));  //支付订单中的子订单ids
        if (orderVo.getGasOrderCategory() == 3) {
            /**
             * 旧换新
             */
            orderInfoPo.setOrderStatus(OrderInfo.EOrderStatus.UNPAID.toString());     //支付状态初始化
        } else {
            orderInfoPo.setOrderStatus(OrderInfo.EOrderStatus.SUCCESS.toString());     //支付状态初始化
        }

        orderInfoPo.setCreateUserId(orderVo.getMemberAddress().getMemberId());    //创建人id
        orderInfoPo.setCreateTime(LocalDateTime.now());   //创建时间
        orderInfoPo.setUpdateUserId(orderVo.getMemberAddress().getMemberId());  //更新人id
        orderInfoPo.setUpdateTime(orderInfoPo.getCreateTime());     //更新时间（订单初始化时，更新时间==创建时间）
        orderInfoPo.setIsDel(0);  //是否删除
        orderInfoMapper.insert(orderInfoPo);

        /**
         * 减库存  ===> 参数1代表增库存，2代表减库存
         */
        if (storeInfo.getStoreTypeId() != 0 && orderVo.getGasOrderCategory() != 3) {
            storeInfoService.storeGoodStock(stockVos, 2, 0l);
        }

        return orderInfoPo.getId();
    }

    /**
     * 根据主订单状态获取数据(已分页)
     *
     * @param orderInfoVo
     * @return
     */
    @Override
    public IPage<OrderInfoPo> getOrderInfosByStatus(OrderInfoVo orderInfoVo) {
        AssertExt.notNull(orderInfoVo.getCreateUserId(), "会员id不能为空");
        AssertExt.notNull(orderInfoVo.getCurrent(), "当前页不能为空");
        AssertExt.notNull(orderInfoVo.getSize(), "页码大小不能为空");

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("create_user_id", orderInfoVo.getCreateUserId());
        if (!StringUtils.isBlank(orderInfoVo.getOrderStatus())) {
            AssertExt.checkEnum(OrderSubInfo.EOrderSubStatus.class, orderInfoVo.getOrderStatus(), "子订单类型错误");
            queryWrapper.eq("order_status", orderInfoVo.getOrderStatus());
        }
        queryWrapper.eq("is_del", 0);
        queryWrapper.orderBy(true, false, "update_time");

        IPage<OrderInfoPo> orderInfoPoIPage = orderInfoMapper.selectPage(new Page<>(orderInfoVo.getCurrent(), orderInfoVo.getSize()), queryWrapper);

        List<OrderSubInfoPo> orderSubInfoPos;   //子订单集合

        GoodsPriceInfoVO goodsPriceInfoVO;
        double expressAmount = 0;   //主订单的总运费

        //遍历某个会员的某个订单状态的所有支付订单
        for (OrderInfoPo orderInfoPo : orderInfoPoIPage.getRecords()) {
            orderInfoPo.setOrderStatus(OrderInfo.EOrderStatus.valueOf(orderInfoPo.getOrderStatus()).desc());
            orderSubInfoPos = new ArrayList<>();

            //拿到子订单ids
            String[] arrStr = orderInfoPo.getSubOrderIds().split(",");
            StoreInfo storeInfo;
            for (int i = 0; i < arrStr.length; i++) {
                //获取每个子订单的信息
                OrderSubInfoPo orderSubInfoPo = orderSubInfoService.getOrderSubById(Long.valueOf(arrStr[i]).longValue());
                if (orderSubInfoPo == null) continue;
                storeInfo = storeInfoService.getStoreById(orderSubInfoPo.getStoreId());
                orderSubInfoPo.setStoreInfo(storeInfo); //设置店铺信息
                orderSubInfoPo.setOrderStatus(OrderSubInfo.EOrderSubStatus.valueOf(orderSubInfoPo.getOrderStatus()).desc());
                List<OrderGoodsPo> orderGoodsPos = orderGoodsService.getOrderGoodsByOrderSubId(orderSubInfoPo.getId());
                //为每个订单商品快照取到具体的商品信息
                for (OrderGoodsPo orderGoodsPo : orderGoodsPos) {
                    //商品模块提供接口：根据sku返回商品信息
                    goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());
                    orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);

                    //金额：单位分转元
                    orderGoodsPo.setPrice(orderGoodsPo.getPrice() / 100);
                    orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
                    orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
                    orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);

                }
                orderSubInfoPo.setOrderGoodsPos(orderGoodsPos); //把商品快照集合设置到子订单
                expressAmount += orderSubInfoPo.getExpressAmount(); //主订单的总运费
                orderSubInfoPos.add(orderSubInfoPo);


                //金额：单位分转元
                orderSubInfoPo.setAmount(orderSubInfoPo.getAmount() / 100);
                orderSubInfoPo.setExpressAmount(orderSubInfoPo.getExpressAmount() / 100);
                orderSubInfoPo.setRentAmount(orderSubInfoPo.getRentAmount() / 100);
                orderSubInfoPo.setRefundAmount(orderSubInfoPo.getRefundAmount() / 100);
                orderSubInfoPo.setDiscountAmount(orderSubInfoPo.getDiscountAmount() / 100);
                orderSubInfoPo.setActualAmount(orderSubInfoPo.getActualAmount() / 100);

            }
            orderInfoPo.setOrderSubInfoPos(orderSubInfoPos);    //把子订单集合设置到支付订单

            orderInfoPo.setExpressAmount(expressAmount / 100);    //主订单的总运费
            orderInfoPo.setAmount(orderInfoPo.getAmount() / 100);
            orderInfoPo.setDiscountAmount(orderInfoPo.getDiscountAmount() / 100);
            orderInfoPo.setActualAmount(orderInfoPo.getActualAmount() / 100);

        }
        return orderInfoPoIPage;
    }


    /**
     * 全部主订单(已分页)
     *
     * @param orderInfoVo
     * @return
     */
    @Override
    public IPage<OrderInfoPo> getOrderinfos(OrderInfoVo orderInfoVo) {
        AssertExt.notNull(orderInfoVo.getCreateUserId(), "会员id不能为空");
        AssertExt.notNull(orderInfoVo.getCurrent(), "当前页不能为空");
        AssertExt.notNull(orderInfoVo.getSize(), "页码大小不能为空");


        IPage<OrderInfoPo> page = new Page(orderInfoVo.getCurrent(), orderInfoVo.getSize());
        IPage<OrderInfoPo> orderInfoPoIPage = orderInfoMapper.getOrderinfos(page, orderInfoVo.getCreateUserId());

        List<OrderSubInfoPo> orderSubInfoPos;   //子订单集合

        GoodsPriceInfoVO goodsPriceInfoVO;

        double expressAmount = 0;   //主订单的总运费

        //遍历某个会员的某个订单状态的所有支付订单
        for (OrderInfoPo orderInfoPo : orderInfoPoIPage.getRecords()) {
            orderInfoPo.setOrderStatus(OrderInfo.EOrderStatus.valueOf(orderInfoPo.getOrderStatus()).desc());
            orderSubInfoPos = new ArrayList<>();

            //拿到子订单ids
            String[] arrStr = orderInfoPo.getSubOrderIds().split(",");
            StoreInfo storeInfo;
            for (int i = 0; i < arrStr.length; i++) {
                //获取每个子订单的信息
                OrderSubInfoPo orderSubInfoPo = orderSubInfoService.getOrderSubById(Long.valueOf(arrStr[i]));
                if (orderSubInfoPo == null) continue;
                storeInfo = storeInfoService.getStoreById(orderSubInfoPo.getStoreId());
                orderSubInfoPo.setStoreInfo(storeInfo); //设置店铺信息
                orderSubInfoPo.setOrderStatus(OrderSubInfo.EOrderSubStatus.valueOf(orderSubInfoPo.getOrderStatus()).desc());
                List<OrderGoodsPo> orderGoodsPos = orderGoodsService.getOrderGoodsByOrderSubId(orderSubInfoPo.getId());
                //为每个订单商品快照取到具体的商品信息
                for (OrderGoodsPo orderGoodsPo : orderGoodsPos) {
                    //商品模块提供接口：根据sku返回商品信息
                    goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());
                    orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);

                    //金额：单位分转元
                    orderGoodsPo.setPrice(orderGoodsPo.getPrice() / 100);
                    orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
                    orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
                    orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);
                }
                orderSubInfoPo.setOrderGoodsPos(orderGoodsPos); //把商品快照集合设置到子订单
                expressAmount += orderSubInfoPo.getExpressAmount(); //主订单的总运费

                //金额：单位分转元
                orderSubInfoPo.setAmount(orderSubInfoPo.getAmount() / 100);
                orderSubInfoPo.setExpressAmount(orderSubInfoPo.getExpressAmount() / 100);
                orderSubInfoPo.setRentAmount(orderSubInfoPo.getRentAmount() / 100);
                orderSubInfoPo.setRefundAmount(orderSubInfoPo.getRefundAmount() / 100);
                orderSubInfoPo.setDiscountAmount(orderSubInfoPo.getDiscountAmount() / 100);
                orderSubInfoPo.setActualAmount(orderSubInfoPo.getActualAmount() / 100);

                orderSubInfoPos.add(orderSubInfoPo);        //把子订单设置到子订单集合
            }
            orderInfoPo.setOrderSubInfoPos(orderSubInfoPos);    //把子订单集合设置到支付订单

            orderInfoPo.setExpressAmount(expressAmount / 100);    //主订单的总运费
            orderInfoPo.setAmount(orderInfoPo.getAmount() / 100);
            orderInfoPo.setDiscountAmount(orderInfoPo.getDiscountAmount() / 100);
            orderInfoPo.setActualAmount(orderInfoPo.getActualAmount() / 100);
        }
        return orderInfoPoIPage;
    }


    /**
     * 根据主订单Id获取数据(主订单详情)
     *
     * @param map
     * @return
     */
    @Override
    public OrderInfoPo getOrderInfoById(Map map) {
        String orderId = String.valueOf(map.get("orderId"));
        AssertExt.notBlank(orderId, "主订单id不能为空");

        OrderInfoPo orderInfoPo = orderInfoMapper.selectById(orderId);
        List<OrderSubInfoPo> orderSubInfoPos = new ArrayList<>();   //子订单集合
        //拿到子订单ids
        String[] arrStr = orderInfoPo.getSubOrderIds().split(",");
        StoreInfo storeInfo;
        GoodsPriceInfoVO goodsPriceInfoVO;
        double expressAmount = 0;   //主订单的总运费
        for (int i = 0; i < arrStr.length; i++) {
            //获取每个子订单的信息
            OrderSubInfoPo orderSubInfoPo = orderSubInfoService.getOrderSubById(Long.valueOf(arrStr[i]).longValue());
            if (orderSubInfoPo == null) continue;
            storeInfo = storeInfoService.getStoreById(orderSubInfoPo.getStoreId());
            orderSubInfoPo.setStoreInfo(storeInfo); //设置店铺信息
            List<OrderGoodsPo> orderGoodsPos = orderGoodsService.getOrderGoodsByOrderSubId(orderSubInfoPo.getId());
            //为每个订单商品快照取到具体的商品信息
            for (OrderGoodsPo orderGoodsPo : orderGoodsPos) {
                //商品模块提供接口：根据sku返回商品信息
                goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());
                orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);

                //金额：单位分转元
                orderGoodsPo.setPrice(orderGoodsPo.getPrice() / 100);
                orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
                orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
                orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);
            }
            orderSubInfoPo.setOrderGoodsPos(orderGoodsPos); //把商品快照集合设置到子订单
            expressAmount += orderSubInfoPo.getExpressAmount(); //主订单的总运费

            //金额：单位分转元
            orderSubInfoPo.setAmount(orderSubInfoPo.getAmount() / 100);
            orderSubInfoPo.setExpressAmount(orderSubInfoPo.getExpressAmount() / 100);
            orderSubInfoPo.setRentAmount(orderSubInfoPo.getRentAmount() / 100);
            orderSubInfoPo.setRefundAmount(orderSubInfoPo.getRefundAmount() / 100);
            orderSubInfoPo.setDiscountAmount(orderSubInfoPo.getDiscountAmount() / 100);
            orderSubInfoPo.setActualAmount(orderSubInfoPo.getActualAmount() / 100);

            orderSubInfoPos.add(orderSubInfoPo);
        }

        orderInfoPo.setOrderSubInfoPos(orderSubInfoPos);    //把子订单集合设置到支付订单

        orderInfoPo.setExpressAmount(expressAmount / 100);    //主订单的总运费
        orderInfoPo.setAmount(orderInfoPo.getAmount() / 100);
        orderInfoPo.setDiscountAmount(orderInfoPo.getDiscountAmount() / 100);
        orderInfoPo.setActualAmount(orderInfoPo.getActualAmount() / 100);
        return orderInfoPo;
    }

    /**
     * 根据订单号获取订单实体内容
     * @param orderNo
     * @return
     */
    @Override
    public OrderInfoPo getOrderInfoByNo(String orderNo) {
        return orderInfoMapper.selectOne(new QueryWrapper<OrderInfoPo>().eq("order_no",orderNo ));
    }



    /**
     * 删除主订单
     *
     * @return
     */
    @Transactional  //事务
    @Override
    public void delOrder(Map params) {
        AssertExt.isFalse(params.get("orderId") == null || params.get("orderId").equals(""), "订单id不能为空");
        AssertExt.isFalse(params.get("memberId") == null || params.get("memberId").equals(""), "会员id不能为空");
        Long orderId = Long.parseLong(params.get("orderId").toString());
        Long memberId = Long.parseLong(params.get("memberId").toString());

        orderInfoMapper.delOrderInfo(orderId, memberId, LocalDateTime.now());//删除主订单

        OrderInfoPo orderInfoPo1 = orderInfoMapper.selectOne(new QueryWrapper<OrderInfoPo>().eq("id", orderId));
        //拿到子订单ids
        String[] arrStr = orderInfoPo1.getSubOrderIds().split(",");
        List<String> ids = Arrays.asList(arrStr);

        orderSubInfoService.delOrderSub(ids);   //删除子订单
        orderGoodsService.delOrderGoods(ids);   //删除订单商品快照

    }


    /**
     * 更新主订单状态
     *
     * @param params
     */
    @Override
    public void updateOrderStatus(Map params) {
        AssertExt.isFalse(params.get("orderId") == null || "".equals(params.get("orderId")), "订单id不能为空");
        AssertExt.isFalse(params.get("status") == null || "".equals(params.get("status")), "订单状态不能为空");
        String orderId = String.valueOf(params.get("orderId"));
        String status = String.valueOf(params.get("status"));
        String memberId = String.valueOf(params.get("memberId"));
        if("null".equals(memberId)){
            memberId = null;
        }
        AssertExt.checkEnum(OrderInfo.EOrderStatus.class, status, "主订单类型错误");


        /**
         * 取消订单
         */
        if (status.equals(OrderInfo.EOrderStatus.CANCEL.toString())) {
            orderInfoMapper.updateOrderInfoStatusById(Long.parseLong(orderId), memberId, status, LocalDateTime.now());

            OrderInfoPo orderInfoPo1 = orderInfoMapper.selectOne(new QueryWrapper<OrderInfoPo>().eq("id", orderId));
            //拿到子订单ids
            String[] arrStr = orderInfoPo1.getSubOrderIds().split(",");
            List<String> ids = Arrays.asList(arrStr);
            params.put("ids", ids);
            params.put("status", status);
            orderSubInfoService.updateOrderSubStatus(params);   //更新子订单状态
        } else if (status.equals(OrderInfo.EOrderStatus.SUCCESS.toString())) {
            orderInfoMapper.updateOrderInfoStatusById(Long.parseLong(orderId), memberId, status, LocalDateTime.now());
            OrderInfoPo orderInfoPo1 = orderInfoMapper.selectOne(new QueryWrapper<OrderInfoPo>().eq("id", orderId));
            //拿到子订单ids
            String[] arrStr = orderInfoPo1.getSubOrderIds().split(",");
            List<String> ids = Arrays.asList(arrStr);
            params.put("ids", ids);
            params.put("status", "PAID");
            orderSubInfoService.updateOrderSubStatus(params);   //更新子订单状态
        } else {
            orderInfoMapper.updateOrderInfoStatusById(Long.parseLong(orderId), memberId, status, LocalDateTime.now());
        }
    }


    /**
     * 获取会员所有订单状态对应的数量
     *
     * @param memberId
     * @param status
     * @return
     */
    @Override
    public int getAllOrderStatusCount(Long memberId, String status) {
        return orderInfoMapper.getAllOrderStatusCount(memberId, status);
    }


    /**
     * 修改主订单数据
     *
     * @param orderNo
     * @param amount
     * @param actualAmount
     * @param now
     */
    @Override
    public void updateOrderByOldforNew(String orderNo, int amount, int actualAmount, LocalDateTime now) {
        orderInfoMapper.updateOrderByOldforNew(orderNo, amount, actualAmount, now);
    }


    /**
     * 支付回调修改订单数据
     * @param orderInfoPo
     */
    @Override
    public void callback(OrderInfoPo orderInfoPo) {
        orderInfoMapper.callback(orderInfoPo);
    }


    /**
     * 检测该订单是否是气订单
     * @param orderInfoPo
     * @return
     */
    @Override
    public boolean checkOrderIsGas(OrderInfoPo orderInfoPo) {
        String[] arrStr = orderInfoPo.getSubOrderIds().split(",");
        if(arrStr.length > 1){
            return false;
        }
        StoreInfo storeInfo = storeInfoService.getStoreById(orderSubInfoService.getOrderSubById(Long.parseLong(arrStr[0])).getStoreId());
        if(storeInfo.getStoreTypeId() == 0){
            return true;
        }
        return false;
    }


    /**
     * 订单支付
     * @param payOrder
     * @param accountId
     * @param ip
     * @return
     */
    @Override
    public PayOrderVo payOrder(PayOrderVo payOrder,Long accountId,String ip) {
        OrderInfoPo orderInfoPo = orderInfoMapper.selectOne(new QueryWrapper<OrderInfoPo>().eq("order_no", payOrder.getOrderNo()));
        GoodsPriceInfoVO goodsPriceInfoVO;
        //拿到子订单ids
        String[] arrStr = orderInfoPo.getSubOrderIds().split(",");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arrStr.length; i++) {
            //获取每个子订单的信息
            OrderSubInfoPo orderSubInfoPo = orderSubInfoService.getOrderSubById(Long.parseLong(arrStr[i]));
            List<OrderGoodsPo> orderGoodsPos = orderGoodsService.getOrderGoodsByOrderSubId(orderSubInfoPo.getId());
            for (OrderGoodsPo orderGoodsPo : orderGoodsPos) {
                //商品模块提供接口：根据sku返回商品信息
                goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());

                stringBuilder.append(goodsPriceInfoVO.getGoodsName());
                stringBuilder.append(" ");
            }
        }

        payOrder.setTotalFee((int)orderInfoPo.getActualAmount());
        payOrder.setDataSource(dataSource);
        payOrder.setSpbillCreateIp(ip);
        payOrder.setBody(stringBuilder.toString());
        payOrder.setOpenid(accountService.getAccountBind(accountId, AccountBind.AccountBindType.PROGRAM_OPENID.val()).getBindAccount());
        return payOrderService.payOrder(payOrder);
    }
}
