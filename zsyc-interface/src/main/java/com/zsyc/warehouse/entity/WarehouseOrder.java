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
 * 备货主表
 * </p>
 *
 * @author MP
 * @since 2019-04-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class WarehouseOrder extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 备货订单编号
     */
    private String warehouseOrderNo;

    /**
     * 生成备货时间
     */
    private LocalDateTime createTime;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 备货状态 READY-准备中，STOCKING-备货中，DONE-确认备货
     */
    private String status;

    /**
     * 备货员id
     */
    private Long warehouseStaffId;

    /**
     * 接收备货时间
     */
    private LocalDateTime receiveTime;

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
     * 备注信息
     */
    private String remark;


}
