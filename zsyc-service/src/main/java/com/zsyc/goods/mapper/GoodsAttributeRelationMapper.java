package com.zsyc.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.goods.bo.GoodsAttributeValueBO;
import com.zsyc.goods.entity.GoodsAttributeRelation;

import java.util.List;

/**
 * <p>
 * 商品属性关联表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsAttributeRelationMapper extends BaseMapper<GoodsAttributeRelation> {

    Integer insertGoodsAttributeRelationList(List<GoodsAttributeRelation> goodsAttributeRelations);

    List<GoodsAttributeValueBO> selectValueNameList(List<String> skus);

    List<GoodsAttributeValueBO> selectValueNameListById(String sku);
}
