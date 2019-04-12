package com.zsyc.warehouse.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.warehouse.entity.WarehouseOrderGoods;

import java.util.List;

/**
 * <p>
 * 备货子表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
public interface WarehouseOrderGoodsMapper extends BaseMapper<WarehouseOrderGoods> {

    Integer updateOrderSubStauts(int id,String stauts);

    Integer wareHouseDone(Long id);
    Integer updateWareHousePackOrder(Long id);
    Integer updateWarehouseOrderGoods(Long warehouseOrderGoodsId,String status,Long userId);
    List<WarehouseOrderGoods> selectWareHouseOrderGoods(Long wareOrderId);

    Integer updateWarehouseOrderRemark(Long id ,String remark );
}
