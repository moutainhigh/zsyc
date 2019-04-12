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
 * 会员充值记录
 * </p>
 *
 * @author MP
 * @since 2019-04-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberBalanceLog extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 操作（'CUT'消费,'ADD'充值,'BOUNS'红包提成,'CASH'提现）
     */
    private String operate;

    /**
     * 充值人民币
     */
    private Long rmb;

    /**
     * 兑换的平台币
     */
    private Long platformAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 付款方式（1微信支付）
     */
    private Integer payType;


}
