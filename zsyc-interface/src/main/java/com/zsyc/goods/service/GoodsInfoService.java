package com.zsyc.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.vo.*;

/**
 * <p>
 * 商品总表 服务类
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsInfoService{

    IPage<GoodsInfoListVO> getGoodsInfoList(Page page, GoodsInfoListSearchVO goodsInfoListSearchVO);

    void addGoodsInfo(GoodsInfoInsertVO goodsInfoVO, Long id);

    void updateGoodsInfo(GoodsInfoUpdateVO goodsInfoUpdateVO,Long id);

    void deleteGoodsInfo(Long id,String sku);

    GoodsInfoVO getGoodsInfo(String sku);

    IPage<GoodsInfoListVO> getGoodsInfoTypeList(Page page);

    IPage<GoodsGroupInfoListVO> getGoodsGroupInfoList(Page page, String sku);

    IPage<GoodsInfoListVO> getGoodsChildInfoList(Page page, Long id);

    IPage<GoodsInfoListVO> getGoodsIngredientInfoList(Page page, Long id);
}
