package com.zsyc.goods.bo;

import com.zsyc.goods.entity.GoodsInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GoodCustomBo{

    /**
     * 门店
     */
    private Long storeId;


    /**
     * 页码
     */
    private Integer currentPage;

    /**
     * 页数
     */
    private Integer pageSize;

    /**
     * 商品分类id
     */
    private Long categoryId;

    /**
     * 地址id
     */
    private Long addressId;

    /**
     * 价格
     */
    private Integer price;

    /**
     * sku
     */
    private String sku;

    /**
     * 商品id
     */
    private Long goodsId;
}
