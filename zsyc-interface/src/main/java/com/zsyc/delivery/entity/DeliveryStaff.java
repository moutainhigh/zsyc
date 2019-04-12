package com.zsyc.delivery.entity;

import java.time.LocalDateTime;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配送员
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeliveryStaff extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 配送员姓名
     */
    private String masterName;

    /**
     * 账号id
     */
    private Long accountId;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 证件图片，身份证正反面，从业资格证
     */
    private String papers;

    /**
     * 父类id
     */
    private Long parentId;

    /**
     * 是否为负责人
     */
    private Integer isLeader;

    /**
     * 配送员类型 STORE-店铺配送，PLATFORM-平台配送
     */
    private String masterType;

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

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPapers() {
        return papers;
    }

    public void setPapers(String papers) {
        this.papers = papers;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(Integer isLeader) {
        this.isLeader = isLeader;
    }

    public String getMasterType() {
        return masterType;
    }

    public void setMasterType(String masterType) {
        this.masterType = masterType;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}
