package com.zsyc.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.entity.GoodsStoreStock;
import com.zsyc.goods.vo.GoodsStoreStockListVO;
import com.zsyc.goods.vo.GoodsStoreStockVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品库存表
 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsStoreStockMapper extends BaseMapper<GoodsStoreStock> {

    IPage<GoodsStoreStockListVO> selectGoodsStoreStockList(Page page, @Param("store_id")Long storeId, @Param("is_del") Integer isNotDel);

    GoodsStoreStockVO selectGoodsStoreStock(@Param("store_id")Long storeId, @Param("is_del")Integer isNotDel, @Param("sku")String sku);
}
