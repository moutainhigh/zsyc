package com.zsyc.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.member.entity.MemberBalance;

import java.time.LocalDateTime;

/**
 * <p>
 * 会员余额 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-03-13
 */
public interface MemberBalanceMapper extends BaseMapper<MemberBalance> {

    /**
     * 充值
     * @param momey
     * @param balanceId
     * @param updateUser
     * @param updateTime
     * @return
     */
    int recharge(Long momey, Long balanceId, Long updateUser, LocalDateTime updateTime);


    /**
     * 会员余额列表查询接口
     */
    IPage<MemberBalance> getBalanceList(IPage<MemberBalance> page, String telephone, String name);

}
