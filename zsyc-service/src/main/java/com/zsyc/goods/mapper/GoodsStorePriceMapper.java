package com.zsyc.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.bo.GoodsAndPriceBO;
import com.zsyc.goods.bo.GoodsBarginRecommendPriceBO;
import com.zsyc.goods.bo.GoodsStorePriceBO;
import com.zsyc.goods.bo.GoodsStorePriceListBO;
import com.zsyc.goods.entity.GoodsInfo;
import com.zsyc.goods.entity.GoodsStorePrice;
import com.zsyc.goods.vo.GoodsStorePriceSearchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品价格表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsStorePriceMapper extends BaseMapper<GoodsStorePrice> {

    IPage<GoodsStorePriceListBO> selectGoodsPriceAndName(Page page, @Param("is_del")Integer isNotDel, @Param("status")String onSale, @Param("store_id")Long storeId);

    GoodsStorePriceBO selectGoodsPriceVO(@Param("sku")String sku, @Param("store_id")Long storeId, @Param("is_del")Integer isNotDel, @Param("status")String onSale);

    IPage<GoodsAndPriceBO> selectGoodsPriceAndNameByCategory(Page page, @Param("is_del")Integer isNotDel, @Param("status")String onSale, @Param("store_id")Long storeId,@Param("category_id")Long categoryId,@Param("category_type")String category_type);

    List<GoodsInfo> selectStoreSkuList(@Param("spu")String spu, @Param("store_id")Long storeId, @Param("is_del")Integer isNotDel, @Param("status")String onSale);

    List<GoodsBarginRecommendPriceBO> selectStoreGoodsPriceList(@Param("page_num")Long pageNum,@Param("page_size")Long pageSize, @Param("store_id")Long storeId, @Param("is_del")Integer isNotDel, @Param("status")String onSale, @Param("is_bargain_price")Integer is_bargain_price,@Param("is_recommend")Integer is_recommend,@Param("goods_style")Integer goods_style,@Param("goods_type")Integer goods_type);

    Integer goodsBarginPriceCount(@Param("store_id")Long storeId, @Param("is_del")Integer isNotDel, @Param("status")String onSale, @Param("is_bargain_price")Integer is_bargain_price,@Param("is_recommend")Integer is_recommend,@Param("goods_style")Integer goods_style,@Param("goods_type")Integer goods_type);

    List<GoodsBarginRecommendPriceBO> selectGoodsCookBookList(@Param("page_num")Long pageNum, @Param("page_size")Long pageSize, @Param("store_id")Long storeId, @Param("is_del")Integer isNotDel, @Param("status")String onSale, @Param("goods_style")Integer normal, @Param("category_type")String category_type,@Param("is_recommend")Integer is_recommend);

    Integer selectGoodsCookBookCount(@Param("store_id")Long storeId, @Param("is_del")Integer isNotDel, @Param("status")String onSale, @Param("goods_style")Integer normal, @Param("category_type")String category_type, @Param("is_recommend")Integer isRecommend);

    IPage<GoodsAndPriceBO> selectGoodsListByName(Page page, @Param("is_del")Integer isNotDel, @Param("status")String onSale, @Param("store_id")Long storeId, @Param("goods_name")String goodsName,@Param("goods_type")Integer goods_type,@Param("goods_style")Integer goods_style);

    IPage<GoodsAndPriceBO> selectWaterOrGasPriceList(Page page, @Param("is_del")Integer isNotDel, @Param("status")String onSale, @Param("store_id")Long storeId,@Param("category_id")Long categoryId);

    IPage<GoodsStorePriceListBO> getGoodsVipPriceList(Page page, @Param("store_price_search")GoodsStorePriceSearchVO goodsStorePriceSearchVO, @Param("is_del")Integer isNotDel,@Param("goods_type")Integer goods_type);

    IPage<GoodsStorePriceListBO> selectGoodsChildPriceAndName(Page page, @Param("is_del")Integer isNotDel, @Param("status")String onSale, @Param("store_id")Long storeId,@Param("goods_type")Integer goods_type,@Param("goods_style")Integer goods_style);

    IPage<GoodsStorePriceListBO> selectGoodsIngredientPriceAndName(Page page, @Param("is_del")Integer isNotDel, @Param("status")String onSale, @Param("store_id")Long storeId,@Param("goods_type")Integer goods_type,@Param("goods_style")Integer goods_style);
}
