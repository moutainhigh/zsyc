package com.zsyc.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zsyc.order.mapper.OrderSubSettlementReportMapper;
import com.zsyc.order.po.OrderSubSettlementReportPo;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 订单子结算报表
 * @author: Mr.Ning
 * @create: 2019-03-21 14:30
 **/
@Service
public class OrderSubSettlementReportServiceImpl implements OrderSubSettlementReportService {

    @Resource
    private OrderSubSettlementReportMapper orderSubSettlementReportMapper;

    /**
     * 创建子订单结算报表
     * @param orderSubSettlementReportPos
     */
    @Override
    public void createOrderSubSettlementReport(List<OrderSubSettlementReportPo> orderSubSettlementReportPos) {
        orderSubSettlementReportMapper.createOrderSubSettlementReport(orderSubSettlementReportPos);
    }
}
