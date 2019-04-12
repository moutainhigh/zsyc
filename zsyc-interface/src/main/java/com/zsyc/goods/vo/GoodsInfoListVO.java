package com.zsyc.goods.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class GoodsInfoListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 1.普通商品，2.组合商品
     */
    private Integer goodsType;

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
     * 多张图片逗号分割
     */
    private String picture;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否删除
     */
    private Integer isDel;

    /**
     * 商品类型
     */
    private Integer goodsStyle;

    /**
     * 属性值名称
     */
    private List<GoodsAttributeValueVO> goodsAttributeValueVOS;


    /**
     * 状态
     */
    private String status;


}
