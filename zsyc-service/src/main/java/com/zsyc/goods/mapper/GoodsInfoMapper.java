package com.zsyc.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.bo.GoodsInfoListBO;
import com.zsyc.goods.entity.GoodsInfo;
import com.zsyc.goods.vo.GoodCustomVo;
import com.zsyc.goods.vo.GoodsInfoListSearchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品总表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsInfoMapper extends BaseMapper<GoodsInfo> {

    Integer insertGoodsList(List<GoodsInfo> goodsInfos);

    IPage<GoodsInfoListBO> selectGoodInfoList(Page page, @Param("is_del") Integer isNotDel,@Param("goods_search") GoodsInfoListSearchVO goodsInfoListSearchVO,@Param("goods_type") Integer goods_type);

    GoodsInfoListBO selectGoodsInfo(@Param("sku")String sku, @Param("is_del")Integer isNotDel);

    IPage<GoodsInfoListBO> selectGoodInfoTypeList(Page page,@Param("is_del")Integer isNotDel,@Param("goods_type")Integer normal);

    /**
     * 商品批量上下架
     * @param map
     * @return
     */
    int updateGoodsInfo(@Param("params")Map<String, Object> map);


    /**
     * 门店自定义商品查询
     * @param storeId
     * @return
     */
    IPage<GoodCustomVo> getGoodsCustom(IPage<GoodCustomVo> customVoIPage, Long storeId, Long categoryId, Long addressId);

    List<GoodsInfoListBO> selectChildGoodsInfoList(@Param("list")List<String> groupSubSku, @Param("is_del")Integer isNotDel, @Param("status")String onSale);

    IPage<GoodsInfo> selectGoodsInfoBySku(Page page, @Param("goods_type")Integer group, @Param("is_del")Integer isNotDel, @Param("sku")String sku);

    IPage<GoodsInfoListBO> selectGoodInfoListByStyle(Page page, @Param("goods_type")Integer normal, @Param("is_del")Integer isNotDel, @Param("list")List<Integer> goods_styles);
}
