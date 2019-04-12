package com.zsyc.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.order.entity.OrderGoods;
import com.zsyc.order.po.OrderGoodsPo;
import com.zsyc.order.po.OrderSubInfoPo;
import com.zsyc.order.vo.OrderGoodsVo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @program: zsyc-parent
 * @description: 订单商品快照
 * @author: Mr.Ning
 * @create: 2019-02-11 09:21
 **/

public interface OrderGoodsMapper extends BaseMapper<OrderGoodsPo> {

    /**
     * 创建商品快照
     */
    void createOrderGoods(List<OrderGoods> ls);

    /**
     * 删除商品快照
     * @return
     */
    int delOrderGoods(Map map);


    /**
     * 根据sku集合查询子订单id（会进行排重）
     * @param ls
     * @return
     */
    List<Long> getOrderSubIdsBySkus(List<String> ls);

    /**
     * 会员最近购买的商品
     * @param page
     * @param orderGoodsVo
     * @return
     */
    IPage<OrderGoodsPo> getLatelyGoodsByMember(IPage<OrderGoodsPo> page,@Param("orderGoodsVo") OrderGoodsVo orderGoodsVo);


    /**
     * 更改商品快照状态(确认、缺货)
     * @param id
     * @param status
     */
    void updateStatusById(@Param("id") long id,@Param("status") String status);


    /**
     * 从订单快照中获取生成订单结算报表需要的数据
     * @return
     */
    List<OrderGoodsPo> getOrderGoodsDataForReport(@Param("storeId") long storeId,@Param("now") LocalDateTime now);
}
