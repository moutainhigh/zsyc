package com.zsyc.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.account.vo.AccountVo;
import com.zsyc.admin.entity.Permission;
import com.zsyc.admin.entity.Role;
import com.zsyc.admin.entity.User;
import com.zsyc.admin.po.PermissionPo;
import com.zsyc.admin.vo.UserVo;
import com.zsyc.store.entity.StoreInfo;

import java.util.List;

/**
 * Created by lcs on 2019-01-14.
 */
public interface AdminService {

	/**
	 * 获取用户
	 */
	User getUser(Long userId);

	/**
	 * 获取所有角色
	 */
	List<Role> getAllRoles(Long loginUserId);

	/**
	 * 获取已绑定的角色
	 * @param loginUserId
	 * @return
	 */
	List<Role> getLoginRoles(Long loginUserId);

	/**
	 * 获取一个用户的角色
	 * @param userId
	 * @return
	 */
	List<Role> getRolesByUserId(Long userId);


	/**
	 * 删除角色
	 * @param id
	 * @return
	 */
	int deleteRole(Long id, Long loginUserId);

	/**
	 * 获取一个用户的权限
	 *
	 * @param userId
	 * @return
	 */
	List<Permission> getPermissionByUserId(Long userId, String type);

	/**
	 * 获取一个角色的权限
	 *
	 * @param roleId
	 * @return
	 */
	List<Permission> getPermissionByRoleId(Long roleId, String type);

	/**
	 * 添加一个角色
	 * @param role
	 * @return
	 */
	int addRole(Role role, Long loginUserId);


	/**
	 * 修改一个角色
	 * @param role
	 * @return
	 */
	int updateRole(Role role, Long loginUserId);

	/**
	 * 更新一个角色的权限
	 *
	 * @param roleId
	 * @param permissionList
	 */
	int updateRolePermission(Long roleId, List<Permission> permissionList, Long loginUserId);

	/**
	 * 更新一个角色的门店
	 * @param roleId
	 * @param storeIds
	 */
	int updateRoleStore(Long roleId, List<Long> storeIds, Long loginUserId);


	/**
	 * 获取一个角色的门店
	 */
	IPage<StoreInfo> getRoleStoreList(Long roleId, Integer currentPage, Integer pageSize);


	/**
	 * 获取所有的门店
	 * @return
	 */
	IPage<StoreInfo> getStoreList( Integer adcode, Integer currentPage, Integer pageSize, Integer storeTypeId);

	/**
	 * 获取一个角色绑定的门店
	 * @param loginUserId
	 * @return
	 */
	IPage<StoreInfo> getStoreList2(Long loginUserId);

	/**
	 * 添加一个用户
	 * @param user
	 * @return
	 */
	int addUser(UserVo user, Long loginUserId);

	/**
	 * 通过account
	 */
	User getUserByAccount(Long accountId);

	/**
	 * 更新一个用户的角色
	 *
	 * @param userId
	 * @param roleList
	 */
	int updateUserRole(Long userId, List<Role> roleList, Long loginUserId);


	/**
	 * 获取后台用户信息列表
	 * @return
	 */
	IPage<User> getUserList(String telephone, String name, Integer currentPage, Integer pageSize);


	/**
	 * 后台用户信息修改
	 * @return
	 */
	int updateUser(Long userId, String telephone, String email, String name, String remark, String status,
				   String accountPassword, String userName, Long accountId, Long loginUserId);


	/**
	 * 添加一个权限
	 * @param permission
	 * @return
	 */
	Permission addPermission(PermissionPo permission, User user);

}
