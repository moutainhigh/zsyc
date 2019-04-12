package com.zsyc.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.member.entity.MemberAddressStore;
import com.zsyc.member.vo.MemberAddressStoreVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberAddressStoreMapper extends BaseMapper<MemberAddressStore> {


    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertList(@Param("list") List<MemberAddressStore> list);

    /**
     * 获取地址和该地址的门店类型
     * @param addressId
     * @return
     */
    List<MemberAddressStoreVo> getMemberAddressStoreAndSoreType(Long addressId);

    /**
     * 修改用户地址类型
     * @param addressId
     * @param addressType
     * @param isCustom
     * @return
     */
    int updateUserType(Integer addressId, String addressType, Integer isCustom);
}
