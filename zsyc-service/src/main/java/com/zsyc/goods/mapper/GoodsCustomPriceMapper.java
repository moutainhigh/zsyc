package com.zsyc.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.entity.GoodsCustomPrice;
import com.zsyc.goods.vo.GoodsCustomPriceListVO;
import com.zsyc.goods.vo.GoodsCustomPriceVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商品自定义价格 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsCustomPriceMapper extends BaseMapper<GoodsCustomPrice> {

    IPage<GoodsCustomPriceListVO> selectCustomPriceList(Page page, @Param("store_id")Long storeId, @Param("is_del")Integer isNotDel, @Param("skus")List<String> skus, @Param("address_id")Long addressId, @Param("sku")String sku, @Param("changeBeforePrice")Integer changeBeforePrice, @Param("changeAfterPrice")Integer changeAfterPrice, @Param("beforeTime")LocalDateTime beforeTime, @Param("afterTime")LocalDateTime afterTime);

    GoodsCustomPriceVO selectCustomPriceInfo(@Param("store_id")Long storeId, @Param("address_id")Long addressId, @Param("is_del")Integer isNotDel);
}
