package com.zsyc.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.order.po.OrderInfoPo;
import com.zsyc.order.po.OrderProtocolGoodsPo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 协议商品 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-14
 */
public interface OrderProtocolGoodsMapper extends BaseMapper<OrderProtocolGoodsPo> {

    /**
     * 退瓶时更新商品数量
     * @param id
     * @param num
     */
    void updateGoodsNum(@Param("id") Long id, @Param("num") Integer num);


    /**
     * 查看店铺所有用户的协议
     * @param protocolNo
     * @return
     */
    OrderProtocolGoodsPo getProtocolGoodsByNo(String protocolNo);
}
