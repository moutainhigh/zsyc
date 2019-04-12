package com.zsyc.warehouse.service;

import com.zsyc.warehouse.BackEndVo.WareHousePackOrderVo;
import com.zsyc.warehouse.entity.WarehousePackOrder;
import com.zsyc.warehouse.entity.WarehousePackOrderGoods;
import com.zsyc.warehouse.po.*;

import java.time.LocalDateTime;
import java.util.List;

public interface WarehousePackOrdersService {

    Object checkGoodsInfo(Long warehouseOrderId);


    Integer packOrderDone(Long subId);

    Integer  packOrder(Long subId,Long staffId);



    Integer packOrderDoneStatus(Long subId);

    Integer updatePackOrderGoodsStatus(Long warehouseOrderPackGoodsId,String status,Long userId);

    Object selectOrderPackOrderId(String orderNo);
    Object selectStaffId(Long staffId,Integer currentPage,Integer pageSize);
    Object selectPackOrderGoodsDoneTime(String startTime, String  endTime,Integer currentPage,Integer pageSize);
    Object selectPackOrderGoodsDoneStatus(String status,Integer currentPage,Integer pageSize);
    Object selectPack(PackPo packPo);
    Integer updatePackOrderStatus(Long packId,String status,Long userId);

    Integer updateStockPackName(StockPackPo stockPackPo);
   Integer updateWarehousePackOrderRemark(Long subId,String remark);
    Integer updateWarehousePackOrderShortage(Long subId);


}
