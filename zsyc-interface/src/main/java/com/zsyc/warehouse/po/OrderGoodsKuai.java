package com.zsyc.warehouse.po;

import com.zsyc.order.entity.OrderGoods;
import lombok.Data;

@Data
public class OrderGoodsKuai extends OrderGoods {
    private String goodsName;
    private String img;
    private Double TotalPrice;
}
