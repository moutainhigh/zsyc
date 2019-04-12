package com.zsyc.api.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.entity.GoodsStoreStock;
import com.zsyc.goods.entity.GoodsStoreStockLog;
import com.zsyc.goods.service.GoodsStoreStockLogService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 周凯俊 on 2019/2/26.
 * @Explain:库存变更记录 前端控制器
 */
@RestController
@RequestMapping("api/goodsStoreStockLog")
@Api
public class GoodsStoreStockLogController {

    @Reference
    private GoodsStoreStockLogService getGoodsStoreStockList;

    @ApiOperation("获取商品库存列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsStoreStockList")
    public IPage<GoodsStoreStockLog> getGoodsStoreStockLogkList(Page page, Long storeId){
        return getGoodsStoreStockList.getGoodsStoreStockLogList(page,storeId);
    }

    @ApiOperation("查询商品库存")
    @ApiResponse(code = 200, message = "success", response = GoodsStoreStock.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "sku", value = "商品sku码",required = true,dataType = "String")})
    @GetMapping(value = "getGoodsStoreStock")
    public GoodsStoreStockLog getGoodsStoreStockLog(Long storeId,String sku){
        return getGoodsStoreStockList.getGoodsStoreStockLog(storeId,sku);
    }
}
