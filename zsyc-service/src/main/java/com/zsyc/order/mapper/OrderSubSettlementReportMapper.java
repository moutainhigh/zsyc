package com.zsyc.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.order.po.OrderSubSettlementReportPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 订单子结算报表
 * @author: Mr.Ning
 * @create: 2019-03-21 14:30
 **/

public interface OrderSubSettlementReportMapper extends BaseMapper<OrderSubSettlementReportPo> {

    /**
     * 创建子订单结算报表
     * @param orderSubSettlementReportPos
     */
    void createOrderSubSettlementReport(List<OrderSubSettlementReportPo> orderSubSettlementReportPos);
}
