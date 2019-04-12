package com.zsyc.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author MP
 * @since 2019-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user")
public class User extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 帐号ID
     */
    private Long accountId;

    /**
     * 账号
     */
    private String userName;

    /**
     * 手机
     */
    private String telphone;

    /**
     * email
     */
    private String email;

    /**
     * 用户名
     */
    private String name;

    /**
     * 登录密码
     */
    @Deprecated
    private String password;

    /**
     * 盐值
     */
    @Deprecated
    private String salt;

    /**
     * 用户状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
