package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zsyc.aftersale.po.AfterSaleOrderAccept;
import com.zsyc.aftersale.po.AfterSaleOrderChildren;
import com.zsyc.aftersale.service.AfterSaleOrderGoodsService;
import com.zsyc.aftersale.service.AfterSaleOrderService;
import com.zsyc.order.entity.OrderGoods;
import com.zsyc.warehouse.service.WarehousePackOrdersService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("api/AfterSaleController")
@Api
public class AfterSaleController {

    @Reference
    public AfterSaleOrderService afterSaleService;
    @Reference
    public AfterSaleOrderGoodsService afterSaleOrderGoodsService;
    @Reference
    public WarehousePackOrdersService warehousePackOrdersService;


    @ApiOperation("确定缺货的接口")
    @ApiResponse(code = 200, message = "success", response = List.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AfterSaleOrderAccept.warehousePackOrderId", value = "分拣订单id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "AfterSaleOrderAccept.orderSubId", value = "子订单id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "AfterSaleOrderAccept.memberAddressId", value = "客户地址ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "AfterSaleOrderAccept.staffPhone", value = "配货员电话号码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "AfterSaleOrderAccept.remark", value = "备注信息", required = true, dataType = "String"),
            @ApiImplicitParam(name = "AfterSaleOrderAccept.staffName", value = "配货员名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "AfterSaleOrderAccept.list.num", value = "原始数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "AfterSaleOrderAccept.list.numIng", value = "现在拥有的数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "AfterSaleOrderAccept.list,sku", value = "商品的sku码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "AfterSaleOrderAccept.list.quick.num", value = "快捷菜里面子商品的原始数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "AfterSaleOrderAccept.list.quick.numIng", value = "快捷菜里面子商品现在拥有的数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "AfterSaleOrderAccept.list.quick.sku", value = "快捷菜里面子商品的sku码", required = true, dataType = "String")
    })
    @PostMapping(value = "outStock")
    public Integer outStock(@RequestBody AfterSaleOrderAccept afterSaleOrderAccept) {
        Long subId=afterSaleOrderAccept.getWarehousePackOrderId();
        String remark=afterSaleOrderAccept.getRemark();
        warehousePackOrdersService.updateWarehousePackOrderRemark(subId,remark);
       // warehousePackOrdersService.updateWarehousePackOrderShortage(subId);
        afterSaleOrderGoodsService.outOcket(afterSaleOrderAccept);
        return 1;


    }

//    @ApiOperation("分拣时候计算退货接口")
//    @ApiResponse(code = 200, message = "success", response = List.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "ordersubId", value = "子订单id", required = true, dataType = "Long"),
//            @ApiImplicitParam(name = "num", value = "原始数量", required = true, dataType = "Integer"),
//            @ApiImplicitParam(name = "numing", value = "现在拥有的数量", required = true, dataType = "Integer"),
//            @ApiImplicitParam(name = "sku", value = "商品的sku码", required = true, dataType = "String")
//
//    })
//    @GetMapping(value = "afterSaleOrderGoods")
//    public Integer afterSaleOrderGoods(Integer num, String sku, Long ordersubId, Integer numing) {
//
//        OrderGoods OGood = afterSaleOrderGoodsService.selectGoodsPrice(ordersubId, sku);
//        Integer price = OGood.getPrice();
//        Integer num1 = num - numing;
//        Integer refundAmount = price * num;
//        LocalDateTime time = LocalDateTime.now();
//        String afteraleSNo = String.valueOf(time.toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
//
//
//        return afterSaleOrderGoodsService.afterSaleOrderGoods(afteraleSNo, num, sku, ordersubId, num1, refundAmount);
//    }

    @ApiOperation("查询客服待处理订单")
    @ApiResponse(code = 200, message = "success", response = List.class)
    @GetMapping(value = "selectAfterSaleOrderAll")
    public Object selectAfterSaleOrderAll() {
        return afterSaleService.selectAfterSaleOrderAll();
    }


    @ApiOperation("查询客服待处理订单商品详细信息")
    @ApiResponse(code = 200, message = "success", response = List.class)
    @GetMapping(value = "selectAfterSaleOrderGoodsAll")
    public Object selectAfterSaleOrderGoodsAll() {

        return afterSaleOrderGoodsService.selectAfterSaleOrderGoods();
    }

    @ApiOperation("客服输入该订单的备注信息")
    @ApiResponse(code = 200, message = "success", response = List.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "backendRemark", value = "客服原因", required = true, dataType = "Strin"),
            @ApiImplicitParam(name = "subId", value = "子订单id", required = true, dataType = "Integer"),
    })
    @GetMapping(value = "updateSubOrderemark")
    public Object updateSubOrderemark(String backendRemark, Long subId) {

        return afterSaleOrderGoodsService.updateSubOrderemark(backendRemark, subId);
    }


}
