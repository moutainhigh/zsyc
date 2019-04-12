package com.zsyc.warehouse.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.goods.entity.GoodsInfo;
import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.warehouse.BackEndVo.WareHousePackOrderPo;
import com.zsyc.warehouse.BackEndVo.WareHousePackOrderVo;
import com.zsyc.warehouse.entity.WarehousePackOrder;
import com.zsyc.warehouse.entity.WarehousePackOrderGoods;
import com.zsyc.warehouse.entity.WarehouseStaff;
import com.zsyc.warehouse.po.BackendWarehousePackOrder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
public interface WarehousePackOrderMapper extends BaseMapper<WarehousePackOrder> {
  List<WarehousePackOrder>selectWareHouseWareHouseId(Long wareId);
  WarehousePackOrder selectPackOrderOrderId(Long subId);
  GoodsInfo selectSkuGoodsInfo(String sku);
  Integer  packOrder(Long subId,Long staffId);
  Integer packOrderDone(Long subId);
  Integer packOrderDoneStatus(Long subId);
  List<WarehousePackOrder> checkWareHousePackOne(Long subId);
  List<WarehousePackOrderGoods> checkWareHousePackTwo(Long subId);
  WarehousePackOrder selectWarehouseAll(Long subId);
  List<WarehousePackOrder>selectWarehousePackPendingSubId();
  List<BackendWarehousePackOrder> selectBackendWarehousePackOrder(Long subId);
  Integer updatePackOrderGoodsStatus(Long warehouseOrderPackGoodsId,String status,Long userId);
  OrderSubInfo selectBackendWarehousePackOrderPoo(Long subId);
  Integer updatePackOrderStatus(Long packId,String status,Long userId);
  List<WarehousePackOrder> selectWarehousePackPendingSubIdDone();
  WarehouseStaff selectStaffName(Long staffId);
  Integer updateOrderSubStatusPack(Long subId);
  List<WareHousePackOrderVo>selectOrderPackOrderId(String orderNo);
  List<WareHousePackOrderPo>WareHousePackOrderPo(Long id);
  IPage<WarehousePackOrder>selectStaffId(IPage<WarehousePackOrder> page,Long staffId);
  OrderSubInfo selectOrderSubId(Long subId);
  List<WareHousePackOrderPo>staffIdOrderId(Long packId);
  Integer updateWarehousePackOrderShortage(Long subId);
  IPage<WarehousePackOrder> selectPackOrderGoodsDoneTime(IPage<WarehousePackOrder> page,LocalDateTime startTime,LocalDateTime endTime);
  IPage<WarehousePackOrder> selectPackOrderGoodsDoneStatus( IPage<WarehousePackOrder> page,String status);
  Integer updateWarehousePackOrderRemark(String remark,Long subId);
  IPage<WarehousePackOrder>selectPack(IPage<WarehousePackOrder>page,String orderNo,Long staffId,Long storeId,LocalDateTime startTime,LocalDateTime endTime,String status);
}

