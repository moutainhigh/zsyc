package com.zsyc.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.member.entity.MemberBalance;
import com.zsyc.member.entity.MemberBalanceLog;

/**
 * <p>
 * 会员充值记录 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-03-13
 */
public interface MemberBalanceLogMapper extends BaseMapper<MemberBalanceLog> {

    /**
     * 会员余额列表查询接口
     */
    IPage<MemberBalanceLog> getBalanceLogList(IPage<MemberBalanceLog> page, Long memberId, String name, String telephone);
}
