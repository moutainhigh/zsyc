package com.zsyc.member.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 会员余额
 * </p>
 *
 * @author MP
 * @since 2019-03-13
 */
@Data
@Accessors(chain = true)
public class MemberBalancePo {

   private Integer bouns;

   private Long money;

   private Long balanceId;

   private Long memberId;

   private Integer payType;

}
