package com.zsyc.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.delivery.entity.DeliveryStaff;
import com.zsyc.delivery.po.DeliverStaffBillBo;
import com.zsyc.delivery.vo.DeliveryStaffVo;
import com.zsyc.member.entity.MemberAddress;
import com.zsyc.store.entity.StoreInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 店铺详情表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-14
 */
public interface DeliveryStaffMapper extends BaseMapper<DeliveryStaff> {

    /**
     * 配送员工资结算
     * @param beginTime
     * @param endTime
     * @return
     */
    List<DeliveryStaffVo> deliverySalary(@Param("beginTime") String beginTime,
                                         @Param("endTime")String endTime);

    /**
     * 配送员上缴金额结算
     * @return
     */
    List<DeliveryStaffVo> deliveryTurnIn(@Param("params") Map map);

    /**
     * 门店配送员列表查询
     * @param storeId
     * @return
     */
    IPage<DeliveryStaff> getDeliveryList(IPage<DeliveryStaff> iPage, Long storeId, String masterType, String masterName, String phone);


    /**
     * 删除配送员
     * @param
     * @return
     */
    int deleteDelivery(List<Long> ids, Long loginUserId);


    IPage<DeliverStaffBillBo> selectDeliVeryStaff(IPage<DeliverStaffBillBo> page, String masterName, String phone, String masterType);
    Integer setIsDel(Long id);
    Integer updateMasterName(String  newMasterName, Long id);
    Integer updateMasterPhone(String phone,Long id);
    Integer  updatePapersPapers(String papers, Long id);
    Integer updateIsLeader(int isLeader,Long id);

}
