package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.api.AccountHelper;
import com.zsyc.goods.service.GoodsInfoService;
import com.zsyc.goods.vo.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 周凯俊 on 2019/2/22.
 * @Explain:商品信息
 */
@RestController
@RequestMapping("api/goodsInfo")
@Api
public class GoodsInfoController {
    
    @Reference
    private GoodsInfoService goodsInfoService;

    @Autowired
    private AccountHelper accountHelper;

    @ApiOperation("获取查询商品列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsInfoList")
    public IPage<GoodsInfoListVO> getGoodsInfoList(Page page, GoodsInfoListSearchVO goodsInfoListSearchVO){
        return goodsInfoService.getGoodsInfoList(page,goodsInfoListSearchVO);
    }

    @ApiOperation("获取添加子商品列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "分类ID",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsChildInfoList")
    public IPage<GoodsInfoListVO> getGoodsChildInfoList(Page page, Long id){
        return goodsInfoService.getGoodsChildInfoList(page,id);
    }

    @ApiOperation("获取配料商品列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "分类ID",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsIngredientInfoList")
    public IPage<GoodsInfoListVO> getGoodsIngredientInfoList(Page page, Long id){
        return goodsInfoService.getGoodsIngredientInfoList(page,id);
    }

    @ApiOperation("获取组合商品列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sku", value = "商品sku码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsGroupInfoList",produces = MediaType.APPLICATION_JSON_VALUE)
    public IPage<GoodsGroupInfoListVO> getGoodsGroupInfoList(Page page,String sku){
        return goodsInfoService.getGoodsGroupInfoList(page,sku);
    }

    @ApiOperation("获取商品列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @GetMapping(value = "getGoodsInfoTypeList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    public IPage<GoodsInfoListVO> getGoodsInfoTypeList(Page page){
        return goodsInfoService.getGoodsInfoTypeList(page);
    }

    @ApiOperation("查询商品信息")
    @ApiResponse(code = 200, message = "success", response = GoodsInfoVO.class)
    @GetMapping(value = "getGoodsInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sku", value = "商品sku码",required = true,dataType = "String")})
    public GoodsInfoVO getGoodsInfo(String sku){
        return goodsInfoService.getGoodsInfo(sku);
    }

    @ApiOperation("添加商品信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "addGoodsInfo",produces = MediaType.APPLICATION_JSON_VALUE)
    public void addGoodsInfo(@RequestBody GoodsInfoInsertVO goodsInfoVO){
        goodsInfoService.addGoodsInfo(goodsInfoVO,accountHelper.getUser().getId());
    }

    @ApiOperation("修改商品信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "updateGoodsInfo",produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateGoodsInfo(@RequestBody GoodsInfoUpdateVO goodsInfoUpdateVO){
        goodsInfoService.updateGoodsInfo(goodsInfoUpdateVO,accountHelper.getUser().getId());
    }

    @ApiOperation("删除商品信息")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sku", value = "商品sku码",required = false,dataType = "String")})
    @GetMapping(value = "deleteGoodsInfo")
    public void deleteGoodsInfo(String sku){
        goodsInfoService.deleteGoodsInfo(accountHelper.getUser().getId(),sku);
    }
}
