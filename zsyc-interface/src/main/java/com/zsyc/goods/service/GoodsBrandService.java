package com.zsyc.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.entity.GoodsBrand;

/**
 * <p>
 * 商品品牌表 服务类
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
public interface GoodsBrandService {

    IPage<GoodsBrand> getGoodsBrandList(Page page,String name);

    void addGoodsBrand(GoodsBrand goodsBrand,Long id);

    void updateGoodsBrand(GoodsBrand goodsBrand);

    void deleteGoodsBrand(Long id);

    GoodsBrand getGoodsBrand(Long id);
}
