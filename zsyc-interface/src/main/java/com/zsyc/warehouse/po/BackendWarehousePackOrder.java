package com.zsyc.warehouse.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BackendWarehousePackOrder extends BaseBean {
    private Long id;
    private LocalDateTime createTime;
    /**
     * 图片
     * */
    private String img;
    /**
     * 规格
     * */
    private String attrValuer;
    /**
     * 数量
     */
    private Long num;
    /**
     * 单位
     */
    private String unit;
    /**
     * 名称
     */
    private String goodsName;
    private String sku;

}
