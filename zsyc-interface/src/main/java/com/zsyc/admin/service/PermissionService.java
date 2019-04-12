package com.zsyc.admin.service;

import com.zsyc.admin.entity.Permission;
import com.zsyc.admin.entity.User;

import java.util.List;

/**
 * Created by lcs on 2019-01-16.
 */
public interface PermissionService {

	/**
	 * 添加一个权限
	 *
	 * @param permission
	 * @return
	 */
	Permission addPermission(User user, Permission permission);

	/**
	 * 获取一个权限
	 * @param permissionId
	 * @return
	 */
	Permission getPermission(Long permissionId);

	/**
	 * 获取一个权限
	 * @param value
	 * @param type
	 * @return
	 */
	Permission getPermission(String value,String type);

	/**
	 * 获取下级权限
	 * @param permissionId
	 * @return
	 */
	List<Permission> getSubPermission(Long permissionId);

	/**
	 * 更新权限
	 * @param permission
	 * @return
	 */
	int updatePermission(User user,Permission... permission);

	/**
	 * 删除权限
	 * @param permissionId
	 * @return
	 */
	int deletePermission(Long...permissionId);

}
