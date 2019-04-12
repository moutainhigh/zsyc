package com.zsyc.goods.vo;

import com.zsyc.goods.bo.GoodsAttributeValueBO;
import com.zsyc.goods.entity.GoodsInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GoodCustomVo extends GoodsInfo {

    /**
     * 商品属性
     */
    private List<GoodsAttributeValueBO> goodsAttributeValueBOS;

    /**
     * 自定义价格
     */
    private int price;



}
