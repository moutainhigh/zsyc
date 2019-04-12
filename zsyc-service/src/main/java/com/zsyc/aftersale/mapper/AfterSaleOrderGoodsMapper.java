package com.zsyc.aftersale.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;


import com.zsyc.aftersale.entity.AfterSaleOrder;
import com.zsyc.aftersale.entity.AfterSaleOrderGoods;
import com.zsyc.aftersale.po.AterSaleOrderVo;
import com.zsyc.aftersale.po.BackendAfterSaleOrderPo;
import com.zsyc.goods.entity.GoodsStorePrice;
import com.zsyc.order.entity.OrderGoods;
import com.zsyc.order.entity.OrderSubInfo;

import java.util.List;

/**
 * <p>
 * 售后订单商品表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
public interface AfterSaleOrderGoodsMapper extends BaseMapper<AfterSaleOrderGoods> {
    OrderGoods selectGoodsPrice(Long ordersubId, String sku);
    GoodsStorePrice selectGoodsStoreIdPrice(Long storeId, String sku);
    Integer updateOrderSub(Long ordersubId);
    List<BackendAfterSaleOrderPo>checkKuaiJie(Long subId,String parentSku,String afterSaleNo);
    Integer updateOrderGoods(Long ordersubId);

    Integer updateAfterStatus(Long afterId, String status);

    List<AterSaleOrderVo> selectAfterSaleOrderGoods(Long subId);

    Integer updateSubOrderemark(String backendRemark, Long subId);

    List<BackendAfterSaleOrderPo> selectAfterSaleOrderOrderNo(String afterSaleNo);

    List<BackendAfterSaleOrderPo> selectAfterSaleOrderSubId(Long subId);

    List<AfterSaleOrder> selectAfterSaleStaffName(String staffName);

    List<AfterSaleOrder> selectAfterSalePhone(String phone);

    IPage<AfterSaleOrder> selectAfterSaleAll(IPage<AfterSaleOrder> page, String afterSaleNo, Long subId, Long staffName, String Phone);
    OrderSubInfo selectOrderNoSubId(Long subId);

    List<BackendAfterSaleOrderPo>checkParentSku(Long subId,String afterSaleNo);

    Integer updateAfterSaleOrderRemark(Long id,String remark);
    Integer  doneService(Long id);
    Integer subProcessed(Long subId);
    AfterSaleOrder selectSaleOrder(Long id);
}
