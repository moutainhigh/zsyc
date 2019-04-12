package com.zsyc.delivery.po;

import com.zsyc.delivery.entity.DeliveryStaffBill;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DeliverStaffBillBo extends BaseBean {
    /**
     * 主键
     */
    private Long id;

    /**
     * 配送员姓名
     */
    private String masterName;

    /**
     * 账号id
     */
    private Long accountId;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 证件图片，身份证正反面，从业资格证
     */
    private String papers;

    /**
     * 父类id
     */
    private Long parentId;

    /**
     * 是否为负责人
     */
    private Integer isLeader;

    /**
     * 配送员类型 STORE-店铺配送，PLATFORM-平台配送
     */
    private String masterType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 是否删除
     */
    private Integer isDel;

    List<DeliverStaffBillCo> deliveryStaffBillList;

    private String storeName;

}
