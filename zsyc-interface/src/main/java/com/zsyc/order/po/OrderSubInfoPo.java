package com.zsyc.order.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zsyc.IEnum;
import com.zsyc.delivery.entity.DeliveryStaff;
import com.zsyc.framework.base.BaseBean;
import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.store.entity.StoreInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 子订单Po
 * @author: Mr.Ning
 * @create: 2019-02-15 10:14
 **/

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_sub_info")
@ApiModel(description = "子订单Po")
public class OrderSubInfoPo extends BaseBean {

    /**
     * 商品集合
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "商品集合")
    private List<OrderGoodsPo> orderGoodsPos;

    /**
     * 店铺信息
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "店铺信息")
    private StoreInfo storeInfo;

    /**
     * 配送员信息
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "配送员信息")
    private DeliveryStaff deliveryStaff;

    /**
     * 主订单支付类型
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "主订单支付类型")
    private String payType;

    /**
     * 是否可以催单
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "是否可以催单")
    private boolean orderIsAbleReminderBack;

    /**
     * 押金
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "押金")
    private double deposit;

    /**
     * 预订配送开始日期
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "预订配送开始日期")
    private LocalDate bookStartDate = LocalDate.now();

    /**
     * 预订配送终止日期
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "预订配送终止日期")
    private LocalDate bookEndDate = LocalDate.now();


    /**
     * 预订配送开始时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "预订配送开始时间")
    private LocalTime bookStartHours = LocalTime.now().withMinute(0).withSecond(0).withNano(0);

    /**
     * 预订配送终止时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "预订配送终止时间")
    private LocalTime bookEndHours = LocalTime.now().plusHours(1).withMinute(0).withSecond(0).withNano(0);

    /**
     * 退瓶瓶数
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "退瓶瓶数")
    private int returnBottleNum;


    /**
     * 实体内容
     */
    /**
     * 子订单ID
     */
    @ApiModelProperty(value = "子订单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户地址ID
     */
    @ApiModelProperty(value = "用户地址ID")
    private Long addressId;

    /**
     * 帐号id
     */
    @ApiModelProperty(value = "帐号id")
    private Long accountId;

    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话")
    private String consigneePhone;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;


    /**
     * 协议号
     */
    @ApiModelProperty(value = "协议号")
    private String protocolNo;


    /**
     * 主订单号
     */
    @ApiModelProperty(value = "主订单号")
    private String orderInfoNo;


    /**
     * 商品金额（不含运费）
     */
    @ApiModelProperty(value = "商品金额（不含运费）")
    private double amount;


    /**
     * 运费金额
     */
    @ApiModelProperty(value = "运费金额")
    private double expressAmount;


    /**
     * 实付金额
     */
    @ApiModelProperty(value = "实付金额")
    private double actualAmount;


    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private double discountAmount;


    /**
     * 租金
     */
    @ApiModelProperty(value = "租金")
    private double rentAmount;


    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private double refundAmount;


    /**
     * 预订配送开始时间
     */
    @ApiModelProperty(value = "预订配送开始时间")
    private LocalDateTime bookStartTime = LocalDateTime.now();

    /**
     * 预订配送终止时间
     */
    @ApiModelProperty(value = "预订配送终止时间")
    private LocalDateTime bookEndTime = LocalDateTime.now().plusHours(1);

    /**
     * 优先级(越大越优先)
     */
    @ApiModelProperty(value = "优先级(越大越优先)")
    private Integer priority;

    /**
     * 优惠卷ID
     */
    @ApiModelProperty(value = "优惠卷ID")
    private Long couponId;


    /**
     * 旧换新订单上传的图片
     */
    @ApiModelProperty(value = "旧换新订单上传的图片")
    private String photo;

    /**
     * 收货人名称
     */
    @ApiModelProperty(value = "收货人名称")
    private String consignee;

    /**
     * 客户备注
     */
    @ApiModelProperty(value = "客户备注")
    private String customerRemark;

    /**
     * 收货人地址
     */
    @ApiModelProperty(value = "收货人地址")
    private String consigneeAddress;

    /**
     * 配送方法
     */
    @ApiModelProperty(value = "配送方法")
    private String deliveryType = OrderSubInfo.EDeliveryType.EXPRESS.toString();

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 配送员ID
     */
    @ApiModelProperty(value = "配送员ID")
    private Long postmanId;

    /**
     * 会员备注
     */
    @ApiModelProperty(value = "会员备注")
    private String remark;

    /**
     * 后台备注
     */
    @ApiModelProperty(value = "后台备注")
    private String backendRemark;

    /**
     * 订单类型（ORDINARY普通订单、BOTTLEBACK退瓶订单、OLDFORNEW旧换新）
     */
    @ApiModelProperty(value = "订单类型（ORDINARY普通订单、BOTTLEBACK退瓶订单、OLDFORNEW旧换新）")
    private String type;

    /**
     * 最后一次催单时间
     */
    @ApiModelProperty(value = "最后一次催单时间")
    private LocalDateTime urgeTime;


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

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private Integer isDel;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "UNPAID(\"待支付\"), PAID(\"支付成功\"),CANCEL(\"取消支付\"),PACK(\"分拣\"),SEND(\"发货\"),DONE(\"完成\"),SHORTAGE(\"缺货\"),REFUND(\"退款(退瓶)\"),APPLY(\"申请退瓶中\"),PROCESSED(\"客服已处理\"),BEENSTOCK(\"已生成备货数据\")")
    private String orderStatus;


    public enum EOrderSubStatus implements IEnum {
        UNPAID("待支付"), PAID("待发货"),CANCEL("取消支付"),PACK("分拣"),SEND("待签收"),DONE("已完成"),SHORTAGE("缺货"),REFUND("退款(退瓶)"),APPLY("申请退瓶中"),PROCESSED("客服已处理"),BEENSTOCK("已生成备货数据");
        private String desc;
        EOrderSubStatus(String desc){
            this.desc = desc;
        }

        @Override
        public String desc() {
            return this.desc;
        }

        @Override
        public String val() {
            return this.name();
        }
    }

    public enum EDeliveryType implements IEnum {
        EXPRESS("配送"), SELF("自提");
        private String desc;
        EDeliveryType(String desc){
            this.desc = desc;
        }

        @Override
        public String desc() {
            return this.desc;
        }

        @Override
        public String val() {
            return this.name();
        }
    }

    public enum EOrderType implements IEnum {
        ORDINARY("普通订单"), BOTTLEBACK("退瓶订单"),OLDFORNEW("旧换新");
        private String desc;
        EOrderType(String desc){
            this.desc = desc;
        }

        @Override
        public String desc() {
            return this.desc;
        }

        @Override
        public String val() {
            return this.name();
        }
    }

}
