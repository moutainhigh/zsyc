package com.zsyc.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.account.mapper.AccountMapper;
import com.zsyc.common.AssertExt;
import com.zsyc.delivery.service.DeliveryService;
import com.zsyc.goods.mapper.GoodsInfoMapper;
import com.zsyc.goods.service.GoodsStorePriceService;
import com.zsyc.goods.vo.GoodsAttributeValueVO;
import com.zsyc.goods.vo.GoodsPriceInfoVO;
import com.zsyc.member.entity.MemberAddress;
import com.zsyc.member.service.MemberInfoService;
import com.zsyc.order.entity.*;
import com.zsyc.order.mapper.OrderGoodsMapper;
import com.zsyc.order.mapper.OrderSubGoodsMapper;
import com.zsyc.order.mapper.OrderSubInfoMapper;
import com.zsyc.order.po.*;
import com.zsyc.order.utils.OrderUtil;
import com.zsyc.order.vo.*;
import com.zsyc.store.entity.StoreDeliveryRelation;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.store.mapper.StoreDeliveryRelationMapper;
import com.zsyc.store.service.StoreInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: zsyc-parent
 * @description: 子订单
 * @author: Mr.Ning
 * @create: 2019-01-15 14:30
 **/
@Service
public class OrderSubInfoServiceImpl implements OrderSubInfoService {

    private static final Logger logger = LoggerFactory.getLogger(OrderInfoServiceImpl.class);

    //催单时间间隔,10分钟（600000毫秒）
    @Value("${order.reminder.time}")
    private Long orderReminderTime;

    @Resource
    private OrderSubInfoMapper orderSubInfoMapper;

    @Resource
    private OrderGoodsService orderGoodsService;

    @Resource
    private OrderProtocolInfoService orderProtocolInfoService;

    @Resource
    private OrderProtocolGoodsService orderProtocolGoodsService;

    @Resource
    private MemberInfoService memberInfoService;

    @Resource
    private StoreInfoService storeInfoService;

    @Resource
    private GoodsStorePriceService goodsStorePriceService;

    @Resource
    private DeliveryService deliveryService;

    @Resource
    private StoreDeliveryRelationMapper storeDeliveryRelationMapper;

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private GoodsInfoMapper goodsInfoMapper;

    @Resource
    private OrderGoodsMapper orderGoodsMapper;

    @Resource
    private OrderSubGoodsService orderSubGoodsService;

    @Resource
    private OrderSubGoodsMapper orderSubGoodsMapper;


    /**
     * 创建子订单
     *
     * @param orderVo
     * @return List<OrderSubInfo> 返回批量插入数据的自增id
     */
    @Override
    public Map<String, Object> createOrderSub(OrderVo orderVo, String orderInfoNo) {

        //订单分类：菜市场订单0或者气订单1
        if (orderVo.getOrderCategory() == 0) {

            /**
             * 菜市场订单
             */
            return orderByFoodMarket(orderVo, orderInfoNo);

        } else {

            /**
             * 气订单
             */
            return orderByGas(orderVo, orderInfoNo);

        }

    }


    /**
     * 菜市场订单
     *
     * @param orderVo
     */
    private Map<String, Object> orderByFoodMarket(OrderVo orderVo, String orderInfoNo) {

        /**
         * 优惠金额按子订单总金额比例分配
         */

        int amount = 0;     //支付订单的总金额
        int discountAmount = 0;     //支付订单的总优惠金额
        int actualAmount = 0;     //支付订单的总应付金额

        int orderSubAmount = 0;     //每个子订单的总金额
        int orderGoodAmount = 0;    //每个订单商品的总金额

        for (OrderSubInfoVo orderSubInfoVo : orderVo.getOrderSubInfoVos()) {

            orderSubInfoVo.setOrderNo(OrderUtil.getOrderNo());    //生成唯一订单号
            orderSubInfoVo.setOrderInfoNo(orderInfoNo);     //生成唯一主订单号

            /**
             * 非自提
             */
            if (orderSubInfoVo.getDeliveryType().equals(OrderSubInfo.EDeliveryType.EXPRESS.toString())) {
                //非自提,默认一小时配送时间
                orderSubInfoVo.setBookStartTime(LocalDateTime.now());
                orderSubInfoVo.setBookEndTime(LocalDateTime.now().plusHours(1));
                //该子订单的运费：地址楼层*店铺设置的运费
                orderSubInfoVo.setExpressAmount(orderVo.getMemberAddress().getStorey() * orderSubInfoVo.getStoreInfo().getCarriage());
                if (orderVo.getMemberAddress().getStorey() < 0) {     //电梯,不需要*楼层
                    orderSubInfoVo.setExpressAmount(orderSubInfoVo.getStoreInfo().getCarriage());
                }
            } else {
                //该子订单的运费：0
                orderSubInfoVo.setExpressAmount(0);
            }

            //计算总金额（单价*数量）
            GoodsPriceInfoVO goodsPriceInfoVO;
            for (OrderGoodsVo orderGoodsVo : orderSubInfoVo.getOrderGoodsVos()) {
                orderGoodAmount = orderGoodsVo.getPrice() * orderGoodsVo.getNum();
                orderSubAmount += orderGoodAmount;
                orderGoodsVo.setAmount(orderGoodAmount - 0);    //优惠券金额未减（功能未做），用0代替
            }

            //该子订单总金额(商品总金额)
            orderSubInfoVo.setAmount(orderSubAmount);

            //该子订单应付金额(注：优惠金额未计算)
            orderSubInfoVo.setActualAmount(orderSubInfoVo.getAmount() + orderSubInfoVo.getExpressAmount() - 0);//优惠券金额未减（功能未做），用0代替

            //支付订单总金额
            amount += orderSubAmount;
            //支付订单总应付金额
            actualAmount += orderSubInfoVo.getActualAmount();
            orderSubAmount = 0;


            //一些基本数据
            orderSubInfoVo.setAddressId(orderVo.getMemberAddress().getId());    //地址id
            orderSubInfoVo.setConsigneePhone(orderVo.getMemberAddress().getTelephone());    //收货人电话
            orderSubInfoVo.setPriority(0);  //优先级
            orderSubInfoVo.setConsignee(orderVo.getMemberAddress().getConsignee()); //收货人姓名
            //客户备注已经在参数里（从前端接收了）
            orderSubInfoVo.setConsigneeAddress(orderVo.getMemberAddress().getLocationAddress() + orderVo.getMemberAddress().getAddress());    //收货地址
            orderSubInfoVo.setType(OrderSubInfo.EOrderType.ORDINARY.toString());    //普通订单
            orderSubInfoVo.setCreateUserId(orderVo.getMemberAddress().getMemberId());   //创建人id
            orderSubInfoVo.setCreateTime(LocalDateTime.now());  //创建时间
            orderSubInfoVo.setUpdateUserId(orderVo.getMemberAddress().getMemberId());   //更新人
            orderSubInfoVo.setUpdateTime(LocalDateTime.now());  //更新时间
            orderSubInfoVo.setIsDel(0); //是否删除
            orderSubInfoVo.setOrderStatus(OrderInfo.EOrderStatus.UNPAID.toString());    //订单状态
        }


        orderSubInfoMapper.createOrderSub(orderVo.getOrderSubInfoVos());

        Map<String, Object> result = new HashMap<>();
        result.put("amount", amount);
        result.put("actualAmount", actualAmount);
        result.put("orderSubInfoVos", orderVo.getOrderSubInfoVos());
        return result;
    }


    /**
     * 气订单
     *
     * @param orderVo
     */
    private Map<String, Object> orderByGas(OrderVo orderVo, String orderInfoNo) {

        /**
         * 优惠金额按子订单总金额比例分配
         */

        /**
         * 互换瓶，新租瓶
         * 新租瓶：押金：押金就是（瓶子的价格*组合商品sku的数量），气跟瓶成了组合商品。我们只拿组合商品的sku就行
         * 互换瓶：没押金。直接拿sku的单价就可以了（非组合商品）
         */

        int amount = 0;     //支付订单的总金额
        int discountAmount = 0;     //支付订单的总优惠金额
        int actualAmount = 0;     //支付订单的总应付金额

        int orderSubAmount = 0;     //每个子订单的总金额
        int orderGoodAmount = 0;    //每个订单商品的总金额

        /**
         * 注意：orderVo.getOrderSubInfoVos()这个集合永远只会循环一遍。
         *      orderSubInfoVo.getOrderGoodsVos()这个集合永远也只会循环一遍
         *      因为互换瓶，新租瓶，旧换新不可以同时下单（即只有一个子订单），而商品也是只有一个
         *      即一张气支付订单永远只有一个子订单和一个商品
         *      即使这样还使用集合,是因为不想更改太多代码(copy菜市场订单)
         */

        for (OrderSubInfoVo orderSubInfoVo : orderVo.getOrderSubInfoVos()) {

            orderSubInfoVo.setOrderNo(OrderUtil.getOrderNo());    //生成唯一订单号
            orderSubInfoVo.setOrderInfoNo(orderInfoNo);     //生成唯一主订单号

            int goodsNum = 0;   //瓶数（用来计算运费）
            GoodsPriceInfoVO goodsPriceInfoVO;
            //计算总金额（单价*数量）
            for (OrderGoodsVo orderGoodsVo : orderSubInfoVo.getOrderGoodsVos()) {
                goodsNum += orderGoodsVo.getNum();
                orderGoodAmount = orderGoodsVo.getPrice() * orderGoodsVo.getNum();
                orderSubAmount += orderGoodAmount;
                orderGoodsVo.setAmount(orderGoodAmount - 0);    //优惠券金额未减（功能未做），用0代替
            }

            /**
             * 非自提
             */
            if (orderSubInfoVo.getDeliveryType().equals(OrderSubInfo.EDeliveryType.EXPRESS.toString())) {
                //非自提,默认一小时配送时间
                orderSubInfoVo.setBookStartTime(LocalDateTime.now());
                orderSubInfoVo.setBookEndTime(LocalDateTime.now().plusHours(1));
                //该子订单的运费：地址楼层*店铺设置的运费*瓶数
                orderSubInfoVo.setExpressAmount(orderVo.getMemberAddress().getStorey() * orderSubInfoVo.getStoreInfo().getCarriage() * goodsNum);
                if (orderVo.getMemberAddress().getStorey() < 0) {     //电梯,不需要*楼层
                    orderSubInfoVo.setExpressAmount(orderSubInfoVo.getStoreInfo().getCarriage() * goodsNum);
                }
            } else {
                //该子订单的运费：0
                orderSubInfoVo.setExpressAmount(0);
            }

            //该子订单总金额(商品总金额)
            orderSubInfoVo.setAmount(orderSubAmount);

            //该子订单的租金
            List<OrderProtocolInfoPo> orderProtocolInfoPos = orderProtocolInfoService.getOrderProtocolInfo(orderVo.getMemberAddress().getMemberId());
            int rentAmount = 0; //租金
            int month = 0; //月份

            for (OrderProtocolInfoPo orderProtocolInfoPo : orderProtocolInfoPos) {
                //如果是借的话，则不计算租金
                if (orderProtocolInfoPo.getType().equals(OrderProtocolInfo.ProtocolType.BORROW.val())) continue;
                month = LocalDateTime.now().getMonthValue() - orderProtocolInfoPo.getLastRentPaymentTime().getMonthValue();
                if (month != 0) {
                    rentAmount += month * orderProtocolInfoPo.getCurrentBottleNum() * orderSubInfoVo.getStoreInfo().getRent();
                }
            }

            orderSubInfoVo.setRentAmount(rentAmount);   //设置租金

            //如果是水店，租金0
            if (orderSubInfoVo.getStoreInfo().getStoreTypeId() == 1) {
                orderSubInfoVo.setRentAmount(0);   //设置租金
            }


            //该子订单应付金额(注：优惠金额未计算)
            orderSubInfoVo.setActualAmount(orderSubInfoVo.getAmount() + orderSubInfoVo.getExpressAmount() + orderSubInfoVo.getRentAmount() - 0);//优惠券金额未减（功能未做），用0代替

            //支付订单总金额   (每个订单的商品金额(押金) + 运费 + 租金)
            amount += orderSubAmount;
            //支付订单总应付金额
            actualAmount += orderSubInfoVo.getActualAmount();
            orderSubAmount = 0;


            //一些基本数据
            orderSubInfoVo.setAddressId(orderVo.getMemberAddress().getId());    //地址id
            orderSubInfoVo.setConsigneePhone(orderVo.getMemberAddress().getTelephone());    //收货人电话
            orderSubInfoVo.setPriority(0);  //优先级
            if (orderVo.getPhoto() != null) {
                orderSubInfoVo.setPhoto(orderVo.getPhoto());    //旧换新的处理图片路径
            }
            orderSubInfoVo.setConsignee(orderVo.getMemberAddress().getConsignee()); //收货人姓名
            //客户备注已经在参数里（从前端接收了）
            orderSubInfoVo.setConsigneeAddress(orderVo.getMemberAddress().getLocationAddress() + orderVo.getMemberAddress().getAddress());    //收货地址
            if (orderVo.getGasOrderCategory() == 3) {
                /**
                 * 旧换新
                 */
                orderSubInfoVo.setType(OrderSubInfo.EOrderType.OLDFORNEW.toString());    //旧换新订单
            } else {
                orderSubInfoVo.setType(OrderSubInfo.EOrderType.ORDINARY.toString());    //普通订单
            }
            orderSubInfoVo.setCreateUserId(orderVo.getMemberAddress().getMemberId());   //创建人id
            orderSubInfoVo.setCreateTime(LocalDateTime.now());  //创建时间
            orderSubInfoVo.setUpdateUserId(orderVo.getMemberAddress().getMemberId());   //更新人
            orderSubInfoVo.setUpdateTime(LocalDateTime.now());  //更新时间
            orderSubInfoVo.setIsDel(0); //是否删除
            if (OrderInfo.EPayType.OFFLINE.val().equals(orderVo.getPayType())) {
                //货到付款，直接支付成功
                orderSubInfoVo.setOrderStatus(OrderSubInfo.EOrderSubStatus.PAID.val());    //订单状态
            } else {
                orderSubInfoVo.setOrderStatus(OrderSubInfo.EOrderSubStatus.UNPAID.val());    //订单状态
            }
        }

        orderSubInfoMapper.createOrderSub(orderVo.getOrderSubInfoVos());

        Map<String, Object> result = new HashMap<>();
        result.put("amount", amount);
        result.put("actualAmount", actualAmount);
        result.put("orderSubInfoVos", orderVo.getOrderSubInfoVos());
        return result;
    }


    /**
     * 后台下单
     *
     * @param orderVo
     */
    public Map<String, Object> createOrderSubBack(OrderVo orderVo, String orderInfoNo) {

        /**
         * 优惠金额按子订单总金额比例分配
         */

        /**
         * 互换瓶，新租瓶
         * 新租瓶：押金：押金就是（瓶子的价格*组合商品sku的数量），气跟瓶成了组合商品。我们只拿组合商品的sku就行
         * 互换瓶：没押金。直接拿sku的单价就可以了（非组合商品）
         */

        int amount = 0;     //支付订单的总金额
        int discountAmount = 0;     //支付订单的总优惠金额
        int actualAmount = 0;     //支付订单的总应付金额

        int orderSubAmount = 0;     //每个子订单的总金额
        int orderGoodAmount = 0;    //每个订单商品的总金额

        /**
         * 注意：orderVo.getOrderSubInfoVos()这个集合永远只会循环一遍。
         *      orderSubInfoVo.getOrderGoodsVos()这个集合永远也只会循环一遍
         *      因为互换瓶，新租瓶，旧换新不可以同时下单（即只有一个子订单），而商品也是只有一个
         *      即一张气支付订单永远只有一个子订单和一个商品
         *      即使这样还使用集合,是因为不想更改太多代码(copy菜市场订单)
         */

        for (OrderSubInfoVo orderSubInfoVo : orderVo.getOrderSubInfoVos()) {

            orderSubInfoVo.setOrderNo(OrderUtil.getOrderNo());    //生成唯一订单号
            orderSubInfoVo.setOrderInfoNo(orderInfoNo);     //生成唯一主订单号

            int goodsNum = 0;   //瓶数（用来计算运费）
            GoodsPriceInfoVO goodsPriceInfoVO;
            //计算总金额（单价*数量）
            for (OrderGoodsVo orderGoodsVo : orderSubInfoVo.getOrderGoodsVos()) {
                goodsNum += orderGoodsVo.getNum();
                orderGoodAmount = orderGoodsVo.getPrice() * orderGoodsVo.getNum();
                orderSubAmount += orderGoodAmount;
                orderGoodsVo.setAmount(orderGoodAmount - 0);    //优惠券金额未减（功能未做），用0代替
            }

            /**
             * 非自提
             */
            if (orderSubInfoVo.getDeliveryType().equals(OrderSubInfo.EDeliveryType.EXPRESS.toString())) {
                //非自提,默认一小时配送时间
                orderSubInfoVo.setBookStartTime(LocalDateTime.now());
                orderSubInfoVo.setBookEndTime(LocalDateTime.now().plusHours(1));
                //该子订单的运费：地址楼层*店铺设置的运费*瓶数
                orderSubInfoVo.setExpressAmount(orderVo.getMemberAddress().getStorey() * orderSubInfoVo.getStoreInfo().getCarriage() * goodsNum);
                if (orderVo.getMemberAddress().getStorey() < 0) {     //电梯,不需要*楼层
                    orderSubInfoVo.setExpressAmount(orderSubInfoVo.getStoreInfo().getCarriage() * goodsNum);
                }
            } else {
                //该子订单的运费：0
                orderSubInfoVo.setExpressAmount(0);
            }

            //该子订单总金额(商品总金额)
            orderSubInfoVo.setAmount(orderSubAmount);

            //该子订单的租金
            List<OrderProtocolInfoPo> orderProtocolInfoPos = orderProtocolInfoService.getOrderProtocolInfo(orderVo.getMemberAddress().getMemberId());
            int rentAmount = 0; //租金
            int month = 0; //月份

            for (OrderProtocolInfoPo orderProtocolInfoPo : orderProtocolInfoPos) {
                //如果是借的话，则不计算租金
                if (orderProtocolInfoPo.getType().equals(OrderProtocolInfo.ProtocolType.BORROW.val())) continue;
                month = LocalDateTime.now().getMonthValue() - orderProtocolInfoPo.getLastRentPaymentTime().getMonthValue();
                if (month != 0) {
                    rentAmount += month * orderProtocolInfoPo.getCurrentBottleNum() * orderSubInfoVo.getStoreInfo().getRent();
                }
            }

            orderSubInfoVo.setRentAmount(rentAmount);   //设置租金

            //如果是水店，租金0
            if (orderSubInfoVo.getStoreInfo().getStoreTypeId() == 1) {
                orderSubInfoVo.setRentAmount(0);   //设置租金
            }


            //该子订单应付金额(注：优惠金额未计算)
            orderSubInfoVo.setActualAmount(orderSubInfoVo.getAmount() + orderSubInfoVo.getExpressAmount() + orderSubInfoVo.getRentAmount() - 0);//优惠券金额未减（功能未做），用0代替

            //支付订单总金额   (每个订单的商品金额(押金) + 运费 + 租金)
            amount += orderSubAmount;
            //支付订单总应付金额
            actualAmount += orderSubInfoVo.getActualAmount();
            orderSubAmount = 0;


            //一些基本数据
            orderSubInfoVo.setAddressId(orderVo.getMemberAddress().getId());    //地址id
            orderSubInfoVo.setConsigneePhone(orderVo.getMemberAddress().getTelephone());    //收货人电话
            orderSubInfoVo.setPriority(0);  //优先级
            if (orderVo.getPhoto() != null) {
                orderSubInfoVo.setPhoto(orderVo.getPhoto());    //旧换新的处理图片路径
            }
            orderSubInfoVo.setConsignee(orderVo.getMemberAddress().getConsignee()); //收货人姓名
            //客户备注已经在参数里（从前端接收了）
            orderSubInfoVo.setConsigneeAddress(orderVo.getMemberAddress().getLocationAddress() + orderVo.getMemberAddress().getAddress());    //收货地址
            if (orderVo.getGasOrderCategory() == 3) {
                /**
                 * 旧换新
                 */
                orderSubInfoVo.setType(OrderSubInfo.EOrderType.OLDFORNEW.toString());    //旧换新订单
                orderSubInfoVo.setOrderStatus(OrderSubInfo.EOrderSubStatus.UNPAID.val());    //订单状态
            } else {
                orderSubInfoVo.setType(OrderSubInfo.EOrderType.ORDINARY.toString());    //普通订单
                orderSubInfoVo.setOrderStatus(OrderSubInfo.EOrderSubStatus.PAID.val());    //订单状态
            }
            orderSubInfoVo.setCreateUserId(orderVo.getMemberAddress().getMemberId());   //创建人id
            orderSubInfoVo.setCreateTime(LocalDateTime.now());  //创建时间
            orderSubInfoVo.setUpdateUserId(orderVo.getMemberAddress().getMemberId());   //更新人
            orderSubInfoVo.setUpdateTime(LocalDateTime.now());  //更新时间
            orderSubInfoVo.setIsDel(0); //是否删除
        }

        orderSubInfoMapper.createOrderSub(orderVo.getOrderSubInfoVos());

        Map<String, Object> result = new HashMap<>();
        result.put("amount", amount);
        result.put("actualAmount", actualAmount);
        result.put("orderSubInfoVos", orderVo.getOrderSubInfoVos());
        return result;
    }

    /**
     * 生成退瓶订单
     *
     * @param orderSubInfoVo
     */
    @Override
    @Transactional
    public void createProtocolRefundOrder(OrderSubInfoVo orderSubInfoVo) {
        AssertExt.notNull(orderSubInfoVo.getProtocolNo(), "协议号不能为空");
        AssertExt.notNull(orderSubInfoVo.getOrderGoodsVos(), "订单商品快照不能为空");
        List<OrderGoodsVo> orderGoodsVos = orderSubInfoVo.getOrderGoodsVos();
        OrderProtocolGoodsPo orderProtocolGoodsPo;
        OrderGoods orderGoods;
        List<OrderGoods> orderGoodsList = new ArrayList<>();
        OrderSubInfoPo orderSubInfoPoParams = new OrderSubInfoPo();

        //根据协议号找到子订单
        OrderSubInfoPo orderSubInfoPo = orderSubInfoMapper.selectOne(new QueryWrapper<OrderSubInfoPo>()
                .eq("protocol_no", orderSubInfoVo.getProtocolNo())
                .eq("type", OrderSubInfo.EOrderType.ORDINARY));

        //根据地址id拿到地址信息
        MemberAddress memberAddress = memberInfoService.getMemberAddressById(orderSubInfoPo.getAddressId());

        //根据店铺id拿到店铺信息
        StoreInfo storeInfo = storeInfoService.getStoreById(orderSubInfoPo.getStoreId());

        //租金(这里只算相对应协议的租金，新下单才算全部协议的租金)
        OrderProtocolInfoPo orderProtocolInfoPo = orderProtocolInfoService.getOrderProtocolInfoPoByProtocolNo(orderSubInfoVo.getProtocolNo());

        //租的
        if (orderProtocolInfoPo.getType().equals(OrderProtocolInfo.ProtocolType.RENT.val())) {
            int currentBottleNum = 0;
            for (OrderProtocolGoodsPo orderProtocolGoodsPo1 : orderProtocolInfoPo.getOrderProtocolGoodsPos()) {
                currentBottleNum += orderProtocolGoodsPo1.getNum();
            }

            int month = LocalDateTime.now().getMonthValue() - orderProtocolInfoPo.getLastRentPaymentTime().getMonthValue();
            if (month == 0) {
                orderSubInfoPoParams.setRentAmount(0);
            } else {

                orderSubInfoPoParams.setRentAmount(month * storeInfo.getRent() * currentBottleNum);

                /**
                 * 更新最后一次缴纳租金时间（如果是气店，才更新）
                 */
                if (storeInfo.getStoreTypeId() == 0) {
                    orderProtocolInfoService.updateOrderProtocolLastRentPaymentTime(orderProtocolInfoPo.getProtocolNo(), LocalDateTime.now());
                }
            }
        } else {
            //借的
            orderSubInfoPoParams.setRentAmount(0);
        }


        //如果是水店，租金0
        if (storeInfo.getStoreTypeId() == 1) {
            orderSubInfoPoParams.setRentAmount(0);
        }


        int amount = 0; //应减去的押金
        Integer goodsNum = 0; //总瓶数
        /**
         * 注意：orderGoodsVo.getId()的id是协议商品表的id，不是订单商品表的id
         */
        for (OrderGoodsVo orderGoodsVo : orderGoodsVos) {
            /**
             * 更新协议商品数量
             */
            orderProtocolGoodsService.updateGoodsNum(orderGoodsVo.getId(), orderGoodsVo.getNum());

            /**
             * 拿到具体的协议商品信息
             */
            orderProtocolGoodsPo = orderProtocolGoodsService.getOrderProtocolGoodsById(orderGoodsVo.getId());

            amount += orderProtocolGoodsPo.getPrice() * orderGoodsVo.getNum();
            goodsNum += orderGoodsVo.getNum();

            //insert订单商品快照表需要的数据（先不insert子订单id和其他信息，后面才insert）
            orderGoods = new OrderGoodsVo();
            orderGoods.setPrice((int) orderProtocolGoodsPo.getPrice());   //协议商品的价格
            orderGoods.setAmount((int) orderProtocolGoodsPo.getPrice() * orderGoodsVo.getNum());
            orderGoods.setSku(orderProtocolGoodsPo.getSku());
            orderGoods.setNum(orderGoodsVo.getNum());   //传进来的数量
            orderGoodsList.add(orderGoods);
        }

        /**
         * 减去相对应的押金
         */
        orderProtocolInfoService.updateDepositCurrent(orderSubInfoVo.getProtocolNo(), amount);


        //运费金额
        orderSubInfoPoParams.setExpressAmount(goodsNum * memberAddress.getStorey() * storeInfo.getCarriage());
        if (memberAddress.getStorey() < 0) {     //电梯,不需要*楼层
            orderSubInfoPoParams.setExpressAmount(goodsNum * storeInfo.getCarriage());
        }

        //实付金额 = 运费金额 + 租金
        orderSubInfoPoParams.setActualAmount(orderSubInfoPoParams.getExpressAmount() + orderSubInfoPoParams.getRentAmount());

        //退款金额
        orderSubInfoPoParams.setRefundAmount(amount);


        //一些基本数据
        orderSubInfoPoParams.setOrderNo(OrderUtil.getOrderNo());    //生成唯一订单号
        orderSubInfoPoParams.setOrderInfoNo(orderSubInfoPo.getOrderInfoNo());    //主订单号
        orderSubInfoPoParams.setAddressId(orderSubInfoPo.getAddressId());    //地址id
        orderSubInfoPoParams.setAccountId(orderSubInfoPo.getAccountId());   //帐号id
        orderSubInfoPoParams.setProtocolNo(orderSubInfoPo.getProtocolNo());     //协议号
        orderSubInfoPoParams.setStoreId(orderSubInfoPo.getStoreId());   //店铺id
        orderSubInfoPoParams.setConsigneePhone(orderSubInfoPo.getConsigneePhone());    //收货人电话
        orderSubInfoPoParams.setConsignee(orderSubInfoPo.getConsignee()); //收货人姓名
        orderSubInfoPoParams.setConsigneeAddress(orderSubInfoPo.getConsigneeAddress());    //收货地址
        orderSubInfoPoParams.setType(OrderSubInfo.EOrderType.BOTTLEBACK.toString());    //普通订单
        orderSubInfoPoParams.setDeliveryType(OrderSubInfo.EDeliveryType.EXPRESS.toString());    //配送方式
        orderSubInfoPoParams.setCreateUserId(orderSubInfoPo.getCreateUserId());   //创建人id
        orderSubInfoPoParams.setCreateTime(LocalDateTime.now());  //创建时间
        orderSubInfoPoParams.setUpdateUserId(orderSubInfoPo.getCreateUserId());   //更新人
        orderSubInfoPoParams.setUpdateTime(LocalDateTime.now());  //更新时间
        orderSubInfoPoParams.setIsDel(0); //是否删除
        orderSubInfoPoParams.setOrderStatus(OrderSubInfo.EOrderSubStatus.APPLY.toString());    //订单状态
        orderSubInfoMapper.insert(orderSubInfoPoParams);


        //insert订单商品快照表需要的数据（insert子订单id和其他信息）
        for (OrderGoods orderGoods1 : orderGoodsList) {
            orderGoods1.setOrderSubId(orderSubInfoPoParams.getId());  //子订单id
            orderGoods1.setCreateUserId(orderSubInfoPoParams.getCreateUserId());
            orderGoods1.setCreateTime(LocalDateTime.now());
            orderGoods1.setUpdateUserId(orderSubInfoPoParams.getUpdateUserId());
            orderGoods1.setUpdateTime(LocalDateTime.now());
            orderGoods1.setIsDel(0);
        }


        /**
         * 生成订单商品快照表
         */
        orderGoodsService.createProtocolRefundOrderGoods(orderGoodsList);

    }


    /**
     * 根据时间段获取子订单（给店铺提供的接口）
     *
     * @param orderSubInfoVo
     * @return
     */
    @Override
    public List<OrderSubInfoPo> getOrderinfosByTimeSlot(OrderSubInfoVo orderSubInfoVo) {
        return orderSubInfoMapper.getOrderinfosByTimeSlot(orderSubInfoVo);
    }


    /**
     * 获取租金和运费信息
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> getRentAmountAndExpressInfo(Map params) {
        AssertExt.isFalse(params.get("addressId") == null || params.get("addressId").equals(""), "地址id为空");
        AssertExt.isFalse(params.get("storeId") == null || params.get("storeId").equals(""), "店铺id为空");
        Long addressId = Long.parseLong(params.get("addressId").toString());
        Long storeId = Long.parseLong(params.get("storeId").toString());

        //根据地址id拿到地址信息
        MemberAddress memberAddress = memberInfoService.getMemberAddressById(addressId);

        //店铺信息
        StoreInfo storeInfo = storeInfoService.getStoreById(storeId);

        //该子订单的租金
        List<OrderProtocolInfoPo> orderProtocolInfoPos = orderProtocolInfoService.getOrderProtocolInfo(memberAddress.getMemberId());
        double rentAmount = 0; //租金
        int month = 0; //月份
        for (OrderProtocolInfoPo orderProtocolInfoPo : orderProtocolInfoPos) {
            //如果是借的话，则不计算租金
            if (orderProtocolInfoPo.getType().equals(OrderProtocolInfo.ProtocolType.BORROW.val())) continue;
            month = LocalDateTime.now().getMonthValue() - orderProtocolInfoPo.getLastRentPaymentTime().getMonthValue();
            if (month != 0) {
                rentAmount += month * orderProtocolInfoPo.getCurrentBottleNum() * storeInfo.getRent();
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("memberAddress", memberAddress);
        result.put("storeInfo", storeInfo);
        result.put("rentAmount", rentAmount / 100);
        return result;
    }

    /**
     * 根据协议号查找对应子订单信息（是协议主单，不是退瓶订单）
     *
     * @param protocolNo
     * @return
     */
    @Override
    public OrderSubInfoPo getOrderSubByProtocolNo(String protocolNo) {
        return orderSubInfoMapper.selectOne(new QueryWrapper<OrderSubInfoPo>().eq("protocol_no", protocolNo).eq("type", OrderSubInfo.EOrderType.ORDINARY.toString()));
    }

    /**
     * 获取店铺旧换新的订单(后台)
     *
     * @param orderSubInfoVo
     * @return
     */
    @Override
    public IPage<OrderSubInfoPo> getOldforNewOrderByStoreId(OrderSubInfoVo orderSubInfoVo) {
        AssertExt.notNull(orderSubInfoVo.getStoreId(), "店铺id为空");
        AssertExt.notNull(orderSubInfoVo.getCurrent(), "当前页为空");
        AssertExt.notNull(orderSubInfoVo.getSize(), "页码大小为空");

        return orderSubInfoMapper.getOldforNewOrderByStoreId(new Page<>(orderSubInfoVo.getCurrent(), orderSubInfoVo.getSize()), orderSubInfoVo);
    }


    /**
     * 旧换新订单绑定商品(后台)
     *
     * @param orderSubInfoVo
     */
    @Override
    @Transactional
    public void orderOldforNewBoundGoods(OrderSubInfoVo orderSubInfoVo) {
        AssertExt.isFalse(orderSubInfoVo.getOrderGoodsVos() == null || orderSubInfoVo.getOrderGoodsVos().size() == 0, "商品列表为空");
        AssertExt.notNull(orderSubInfoVo.getId(), "子订单id为空");
        for (OrderGoodsVo orderGoodsVo : orderSubInfoVo.getOrderGoodsVos()) {
            AssertExt.notBlank(orderGoodsVo.getSku(), "sku为空");
            AssertExt.notNull(orderGoodsVo.getNum(), "数量为空");
            AssertExt.notNull(orderGoodsVo.getBalance(), "差价为空");
            AssertExt.notBlank(orderGoodsVo.getTypesProcessing(), "旧换新处理类型为空");
            AssertExt.checkEnum(OrderGoods.ETypesProcessing.class, orderGoodsVo.getTypesProcessing(), "旧换新处理类型有误");
        }

        int amount = 0;     //支付订单的总金额
        int discountAmount = 0;     //支付订单的总优惠金额
        int actualAmount = 0;     //支付订单的总应付金额

        int orderSubAmount = 0;     //每个子订单的总金额
        int orderGoodAmount = 0;    //每个订单商品的总金额

        OrderSubInfoPo orderSubInfoPo = orderSubInfoMapper.selectById(orderSubInfoVo.getId());
        MemberAddress memberAddress = memberInfoService.getMemberAddressById(orderSubInfoPo.getAddressId());
        StoreInfo storeInfo = storeInfoService.getStoreById(orderSubInfoPo.getStoreId());

        orderSubInfoVo.setAddressId(orderSubInfoPo.getAddressId());
        orderSubInfoVo.setStoreId(orderSubInfoPo.getStoreId());
        orderSubInfoVo.setOrderInfoNo(orderSubInfoPo.getOrderInfoNo());
        GoodsPriceInfoVO goodsPriceInfoVO;
        List<OrderSubGoods> orderSubGoodsList = new ArrayList<>();
        List<OrderGoods> orderGoodsList = new ArrayList<>();
        OrderGoods orderGoods;
        OrderSubGoods orderSubGoods;
        List<StockVo> stockVos = new ArrayList<>();
        StockVo stockVo;

        for (OrderGoodsVo orderGoodsVo : orderSubInfoVo.getOrderGoodsVos()) {
            goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoVo.getStoreId(), orderSubInfoVo.getAddressId(), orderGoodsVo.getSku());
            if (goodsPriceInfoVO.getGoodsType() == 2 && goodsPriceInfoVO.getGoodsPriceInfoVOS() != null) {
                double amount1 = 0;
                for (GoodsPriceInfoVO goodsPriceInfoVO1 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                    amount1 += goodsPriceInfoVO1.getPrice();
                }
                goodsPriceInfoVO.setPrice(amount1);
            }


            orderGoods = new OrderGoods();
            orderGoods.setOrderSubId(orderSubInfoVo.getId());
            orderGoods.setSku(orderGoodsVo.getSku());
            orderGoods.setNum(orderGoodsVo.getNum());
            orderGoods.setTypesProcessing(orderGoodsVo.getTypesProcessing());
            orderGoods.setBalance(orderGoodsVo.getBalance());   //前端传的是分
            if (goodsPriceInfoVO.getPrice() * 100 - orderGoods.getBalance() < 0) {
                AssertExt.isFalse(true, "差价不能大于商品金额");
            }
            orderGoods.setPrice((int) (goodsPriceInfoVO.getPrice() * 100 - orderGoods.getBalance()));
            orderGoods.setAmount(BigDecimal.valueOf(goodsPriceInfoVO.getPrice() * 100 - orderGoods.getBalance()).multiply(BigDecimal.valueOf(orderGoodsVo.getNum())).intValue());
            orderGoods.setCreateUserId(memberAddress.getMemberId());
            orderGoods.setCreateTime(LocalDateTime.now());
            orderGoods.setUpdateUserId(memberAddress.getMemberId());
            orderGoods.setUpdateTime(LocalDateTime.now());
            orderGoods.setIsDel(0);
            orderGoodsList.add(orderGoods);

            //如果是非组合商品，则设置冗余单位
            if (goodsPriceInfoVO.getGoodsType() == 1) {
                for (GoodsAttributeValueVO goodsAttributeValueVO : goodsPriceInfoVO.getGoodsAttributeValueVOS()) {
                    if (goodsAttributeValueVO.getIsSale() == 1) {
                        orderGoods.setUnit(goodsAttributeValueVO.getAttrKeyName());
                    }
                }
            }

            if (goodsPriceInfoVO.getGoodsType() == 2 && goodsPriceInfoVO.getGoodsPriceInfoVOS() != null) {
                for (GoodsPriceInfoVO goodsPriceInfoVO1 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                    /**
                     * 子商品快照信息
                     */
                    orderSubGoods = new OrderSubGoods();
                    orderSubGoods.setOrderSubId(orderGoodsVo.getOrderSubId());
                    orderSubGoods.setSku(orderGoodsVo.getSku());
                    orderSubGoods.setSkuSub(goodsPriceInfoVO1.getSku());
                    orderSubGoods.setNum(orderGoodsVo.getNum());
                    orderSubGoods.setPrice((int) (double) (goodsPriceInfoVO1.getPrice() * 100));
                    orderSubGoods.setGoodsStyle(goodsPriceInfoVO1.getGoodsStyle());
                    orderSubGoods.setCreateUserId(memberAddress.getMemberId());
                    orderSubGoods.setCreateTime(LocalDateTime.now());
                    orderSubGoods.setUpdateUserId(memberAddress.getMemberId());
                    orderSubGoods.setUpdateTime(LocalDateTime.now());
                    orderSubGoodsList.add(orderSubGoods);
                }
            }


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


        /**
         * 快照表操作
         */
        if (orderGoodsList.size() != 0) {
            orderGoodsMapper.createOrderGoods(orderGoodsList);  //主商品快照insert
            if (orderSubGoodsList.size() != 0) {     //子商品快照insert
                orderSubGoodsService.createOrderSubGoods(orderSubGoodsList);
            }
        }


        /**
         * 修改主子单信息
         */
        int goodsNum = 0;   //瓶数（用来计算运费）
        //计算总金额（单价*数量）
        for (OrderGoods orderGoods1 : orderGoodsList) {
            goodsNum += orderGoods1.getNum();
            orderGoodAmount = orderGoods1.getPrice() * orderGoods1.getNum();
            orderSubAmount += orderGoodAmount;
        }

        //非自提
        if (orderSubInfoVo.getDeliveryType().equals(OrderSubInfo.EDeliveryType.EXPRESS.toString())) {
            //非自提,默认一小时配送时间
            orderSubInfoVo.setBookStartTime(LocalDateTime.now());
            orderSubInfoVo.setBookEndTime(LocalDateTime.now().plusHours(1));
            //该子订单的运费：地址楼层*店铺设置的运费*瓶数
            orderSubInfoVo.setExpressAmount(memberAddress.getStorey() * storeInfo.getCarriage() * goodsNum);
            if (memberAddress.getStorey() < 0) {     //电梯,不需要*楼层
                orderSubInfoVo.setExpressAmount(storeInfo.getCarriage() * goodsNum);
            }
        } else {
            //该子订单的运费：0
            orderSubInfoVo.setExpressAmount(0);
        }

        //该子订单总金额(商品总金额)
        orderSubInfoVo.setAmount(orderSubAmount);

        //该子订单的租金
        List<OrderProtocolInfoPo> orderProtocolInfoPos = orderProtocolInfoService.getOrderProtocolInfo(memberAddress.getMemberId());
        int rentAmount = 0; //租金
        int month = 0; //月份

        for (OrderProtocolInfoPo orderProtocolInfoPo : orderProtocolInfoPos) {
            //如果是借的话，则不计算租金
            if (orderProtocolInfoPo.getType().equals(OrderProtocolInfo.ProtocolType.BORROW.val())) continue;
            month = LocalDateTime.now().getMonthValue() - orderProtocolInfoPo.getLastRentPaymentTime().getMonthValue();
            if (month != 0) {
                rentAmount += month * orderProtocolInfoPo.getCurrentBottleNum() * storeInfo.getRent();
            }
        }

        orderSubInfoVo.setRentAmount(rentAmount);   //设置租金

        //该子订单应付金额(注：优惠金额未计算)
        orderSubInfoVo.setActualAmount(orderSubInfoVo.getAmount() + orderSubInfoVo.getExpressAmount() + orderSubInfoVo.getRentAmount() - 0);//优惠券金额未减（功能未做），用0代替

        //支付订单总金额   (每个订单的商品金额(押金) + 运费 + 租金)
        amount += orderSubAmount;
        //支付订单总应付金额
        actualAmount += orderSubInfoVo.getActualAmount();


        //修改子订单数据,将订单类型修改为普通订单,状态改为已处理
        orderSubInfoVo.setType(OrderSubInfo.EOrderType.ORDINARY.val());
        orderSubInfoVo.setOrderStatus(OrderSubInfo.EOrderSubStatus.PROCESSED.val());
        orderSubInfoVo.setUpdateTime(LocalDateTime.now());
        orderSubInfoMapper.updateOrderSubByOldforNew(orderSubInfoVo);

        //修改主订单数据
        orderInfoService.updateOrderByOldforNew(orderSubInfoVo.getOrderInfoNo(), amount, actualAmount, LocalDateTime.now());
    }


    /**
     * 从订单中获取生成订单结算报表需要的数据
     *
     * @return
     */
    @Override
    public List<OrderSubInfoPo> getOrderDataForReport(LocalDateTime now) {
        return orderSubInfoMapper.getOrderDataForReport(now);
    }


    /**
     * 修改子订单数据(后台)
     *
     * @param orderSubInfoVo
     */
    @Override
    public void updateOrdersubData(OrderSubInfoVo orderSubInfoVo) {
        AssertExt.notNull(orderSubInfoVo.getId(), "子订单id为空");
        AssertExt.notBlank(orderSubInfoVo.getConsignee(), "收货人为空");
        AssertExt.notBlank(orderSubInfoVo.getConsigneePhone(), "收货人电话为空");
        AssertExt.notBlank(orderSubInfoVo.getConsigneeAddress(), "收货人地址为空");
        AssertExt.notNull(orderSubInfoVo.getBookStartTime(), "开始配送时间为空");
        AssertExt.notNull(orderSubInfoVo.getBookEndTime(), "结束配送时间为空");
        AssertExt.notBlank(orderSubInfoVo.getOrderStatus(), "子订单状态为空");
        AssertExt.checkEnum(OrderSubInfo.EOrderSubStatus.class, orderSubInfoVo.getOrderStatus(), "子订单状态有误");
        orderSubInfoMapper.updateOrdersubData(orderSubInfoVo);
    }


    /**
     * 催单(后台)
     *
     * @param params
     */
    @Override
    public void orderOeminderBack(Map params) {
        AssertExt.isFalse(params.get("id") == null || String.valueOf(params.get("id")).equals(""), "订单id不能为空");
        String orderId = String.valueOf(params.get("id"));
        Map map = new HashMap();
        map.put("orderId", Long.parseLong(orderId));
        map.put("updateTime", LocalDateTime.now());
        orderSubInfoMapper.orderReminderBack(map);  //次数+2
    }


    /**
     * 接单列表（配送端）
     *
     * @param orderSubInfoVo
     * @return
     */
    @Override
    public IPage<OrderSubInfoPo> getOrderTakingList(OrderSubInfoVo orderSubInfoVo) {
        AssertExt.notNull(orderSubInfoVo.getPostmanId(), "配送员id为空");

        //拿到该配送员所有的店铺信息
        List<StoreDeliveryRelation> storeDeliveryRelations = storeDeliveryRelationMapper.selectList(new QueryWrapper<StoreDeliveryRelation>().eq("master_id", orderSubInfoVo.getPostmanId()));

        //获取该配送员所有的店铺的配送订单(把已经被别的配送员接单的订单排除)
        IPage<OrderSubInfoPo> page = new Page<>(orderSubInfoVo.getCurrent(), orderSubInfoVo.getSize());
        IPage<OrderSubInfoPo> orderSubInfoPoIPage = orderSubInfoMapper.getOrderTakingList(page, storeDeliveryRelations);

        GoodsPriceInfoVO goodsPriceInfoVO;
        for (OrderSubInfoPo orderSubInfoPo : orderSubInfoPoIPage.getRecords()) {
            List<OrderGoodsPo> orderGoodsPos = orderGoodsService.getOrderGoodsByOrderSubId(orderSubInfoPo.getId());
            //为每个订单商品快照取到具体的商品信息
            for (OrderGoodsPo orderGoodsPo : orderGoodsPos) {
                //商品模块提供接口：根据sku返回商品信息
                goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());
                goodsPriceInfoVO.setGoodsPriceInfoVOS(null);
                orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);

                //金额：单位分转元
                orderGoodsPo.setPrice(orderGoodsPo.getPrice() / 100);
                orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
                orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
                orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);

                /**
                 * 子商品信息
                 */
                List<OrderSubGoodsPo> orderSubGoodsPos = orderSubGoodsMapper.selectList(new QueryWrapper<OrderSubGoodsPo>().eq("order_sub_id", orderSubInfoPo.getId()).eq("sku", orderGoodsPo.getSku()));

                for (OrderSubGoodsPo orderSubGoodsPo : orderSubGoodsPos) {
                    goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderSubGoodsPo.getSkuSub());
                    goodsPriceInfoVO.setGoodsPriceInfoVOS(null);
                    orderSubGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);

                    //金额：单位分转元
                    orderSubGoodsPo.setPrice(orderSubGoodsPo.getPrice() / 100);

                }
                orderGoodsPo.setOrderSubGoodsPos(orderSubGoodsPos);
            }
            orderSubInfoPo.setOrderGoodsPos(orderGoodsPos); //把商品快照集合设置到子订单

            orderSubInfoPo.setOrderStatus(OrderSubInfo.EOrderSubStatus.valueOf(orderSubInfoPo.getOrderStatus()).desc());
            orderSubInfoPo.setPayType(OrderInfo.EPayType.valueOf(orderSubInfoPo.getPayType()).desc());

            //金额：单位分转元
            orderSubInfoPo.setAmount(orderSubInfoPo.getAmount() / 100);
            orderSubInfoPo.setExpressAmount(orderSubInfoPo.getExpressAmount() / 100);
            orderSubInfoPo.setRentAmount(orderSubInfoPo.getRentAmount() / 100);
            orderSubInfoPo.setRefundAmount(orderSubInfoPo.getRefundAmount() / 100);
            orderSubInfoPo.setDiscountAmount(orderSubInfoPo.getDiscountAmount() / 100);
            orderSubInfoPo.setActualAmount(orderSubInfoPo.getActualAmount() / 100);
        }

        return orderSubInfoPoIPage;
    }


    /**
     * 配送员接单（配送端）
     *
     * @param orderSubInfoVo
     * @return
     */
    @Override
    public synchronized boolean orderTaking(OrderSubInfoVo orderSubInfoVo) {
        /**
         * synchronized此方法已被加锁,防止同一张订单都被接单
         */
        AssertExt.notNull(orderSubInfoVo.getId(), "子订单id为空");
        AssertExt.notNull(orderSubInfoVo.getPostmanId(), "配送员id为空");

        /**
         * 虽然获取订单列表的时候会把已经有配送员的订单排除掉，但是当获取到列表后。列表里的单还是会被人接单的
         * 接单前检测此单是否已被其他配送员接单
         */
        OrderSubInfoPo orderSubInfoPo = orderSubInfoMapper.selectById(orderSubInfoVo.getId());
        if (orderSubInfoPo.getPostmanId() != null) {
            /**
             * 已被接单
             */
            return false;
        }
        orderSubInfoVo.setOrderStatus(OrderSubInfo.EOrderSubStatus.SEND.val()); //修改订单状态
        orderSubInfoMapper.orderTaking(orderSubInfoVo);     //为订单绑定配送员
        return true;
    }


    /**
     * 送单列表和完成列表（配送端）
     *
     * @param orderSubInfoVo
     * @return
     */
    @Override
    public IPage<OrderSubInfoPo> getOrderByPostmanIdAndStatus(OrderSubInfoVo orderSubInfoVo) {
        AssertExt.notNull(orderSubInfoVo.getPostmanId(), "配送员id为空");
        AssertExt.notBlank(orderSubInfoVo.getOrderStatus(), "子订单状态为空");
        AssertExt.checkEnum(OrderSubInfo.EOrderSubStatus.class, orderSubInfoVo.getOrderStatus(), "子订单状态有误");

        IPage<OrderSubInfoPo> page = new Page<>(orderSubInfoVo.getCurrent(), orderSubInfoVo.getSize());
        IPage<OrderSubInfoPo> orderSubInfoPoIPage = orderSubInfoMapper.getOrderByPostmanIdAndStatus(page, orderSubInfoVo);

        GoodsPriceInfoVO goodsPriceInfoVO;
        for (OrderSubInfoPo orderSubInfoPo : orderSubInfoPoIPage.getRecords()) {
            List<OrderGoodsPo> orderGoodsPos = orderGoodsService.getOrderGoodsByOrderSubId(orderSubInfoPo.getId());
            //为每个订单商品快照取到具体的商品信息
            for (OrderGoodsPo orderGoodsPo : orderGoodsPos) {
                //商品模块提供接口：根据sku返回商品信息
                goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());
                goodsPriceInfoVO.setGoodsPriceInfoVOS(null);
                orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);

                //金额：单位分转元
                orderGoodsPo.setPrice(orderGoodsPo.getPrice() / 100);
                orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
                orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
                orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);

                /**
                 * 子商品信息
                 */
                List<OrderSubGoodsPo> orderSubGoodsPos = orderSubGoodsMapper.selectList(new QueryWrapper<OrderSubGoodsPo>().eq("order_sub_id", orderSubInfoPo.getId()).eq("sku", orderGoodsPo.getSku()));

                for (OrderSubGoodsPo orderSubGoodsPo : orderSubGoodsPos) {
                    goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderSubGoodsPo.getSkuSub());
                    goodsPriceInfoVO.setGoodsPriceInfoVOS(null);
                    orderSubGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);

                    //金额：单位分转元
                    orderSubGoodsPo.setPrice(orderSubGoodsPo.getPrice() / 100);

                }
                orderGoodsPo.setOrderSubGoodsPos(orderSubGoodsPos);
            }
            orderSubInfoPo.setOrderGoodsPos(orderGoodsPos); //把商品快照集合设置到子订单

            orderSubInfoPo.setOrderStatus(OrderSubInfo.EOrderSubStatus.valueOf(orderSubInfoPo.getOrderStatus()).desc());
            orderSubInfoPo.setPayType(OrderInfo.EPayType.valueOf(orderSubInfoPo.getPayType()).desc());

            //金额：单位分转元
            orderSubInfoPo.setAmount(orderSubInfoPo.getAmount() / 100);
            orderSubInfoPo.setExpressAmount(orderSubInfoPo.getExpressAmount() / 100);
            orderSubInfoPo.setRentAmount(orderSubInfoPo.getRentAmount() / 100);
            orderSubInfoPo.setRefundAmount(orderSubInfoPo.getRefundAmount() / 100);
            orderSubInfoPo.setDiscountAmount(orderSubInfoPo.getDiscountAmount() / 100);
            orderSubInfoPo.setActualAmount(orderSubInfoPo.getActualAmount() / 100);
        }

        return orderSubInfoPoIPage;
    }

    /**
     * 获取新租瓶的订单列表（后台）
     *
     * @param orderSubInfoVo
     * @return
     */
    @Override
    public IPage<OrderSubInfoPo> getNewOrderList(OrderSubInfoVo orderSubInfoVo) {
        AssertExt.notNull(orderSubInfoVo.getStoreId(), "店铺id为空");

        /**
         * 不用判断店铺类型是否是气是水还是菜，因为如果是菜店铺，是不会显示这个界面的，即不会这个接口
         * 所以店铺类型必定是气或水店
         */
        //还是判断一下，免得前端还真显示了
        StoreInfo storeInfo = storeInfoService.getStoreById(orderSubInfoVo.getStoreId());
        if (storeInfo.getStoreTypeId() != 0 && storeInfo.getStoreTypeId() != 1) {
            AssertExt.isFalse(true, "该店铺不是气店或者水店，不能添加协议");
        }
        IPage<OrderSubInfoPo> page = new Page<>(orderSubInfoVo.getCurrent(), orderSubInfoVo.getSize());
        IPage<OrderSubInfoPo> orderSubInfoPoIPage = orderSubInfoMapper.getNewOrderList(page, orderSubInfoVo.getStoreId());

        GoodsPriceInfoVO goodsPriceInfoVO;
        for (OrderSubInfoPo orderSubInfoPo : orderSubInfoPoIPage.getRecords()) {
            List<OrderGoodsPo> orderGoodsPos = orderGoodsService.getOrderGoodsByOrderSubId(orderSubInfoPo.getId());
            //为每个订单商品快照取到具体的商品信息
            for (OrderGoodsPo orderGoodsPo : orderGoodsPos) {
                //商品模块提供接口：根据sku返回商品信息
                goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());
                goodsPriceInfoVO.setGoodsPriceInfoVOS(null);
                orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);

                //金额：单位分转元
                orderGoodsPo.setPrice(orderGoodsPo.getPrice() / 100);
                orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
                orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
                orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);

                /**
                 * 子商品信息
                 */
                List<OrderSubGoodsPo> orderSubGoodsPos = orderSubGoodsMapper.selectList(new QueryWrapper<OrderSubGoodsPo>().eq("order_sub_id", orderSubInfoPo.getId()).eq("sku", orderGoodsPo.getSku()));

                for (OrderSubGoodsPo orderSubGoodsPo : orderSubGoodsPos) {
                    goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderSubGoodsPo.getSkuSub());
                    goodsPriceInfoVO.setGoodsPriceInfoVOS(null);
                    orderSubGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);

                    //金额：单位分转元
                    orderSubGoodsPo.setPrice(orderSubGoodsPo.getPrice() / 100);

                }
                orderGoodsPo.setOrderSubGoodsPos(orderSubGoodsPos);
            }
            orderSubInfoPo.setOrderGoodsPos(orderGoodsPos); //把商品快照集合设置到子订单

            orderSubInfoPo.setOrderStatus(OrderSubInfo.EOrderSubStatus.valueOf(orderSubInfoPo.getOrderStatus()).desc());

            //金额：单位分转元
            orderSubInfoPo.setAmount(orderSubInfoPo.getAmount() / 100);
            orderSubInfoPo.setExpressAmount(orderSubInfoPo.getExpressAmount() / 100);
            orderSubInfoPo.setRentAmount(orderSubInfoPo.getRentAmount() / 100);
            orderSubInfoPo.setRefundAmount(orderSubInfoPo.getRefundAmount() / 100);
            orderSubInfoPo.setDiscountAmount(orderSubInfoPo.getDiscountAmount() / 100);
            orderSubInfoPo.setActualAmount(orderSubInfoPo.getActualAmount() / 100);
        }

        return orderSubInfoPoIPage;
    }


    /**
     * 确认订单(后台)
     *
     * @param orderId
     */
    @Override
    @Transactional
    public void firmOrder(Long orderId) {

        /**
         * 红包操作
         */



        /**
         * 气和水都更为确认状态
         */
        Map params = new HashMap();
        params.put("status", OrderSubInfo.EOrderSubStatus.CONFIRMED.val());
        params.put("updateTime", LocalDateTime.now());
        params.put("orderId", orderId);
        orderSubInfoMapper.updateOrderSubStatus(params);

        /**
         * 如果是气，则扣库存
         */
        StoreInfo storeInfo = storeInfoService.getStoreById(orderSubInfoMapper.selectById(id).getStoreId());
        if (storeInfo.getStoreTypeId() != 0) {
            return;
        }

        List<StockVo> stockVos = new ArrayList<>(10);
        StockVo stockVo;
        Map map = new HashMap();
        map.put("orderId", id);
        OrderSubInfoPo orderSubInfoPo = getOrderSubInfoById(map);
        for (OrderGoodsPo orderGoodsPo : orderSubInfoPo.getOrderGoodsPos()) {
            GoodsPriceInfoVO goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());
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
        storeInfoService.storeGoodStock(stockVos, 2, 0l);
    }


    /**
     * 获取需要确认订单的订单列表(已分页)
     * @param orderSubInfoVo
     * @return
     */
    @Override
    public IPage<OrderSubInfoPo> firmOrderList(OrderSubInfoVo orderSubInfoVo) {
        AssertExt.notNull(orderSubInfoVo.getStoreId(), "店铺id不能为空");
        AssertExt.notNull(orderSubInfoVo.getCurrent(), "当前页不能为空");
        AssertExt.notNull(orderSubInfoVo.getSize(), "页码大小不能为空");

        Long storeTypeId = storeInfoService.getStoreById(orderSubInfoVo.getStoreId()).getStoreTypeId();
        if(storeTypeId.longValue() != 0 && storeTypeId.longValue() != 1){
            AssertExt.isFalse(true, "该店铺不是气店或水店");
        }

        QueryWrapper<OrderSubInfoPo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id", orderSubInfoVo.getStoreId());
        queryWrapper.eq("order_status", OrderSubInfo.EOrderSubStatus.DONE.val());
        queryWrapper.notIn("type",OrderSubInfo.EOrderType.OLDFORNEW.val());
        queryWrapper.eq("is_del", 0);
        queryWrapper.orderBy(true, false, "update_time");

        IPage<OrderSubInfoPo> OrderSubInfoPoIPage = orderSubInfoMapper.selectPage(new Page<>(orderSubInfoVo.getCurrent(), orderSubInfoVo.getSize()), queryWrapper);
        StoreInfo storeInfo;
        GoodsPriceInfoVO goodsPriceInfoVO;
        //遍历某个会员的某个订单状态的所有订单
        for (OrderSubInfoPo orderSubInfoPo : OrderSubInfoPoIPage.getRecords()) {

            storeInfo = storeInfoService.getStoreById(orderSubInfoPo.getStoreId());
            orderSubInfoPo.setDeliveryStaff(deliveryService.getDeliveryById(orderSubInfoPo.getPostmanId()));

            orderSubInfoPo.setStoreInfo(storeInfo); //设置店铺信息

            orderSubInfoPo.setOrderStatus(OrderSubInfo.EOrderSubStatus.valueOf(orderSubInfoPo.getOrderStatus()).desc());

            List<OrderGoodsPo> orderGoodsPos = orderGoodsService.getOrderGoodsByOrderSubId(orderSubInfoPo.getId());
            //为每个订单商品快照取到具体的商品信息
            for (OrderGoodsPo orderGoodsPo : orderGoodsPos) {
                //商品模块提供接口：根据sku返回商品信息
                goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());

                //金额：单位分转元
                orderGoodsPo.setPrice(orderGoodsPo.getPrice() / 100);
                orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
                orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
                orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);

                orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);
            }

            //金额：单位分转元
            orderSubInfoPo.setAmount(orderSubInfoPo.getAmount() / 100);
            orderSubInfoPo.setExpressAmount(orderSubInfoPo.getExpressAmount() / 100);
            orderSubInfoPo.setRentAmount(orderSubInfoPo.getRentAmount() / 100);
            orderSubInfoPo.setRefundAmount(orderSubInfoPo.getRefundAmount() / 100);
            orderSubInfoPo.setDiscountAmount(orderSubInfoPo.getDiscountAmount() / 100);
            orderSubInfoPo.setActualAmount(orderSubInfoPo.getActualAmount() / 100);

            orderSubInfoPo.setOrderGoodsPos(orderGoodsPos); //把商品快照集合设置到子订单
        }
        return OrderSubInfoPoIPage;
    }


    /**
     * 在返回批量插入数据的自增id里，根据店铺id获取子订单id
     *
     * @param map
     * @return
     */
    @Override
    public Long getOrderSubIdByStoreId(Map map) {
        return orderSubInfoMapper.getOrderSubIdByStoreId(map);
    }

    /**
     * 根据店铺id获取子订单信息（给店铺提供的接口所依赖）
     *
     * @param storeId
     * @return
     */
    @Override
    public List<OrderSubInfoPo> getOrderSubByStoreId(Long storeId) {
        return orderSubInfoMapper.selectList(new QueryWrapper<OrderSubInfoPo>().eq("store_id", storeId));
    }

    /**
     * 根据id查询
     *
     * @return
     */
    @Override
    public OrderSubInfoPo getOrderSubById(Long id) {
        return orderSubInfoMapper.selectOne(new QueryWrapper<OrderSubInfoPo>().eq("id", id).eq("is_del", 0));
    }

    /**
     * 删除子订单
     *
     * @param ids
     * @return
     */
    @Override
    public int delOrderSub(List<String> ids) {
        Map map = new HashMap();
        map.put("ids", ids);
        map.put("updateUserId", 1);
        map.put("updateTime", LocalDateTime.now());
        return orderSubInfoMapper.delOrderSub(map);
    }


    /**
     * 更新子订单状态
     */
    @Override
    public void updateOrderSubStatus(Map<String, Object> params) {
        AssertExt.isFalse(params.get("orderId") == null || "".equals(params.get("orderId")), "订单id不能为空");
        AssertExt.isFalse(params.get("status") == null || "".equals(params.get("status")), "订单状态不能为空");
        String status = String.valueOf(params.get("status"));
        AssertExt.checkEnum(OrderSubInfo.EOrderSubStatus.class, status, "子订单类型错误");
        if (status.equals(OrderSubInfo.EOrderSubStatus.CANCEL.toString()) || status.equals(OrderSubInfo.EOrderSubStatus.PAID.toString())) {
            Map map = new HashMap();
            map.put("ids", (List<String>) params.get("ids"));
            map.put("status", String.valueOf(params.get("status")));
            map.put("updateTime", LocalDateTime.now());
            orderSubInfoMapper.updateOrderSubStatusByIds(map);
        } else {
            params.put("updateTime", LocalDateTime.now());
            orderSubInfoMapper.updateOrderSubStatus(params);
        }
    }


    /**
     * 根据子订单状态获取数据(已分页)
     *
     * @param orderSubInfoVo
     * @return
     */
    @Override
    public IPage<OrderSubInfoPo> getOrderSubInfosByStatus(OrderSubInfoVo orderSubInfoVo) {
        AssertExt.notNull(orderSubInfoVo.getCreateUserId(), "会员id不能为空");
        AssertExt.notNull(orderSubInfoVo.getCurrent(), "当前页不能为空");
        AssertExt.notNull(orderSubInfoVo.getSize(), "页码大小不能为空");
        QueryWrapper<OrderSubInfoPo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("create_user_id", orderSubInfoVo.getCreateUserId());
        if (!StringUtils.isBlank(orderSubInfoVo.getOrderStatus())) {
            AssertExt.checkEnum(OrderSubInfo.EOrderSubStatus.class, orderSubInfoVo.getOrderStatus(), "子订单类型错误");
            if (orderSubInfoVo.getOrderStatus().equals(OrderSubInfo.EOrderSubStatus.SEND.toString())) {
                queryWrapper.and(wrapper -> wrapper.eq("order_status", OrderSubInfo.EOrderSubStatus.SEND.toString())
                        .or().eq("order_status", OrderSubInfo.EOrderSubStatus.SHORTAGE.toString())
                        .or().eq("order_status", OrderSubInfo.EOrderSubStatus.PACK.toString())
                        .or().eq("order_status", OrderSubInfo.EOrderSubStatus.BEENSTOCK.toString())
                        .or().eq("order_status", OrderSubInfo.EOrderSubStatus.PROCESSED.toString())
                );
            } else if (orderSubInfoVo.getOrderStatus().equals(OrderSubInfo.EOrderSubStatus.DONE.toString())) {
                queryWrapper.and(wrapper -> wrapper.eq("order_status", OrderSubInfo.EOrderSubStatus.DONE.toString())
                        .or().eq("order_status", OrderSubInfo.EOrderSubStatus.CONFIRMED.toString()));
            } else {
                queryWrapper.eq("order_status", orderSubInfoVo.getOrderStatus());
            }
        }
        queryWrapper.eq("is_del", 0);
        queryWrapper.orderBy(true, false, "update_time");
        queryWrapper.eq("type", OrderSubInfo.EOrderType.ORDINARY.toString());   //普通订单

        IPage<OrderSubInfoPo> OrderSubInfoPoIPage = orderSubInfoMapper.selectPage(new Page<>(orderSubInfoVo.getCurrent(), orderSubInfoVo.getSize()), queryWrapper);
        StoreInfo storeInfo;
        GoodsPriceInfoVO goodsPriceInfoVO;
        //遍历某个会员的某个订单状态的所有订单
        for (OrderSubInfoPo orderSubInfoPo : OrderSubInfoPoIPage.getRecords()) {

            storeInfo = storeInfoService.getStoreById(orderSubInfoPo.getStoreId());
            orderSubInfoPo.setDeliveryStaff(deliveryService.getDeliveryById(orderSubInfoPo.getPostmanId()));

            orderSubInfoPo.setStoreInfo(storeInfo); //设置店铺信息

            if (orderSubInfoPo.getOrderStatus().equals(OrderSubInfo.EOrderSubStatus.SEND.toString())
                    || orderSubInfoPo.getOrderStatus().equals(OrderSubInfo.EOrderSubStatus.SHORTAGE.toString())
                    || orderSubInfoPo.getOrderStatus().equals(OrderSubInfo.EOrderSubStatus.PACK.toString())
                    || orderSubInfoPo.getOrderStatus().equals(OrderSubInfo.EOrderSubStatus.BEENSTOCK.toString())
                    || orderSubInfoPo.getOrderStatus().equals(OrderSubInfo.EOrderSubStatus.PROCESSED.toString())) {
                orderSubInfoPo.setOrderStatus(OrderSubInfo.EOrderSubStatus.SEND.desc());
            } else {
                orderSubInfoPo.setOrderStatus(OrderSubInfo.EOrderSubStatus.valueOf(orderSubInfoPo.getOrderStatus()).desc());
            }

            List<OrderGoodsPo> orderGoodsPos = orderGoodsService.getOrderGoodsByOrderSubId(orderSubInfoPo.getId());
            //为每个订单商品快照取到具体的商品信息
            for (OrderGoodsPo orderGoodsPo : orderGoodsPos) {
                //商品模块提供接口：根据sku返回商品信息
                goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());

                //金额：单位分转元
                orderGoodsPo.setPrice(orderGoodsPo.getPrice() / 100);
                orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
                orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
                orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);

                orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);
            }

            //金额：单位分转元
            orderSubInfoPo.setAmount(orderSubInfoPo.getAmount() / 100);
            orderSubInfoPo.setExpressAmount(orderSubInfoPo.getExpressAmount() / 100);
            orderSubInfoPo.setRentAmount(orderSubInfoPo.getRentAmount() / 100);
            orderSubInfoPo.setRefundAmount(orderSubInfoPo.getRefundAmount() / 100);
            orderSubInfoPo.setDiscountAmount(orderSubInfoPo.getDiscountAmount() / 100);
            orderSubInfoPo.setActualAmount(orderSubInfoPo.getActualAmount() / 100);


            orderSubInfoPo.setOrderGoodsPos(orderGoodsPos); //把商品快照集合设置到子订单
        }
        return OrderSubInfoPoIPage;
    }

    /**
     * 全部子订单(已分页)
     *
     * @param orderSubInfoVo
     * @return
     */
    @Override
    public IPage<OrderSubInfoPo> getOrderSubinfos(OrderSubInfoVo orderSubInfoVo) {
        AssertExt.notNull(orderSubInfoVo.getCreateUserId(), "会员id不能为空");
        AssertExt.notNull(orderSubInfoVo.getCurrent(), "当前页不能为空");
        AssertExt.notNull(orderSubInfoVo.getSize(), "页码大小不能为空");

        IPage<OrderSubInfoPo> page = new Page(orderSubInfoVo.getCurrent(), orderSubInfoVo.getSize());
        IPage<OrderSubInfoPo> OrderSubInfoPoIPage = orderSubInfoMapper.getOrderSubinfos(page, orderSubInfoVo.getCreateUserId(), OrderSubInfo.EOrderType.ORDINARY.toString());
        StoreInfo storeInfo;
        GoodsPriceInfoVO goodsPriceInfoVO;
        for (OrderSubInfoPo orderSubInfoPo : OrderSubInfoPoIPage.getRecords()) {

            storeInfo = storeInfoService.getStoreById(orderSubInfoPo.getStoreId());
            orderSubInfoPo.setDeliveryStaff(deliveryService.getDeliveryById(orderSubInfoPo.getPostmanId()));

            orderSubInfoPo.setStoreInfo(storeInfo); //设置店铺信息

            if (orderSubInfoPo.getOrderStatus().equals(OrderSubInfo.EOrderSubStatus.SEND.toString())
                    || orderSubInfoPo.getOrderStatus().equals(OrderSubInfo.EOrderSubStatus.SHORTAGE.toString())
                    || orderSubInfoPo.getOrderStatus().equals(OrderSubInfo.EOrderSubStatus.PACK.toString())
                    || orderSubInfoPo.getOrderStatus().equals(OrderSubInfo.EOrderSubStatus.BEENSTOCK.toString())
                    || orderSubInfoPo.getOrderStatus().equals(OrderSubInfo.EOrderSubStatus.PROCESSED.toString())) {
                orderSubInfoPo.setOrderStatus(OrderSubInfo.EOrderSubStatus.SEND.desc());
            } else {
                orderSubInfoPo.setOrderStatus(OrderSubInfo.EOrderSubStatus.valueOf(orderSubInfoPo.getOrderStatus()).desc());
            }

            List<OrderGoodsPo> orderGoodsPos = orderGoodsService.getOrderGoodsByOrderSubId(orderSubInfoPo.getId());
            //为每个订单商品快照取到具体的商品信息
            for (OrderGoodsPo orderGoodsPo : orderGoodsPos) {
                //商品模块提供接口：根据sku返回商品信息
                goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());

                //金额：单位分转元
                orderGoodsPo.setPrice(orderGoodsPo.getPrice() / 100);
                orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
                orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
                orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);

                orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);
            }

            //金额：单位分转元
            orderSubInfoPo.setAmount(orderSubInfoPo.getAmount() / 100);
            orderSubInfoPo.setExpressAmount(orderSubInfoPo.getExpressAmount() / 100);
            orderSubInfoPo.setRentAmount(orderSubInfoPo.getRentAmount() / 100);
            orderSubInfoPo.setRefundAmount(orderSubInfoPo.getRefundAmount() / 100);
            orderSubInfoPo.setDiscountAmount(orderSubInfoPo.getDiscountAmount() / 100);
            orderSubInfoPo.setActualAmount(orderSubInfoPo.getActualAmount() / 100);

            orderSubInfoPo.setOrderGoodsPos(orderGoodsPos); //把商品快照集合设置到子订单
        }
        return OrderSubInfoPoIPage;
    }

    /**
     * 根据子订单Id获取数据(子订单详情)
     *
     * @param map
     * @return
     */
    @Override
    public OrderSubInfoPo getOrderSubInfoById(Map map) {
        AssertExt.isFalse(map.get("orderId") == null || "".equals(map.get("orderId")), "订单id不能为空");
        String orderId = String.valueOf(map.get("orderId"));
        OrderSubInfoPo orderSubInfoPo = orderSubInfoMapper.getOrderSubById(Long.parseLong(orderId));

        StoreInfo storeInfo = storeInfoService.getStoreById(orderSubInfoPo.getStoreId());
        orderSubInfoPo.setDeliveryStaff(deliveryService.getDeliveryById(orderSubInfoPo.getPostmanId()));

        orderSubInfoPo.setStoreInfo(storeInfo); //设置店铺信息
        orderSubInfoPo.setType(OrderInfo.EPayType.valueOf(orderSubInfoPo.getPayType()).desc());

        if (orderSubInfoPo.getCustomerRemark() == null) {
            orderSubInfoPo.setCustomerRemark("");
        }
        if (orderSubInfoPo.getBackendRemark() == null) {
            orderSubInfoPo.setBackendRemark("");
        }

        List<OrderGoodsPo> orderGoodsPos = orderGoodsService.getOrderGoodsByOrderSubId(orderSubInfoPo.getId());
        GoodsPriceInfoVO goodsPriceInfoVO;
        //为每个订单商品快照取到具体的商品信息
        for (OrderGoodsPo orderGoodsPo : orderGoodsPos) {
            //商品模块提供接口：根据sku返回商品信息
            goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());

            //金额：单位分转元
            orderGoodsPo.setPrice(orderGoodsPo.getPrice() / 100);
            orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
            orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
            orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);

            orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);
        }

        //金额：单位分转元
        orderSubInfoPo.setAmount(orderSubInfoPo.getAmount() / 100);
        orderSubInfoPo.setExpressAmount(orderSubInfoPo.getExpressAmount() / 100);
        orderSubInfoPo.setRentAmount(orderSubInfoPo.getRentAmount() / 100);
        orderSubInfoPo.setRefundAmount(orderSubInfoPo.getRefundAmount() / 100);
        orderSubInfoPo.setDiscountAmount(orderSubInfoPo.getDiscountAmount() / 100);
        orderSubInfoPo.setActualAmount(orderSubInfoPo.getActualAmount() / 100);

        orderSubInfoPo.setOrderGoodsPos(orderGoodsPos); //把商品快照集合设置到子订单
        return orderSubInfoPo;
    }


    /**
     * 获取会员所有订单状态对应的数量
     *
     * @param memberId
     * @return
     */
    @Override
    public Map<String, Integer> getAllOrderStatusCount(Long memberId) {
        AssertExt.notNull(memberId, "会员id不能为空");
        Map<String, Integer> map = new HashMap<>();

        //待付款
        map.put(OrderInfo.EOrderStatus.UNPAID.toString(), orderInfoService.getAllOrderStatusCount(memberId, OrderInfo.EOrderStatus.UNPAID.toString()));

        //待发货
        map.put(OrderSubInfo.EOrderSubStatus.PAID.toString(), orderSubInfoMapper.getAllOrderStatusCount(memberId, OrderSubInfo.EOrderSubStatus.PAID.toString()));

        int sendCount = 0;  //已发货的数量 == SEND + PACK + BEENSTOCK + SHORTAGE + PROCESSED
        List<String> ls = new ArrayList<>();
        ls.add(OrderSubInfo.EOrderSubStatus.SEND.toString());//已发货(待签收)
        ls.add(OrderSubInfo.EOrderSubStatus.PACK.toString());
        ls.add(OrderSubInfo.EOrderSubStatus.BEENSTOCK.toString());
        ls.add(OrderSubInfo.EOrderSubStatus.SHORTAGE.toString());
        ls.add(OrderSubInfo.EOrderSubStatus.PROCESSED.toString());
        for (String status : ls) {
            sendCount += orderSubInfoMapper.getAllOrderStatusCount(memberId, status);
        }
        map.put(OrderSubInfo.EOrderSubStatus.SEND.toString(), sendCount);
        return map;
    }


    /**
     * 根据子订单号获取数据
     *
     * @param map
     * @return
     */
    @Override
    public OrderSubInfoPo getOrderSubInfoByOrderNo(Map map) {
        AssertExt.isFalse(map.get("orderNo") == null || "".equals(map.get("orderNo")), "订单号不能为空");
        String orderNo = String.valueOf(map.get("orderNo"));
        OrderSubInfoPo orderSubInfoPo = orderSubInfoMapper.selectOne(new QueryWrapper<OrderSubInfoPo>().eq("order_no", orderNo)
                .eq("type", OrderSubInfo.EOrderType.ORDINARY.toString()));//普通订单
        orderSubInfoPo.setDeliveryStaff(deliveryService.getDeliveryById(orderSubInfoPo.getPostmanId()));
        return orderSubInfoPo;
    }


    /**
     * 计算气订单的租金、押金、运费、优惠、总金额等(确认订单接口)
     *
     * @param orderInfoVo
     * @return
     */
    @Override
    public OrderInfoPo getGasCalculatedAmount(OrderInfoVo orderInfoVo) {
        AssertExt.notNull(orderInfoVo.getAddressId(), "店铺id不能为空");

        double amount = 0;     //支付订单的总金额
        double discountAmount = 0;     //支付订单的总优惠金额
        double actualAmount = 0;     //支付订单的总应付金额

        double orderSubAmount = 0;     //每个子订单的总金额
        double orderGoodAmount = 0;    //每个订单商品的总金额

        List<OrderSubInfoPo> orderSubInfoPos = new ArrayList<>();
        List<OrderGoodsPo> orderGoodsPos;
        OrderSubInfoPo orderSubInfoPo;
        OrderGoodsPo orderGoodsPo;
        GoodsPriceInfoVO goodsPriceInfoVO;
        StoreInfo storeInfo;
        //根据地址id拿到地址信息
        MemberAddress memberAddress = memberInfoService.getMemberAddressById(orderInfoVo.getAddressId());

        for (OrderSubInfoVo orderSubInfoVo : orderInfoVo.getOrderSubInfoVoList()) {
            orderSubInfoPo = new OrderSubInfoPo();
            orderGoodsPos = new ArrayList<>();
            //根据店铺id拿到店铺信息
            storeInfo = storeInfoService.getStoreById(orderSubInfoVo.getStoreId());
            storeInfo.setCreateTime(null);
            storeInfo.setUpdateTime(null);

            //该子订单的租金
            List<OrderProtocolInfoPo> orderProtocolInfoPos = orderProtocolInfoService.getOrderProtocolInfo(memberAddress.getMemberId());
            int rentAmount = 0; //租金
            int month = 0; //月份
            for (OrderProtocolInfoPo orderProtocolInfoPo : orderProtocolInfoPos) {
                //如果是借的话，则不计算租金
                if (orderProtocolInfoPo.getType().equals(OrderProtocolInfo.ProtocolType.BORROW.val())) continue;
                month = LocalDateTime.now().getMonthValue() - orderProtocolInfoPo.getLastRentPaymentTime().getMonthValue();
                if (month != 0) {
                    rentAmount += month * orderProtocolInfoPo.getCurrentBottleNum() * storeInfo.getRent();
                }
            }
            orderSubInfoPo.setRentAmount(rentAmount);   //设置租金
            //如果是水店，租金0
            if (storeInfo.getStoreTypeId() == 1) {
                orderSubInfoPo.setRentAmount(0);   //设置租金
            }

            int goodsNum = 0;   //瓶数（用来计算运费）
            double deposit = 0;    //子订单总押金
            boolean isNewGas = true;   //是否是新租瓶的确认订单
            for (OrderGoodsVo orderGoodsVo : orderSubInfoVo.getOrderGoodsVos()) {
                orderGoodsPo = new OrderGoodsPo();
                //商品模块提供接口：根据sku(orderGoodsVo.getSku())拿到商品
                //模拟数据
                goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoVo.getStoreId(), orderInfoVo.getAddressId(), orderGoodsVo.getSku());

                if (goodsPriceInfoVO.getGoodsType() == 2 && goodsPriceInfoVO.getGoodsPriceInfoVOS() != null) {
                    double amount1 = 0;
                    isNewGas = false;    //是新租瓶的确认订单
                    for (GoodsPriceInfoVO goodsPriceInfoVO1 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                        amount1 += goodsPriceInfoVO1.getPrice();
                        if (goodsPriceInfoVO1.getGoodsStyle() == 1 || goodsPriceInfoVO1.getGoodsStyle() == 5) { //1是瓶，5是桶
                            //此商品是瓶，设为押金
                            orderGoodsPo.setDeposit(goodsPriceInfoVO1.getPrice());
                            deposit += goodsPriceInfoVO1.getPrice() * 100 * orderGoodsVo.getNum();
                        } else if (goodsPriceInfoVO1.getGoodsStyle() == 2 || goodsPriceInfoVO1.getGoodsStyle() == 3) { //2是气，3是水
                            orderGoodsPo.setGasPrice(goodsPriceInfoVO1.getPrice());
                        }
                    }
                    goodsPriceInfoVO.setPrice(amount1);
                }

                orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);
                goodsNum += orderGoodsVo.getNum();
                orderGoodAmount = goodsPriceInfoVO.getPrice() * 100 * orderGoodsVo.getNum();    //商品那边已经/100，这边*100好计算，然后再/100
                orderSubAmount += orderGoodAmount;
                orderGoodsPo.setAmount(orderGoodAmount - 0);    //优惠券金额未减（功能未做），用0代替
                orderGoodsPo.setSku(goodsPriceInfoVO.getSku());
                orderGoodsPo.setNum(orderGoodsVo.getNum());
                if (isNewGas) {
                    orderGoodsPo.setPrice(goodsPriceInfoVO.getPrice());
                }

                //金额：单位分转元
                orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
                orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
                orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);


                orderGoodsPos.add(orderGoodsPo);
            }

            /**
             * 非自提
             */
            if (orderSubInfoVo.getDeliveryType().equals(OrderSubInfo.EDeliveryType.EXPRESS.toString())) {
                //该子订单的运费：地址楼层*店铺设置的运费*瓶数
                orderSubInfoPo.setExpressAmount(memberAddress.getStorey() * storeInfo.getCarriage() * goodsNum);
                if (memberAddress.getStorey() < 0) {     //电梯,不需要*楼层
                    orderSubInfoPo.setExpressAmount(storeInfo.getCarriage() * goodsNum);
                }
            } else {
                //该子订单的运费：0
                orderSubInfoPo.setExpressAmount(0);
            }

            //该子订单总金额(商品总金额)
            orderSubInfoPo.setAmount(orderSubAmount);

            //子订单的总押金
            orderSubInfoPo.setDeposit(deposit);

            //该子订单的门店信息
            orderSubInfoPo.setStoreInfo(storeInfo);

            //该子订单应付金额(注：优惠金额未计算)
            orderSubInfoPo.setActualAmount(orderSubInfoPo.getAmount() + orderSubInfoPo.getRentAmount() + orderSubInfoPo.getExpressAmount() - 0);//优惠券金额未减（功能未做），用0代替

            orderSubInfoPo.setDiscountAmount(0);

            //支付订单总金额
            amount += orderSubAmount;

            //支付订单总应付金额
            actualAmount += orderSubInfoPo.getActualAmount();
            orderSubAmount = 0;

            orderSubInfoPo.setOrderGoodsPos(orderGoodsPos);

            orderSubInfoPo.setCustomerRemark("");

            //金额：单位分转元
            orderSubInfoPo.setAmount(orderSubInfoPo.getAmount() / 100);
            orderSubInfoPo.setExpressAmount(orderSubInfoPo.getExpressAmount() / 100);
            orderSubInfoPo.setRentAmount(orderSubInfoPo.getRentAmount() / 100);
            orderSubInfoPo.setRefundAmount(orderSubInfoPo.getRefundAmount() / 100);
            orderSubInfoPo.setDiscountAmount(orderSubInfoPo.getDiscountAmount() / 100);
            orderSubInfoPo.setActualAmount(orderSubInfoPo.getActualAmount() / 100);
            orderSubInfoPo.setDeposit(orderSubInfoPo.getDeposit() / 100);


            orderSubInfoPos.add(orderSubInfoPo);
        }


        OrderInfoPo orderInfoPo = new OrderInfoPo();
        orderInfoPo.setOrderSubInfoPos(orderSubInfoPos);
        orderInfoPo.setAmount(amount);
        orderInfoPo.setActualAmount(actualAmount);
        orderInfoPo.setDiscountAmount(0);
        orderInfoPo.setMemberAddress(memberAddress);

        orderInfoPo.setAmount(orderInfoPo.getAmount() / 100);
        orderInfoPo.setDiscountAmount(orderInfoPo.getDiscountAmount() / 100);
        orderInfoPo.setActualAmount(orderInfoPo.getActualAmount() / 100);

        List<PayTypePo> ls = new ArrayList<>();
        PayTypePo payTypePo = new PayTypePo();
        payTypePo.setName(OrderInfo.EPayType.ONLINE.desc());
        payTypePo.setValue(OrderInfo.EPayType.ONLINE.toString());
        PayTypePo payTypePo1 = new PayTypePo();
        payTypePo1.setName(OrderInfo.EPayType.OFFLINE.desc());
        payTypePo1.setValue(OrderInfo.EPayType.OFFLINE.toString());
        PayTypePo payTypePo2 = new PayTypePo();
        payTypePo2.setName(OrderInfo.EPayType.ACCOUNT.desc());
        payTypePo2.setValue(OrderInfo.EPayType.ACCOUNT.toString());
        ls.add(payTypePo);
        ls.add(payTypePo1);
        ls.add(payTypePo2);
        orderInfoPo.setPayTypes(ls);

        return orderInfoPo;
    }


    /**
     * 计算菜市场订单的运费、优惠、总金额等(确认订单接口)
     *
     * @param orderInfoVo
     * @return
     */
    @Override
    public OrderInfoPo getCalculatedAmount(OrderInfoVo orderInfoVo) {
        AssertExt.notNull(orderInfoVo.getAddressId(), "店铺id不能为空");

        double amount = 0;     //支付订单的总金额
        double discountAmount = 0;     //支付订单的总优惠金额
        double actualAmount = 0;     //支付订单的总应付金额

        double orderSubAmount = 0;     //每个子订单的总金额
        double orderGoodAmount = 0;    //每个订单商品的总金额

        List<OrderSubInfoPo> orderSubInfoPos = new ArrayList<>();
        List<OrderGoodsPo> orderGoodsPos;
        OrderSubInfoPo orderSubInfoPo;
        OrderGoodsPo orderGoodsPo;
        GoodsPriceInfoVO goodsPriceInfoVO;
        StoreInfo storeInfo;
        //根据地址id拿到地址信息
        MemberAddress memberAddress = memberInfoService.getMemberAddressById(orderInfoVo.getAddressId());

        for (OrderSubInfoVo orderSubInfoVo : orderInfoVo.getOrderSubInfoVoList()) {
            orderSubInfoPo = new OrderSubInfoPo();
            orderGoodsPos = new ArrayList<>();
            //根据店铺id拿到店铺信息
            storeInfo = storeInfoService.getStoreById(orderSubInfoVo.getStoreId());
            storeInfo.setCreateTime(null);
            storeInfo.setUpdateTime(null);

            for (OrderGoodsVo orderGoodsVo : orderSubInfoVo.getOrderGoodsVos()) {
                orderGoodsPo = new OrderGoodsPo();
                //商品模块提供接口：根据sku(orderGoodsVo.getSku())拿到商品
                //模拟数据
                goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoVo.getStoreId(), orderInfoVo.getAddressId(), orderGoodsVo.getSku());

                //goodsPriceInfoVO.getGoodsPriceInfoVOS() != null 判断：是快捷菜，但是没有子商品
                if (goodsPriceInfoVO.getGoodsType() == 2 && goodsPriceInfoVO.getGoodsPriceInfoVOS() != null) {
                    double amount1 = 0;
                    for (GoodsPriceInfoVO goodsPriceInfoVO1 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                        amount1 += goodsPriceInfoVO1.getPrice();
                    }
                    goodsPriceInfoVO.setPrice(amount1);
                }
                orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);
                orderGoodAmount = goodsPriceInfoVO.getPrice() * 100 * orderGoodsVo.getNum();
                orderSubAmount += orderGoodAmount;
                orderGoodsPo.setAmount(orderGoodAmount - 0);    //优惠券金额未减（功能未做），用0代替
                orderGoodsPo.setSku(goodsPriceInfoVO.getSku());
                orderGoodsPo.setNum(orderGoodsVo.getNum());
                orderGoodsPo.setPrice(goodsPriceInfoVO.getPrice());

                //金额：单位分转元
                orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
                orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
                orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);


                orderGoodsPos.add(orderGoodsPo);
            }

            /**
             * 非自提
             */
            if (orderSubInfoVo.getDeliveryType().equals(OrderSubInfo.EDeliveryType.EXPRESS.toString())) {
                //该子订单的运费：地址楼层*店铺设置的运费
                orderSubInfoPo.setExpressAmount(storeInfo.getCarriage() * memberAddress.getStorey());
                if (memberAddress.getStorey() < 0) {     //电梯,不需要*楼层
                    orderSubInfoPo.setExpressAmount(storeInfo.getCarriage());
                }
            } else {
                //该子订单的运费：0
                orderSubInfoPo.setExpressAmount(0);
            }

            //该子订单总金额(商品总金额)
            orderSubInfoPo.setAmount(orderSubAmount);

            //该子订单的门店信息
            orderSubInfoPo.setStoreInfo(storeInfo);

            //该子订单应付金额(注：优惠金额未计算)
            orderSubInfoPo.setActualAmount(orderSubInfoPo.getAmount() + orderSubInfoPo.getExpressAmount() - 0);//优惠券金额未减（功能未做），用0代替

            orderSubInfoPo.setDiscountAmount(0);

            //支付订单总金额
            amount += orderSubAmount;

            //支付订单总应付金额
            actualAmount += orderSubInfoPo.getActualAmount();
            orderSubAmount = 0;

            orderSubInfoPo.setOrderGoodsPos(orderGoodsPos);

            orderSubInfoPo.setCustomerRemark("");

            //金额：单位分转元
            orderSubInfoPo.setAmount(orderSubInfoPo.getAmount() / 100);
            orderSubInfoPo.setExpressAmount(orderSubInfoPo.getExpressAmount() / 100);
            orderSubInfoPo.setRentAmount(orderSubInfoPo.getRentAmount() / 100);
            orderSubInfoPo.setRefundAmount(orderSubInfoPo.getRefundAmount() / 100);
            orderSubInfoPo.setDiscountAmount(orderSubInfoPo.getDiscountAmount() / 100);
            orderSubInfoPo.setActualAmount(orderSubInfoPo.getActualAmount() / 100);


            orderSubInfoPos.add(orderSubInfoPo);
        }


        OrderInfoPo orderInfoPo = new OrderInfoPo();
        orderInfoPo.setOrderSubInfoPos(orderSubInfoPos);
        orderInfoPo.setAmount(amount);
        orderInfoPo.setActualAmount(actualAmount);
        orderInfoPo.setDiscountAmount(0);
        orderInfoPo.setMemberAddress(memberAddress);

        orderInfoPo.setAmount(orderInfoPo.getAmount() / 100);
        orderInfoPo.setDiscountAmount(orderInfoPo.getDiscountAmount() / 100);
        orderInfoPo.setActualAmount(orderInfoPo.getActualAmount() / 100);

        List<PayTypePo> ls = new ArrayList<>();
        PayTypePo payTypePo = new PayTypePo();
        payTypePo.setName(OrderInfo.EPayType.ONLINE.desc());
        payTypePo.setValue(OrderInfo.EPayType.ONLINE.toString());
        ls.add(payTypePo);
        orderInfoPo.setPayTypes(ls);

        return orderInfoPo;
    }


    /**
     * 是否可以催单
     *
     * @return
     */
    @Override
    public boolean orderIsAbleReminder(Map params) {
        AssertExt.isFalse(params.get("orderId") == null || "".equals(params.get("orderId")), "订单id不能为空");
        String orderId = String.valueOf(params.get("orderId"));
        OrderSubInfoPo orderSubInfoPo = orderSubInfoMapper.selectById(orderId);
        if (orderSubInfoPo.getUrgeTime() == null) {
            //如果为null，即表示用户尚未催过单
            Long time = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - orderSubInfoPo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            if (time > orderReminderTime) {
                //600000毫秒=10分钟
                return true;
            } else {
                return false;
            }
        }

        //如果为非null，即表示用户已经催过单，需要等10分钟才能继续催单
        Long time = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - orderSubInfoPo.getUrgeTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if (time > 600000) {
            //600000毫秒=10分钟
            return true;
        }
        return false;
    }

    /**
     * 催单
     *
     * @param params
     */
    @Override
    @Transactional
    public void orderReminder(Map params) {
        AssertExt.isFalse(params.get("orderId") == null || "".equals(params.get("orderId")), "订单id不能为空");
        String orderId = String.valueOf(params.get("orderId"));
        Map map = new HashMap();
        map.put("orderId", Long.parseLong(orderId));
        map.put("updateTime", LocalDateTime.now());
        map.put("urgeTime", LocalDateTime.now());   //更新催单时间
        orderSubInfoMapper.orderReminder(map);  //次数+1
    }


    /**
     * 获取店铺的订单(后台)
     *
     * @param orderSubInfoVo
     * @return
     */
    @Override
    public IPage<OrderSubInfoPo> getOrderSubInfosByStoreId(OrderSubInfoVo orderSubInfoVo) {
        AssertExt.notNull(orderSubInfoVo.getStoreId(), "店铺id不能为空");
        AssertExt.notNull(orderSubInfoVo.getCurrent(), "当前页不能为空");
        AssertExt.notNull(orderSubInfoVo.getSize(), "页码大小不能为空");

        QueryWrapper<OrderSubInfoPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_id", orderSubInfoVo.getStoreId());
        queryWrapper.notIn("type", OrderSubInfo.EOrderType.OLDFORNEW.val());    //排除旧换新订单
        queryWrapper.orderByDesc("update_time");

        //搜索条件
        if (!StringUtils.isBlank(orderSubInfoVo.getOrderNo()))
            queryWrapper.eq("order_no", orderSubInfoVo.getOrderNo());   //单号
        if (!StringUtils.isBlank(orderSubInfoVo.getConsignee()))
            queryWrapper.eq("consignee", orderSubInfoVo.getConsignee());    //收货人
        if (!StringUtils.isBlank(orderSubInfoVo.getConsigneePhone()))
            queryWrapper.eq("consignee_phone", orderSubInfoVo.getConsigneePhone());     //收货电话
        if (!StringUtils.isBlank(orderSubInfoVo.getOrderStatus()))
            queryWrapper.eq("order_status", orderSubInfoVo.getOrderStatus());    //子订单状态

        if (orderSubInfoVo.getOrderStartTime() != null && orderSubInfoVo.getOrderEndTime() != null) {
            queryWrapper.between("create_time", orderSubInfoVo.getOrderStartTime(), orderSubInfoVo.getOrderEndTime());
        }

        IPage<OrderSubInfoPo> iPage;
        StoreInfo storeInfo;
        GoodsPriceInfoVO goodsPriceInfoVO;
        iPage = orderSubInfoMapper.selectPage(new Page<>(orderSubInfoVo.getCurrent(), orderSubInfoVo.getSize()), queryWrapper);

        if (iPage.getRecords().size() == 0) {
            //可能按照子订单号来查，没有数据，再按照主订单号来查一遍，条件都是一样的
            QueryWrapper<OrderSubInfoPo> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("store_id", orderSubInfoVo.getStoreId());
            queryWrapper.notIn("type", OrderSubInfo.EOrderType.OLDFORNEW.val());    //排除旧换新订单
            queryWrapper2.orderByDesc("update_time");

            //搜索条件
            if (!StringUtils.isBlank(orderSubInfoVo.getOrderNo()))
                queryWrapper2.eq("order_info_no", orderSubInfoVo.getOrderNo());   //单号
            if (!StringUtils.isBlank(orderSubInfoVo.getConsignee()))
                queryWrapper2.eq("consignee", orderSubInfoVo.getConsignee());    //收货人
            if (!StringUtils.isBlank(orderSubInfoVo.getConsigneePhone()))
                queryWrapper2.eq("consignee_phone", orderSubInfoVo.getConsigneePhone());     //收货电话
            if (!StringUtils.isBlank(orderSubInfoVo.getOrderStatus()))
                queryWrapper2.eq("order_status", orderSubInfoVo.getOrderStatus());    //子订单状态

            if (orderSubInfoVo.getOrderStartTime() != null && orderSubInfoVo.getOrderEndTime() != null) {
                queryWrapper2.between("create_time", orderSubInfoVo.getOrderStartTime(), orderSubInfoVo.getOrderEndTime());
            }

            iPage = orderSubInfoMapper.selectPage(new Page<>(orderSubInfoVo.getCurrent(), orderSubInfoVo.getSize()), queryWrapper2);
        }


        for (OrderSubInfoPo orderSubInfoPo : iPage.getRecords()) {

            storeInfo = storeInfoService.getStoreById(orderSubInfoVo.getStoreId());

            orderSubInfoPo.setStoreInfo(storeInfo); //设置店铺信息
            orderSubInfoPo.setOrderStatus(OrderSubInfo.EOrderSubStatus.valueOf(orderSubInfoPo.getOrderStatus()).desc());
            orderSubInfoPo.setOrderIsAbleReminderBack(
                    orderSubInfoPo.getOrderStatus().equals(OrderSubInfo.EOrderSubStatus.UNPAID.desc())
                            || orderSubInfoPo.getOrderStatus().equals(OrderSubInfo.EOrderSubStatus.CANCEL.desc()) ? false : true);

            List<OrderGoodsPo> orderGoodsPos = orderGoodsService.getOrderGoodsByOrderSubId(orderSubInfoPo.getId());
            //为每个订单商品快照取到具体的商品信息
            for (OrderGoodsPo orderGoodsPo : orderGoodsPos) {
                //商品模块提供接口：根据sku返回商品信息
                goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderGoodsPo.getSku());
                goodsPriceInfoVO.setGoodsPriceInfoVOS(null);
                orderGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);

                //金额：单位分转元
                orderGoodsPo.setPrice(orderGoodsPo.getPrice() / 100);
                orderGoodsPo.setAmount(orderGoodsPo.getAmount() / 100);
                orderGoodsPo.setDiscountAmount(orderGoodsPo.getDiscountAmount() / 100);
                orderGoodsPo.setBalance(orderGoodsPo.getBalance() / 100);

                /**
                 * 子商品信息
                 */
                List<OrderSubGoodsPo> orderSubGoodsPos = orderSubGoodsMapper.selectList(new QueryWrapper<OrderSubGoodsPo>().eq("order_sub_id", orderSubInfoPo.getId()).eq("sku", orderGoodsPo.getSku()));

                for (OrderSubGoodsPo orderSubGoodsPo : orderSubGoodsPos) {
                    goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(orderSubInfoPo.getStoreId(), orderSubInfoPo.getAddressId(), orderSubGoodsPo.getSkuSub());
                    goodsPriceInfoVO.setGoodsPriceInfoVOS(null);
                    orderSubGoodsPo.setGoodsPriceInfoVO(goodsPriceInfoVO);

                    //金额：单位分转元
                    orderSubGoodsPo.setPrice(orderSubGoodsPo.getPrice() / 100);

                }
                orderGoodsPo.setOrderSubGoodsPos(orderSubGoodsPos);
            }
            orderSubInfoPo.setOrderGoodsPos(orderGoodsPos); //把商品快照集合设置到子订单

            //金额：单位分转元
            orderSubInfoPo.setAmount(orderSubInfoPo.getAmount() / 100);
            orderSubInfoPo.setExpressAmount(orderSubInfoPo.getExpressAmount() / 100);
            orderSubInfoPo.setRentAmount(orderSubInfoPo.getRentAmount() / 100);
            orderSubInfoPo.setRefundAmount(orderSubInfoPo.getRefundAmount() / 100);
            orderSubInfoPo.setDiscountAmount(orderSubInfoPo.getDiscountAmount() / 100);
            orderSubInfoPo.setActualAmount(orderSubInfoPo.getActualAmount() / 100);
        }
        return iPage;
    }


    /**
     * 获取某个协议的退瓶订单列表
     *
     * @param current
     * @param size
     * @param protocolNo
     * @return
     */
    @Override
    public IPage<OrderSubInfoPo> getProtocolRefundOrder(Long current, Long size, String protocolNo) {
        return orderSubInfoMapper.selectPage(new Page<>(current, size),
                new QueryWrapper<OrderSubInfoPo>().eq("protocol_no", protocolNo)
                        .eq("type", OrderSubInfo.EOrderType.BOTTLEBACK.toString()).orderByDesc("create_time"));
    }


}
