package com.zsyc.goods.vo;

import com.zsyc.goods.bo.GoodsSkuBO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class GoodsAttributePriceInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品sku码 唯一商品
     */
    private String sku;

    /**
     * 商品spu码 同款商品
     */
    private String spu;

    /**
     * 1.普通商品，2.组合商品
     */
    private Integer goodsType;

    /**
     * 多张图片逗号分割
     */
    private String picture;

    /**
     * 状态
     */
    private String status;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品类型
     */
    private Integer goodsStyle;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品sku码属性
     */
    private List<GoodsSkuBO> goodsSkuBOS;

    /**
     * 属性值名称
     */
    private List<GoodsNameAndAttributeVO> goodsNameAndAttributeVOS;

    /**
     * 商品详情展示类型
     */
    private Integer type;
}
