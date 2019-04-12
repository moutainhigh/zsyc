package com.zsyc.warehouse.po;

import com.zsyc.framework.base.BaseBean;
import io.swagger.models.auth.In;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockPackPo extends BaseBean {
    private  Long id;
    private LocalDateTime createTime;
    private String stockPackName;
    private String phone;
    private String stockType;
    private String IdNumber;
    private Integer currentPage =1;
    private Integer pageSize =10;
    private String idCardImg;
    private Long  storeId;




}
