package com.zsyc.account.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zsyc.account.entity.AccountBind;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 绑定帐号 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2018-12-25
 */
public interface AccountBindMapper extends BaseMapper<AccountBind> {

    @Override
    AccountBind selectOne(@Param(Constants.WRAPPER) Wrapper<AccountBind> queryWrapper);

    @Override
    int insert(AccountBind entity);

}
