package com.zsyc.order.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zsyc.framework.base.BaseBean;
import com.zsyc.goods.vo.GoodsPriceInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单子商品快照表Po
 * </p>
 *
 * @author Mr.Ning
 * @since 2019-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(description = "订单子商品快照表Po")
@TableName(value = "order_sub_goods")
public class OrderSubGoodsPo extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 此订单商品快照对应的商品信息
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "此订单商品快照对应的商品信息")
    private GoodsPriceInfoVO goodsPriceInfoVO;


    /**
     * 实体内容
     */
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单子表ID
     */
    private Long orderSubId;

    /**
     * 组合商品sku码
     */
    private String sku;

    /**
     * 子商品sku码
     */
    private String skuSub;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 单价
     */
    private double price;

    /**
     * 商品类型(0普通,1瓶,2气,3水,4配料,5水桶)
     */
    private Integer goodsStyle;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;


}
