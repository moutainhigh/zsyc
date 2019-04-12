package com.zsyc.order.vo;

import com.zsyc.goods.vo.GoodsPriceInfoVO;
import com.zsyc.order.entity.OrderGoods;
import lombok.Data;

/**
 * @program: zsyc-parent
 * @description: OrderGoodsVo
 * @author: Mr.Ning
 * @create: 2019-02-12 09:55
 **/
@Data
public class OrderGoodsVo extends OrderGoods {

    /**
     * 当前页码
     */
    private Integer current = 1;

    /**
     * 页码大小
     */
    private Integer size = 10;

    /**
     * 商品价格信息
     */
    private GoodsPriceInfoVO goodsPriceInfoVO;

}
