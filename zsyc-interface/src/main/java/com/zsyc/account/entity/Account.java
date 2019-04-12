package com.zsyc.account.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.ApiParam;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 帐号
 * </p>
 *
 * @author MP
 * @since 2018-12-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("zs_account")

public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiParam(hidden = true)
    private Long id;

    /**
     * 帐号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * salt
     */
    private String salt;

    /**
     * 状态，1:有效,2:锁定,3:禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}
