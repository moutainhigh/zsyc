package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.admin.entity.Permission;
import com.zsyc.admin.entity.Role;
import com.zsyc.admin.entity.User;
import com.zsyc.admin.po.PermissionPo;
import com.zsyc.admin.po.RoleStorePo;
import com.zsyc.admin.po.UserPo;
import com.zsyc.admin.po.UserRolePo;
import com.zsyc.admin.service.AdminService;
import com.zsyc.admin.service.PermissionService;
import com.zsyc.admin.vo.UserVo;
import com.zsyc.api.AccountHelper;
import com.zsyc.common.AssertExt;
import com.zsyc.store.entity.StoreInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lcs on 2019-01-19.
 */
@RestController
@RequestMapping("api/admin")
@Api
public class AdminController  {

	@Reference
	private AdminService adminService;
	@Reference
	private PermissionService permissionService;
	@Autowired
	private AccountHelper accountHelper;

	@ApiOperation("添加用户---后台")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "用户名称", required = true, dataType = "string"),
			@ApiImplicitParam(name = "userName", value = "登录帐号", required = true, dataType = "string"),
			@ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "string"),
			@ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "string"),
			@ApiImplicitParam(name = "accountPassword", value = "密码", required = true, dataType = "string")
	})
	@PostMapping("addUser")
	public int addUser(@RequestBody UserVo userVo) {
		return this.adminService.addUser(userVo, accountHelper.getUser().getId());
	}


	@ApiOperation("获取后台菜单")
	@GetMapping("permissions")
	public List<Permission> permissions() {
		return this.adminService.getPermissionByUserId(accountHelper.getUser().getId(), null);
	}

	@ApiOperation("获取一角色菜单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleId", value = "角色Id", required = true)
	})
	@GetMapping("getRolePermission")
	public List<Permission> getRolePermission(Long roleId) {
		return this.adminService.getPermissionByRoleId(roleId, null);
	}

	@ApiOperation("添加角色")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "角色名称",required = true),
			@ApiImplicitParam(name = "remark", value = "备注")
	})
	@PostMapping("addRole")
	public int addRole(@RequestBody Role role) {
		return this.adminService.addRole(role, accountHelper.getUser().getId());
	}

	@ApiOperation("修改角色")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "角色id",required = true),
			@ApiImplicitParam(name = "name", value = "角色名称",required = true),
			@ApiImplicitParam(name = "remark", value = "备注")
	})
	@PostMapping("updateRole")
	public int updateRole(@RequestBody Role role) {
		return this.adminService.updateRole(role,  accountHelper.getUser().getId());
	}

	@ApiOperation("获取所有角色")
	@GetMapping("getAllRoles")
	public List<Role> getAllRoles() {
		return this.adminService.getAllRoles(null);
	}

	@ApiOperation("获取已绑定的角色")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "用户id",required = true),
	})
	@GetMapping("getLoginRoles")
	public List<Role> getLoginRoles(Long id) {
		return adminService.getLoginRoles(id);
	}

	@ApiOperation("删除角色")
	@GetMapping("deleteRole")
	public int deleteRole(Long id){
		return adminService.deleteRole(id, accountHelper.getUser().getId());
	}

	@ApiOperation("更新一个角色的权限, 但不能给自己的角色改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleId", value = "角色ID", required = true, type = "Long"),
			@ApiImplicitParam(name = "permissionIds", value = "权限ID数组", required = true, type = "Long[]")
	})
	@PostMapping("updateRolePermission")
	public Object updateRolePermission(@RequestBody UserRolePo userRolePo) {
		return adminService.updateRolePermission(userRolePo.getRoleId(), userRolePo.getPermissionIds() == null ? null : userRolePo.getPermissionIds().stream()
				.map(id -> Permission.builder().id(id).build()).collect(Collectors.toList()),  accountHelper.getUser().getId());
	}

	@ApiOperation("更新一个角色的门店")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleId", value = "角色ID", required = true, type = "Long"),
			@ApiImplicitParam(name = "storeIds", value = "门店ID数组", required = true, type = "Long[]")
	})
	@PostMapping("updateRoleStore")
	public Object updateRoleStore(@RequestBody UserRolePo userRolePo) {
		return adminService.updateRoleStore(userRolePo.getRoleId(), userRolePo.getStoreIds(),  accountHelper.getUser().getId());
	}

	@ApiOperation("获取一个角色的门店")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleId", value = "角色ID", required = true, type = "Long"),
			@ApiImplicitParam(name = "currentPage", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(name = "pageSize", value = "页数", required = true, dataType = "int"),
	})
	@GetMapping("getRoleStore")
	public IPage<StoreInfo> getRoleStore(Long roleId, Integer currentPage, Integer pageSize) {
		return adminService.getRoleStoreList(roleId, currentPage, pageSize);
	}

    @ApiOperation("获取所有门店")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeTypeId", value = "门店类型", required = true, dataType = "int"),
            @ApiImplicitParam(name = "adcode", value = "最后一级地区编码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "currentPage", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页数", required = true, dataType = "int"),
    })
	@GetMapping("getRoleStoreList")
    public IPage<StoreInfo> getRoleStoreList(RoleStorePo roleStorePo){
	    return adminService.getStoreList(roleStorePo.getAdcode(), roleStorePo.getCurrentPage(), roleStorePo.getPageSize(), roleStorePo.getStoreTypeId());
    }

	@ApiOperation("获取一个用户绑定的门店")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户Id", required = true, type = "Long"),
	})
	@GetMapping("getRoleStoreList2")
	public IPage<StoreInfo> getRoleStoreList2(Long userId){
		return adminService.getStoreList2(userId);
	}

	@ApiOperation("更新一个用户的角色，不能给自己改角色")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户ID", required = true, type = "Long"),
			@ApiImplicitParam(name = "roleIds", value = "角色ID数组", required = true, type = "Long[]")
	})
	@PostMapping("updateUserRole")
	public Object updateUserRole(@RequestBody UserRolePo userRolePo) {
		return adminService.updateUserRole(userRolePo.getUserId(), userRolePo.getRoleIds() == null ? null : userRolePo.getRoleIds().stream()
				.map(id -> Role.builder().id(id).build()).collect(Collectors.toList()), this.accountHelper.getUser().getId());
	}

	@ApiOperation("后台用户信息列表---后台")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "telphone", value = "手机", required = true, dataType = "string"),
			@ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "string"),
			@ApiImplicitParam(name = "currentPage", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(name = "pageSize", value = "页数", required = true, dataType = "int"),
	})
	@PostMapping(value = "getUserList",produces = MediaType.APPLICATION_JSON_VALUE)
	public IPage<User> getUserList(@RequestBody UserPo userPo){
		return adminService.getUserList(userPo.getTelphone(), userPo.getName(), userPo.getCurrentPage(), userPo.getPageSize());
	}


	@ApiOperation("后台用户信息修改---后台")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "accountPassword", value = "密码", required = true, dataType = "string"),
			@ApiImplicitParam(name = "userName", value = "登录帐号", required = true, dataType = "string"),
			@ApiImplicitParam(name = "accountId", value = "账号id", required = true, dataType = "long"),
			@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "long"),
			@ApiImplicitParam(name = "telphone", value = "电话", required = true, dataType = "string"),
			@ApiImplicitParam(name = "email", value = "邮件", required = true, dataType = "string"),
			@ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "string"),
			@ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "string"),
			@ApiImplicitParam(name = "status", value = "状态('active':正常,'forbidden'：禁用,'lock'：锁定)", required = true, dataType = "int"),
	})
	@PostMapping(value = "updateUser",produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateUser(@RequestBody UserPo userPo){
		return adminService.updateUser(userPo.getUserId(), userPo.getTelphone(), userPo.getEmail(), userPo.getName(), userPo.getRemark(),
				userPo.getStatus(), userPo.getAccountPassword(), userPo.getUserName(), userPo.getAccountId(), accountHelper.getUser().getId());
	}

	@ApiOperation("添加一个权限")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "权限名", required = true, dataType = "string"),
			@ApiImplicitParam(name = "type", value = "权限类型（button，menu）", required = true, dataType = "string"),
			@ApiImplicitParam(name = "parentId", value = "父级id", required = true, dataType = "long"),
			@ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "long")

	})
	@PostMapping("addPermission")
	public Permission addPermission(@RequestBody PermissionPo permission){
		User user = new User();
		user.setId(5l);
		return adminService.addPermission(permission, user);
	}
}
