package com.zsyc.goods.bo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GoodVipBo {

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
     * vip价格
     */
    private Integer VipPrice;

    /**
     * sku
     */
    private String sku;

    /**
     * 商品id
     */
    private Long goodsId;
}
