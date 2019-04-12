package com.zsyc.aftersale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.aftersale.entity.AfterSaleOrder;
import com.zsyc.aftersale.entity.AfterSaleOrderGoods;
import com.zsyc.order.entity.OrderSubInfo;

import java.util.List;

/**
 * <p>
 * 售后单主表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
public interface AfterSaleOrderMapper extends BaseMapper<AfterSaleOrder> {
  Integer  warehouseOrderPARTIAL(Long warehouseOrderPARTIAL);

  List<AfterSaleOrder> selectAfterSaleOrderSubId();
  List<OrderSubInfo> selectAfterSaleOrderAll(Long subId);
  Integer afterUpdateOrderSubStauts(Long subId);
  
  List<AfterSaleOrderGoods>selectAfterSaleOrderGoods(Long afterSaleOrderGoodsId);
}
