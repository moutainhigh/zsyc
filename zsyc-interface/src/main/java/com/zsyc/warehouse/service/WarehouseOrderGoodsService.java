package com.zsyc.warehouse.service;

/**
 * <p>
 * 备货主表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
public interface WarehouseOrderGoodsService {

    Integer updateOrderSubStauts(int id,String stauts);
    Integer wareHouseDone(Long id,String remark);
    Integer updateWarehouseOrderGoods(Long warehouseOrderGoodsId,String status,Long userId);
    Integer updateWarehouseOrderRemark(Long id ,String remark );


}
