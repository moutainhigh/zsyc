package com.zsyc.aftersale.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.aftersale.entity.AfterSaleOrder;
import com.zsyc.aftersale.entity.AfterSaleOrderGoods;
import com.zsyc.aftersale.mapper.AfterSaleOrderGoodsMapper;
import com.zsyc.aftersale.mapper.AfterSaleOrderMapper;
import com.zsyc.aftersale.po.*;
import com.zsyc.common.AssertExt;
import com.zsyc.goods.entity.GoodsInfo;
import com.zsyc.goods.entity.GoodsStorePrice;
import com.zsyc.goods.service.GoodsStorePriceService;
import com.zsyc.order.entity.OrderGoods;
import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.warehouse.mapper.WarehousePackOrderMapper;
import com.zsyc.warehouse.service.WarehousePackOrdersService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class IAfterSaleOrderGoodsServiceImpl implements AfterSaleOrderGoodsService {
    @Autowired
    public AfterSaleOrderGoodsMapper afterSaleOrderGoodsMapper;
    @Autowired
    public AfterSaleOrderMapper afterSaleOrderMapper;
    @Autowired
    public WarehousePackOrdersService warehousePackOrdersService;
    @Autowired
    public AfterSaleOrderService afterSaleService;
    @Autowired
    private WarehousePackOrderMapper warehousePackOrderMapper;
    @Autowired
    public GoodsStorePriceService goodsStorePriceService;

    @Override
    public Integer afterSaleOrderGoods(String afteraleSNo, Integer original, String sku, Long ordersubId, Integer num, Integer refundAmount,String parentSku) {


        AfterSaleOrderGoods afterSaleOrderGoods = new AfterSaleOrderGoods();
        afterSaleOrderGoods.setAfterSaleNo(afteraleSNo);
        afterSaleOrderGoods.setOriginal(original);
        afterSaleOrderGoods.setSku(sku);
        afterSaleOrderGoods.setOrderSubId(ordersubId);
        afterSaleOrderGoods.setNum(num);
        afterSaleOrderGoods.setRefundAmount(refundAmount);
        afterSaleOrderGoods.setParentSku(parentSku);
        return afterSaleOrderGoodsMapper.insert(afterSaleOrderGoods);
    }

    @Override
    public OrderGoods selectGoodsPrice(Long subId, String sku) {


        return afterSaleOrderGoodsMapper.selectGoodsPrice(subId, sku);
    }

    @Override
    public Integer updateOrderSub(Long ordersubId) {
        return afterSaleOrderGoodsMapper.updateOrderSub(ordersubId);
    }

    @Override
    public Integer updateOrderGoods(Long ordersubId) {
        return afterSaleOrderGoodsMapper.updateOrderGoods(ordersubId);
    }

    @Override
    public Object selectAfterSaleOrderGoods() {
        List<AfterSaleOrder> afterSaleOrders = afterSaleOrderMapper.selectAfterSaleOrderSubId();
        List<AterSaleOrderPo> afterSaleOrderPoo = new ArrayList<>();
        for (AfterSaleOrder afterSaleOrder : afterSaleOrders) {
            Long subId = afterSaleOrder.getOrderSubId();
            List<AterSaleOrderVo> aterSaleOrderVos = afterSaleOrderGoodsMapper.selectAfterSaleOrderGoods(subId);
            for (AterSaleOrderVo afterSaleOrderv : aterSaleOrderVos) {
                Long priceAll = afterSaleOrderv.getNum() * afterSaleOrderv.getPrice();
                afterSaleOrderv.setPriceAll(priceAll);
                if (afterSaleOrderv.getStatus().equals("SHORTAGE")) {
                    afterSaleOrderv.setStatus("缺货");
                } else {
                    afterSaleOrderv.setStatus("有货");
                }

            }
            List<OrderSubInfo> afterSaleOrderAll = afterSaleOrderMapper.selectAfterSaleOrderAll(subId);
            for (OrderSubInfo orderSubInfo : afterSaleOrderAll) {
                AterSaleOrderPo aterSaleOrderPoa = new AterSaleOrderPo();
                aterSaleOrderPoa.setOrderNo(orderSubInfo.getOrderNo());
                aterSaleOrderPoa.setConsigneePhone(orderSubInfo.getConsigneePhone());
                aterSaleOrderPoa.setCreateTime(orderSubInfo.getCreateTime());
                aterSaleOrderPoa.setConsignee(orderSubInfo.getConsignee());
                aterSaleOrderPoa.setList(aterSaleOrderVos);

                afterSaleOrderPoo.add(aterSaleOrderPoa);
            }


        }

        return afterSaleOrderPoo;
    }

    @Override
    public Integer updateSubOrderemark(String backendRemark, Long subId) {
        return afterSaleOrderGoodsMapper.updateSubOrderemark(backendRemark, subId);
    }
//缺货插入客服表按钮
    @Override
    public Object outOcket(AfterSaleOrderAccept afterSaleOrderAccept) {
        //获得主备货单
        Long ordersubId = afterSaleOrderAccept.getOrderSubId();
        Long packId=afterSaleOrderAccept.getWarehousePackOrderId();

        List<AfterSaleOrderChildren> afterSaleOrderAcceptList = afterSaleOrderAccept.getList();

        LocalDateTime time = LocalDateTime.now();
//        售后单号
        String afteraleSNo = String.valueOf(time.toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
                //循环第一个对象看看是否快捷菜
        for (AfterSaleOrderChildren afterSaleOrderChildren : afterSaleOrderAcceptList) {
            String sku = afterSaleOrderChildren.getSku();
            GoodsInfo goodsInfo1 = warehousePackOrderMapper.selectSkuGoodsInfo(sku);
            //如果不是快捷菜
            if( afterSaleOrderChildren.getQuick().size()==0&&goodsInfo1.getGoodsType()!=2){
                OrderGoods OGood = selectGoodsPrice(ordersubId, sku);
                Integer price = OGood.getPrice();
                Integer num = afterSaleOrderChildren.getNum();
                Integer numIng = afterSaleOrderChildren.getNumIng();
                Integer num1 = num - numIng;
                Integer refundAmount = price * num;
                afterSaleOrderGoods(afteraleSNo, num, sku, ordersubId, num1, refundAmount,null);
            }else {
                //如果是快捷菜把里面的list拿出来
                List<AfterSaleOrderChildren> afterSaleOrderChildren1=afterSaleOrderChildren.getQuick();
                String quick=sku;
                afterSaleOrderGoods(afteraleSNo, afterSaleOrderChildren.getNum(), sku, ordersubId, null, null,null);
                for (AfterSaleOrderChildren afterSaleOrderAcceptKuai:afterSaleOrderChildren1){
                        //GoodsInfo goodsInfo = warehousePackOrderMapper.selectSkuGoodsInfo(afterSaleOrderAcceptKuai.getSku());
                        OrderSubInfo orderSubInfo=afterSaleOrderGoodsMapper.selectOrderNoSubId(ordersubId);
                        GoodsStorePrice goodsStorePrice = afterSaleOrderGoodsMapper.selectGoodsStoreIdPrice(orderSubInfo.getStoreId(),afterSaleOrderAcceptKuai.getSku());
                        Integer price = goodsStorePrice.getPrice();
                        Integer num=afterSaleOrderAcceptKuai.getNum();
                        Integer numing=afterSaleOrderAcceptKuai.getNumIng();
                        Integer num1 = num - numing;
                        Integer refundAmount = price * num1;
                        afterSaleOrderGoods(afteraleSNo, num, afterSaleOrderAcceptKuai.getSku(), ordersubId, numing, refundAmount,quick);
                }
            }

        }
        updateOrderGoods(ordersubId);
        updateOrderSub(ordersubId);
        afterSaleService.outStock(afteraleSNo, afterSaleOrderAccept.getStaffPhone(), afterSaleOrderAccept.getStaffName(), afterSaleOrderAccept.getWarehousePackOrderId(), ordersubId, afterSaleOrderAccept.getMemberAddressId());
        //用主分拣单id更改主分拣单信息
        warehousePackOrdersService.packOrderDoneStatus(packId);
        return 1;
    }

    @Override
    public Object selectAfterSaleOrderOrderNo(String afterSaleNo) {

        List<BackendAfterSaleOrderPo> afterSaleOrderPos = afterSaleOrderGoodsMapper.selectAfterSaleOrderOrderNo(afterSaleNo);
        BackendAfterSaleOrderVo backendAfterSaleOrderVo = new BackendAfterSaleOrderVo();
        backendAfterSaleOrderVo.setAfterSaleNo(afterSaleOrderPos.get(0).getAfterSaleNo());
        backendAfterSaleOrderVo.setStaffName(afterSaleOrderPos.get(0).getStaffName());
        backendAfterSaleOrderVo.setStaffPhone(afterSaleOrderPos.get(0).getStaffPhone());
        backendAfterSaleOrderVo.setList(afterSaleOrderPos);
        return backendAfterSaleOrderVo;
    }

    @Override
    public Object selectAfterSaleOrderSubId(Long subId) {
        List<BackendAfterSaleOrderPo> afterSaleOrderPos = afterSaleOrderGoodsMapper.selectAfterSaleOrderSubId(subId);
        BackendAfterSaleOrderVo backendAfterSaleOrderVo = new BackendAfterSaleOrderVo();
        backendAfterSaleOrderVo.setAfterSaleNo(afterSaleOrderPos.get(0).getAfterSaleNo());
        backendAfterSaleOrderVo.setStaffName(afterSaleOrderPos.get(0).getStaffName());
        backendAfterSaleOrderVo.setStaffPhone(afterSaleOrderPos.get(0).getStaffPhone());
        backendAfterSaleOrderVo.setList(afterSaleOrderPos);
        return backendAfterSaleOrderVo;

    }

    @Override
    public Object selectAfterSaleStaffName(String staffName) {
        List<AfterSaleOrder> afterSaleOrders = afterSaleOrderGoodsMapper.selectAfterSaleStaffName(staffName);
        List<BackendAfterSaleOrderVo> backendAfterSaleOrderVos = new ArrayList<>();
        for (AfterSaleOrder afterSaleOrder : afterSaleOrders) {
            String afterSaleNo = afterSaleOrder.getAfterSaleNo();
            List<BackendAfterSaleOrderPo> afterSaleOrderPos = afterSaleOrderGoodsMapper.selectAfterSaleOrderOrderNo(afterSaleNo);
            BackendAfterSaleOrderVo backendAfterSaleOrderVo = new BackendAfterSaleOrderVo();
            backendAfterSaleOrderVo.setAfterSaleNo(afterSaleOrderPos.get(0).getAfterSaleNo());
            backendAfterSaleOrderVo.setStaffName(afterSaleOrderPos.get(0).getStaffName());
            backendAfterSaleOrderVo.setStaffPhone(afterSaleOrderPos.get(0).getStaffPhone());
            backendAfterSaleOrderVo.setList(afterSaleOrderPos);
            backendAfterSaleOrderVos.add(backendAfterSaleOrderVo);
        }
        return backendAfterSaleOrderVos;
    }

    @Override
    public Object selectAfterSalePhone(String phone) {
        List<AfterSaleOrder> afterSaleOrders = afterSaleOrderGoodsMapper.selectAfterSalePhone(phone);
        List<BackendAfterSaleOrderVo> backendAfterSaleOrderVos = new ArrayList<>();
        for (AfterSaleOrder afterSaleOrder : afterSaleOrders) {
            String afterSaleNo = afterSaleOrder.getAfterSaleNo();
            List<BackendAfterSaleOrderPo> afterSaleOrderPos = afterSaleOrderGoodsMapper.selectAfterSaleOrderOrderNo(afterSaleNo);
            BackendAfterSaleOrderVo backendAfterSaleOrderVo = new BackendAfterSaleOrderVo();
            backendAfterSaleOrderVo.setAfterSaleNo(afterSaleOrderPos.get(0).getAfterSaleNo());
            backendAfterSaleOrderVo.setStaffName(afterSaleOrderPos.get(0).getStaffName());
            backendAfterSaleOrderVo.setStaffPhone(afterSaleOrderPos.get(0).getStaffPhone());
            backendAfterSaleOrderVo.setList(afterSaleOrderPos);
            backendAfterSaleOrderVos.add(backendAfterSaleOrderVo);
        }
        return backendAfterSaleOrderVos;

    }

    @Override
    public Object selectAfterSaleAll(AfterSalePo afterSalePo) {

        String afterSaleNo = afterSalePo.getAfterSaleNo();
        Long subId = afterSalePo.getSubId();

        Long staffName = afterSalePo.getStaffName();
        String Phone = afterSalePo.getPhone();
        if(StringUtils.isBlank(afterSaleNo))afterSaleNo = null;

        if(StringUtils.isBlank(Phone))Phone = null;


        Integer currentPage = afterSalePo.getCurrentPage();
        Integer pageSize = afterSalePo.getPageSize();
        IPage<AfterSaleOrder> page = new Page<AfterSaleOrder>(currentPage, pageSize);
        List<BackendAfterSaleOrderVo> backendAfterSaleOrderVos = new ArrayList<>();
        //获得所有客服表信息
        IPage<AfterSaleOrder>afterSaleOrderIPage= afterSaleOrderGoodsMapper.selectAfterSaleAll(page, afterSaleNo, subId, staffName, Phone);
        for (AfterSaleOrder afterSaleOrder : afterSaleOrderIPage.getRecords()) {
            String afterSaleNo1 = afterSaleOrder.getAfterSaleNo();
            OrderSubInfo orderSubInfo =afterSaleOrderGoodsMapper.selectOrderNoSubId(afterSaleOrder.getOrderSubId());
            //用售后单号售后字表信息
            List<BackendAfterSaleOrderPo> afterSaleOrderPos = afterSaleOrderGoodsMapper.selectAfterSaleOrderOrderNo(afterSaleNo1);
            BackendAfterSaleOrderVo backendAfterSaleOrderVo = new BackendAfterSaleOrderVo();
            for(BackendAfterSaleOrderPo afterSaleNo12:afterSaleOrderPos){
                GoodsInfo goodsInfo = warehousePackOrderMapper.selectSkuGoodsInfo(afterSaleNo12.getSku());

                //查找该sku是否组合商品
                if (goodsInfo.getGoodsType() == 2) {
                    String parentSku=afterSaleNo12.getSku();
                    List<BackendAfterSaleOrderPo>afterSaleOrderGoods=afterSaleOrderGoodsMapper.checkKuaiJie(afterSaleOrder.getOrderSubId(),parentSku,afterSaleNo12.getAfterSaleNo());
                    for(BackendAfterSaleOrderPo afterSaleOrderGoods3:afterSaleOrderGoods){
                        BigDecimal e =new BigDecimal(afterSaleOrderGoods3.getPrice());
                        BigDecimal t =new BigDecimal(100);
                        BigDecimal a =e.divide(t);
                        BigDecimal b =new BigDecimal(afterSaleOrderGoods3.getNum());
                        BigDecimal d=a.multiply(b);
                        afterSaleOrderGoods3.setRefundAmount(d);
                    }
                    BackendAfterSaleOrderPo backendAfterSaleOrderPo =afterSaleNo12;
                    List<BackendAfterSaleOrderPo> backendAfterSaleOrderPos =new ArrayList<>();
                    backendAfterSaleOrderPo.setOrderNo(orderSubInfo.getOrderNo());
                    backendAfterSaleOrderPo.setList(afterSaleOrderGoods);
                    backendAfterSaleOrderPos.add(backendAfterSaleOrderPo);

                    backendAfterSaleOrderVo.setList(backendAfterSaleOrderPos);
                    backendAfterSaleOrderVo.setOrderNo(orderSubInfo.getOrderNo());
                    backendAfterSaleOrderVo.setAfterSaleNo(afterSaleOrderPos.get(0).getAfterSaleNo());
                    if((afterSaleOrder.getStatus()!=null)){

                        if(afterSaleOrder.getStatus().equals("EXCHANGE")){
                            backendAfterSaleOrderVo.setStatus("换货");
                        }else if(afterSaleOrder.getStatus().equals("REFUND")){
                            backendAfterSaleOrderVo.setStatus("退货");
                        }else {
                            backendAfterSaleOrderVo.setStatus("待处理");
                        }
                    }

                    afterSaleNo12.setOrderNo(orderSubInfo.getOrderNo());

                    backendAfterSaleOrderVo.setStaffName(afterSaleOrderPos.get(0).getStaffName());
                    backendAfterSaleOrderVo.setPhone(orderSubInfo.getConsigneePhone());
                    backendAfterSaleOrderVo.setStaffPhone(afterSaleOrderPos.get(0).getStaffPhone());
                    backendAfterSaleOrderVo.setId(afterSaleOrder.getId());
                    backendAfterSaleOrderVo.setRemark(afterSaleOrderPos.get(0).getRemark());
                }else {
                    BigDecimal e =new BigDecimal(afterSaleNo12.getPrice());
                    BigDecimal t =new BigDecimal(100);
                    BigDecimal a =e.divide(t);
                    BigDecimal b =new BigDecimal(afterSaleNo12.getNum());
                    BigDecimal d=a.multiply(b);
                    afterSaleNo12.setRefundAmount(d);

                    if((afterSaleOrder.getStatus()!=null)){

                        if(afterSaleOrder.getStatus().equals("EXCHANGE")){
                            backendAfterSaleOrderVo.setStatus("换货");
                        }else if(afterSaleOrder.getStatus().equals("REFUND")){
                            backendAfterSaleOrderVo.setStatus("退货");
                        }else {
                            backendAfterSaleOrderVo.setStatus("待处理");
                        }
                    }



                    afterSaleNo12.setOrderNo(orderSubInfo.getOrderNo());
                    backendAfterSaleOrderVo.setAfterSaleNo(afterSaleOrderPos.get(0).getAfterSaleNo());
                    backendAfterSaleOrderVo.setStaffName(afterSaleOrderPos.get(0).getStaffName());
                    backendAfterSaleOrderVo.setPhone(orderSubInfo.getConsigneePhone());
                    backendAfterSaleOrderVo.setStaffPhone(afterSaleOrderPos.get(0).getStaffPhone());
                    backendAfterSaleOrderVo.setId(afterSaleOrder.getId());
                    backendAfterSaleOrderVo.setRemark(afterSaleOrderPos.get(0).getRemark());





                }

                List<BackendAfterSaleOrderPo> afterSaleOrderGoods1 = afterSaleOrderGoodsMapper.checkParentSku(afterSaleNo12.getOrderSubId(), afterSaleNo12.getAfterSaleNo());
                for (BackendAfterSaleOrderPo afterSaleOrderGoods3:afterSaleOrderGoods1){
                    afterSaleOrderGoods3.setOrderNo(orderSubInfo.getOrderNo());
                }
                backendAfterSaleOrderVo.getList().addAll(afterSaleOrderGoods1);
            }

            backendAfterSaleOrderVos.add(backendAfterSaleOrderVo);

        }
        return backendAfterSaleOrderVos;
    }


    @Override
    public Integer updateAfterStatus(Long afterId, String status) {

        return afterSaleOrderGoodsMapper.updateAfterStatus(afterId,status);
    }

    @Override
    public Integer updateAfterSaleOrderRemark(AfterSaleOrder afterSaleOrder) {
        String remark= afterSaleOrder.getRemark();
        if(StringUtils.isBlank(remark))remark = null;

        AssertExt.isFalse(remark==null,"备注不可以不写啊");


        return afterSaleOrderGoodsMapper.updateAfterSaleOrderRemark(afterSaleOrder.getId(),remark);
    }

    @Override
    public Integer doneService(Long id) {
        AfterSaleOrder afterSaleOrder= afterSaleOrderGoodsMapper.selectSaleOrder(id);
        afterSaleOrderGoodsMapper.subProcessed(afterSaleOrder.getOrderSubId());
        return afterSaleOrderGoodsMapper.doneService(id);
    }
}
