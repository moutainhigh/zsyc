package com.zsyc.aftersale.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PayServicePo extends BaseBean {
    private Long id;
    private LocalDateTime localDateTime;
    private String orderNo;
    private Long[] afterSaleOrderGoodsId;
}
