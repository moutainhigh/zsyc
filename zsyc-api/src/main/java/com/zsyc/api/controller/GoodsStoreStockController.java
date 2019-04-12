package com.zsyc.api.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.api.AccountHelper;
import com.zsyc.goods.entity.GoodsStoreStock;
import com.zsyc.goods.service.GoodsStoreStockService;
import com.zsyc.goods.vo.GoodsStoreStockListVO;
import com.zsyc.goods.vo.GoodsStoreStockVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 周凯俊 on 2019/2/26.
 * @Explain:商品库存表
 *  前端控制器
 */
@RestController
@RequestMapping("api/goodsStoreStock")
@Api
public class GoodsStoreStockController {

    @Reference
    private GoodsStoreStockService goodsStoreStockService;

    @Autowired
    private AccountHelper accountHelper;

    @ApiOperation("获取商品库存列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsStoreStockList")
    public IPage<GoodsStoreStockListVO> getGoodsStoreStockList(Page page, Long storeId){
        return goodsStoreStockService.getGoodsStoreStockList(page,storeId);
    }

    @ApiOperation("查询商品库存")
    @ApiResponse(code = 200, message = "success", response = GoodsStoreStockVO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "sku", value = "商品sku码",required = true,dataType = "String")})
    @GetMapping(value = "getGoodsStoreStock")
    public GoodsStoreStockVO getGoodsStoreStock(Long storeId, String sku){
        return goodsStoreStockService.getGoodsStoreStock(storeId,sku);
    }

    @ApiOperation("设置店铺商品备货信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "updateGoodsStoreStock",produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateGoodsStoreStock(@RequestBody GoodsStoreStock goodsStoreStock){
        goodsStoreStockService.updateGoodsStoreStock(goodsStoreStock,accountHelper.getUser().getId());
    }

    @ApiOperation("设置店铺商品备货信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "addStoreStockByStoreId",produces = MediaType.APPLICATION_JSON_VALUE)
    public void addStoreStockByStoreId(@RequestBody GoodsStoreStock goodsStoreStock){
        goodsStoreStockService.addStoreStockByStoreId(goodsStoreStock,accountHelper.getUser().getId());
    }

    @ApiOperation("设置店铺商品回仓信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "declineStockByStoreId",produces = MediaType.APPLICATION_JSON_VALUE)
    public void declineStockByStoreId(@RequestBody GoodsStoreStock goodsStoreStock){
        goodsStoreStockService.declineStockByStoreId(goodsStoreStock,accountHelper.getUser().getId());
    }
}
