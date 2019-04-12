package com.zsyc.aftersale.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 售后单主表
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AfterSaleOrder extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分拣订单id
     */
    private Long warehousePackOrderId;

    /**
     * 子订单id
     */
    private Long orderSubId;

    /**
     * 客服处理状态
     */
    private String status;

    /**
     * 售后单号
     */
    private String afterSaleNo;

    /**
     * 配货员电话
     */
    private String staffPhone;

    /**
     * 配货员名称
     */
    private String staffName;

    /**
     * 客户地址id
     */
    private Long memberAddressId;

    /**
     * 客服备注
     */
    private String remark;


}
