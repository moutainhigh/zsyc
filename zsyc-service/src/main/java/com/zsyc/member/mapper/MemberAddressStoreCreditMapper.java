package com.zsyc.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.member.entity.MemberAddressStoreCredit;


public interface MemberAddressStoreCreditMapper extends BaseMapper<MemberAddressStoreCredit> {

    /**
     * 查询会员账期列表
     * @param page
     * @param storeId
     * @return
     */
    IPage<MemberAddressStoreCredit> getMemberStoreCreditList(IPage<MemberAddressStoreCredit> page, Long storeId,
                                                             String name, String telephone, String status);


}
