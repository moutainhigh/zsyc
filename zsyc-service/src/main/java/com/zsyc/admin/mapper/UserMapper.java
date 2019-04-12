package com.zsyc.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.admin.entity.User;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-14
 */
public interface UserMapper extends BaseMapper<User> {


    IPage<User> getUserList(IPage<User> iPage, String telephone, String name, Integer currentPage, Integer pageSize);
}
