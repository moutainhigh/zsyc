package com.zsyc.account.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 登录日志
 * </p>
 *
 * @author MP
 * @since 2019-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("zs_account_login_log")
public class AccountLoginLog extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 帐号ID
     */
    private Long accountId;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * IP
     */
    private String ip;

    /**
     * 数据来源
     */
    private String dataSources;

    /**
     * 登录方法(帐号密码、微信、手机。。。)
     */
    private String loginType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
