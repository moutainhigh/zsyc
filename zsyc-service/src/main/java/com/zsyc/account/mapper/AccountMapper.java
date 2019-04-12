package com.zsyc.account.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zsyc.account.entity.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 帐号 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2018-12-25
 */
public interface AccountMapper extends BaseMapper<Account> {

	List<Account> getAccounts(@Param("num") Integer num);

	/**
	 * 获取账号列表
	 * @param iPage
	 * @param account
	 * @param status
	 * @return
	 */
	IPage<Account> getAccountList(IPage<Account> iPage, String account, Integer status);
}
