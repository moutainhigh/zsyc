package com.zsyc.account.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.zsyc.IEnum;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 绑定帐号
 * </p>
 *
 * @author MP
 * @since 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("zs_account_bind")
public class AccountBind extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * account_id
     */
    private Long accountId;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getBindAccount() {
        return bindAccount;
    }

    public void setBindAccount(String bindAccount) {
        this.bindAccount = bindAccount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 绑定帐号名
     */
    private String bindAccount;

    /**
     * 绑定类型（TEL,EMAIL,WECHAT）
     */
    private String type;

    /**
     * 状态，1:有效，0:无效
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    public enum AccountBindType implements IEnum{

        TEL("电话"), EMAIL("邮件"), WECHAT("微信"), MINIPROGRAM("不知什么枚举"),
        PUBLIC_OPENID("公众号openid"), UNIONID("UNIONID"), PROGRAM_OPENID("小程序openid");

        private String desc;

        private AccountBindType(String desc){
            this.desc = desc;
        }

        @Override
        public String desc() {
            return this.desc;
        }

        @Override
        public String val() {
            return this.name();
        }
    }

}
