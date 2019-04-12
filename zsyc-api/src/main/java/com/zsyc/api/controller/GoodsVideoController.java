package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zsyc.goods.entity.GoodsVideo;
import com.zsyc.goods.service.GoodsVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zy on 2019/3/11.
 * @Explain:商品信息
 */
@RestController
@RequestMapping("api/goodsVideo")
@Api
public class GoodsVideoController {

    @Reference
    private GoodsVideoService goodsVideoService;

    @ApiOperation("查询商品视频信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sku", value = "商品编码",required = true,dataType = "String")})
    @GetMapping(value = "getGoodsVideo")
    public GoodsVideo getGoodsVideo(String sku){
        return goodsVideoService.getGoodsVideo(sku);
    }
}
