package com.zsyc.goods.entity;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 商品组合表
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsGroup extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * sku编码 组合商品
     */
    private String sku;

    /**
     * sku编码 子商品
     */
    private String subSku;

    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 子商品排序
     */
    private Integer sort;


}
