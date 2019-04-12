package com.zsyc.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.entity.GoodsAttributeValue;

/**
 * <p>
 * 商品属性值表 服务类
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsAttributeValueService{

    IPage<GoodsAttributeValue> getGoodsAttributeValueList(Page page,Long id);

    void addGoodsAttributeValue(GoodsAttributeValue goodsAttributeValue,Long id);

    void updateGoodsAttributeValue(GoodsAttributeValue goodsAttributeValue);

    void deleteGoodsAttributeValue(Long id);

    GoodsAttributeValue getGoodsAttributeValue(Long id);

}
