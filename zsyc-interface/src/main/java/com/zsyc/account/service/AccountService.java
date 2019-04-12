package com.zsyc.account.service;

import com.zsyc.account.entity.AccountBind;
import springfox.documentation.service.ApiListing;

/**
 * Created by lcs on 2019-04-09.
 */
public interface AccountService {
	/**
	 * 获取一个 AccountBind
	 * @param accountId
	 * @param type
	 * @return
	 */
	AccountBind getAccountBind(Long accountId, String type);
}
