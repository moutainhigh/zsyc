package com.zsyc.warehouse.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class NewWarehouse extends BaseBean {
    private Long id;



    private LocalDateTime createTime;

    private List<ReturnMinWareHousePo> returnMinWareHousePo = new ArrayList<>();
}
