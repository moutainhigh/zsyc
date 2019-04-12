package com.zsyc.aftersale.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AfterSalePo extends BaseBean {
    private Long id;
    private LocalDateTime createTime;
    private String afterSaleNo;
    private  Long subId;
    private  Long staffName;
    private String Phone;

    private Integer currentPage;
    private Integer pageSize;

}
