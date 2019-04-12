package com.zsyc.warehouse.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.zsyc.common.AssertExt;
import com.zsyc.goods.entity.GoodsInfo;
import com.zsyc.goods.entity.GoodsStorePrice;
import com.zsyc.goods.service.GoodsStorePriceService;
import com.zsyc.goods.vo.GoodsAttributeValueVO;
import com.zsyc.goods.vo.GoodsPriceInfoVO;
import com.zsyc.order.entity.OrderGoods;
import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.order.mapper.OrderGoodsMapper;
import com.zsyc.order.po.OrderGoodsPo;
import com.zsyc.warehouse.BackEndVo.WareHouseOrderVo;
import com.zsyc.warehouse.BackEndVo.WareHousePackOrderPo;
import com.zsyc.warehouse.BackEndVo.WareHousePackOrderVo;
import com.zsyc.warehouse.entity.*;
import com.zsyc.warehouse.mapper.*;
import com.zsyc.warehouse.po.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class IWarehousePackOrdersServiceImpl implements WarehousePackOrdersService {

    @Autowired
    private WarehousePackOrderGoodsMapper warehousePackOrderGoodsMapper;
    @Autowired
    private WarehousePackOrderMapper warehousePackOrderMapper;
    @Autowired
    private WarehouseOrderMapper warehouseOrder;
    @Autowired
    private WarehouseOrderGoodsMapper warehouseOrderGoodsMapper;
    @Autowired
    private WarehouseStaffMapper warehouseStaffMapper;
    @Autowired
    public GoodsStorePriceService goodsStorePriceService;



    //备货员已接单数据
    @Override
    public Object checkGoodsInfo(Long wareHouseId) {
//        用备货员id查主备货id
        List<WarehouseOrder> wareHouSpack = warehousePackOrderGoodsMapper.checkOrdersubId(wareHouseId);
        List<ReturnMinWareHousePo> returnMinWareHousePos = new ArrayList<ReturnMinWareHousePo>();
        for (WarehouseOrder wareHouseIdd : wareHouSpack) {
            List<SortingOrderPo> sortingOrderPos = new ArrayList<>();
            //用主备货表id查子订单id
            List<WarehousePackOrder> warehousePackOrders = warehousePackOrderMapper.selectWareHouseWareHouseId(wareHouseIdd.getId());
            //用备货主id查备货主表
            List<WarehouseOrderGoods> warehouseOrderGoods = warehouseOrderGoodsMapper.selectWareHouseOrderGoods(wareHouseIdd.getId());
            for (WarehouseOrderGoods warehouseOrderGoods1 : warehouseOrderGoods) {
                SortingOrderPo sortingOrderPo = new SortingOrderPo();
                sortingOrderPo.setMessage(warehouseOrderGoods1.getMessage());
                GoodsInfo goodsInfo = warehousePackOrderMapper.selectSkuGoodsInfo(warehouseOrderGoods1.getSku());
                sortingOrderPo.setGoodsName(goodsInfo.getGoodsName());
                sortingOrderPo.setId(warehouseOrderGoods1.getId());

                GoodsInfo goodsInfo1 =warehousePackOrderMapper.selectSkuGoodsInfo(warehouseOrderGoods1.getSku());
                sortingOrderPo.setImg(goodsInfo1.getPicture());
                String str_string = sortingOrderPo.getMessage().replaceAll("[^0-9]", "");//  \d 为正则表达式表示[0-9]数字
                Integer num =Integer.valueOf(str_string);
                GoodsStorePrice goodsStorePrice =warehouseOrder.selectPriceSku(warehouseOrderGoods1.getSku(),wareHouseIdd.getStoreId());
                BigDecimal e =new BigDecimal(goodsStorePrice.getCostPrice());
                BigDecimal t =new BigDecimal(100);
                // Double d= Double.valueOf(pricee*num);
                BigDecimal a =e.divide(t);
                BigDecimal b =new BigDecimal(num);
                BigDecimal d=a.multiply(b);
                sortingOrderPo.setTotalPrice(d);
                sortingOrderPos.add(sortingOrderPo);
            }
            ReturnMinWareHousePo returnMinWareHousePo = new ReturnMinWareHousePo();
            returnMinWareHousePo.setList(sortingOrderPos);
            returnMinWareHousePo.setWarehouseOrderNo(wareHouseIdd.getWarehouseOrderNo());
            returnMinWareHousePo.setCreateTime(wareHouseIdd.getCreateTime());
            returnMinWareHousePo.setId(wareHouseIdd.getId());
            returnMinWareHousePos.add(returnMinWareHousePo);

        }
        return returnMinWareHousePos;
    }

    //        分拣完成总按钮
    @Override
    public Integer packOrderDone(Long subId) {
        WarehousePackOrder warehousePackOrder=warehousePackOrderMapper.selectPackOrderOrderId(subId);

        warehousePackOrderMapper.updateOrderSubStatusPack(warehousePackOrder.getOrderId());
        warehousePackOrderMapper.packOrderDoneStatus(subId);
        return warehousePackOrderMapper.packOrderDone(subId);
    }

    @Override
    public  synchronized Integer packOrder(Long subId, Long staffId) {
        List<WarehousePackOrder> wa = warehousePackOrderGoodsMapper.wareHousePackOrderStatus(subId);
            for (WarehousePackOrder w : wa) {
                AssertExt.isTrue(w.getStatus().equals("PENDDING"), "此订单已经有人分拣了");
            }
        return warehousePackOrderMapper.packOrder(subId, staffId);
    }


    //    分拣员接单

    //分拣单子订单完成按钮
    @Override
    public Integer packOrderDoneStatus(Long subId) {



        return warehousePackOrderMapper.packOrderDoneStatus(subId);
    }
    //后台更改子表状态
    @Override
    public Integer updatePackOrderGoodsStatus(Long warehouseOrderPackGoodsId, String status,Long userId) {
        return warehousePackOrderMapper.updatePackOrderGoodsStatus(warehouseOrderPackGoodsId, status,userId);
    }
    //检查分拣表状态是否不为Done


    @Override
    public Object selectOrderPackOrderId(String orderNo) {
//        根据订单编号查询分拣表id
        List<WareHousePackOrderVo> warehousePackOrders = warehousePackOrderMapper.selectOrderPackOrderId(orderNo);
        List<WareHousePackOrderVo> wareHousePackOrderVoListM = new ArrayList<>();
        WareHousePackOrderVo wareHousePackOrderVo = new WareHousePackOrderVo();
        wareHousePackOrderVo.setOrderNo(warehousePackOrders.get(0).getOrderNo());
        wareHousePackOrderVo.setConsignee(warehousePackOrders.get(0).getConsignee());
        wareHousePackOrderVo.setConsigneePhone(warehousePackOrders.get(0).getConsigneePhone());
        List<WareHousePackOrderPo> wareHousePackOrderPosList = new ArrayList<>();
        for (WareHousePackOrderVo warehousePackOrder : warehousePackOrders) {
            Long id = warehousePackOrder.getId();
            List<WareHousePackOrderPo> wareHousePackOrderPos = warehousePackOrderMapper.WareHousePackOrderPo(id);
            for (WareHousePackOrderPo wareHousePackOrderPos1 : wareHousePackOrderPos) {
                wareHousePackOrderPosList.add(wareHousePackOrderPos1);
            }
        }
        wareHousePackOrderVo.setList(wareHousePackOrderPosList);


        return wareHousePackOrderVo;
    }

    //根据分拣员id查询分拣信息
    @Override
    public Object selectStaffId(Long staffId, Integer currentPage, Integer pageSize) {
        IPage<WarehousePackOrder> page = new Page<WarehousePackOrder>(currentPage, pageSize);
        IPage<WarehousePackOrder> wareHousePackOrderVos = warehousePackOrderMapper.selectStaffId(page, staffId);
        List<WareHousePackOrderVo> wareHousePackOrderPosList = new ArrayList<>();
        for (WarehousePackOrder wareHousePackOrderVos1 : wareHousePackOrderVos.getRecords()) {
            Long subId = wareHousePackOrderVos1.getOrderId();
            OrderSubInfo orderSubInfo = warehousePackOrderMapper.selectOrderSubId(subId);
            List<WareHousePackOrderPo> wareHousePackOrderVos2 = warehousePackOrderMapper.staffIdOrderId(subId);
            WareHousePackOrderVo wareHousePackOrderPos = new WareHousePackOrderVo();
            wareHousePackOrderPos.setOrderNo(orderSubInfo.getOrderNo());
            wareHousePackOrderPos.setConsignee(orderSubInfo.getConsignee());
            wareHousePackOrderPos.setConsigneePhone(orderSubInfo.getConsigneePhone());
            wareHousePackOrderPos.setList(wareHousePackOrderVos2);
            wareHousePackOrderPosList.add(wareHousePackOrderPos);
        }
        return wareHousePackOrderPosList;
    }

    @Override
    public Object selectPackOrderGoodsDoneTime(String startTime, String endTime, Integer currentPage, Integer pageSize) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //开始时间转换
        LocalDateTime ldt = LocalDateTime.parse(startTime, df);
        //结束时间转换
        LocalDateTime ldt2 = LocalDateTime.parse(endTime, df);
        List<WareHousePackOrderVo> wareHousePackOrderPosList = new ArrayList<>();
        IPage<WarehousePackOrder> page = new Page<WarehousePackOrder>(currentPage, pageSize);
        IPage<WarehousePackOrder> warehousePackOrders = warehousePackOrderMapper.selectPackOrderGoodsDoneTime(page, ldt, ldt2);
        for (WarehousePackOrder warehousePackOrders1 : warehousePackOrders.getRecords()) {
            Long subId = warehousePackOrders1.getOrderId();
            OrderSubInfo orderSubInfo = warehousePackOrderMapper.selectOrderSubId(subId);
            List<WareHousePackOrderPo> wareHousePackOrderVos2 = warehousePackOrderMapper.staffIdOrderId(subId);
            WareHousePackOrderVo wareHousePackOrderPos = new WareHousePackOrderVo();
            wareHousePackOrderPos.setOrderNo(orderSubInfo.getOrderNo());
            wareHousePackOrderPos.setConsignee(orderSubInfo.getConsignee());
            wareHousePackOrderPos.setConsigneePhone(orderSubInfo.getConsigneePhone());
            wareHousePackOrderPos.setList(wareHousePackOrderVos2);
            wareHousePackOrderPosList.add(wareHousePackOrderPos);
        }
        return wareHousePackOrderPosList;
    }

    @Override
    public Object selectPackOrderGoodsDoneStatus(String status, Integer currentPage, Integer pageSize) {
        List<WareHousePackOrderVo> wareHousePackOrderPosList = new ArrayList<>();
        IPage<WarehousePackOrder> page = new Page<WarehousePackOrder>(currentPage, pageSize);
        IPage<WarehousePackOrder> warehousePackOrders = warehousePackOrderMapper.selectPackOrderGoodsDoneStatus(page, status);

        for (WarehousePackOrder warehousePackOrders1 : warehousePackOrders.getRecords()) {
            Long subId = warehousePackOrders1.getOrderId();
            OrderSubInfo orderSubInfo = warehousePackOrderMapper.selectOrderSubId(subId);
            List<WareHousePackOrderPo> wareHousePackOrderVos2 = warehousePackOrderMapper.staffIdOrderId(subId);
            WareHousePackOrderVo wareHousePackOrderPos = new WareHousePackOrderVo();
            wareHousePackOrderPos.setOrderNo(orderSubInfo.getOrderNo());
            wareHousePackOrderPos.setConsignee(orderSubInfo.getConsignee());
            wareHousePackOrderPos.setConsigneePhone(orderSubInfo.getConsigneePhone());
            wareHousePackOrderPos.setList(wareHousePackOrderVos2);
            wareHousePackOrderPosList.add(wareHousePackOrderPos);
        }
        return wareHousePackOrderPosList;

    }

//后台按条件查询分拣单
    @Override
    public Object selectPack(PackPo packPo) {
        String orderNo = packPo.getOrderNo();
        if (StringUtils.isBlank(orderNo)) orderNo = null;
        Long staffId = packPo.getStaffId();
        Long storeId = packPo.getStoreId();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (packPo.getTime() != null) {
            LocalDateTime[] packPoTime = packPo.getTime();
            LocalDateTime st = packPoTime[0];
            LocalDateTime st2 = packPoTime[1];
            startTime = st;
            endTime = st2;
        }
        String status = packPo.getStatus();
        if (StringUtils.isBlank(status)) status = null;
        Integer currentPage = packPo.getCurrentPage();
        Integer pageSize = packPo.getPageSize();
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        IPage<WarehousePackOrder> page = new Page<WarehousePackOrder>(currentPage, pageSize);
        List<WareHousePackOrderVo> wareHousePackOrderPosList = new ArrayList<>();
        IPage<WarehousePackOrder> warehousePackOrderIPage = warehousePackOrderMapper.selectPack(page, orderNo, staffId, storeId, startTime, endTime, status);
        for (WarehousePackOrder warehousePackOrders1 : warehousePackOrderIPage.getRecords()) {
            Long subId = warehousePackOrders1.getOrderId();
            Long packId = warehousePackOrders1.getId();
            OrderSubInfo orderSubInfo = warehousePackOrderMapper.selectOrderSubId(subId);
            List<WareHousePackOrderPo> wareHousePackOrderVos2 = warehousePackOrderMapper.staffIdOrderId(packId);
            WareHousePackOrderVo wareHousePackOrderPos = new WareHousePackOrderVo();
            wareHousePackOrderPos.setRemark(warehousePackOrders1.getRemark());
            for (WareHousePackOrderPo warehousePackOrderIPage1 : wareHousePackOrderVos2) {

                GoodsInfo goodsInfo = warehousePackOrderMapper.selectSkuGoodsInfo(warehousePackOrderIPage1.getSku());
                warehousePackOrderIPage1.setImg(goodsInfo.getPicture());
                if (warehousePackOrderIPage1.getWareHousePackOrderGoodsStatus() != null) {
                    if (warehousePackOrderIPage1.getWareHousePackOrderGoodsStatus().equals("DONE")) {
                        warehousePackOrderIPage1.setWareHousePackOrderGoodsStatus("完成");
                    }
                    if (warehousePackOrderIPage1.getWareHousePackOrderGoodsStatus().equals("SHORTAGE")) {
                        warehousePackOrderIPage1.setWareHousePackOrderGoodsStatus("缺货");
                    }
                }
                if (warehousePackOrderIPage1.getWareHousePackOrderGoodsStatus() == null) {
                    warehousePackOrderIPage1.setWareHousePackOrderGoodsStatus("还没有备货");
                }
                //查找该sku是否组合商品
                WarehouseGoods warehouseGoods = new WarehouseGoods();
                OrderSubInfo oIn = warehousePackOrderGoodsMapper.selectOrderInfoNo(subId);
                storeId=warehousePackOrderIPage1.getStoreId();

                //如果不是组合商品走正常流程
                if (goodsInfo.getGoodsType() == 2) {
                    List<OrderGoodsKuai> shenqi =new ArrayList<>();
                    //如果是组合商品的话（获得组合商品的子商品信息）
                    GoodsPriceInfoVO goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(storeId, oIn.getAddressId(), warehousePackOrderIPage1.getSku());
                    //删除当前快捷菜的sku对象
//                        goodsList.remove(goods);
                    //查子商品信息
                    for (GoodsPriceInfoVO goodsPriceInfoVO1 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                        //查子商品的冗余单位(斤)
                        for (GoodsAttributeValueVO goodsAttributeValueVO : goodsPriceInfoVO1.getGoodsAttributeValueVOS()) {
                            if (goodsAttributeValueVO.getIsSale() == 1) {
                                //拿到子商品的冗余单位
                                GoodsInfo goodsInfo1 = warehousePackOrderMapper.selectSkuGoodsInfo(goodsPriceInfoVO1.getSku());
                                OrderGoodsKuai orderGoods1 = new OrderGoodsKuai();
                                orderGoods1.setSku(goodsPriceInfoVO1.getSku());
                                orderGoods1.setPrice(goodsPriceInfoVO1.getPrice().intValue());
                                //  orderGoods.setId(goods.getId());
                                // orderGoods.setOrderSubId(order.getId());
                                //大佬说暂时快捷菜的子商品都是一个
                                orderGoods1.setNum(1*warehousePackOrderIPage1.getNum().intValue());
                                orderGoods1.setImg(goodsInfo1.getPicture());

                                orderGoods1.setGoodsName(goodsInfo1.getGoodsName());
                                //orderGoods1.setCreateTime(order.getCreateTime());
                                orderGoods1.setUnit(goodsAttributeValueVO.getAttrKeyName());
                                shenqi.add(orderGoods1);
                            }
                        }
                    }

//                    WareHousePackOrderVo wareHousePackOrderPos1 = new WareHousePackOrderVo();

                    wareHousePackOrderPos.setId(warehousePackOrders1.getId());
                    wareHousePackOrderPos.setOrderNo(orderSubInfo.getOrderNo());
                    if (wareHousePackOrderVos2.get(0) != null) {
                        if (wareHousePackOrderVos2.get(0).getWareHousePackOrderStatus() != null) {
                            if (wareHousePackOrderVos2.get(0).getWareHousePackOrderStatus().equals("PENDDING")) {
                                wareHousePackOrderPos.setStatus("等待分拣中");
                            } else if (wareHousePackOrderVos2.get(0).getWareHousePackOrderStatus().equals("RECEIVED")) {
                                wareHousePackOrderPos.setStatus("正在分拣中");
                            } else {
                                wareHousePackOrderPos.setStatus("分拣完成");
                            }
                        } else {
                            wareHousePackOrderPos.setStatus("还没有备货");
                        }
                    } else {
                        wareHousePackOrderPos.setStatus("还没有备货");
                    }
                    if (warehousePackOrders1.getWarehouseStaffId() != null) {
                        WarehouseStaff warehouseStaff = warehousePackOrderMapper.selectStaffName(warehousePackOrders1.getWarehouseStaffId());
                        wareHousePackOrderPos.setConsignee(warehouseStaff.getStockName());
                        wareHousePackOrderPos.setConsigneePhone(warehouseStaff.getPhone());
                    } else {
                        wareHousePackOrderPos.setConsignee("暂时没有分拣员");
                        wareHousePackOrderPos.setConsigneePhone("");
                    }
                    if (wareHousePackOrderVos2.get(0).getDoneTime() != null) {
                        wareHousePackOrderPos.setDoneTime(wareHousePackOrderVos2.get(0).getDoneTime());
                    }
                    warehousePackOrderIPage1.setKuaijie(shenqi);
                    wareHousePackOrderPos.setList(wareHousePackOrderVos2);


                }else {

                    wareHousePackOrderPos.setId(warehousePackOrders1.getId());
                    wareHousePackOrderPos.setOrderNo(orderSubInfo.getOrderNo());
                    if (wareHousePackOrderVos2.get(0) != null) {
                        if (wareHousePackOrderVos2.get(0).getWareHousePackOrderStatus() != null) {
                            if (wareHousePackOrderVos2.get(0).getWareHousePackOrderStatus().equals("PENDDING")) {
                                wareHousePackOrderPos.setStatus("等待分拣中");
                            } else if (wareHousePackOrderVos2.get(0).getWareHousePackOrderStatus().equals("RECEIVED")) {
                                wareHousePackOrderPos.setStatus("正在分拣中");
                            } else {
                                wareHousePackOrderPos.setStatus("分拣完成");
                            }
                        } else {
                            wareHousePackOrderPos.setStatus("还没有备货");
                        }
                    } else {
                        wareHousePackOrderPos.setStatus("还没有备货");
                    }
                    if (warehousePackOrders1.getWarehouseStaffId() != null) {
                        WarehouseStaff warehouseStaff = warehousePackOrderMapper.selectStaffName(warehousePackOrders1.getWarehouseStaffId());
                        wareHousePackOrderPos.setConsignee(warehouseStaff.getStockName());
                        wareHousePackOrderPos.setConsigneePhone(warehouseStaff.getPhone());
                    } else {
                        wareHousePackOrderPos.setConsignee("暂时没有分拣员");
                        wareHousePackOrderPos.setConsigneePhone("");
                    }
                    if (wareHousePackOrderVos2.get(0).getDoneTime() != null) {
                        wareHousePackOrderPos.setDoneTime(wareHousePackOrderVos2.get(0).getDoneTime());
                    }
                    wareHousePackOrderPos.setList(wareHousePackOrderVos2);

                }
                }

            wareHousePackOrderPosList.add(wareHousePackOrderPos);
        }

        return wareHousePackOrderPosList;
    }

    @Override
    public Integer updatePackOrderStatus(Long packId, String status,Long userId) {
        return warehousePackOrderMapper.updatePackOrderStatus(packId, status,userId);
    }


    //后台分拣备货人员更改资料
    @Override
    public Integer updateStockPackName(StockPackPo stockPackPo) {
        String rexName="^[\\u4e00-\\u9fa5]{0,}$replace";
        String rexPhone="^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";


        String stockPackName = stockPackPo.getStockPackName();

        AssertExt.isFalse(stockPackName.matches(rexName),"姓名不合法");



        String phone = stockPackPo.getPhone();

        AssertExt.isFalse(phone.matches(rexPhone),"不是正确的手机格式");


        Long id = stockPackPo.getId();
        String idNumber = stockPackPo.getIdNumber();
        String idCardImg = stockPackPo.getIdCardImg();
        if (StringUtils.isBlank(stockPackName)) stockPackName = null;
        if (StringUtils.isBlank(phone)) phone = null;
        if (StringUtils.isBlank(idNumber)) idNumber = null;
        if (StringUtils.isBlank(idCardImg)) idCardImg = null;

        return warehouseStaffMapper.updateStockPackName(stockPackName, phone, idNumber, idCardImg, id);
    }

    @Override
    public Integer updateWarehousePackOrderRemark(Long subId, String remark) {

        if (remark.equals("null")) remark = null;


        return warehousePackOrderMapper.updateWarehousePackOrderRemark(remark,subId);
    }

    @Override
    public Integer updateWarehousePackOrderShortage(Long subId) {
        return warehousePackOrderMapper.updateWarehousePackOrderShortage(subId);
    }


}
