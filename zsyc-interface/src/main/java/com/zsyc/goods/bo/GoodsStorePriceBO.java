package com.zsyc.goods.bo;

import com.zsyc.goods.vo.GoodsAttributeValueVO;
import com.zsyc.goods.vo.GoodsInfoListVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GoodsStorePriceBO extends GoodsInfoListVO{

    /**
     * 主键
     */
    private Long id;

    /**
     * 商品价格
     */
    private Integer price;

    /**
     * vip价格
     */
    private Integer vipPrice;

    /**
     * 成本价格
     */
    private Integer costPrice;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 属性值名称
     */
    private List<GoodsAttributeValueVO> goodsAttributeValueVOS;

    /**
     * 商品sku码 唯一商品
     */
    private String sku;

    /**
     * 是否特价
     */
    private Integer isBargainPrice;

    /**
     * 门店上下架
     */
    private String status;
}