package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.api.AccountHelper;
import com.zsyc.goods.entity.GoodsAttributeValue;
import com.zsyc.goods.service.GoodsAttributeValueService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 周凯俊 on 2019/2/22.
 * @Explain:商品属性管理
 */
@RestController
@RequestMapping("api/goodsAttributeValue")
@Api
public class GoodsAttributeValueController {

    @Reference
    private GoodsAttributeValueService goodsAttributeValueService;

    @Autowired
    private AccountHelper accountHelper;

    @ApiOperation("获取属性值列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "属性key",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsAttributeValueList")
    public IPage<GoodsAttributeValue> getGoodsAttributeValueList(Page page,Long id){
        return goodsAttributeValueService.getGoodsAttributeValueList(page,id);
    }

    @ApiOperation("查询属性值")
    @ApiResponse(code = 200, message = "success", response = GoodsAttributeValue.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "属性Id",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsAttributeValue")
    public GoodsAttributeValue getGoodsAttributeValue(Long id){
        return goodsAttributeValueService.getGoodsAttributeValue(id);
    }

    @ApiOperation("添加属性值信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "addGoodsAttributeValue",produces = MediaType.APPLICATION_JSON_VALUE)
    public void addGoodsAttributeValue(@RequestBody GoodsAttributeValue goodsAttributeValue){
        goodsAttributeValueService.addGoodsAttributeValue(goodsAttributeValue,accountHelper.getUser().getId());
    }

    @ApiOperation("修改属性值信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "updateGoodsAttributeValue",produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateGoodsAttributeValue(@RequestBody GoodsAttributeValue goodsAttributeValue){
        goodsAttributeValueService.updateGoodsAttributeValue(goodsAttributeValue);
    }

    @ApiOperation("删除属性值信息")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "属性Id",required = true,dataType = "Long")})
    @GetMapping(value = "deleteGoodsAttributeValue")
    public void deleteGoodsAttributeValue(Long id){
        goodsAttributeValueService.deleteGoodsAttributeValue(id);
    }
}
