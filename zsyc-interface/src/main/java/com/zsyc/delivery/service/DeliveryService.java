package com.zsyc.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.delivery.entity.DeliveryStaff;
import com.zsyc.delivery.entity.DeliveryStaffBill;
import com.zsyc.delivery.po.DeliVeryStaffAo;
import com.zsyc.delivery.vo.DeliveryStaffVo;

import java.util.List;

public interface DeliveryService {



    /**
     * 配送员详情
     * @param deliveryId
     * @return
     */
    DeliveryStaff getDeliveryById(Long deliveryId);

    /**
     * 门店配送员列表
     * @param storeId
     * @return
     */
    IPage<DeliveryStaff> getDeliveryList(Long storeId, Integer currentPage, String masterType, Integer pageSize, String masterName, String phone);




    Object selectDeliVeryStaff(String masterName,String phone,String masterType,Integer currentPage,Integer pageSize);

    Integer setIsDel(Long id);

    Integer updateMasterName(String  newMasterName, Long id);

    Integer updateMasterPhone(String phone,Long id);

    Integer  updatePapersPapers(String papers, Long id);

    Integer updateIsLeader(int isLeader,Long id);

    Object deliveryStaffBill(String masterName,String phone,String billStatus,String turnInStatus,Integer currentPage,Integer pageSize,Long storeId);

    Integer updateDeliverAll(DeliVeryStaffAo deliVeryStaffAo);


}
