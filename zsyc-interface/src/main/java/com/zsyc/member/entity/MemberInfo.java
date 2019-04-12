package com.zsyc.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会员扩展表
 * </p>
 *
 * @author MP
 * @since 2019-04-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberInfo extends BaseBean {

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
     * 呢称
     */
    private String nickName;

    /**
     * 性别:0不知，1男2女
     */
    private Integer sex;

    /**
     * 生日
     */
    private LocalDateTime birthday;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 会员
     */
    private String email;

    /**
     * 会员状态('NORMAL'正常,'LOCK'锁定)
     */
    private String status;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 是否删除 0没删除1删除
     */
    private Integer isDel;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private String img;

    /**
     * 父级memberId
     */
    private Long parentId;


}
