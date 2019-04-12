package com.zsyc.delivery.po;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliverStaffBillCo extends BaseBean {
    @Override
    public Long getId() {
        return id;
    }


    @Override
    public LocalDateTime getCreateTime() {
        return createTime;
    }


    /**
     * 主键
     */
    private Long id;

    /**
     * 配送员id
     */
    private Long masterId;

    /**
     * 结算日期
     */

    private LocalDateTime billTime;

    /**
     * 结算工资
     */
    private Integer billSalary;

    /**
     * 状态 DONE-已结算，UNPAID-未结算
     */
    private String billStatus;

    /**
     * 上缴款
     */
    private Integer turnIn;

    /**
     * 上缴状态 已上缴-DONE，未上缴-UNPAID
     */
    private String turnInStatus;

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

    /**
     * 是否删除
     */
    private Integer isDel;





}
