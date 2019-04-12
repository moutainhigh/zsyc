package com.zsyc.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.goods.entity.GoodsBrandRelation;

import java.util.List;

/**
 * <p>
 * 品牌商品关联表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsBrandRelationMapper extends BaseMapper<GoodsBrandRelation> {

    Integer insertBrandList(List<GoodsBrandRelation> goodsBrandRelations);
}
