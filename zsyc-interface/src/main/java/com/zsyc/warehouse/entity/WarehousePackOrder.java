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
 *
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class WarehousePackOrder extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    public  Long id;

    /**
     * 备货主表id
     */
    private Long warehouseOrderId;

    /**
     * 订单id（订单子表id）
     */
    private Long orderId;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 分拣员id
     */
    private Long warehouseStaffId;

    /**
     * 分拣完成时间
     */
    private LocalDateTime doneTime;

    /**
     * 备注（备注分拣订单特殊情况）
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 分拣状态
     */
    private String status;




}
