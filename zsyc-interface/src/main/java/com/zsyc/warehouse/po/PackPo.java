package com.zsyc.warehouse.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PackPo extends BaseBean {
    private Long id;
    private LocalDateTime createTime;
    //订单编号
    private  String orderNo;
    //分拣员id
    private Long staffId;
    //门店id
    private Long storeId;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //状态
    private String status;

    private LocalDateTime[] time;
    //页数
    private Integer currentPage;
    //码数
    private Integer pageSize;
}
