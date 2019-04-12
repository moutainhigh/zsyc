package com.zsyc.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单子商品快照表
 * </p>
 *
 * @author MP
 * @since 2019-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(description = "订单")
@TableName(value = "order_sub_goods")
public class OrderSubGoods extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单子表ID
     */
    private Long orderSubId;

    /**
     * 组合商品sku码
     */
    private String sku;

    /**
     * 子商品sku码
     */
    private String skuSub;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 单价
     */
    private Integer price;

    /**
     * 商品类型(0普通,1瓶,2气,3水,4配料,5水桶)
     */
    private Integer goodsStyle;

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


}
