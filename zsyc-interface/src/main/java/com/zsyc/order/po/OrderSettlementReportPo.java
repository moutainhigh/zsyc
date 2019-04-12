package com.zsyc.order.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 订单结算报表Po
 * </p>
 *
 * @author MP
 * @since 2019-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(description = "订单结算报表Po")
@TableName("order_settlement_report")
public class OrderSettlementReportPo extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 结算号
     */
    @ApiModelProperty(value = "结算号")
    private String settlementNo;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 订单总额(实付)
     */
    @ApiModelProperty(value = "订单总额(实付)")
    private double amount;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer num;

    /**
     * 结算日期
     */
    @ApiModelProperty(value = "结算日期")
    private LocalDateTime createTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "子报表数据")
    private List<OrderSubSettlementReportPo> orderSubSettlementReportPos;
}
