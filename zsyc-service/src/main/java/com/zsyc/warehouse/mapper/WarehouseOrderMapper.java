package com.zsyc.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.goods.entity.GoodsInfo;
import com.zsyc.goods.entity.GoodsStorePrice;
import com.zsyc.order.entity.OrderGoods;
import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.warehouse.BackEndVo.WareHouseOrderVo;
import com.zsyc.warehouse.entity.WarehouseOrder;
import com.zsyc.warehouse.entity.WarehouseOrderGoods;
import com.zsyc.warehouse.entity.WarehouseStaff;
import com.zsyc.warehouse.po.*;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 备货主表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
public interface WarehouseOrderMapper extends BaseMapper<WarehouseOrder> {
     GoodsStorePrice selectPriceSku(String sku, Long storeId);
    List<WarehouseOrder> selectWareHouseAll(Long storeId, Long wareHouseId);

    WarehouseStaff selectStaffName(Long staffId);

    List<OrderGoods> selectOrdergoods(Long subId);

    List<OrderSubInfo> selectOrdersubinfo(Long storeId);

    Integer updateBeenStock(Long subId);

    List<WarehousePo> selectOrderGoods(Long storeId);

    Integer updateWareHouseStatus(Long warehouseOrderId, Long wareHouseId, LocalDateTime receiveTime);

    //更新状态接口
    Integer updateOrderSubStauts(String stauts, Integer id);

    List<OrderSubInfo> ordersubID();

    List<WarehousePo> selectWarehouse();

    //查找Ready状态的备货单
    List<WarehouseOrderGoods> selectWareHouseOrderGoods(Long storeId);

    List<SortingOrderPo> miniWareHouse(Long checkId);

    List<BackendWarehouseOrder> selectReadyWareHouse();

    List<BackendWarehouseOrder> selectReadyWareHouseTrue();

    List<BackendStockWarehouse> selectStockWareHouse(Long storeId);

    //根据店铺ID和预约提前时间获取订单字表
    List<OrderSubInfo> selectOrderGoodsPresetTime(@Param("storeId") Long storeId, @Param("minute") Integer minute);

    //根据订单字表ID查询对应的商品快照信息
    List<OrderGoods> selectOrderGoodsByOrderId(Long orderSubId);


    WarehouseOrder wareHouseOrderStatus(Long warehouseOrderId);

    List<BackendWarehouseOrder> selectAttrKey(String sku);

    List<BackendWarehouseOrder> selectReadyWareHouseDone();


    GoodsInfo selectSKuZu(String sku);

    List<WareHouseOrderVo> warehouseOrderGoodsOrderNo(String warehouseOrderNo);

    IPage<WarehouseOrder> warehouseOrderGoodsCreateTime(IPage<WarehouseOrder> page, LocalDateTime startTime, LocalDateTime endTime);

    List<WareHouseOrderVo> warehouseOrderGoodsCreateTimeAll(Long id);


    List<StoreInfo> selectStoreInfo();


    IPage<WarehouseOrder> selectStock(IPage<WarehouseOrder> page, Long storeId, String status, Long staffId, LocalDateTime startTime, LocalDateTime endTime, String warehouseOrderNo);


    Integer updateWareHouseOrderStatusAll(Long wareHouseOrderid, String status,Long userId);

    IPage<WarehouseOrder> selectStoreId(IPage<WarehouseOrder> page, Long storeId);

    IPage<WarehouseOrder> warehouseOrderGoodsStatus(IPage<WarehouseOrder> page, String status);

    IPage<WarehouseOrder> selectWareHouseOrderStaffId(IPage<WarehouseOrder> page, Long staffId);



}
