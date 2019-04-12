package com.zsyc.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.common.AssertExt;
import com.zsyc.goods.entity.GoodsInfo;
import com.zsyc.goods.mapper.GoodsInfoMapper;
import com.zsyc.order.mapper.OrderGoodsMapper;
import com.zsyc.order.mapper.OrderSettlementReportMapper;
import com.zsyc.order.mapper.OrderSubInfoMapper;
import com.zsyc.order.mapper.OrderSubSettlementReportMapper;
import com.zsyc.order.po.OrderGoodsPo;
import com.zsyc.order.po.OrderSettlementReportPo;
import com.zsyc.order.po.OrderSubInfoPo;
import com.zsyc.order.po.OrderSubSettlementReportPo;
import com.zsyc.order.utils.OrderUtil;
import com.zsyc.order.vo.OrderSettlementReportVo;
import com.zsyc.store.mapper.StoreInfoMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: zsyc-parent
 * @description: 订单结算报表
 * @author: Mr.Ning
 * @create: 2019-03-21 14:25
 **/
@Service
public class OrderSettlementReportServiceImpl implements OrderSettlementReportService {

    @Resource
    private OrderSettlementReportMapper orderSettlementReportMapper;

    @Resource
    private OrderSubSettlementReportMapper orderSubSettlementReportMapper;

    @Resource
    private OrderSubSettlementReportService orderSubSettlementReportService;

    @Resource
    private OrderSubInfoService orderSubInfoService;

    @Resource
    private OrderSubInfoMapper orderSubInfoMapper;

    @Resource
    private OrderGoodsService orderGoodsService;

    @Resource
    private OrderGoodsMapper orderGoodsMapper;

    @Resource
    private StoreInfoMapper storeInfoMapper;

    @Resource
    private GoodsInfoMapper goodsInfoMapper;


    /**
     * 定时生成订单结算报表(后台)===>凌晨0点执行
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void produceOrderSettlementReport(){

        /**
         * 主报表操作
         * 注意orderSubInfoPos，里面的金额是全部子订单的金额
         */
        List<OrderSubInfoPo> orderSubInfoPos = orderSubInfoService.getOrderDataForReport(LocalDateTime.now());
        List<OrderGoodsPo> orderGoodsPos;
        List<OrderSettlementReportPo> orderSettlementReportPos = new ArrayList<>();
        List<OrderSubSettlementReportPo> orderSubSettlementReportPos = new ArrayList<>();
        OrderSettlementReportPo orderSettlementReportPo;
        OrderSubSettlementReportPo orderSubSettlementReportPo;
        for(OrderSubInfoPo orderSubInfoPo : orderSubInfoPos){
            String settlementNo = OrderUtil.getOrderNo();   //结算号

            orderSettlementReportPo = new OrderSettlementReportPo();
            orderSettlementReportPo.setSettlementNo(settlementNo);    //结算号
            orderSettlementReportPo.setStoreId(orderSubInfoPo.getStoreId());   //店铺id
            orderSettlementReportPo.setStoreName(storeInfoMapper.selectById(orderSubInfoPo.getStoreId()).getStoreName()); //店铺名称
            orderSettlementReportPo.setAmount(orderSubInfoPo.getActualAmount());    //总金额
            orderSettlementReportPo.setCreateTime(LocalDateTime.now());     //结算时间

            /**
             * 子报表操作
             */
            int num = 0;
            orderGoodsPos = orderGoodsService.getOrderGoodsDataForReport(orderSubInfoPo.getStoreId(),LocalDateTime.now());
            for(OrderGoodsPo orderGoodsPo : orderGoodsPos){
                orderSubSettlementReportPo = new OrderSubSettlementReportPo();
                orderSubSettlementReportPo.setSettlementNo(settlementNo);
                orderSubSettlementReportPo.setSku(orderGoodsPo.getSku());
                orderSubSettlementReportPo.setNum(orderGoodsPo.getNum());
                num += orderGoodsPo.getNum();
                orderSubSettlementReportPos.add(orderSubSettlementReportPo);
            }

            orderSettlementReportPo.setNum(num);   //总数量

            orderSettlementReportPos.add(orderSettlementReportPo);

        }

        //主报表insert
        if(orderSettlementReportPos.size() != 0){
            orderSettlementReportMapper.createOrderSettlementReport(orderSettlementReportPos);
        }

        //子报表insert
        if(orderSubSettlementReportPos.size() != 0){
            orderSubSettlementReportService.createOrderSubSettlementReport(orderSubSettlementReportPos);
        }

    }


    /**
     * 获取报表数据(后台)
     * @param orderSettlementReportVo
     * @return
     */
    @Override
    public IPage<OrderSettlementReportPo> getReportData(OrderSettlementReportVo orderSettlementReportVo) {
        AssertExt.notNull(orderSettlementReportVo.getCurrent(),"当前页为空" );
        AssertExt.notNull(orderSettlementReportVo.getSize(),"页码大小为空" );
        QueryWrapper<OrderSettlementReportPo> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
        if(orderSettlementReportVo.getReportStartTime() != null && orderSettlementReportVo.getReportEndTime() != null){
            queryWrapper.between("create_time", orderSettlementReportVo.getReportStartTime().toLocalDate(),orderSettlementReportVo.getReportEndTime().plusDays(1).toLocalDate());
        }
        if(orderSettlementReportVo.getStoreId() != null){
            queryWrapper.eq("store_id", orderSettlementReportVo.getStoreId());
        }
        IPage<OrderSettlementReportPo> page = new Page<>(orderSettlementReportVo.getCurrent(), orderSettlementReportVo.getSize());
        IPage<OrderSettlementReportPo> orderSettlementReportPoIPage = orderSettlementReportMapper.selectPage(page, queryWrapper);

        for(OrderSettlementReportPo orderSettlementReportPo : orderSettlementReportPoIPage.getRecords()){
            orderSettlementReportPo.setAmount(orderSettlementReportPo.getAmount() / 100);

            List<OrderSubSettlementReportPo> orderSubSettlementReportPos = orderSubSettlementReportMapper.selectList(new QueryWrapper<OrderSubSettlementReportPo>().eq("settlement_no", orderSettlementReportPo.getSettlementNo()));
            for(OrderSubSettlementReportPo orderSubSettlementReportPo : orderSubSettlementReportPos){
                orderSubSettlementReportPo.setGoodsInfo(goodsInfoMapper.selectOne(new QueryWrapper<GoodsInfo>().eq("sku", orderSubSettlementReportPo.getSku())));
            }
            orderSettlementReportPo.setOrderSubSettlementReportPos(orderSubSettlementReportPos);
        }

        return orderSettlementReportPoIPage;
    }

}
