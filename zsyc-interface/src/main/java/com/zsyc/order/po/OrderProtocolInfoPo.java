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
 * @program: zsyc-parent
 * @description: 协议
 * @author: Mr.Ning
 * @create: 2019-02-26 17:47
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_protocol_info")
@ApiModel(description = "协议信息Po")
public class OrderProtocolInfoPo extends BaseBean {

    /**
     * 协议商品集合
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "协议商品集合")
    private List<OrderProtocolGoodsPo> orderProtocolGoodsPos;

    /**
     * 地址
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "地址")
    private String memberAddress;

    /**
     * 当前瓶数
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "当前瓶数")
    private Integer CurrentBottleNum;

    /**
     * 协议状态
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "协议状态")
    private String status;

    /**
     * 协议下单时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "协议状态")
    private LocalDateTime orderProtocolTime;


    /**
     * 实体内容
     */
    /**
     * 协议Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "协议Id")
    private Long id;

    /**
     * 协议名称
     */
    @ApiModelProperty(value = "协议名称")
    private String protocolName;

    /**
     * 用户地址id
     */
    @ApiModelProperty(value = "用户地址id")
    private Long memberAddressId;

    /**
     * 门店Id
     */
    @ApiModelProperty(value = "门店Id")
    private Long storeId;

    /**
     * 协议号
     */
    @ApiModelProperty(value = "协议号")
    private String protocolNo;

    /**
     * 总押金
     */
    @ApiModelProperty(value = "总押金")
    private double depositAmount;


    /**
     * 当前押金
     */
    @ApiModelProperty(value = "当前押金")
    private double depositCurrent;


    /**
     * 押的sku总数量
     */
    @ApiModelProperty(value = "押的sku总数量")
    private Integer num;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;


    /**
     * 最后一次缴纳租金时间
     */
    @ApiModelProperty(value = "最后一次缴纳租金时间")
    private LocalDateTime lastRentPaymentTime;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 更新人ID
     */
    @ApiModelProperty(value = "更新人ID")
    private Long updateUserId;
}
