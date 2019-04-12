package com.zsyc.warehouse.BackEndVo;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WareHouseOrderPo extends BaseBean {
    private String warehouseOrderNo;
    private LocalDateTime wareHouseCreateTime;
    private List<WareHouseOrderVo> wareHouseOrderVo;
    private Long wareHouseOrderid;
    private String status;
    private Long id;
    private String staffName;
    private String staffPhone;
    private String remark;
    private LocalDateTime createTime;
}
