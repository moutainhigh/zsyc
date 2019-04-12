package com.zsyc.order.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单结算报表
 * </p>
 *
 * @author MP
 * @since 2019-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_settlement_report")
public class OrderSettlementReportVo extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页
     */
    private Integer current = 1;

    /**
     * 页码大小
     */
    private Integer size = 10;


    @ApiModelProperty(value = "开始下单时间")
    private LocalDateTime reportStartTime;

    @ApiModelProperty(value = "结束下单时间")
    private LocalDateTime reportEndTime;


    /**
     *实体内容
     */
    /**
     * 主键
     */
    private Long id;

    /**
     * 结算号
     */
    private String settlementNo;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 订单总额(实付)
     */
    private Integer amount;

    /**
     * 商品数量
     */
    private Integer num;

    /**
     * 结算日期
     */
    private LocalDateTime createTime;


}
