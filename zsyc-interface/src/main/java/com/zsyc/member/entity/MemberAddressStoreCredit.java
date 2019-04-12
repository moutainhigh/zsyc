package com.zsyc.member.entity;

import java.time.LocalDateTime;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户帐期
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberAddressStoreCredit extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 可用额度
     */
    private Long availableCredit;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 额度（分）
     */
    private Long credit;

    /**
     * 用户地址店铺表ID

     */
    private Long memberAddressStoreId;

    /**
     * 店铺ID（冗余）
     */
    private Long storeId;

    /**
     * 有效天数
     */
    private Integer availableDay;

    private String status;

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
