package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.api.AccountHelper;
import com.zsyc.goods.entity.GoodsAttribute;
import com.zsyc.goods.service.GoodsAttributeService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 周凯俊 on 2019/2/22.
 * @Explain:商品属性
 */
@RestController
@RequestMapping("api/goodsAttribute")
@Api
public class GoodsAttributeController {
    
    @Reference
    private GoodsAttributeService goodsAttributeService;

    @Autowired
    private AccountHelper accountHelper;

    @ApiOperation("获取属性列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attrKeyName", value = "属性名称",required = true,dataType = "String"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsAttributeList")
    public IPage<GoodsAttribute> getGoodsAttributeList(Page page,String attrKeyName){
        return goodsAttributeService.getGoodsAttributeList(page,attrKeyName);
    }

    @ApiOperation("查询属性")
    @ApiResponse(code = 200, message = "success", response = GoodsAttribute.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "属性Id",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsAttribute")
    public GoodsAttribute getGoodsAttribute(Long id){
        return goodsAttributeService.getGoodsAttribute(id);
    }

    @ApiOperation("添加属性信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "addGoodsAttribute",produces = MediaType.APPLICATION_JSON_VALUE)
    public void addGoodsAttribute(@RequestBody GoodsAttribute goodsAttribute){
        goodsAttributeService.addGoodsAttribute(goodsAttribute,accountHelper.getUser().getId());
    }

    @ApiOperation("修改属性信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "updateGoodsAttribute",produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateGoodsAttribute(@RequestBody GoodsAttribute goodsAttribute){
        goodsAttributeService.updateGoodsAttribute(goodsAttribute);
    }

    @ApiOperation("删除属性信息")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "属性Id",required = true,dataType = "Long")})
    @GetMapping(value = "deleteGoodsAttribute")
    public void deleteGoodsAttribute(Long id){
        goodsAttributeService.deleteGoodsAttribute(id);
    }
}
