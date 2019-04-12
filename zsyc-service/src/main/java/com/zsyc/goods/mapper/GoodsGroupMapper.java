package com.zsyc.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.goods.bo.GoodsGroupBO;
import com.zsyc.goods.bo.GoodsNameGroupBO;
import com.zsyc.goods.entity.GoodsGroup;
import com.zsyc.goods.vo.GoodsGroupSkuNameVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品组合表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsGroupMapper extends BaseMapper<GoodsGroup> {

    Integer insertGoodsGroupList(List<GoodsGroup> goodsGroups);

    List<GoodsGroupSkuNameVO> selectGoodsSkuNameInfo(String sku);

    List<GoodsGroupBO> selectGoodsPrice(@Param("store_id")Long storeId,@Param("list")List<String> collect);

    List<GoodsNameGroupBO> selectGoodsNameGroupList(String sku);
}
