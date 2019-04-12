package com.zsyc.member.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zsyc.member.entity.MemberInfo;
import com.zsyc.member.vo.MemberInfoVo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberInfoMapper extends BaseMapper<MemberInfo> {

    /**
     * 查询门店列表
     * @param name
     * @param beginTime
     * @param endTime
     * @return
     */
    IPage<MemberInfo> getMemberList(IPage<MemberInfo> page,
                                    String name,
                                    String beginTime,
                                    String endTime,
                                    String telephone,
                                    String status,
                                    Long storeId);

    @Override
    MemberInfo selectOne(@Param(Constants.WRAPPER) Wrapper<MemberInfo> queryWrapper);

    @Override
    int insert(MemberInfo entity);

    @Override
    int update(@Param(Constants.ENTITY) MemberInfo entity, @Param(Constants.WRAPPER) Wrapper<MemberInfo> updateWrapper);


    /**
     * 通过openiD或UnionId查询出用户
     * @param openId
     * @return
     */
    MemberInfo getMemberByUnionIdOrOpenId(String openId, String type);
}
