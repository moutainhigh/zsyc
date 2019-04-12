package com.zsyc.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * Created by lcs on 2019-01-17.
 */
public class BaseService {
	public static <T> QueryWrapper<T> newQueryWrapper(Class<T> clazz){
		return new QueryWrapper<T>();
	}
}
