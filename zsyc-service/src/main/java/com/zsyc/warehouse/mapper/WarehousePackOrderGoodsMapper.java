package com.zsyc.warehouse.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.order.entity.OrderGoods;
import com.zsyc.order.entity.OrderInfo;
import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.warehouse.entity.WarehouseOrder;
import com.zsyc.warehouse.entity.WarehousePackOrder;
import com.zsyc.warehouse.entity.WarehousePackOrderGoods;
import com.zsyc.warehouse.po.SortingOrderPo;
import com.zsyc.warehouse.po.WarehouseGoods;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 备货订单商品中间表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
public interface WarehousePackOrderGoodsMapper extends BaseMapper<WarehousePackOrderGoods> {
    List<WarehousePackOrderGoods>selectPackid(Long packId);
    List<WarehouseOrder> checkOrdersubId(Long warehouseOrderId);
    List<OrderGoods>selectGoods(Long subId);
    List<SortingOrderPo> checkGoodsinfo(Long wareHouseId);
    WarehousePackOrderGoods selectWareHouseOrderGoodsss(Long gooodsId ,Long wareOrderGoodId);

    OrderGoods selectOrderGoodsAll(Long goodsId);
    List<WarehousePackOrder> selectWarehouseDone();

    List<WarehouseGoods> selectDoneOrderGoods(Long subId);

    Integer updateWarehouseStauts(Long warehouseGoodsId);

    Integer distribution(Long subId, Long postmanId);

    Integer distributionDone(Long subId, Long postmanId);

    OrderSubInfo urgeTime(Long subId);

    Integer updateUrgeTime(LocalDateTime urgeTime, Long subId, int priorityNum);

    List<WarehousePackOrder> wareHousePackOrderStatus(Long subId);

    List<WarehousePackOrder> selectPackOrder(Long staffId);

    List<WarehousePackOrder> selectPackStaffId(Long staffId);

    List<WarehouseGoods> packOrderDone(Long staffId);

    List<WarehousePackOrderGoods> selectStaffIdDone(Long staffId, Long subId);

    Integer wareHousePackStatusDone(Long subId,Long warehousePackOrderGoodsId);

    Integer packOrderShortage(Long subId,Long warehousePackOrderGoodsId);

    OrderInfo selectAddress(Long subId);

    OrderSubInfo selectOrderInfoNo(Long subId);

    Integer delStockPack(Integer staffId);


    Integer updateStockPackName(String stockPackName,Long id);

    Integer updateOrderSubStatusPack(Long subId);



}
