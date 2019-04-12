package com.zsyc.order.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.zsyc.IEnum;
import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 订单主表
 * </p>
 *
 * @author MP
 * @since 2019-02-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_info")
@ApiModel(description = "主订单")
public class OrderInfo extends BaseBean {

    private static final long serialVersionUID = 1L;

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
    private Integer amount;

    /**
     * 实付金额
     */
    @ApiModelProperty(value = "实付金额")
    private Integer actualAmount;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private Integer discountAmount;

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
