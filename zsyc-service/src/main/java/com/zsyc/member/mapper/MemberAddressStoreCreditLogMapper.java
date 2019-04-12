package com.zsyc.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.member.entity.MemberAddressStoreCredit;
import com.zsyc.member.entity.MemberAddressStoreCreditLog;

import java.util.List;


public interface MemberAddressStoreCreditLogMapper extends BaseMapper<MemberAddressStoreCreditLog> {

    /**
     * 查询门店账期使用记录
     * @param storeId
     * @return
     */
    List<MemberAddressStoreCreditLog> getMemberAddressStoreCreditLog(Long storeId);
}
