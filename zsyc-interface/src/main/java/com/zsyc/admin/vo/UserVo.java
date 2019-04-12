package com.zsyc.admin.vo;

import com.zsyc.admin.entity.User;
import lombok.Data;

/**
 * Created by lcs on 2019-01-24.
 */
@Data
public class UserVo extends User {
	/**
	 * 登录密码 account.password
	 */
	private String accountPassword;
}
