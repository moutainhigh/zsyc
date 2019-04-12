package com.zsyc.aftersale.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AfterSaleOrderAccept extends BaseBean {
    private Long id;
    private LocalDateTime createTime;
    /**
     * 分拣订单id
     */
    private Long warehousePackOrderId;

    /**
     * 子订单id
     */
    private Long orderSubId;


    /**
     * 售后单号
     */
    private String afterSaleNo;

    /**
     * 配货员电话
     */
    private String staffPhone;

    /**
     * 配货员名称
     */
    private String staffName;

    /**
     * 客户地址id
     */
    private Long memberAddressId;
    private String remark;

    List<AfterSaleOrderChildren> list;
}
