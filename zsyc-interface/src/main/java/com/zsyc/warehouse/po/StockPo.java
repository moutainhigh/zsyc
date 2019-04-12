package com.zsyc.warehouse.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockPo  extends BaseBean {
    //备货单号
    private String warehouseOrderNo;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //门店id
    private Long storeId;
    //状态
    private String status;
    //备货员id
    private Long staffId;
    private Long id;
    private LocalDateTime createTime;

    //页数
    private Integer currentPage;
    //码数
    private Integer pageSize;
    private LocalDateTime[] time;



}
