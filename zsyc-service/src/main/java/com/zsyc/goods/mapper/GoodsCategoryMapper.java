package com.zsyc.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.entity.GoodsCategory;
import com.zsyc.goods.vo.GoodsCategoryTreeListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品分类表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsCategoryMapper extends BaseMapper<GoodsCategory> {

    Integer deleteByIdList(@Param("list")List<GoodsCategory> categories,@Param("is_del")Integer isNotDel);

    IPage<GoodsCategoryTreeListVO> selectCategoryParentList(Page page, @Param("parent_id")Long isParent, @Param("is_del")Integer isNotDel);

    List<GoodsCategoryTreeListVO> selectCategoryChildList(@Param("parent_id")Long isParent, @Param("is_del")Integer isNotDel);

    List<GoodsCategoryTreeListVO> selectGoodsCategoryList(Integer is_del);

    List<GoodsCategoryTreeListVO> selectGoodsCategorySearchList(@Param("id")Long id, @Param("category_name")String categoryName, @Param("is_del")Integer isNotDel);

    List<GoodsCategoryTreeListVO> selectGoodsCategoryBaseList(Integer isNotDel);

    GoodsCategory selectGoodsCategoryInfoBySku(String sku);
}
