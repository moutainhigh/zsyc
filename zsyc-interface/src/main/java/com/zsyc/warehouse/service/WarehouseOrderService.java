package com.zsyc.warehouse.service;

import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.warehouse.entity.WarehouseOrder;
import com.zsyc.warehouse.entity.WarehouseOrderGoods;
import com.zsyc.warehouse.po.*;

import java.util.List;

/**
 * <p>
 * 备货主表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
public interface WarehouseOrderService {

    List<OrderSubInfo> selectOrdersubinfo(Long storeId);

    List<WarehousePo> selectOrderGoods(Long storeId);

    int insert(WarehouseOrder warehouseOrder);

    Object WareHousemap(Long storeIdtest);

    Integer updateWareHouseStatus(Long warehouseOrderId, Long wareHouseId);

    Integer updateOrderSubStauts(String stauts, Integer id);

    List<OrderSubInfo> ordersubID();

    List<SortingOrderPo> miniWareHouse(Long checkId);

    List<WarehousePo> selectWarehouse();

    List<WarehouseOrderGoods> selectWareHouseOrderGoods(Long storeId);

    List<BackendWarehouseOrder> selectReadyWareHouse();

    List<BackendWarehouseOrder> selectReadyWareHouseTrue();

    List<BackendStockWarehouse> selectStockWarehouse(Long subId);

    Object returnStockReady(Long storeId);

    void createWarehouseByStoreId(Long storeId, Integer minute);

    Object returnMinWareHouse(Long storeId);

    Object selectWareHouseAll(Long storeId, Long wareHouseId);

    Object selectAttKeyAll(String sku);

    List<BackendWarehouseOrder> selectReadyWareHouseDone();

    Object warehouseOrderGoodsOrderNo(String warehouseOrderNo);

    Object warehouseOrderGoodsCreateTime(String startTime, String endTime, Integer currentPage, Integer pageSize);

    List<StoreInfo> selectStoreInfo();

    Object selectStock(StockPo stockPo);

    Object warehouseOrderGoodsStoreIdAll(Long storeId, Integer currentPage, Integer pageSize);

    Object warehouseOrderGoodsStatus(String status, Integer currentPage, Integer pageSize);

    Object selectWareHouseOrderStaffId(Long staffId, Integer currentPage, Integer pageSize);

    Integer updateWareHouseOrderStatusAll(Long wareHouseOrderid, String status,Long userId);



}
