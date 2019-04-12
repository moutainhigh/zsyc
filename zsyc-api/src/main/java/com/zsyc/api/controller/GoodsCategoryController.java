package com.zsyc.api.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.api.AccountHelper;
import com.zsyc.goods.entity.GoodsCategory;
import com.zsyc.goods.service.GoodsCategoryService;
import com.zsyc.goods.vo.GoodsCategoryListVO;
import com.zsyc.goods.vo.GoodsCategoryTreeListVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by 周凯俊 on 2019/2/22.
 * @Explain:商品分类
 */
@RestController
@RequestMapping("api/goodsCategory")
@Api
public class GoodsCategoryController {

    @Reference
    private GoodsCategoryService goodsCategoryService;

    @Autowired
    private AccountHelper accountHelper;

    @ApiOperation("获取类型列表")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "分类id",required = false,dataType = "Long"),
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsCategoryList")
    public IPage<GoodsCategory> getGoodsCategoryList(Page page,Long id){
        return goodsCategoryService.getGoodsCategoryList(page,id);
    }

    @ApiOperation("获取店铺分类列表")
    @ApiResponse(code = 200, message = "success", response = List.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsCategoryListByStoreId")
    public List<GoodsCategory> getGoodsCategoryListByStoreId(Long storeId,Long parentId){
        return goodsCategoryService.getGoodsCategoryListByStoreId(storeId,parentId);
    }

    @ApiOperation("获取类型树")
    @ApiResponse(code = 200, message = "success", response = IPage.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "第几页",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "一页几条",required = true,dataType = "Long")})
    @GetMapping(value = "getGoodsCategoryTreeList")
    public IPage<GoodsCategoryTreeListVO> getGoodsCategoryTreeList(Page page,Long id,String categoryName){
        return goodsCategoryService.getGoodsCategoryTreeList(page,id,categoryName);
    }

    @ApiOperation("添加类型信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "addGoodsCategory",produces = MediaType.APPLICATION_JSON_VALUE)
    public void addGoodsCategory(@RequestBody GoodsCategory goodsCategory){
        goodsCategoryService.addGoodsCategory(goodsCategory,accountHelper.getUser().getId());
    }

    @ApiOperation("修改类型信息")
    @ApiResponse(code = 200, message = "success")
    @PostMapping(value = "updateGoodsCategory",produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateGoodsCategory(@RequestBody GoodsCategory goodsCategory){
        goodsCategoryService.updateGoodsCategory(goodsCategory);
    }

    @ApiOperation("删除类型信息")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "分类id",required = true,dataType = "Long")})
    @GetMapping(value = "deleteGoodsCategory")
    public void deleteGoodsCategory(Long id){
        goodsCategoryService.deleteGoodsCategory(id);
    }

    @ApiOperation("小程序类型菜单列表")
    @ApiResponse(code = 200, message = "success", response = GoodsCategoryListVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long")})
    @GetMapping(value = "getMiniGoodsCategoryMenuInfo")
    public GoodsCategoryListVO getMiniGoodsCategoryMenuInfo(Long storeId){
        return goodsCategoryService.getMiniGoodsCategoryMenuList(storeId);
    }

    @ApiOperation("小程序类型列表")
    @ApiResponse(code = 200, message = "success", response = List.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "storeId", value = "店铺id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "categoryType", value = "分类类型",required = true,dataType = "String")})
    @GetMapping(value = "getMiniGoodsCategoryList")
    public List<GoodsCategory> getMiniGoodsCategoryList(Long storeId,String categoryType){
        return goodsCategoryService.getMiniGoodsCategoryList(storeId,categoryType);
    }

}
