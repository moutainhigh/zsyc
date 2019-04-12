package com.zsyc.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.entity.GoodsStoreStock;
import com.zsyc.goods.vo.GoodsStoreStockListVO;
import com.zsyc.goods.vo.GoodsStoreStockVO;

/**
 * <p>
 * 商品库存表
 服务类
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsStoreStockService {


    IPage<GoodsStoreStockListVO> getGoodsStoreStockList(Page page, Long storeId);

    GoodsStoreStockVO getGoodsStoreStock(Long storeId, String sku);

    void updateGoodsStoreStock(GoodsStoreStock goodsStoreStock,Long id);

    void addStoreStockByStoreId(GoodsStoreStock goodsStoreStock,Long id);

    void declineStockByStoreId(GoodsStoreStock goodsStoreStock,Long id);
}
