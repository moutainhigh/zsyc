package com.zsyc.goods.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zsyc.common.AssertExt;
import com.zsyc.goods.entity.GoodsVideo;
import com.zsyc.goods.mapper.GoodsVideoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class GoodsVideoServiceImpl implements GoodsVideoService{

    @Autowired
    private GoodsVideoMapper goodsVideoMapper;

    @Override
    public GoodsVideo getGoodsVideo(String sku) {
        AssertExt.notBlank(sku,"商品sku码为空");
        return goodsVideoMapper.getGoodsVideo(sku);
    }
}
