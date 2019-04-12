package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zsyc.aftersale.entity.AfterSaleOrder;
import com.zsyc.aftersale.po.AfterSalePo;
import com.zsyc.aftersale.po.PayServicePo;
import com.zsyc.aftersale.service.AfterSaleOrderGoodsService;
import com.zsyc.aftersale.service.AfterSaleOrderService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/BackendAfterSaleController")
@Api
public class BackendAfterSaleController {

    @Reference
    public AfterSaleOrderService afterSaleService;
    @Reference
    public AfterSaleOrderGoodsService afterSaleOrderGoodsService;

    @ApiOperation("后台根据售后单号查找售后信息")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "afterSaleNo", value = "售后单号", required = true, dataType = "String")
    })
    @GetMapping(value = "selectAfterSaleOrder")
    public Object selectAfterSaleOrder(String  afterSaleNo){
        return afterSaleOrderGoodsService.selectAfterSaleOrderOrderNo(afterSaleNo);
    }

    @ApiOperation("后台根据子订单id查找售后信息")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subId", value = "子订单id", required = true, dataType = "String")
    })
    @GetMapping(value = "selectAfterSaleOrderSubId")
    public Object selectAfterSaleOrderSubId(Long  subId){
        return afterSaleOrderGoodsService.selectAfterSaleOrderSubId(subId);
    }

    @ApiOperation("后台根据配送员名字查找售后信息")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "staffName", value = "配送员名字", required = true, dataType = "String")
    })
    @GetMapping(value = "selectAfterSaleStaffName")
    public Object selectAfterSaleStaffName(String  staffName){

        return afterSaleOrderGoodsService.selectAfterSaleStaffName(staffName);
    }


    @ApiOperation("后台根据配送员电话查找售后信息")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "配送员电话", required = true, dataType = "String")
    })
    @GetMapping(value = "selectAfterSalePhone")
    public Object selectAfterSalePhone(String  phone){

        return afterSaleOrderGoodsService.selectAfterSalePhone(phone);
    }

    @ApiOperation("后台根据条件查找售后信息")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AfterSalePo.Phone", value = "配送员电话", required = true, dataType = "String"),
            @ApiImplicitParam(name = "AfterSalePo.afterSaleNo", value = "售后单号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "AfterSalePo.subId", value = "子订单号", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "AfterSalePo.staffName", value = "配送员名字", required = true, dataType = "String"),
            @ApiImplicitParam(name = "AfterSalePo.currentPage", value = "页数(必传)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "AfterSalePo.pageSize", value = "条数(必传)", required = true, dataType = "String")
    })
    @PostMapping(value = "selectAfterSaleAll")
    public Object selectAfterSaleAll(@RequestBody AfterSalePo afterSalePo){

        return afterSaleOrderGoodsService.selectAfterSaleAll(afterSalePo);
    }


    @ApiOperation("后台客服更改客服单状态")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "afterId", value = "客服主表id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "status", value = "状态(传“EXCHANGE“是换货状态，传“REFUND”是退货，传“FOLLOWUP是正在处理”)", required = true, dataType = "String")
    })
    @GetMapping(value = "updateAfterStatus")
    public Object updateAfterStatus(Long afterId,String status){
        return afterSaleOrderGoodsService.updateAfterStatus(afterId,status);
    }



    @ApiOperation("后台客服处理完订单更改子订单状态")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subId", value = "子订单id", required = true, dataType = "Long"),
    })
    @GetMapping(value = "afterUpdateOrderSubStauts")
    public Object afterUpdateOrderSubStauts(Long subId){
        return afterSaleService.afterUpdateOrderSubStauts(subId);
    }





    @ApiOperation("后台客服更改客服单备注")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "客服表id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "remark", value = "备注信息", required = true, dataType = "String")
    })
    @PostMapping(value = "updateAfterSaleOrderRemark")
    public Object  updateAfterSaleOrderRemark(@RequestBody AfterSaleOrder afterSaleOrder) {

     return    afterSaleOrderGoodsService.updateAfterSaleOrderRemark(afterSaleOrder);


    }



    @ApiOperation("后台客服完成客服单")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "客服表id", required = true, dataType = "Long"),

    })
    @GetMapping(value = "doneService")
    public Object  doneService(Long id) {

    return afterSaleOrderGoodsService.doneService(id);
    }

    @ApiOperation("后台客服退款")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PayServicePo.afterSaleOrderGoodsId[]", value = "客服子表商品id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "PayServicePo.orderNo", value = "订单号", required = true, dataType = "Long"),

    })
        @PostMapping(value = "payService")
    public  Object payService(@RequestBody PayServicePo payServicePo){


        return afterSaleService.selectAfterSaleOrderGoods(payServicePo);
    }








}
