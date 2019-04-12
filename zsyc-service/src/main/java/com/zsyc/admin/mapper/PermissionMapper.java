package com.zsyc.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.admin.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-14
 */
public interface PermissionMapper extends BaseMapper<Permission> {

	List<Permission> selectPermissionByUserId(@Param("userId")Long userId, @Param("type") String type);
}
