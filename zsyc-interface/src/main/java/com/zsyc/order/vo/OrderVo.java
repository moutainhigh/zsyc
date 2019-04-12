package com.zsyc.order.vo;

import com.zsyc.member.entity.MemberAddress;
import com.zsyc.order.entity.OrderInfo;
import com.zsyc.store.entity.StoreInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 订单Vo
 * @author: Mr.Ning
 * @create: 2019-02-12 17:55
 **/

@Data
public class OrderVo implements Serializable {

    /**
     * 付款方式('ONLINE'(微信支付),'OFFLINE(货到付款)','ACCOUNT(账期支付)')
     */
    private String payType;


    /**
     * 旧换新订单上传的图片
     */
    private String photo;

    /**
     * 子订单
     */
    private List<OrderSubInfoVo> orderSubInfoVos;

    /**
     * 订单类型：菜市场和快捷菜订单为0，气订单为1
     */
    private Integer orderCategory;

    /**
     * 气订单类型：1互换瓶，2新租瓶，3旧换新
     */
    private Integer gasOrderCategory;

    /**
     * 会员地址id
     */
    private Long addressId;

    /**
     * 会员地址信息
     */
    private MemberAddress memberAddress;


    /**
     *
     * 后台接口参数
     *
     */
    /**
     * 收货人电话
     */
    private String consigneePhone;

    /**
     * 收货人地址
     */
    private String consigneeAddress;

    /**
     * 收货人姓名
     */
    private String consignee;

    /**
     * 楼层
     */
    private Integer storey;
}
