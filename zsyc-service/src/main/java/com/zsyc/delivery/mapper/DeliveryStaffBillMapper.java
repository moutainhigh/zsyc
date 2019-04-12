package com.zsyc.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.delivery.entity.DeliveryStaff;
import com.zsyc.delivery.entity.DeliveryStaffBill;
import com.zsyc.delivery.po.DeliverStaffBillBo;
import com.zsyc.delivery.po.DeliverStaffBillCo;
import com.zsyc.store.entity.StoreDeliveryRelation;

import java.util.List;

/**
 * <p>
 * 店铺详情表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-14
 */
public interface DeliveryStaffBillMapper extends BaseMapper<DeliveryStaffBill> {

    /**
     * 生成配送员账单
     * @param deliveryStaffBills
     * @return
     */
    int insertList(List<DeliveryStaffBill> deliveryStaffBills);

    IPage<DeliverStaffBillBo> deliveryStaffBill(IPage<DeliverStaffBillBo> page,String masterName, String phone, String billStatus, String turnInStatus,Long storeId);


    List<DeliverStaffBillCo>selectDeliverStaffBillAll(Long deliveryStaffs1Id);

    Integer updateDeliverAll(String newMasterName,String phone,String papers,Integer isLeader,Long id);

    List<StoreDeliveryRelation> stroedeliverId(Long masterId);


}
