package com.zsyc.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.order.po.OrderSettlementReportPo;
import com.zsyc.order.vo.OrderSettlementReportVo;

import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 订单结算报表
 * @author: Mr.Ning
 * @create: 2019-03-21 14:22
 **/

public interface OrderSettlementReportService {

    /**
     * 获取报表数据(后台)
     * @param orderSettlementReportVo
     * @return
     */
    IPage<OrderSettlementReportPo> getReportData(OrderSettlementReportVo orderSettlementReportVo);
}
