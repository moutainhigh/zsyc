package com.zsyc.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.goods.entity.GoodsVideo;

/**
 * <p>
 * 商品组合表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsVideoMapper extends BaseMapper<GoodsVideo> {

    GoodsVideo getGoodsVideo(String sku);
}
