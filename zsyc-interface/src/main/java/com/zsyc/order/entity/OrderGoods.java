package com.zsyc.order.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsyc.IEnum;
import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单商品快照表
 * </p>
 *
 * @author MP
 * @since 2019-02-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(description = "订单商品快照")
public class OrderGoods extends BaseBean {

    private static final long serialVersionUID = 1L;

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
    private Integer price;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private Integer discountAmount;

    /**
     * 旧换新的时候产生差额，差额=新瓶的单价减去旧瓶的估值
     */
    @ApiModelProperty(value = "旧换新的时候产生差额，差额=新瓶的单价减去旧瓶的估值")
    private Integer balance;

    /**
     * 应付金额
     */
    @ApiModelProperty(value = "应付金额")
    private Integer amount;

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
     * 旧换新处理类型，补差-UP 补废-FILL 免差-FREE
     */
    @ApiModelProperty(value = "旧换新处理类型，补差-UP 补废-FILL 免差-FREE")
    private String typesProcessing;

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

    public enum ETypesProcessing implements IEnum {
        UP("补差"), FILL("补废"),FREE("免差");
        private String desc;
        ETypesProcessing(String desc){
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
