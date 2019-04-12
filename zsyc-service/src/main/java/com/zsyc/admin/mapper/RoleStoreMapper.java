package com.zsyc.admin.mapper;

import com.zsyc.admin.entity.RoleStore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色门店关联表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-03-28
 */
public interface RoleStoreMapper extends BaseMapper<RoleStore> {

    /**
     * 把一个用户的所有角色绑定一个门店
     * @param rolesdIds
     * @param storeId
     * @return
     */
    int insertList(@Param("list")List<Long> rolesdIds, @Param("storeId")Long storeId);
}
