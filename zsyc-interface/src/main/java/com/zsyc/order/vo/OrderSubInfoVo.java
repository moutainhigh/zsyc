package com.zsyc.order.vo;

import com.zsyc.member.entity.MemberAddress;
import com.zsyc.member.entity.MemberInfo;
import com.zsyc.order.entity.OrderGoods;
import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.store.entity.StoreInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: zsyc-parent
 * @description: OrderSubInfoVo
 * @author: Mr.Ning
 * @create: 2019-02-12 09:54
 **/
@Data
public class OrderSubInfoVo extends OrderSubInfo {

    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;

    @ApiModelProperty(value = "页码大小")
    private Integer size = 10;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "帐号")
    private String account;

    @ApiModelProperty(value = "开始下单时间")
    private LocalDateTime orderStartTime;

    @ApiModelProperty(value = "结束下单时间")
    private LocalDateTime orderEndTime;

    @ApiModelProperty(value = "支付类型")
    private String payType;

    /**
     * 订单商品集合
     */
    private List<OrderGoodsVo> orderGoodsVos;

    /**
     * 店铺信息
     */
    private StoreInfo storeInfo;

}
