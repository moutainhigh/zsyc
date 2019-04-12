package com.zsyc.order.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zsyc.IEnum;
import com.zsyc.framework.base.BaseBean;
import com.zsyc.member.entity.MemberAddress;
import com.zsyc.order.entity.OrderInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 主订单Po
 * @author: Mr.Ning
 * @create: 2019-02-15 10:19
 **/

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_info")
@ApiModel(description = "主订单Po")
public class OrderInfoPo extends BaseBean {
    /**
     * 子订单集合
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "子订单集合")
    private List<OrderSubInfoPo> orderSubInfoPos;


    @TableField(exist = false)
    @ApiModelProperty(value = "支付方式集合")
    private List<PayTypePo> payTypes;

    @TableField(exist = false)
    @ApiModelProperty(value = "会员地址信息")
    private MemberAddress memberAddress;

    @TableField(exist = false)
    @ApiModelProperty(value = "总配送费")
    private double expressAmount ;


    /**
     * 实体内容
     */
    /**
     * 主订单ID
     */
    @ApiModelProperty(value = "主订单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 主订单号
     */
    @ApiModelProperty(value = "主订单号")
    private String orderNo;

    /**
     * 交易ID
     */
    @ApiModelProperty(value = "交易ID")
    private Long tranId;

    /**
     * 地址ID
     */
    @ApiModelProperty(value = "地址ID")
    private Long addressId;

    /**
     * 商品金额（不含运费）
     */
    @ApiModelProperty(value = "商品金额（不含运费）")
    private double amount;


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
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态:UNPAID(\"待支付\"), SUCCESS(\"支付成功\"),CANCEL(\"取消支付\"),FAIL(\"支付失败\"),ERROR(\"系统错误\")")
    private String orderStatus;

    /**
     * 付款时间
     */
    @ApiModelProperty(value = "付款时间")
    private LocalDateTime payTime;

    /**
     * 支付流水ID
     */
    @ApiModelProperty(value = "支付流水ID")
    private String payFlowId;

    /**
     * 支付流水号
     */
    @ApiModelProperty(value = "支付流水号")
    private String payFlowNo;

    /**
     * 付款方式('ONLINE'(微信支付),'OFFLINE(货到付款)','ACCOUNT(账期支付)')
     */
    @ApiModelProperty(value = "付款方式")
    private String payType;

    /**
     * 订单子表，使用逗号分开
     */
    @ApiModelProperty(value = "订单子ID，使用逗号分开")
    private String subOrderIds;


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


    public enum EOrderStatus implements IEnum {
        UNPAID("待支付"), SUCCESS("支付成功"),CANCEL("取消支付"),FAIL("支付失败"),ERROR("系统错误");
        private String desc;
        private EOrderStatus(String desc){
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

    public enum EPayType implements IEnum {
        ONLINE("微信支付"), OFFLINE("货到付款"),ACCOUNT("账期支付");
        private String desc;
        private EPayType(String desc){
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
