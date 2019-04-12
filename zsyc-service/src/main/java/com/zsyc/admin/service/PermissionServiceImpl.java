package com.zsyc.admin.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsyc.admin.entity.Permission;
import com.zsyc.admin.entity.User;
import com.zsyc.admin.mapper.PermissionMapper;
import com.zsyc.common.AssertExt;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lcs on 2019-01-16.
 */
@Service
public class PermissionServiceImpl extends BaseService implements PermissionService {
	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public Permission addPermission( User user, Permission permission) {
		AssertExt.notBlank(permission.getName(), "没有权限名称");
		AssertExt.notBlank(permission.getType(),"没有权限类型");
//		AssertExt.notBlank(permission.getValue(), "没有权限值");
		Permission parent = null;
		if( permission.getParentId()!= null ){
			parent = this.permissionMapper.selectById(permission.getParentId());
			AssertExt.notNull(parent, "parentId[%s]不存在", permission.getParentId());
		}
		permission.setCreateTime(LocalDateTime.now());

		permission.setCreateUserId(user.getId());
		this.permissionMapper.insert(permission);
		if (parent != null) {
			permission.setTreePath(parent.getTreePath() + "-" + permission.getId());
			permission.setTreeLevel(parent.getTreeLevel() + 1);
		}else{
			permission.setTreePath(permission.getId().toString());
			permission.setTreeLevel(0);
		}
		this.permissionMapper.updateById(permission);
		return permission;
	}

	@Override
	public Permission getPermission(Long permissionId) {
		AssertExt.hasId(permissionId, "无效ID");
		return this.permissionMapper.selectById(permissionId);
	}

	@Override
	public Permission getPermission(String value, String type) {
		AssertExt.notBlank(value,"no value");
		AssertExt.notBlank(type,"no type");
		return this.permissionMapper.selectOne(new QueryWrapper<Permission>().eq("value", value).eq("type", type));
	}


	@Override
	public List<Permission> getSubPermission(Long permissionId) {
		AssertExt.hasId(permissionId, "无效ID");
		return this.permissionMapper.selectList(new QueryWrapper<Permission>().eq("parent_id", permissionId));
	}

	@Override
	public int updatePermission(User user,Permission... permissions) {
		AssertExt.notEmpty(permissions, "无效参数");
		AssertExt.isTrue(permissions.length < 1000, "不能更新超过1000个");
		int count = 0;
		for (Permission permission: permissions) {
			AssertExt.hasId(permission.getId(), "没有Id");
			permission.setUpdateTime(LocalDateTime.now());
			permission.setUpdateUserId(user.getId());
			count += this.permissionMapper.updateById(permission);
		}
		return count;
	}

	@Override
	public int deletePermission(Long... permissionIds) {
		AssertExt.notEmpty(permissionIds, "无效参数");
		AssertExt.isTrue(permissionIds.length < 1000, "不能删除超过1000个");
		return this.permissionMapper.deleteBatchIds(Arrays.asList(permissionIds));
	}
}
