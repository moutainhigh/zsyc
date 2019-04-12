package com.zsyc.warehouse.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.order.entity.OrderInfo;
import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.warehouse.entity.WarehousePackOrder;
import com.zsyc.warehouse.entity.WarehousePackOrderGoods;
import com.zsyc.warehouse.po.*;

import java.time.LocalDateTime;
import java.util.List;

public interface WarehousePackOrdersGoodsService {

    List<WarehouseGoods> selectDoneOrderGoods(Long subId);

    Object returnOrderGoodsDone();

    Integer updateWarehouseStauts(Long warehouseGoodsId);

    List<ReturnMinWareHousePo> packOrderDone(Long staffId);

    Integer distribution(Long subId, Long postmanId);

    Integer distributionDone(Long subId, Long postmanId);

    OrderSubInfo urgeTime(Long subId);

    Integer DoneTime(Long subId);

    List<WarehousePackOrder> warehouse_pack_orderStatus(Long subId);

    ReturnMinWareHousePo checkGoodsinfo(Long wareHouseId);

    Object selectPackOrder(Long staffId);

    List<WarehousePackOrder> selectPackStaffId(Long staffId);

    Object selectStaffIdDone(Long staffId, Long subId);

    ReturnMinWareHousePo staffIdDoneAll(Long staffId, Long subId);

    Integer wareHousePackStatusDone(Long subId, Long warehousePackOrderGoodsId);

    Integer packOrderShortage(Long subId, Long warehousePackOrderGoodsId);

    OrderInfo selectAddress(Long subId);

    Object selectStockPack(StockPackPo stockPackPo);

    Object delStockPack(Integer staffId);

    Integer updateStockPackName(String stockPackName, Long id);

}
