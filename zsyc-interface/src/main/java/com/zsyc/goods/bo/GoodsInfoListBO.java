package com.zsyc.goods.bo;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsInfoListBO extends BaseBean {

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
     * 品牌编码
     */
    private String brandCode;

    /**
     * 状态
     */
    private String status;

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


}
