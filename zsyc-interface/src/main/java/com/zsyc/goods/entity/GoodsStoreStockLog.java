package com.zsyc.goods.entity;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 库存变更记录
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsStoreStockLog extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 商品编码
     */
    private String sku;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 扣减或者增加数量
     */
    private Long num;

    /**
     * add增加 ,reduce扣减
     */
    private String operate;

    /**
     * 创建人
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
