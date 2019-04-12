package com.zsyc.account.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zsyc.account.entity.AccountBind;
import com.zsyc.account.mapper.AccountBindMapper;
import com.zsyc.admin.service.BaseService;
import com.zsyc.common.AssertExt;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lcs on 2019-04-09.
 */
@Service
public class AccountServiceImpl extends BaseService implements AccountService {
	@Autowired
	private AccountBindMapper accountBindMapper;

	@Override
	public AccountBind getAccountBind(Long accountId, String type) {
		AssertExt.hasId(accountId, "无效 accountId");
		AssertExt.notBlank(type, "无效 type");
		return this.accountBindMapper.selectOne(newQueryWrapper(AccountBind.class)
				.eq("account_id", accountId).eq("type", type));

	}
}
