package com.zsyc.delivery.vo;

import com.zsyc.delivery.entity.DeliveryStaffBill;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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
public class DeliveryStaffBillVo extends DeliveryStaffBill {



}
