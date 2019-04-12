package com.zsyc.api.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.api.AccountHelper;
import com.zsyc.goods.entity.GoodsCustomPrice;
import com.zsyc.goods.service.GoodsCustomPriceService;
import com.zsyc.goods.vo.GoodsCustomPriceInsertVO;
import com.zsyc.goods.vo.GoodsCustomPriceListVO;
import com.zsyc.goods.vo.GoodsCustomPriceSearchVO;
import com.zsyc.goods.vo.GoodsCustomPriceVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 周凯俊 on 2019/2/27.
 * @Explain:商品自定义价格 前端控制器
 */
@RestController
@RequestMapping("api/goodsCustomPrice")
@Api
public class GoodsCustomPriceController {

    @Reference
    private GoodsCustomPriceService goodsCustomPriceService;

    @Autowired
    private AccountHelper accountHelper;

    @ApiOperation("获取自定义价格列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "addressId", value = "地址ID",required = false,dataType = "String"),
            @ApiImplicitParam(name = "sku", value = "商品sku码",required = false,dataType = "String"),
            @ApiImplicitParam(name = "beforePrice", value = "beforePrice",required = false,dataType = "BigDecimal"),
            @ApiImplicitParam(name = "afterPrice", value = "afterPrice",required = false,dataType = "BigDecimal"),
            @ApiImplicitParam(name = "beforeTime", value = "开始时间",required = false,dataType = "LocalDateTime"),
            @ApiImplicitParam(name = "afterTime", value = "结束时间",required = false,dataType = "LocalDateTime"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsCustomPriceList")
    public IPage<GoodsCustomPriceListVO> getGoodsCustomPriceList(Page page, GoodsCustomPriceSearchVO goodsCustomPriceSearchVO){
        return goodsCustomPriceService.getGoodsCustomPriceList(page,goodsCustomPriceSearchVO);
    }

    @ApiOperation("查询自定义价格")
    @ApiResponse(code = 200, message = "success", response = GoodsCustomPrice.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "sku", value = "商品sku码",required = true,dataType = "String")})
    @GetMapping(value = "getGoodsCustomPrice")
    public GoodsCustomPriceVO getGoodsCustomPrice(Long storeId,Long addressId){
        return goodsCustomPriceService.getGoodsCustomPrice(storeId,addressId);
    }

    @ApiOperation("添加自定义价格")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "addGoodsCustomPrice",produces = MediaType.APPLICATION_JSON_VALUE)
    public void addGoodsCustomPrice(@RequestBody GoodsCustomPriceInsertVO goodsCustomPriceInsertVO){
        goodsCustomPriceService.addGoodsCustomPrice(goodsCustomPriceInsertVO,accountHelper.getUser().getId());
    }

    @ApiOperation("修改自定义价格")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "updateGoodsCustomPrice",produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateGoodsCustomPrice(@RequestBody GoodsCustomPriceInsertVO goodsCustomPriceInsertVO){
        goodsCustomPriceService.updateGoodsCustomPrice(goodsCustomPriceInsertVO,accountHelper.getUser().getId());
    }

    @ApiOperation("删除自定义价格")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "deleteGoodsCustomPrice",produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteGoodsCustomPrice(@RequestBody GoodsCustomPriceInsertVO goodsCustomPriceInsertVO){
        goodsCustomPriceService.deleteGoodsCustomPrice(goodsCustomPriceInsertVO,accountHelper.getUser().getId());
    }

}
