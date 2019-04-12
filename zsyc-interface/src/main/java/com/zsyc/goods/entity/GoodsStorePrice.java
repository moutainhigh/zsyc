package com.zsyc.goods.entity;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 商品价格表
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsStorePrice extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 商品sku码
     */
    private String sku;

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 是否删除
     */
    private Integer isDel;

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
     * 红包价格
     */
    private Integer bouns;
}
