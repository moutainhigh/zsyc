package com.zsyc.store.entity;

import java.time.LocalDateTime;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 开收盘时间
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StoreOpenTime extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 开盘时间
     */
    private LocalDateTime openTime;

    /**
     * 收盘时间
     */
    private LocalDateTime closeTime;

    /**
     * 状态（是否启用）0：否 ，1：是
     */
    private Integer status;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public LocalDateTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalDateTime openTime) {
        this.openTime = openTime;
    }

    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalDateTime closeTime) {
        this.closeTime = closeTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
