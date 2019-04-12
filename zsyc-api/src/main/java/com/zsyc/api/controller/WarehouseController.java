package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;


import com.zsyc.aftersale.service.AfterSaleOrderService;
import com.zsyc.warehouse.service.WarehouseOrderGoodsService;
import com.zsyc.warehouse.service.WarehouseOrderService;
import com.zsyc.warehouse.service.WarehousePackOrdersGoodsService;
import com.zsyc.warehouse.service.WarehousePackOrdersService;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/Warehouse")
@Api
public class WarehouseController {
    @Reference
    public WarehouseOrderService warehouseService;
    @Reference
    public WarehouseOrderGoodsService warehousePackoreder;
    @Reference
    public WarehousePackOrdersService warehousePackOrdersService;
    @Reference
    public WarehousePackOrdersGoodsService warehousePackOrdersGoodsService;
    @Reference
    public WarehouseOrderGoodsService warehouseOrderGoodsService;
    @Reference
    public AfterSaleOrderService afterSaleService;




//    @ApiOperation("生成备货表")
//    @ApiResponse(code = 200, message = "success")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "storeIdtest", value = "店铺Id", required = true, dataType = "Long")
//
//    })
//    @GetMapping(value = "selectOrderSub", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Object selectOrderSub(Long storeIdtest) {
//        return warehouseService.WareHousemap(storeIdtest);
//    }


    @ApiOperation("分拣部分缺货的接口")
    @ApiResponse(code = 200, message = "success", response = List.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "warehousePackOrderId", value = "子备货id", required = true, dataType = "Long")

    })
    @GetMapping(value = "warehouseOrderPARTIAL")
    public Integer warehouseOrderPARTIAL(Long warehousePackOrderId) {
        return afterSaleService.warehouseOrderPARTIAL(warehousePackOrderId);
    }


    @ApiOperation("分拣员接单接口(备货订单商品表分拣状态)")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subId", value = "子订单id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "staffId", value = "分拣员id", required = true, dataType = "Long"),
    })

    @GetMapping(value = "packOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object packOrder(Long subId, Long staffId) { //Long staffId

        return warehousePackOrdersService.packOrder(subId, staffId);
    }


    @ApiOperation("分拣显示已该分拣员分拣完成的分拣单")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "staffId", value = "分拣员id", required = true, dataType = "Long"),

    })
    @GetMapping(value = "staffIdDone", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object staffIdDone(Long staffId) {

        return warehousePackOrdersGoodsService.packOrderDone(staffId);
    }


    @ApiOperation("分拣显示已该分拣员分拣完成的分拣单(详细信息)")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "staffId", value = "分拣员id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "subId", value = "子订单id", required = true, dataType = "Long"),
    })
    @GetMapping(value = "staffIdDoneAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object staffIdDoneAll(Long staffId, Long subId) {

        return warehousePackOrdersGoodsService.staffIdDoneAll(staffId, subId);
    }


    @ApiOperation("显示分拣员分拣的子订单")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "staffId", value = "分拣员id", required = true, dataType = "Long"),
    })
    @GetMapping(value = "StaffIdpackOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object staffIdpackOrder(Long staffId) { //Long staffId

        return warehousePackOrdersGoodsService.selectPackOrder(staffId);
    }


    @ApiOperation("小程序分拣缺货按钮")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subId", value = "子订单id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "warehousePackOrderGoodsId", value = "分拣商品表id", required = true, dataType = "Long")
    })
    @GetMapping(value = "packOrderShortage", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer packOrderShortage(Long subId, Long warehousePackOrderGoodsId) {

        return warehousePackOrdersGoodsService.packOrderShortage(subId, warehousePackOrderGoodsId);
    }


    @ApiOperation("小程序分拣商品完成按钮")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subId", value = "子订单id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "warehousePackOrderGoodsId", value = "分拣商品表id", required = true, dataType = "Long")
    })
    @GetMapping(value = "packOrderDone", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer packOrderDone(Long subId, Long warehousePackOrderGoodsId) {
        warehousePackOrdersService.packOrderDoneStatus(subId);
        return warehousePackOrdersGoodsService.wareHousePackStatusDone(subId, warehousePackOrderGoodsId);
    }


    @ApiOperation("小程序分拣完成订单按钮(分拣总完成按钮)")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subId", value = "分拣单id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "remark", value = "备注信息", required = true, dataType = "String"),
    })
    @GetMapping(value = "packOrderDoneAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer packOrderDoneAll(Long subId,String remark) {
        warehousePackOrdersService.updateWarehousePackOrderRemark(subId,remark);
        warehousePackOrdersService.packOrderDoneStatus(subId);

        return warehousePackOrdersService.packOrderDone(subId);


    }


    @ApiOperation("小程序显示已备好货状态的子订单数据(分拣显示数据)")
    @ApiResponse(code = 200, message = "success")
        @GetMapping(value = "returnOrderGoodsDone", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object returnOrderGoodsDone() {

        return warehousePackOrdersGoodsService.returnOrderGoodsDone();

    }


    @ApiOperation("备货时候发现缺货得时候更改状态")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "子备货单id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "status", value = "status  (传入1是完成传入0是缺货)", required = true, dataType = "int"),
    })
    @GetMapping(value = "UpdateOrderSubStauts", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateOrderSubStauts(Integer status, Integer id) {
        if (status == 1) {
            String statusA = "DONE";
            return warehouseService.updateOrderSubStauts(statusA, id);
        } else {
            String statusA = "SHORTAGE";
            return warehouseService.updateOrderSubStauts(statusA, id);
        }

    }

    @ApiOperation("备货单（主完成按钮）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主备货单id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "String")

    })
    @ApiResponse(code = 200, message = "success")
    @GetMapping(value = "updateOrderSubStautsA", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer updateOrderSubStautsAll(Long id,String remark) {

        return warehouseOrderGoodsService.wareHouseDone(id,remark);


    }


    @ApiOperation("小程序显示备货单")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "店铺ID", required = true, dataType = "Long"),
    })
    @GetMapping(value = "MiniWarehouse", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object miniWarehouse(Long storeId) {

        return warehouseService.returnMinWareHouse(storeId);
    }


    @ApiOperation("小程序显示备货员备货单")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "wareHouseId", value = "备货员id", required = true, dataType = "Long"),
    })
    @GetMapping(value = "returnDocumentsPrepared", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object returnWareHouseId(Long wareHouseId) {


        return warehousePackOrdersService.checkGoodsInfo(wareHouseId);
    }


    @ApiOperation("小程序备货接单")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "warehouseOrderId", value = "主备货单id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "wareHouseId", value = "备货员id", required = true, dataType = "Long")
    })
    @GetMapping(value = "documentsPrepared", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object documentsPrepared(Long warehouseOrderId, Long wareHouseId) {


        return warehouseService.updateWareHouseStatus(warehouseOrderId, wareHouseId);


    }


    @ApiOperation("显示已备货好的备货单信息")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "店铺id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "wareHouseId", value = "备货员id", required = true, dataType = "Long")
    })

    @GetMapping(value = "selectWareHouseAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object selectWareHouseAll(Long storeId, Long wareHouseId) {


        return warehouseService.selectWareHouseAll(storeId, wareHouseId);
    }


//    @ApiOperation("备货完成详细信息")
//    @ApiResponse(code = 200, message = "success")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "warehouseOrderId", value = "主备货单id", required = true, dataType = "Long")
//    })
//    @GetMapping(value = "selectWareHouseMessage", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Object selectWareHouseMessage(Long warehouseOrderId) {
//
//        return warehousePackOrdersGoodsService.checkGoodsinfo(warehouseOrderId);
//    }


    @ApiOperation("主动生成备货信息")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "店铺Id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "minute", value = "提前时间", required = true, dataType = "Long"),

    })
    @GetMapping(value = "createStock", produces = MediaType.APPLICATION_JSON_VALUE)
    public void  createStock(Long  storeId,Integer minute){

        warehouseService.createWarehouseByStoreId(storeId,minute);

    }





}
