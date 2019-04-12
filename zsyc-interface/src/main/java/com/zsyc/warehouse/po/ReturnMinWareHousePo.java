package com.zsyc.warehouse.po;

import com.zsyc.framework.base.BaseBean;
import com.zsyc.order.entity.OrderGoods;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ReturnMinWareHousePo extends BaseBean {
    private  Long id;
    private String warehouseOrderNo;
    private LocalDateTime createTime;
    private Long WarehousePackOrderID;
    private String  address;
    private Long addressId;
    private String remark;
    private List<SortingOrderPo> list = new ArrayList<>();


}
