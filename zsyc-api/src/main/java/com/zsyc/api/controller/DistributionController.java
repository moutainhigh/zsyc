package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zsyc.warehouse.service.WarehousePackOrdersGoodsService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/Distribution")
@Api
public class DistributionController {
    @Reference
    private WarehousePackOrdersGoodsService warehousePackOrdersGoodsService;




    @ApiOperation("配送接单")
    @ApiResponse(code = 200, message = "success", response = List.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subId", value = "子订单id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "postmanId", value = "配送员Id", required = true, dataType = "Long"),
    })
    @GetMapping(value = "DistributionSend")
    public Integer DistributionSend(Long subId,Long postmanId){
      return   warehousePackOrdersGoodsService.distribution(subId,postmanId);

    }

    @ApiOperation("配送完成")
    @ApiResponse(code = 200, message = "success", response = List.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subId", value = "子订单id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "postmanId", value = "配送员Id", required = true, dataType = "Long"),
    })
    @GetMapping(value = "DistributionDone")
    public Integer DistributionDone(Long subId,Long postmanId){
        return   warehousePackOrdersGoodsService.distributionDone(subId,postmanId);

    }
    @ApiOperation("催单接口")
    @ApiResponse(code = 200, message = "success", response = List.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subId", value = "子订单id", required = true, dataType = "Long"),

    })
    @PostMapping(value = "reminder")
    public Integer reminder(Long subId){

        System.out.println("进来了");

        return   warehousePackOrdersGoodsService.DoneTime(subId);


    }

}
