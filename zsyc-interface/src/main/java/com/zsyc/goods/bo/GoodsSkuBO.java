package com.zsyc.goods.bo;

import com.zsyc.goods.vo.GoodsAttributeValueVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class GoodsSkuBO implements Serializable {

    /**
     * 商品sku码 唯一商品
     */
    private String sku;

    /**
     * 属性值名称
     */
    private List<GoodsAttributeValueVO> goodsAttributeValueVOS;
}
