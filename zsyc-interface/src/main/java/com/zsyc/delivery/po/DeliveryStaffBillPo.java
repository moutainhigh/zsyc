package com.zsyc.delivery.po;

import com.zsyc.delivery.entity.DeliveryStaffBill;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配送结算
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeliveryStaffBillPo extends BaseBean {

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

}
