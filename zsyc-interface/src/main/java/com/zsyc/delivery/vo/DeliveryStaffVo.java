package com.zsyc.delivery.vo;

import com.zsyc.delivery.entity.DeliveryStaff;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 配送员
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
public class DeliveryStaffVo extends DeliveryStaff {

    /**
     * 配送员工资
     */
    private Integer amount;

    /**
     * 配送员上缴金额
     */
    private Integer turnIn;

    public Integer getTurnIn() {
        return turnIn;
    }

    public void setTurnIn(Integer turnIn) {
        this.turnIn = turnIn;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
