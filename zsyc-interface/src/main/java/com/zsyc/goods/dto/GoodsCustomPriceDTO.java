package com.zsyc.goods.dto;

import com.zsyc.goods.entity.GoodsCustomPrice;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class GoodsCustomPriceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 是否为vip
     */
    private Boolean vipType;

    /**
     * 自定义价格列表
     */
    private List<GoodsCustomPrice> goodsCustomPrices;

    /**
     * 商品sku码
     */
    private List<String> skus;
}
