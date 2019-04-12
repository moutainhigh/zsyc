package com.zsyc.warehouse.po;

import com.zsyc.warehouse.entity.WarehouseOrderGoods;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SortingPo extends WarehouseOrderGoods {
    /**
     * 订单号
     */
    private String orderNo;

    private LocalDateTime bookStartTime;

    private String consigneeAddress;

    private List<WarehouseGoods> list = new ArrayList<>();
    private Long orderSubId;


}
