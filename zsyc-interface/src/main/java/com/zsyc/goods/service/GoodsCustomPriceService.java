package com.zsyc.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.vo.GoodsCustomPriceInsertVO;
import com.zsyc.goods.vo.GoodsCustomPriceListVO;
import com.zsyc.goods.vo.GoodsCustomPriceSearchVO;
import com.zsyc.goods.vo.GoodsCustomPriceVO;

/**
 * <p>
 * 商品自定义价格 服务类
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsCustomPriceService{

    IPage<GoodsCustomPriceListVO> getGoodsCustomPriceList(Page page, GoodsCustomPriceSearchVO goodsCustomPriceSearchVO);

    GoodsCustomPriceVO getGoodsCustomPrice(Long storeId,Long addressId);

    void addGoodsCustomPrice(GoodsCustomPriceInsertVO goodsCustomPriceInsertVO, Long id);

    void updateGoodsCustomPrice(GoodsCustomPriceInsertVO goodsCustomPriceInsertVO,Long id);

    void deleteGoodsCustomPrice(GoodsCustomPriceInsertVO goodsCustomPriceInsertVO,Long id);
}
