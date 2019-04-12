package com.zsyc.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 备货订单商品中间表
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class WarehousePackOrderGoods extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分拣订单表ID
     */
    private Long warehousePackOrderId;

    /**
     * 订单商品快照表id
     */
    private Long orderGoodsId;

    /**
     * 冗余数量
     */
    private Integer num;

    /**
     * 冗余计量单位(总数，如：250g）
     */
    private String unit;

    /**
     * 状态 DONE-完成，SHORTAGE-缺货
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 备货订单商品表id(备货子表)
     */
    private Long warehouseOrderGoodsId;


}
