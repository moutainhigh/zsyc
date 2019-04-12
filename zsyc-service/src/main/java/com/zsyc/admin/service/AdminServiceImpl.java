package com.zsyc.admin.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.account.entity.Account;
import com.zsyc.account.mapper.AccountMapper;
import com.zsyc.account.service.LoginService;
import com.zsyc.account.vo.AccountVo;
import com.zsyc.admin.entity.*;
import com.zsyc.admin.mapper.*;
import com.zsyc.admin.po.PermissionPo;
import com.zsyc.admin.vo.UserVo;
import com.zsyc.common.AssertExt;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.store.mapper.StoreInfoMapper;
import com.zsyc.store.service.StoreInfoService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lcs on 2019-01-14.
 */
@Service
@Slf4j
public class AdminServiceImpl extends BaseService implements AdminService {

	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private RolePermissionMapper rolePermissionMapper;
	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private LoginService loginService;
	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private RoleStoreMapper roleStoreMapper;
	@Autowired
	private StoreInfoMapper storeInfoMapper;
	@Autowired
	private StoreInfoService storeInfoService;
	@Autowired
	private PermissionService permissionService;

	@Override
	public User getUser(Long userId) {
		AssertExt.hasId(userId, "无效ID");
		return this.userMapper.selectById(userId);
	}

	@Override
	public List<Role> getAllRoles(Long loginUserId) {
		return roleMapper.getRoleList(loginUserId);
	}

    @Override
    public List<Role> getLoginRoles(Long loginUserId) {
        return roleMapper.getLoginRoles(loginUserId);
    }

    @Override
	public List<Role> getRolesByUserId(Long userId) {
		List<UserRole> list = this.userRoleMapper.selectList(new QueryWrapper<UserRole>().ge("user_id", userId));

		if(list.isEmpty()) return Collections.emptyList();
		AssertExt.isTrue(list.size() < 1000, "用户角色过多，无法加载");
		return this.roleMapper.selectBatchIds(list.stream().map(UserRole::getRoleId).distinct().collect(Collectors.toList()));
	}

	@Override
	public int deleteRole(Long id, Long loginUserId) {

		QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
		userRoleQueryWrapper.eq("role_id", id);
		int i = userRoleMapper.selectCount(userRoleQueryWrapper);
		AssertExt.isTrue(i == 0, "用户已绑定角色， 不能删除");

		QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
		rolePermissionQueryWrapper.eq("role_id", id);
		RolePermission permission = rolePermissionMapper.selectOne(rolePermissionQueryWrapper);

		roleMapper.deleteById(id);
		if(permission != null){
			rolePermissionMapper.deleteById(permission.getId());
		}

		return 1;
	}

	@Override
	public List<Permission> getPermissionByUserId(Long userId, String type) {
		AssertExt.hasId(userId,"无效用户ID");
		return this.permissionMapper.selectPermissionByUserId(userId, type);
	}

	@Override
	public List<Permission> getPermissionByRoleId(Long roleId, String type) {
		AssertExt.hasId(roleId,"无效roleId");

		val queryWrapper = newQueryWrapper(RolePermission.class).eq("role_id", roleId);

		List<Long> permissionIds = this.rolePermissionMapper.selectList(queryWrapper)
				.stream()
				.map(RolePermission::getPermissionId)
				.distinct()
				.collect(Collectors.toList());

		if (permissionIds.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		val permissionQueryWrapper = newQueryWrapper(Permission.class).in("id", permissionIds);
		if(StringUtils.isNotBlank(type)){
			permissionQueryWrapper.like("type", type);
		}
		return this.permissionMapper.selectList(permissionQueryWrapper);
	}

	@Override
	public int addRole(Role role, Long loginUserId) {
		AssertExt.notBlank(role.getName(), "角色为空");
		List<Role> list = this.roleMapper.selectList(newQueryWrapper(Role.class).eq("name", role.getName()));

		AssertExt.isTrue(list.isEmpty(), "角色已存在");
		role.setCreateTime(LocalDateTime.now());
		this.roleMapper.insert(role);
		return 1;
	}


	@Override
	public int updateRole(Role role, Long loginUserId) {
		AssertExt.notBlank(role.getName(), "角色为空");
//		List<Role> list = this.roleMapper.selectList(newQueryWrapper(Role.class).eq("name", role.getName()));

//		AssertExt.isTrue(list.isEmpty(), "角色已存在");

		UpdateWrapper<Role> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", role.getId());
		updateWrapper.set("name", role.getName());
		updateWrapper.set("remark", role.getRemark());
		updateWrapper.set("update_time", LocalDateTime.now());
		this.roleMapper.update(role, updateWrapper);

		return 1;
	}

	@Override
	public int updateRolePermission(final Long roleId, List<Permission> permissionList, Long loginUserId) {
		AssertExt.hasId(roleId, "无效roleId");

		//获取登陆人的所有角色
		List<Role> roleList = getAllRoles(loginUserId);
		for(Role role: roleList){
			AssertExt.isTrue(role.getId().longValue() != roleId.longValue(), "不能给自己的角色修改权限");
		}


		this.rolePermissionMapper.delete(newQueryWrapper(RolePermission.class).eq("role_id", roleId));
		if(permissionList == null || permissionList.isEmpty()) return 0;

		this.permissionMapper.selectBatchIds(permissionList.stream().map(Permission::getId).collect(Collectors.toList()))
				.forEach(permission -> {
					this.rolePermissionMapper.insert(RolePermission
							.builder()
							.permissionId(permission.getId())
							.roleId(roleId)
							.createTime(LocalDateTime.now()).build()
					);
		});

		return 1;
	}

	@Override
	public int updateRoleStore(Long roleId, List<Long> storeIds, Long loginUserId) {
		AssertExt.hasId(roleId, "无效roleId");

		//获取登陆人的所有角色
		List<Role> roleList = getAllRoles(loginUserId);
		for(Role role: roleList){
			AssertExt.isTrue(role.getId().longValue() != roleId.longValue(), "不能给自己的角色修改门店");
		}

		//干掉原来的关联表
		roleStoreMapper.delete(new QueryWrapper<RoleStore>().eq("role_id", roleId));
		//判断门店是否存在
		List<StoreInfo> storeInfoList = storeInfoMapper.getStoreByIds(storeIds);
		if(storeInfoList == null || storeInfoList.isEmpty()) return 0;

		this.storeInfoMapper.selectBatchIds(storeInfoList.stream().map(StoreInfo::getId).collect(Collectors.toList()))
				.forEach(store -> {
					this.roleStoreMapper.insert(RoleStore
							.builder()
							.storeId(store.getId())
							.roleId(roleId)
							.updateTime(LocalDateTime.now())
							.createTime(LocalDateTime.now()).build()
					);
				});

		return 1;
	}

	@Override
	public IPage<StoreInfo> getRoleStoreList(Long roleId, Integer currentPage, Integer pageSize) {

		if(currentPage == null)currentPage = 1;
		if(pageSize == null)pageSize = 10;

		//把角色id拿出
		List<Long> rolesdIds = new ArrayList<>();
		rolesdIds.add(roleId);

		IPage<StoreInfo> page = new Page<StoreInfo>(currentPage, pageSize);
		IPage<StoreInfo> storeInfoVoPage = storeInfoMapper.getStoreList(page, null, null, null, null, null, null, rolesdIds);

		return storeInfoVoPage;
	}

	@Override
    public IPage<StoreInfo> getStoreList(Integer adcode, Integer currentPage, Integer pageSize, Integer storeTypeId) {
		if(currentPage == null)currentPage = 1;
		if(pageSize == null)pageSize = 10;

		IPage<StoreInfo> page = new Page<StoreInfo>(currentPage, pageSize);
		IPage<StoreInfo> storeInfoVoPage = storeInfoMapper.getStoreList3(page, adcode, storeTypeId);
		return storeInfoVoPage;
    }

	@Override
	public IPage<StoreInfo> getStoreList2(Long loginUserId) {
		return storeInfoService.getStoreList(null, null, 1, 10, null, null, null, null, loginUserId);
	}

	@Override
	public int addUser(UserVo user, Long loginUserId) {
		AssertExt.notBlank(user.getName(), "no name");
		AssertExt.notBlank(user.getUserName(), "no userName");
		AssertExt.notBlank(user.getAccountPassword(), "no account password");

		List<User> list = this.userMapper.selectList(newQueryWrapper(User.class).eq("user_name", user.getUserName()));
		AssertExt.isTrue(list.isEmpty(), "user_name[%s] exists", user.getUserName());

		AccountVo accountVo = new AccountVo();
		accountVo.setAccount(user.getUserName());
		accountVo.setPassword(user.getAccountPassword());
		accountVo.setPasswordConfirm(user.getAccountPassword());
		Account account = loginService.registry(accountVo);

		LocalDateTime now = LocalDateTime.now();
		user.setAccountId(account.getId());
		user.setCreateTime(now);
		user.setUpdateTime(now);
		user.setStatus("active");
		this.userMapper.insert(user);

		return 1;
	}


	@Override
	public User getUserByAccount(Long accountId) {
		return this.userMapper.selectOne(newQueryWrapper(User.class).eq("account_id", accountId));
	}

	@Override
	public int updateUserRole(final Long userId, List<Role> roleList, Long loginUserId) {

		AssertExt.hasId(userId, "无效userId");

		AssertExt.isTrue(loginUserId.longValue() != userId.longValue(), "不能给自己修改角色");

		this.userRoleMapper.delete(newQueryWrapper(UserRole.class).eq("user_id", userId));

		if (roleList == null || roleList.isEmpty()) return 1 ;

		this.roleMapper.selectBatchIds(roleList.stream().map(Role::getId).collect(Collectors.toList()))
				.forEach(role -> {
					this.userRoleMapper.insert(UserRole
							.builder()
							.roleId(role.getId())
							.userId(userId)
							.createTime(LocalDateTime.now()).build()
					);
				});

		return 1;
	}



	@Override
	public IPage<User> getUserList(String telephone, String name, Integer currentPage, Integer pageSize) {

		if(com.alibaba.dubbo.common.utils.StringUtils.isBlank(telephone))telephone = null;
		if(com.alibaba.dubbo.common.utils.StringUtils.isBlank(name) )name = null;
		if(currentPage == null)currentPage = 1;
		if(pageSize == null)pageSize = 10;

		IPage<User> page = new Page<>(currentPage, pageSize);
		return userMapper.getUserList(page, telephone, name, currentPage, pageSize);
	}



	@Override
	public int updateUser(Long userId, String telephone, String email, String name, String remark, String status,
						  String accountPassword, String userName, Long accountId, Long loginUserId) {

		User user = new User();

		LocalDateTime now = LocalDateTime.now();
		UpdateWrapper<User> updateWrapper = new UpdateWrapper();
		updateWrapper.eq("id", userId);
		updateWrapper.set("telphone", telephone);
		updateWrapper.set("email", email);
		updateWrapper.set("name", name);
		updateWrapper.set("remark", remark);
		updateWrapper.set("update_time", now);
		updateWrapper.set("status", status);
		updateWrapper.set("user_name", userName);
		userMapper.update(user, updateWrapper);


		UpdateWrapper<Account> updateWrapper1 = new UpdateWrapper();
		updateWrapper1.eq("id", accountId);
		updateWrapper1.set("account", userName);
		String salt = RandomStringUtils.random(10,true,true);
		updateWrapper1.set("salt", salt);
		updateWrapper1.set("password", DigestUtils.sha1Hex(accountPassword + salt));
		updateWrapper1.set("update_time", now);
		accountMapper.update(new Account(), updateWrapper1);

		return 1;
	}

	@Override
	public Permission addPermission(PermissionPo permission, User user) {
		Permission permission2 = new Permission();

		permission2.setName(permission.getName());
		permission2.setType(permission.getType());
		permission2.setParentId(permission.getParentId());

		Permission permission1 = permissionService.addPermission(user, permission2);
		Long roleId = permission.getRoleId();
		if(roleId == null){
			roleId = 2L;
		}

		//获得权限id
		LocalDateTime time = LocalDateTime.now();
		Long permission1Id = permission1.getId();

		RolePermission r = new RolePermission();
		r.setPermissionId(permission1Id);
		r.setRoleId(roleId);
		r.setCreateTime(time);
		rolePermissionMapper.insert(r);
		return permission1;
	}
}
