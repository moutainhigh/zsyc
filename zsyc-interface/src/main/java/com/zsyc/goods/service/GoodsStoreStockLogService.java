package com.zsyc.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.entity.GoodsStoreStockLog;

/**
 * <p>
 * 库存变更记录 服务类
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsStoreStockLogService {

    IPage<GoodsStoreStockLog> getGoodsStoreStockLogList(Page page, Long storeId);

    GoodsStoreStockLog getGoodsStoreStockLog(Long storeId, String sku);
}
