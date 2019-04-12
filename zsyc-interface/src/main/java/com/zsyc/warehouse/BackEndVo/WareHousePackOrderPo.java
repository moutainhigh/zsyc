package com.zsyc.warehouse.BackEndVo;

import com.zsyc.framework.base.BaseBean;
import com.zsyc.warehouse.po.OrderGoodsKuai;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class WareHousePackOrderPo extends BaseBean {
    private Long id;
    private LocalDateTime createTime;
    private Long wareHousePackOrderGoodId;
    private Long  wareHousePackId;
    private Long orderId;
    private Long storeId;
    private Long warehouseStaffId;
    private LocalDateTime doneTime;
    private String remark;
    private String wareHousePackOrderStatus;
    private  Long num;
    private String unit;
    private String WareHousePackOrderGoodsStatus;
    private String sku;
    private String goodsName;
    private String img;
    private List<OrderGoodsKuai> kuaijie=new ArrayList<>();



}
