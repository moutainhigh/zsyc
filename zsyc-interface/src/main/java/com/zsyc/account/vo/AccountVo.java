package com.zsyc.account.vo;

import com.zsyc.account.entity.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Created by lcs on 2018-12-29.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AccountVo extends Account {

	/**
	 * 二次确认密码
	 */
	private String passwordConfirm;

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	private Integer currentPage;

	private Integer pageSize;
}
