package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.order.entity.OrderProtocolInfo;
import com.zsyc.order.po.OrderProtocolGoodsPo;
import com.zsyc.order.po.OrderProtocolInfoPo;
import com.zsyc.order.po.OrderSubInfoPo;
import com.zsyc.order.service.OrderInfoService;
import com.zsyc.order.service.OrderProtocolGoodsService;
import com.zsyc.order.service.OrderProtocolInfoService;
import com.zsyc.order.service.OrderSubInfoService;
import com.zsyc.order.utils.OrderUtil;
import com.zsyc.order.vo.OrderProtocolInfoVo;
import com.zsyc.order.vo.OrderSubInfoVo;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @program: zsyc-parent
 * @description: 协议Controller
 * @author: Mr.Ning
 * @create: 2019-02-27 16:47
 **/
@RestController
@RequestMapping("api/orderProtocol")
@Api
public class OrderProtocolController {

    @Reference
    private OrderProtocolInfoService orderProtocolInfoService;

    @Reference
    private OrderProtocolGoodsService orderProtocolGoodsService;

    @Reference
    private OrderInfoService orderInfoService;

    @Reference
    private OrderSubInfoService orderSubInfoService;

    @ApiOperation("查询会员协议(已分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "memberId", value = "会员id",required = true,dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码",required = true,dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小",required = true,dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getOrderprotocolinfo")
    public IPage<OrderProtocolInfoPo> getOrderProtocolInfo(@ApiIgnore @RequestParam Map map){
        return orderProtocolInfoService.getOrderProtocolInfo(map);
    }


    @ApiOperation("协议详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "protocolNo", value = "协议号",required = true,dataType = "string")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("protocolDetails")
    public OrderProtocolInfoPo protocolDetails(@ApiIgnore @RequestParam Map map){
        return orderProtocolInfoService.protocolDetails(map);
    }


    @ApiOperation("协议详情（获取某个协议的退瓶订单列表）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "protocolNo", value = "协议号",required = true,dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码",required = true,dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小",required = true,dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getProtocolRefundOrder")
    public IPage<OrderSubInfoPo> getProtocolRefundOrder(@ApiIgnore @RequestParam Map map){
        return orderProtocolInfoService.getProtocolRefundOrder(map);
    }

    @ApiOperation("创建退瓶订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "protocolNo", value = "协议号",required = true,dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "orderGoodsVos[].id", value = "协议商品的id",allowMultiple = true,required = true,dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "orderGoodsVos[].sku", value = "协议商品的sku",allowMultiple = true,required = true,dataType = "String"),
            @ApiImplicitParam(paramType="query",name = "orderGoodsVos[].num", value = "退瓶的数量",allowMultiple = true,required = true,dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("createProtocolRefundOrder")
    public void createProtocolRefundOrder(@ApiIgnore @RequestBody OrderSubInfoVo orderSubInfoVo){
        orderSubInfoService.createProtocolRefundOrder(orderSubInfoVo);
    }


    @ApiOperation("获取协议的商品列表(如果退瓶为0的排除)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "protocolNo", value = "协议号",required = true,dataType = "string")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getProtocolGoods")
    public List<OrderProtocolGoodsPo> getProtocolGoods(@ApiIgnore @RequestParam Map map){
        return orderProtocolGoodsService.getProtocolGoods(map);
    }


    @ApiOperation("协议列表（后台）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "storeId", value = "店铺id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "protocolNo", value = "协议号",  dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "memberAddressId", value = "用户地址id",  dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "current", value = "当前页码", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query",name = "size", value = "页码大小", required = true, dataType = "Long")})
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getAllProtocolByStoreId")
    public IPage<OrderProtocolInfoPo> getAllProtocolByStoreId(@ApiIgnore OrderProtocolInfoVo orderProtocolInfoVo) {
        return orderProtocolInfoService.getAllProtocolByStoreId(orderProtocolInfoVo);
    }


    @ApiOperation("获取协议类型(后台)")
    @ApiResponse(code = 200, message = "success")
    @GetMapping("getProtocolType")
    public Map<Object,String> getProtocolType() {
        return OrderUtil.EnumToMap(OrderProtocolInfo.ProtocolType.class);
    }


    @ApiOperation("添加协议（后台）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name = "orderId", value = "子订单id",required = true,dataType = "long"),
            @ApiImplicitParam(paramType="query",name = "protocolName", value = "协议名称",required = true,dataType = "string"),
            @ApiImplicitParam(paramType="query",name = "type", value = "协议类型",required = true,dataType = "string")})
    @ApiResponse(code = 200, message = "success")
    @PostMapping("addOrderProtocol")
    public void addOrderProtocol(@ApiIgnore @RequestBody OrderProtocolInfoVo orderProtocolInfoVo){
        orderProtocolInfoService.addOrderProtocol(orderProtocolInfoVo);
    }

}
