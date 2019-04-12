package com.zsyc.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.warehouse.entity.WarehouseStaff;

import java.util.List;

public interface WarehouseStaffMapper extends BaseMapper<WarehouseStaff> {

    /**
     * 查询门店的备货员
     * @param storeId
     * @return
     */
    IPage<WarehouseStaff> getWarehouseStaff(IPage<WarehouseStaff> page, Long storeId, String name, String phone, String stockType);

    /**
     * 删除
     * @param ids
     * @return
     */
    int deleteWarehouseStaff(List<Long> ids, Long loginUserId);




    /**
     * 查询配送员
     * @param page
     * @param stockPackName
     * @param phone
     * @param stockType
     * @param IdNumber
     * @return
     */
    IPage<WarehouseStaff>selectStockPack(IPage<WarehouseStaff>page,String stockPackName,String phone ,String stockType,String IdNumber,Long storeId);

    /**
     * 更改备货分拣员信息
     * @param stockPackName
     * @param phone
     * @param IdNumber
     * @param idCardImg
     * @param id
     * @return
     */
    Integer updateStockPackName(String stockPackName,String phone,String IdNumber,String idCardImg,Long id);
}
