package com.zsyc.order.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zsyc.framework.base.BaseBean;
import com.zsyc.goods.entity.GoodsInfo;
import com.zsyc.goods.entity.GoodsStorePrice;
import com.zsyc.goods.vo.GoodsPriceInfoVO;
import com.zsyc.order.entity.OrderProtocolGoods;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @program: zsyc-parent
 * @description: 协议商品
 * @author: Mr.Ning
 * @create: 2019-02-26 17:47
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_protocol_goods")
@ApiModel(description = "协议商品Po")
public class OrderProtocolGoodsPo extends BaseBean {

    /**
     * 协议商品信息
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "协议商品信息")
    private GoodsPriceInfoVO goodsPriceInfoVO;


    /**
     * 实体内容
     */
    /**
     * 协议商品Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "协议商品Id")
    private Long id;

    /**
     * 协议号
     */
    @ApiModelProperty(value = "协议号")
    private String protocolNo;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String sku;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer num;

    /**
     * 一瓶的押金，单价
     */
    @ApiModelProperty(value = "一瓶的押金，单价")
    private double price;


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
