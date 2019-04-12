package com.zsyc.member.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zsyc.member.entity.MemberAddress;
import com.zsyc.member.entity.MemberAddressStore;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

public interface MemberAddressMapper extends BaseMapper<MemberAddress> {

    /**
     * 查询没绑定该类型门店的会员地址
     * @param storeTypeId
     * @return
     */
    List<MemberAddress> getMemberAddressNoStore(Long storeTypeId);


    /**
     * 查询会员地址
     * @param
     * @return
     */
    List<MemberAddress> getMemberAddressList(Long memeberId);


    /**
     * 修改地址为非默认
     * @param
     * @return
     */
    int updateNotDeafult(List<MemberAddress> memberAddressList);


    /**
     *门店用户地址列表接口
     */
    IPage<MemberAddress> getAddressByStoreId(IPage<MemberAddress> iPage, Long storeId, String consignee,
                                             String telephone, String locationAddress, Long adcode);
}
