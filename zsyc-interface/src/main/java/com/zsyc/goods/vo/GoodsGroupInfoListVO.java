package com.zsyc.goods.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class GoodsGroupInfoListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品sku码 唯一商品
     */
    private String sku;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 多张图片逗号分割
     */
    private String picture;

    /**
     * 商品详情展示类型
     */
    private Integer childType;

    /**
     * 子商品列表
     */
    private List<GoodsInfoListVO> goodsInfoListVOS;
}
