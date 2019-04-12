package com.zsyc.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsyc.order.po.OrderSettlementReportPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 订单结算报表
 * @author: Mr.Ning
 * @create: 2019-03-21 14:26
 **/

public interface OrderSettlementReportMapper extends BaseMapper<OrderSettlementReportPo> {

    /**
     * 创建订单结算报表
     * @param orderSettlementReportPos
     */
    void createOrderSettlementReport(List<OrderSettlementReportPo> orderSettlementReportPos);
}
