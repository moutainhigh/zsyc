package com.zsyc.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.goods.entity.GoodsCategoryRelation;

import java.util.List;

/**
 * <p>
 * 分类商品关系表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsCategoryRelationMapper extends BaseMapper<GoodsCategoryRelation> {

    Integer insertCategoryList(List<GoodsCategoryRelation> goodsCategoryRelations);
}
