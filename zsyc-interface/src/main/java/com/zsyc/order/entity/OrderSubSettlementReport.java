package com.zsyc.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单子结算报表
 * </p>
 *
 * @author MP
 * @since 2019-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(description = "订单子结算报表")
@TableName("order_sub_settlement_report")
public class OrderSubSettlementReport extends BaseBean {

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
     * sku
     */
    @ApiModelProperty(value = "sku")
    private String sku;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer num;

    /**
     * 创建时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
