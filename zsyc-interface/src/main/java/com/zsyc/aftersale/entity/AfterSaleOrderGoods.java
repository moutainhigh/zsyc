package com.zsyc.aftersale.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 售后订单商品表
 * </p>
 *
 * @author MP
 * @since 2019-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AfterSaleOrderGoods extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 售后单号
     */
    private String afterSaleNo;

    /**
     * 子订单id
     */
    private Long orderSubId;

    /**
     * 商品编码
     */
    private String sku;

    /**
     * 原始下单时下的商品数量
     */
    private Integer original;

    /**
     * 退货的数量=原始下单时下的商品数量-分拣摊剩余的数量
     */
    private Integer num;

    /**
     * 退款总价（实际付款）=退货的数量*商品单价
     */
    private Integer refundAmount;

    /**
     * 快捷菜的父级sku
     */
    private String parentSku;


}
