package com.zsyc.warehouse.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReturnMinWareHouseVo extends BaseBean {
    private Long id;
    private LocalDateTime createTime;
    private ReturnMinWareHousePo list;
}
