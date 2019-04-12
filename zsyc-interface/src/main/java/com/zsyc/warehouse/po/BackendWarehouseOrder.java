package com.zsyc.warehouse.po;

import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BackendWarehouseOrder extends BaseBean {
    private String sku;
    /**
     * 备货子表id
     */
    private Long warehouseGoodsId;

    private Long id;
    /**
     * 备货订单编号
     */
    private String warehouseOrderNo;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 属性key名称
     */
    private String attrKeyName;
    /**
     * 备货信息（多少斤，多少个，多少单）
     */
    private String message;
    /**
     * 属性值名称
     */
    private String attrValueName;


    private LocalDateTime createTime;
    //规格
    private String attrValue;

    /**
     * 状态
    * */
    private String status;

}
