package com.zsyc.order.service;

import com.zsyc.order.po.OrderSubSettlementReportPo;

import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 订单子结算报表
 * @author: Mr.Ning
 * @create: 2019-03-21 14:29
 **/

public interface OrderSubSettlementReportService {

    /**
     * 创建子订单结算报表
     * @param orderSubSettlementReportPos
     */
    void createOrderSubSettlementReport(List<OrderSubSettlementReportPo> orderSubSettlementReportPos);
}
