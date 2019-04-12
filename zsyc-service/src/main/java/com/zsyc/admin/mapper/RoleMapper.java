package com.zsyc.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.admin.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-14
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * loginUserId不传，就是所有的角色，否则获取loginUserId的所有角色
     * @param loginUserId
     * @return
     */
    List<Role> getRoleList(@Param("loginUserId") Long loginUserId);

    /**
     * 获取登陆账号的角色
     * @param loginUserId
     * @return
     */
    List<Role> getLoginRoles(@Param("loginUserId") Long loginUserId);

}
