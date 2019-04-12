package com.zsyc.goods.bo;

import com.zsyc.goods.vo.GoodsInfoListVO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GoodsStorePriceListBO extends GoodsInfoListVO {

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
     * 多张图片逗号分割
     */
    private String picture;

    /**
     * 品牌名称
     */
    private String brandName;

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

    /**
     * 是否推荐 1推荐 0不推荐
     */
    private Integer isRecommend;

    /**
     * 商品类型
     */
    private Integer goodsStyle;
}
