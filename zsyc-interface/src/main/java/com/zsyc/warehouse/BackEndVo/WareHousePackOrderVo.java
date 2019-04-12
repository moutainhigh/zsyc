package com.zsyc.warehouse.BackEndVo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WareHousePackOrderVo extends WareHouseOrderPo {
    private String orderNo;
    private LocalDateTime createTime;
    private String consigneePhone;
    private String consignee;
    private String status;
    private List<WareHousePackOrderPo> list;
    private LocalDateTime doneTime;

}
