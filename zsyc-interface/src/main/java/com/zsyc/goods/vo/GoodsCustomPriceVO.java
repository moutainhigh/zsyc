package com.zsyc.goods.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 商品自定义价格
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsCustomPriceVO extends GoodsCustomPriceListVO {

    /**
     * 属性值名称
     */
    private List<GoodsAttributeValueVO> goodsAttributeValueVOS;

}
