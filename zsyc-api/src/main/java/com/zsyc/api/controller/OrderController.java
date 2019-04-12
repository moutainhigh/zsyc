package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.api.AccountHelper;
import com.zsyc.order.entity.OrderGoods;
import com.zsyc.order.entity.OrderInfo;
import com.zsyc.order.entity.OrderProtocolInfo;
import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.order.po.OrderGoodsPo;
import com.zsyc.order.po.OrderInfoPo;
import com.zsyc.order.po.OrderSettlementReportPo;
import com.zsyc.order.po.OrderSubInfoPo;
import com.zsyc.order.service.OrderGoodsService;
import com.zsyc.order.service.OrderInfoService;
import com.zsyc.order.service.OrderSettlementReportService;
import com.zsyc.order.service.OrderSubInfoService;
import com.zsyc.order.utils.OrderUtil;
import com.zsyc.order.vo.*;
import com.zsyc.pay.vo.PayOrderVo;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @program: zsyc-parent
 * @description: 订单API
 * @author: Mr.Ning
 * @create: 2019-01-14 20:31
 **/
@RestController
@RequestMapping("api/order")
@Api
public class OrderController extends BaseController{

    @Reference
    private OrderInfoService orderInfoService;

    @Reference
    private OrderSubInfoService orderSubInfoService;

    @Reference
    private OrderGoodsService orderGoodsService;

    @Reference
    private OrderSettlementReportService orderSettlementReportService;

    @Resource
    private AccountHelper accountHelper;


    /**
     *
     * 小程序接口
     *
     */

    @ApiOperation("全部主订单(已分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小", required = true, dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getOrderinfos")
    public IPage<OrderInfoPo> getOrderinfos(@ApiIgnore OrderInfoVo orderInfoVo) {
        orderInfoVo.setCreateUserId(accountHelper.getMember().getId());
        return orderInfoService.getOrderinfos(orderInfoVo);
    }

    @ApiOperation("全部子订单(已分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小", required = true, dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getOrdersubinfos")
    public IPage<OrderSubInfoPo> getOrderSubinfos(@ApiIgnore OrderSubInfoVo orderSubInfoVo) {
        orderSubInfoVo.setCreateUserId(accountHelper.getMember().getId());
        return orderSubInfoService.getOrderSubinfos(orderSubInfoVo);
    }


    @ApiOperation("根据主订单状态获取数据(已分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderStatus", value = "主订单状态(UNPAID(待支付), SUCCESS(支付成功),CANCEL(取消支付),FAIL(支付失败),ERROR(系统错误))", dataType = "string")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getOrderinfosBystatus")
    public IPage<OrderInfoPo> getOrderInfosByStatus(@ApiIgnore OrderInfoVo orderInfoVo) {
        orderInfoVo.setCreateUserId(accountHelper.getMember().getId());
        return orderInfoService.getOrderInfosByStatus(orderInfoVo);
    }

    @ApiOperation("根据主订单Id获取数据(主订单详情)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "orderId", value = "主订单Id", required = true, dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getOrderinfoByid")
    public OrderInfoPo getOrderInfoById(@ApiIgnore @RequestParam Map map) {
        return orderInfoService.getOrderInfoById(map);
    }


    @ApiOperation("根据子订单状态获取数据(已分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderStatus", value = "子订单状态(UNPAID(\"待支付\"), PAID(\"支付成功\"),CANCEL(\"取消支付\"),PACK(\"分拣\"),SEND(\"发货\"),DONE(\"完成\"),SHORTAGE(\"缺货\"),REFUND(\"退款(退瓶)\"),APPLY(\"申请退瓶中\"),PROCESSED(\"客服已处理\"))", dataType = "string")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getOrdersubinfosBystatus")
    public IPage<OrderSubInfoPo> getOrderSubInfosByStatus(@ApiIgnore OrderSubInfoVo orderSubInfoVo) {
        orderSubInfoVo.setCreateUserId(accountHelper.getMember().getId());
        return orderSubInfoService.getOrderSubInfosByStatus(orderSubInfoVo);
    }

    @ApiOperation("根据子订单Id获取数据(子订单详情)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "orderId", value = "子订单Id", required = true, dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getOrdersubinfoByid")
    public OrderSubInfoPo getOrderSubInfoById(@ApiIgnore @RequestParam Map map) {
        return orderSubInfoService.getOrderSubInfoById(map);
    }



    @ApiOperation("计算气订单的租金、押金、运费、优惠、总金额等(确认订单接口)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "addressId", value = "地址id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVoList[].storeId", value = "店铺id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVoList[].orderGoodsVos[].sku", value = "商品sku",allowMultiple = true, required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVoList[].orderGoodsVos[].num", value = "商品数量",allowMultiple = true, required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVoList[].deliveryType", value = "配送类型(EXPRESS(\"配送\"), SELF(\"自提\")，默认配送)",allowMultiple = true, required = true, dataType = "String")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("getGasCalculatedAmount")
    public OrderInfoPo getGasCalculatedAmount(@ApiIgnore @RequestBody OrderInfoVo orderInfoVo) {
        return orderSubInfoService.getGasCalculatedAmount(orderInfoVo);
    }

    @ApiOperation("计算菜市场订单的运费、优惠、总金额等(确认订单接口)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "addressId", value = "地址id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVoList[].storeId", value = "店铺id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVoList[].orderGoodsVos[].sku", value = "商品sku",allowMultiple = true, required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVoList[].orderGoodsVos[].num", value = "商品数量",allowMultiple = true, required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVoList[].deliveryType", value = "配送类型(EXPRESS(\"配送\"), SELF(\"自提\"))",allowMultiple = true, required = true, dataType = "String")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("getCalculatedAmount")
    public OrderInfoPo getCalculatedAmount(@ApiIgnore @RequestBody OrderInfoVo orderInfoVo) {
        return orderSubInfoService.getCalculatedAmount(orderInfoVo);
    }


    @ApiOperation("创建支付订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "orderCategory", value = "订单类型（订单类型：菜市场和快捷菜订单为0，气订单为1）", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "gasOrderCategory", value = "气订单类型：1互换瓶，2新租瓶，3旧换新", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "addressId", value = "地址", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "photo", value = "旧换新订单上传的图片地址", dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "payType", value = "支付方式(ONLINE(\"微信支付\"), OFFLINE(\"货到付款\"),ACCOUNT(\"账期支付\"))，旧换新不用传该参数", dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVos[].storeId", value = "店铺id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVos[].customerRemark", value = "客户备注", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVos[].bookStartTime", value = "开始配送时间(如果是自提则需要配送时间，配送不用)", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVos[].bookEndTime", value = "结束配送时间(如果是自提则需要配送时间，配送不用)", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVos[].orderGoodsVos[].sku", value = "商品sku",allowMultiple = true, required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVos[].orderGoodsVos[].num", value = "商品数量",allowMultiple = true, required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVos[].deliveryType", value = "配送类型(EXPRESS(\"配送\"), SELF(\"自提\"))",allowMultiple = true, required = true, dataType = "String")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("createOrderInfo")
    public String createOrderInfo(@ApiIgnore @RequestBody OrderVo orderVo) {
        return orderInfoService.createOrderInfo(orderVo);
    }

    @ApiOperation("订单支付")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "orderNo", value = "主订单号", required = true, dataType = "String")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("payOrder")
    public PayOrderVo payOrder(@ApiIgnore @RequestBody PayOrderVo payOrder, HttpServletRequest request) {
        return orderInfoService.payOrder(payOrder,accountHelper.getMember().getAccountId(),super.getIP(request));
    }


    @ApiOperation("更改主订单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "orderId", value = "主订单id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "status", value = "主订单状态(UNPAID(待支付), SUCCESS(支付成功),CANCEL(取消支付),FAIL(支付失败),ERROR(系统错误))", required = true, dataType = "String")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("updateOrderStatus")
    public void updateOrderStatus(@ApiIgnore @RequestBody Map params) {
        params.put("memberId", accountHelper.getMember().getId());
        orderInfoService.updateOrderStatus(params);
    }


    @ApiOperation("更改子订单状态(发货-缺货-分拣等)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "orderId", value = "子订单id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "status", value = "子订单状态(UNPAID(\"待支付\"), PAID(\"支付成功\"),CANCEL(\"取消支付\"),PACK(\"分拣\"),SEND(\"发货\"),DONE(\"完成\"),SHORTAGE(\"缺货\"),REFUND(\"退款(退瓶)\"),APPLY(\"申请退瓶中\"),PROCESSED(\"客服已处理\"))", required = true, dataType = "String")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("updateOrdersubStatus")
    public void updateOrderSubStatus(@ApiIgnore @RequestBody Map params) {
        orderSubInfoService.updateOrderSubStatus(params);
    }


    @ApiOperation("更改商品快照状态(确认、缺货)（给备货分拣提供的接口）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "orderGoodsId", value = "订单商品id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "status", value = "订单商品状态(DONE(\"确认\"),SHORTAGE(\"缺货\"))", required = true, dataType = "String")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("updateOrdergoodsStatus")
    public void updateOrderGoodsStatus(@ApiIgnore @RequestBody Map params) {
        orderGoodsService.updateOrderGoodsStatus(params);
    }


    @ApiOperation("是否可以催单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "orderId", value = "子订单id", required = true, dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("orderIsableReminder")
    public boolean orderIsAbleReminder(@ApiIgnore @RequestParam Map params) {
        return orderSubInfoService.orderIsAbleReminder(params);
    }


    @ApiOperation("催单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "orderId", value = "子订单id", required = true, dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("orderReminder")
    public void orderOeminder(@ApiIgnore @RequestBody Map params) {
        orderSubInfoService.orderReminder(params);
    }


    @ApiOperation("删除支付订单(逻辑删)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "orderId", value = "订单id", required = true, dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("delOrder")
    public void delOrderStatus(@ApiIgnore @RequestBody Map params) {
        orderInfoService.delOrder(params);
    }


    @ApiOperation("获取当前服务器时间（用于30分订单取消）")
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getTime")
    public LocalDateTime getTime() {
        return LocalDateTime.now();
    }


    @ApiOperation("获取会员所有订单状态对应的数量")
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getAllOrderStatusCount")
    public Map<String,Integer> getAllOrderStatusCount() {
        return orderSubInfoService.getAllOrderStatusCount(accountHelper.getMember().getId());
    }

    @ApiOperation("会员最近购买的商品")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小", required = true, dataType = "Long")})
    @GetMapping("getLatelygoodsByMember")
    public IPage<OrderGoodsPo> getLatelyGoodsByMember(@ApiIgnore OrderGoodsVo orderGoodsVo) {
        orderGoodsVo.setCreateUserId(accountHelper.getMember().getId());
        return orderGoodsService.getLatelyGoodsByMember(orderGoodsVo);
    }

    @ApiOperation("获取租金和运费信息")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "addressId", value = "地址id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "storeId", value = "店铺id", required = true, dataType = "Long")})
    @GetMapping("getRentamountAndExpressinfo")
    public Map<String,Object> getRentAmountAndExpressInfo(@ApiIgnore @RequestParam Map params) {
        return orderSubInfoService.getRentAmountAndExpressInfo(params);
    }


    /**
     *
     * 后台接口
     *
     */

    @ApiOperation("获取店铺旧换新的订单(后台)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "storeId", value = "店铺id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小", required = true, dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getOldfornewOrderBystoreId")
    public IPage<OrderSubInfoPo> getOldforNewOrderByStoreId(@ApiIgnore OrderSubInfoVo orderSubInfoVo) {
        return orderSubInfoService.getOldforNewOrderByStoreId(orderSubInfoVo);
    }

    @ApiOperation("旧换新订单绑定商品(后台)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "id", value = "子订单id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderGoodsVos[].sku", value = "sku", required = true, dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "orderGoodsVos[].num", value = "数量", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderGoodsVos[].balance", value = "差价", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderGoodsVos[].typesProcessing", value = "旧换新处理类型", required = true,dataType = "string")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("orderOldfornewBoundGoods")
    public void orderOldforNewBoundGoods(@ApiIgnore @RequestBody OrderSubInfoVo orderSubInfoVo) {
        orderSubInfoService.orderOldforNewBoundGoods(orderSubInfoVo);
    }


    @ApiOperation("获取店铺的订单(后台)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "orderNo", value = "订单号", dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "consignee", value = "收货人", dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "consigneePhone", value = "收货人电话", dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "orderStatus", value = "子订单状态", dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "orderStartTime", value = "订单创建开始时间", dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "orderEndTime", value = "订单创建结束时间", dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "storeId", value = "店铺id", required = true , dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小", required = true, dataType = "Long")
            })
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getOrdersubinfosBystoreId")
    public IPage<OrderSubInfoPo> getOrderSubInfosByStoreId(@ApiIgnore OrderSubInfoVo orderSubInfoVo) {
        return orderSubInfoService.getOrderSubInfosByStoreId(orderSubInfoVo);
    }


    @ApiOperation("获取需要确认订单的订单列表(已分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "storeId", value = "店铺id", required = true , dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小", required = true, dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("firmOrderList")
    public IPage<OrderSubInfoPo> firmOrderList(@ApiIgnore OrderSubInfoVo orderSubInfoVo) {
        return orderSubInfoService.firmOrderList(orderSubInfoVo);
    }

    @ApiOperation("确认订单(后台)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "id", value = "子订单id", dataType = "long"),
    })
    @ApiResponse(code = 200, message = "success")
    @PostMapping("firmOrder")
    public void firmOrder(@ApiIgnore Long id) {
        orderSubInfoService.firmOrder(id);
    }

    @ApiOperation("获取子订单的每个状态(后台)")
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getOrdersubStatus")
    public Map<Object,String> getOrderSubStatus() {
        return OrderUtil.EnumToMap(OrderSubInfo.EOrderSubStatus.class);
    }

    @ApiOperation("获取旧换新处理类型，补差-UP 补废-FILL 免差-FREE(后台)")
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getTypesProcessing")
    public Map<Object,String> getTypesProcessing() {
        return OrderUtil.EnumToMap(OrderGoods.ETypesProcessing.class);
    }


    @ApiOperation("获取报表数据(后台)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "storeId", value = "店铺id(不是必传的)", dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "reportStartTime", value = "开始结算时间(不是必传的)", dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "reportEndTime", value = "结束结算时间(不是必传的)", dataType = "string"),})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getReportData")
    public IPage<OrderSettlementReportPo> getReportData(@ApiIgnore OrderSettlementReportVo orderSettlementReportVo) {
        return orderSettlementReportService.getReportData(orderSettlementReportVo);
    }


    @ApiOperation("修改子订单数据(后台)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "id", value = "子订单id", required = true, dataType = "long"),
            @ApiImplicitParam(paramType="query",name = "consigneePhone", value = "收货人电话", required = true, dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "consigneeAddress", value = "收货人地址", required = true, dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "consignee", value = "收货人姓名", required = true, dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "bookStartTime", value = "开始配送时间", required = true, dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "bookEndTime", value = "结束配送时间", required = true, dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "orderStatus", value = "订单状态", required = true, dataType = "string")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("updateOrdersubData")
    public void updateOrdersubData(@ApiIgnore @RequestBody OrderSubInfoVo orderSubInfoVo) {
        orderSubInfoService.updateOrdersubData(orderSubInfoVo);
    }


    @ApiOperation("催单(后台)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "id", value = "子订单id", required = true, dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("orderReminderBack")
    public void orderOeminderBack(@ApiIgnore @RequestBody Map params) {
        orderSubInfoService.orderOeminderBack(params);
    }


    @ApiOperation("获取新租瓶的订单列表（后台）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "storeId", value = "店铺id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小", required = true, dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getNewOrderList")
    public IPage<OrderSubInfoPo> getNewOrderList(@ApiIgnore OrderSubInfoVo orderSubInfoVo) {
        return orderSubInfoService.getNewOrderList(orderSubInfoVo);
    }


    @ApiOperation("后台下单（后台）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "gasOrderCategory", value = "气订单类型：1互换瓶，2新租瓶，3旧换新", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "photo", value = "旧换新订单上传的图片地址", dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "addressId", value = "地址", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVos[].storeId", value = "店铺id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVos[].backendRemark", value = "后台备注备注", dataType = "String"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVos[].orderGoodsVos[].sku", value = "商品sku",allowMultiple = true, required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query",name = "orderSubInfoVos[].orderGoodsVos[].num", value = "商品数量",allowMultiple = true, required = true, dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("createOrderInfoBack")
    public Long createOrderInfoBack(@ApiIgnore @RequestBody OrderVo orderVo) {
        return orderInfoService.createOrderInfoBack(orderVo);
    }

    @GetMapping("test")
    public List<OrderInfo> test() {
       return orderInfoService.test();
    }


    /**
     *
     * 给其他模块提供的接口
     *
     */
    @ApiOperation("根据时间段获取子订单（给店铺提供的接口）")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "orderStartTime", value = "开始下单时间", required = true, dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "orderEndTime", value = "结束下单时间", required = true, dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "storeId", value = "店铺id", required = true, dataType = "Long")})
    @GetMapping("getOrdersubinfosByTimeslot")
    public List<OrderSubInfoPo> getOrderinfosByTimeSlot(@ApiIgnore OrderSubInfoVo orderSubInfoVo) {
        return orderSubInfoService.getOrderinfosByTimeSlot(orderSubInfoVo);
    }

    @ApiOperation("接单列表（配送端）")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "postmanId", value = "配送员id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小", required = true, dataType = "Long")})
    @GetMapping("getOrderTakingList")
    public IPage<OrderSubInfoPo> getOrderTakingList(@ApiIgnore OrderSubInfoVo orderSubInfoVo) {
        return orderSubInfoService.getOrderTakingList(orderSubInfoVo);
    }

    @ApiOperation("配送员接单（true则代表接单成功，false则代表此单已被别人抢先接单了）（配送端）")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "id", value = "子订单id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "postmanId", value = "配送员id", required = true, dataType = "Long")})
    @PostMapping("orderTaking")
    public boolean orderTaking(@ApiIgnore @RequestBody OrderSubInfoVo orderSubInfoVo) {
        return orderSubInfoService.orderTaking(orderSubInfoVo);
    }


    @ApiOperation("送单列表和完成列表（配送端）")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "postmanId", value = "配送员id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderStatus", value = "订单状态", required = true, dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小", required = true, dataType = "Long")})
    @GetMapping("getOrderByPostmanIdAndstatus")
    public IPage<OrderSubInfoPo> getOrderByPostmanIdAndStatus(@ApiIgnore OrderSubInfoVo orderSubInfoVo) {
        return orderSubInfoService.getOrderByPostmanIdAndStatus(orderSubInfoVo);
    }

}
