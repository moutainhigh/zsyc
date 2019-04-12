package com.zsyc.warehouse.po;

import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class BackendStockWarehouse extends BaseBean {

   private  List<BackendStockWarehouse>  a;
    private Long id;
    /**
     * 订单号
     */
    private String orderNo;

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
     * 属性值名称
     */
    private String attrValueName;

    private LocalDateTime createTime;
    //规格
    private String attrValue;

    /**
     * 图片
    * */
    private String picture;
    /**
     * 收货人手机
     */
    private String Phone;
    /**
     * 收货人名称
     */
    private String consignee;



    /**
     * 数量
     */
    private Long num;



    /**
     * 状态
    * */
    private String status;

}
