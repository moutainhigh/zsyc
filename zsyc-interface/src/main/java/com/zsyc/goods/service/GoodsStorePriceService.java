package com.zsyc.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.vo.*;

/**
 * <p>
 * 商品价格表 服务类
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsStorePriceService {

    IPage<GoodsStorePriceListVO> getGoodsPriceList(Page page, Long storeId);

    void addGoodsPrice(GoodsPriceInsertVO goodsPriceInsertVO, Long id);

    void updateGoodsPrice(GoodsPriceInsertVO goodsPriceInsertVO, Long id);

    void deleteGoodsPrice(Long id,String sku,Long storeId);

    GoodsStorePriceVO getGoodsPrice(String sku, Long storeId);

    void updateStatusToOnSale(Long id, String sku, Long storeId);

    void updateStatusToOffSale(Long id, String sku, Long storeId);

    IPage<GoodsAndPriceListVO> getMiniGoodsPriceList(Page page, Long storeId,Long addressId,Long categoryId);

    GoodsPriceInfoVO getGoodsPriceInfo(Long storeId, Long addressId,String sku);

    GoodsAttributePriceInfoVO getGoodsDetailInfo(Long storeId, Long addressId, String sku);

    GoodsSkuAndPriceVO getMiniGoodsDetailPrice(Long storeId, Long addressId, String sku);

    IPage<GoodsHomePagePriceVO> getMiniGoodsBarginPrice(Page page, Long storeId, Long addressId);

    IPage<GoodsHomePagePriceVO> getMiniGoodsRecommendList(Page page, Long storeId, Long addressId);

    IPage<GoodsHomePagePriceVO> getMiniCookBookList(Page page, Long storeId, Long addressId);

    IPage<GoodsAndPriceListVO> getMiniGoodsPriceListByName(Page page, Long storeId, Long addressId, String goodsName);

    void updateIsRecommend(Long id, String sku, Long storeId);

    void updateIsNotRecommend(Long id, String sku, Long storeId);

    IPage<GoodsStorePriceListVO> getGoodsVipPriceList(Page page, GoodsStorePriceSearchVO goodsStorePriceSearchVO);

    IPage<GoodsAndPriceListVO> getMiniFastFoodPriceListByName(Page page, Long storeId, Long addressId, String goodsName);

    IPage<GoodsStorePriceListVO> getGoodsChildPriceList(Page page, Long storeId);

    IPage<GoodsStorePriceListVO> getGoodsIngredientPriceList(Page page, Long storeId);

    IPage<GoodsAndPriceListVO> getRentBottleGoodsPriceList(Page page, Long storeId, Long addressId,Long categoryId);
}
