package com.zsyc.goods.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.common.AssertExt;
import com.zsyc.goods.entity.GoodsStoreStockLog;
import com.zsyc.goods.mapper.GoodsStoreStockLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 库存变更记录 服务实现类
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Service
@Slf4j
public class GoodsStoreStockLogServiceImpl implements GoodsStoreStockLogService {

    @Autowired
    private GoodsStoreStockLogMapper goodsStoreStockLogMapper;

    @Override
    public IPage<GoodsStoreStockLog> getGoodsStoreStockLogList(Page page, Long storeId) {
        return goodsStoreStockLogMapper.selectPage(page,new QueryWrapper<GoodsStoreStockLog>().eq("store_id",storeId));
    }

    @Override
    public GoodsStoreStockLog getGoodsStoreStockLog(Long storeId, String sku) {
        GoodsStoreStockLog goodsStoreStockLog=goodsStoreStockLogMapper.selectOne(new QueryWrapper<GoodsStoreStockLog>().eq("store_id",storeId).eq("sku",sku));
        AssertExt.notNull(goodsStoreStockLog,"该商品库存不存在失败");
        return goodsStoreStockLog;
    }
}
