package com.zsyc.member.vo;

import com.zsyc.framework.base.BaseBean;
import com.zsyc.member.entity.MemberAddress;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 会员地址
 * </p>
 *
 * @author MP
 * @since 2019-02-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberAddressVo extends MemberAddress {

    private String addressType;

    private Integer isCustom;

    private Integer isElevator;

}
