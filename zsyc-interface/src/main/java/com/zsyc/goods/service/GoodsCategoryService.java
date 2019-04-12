package com.zsyc.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.entity.GoodsCategory;
import com.zsyc.goods.vo.GoodsCategoryListVO;
import com.zsyc.goods.vo.GoodsCategoryTreeListVO;

import java.util.List;

/**
 * <p>
 * 商品分类表 服务类
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsCategoryService{

    IPage<GoodsCategory> getGoodsCategoryList(Page page,Long id);

    void addGoodsCategory(GoodsCategory goodsCategory,Long id);

    void updateGoodsCategory(GoodsCategory goodsCategory);

    void deleteGoodsCategory(Long id);

    List<GoodsCategory> getMiniGoodsCategoryList(Long storeId,String categoryType);

    GoodsCategoryListVO getMiniGoodsCategoryMenuList(Long storeId);

    IPage<GoodsCategoryTreeListVO> getGoodsCategoryTreeList(Page page,Long id,String categoryName);

    List<GoodsCategory> getGoodsCategoryListByStoreId(Long storeId,Long parentId);
}
