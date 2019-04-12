package com.zsyc.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.entity.GoodsAttribute;

/**
 * <p>
 * 商品属性表 服务类
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsAttributeService{

    IPage<GoodsAttribute> getGoodsAttributeList(Page page,String attrKeyName);

    void addGoodsAttribute(GoodsAttribute goodsAttribute,Long id);

    void updateGoodsAttribute(GoodsAttribute goodsAttribute);

    void deleteGoodsAttribute(Long id);

    GoodsAttribute getGoodsAttribute(Long id);

}
