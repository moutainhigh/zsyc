package com.zsyc.api.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.api.AccountHelper;
import com.zsyc.goods.service.GoodsStorePriceService;
import com.zsyc.goods.vo.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 周凯俊 on 2019/2/22.
 * @Explain:商品价格表 前端控制器
 */
@RestController
@RequestMapping("api/goodsStorePrice")
@Api
public class GoodsStorePriceController {
    
    @Reference
    private GoodsStorePriceService goodsStorePriceService;

    @Autowired
    private AccountHelper accountHelper;

    @ApiOperation("获取店铺子商品价格列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsChildPriceList")
    public IPage<GoodsStorePriceListVO> getGoodsChildPriceList(Page page, Long storeId){
        return goodsStorePriceService.getGoodsChildPriceList(page,storeId);
    }

    @ApiOperation("获取店铺配料商品价格列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsIngredientPriceList")
    public IPage<GoodsStorePriceListVO> getGoodsIngredientPriceList(Page page, Long storeId){
        return goodsStorePriceService.getGoodsIngredientPriceList(page,storeId);
    }

    @ApiOperation("查询店铺商品价格列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @PostMapping(value = "getGoodsVipPriceList")
    public IPage<GoodsStorePriceListVO> getGoodsVipPriceList(Page page,@RequestBody GoodsStorePriceSearchVO goodsStorePriceSearchVO){
        return goodsStorePriceService.getGoodsVipPriceList(page,goodsStorePriceSearchVO);
    }

    @ApiOperation("查询sku商品信息")
    @ApiResponse(code = 200, message = "success", response = GoodsStorePriceVO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "sku", value = "商品sku码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsPrice")
    public GoodsStorePriceVO getGoodsPrice(String sku, Long storeId){
        return goodsStorePriceService.getGoodsPrice(sku,storeId);
    }

    @ApiOperation("添加店铺商品价格信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "addGoodsPrice",produces = MediaType.APPLICATION_JSON_VALUE)
    public void addGoodsPrice(@RequestBody GoodsPriceInsertVO goodsPriceInsertVO){
        goodsStorePriceService.addGoodsPrice(goodsPriceInsertVO,accountHelper.getUser().getId());
    }

    @ApiOperation("修改店铺商品价格信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "updateGoodsPrice",produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateGoodsPrice(@RequestBody GoodsPriceInsertVO goodsPriceInsertVO){
        goodsStorePriceService.updateGoodsPrice(goodsPriceInsertVO,accountHelper.getUser().getId());
    }

    @ApiOperation("删除店铺商品价格信息")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({@ApiImplicitParam(name = "sku", value = "商品sku码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long")})
    @GetMapping(value = "deleteGoodsPrice")
    public void deleteGoodsPrice(String sku,Long storeId){
        goodsStorePriceService.deleteGoodsPrice(accountHelper.getUser().getId(),sku,storeId);
    }

    @ApiOperation("上架店铺商品")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({@ApiImplicitParam(name = "sku", value = "商品sku码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long")})
    @GetMapping(value = "updateStatusToOnSale")
    public void updateStatusToOnSale(String sku,Long storeId){
        goodsStorePriceService.updateStatusToOnSale(accountHelper.getUser().getId(),sku,storeId);
    }

    @ApiOperation("下架店铺商品")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({@ApiImplicitParam(name = "sku", value = "商品sku码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long")})
    @GetMapping(value = "updateStatusToOffSale")
    public void updateStatusToOffSale(String sku,Long storeId){
        goodsStorePriceService.updateStatusToOffSale(accountHelper.getUser().getId(),sku,storeId);
    }

    @ApiOperation("设置推荐")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({@ApiImplicitParam(name = "sku", value = "商品sku码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long")})
    @GetMapping(value = "updateIsRecommend")
    public void updateIsRecommend(String sku,Long storeId){
        goodsStorePriceService.updateIsRecommend(accountHelper.getUser().getId(),sku,storeId);
    }

    @ApiOperation("设置不推荐")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({@ApiImplicitParam(name = "sku", value = "商品sku码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long")})
    @GetMapping(value = "updateIsNotRecommend")
    public void updateIsNotRecommend(String sku,Long storeId){
        goodsStorePriceService.updateIsNotRecommend(accountHelper.getUser().getId(),sku,storeId);
    }

    @ApiOperation("获取小程序商品列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "categoryId", value = "分类id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "addressId", value = "地址id",required = true,dataType = "Long")})
    @GetMapping(value = "getMiniGoodsPriceList")
    public IPage<GoodsAndPriceListVO> getMiniGoodsPriceList(Page page,Long storeId,Long addressId,Long categoryId){
        return goodsStorePriceService.getMiniGoodsPriceList(page,storeId,addressId,categoryId);
    }

    @ApiOperation("获取新租瓶列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "categoryId", value = "分类id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "addressId", value = "地址id",required = true,dataType = "Long")})
    @GetMapping(value = "getRentBottleGoodsPriceList")
    public IPage<GoodsAndPriceListVO> getRentBottleGoodsPriceList(Page page,Long storeId,Long addressId,Long categoryId){
        return goodsStorePriceService.getRentBottleGoodsPriceList(page,storeId,addressId,categoryId);
    }

    @ApiOperation("获取小程序订单商品信息")
    @ApiResponse(code = 200, message = "success", response = GoodsPriceInfoVO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "sku", value = "商品sku码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "addressId", value = "地址id",required = true,dataType = "Long")})
    @GetMapping(value = "getMiniGoodsPriceInfo")
    public GoodsPriceInfoVO getGoodsPriceInfo(Long storeId,Long addressId,String sku){
        return goodsStorePriceService.getGoodsPriceInfo(storeId,addressId,sku);
    }

    @ApiOperation("获取小程序商品详情信息")
    @ApiResponse(code = 200, message = "success", response = GoodsAttributePriceInfoVO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "sku", value = "商品sku码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "addressId", value = "地址id",required = true,dataType = "Long")})
    @GetMapping(value = "getMiniGoodsDetailInfo")
    public GoodsAttributePriceInfoVO getMiniGoodsDetailInfo(Long storeId,Long addressId,String sku){
        return goodsStorePriceService.getGoodsDetailInfo(storeId,addressId,sku);
    }

    @ApiOperation("获取小程序商品价格信息")
    @ApiResponse(code = 200, message = "success", response = GoodsSkuAndPriceVO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "sku", value = "商品sku码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "addressId", value = "地址id",required = true,dataType = "Long")})
    @GetMapping(value = "getMiniGoodsDetailPrice")
    public GoodsSkuAndPriceVO getMiniGoodsDetailPrice(Long storeId,Long addressId,String sku){
        return goodsStorePriceService.getMiniGoodsDetailPrice(storeId,addressId,sku);
    }

    @ApiOperation("获取小程序今日特价")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "addressId", value = "地址id",required = true,dataType = "Long")})
    @GetMapping(value = "getMiniGoodsBarginPriceList")
    public IPage<GoodsHomePagePriceVO> getMiniGoodsBarginPriceList(Page page, Long storeId, Long addressId){
        return goodsStorePriceService.getMiniGoodsBarginPrice(page,storeId,addressId);
    }

    @ApiOperation("获取小程序推荐商品")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "addressId", value = "地址id",required = true,dataType = "Long")})
    @GetMapping(value = "getMiniGoodsRecommendList")
    public IPage<GoodsHomePagePriceVO> getMiniGoodsRecommendList(Page page, Long storeId, Long addressId){
        return goodsStorePriceService.getMiniGoodsRecommendList(page,storeId,addressId);
    }

    @ApiOperation("获取小程序今日菜谱")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "addressId", value = "地址id",required = true,dataType = "Long")})
    @GetMapping(value = "getMiniCookBookList")
    public IPage<GoodsHomePagePriceVO> getMiniCookBookList(Page page, Long storeId, Long addressId){
        return goodsStorePriceService.getMiniCookBookList(page,storeId,addressId);
    }

    @ApiOperation("获取小程序查询菜市场列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称",required = true,dataType = "String"),
            @ApiImplicitParam(name = "addressId", value = "地址id",required = true,dataType = "Long")})
    @GetMapping(value = "getMiniGoodsPriceListByName")
    public IPage<GoodsAndPriceListVO> getMiniGoodsPriceListByName(Page page,Long storeId,Long addressId,String goodsName){
        return goodsStorePriceService.getMiniGoodsPriceListByName(page,storeId,addressId,goodsName);
    }

    @ApiOperation("获取小程序查询快捷菜列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称",required = true,dataType = "String"),
            @ApiImplicitParam(name = "addressId", value = "地址id",required = true,dataType = "Long")})
    @GetMapping(value = "getMiniFastFoodPriceListByName")
    public IPage<GoodsAndPriceListVO> getMiniFastFoodPriceListByName(Page page,Long storeId,Long addressId,String goodsName){
        return goodsStorePriceService.getMiniFastFoodPriceListByName(page,storeId,addressId,goodsName);
    }
}
