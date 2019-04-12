package com.zsyc.order.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zsyc.IEnum;
import com.zsyc.framework.base.BaseBean;
import com.zsyc.goods.vo.GoodsPriceInfoVO;
import com.zsyc.order.entity.OrderGoods;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 订单商品快照Po
 * @author: Mr.Ning
 * @create: 2019-02-15 10:13
 **/

@Data
@TableName("order_goods")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(description = "订单商品快照Po")
public class OrderGoodsPo extends BaseBean {

    /**
     * 此订单商品快照对应的商品信息
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "此订单商品快照对应的商品信息")
    private GoodsPriceInfoVO goodsPriceInfoVO;

    @TableField(exist = false)
    @ApiModelProperty(value = "气的价格")
    private double gasPrice;

    @TableField(exist = false)
    @ApiModelProperty(value = "押金")
    private double deposit;

    @TableField(exist = false)
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    @TableField(exist = false)
    @ApiModelProperty(value = "地址id")
    private Long addressId;

    @TableField(exist = false)
    @ApiModelProperty(value = "订单子商品快照")
    private List<OrderSubGoodsPo> orderSubGoodsPos;


    /**
     * 实体类内容
     */
    /**
     * 订单商品快照ID
     */
    @ApiModelProperty(value = "订单商品快照ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单子表ID
     */
    @ApiModelProperty(value = "订单子ID")
    private Long orderSubId;

    /**
     * 商品sku码
     */
    @ApiModelProperty(value = "商品sku码")
    private String sku;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer num;

    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private double price;


    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private double discountAmount;


    /**
     * 旧换新的时候产生差额，差额=新瓶的单价减去旧瓶的估值
     */
    @ApiModelProperty(value = "旧换新的时候产生差额，差额=新瓶的单价减去旧瓶的估值")
    private double balance;

    /**
     * 旧换新处理类型，补差-UP 补废-FILL 免差-FREE
     */
    @ApiModelProperty(value = "旧换新处理类型，补差-UP 补废-FILL 免差-FREE")
    private String typesProcessing;


    /**
     * 应付金额
     */
    @ApiModelProperty(value = "应付金额")
    private double amount;


    /**
     * 优惠卷ID
     */
    @ApiModelProperty(value = "优惠卷ID")
    private Integer couponId;

    /**
     * 商品属性(json)
     */
    @ApiModelProperty(value = "商品属性(json)")
    private String goodsAttrs;

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
     * 冗余计量单位
     */
    @ApiModelProperty(value = "冗余计量单位")
    private String unit;

    /**
     * 状态 DONE-确认，SHORTAGE-缺货
     */
    @ApiModelProperty(value = "状态 DONE-确认，SHORTAGE-缺货")
    private String status;


    public enum EOrderGoodsStatus implements IEnum {
        DONE("确认"),SHORTAGE("缺货");
        private String desc;
        private EOrderGoodsStatus(String desc){
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
