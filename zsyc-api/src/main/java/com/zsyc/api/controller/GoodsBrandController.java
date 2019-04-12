package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.api.AccountHelper;
import com.zsyc.goods.entity.GoodsBrand;
import com.zsyc.goods.service.GoodsBrandService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 周凯俊 on 2019/2/1.
 * @Explain:商品品牌
 */

@RestController
@RequestMapping("api/goodsBrand")
@Api
public class GoodsBrandController {

    @Reference
    private GoodsBrandService goodsBrandService;

    @Autowired
    private AccountHelper accountHelper;

    @ApiOperation("获取品牌列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "name", value = "搜索条件",required = true,dataType = "String"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsBrandList")
    public IPage<GoodsBrand> getGoodsBrandList(Page page,String name){
        return goodsBrandService.getGoodsBrandList(page,name);
    }

    @ApiOperation("查询品牌")
    @ApiResponse(code = 200, message = "success", response = GoodsBrand.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "品牌id",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsBrand")
    public GoodsBrand getGoodsBrand(Long id){
        return goodsBrandService.getGoodsBrand(id);
    }

    @ApiOperation("添加品牌信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "addGoodsBrand",produces = MediaType.APPLICATION_JSON_VALUE)
    public void addGoodsBrand(@RequestBody GoodsBrand goodsBrand){
        goodsBrandService.addGoodsBrand(goodsBrand,accountHelper.getUser().getId());
    }

    @ApiOperation("修改品牌信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "updateGoodsBrand",produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateGoodsBrand(@RequestBody GoodsBrand goodsBrand){
        goodsBrandService.updateGoodsBrand(goodsBrand);
    }

    @ApiOperation("删除品牌信息")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "品牌id",required = true,dataType = "Long")})
    @GetMapping(value = "deleteGoodsBrand")
    public void deleteGoodsBrand(Long id){
        goodsBrandService.deleteGoodsBrand(id);
    }
}
