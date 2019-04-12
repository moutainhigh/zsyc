package com.zsyc.warehouse.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
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
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.warehouse.BackEndVo.WareHouseOrderPo;
import com.zsyc.warehouse.BackEndVo.WareHouseOrderVo;
import com.zsyc.warehouse.entity.*;
import com.zsyc.warehouse.mapper.WarehouseOrderGoodsMapper;
import com.zsyc.warehouse.mapper.WarehouseOrderMapper;
import com.zsyc.warehouse.mapper.WarehousePackOrderGoodsMapper;
import com.zsyc.warehouse.mapper.WarehousePackOrderMapper;
import com.zsyc.warehouse.po.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class IWarehouseOrderServiceImpl implements WarehouseOrderService {
    @Autowired
    public WarehouseOrderMapper warehouseOrdemapper;
    @Autowired
    public WarehouseOrderGoodsMapper warehouseOrdeGoodsmapper;
    @Autowired
    public WarehousePackOrderMapper warehousePackOrderMapper;
    @Autowired
    public WarehousePackOrderGoodsMapper warehousePackOrderGoodsMapper;
    @Autowired
    public GoodsStorePriceService goodsStorePriceService;
    @Autowired
    public WarehousePackOrdersGoodsService warehousePackOrdersGoodsService;


    @Override
//    查找总体的店铺ID
    public List<OrderSubInfo> selectOrdersubinfo(Long storeId) {
        return warehouseOrdemapper.selectOrdersubinfo(storeId);
    }

    //    根据子订单id获得所有订单快照表的数据(sku)
    @Override
    public List<WarehousePo> selectOrderGoods(Long storeId) {
        return warehouseOrdemapper.selectOrderGoods(storeId);
    }

    @Override
    public int insert(WarehouseOrder warehouseOrder) {


        return warehouseOrdemapper.insert(warehouseOrder);
    }


    //根据店铺ID 和 提前时间生成备货单（定时调用或者手动生成）
    @Override
    public void createWarehouseByStoreId(Long storeId, Integer minute) {
        if (storeId == 156 || storeId == 155) {
            //查询子订单  子订单缺少一个状态：未生成备货单
            List<OrderSubInfo> orders = warehouseOrdemapper.selectOrderGoodsPresetTime(storeId, minute);
            Map<String, List<OrderGoods>> goodsMap = null;
            LocalDateTime time = null;
            Map<String, Long> skuMap = null;
            if (orders != null && orders.size() > 0) {
                time = LocalDateTime.now();
                goodsMap = new HashMap<String, List<OrderGoods>>();
                skuMap = new HashMap<>();

                WarehouseOrder who = new WarehouseOrder();
                who.setCreateTime(time);
                who.setStatus("READY");

                String num = String.valueOf(time.toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
                who.setWarehouseOrderNo(num);
                who.setStoreId(storeId);
                //1.插入备货主表
                warehouseOrdemapper.insert(who);

                //备货主表ID
                Long warehouseId = who.getId();

//          Map<sku,List<快照表商品>> map
                List<OrderGoods> shenqi = new ArrayList();
                for (OrderSubInfo order : orders) {
                    //根据子订单编号查询商品快照信息
                    List<OrderGoods> goodsList = warehouseOrdemapper.selectOrderGoodsByOrderId(order.getId());
                    for (OrderGoods goods : goodsList) {

                        //查找该sku是否组合商品
                        GoodsInfo goodsInfo = warehouseOrdemapper.selectSKuZu(goods.getSku());
                        //如果不是组合商品走正常流程
                        if (goodsInfo.getGoodsType() == 2) {
                            //如果是组合商品的话（获得组合商品的子商品信息）
                            GoodsPriceInfoVO goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(storeId, order.getAddressId(), goods.getSku());
                            //删除当前快捷菜的sku对象
//                        goodsList.remove(goods);
                            //查子商品信息
                            for (GoodsPriceInfoVO goodsPriceInfoVO1 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                                //查子商品的冗余单位(斤)
                                for (GoodsAttributeValueVO goodsAttributeValueVO : goodsPriceInfoVO1.getGoodsAttributeValueVOS()) {
                                    if (goodsAttributeValueVO.getIsSale() == 1) {
                                        //拿到子商品的冗余单位
                                        OrderGoods orderGoods = new OrderGoods();
                                        orderGoods.setSku(goodsPriceInfoVO1.getSku());
                                        orderGoods.setPrice(goodsPriceInfoVO1.getPrice().intValue());
                                        orderGoods.setId(goods.getId());
                                        orderGoods.setOrderSubId(order.getId());
                                        orderGoods.setNum(1);
                                        orderGoods.setCreateTime(order.getCreateTime());
                                        orderGoods.setUnit(goodsAttributeValueVO.getAttrKeyName());
                                        shenqi.add(orderGoods);
                                    }
                                }
                            }
                        } else {
                            shenqi.add(goods);
                        }
                    }

                }
                for (OrderGoods goods : shenqi) {
                    if (goodsMap.containsKey(goods.getSku())) {
                        goodsMap.get(goods.getSku()).add(goods);
                    } else {
                        List<OrderGoods> goodss = new ArrayList<OrderGoods>();
                        goodss.add(goods);
                        goodsMap.put(goods.getSku(), goodss);
                    }
                }


                Iterator<Map.Entry<String, List<OrderGoods>>> iterator = goodsMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, List<OrderGoods>> entry = iterator.next();
                    List<OrderGoods> goodsList = entry.getValue();
                    WarehouseOrderGoods warehouseOrderGoods = new WarehouseOrderGoods();
                    warehouseOrderGoods.setSku(entry.getKey());
                    warehouseOrderGoods.setMessage(splitGoods(goodsList));
                    warehouseOrderGoods.setCreateTime(time);
                    warehouseOrderGoods.setWarehouseOrderId(warehouseId);
                    warehouseOrderGoods.setIsDel(0);
                    //3.插入备货商品子表
                    warehouseOrdeGoodsmapper.insert(warehouseOrderGoods);
                    //备货子表id
                    Long whoId = warehouseOrderGoods.getId();
                    skuMap.put(entry.getKey(), whoId);
//                WarehousePackOrderGoods packGoods = null;
//                for (OrderGoods goods : goodsList) {
//
//                	packGoods = new WarehousePackOrderGoods();
//
//                    packGoods.setWarehousePackOrderId(whoId);
//                    packGoods.setOrderGoodsId(goods.getOrderSubId());
//                    packGoods.setNum(goods.getNum());
//                    packGoods.setUnit(goods.getUnit());
//                    packGoods.setWarehouseOrderGoodsId(whoId);
//
//                    //4.插入 备货订单商品中间表（分拣订单商品表）
//                    warehousePackOrderGoodsMapper.insert(packGoods);
//                }

                }

                //生成分拣表
                for (OrderSubInfo order : orders) {

                    WarehousePackOrder wpo = new WarehousePackOrder();
                    wpo.setOrderId(order.getId());
                    wpo.setStoreId(storeId);
                    wpo.setWarehouseOrderId(warehouseId);
                    wpo.setCreateTime(time);
                    //2.插入备货订单中间表（分拣订单表）
                    warehousePackOrderMapper.insert(wpo);
                    Long wpoId = wpo.getId();

                    //根据子订单编号查询商品快照信息
                    List<OrderGoods> goodsList = warehouseOrdemapper.selectOrderGoodsByOrderId(order.getId());
                    WarehousePackOrderGoods packGoods = null;
                    for (OrderGoods goods : goodsList) {

                        packGoods = new WarehousePackOrderGoods();
                        packGoods.setWarehousePackOrderId(wpoId);
                        packGoods.setOrderGoodsId(goods.getId());
                        packGoods.setNum(goods.getNum());
                        if(goods.getUnit()==null){
                            packGoods.setUnit("份");
                        }else {
                            packGoods.setUnit(goods.getUnit());
                        }
                        packGoods.setCreateTime(time);
                        packGoods.setWarehouseOrderGoodsId(skuMap.get(goods.getSku()));

                        //4.插入 备货订单商品中间表（分拣订单商品表）
                        warehousePackOrderGoodsMapper.insert(packGoods);


                    }
                    //更改子订单状态为已生成备货信息
                    warehouseOrdemapper.updateBeenStock(order.getId());
                }

            }

        }

    }

    @Override
    public Object returnMinWareHouse(Long storeId) {

        List<WarehouseOrderGoods> check = selectWareHouseOrderGoods(storeId);
        NewWarehouse newWarehouse = new NewWarehouse();

        List<ReturnMinWareHousePo> returnMinWareHousePoList = newWarehouse.getReturnMinWareHousePo();
        for (WarehouseOrderGoods c : check) {
            ReturnMinWareHousePo returnMinWareHousePo = new ReturnMinWareHousePo();
            Long checkId = c.getId();

            List<SortingOrderPo> min = miniWareHouse(checkId);
//            for(SortingOrderPo m:min){
//                if(map.containsKey(m.getWarehouseOrderId())){
//                    map.get(m.getWarehouseOrderId()).add(m);
//                    rem.setWarehouseOrderNo(m.getWarehouseOrderNo());
//                    retruna.add(rem);
//                }else{
//                    List<SortingOrderPo> gg=new ArrayList<SortingOrderPo>();
//                    gg.add(m);
//                    map.put(m.getWarehouseOrderId(),gg);
//                    rem.setWarehouseOrderNo(m.getWarehouseOrderNo());
//                    retruna1.add(rem);
//                }
//            }
            for(SortingOrderPo m:min){
                GoodsInfo goodsInfo1 =warehousePackOrderMapper.selectSkuGoodsInfo(m.getSku());
                m.setImg(goodsInfo1.getPicture());
                String str_string = m.getMessage().replaceAll("[^0-9]", "");//  \d 为正则表达式表示[0-9]数字
                Integer num =Integer.valueOf(str_string);
                GoodsStorePrice goodsStorePrice =warehouseOrdemapper.selectPriceSku(m.getSku(),storeId);
                BigDecimal e =new BigDecimal(goodsStorePrice.getCostPrice());
                BigDecimal t =new BigDecimal(100);
               // Double d= Double.valueOf(pricee*num);
                BigDecimal a =e.divide(t);
                BigDecimal b =new BigDecimal(num);
                BigDecimal d=a.multiply(b);
                m.setTotalPrice(d);
            }

            if (null != min && min.size() > 0) {
                returnMinWareHousePo.getList().addAll(min);
                returnMinWareHousePo.setWarehouseOrderNo(min.get(0).getWarehouseOrderNo());
                returnMinWareHousePo.setCreateTime(min.get(0).getCreateTime());
                returnMinWareHousePo.setId(min.get(0).getWarehouseOrderId());
            }

            returnMinWareHousePoList.add(returnMinWareHousePo);


//            for(SortingOrderPo m:min){
//                mmm.getList().add(m);
//
//                newWarehouse.getReturnMinWareHousePo().add(mmm);
//            }
            //  re.add(min);


        }


        return newWarehouse;

    }

    //显示已完成的备货单
    @Override
    public Object selectWareHouseAll(Long storeId, Long wareHouseId) {
        List<WarehouseOrder> warehouseOrders= warehouseOrdemapper.selectWareHouseAll(storeId, wareHouseId);
        List<ReturnMinWareHousePo> objects=new ArrayList<>();
        for(WarehouseOrder wareHouseId1:warehouseOrders){
            ReturnMinWareHouseVo returnMinWareHouseVo= new ReturnMinWareHouseVo();
            ReturnMinWareHousePo q= warehousePackOrdersGoodsService.checkGoodsinfo(wareHouseId1.getId());
            //returnMinWareHouseVo.setList(q);
            objects.add(q);
        }


        return objects;
    }


    //定时执行 一次生成4个表的记录根据店铺ID生成

    @Override
    public Object selectAttKeyAll(String sku) {

        List<BackendWarehouseOrder> key = warehouseOrdemapper.selectAttrKey(sku);
        for (BackendWarehouseOrder k : key) {
            String str = "";
            str = k.getAttrValueName() + k.getAttrKeyName();
            k.setAttrValue(str);
        }

        return key;
    }


    @Override
    public Object WareHousemap(Long storeIdtest) {

        //1.根据店铺ID获取当前店铺的所有预约时间或者完成时间接近的子订单
//    	 如果子订单个数大于0 （插入备货表记录1条）
        Map<String, List<OrderGoods>> goodsMap = new HashMap<String, List<OrderGoods>>();
        Long warehouseOrderId = 0L;
        List<OrderSubInfo> stoid = warehouseOrdemapper.selectOrdersubinfo(storeIdtest);
        if (stoid != null && stoid.size() > 0) {
            LocalDateTime time = LocalDateTime.now();
            String num = String.valueOf(time.toInstant(ZoneOffset.ofHours(8)).toEpochMilli());

            WarehouseOrder who = new WarehouseOrder();
            who.setCreateTime(time);
            who.setStatus("READY");


            who.setWarehouseOrderNo(num);
            who.setStoreId(storeIdtest);
            //1.插入备货表
            warehouseOrdemapper.insert(who);

            //备货主表ID
            Long warehouseId = who.getId();

//          Map<sku,List<快照表商品>> map
            List<OrderGoods> shenqi = new ArrayList();
            for (OrderSubInfo order : stoid) {
                //根据子订单编号查询商品快照信息
                List<OrderGoods> goodsList = warehouseOrdemapper.selectOrderGoodsByOrderId(order.getId());
                WarehousePackOrder wpo = new WarehousePackOrder();
                wpo.setOrderId(order.getId());
                wpo.setStoreId(storeIdtest);
                wpo.setWarehouseOrderId(warehouseId);
                //2.插入分拣主表
                warehousePackOrderMapper.insert(wpo);

                WarehousePackOrderGoods packGoods = new WarehousePackOrderGoods();
                for (OrderGoods goods : goodsList) {
                    packGoods.setOrderGoodsId(goods.getId());
                    packGoods.setNum(goods.getNum());
                    packGoods.setUnit(goods.getUnit());
                    packGoods.setWarehouseOrderGoodsId(wpo.getId());
                    //4.插入 备货订单商品中间表
                    warehousePackOrderGoodsMapper.insert(packGoods);
                }


                for (OrderGoods goods : goodsList) {

                    //查找该sku是否组合商品
                    GoodsInfo goodsInfo = warehouseOrdemapper.selectSKuZu(goods.getSku());
                    //如果不是组合商品走正常流程
                    if (goodsInfo.getGoodsType() == 2) {
                        //如果是组合商品的话（获得组合商品的子商品信息）
                        GoodsPriceInfoVO goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(storeIdtest, order.getAddressId(), goods.getSku());
                        //删除当前快捷菜的sku对象
//                        goodsList.remove(goods);
                        //查子商品信息
                        for (GoodsPriceInfoVO goodsPriceInfoVO1 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                            //查子商品的冗余单位(斤)
                            for (GoodsAttributeValueVO goodsAttributeValueVO : goodsPriceInfoVO1.getGoodsAttributeValueVOS()) {
                                if (goodsAttributeValueVO.getIsSale() == 1) {
                                    //拿到子商品的冗余单位
                                    OrderGoods orderGoods = new OrderGoods();
                                    orderGoods.setSku(goodsPriceInfoVO1.getSku());
                                    orderGoods.setPrice(goodsPriceInfoVO1.getPrice().intValue());
                                    orderGoods.setId(goods.getId());
                                    orderGoods.setOrderSubId(order.getId());
                                    orderGoods.setNum(1);
                                    orderGoods.setCreateTime(order.getCreateTime());
                                    orderGoods.setUnit(goodsAttributeValueVO.getAttrKeyName());
                                    shenqi.add(orderGoods);
                                }
                            }
                        }
                    } else {
                        shenqi.add(goods);
                    }


                }
                //修改订单表状态  已生成备货单
            }
            for (OrderGoods goods : shenqi) {
                if (goodsMap.containsKey(goods.getSku())) {
                    goodsMap.get(goods.getSku()).add(goods);
                } else {
                    List<OrderGoods> goodss = new ArrayList<OrderGoods>();
                    goodss.add(goods);
                    goodsMap.put(goods.getSku(), goodss);
                }
            }


            Iterator<Map.Entry<String, List<OrderGoods>>> iterator = goodsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<OrderGoods>> entry = iterator.next();
                String sku = entry.getKey();
                List<OrderGoods> goodsList = entry.getValue();
                WarehouseOrderGoods warehouseOrderGoods = new WarehouseOrderGoods();
                warehouseOrderGoods.setSku(entry.getKey());
                warehouseOrderGoods.setMessage(splitGoods(goodsList));
                warehouseOrderGoods.setCreateTime(time);
                warehouseOrderGoods.setWarehouseOrderId(warehouseId);
                //3.插入备货子表
                warehouseOrdeGoodsmapper.insert(warehouseOrderGoods);


            }

        }

        return goodsMap;

    }

    //}


    //    接单后根据备货id更改主备货单（备货员id）(备货状态)
    @Override
    public synchronized Integer updateWareHouseStatus(Long warehouseOrderId, Long staffId) {
        WarehouseOrder wa = warehouseOrdemapper.wareHouseOrderStatus(warehouseOrderId);

         AssertExt.isTrue(wa.getStatus().equals("READY"), "已经有人备货了");


        LocalDateTime receiveTime = LocalDateTime.now();


        return warehouseOrdemapper.updateWareHouseStatus(warehouseOrderId, staffId, receiveTime);


    }

    //    更新子备货单status状态（0是缺货1是完成）
    @Override
    public Integer updateOrderSubStauts(String stauts, Integer id) {

        return warehouseOrdemapper.updateOrderSubStauts(stauts, id);
    }

    //    获得备货单列表
    @Override
    public List<OrderSubInfo> ordersubID() {
        return warehouseOrdemapper.ordersubID();
    }

    //    按照备货单查找具体数据
    @Override
    public List<SortingOrderPo> miniWareHouse(Long checkId) {


        return warehouseOrdemapper.miniWareHouse(checkId);
    }

    @Override
    public List<WarehousePo> selectWarehouse() {
        return null;
    }
    //    查找准备状态的备货单


    @Override
    public List<WarehouseOrderGoods> selectWareHouseOrderGoods(Long storeId) {


        return warehouseOrdemapper.selectWareHouseOrderGoods(storeId);
    }

    //    后台查询已备货列表
    @Override
    public List<BackendWarehouseOrder> selectReadyWareHouseDone() {
        List<BackendWarehouseOrder> bb = new ArrayList<>();
        List<BackendWarehouseOrder> end = warehouseOrdemapper.selectReadyWareHouseDone();
        for (BackendWarehouseOrder e : end) {
            String sku = e.getSku();
            String str = "";
            List<BackendWarehouseOrder> key = warehouseOrdemapper.selectAttrKey(sku);
            for (BackendWarehouseOrder k : key) {
                if (str.equals("")) {
                    str = k.getAttrValueName() + k.getAttrKeyName();
                } else {
                    str = str + "/" + k.getAttrValueName() + k.getAttrKeyName();
                }
            }
            e.setAttrValue(str);
            bb.add(e);
        }

        return bb;
    }

    //后台根据备货单号查备货列表
    @Override
    public Object warehouseOrderGoodsOrderNo(String warehouseOrderNo) {

        List<WareHouseOrderVo> wareHouseOrderVos = warehouseOrdemapper.warehouseOrderGoodsOrderNo(warehouseOrderNo);
        WareHouseOrderPo wareHouseOrderPo1 = new WareHouseOrderPo();
        wareHouseOrderPo1.setWarehouseOrderNo(wareHouseOrderVos.get(0).getWarehouseOrderNo());
        wareHouseOrderPo1.setWareHouseCreateTime(wareHouseOrderVos.get(0).getCreateTime());
        wareHouseOrderPo1.setWareHouseOrderVo(wareHouseOrderVos);


        return wareHouseOrderPo1;
    }

    @Override
    public Object warehouseOrderGoodsCreateTime(String startTime, String endTime, Integer currentPage, Integer pageSize) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //开始时间转换
        LocalDateTime ldt = LocalDateTime.parse(startTime, df);
        //结束时间转换
        LocalDateTime ldt2 = LocalDateTime.parse(endTime, df);
        IPage<WarehouseOrder> page = new Page<WarehouseOrder>(currentPage, pageSize);
        System.out.println("String类型的时间转成LocalDateTime：" + ldt);

        List<WareHouseOrderPo> wareHouseOrderPo1 = new ArrayList<>();


        IPage<WarehouseOrder> wareHouseOrderVos = warehouseOrdemapper.warehouseOrderGoodsCreateTime(page, ldt, ldt2);
        for (WarehouseOrder Warehouse : wareHouseOrderVos.getRecords()) {
            Long id = Warehouse.getId();
            List<WareHouseOrderVo> wareHouseOrderVos1 = warehouseOrdemapper.warehouseOrderGoodsCreateTimeAll(id);
            WareHouseOrderPo wareHouseOrderPo = new WareHouseOrderPo();
            wareHouseOrderPo.setWarehouseOrderNo(wareHouseOrderVos1.get(0).getWarehouseOrderNo());
            wareHouseOrderPo.setWareHouseCreateTime(wareHouseOrderVos1.get(0).getCreateTime());
            wareHouseOrderPo.setWareHouseOrderVo(wareHouseOrderVos1);
            wareHouseOrderPo1.add(wareHouseOrderPo);

        }


        return wareHouseOrderPo1;
    }

    //    后台查找门店下拉
    @Override
    public List<StoreInfo> selectStoreInfo() {
        return warehouseOrdemapper.selectStoreInfo();
    }

    @Override
    public Object selectStock(StockPo stockPo) {

        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        if (stockPo.getTime()!= null) {
            LocalDateTime ldt=stockPo.getTime()[0];
            LocalDateTime ldt2=stockPo.getTime()[1];
            startTime = ldt;
            endTime = ldt2;
        }

        List<WareHouseOrderPo> wareHouseOrderPo1 = new ArrayList<>();


        Long storeId = stockPo.getStoreId();

        if(storeId==null){
            storeId=0L;
        }

        String status = stockPo.getStatus();
        if (StringUtils.isBlank(status)) status = null;
        Long staffId = stockPo.getStaffId();


        String warehouseOrderNo = stockPo.getWarehouseOrderNo();
        if (StringUtils.isBlank(warehouseOrderNo)) warehouseOrderNo = null;
        Integer currentPage = stockPo.getCurrentPage();
        Integer pageSize = stockPo.getPageSize();
        if(currentPage==null){
            currentPage=1;
        }
        if(pageSize==null){
            pageSize=10;
        }

        IPage<WarehouseOrder> page = new Page<WarehouseOrder>(currentPage, pageSize);


        IPage<WarehouseOrder> warehouseOrders = warehouseOrdemapper.selectStock(page, storeId, status, staffId, startTime, endTime, warehouseOrderNo);
        for (WarehouseOrder warehouseOrder : warehouseOrders.getRecords()) {
            Long id = warehouseOrder.getId();
            List<WareHouseOrderVo> wareHouseOrderVos = warehouseOrdemapper.warehouseOrderGoodsCreateTimeAll(id);


            WareHouseOrderPo wareHouseOrderPo = new WareHouseOrderPo();
            wareHouseOrderPo.setRemark(wareHouseOrderVos.get(0).getRemark());
            wareHouseOrderPo.setStatus(wareHouseOrderVos.get(0).getWareHouseStatus());
            wareHouseOrderPo.setWarehouseOrderNo(wareHouseOrderVos.get(0).getWarehouseOrderNo());
            wareHouseOrderPo.setWareHouseCreateTime(wareHouseOrderVos.get(0).getCreateTime());
            wareHouseOrderPo.setWareHouseOrderVo(wareHouseOrderVos);

            wareHouseOrderPo.setWareHouseOrderid(wareHouseOrderVos.get(0).getWareHouseOrderid());
            if (wareHouseOrderVos.get(0).getWarehouseStaffId() != null) {
                Long staffId1 = wareHouseOrderVos.get(0).getWarehouseStaffId();
                WarehouseStaff warehouseStaff = warehouseOrdemapper.selectStaffName(staffId1);
                if (warehouseStaff.getStockType().equals("STOCK")) {
                    wareHouseOrderPo.setStaffName(warehouseStaff.getStockName());
                    wareHouseOrderPo.setStaffPhone(warehouseStaff.getPhone());
                } else {
                    wareHouseOrderPo.setStaffName("暂时没有备货员备货");
                }
            } else {
                wareHouseOrderPo.setStaffName("暂时没有备货员备货");
            }

            if (wareHouseOrderPo.getStatus().equals("DONE")) {
                wareHouseOrderPo.setStatus("备货完成");
            } else if (wareHouseOrderPo.getStatus().equals("READY")) {
                wareHouseOrderPo.setStatus("等待备货");
            } else {
                wareHouseOrderPo.setStatus("正在备货中");
            }
            wareHouseOrderPo1.add(wareHouseOrderPo);
        }
        return wareHouseOrderPo1;
    }

    //    根据门店id查找备货列表
    @Override
    public Object warehouseOrderGoodsStoreIdAll(Long storeId, Integer currentPage, Integer pageSize) {

        IPage<WarehouseOrder> page = new Page<WarehouseOrder>(currentPage, pageSize);
        IPage<WarehouseOrder> warehouseOrders = warehouseOrdemapper.selectStoreId(page, storeId);
        List<WareHouseOrderPo> wareHouseOrderPo1 = new ArrayList<>();
        for (WarehouseOrder wareHouseOrderVos1 : warehouseOrders.getRecords()) {
            Long id = wareHouseOrderVos1.getId();
            List<WareHouseOrderVo> wareHouseOrderVos = warehouseOrdemapper.warehouseOrderGoodsCreateTimeAll(id);
            WareHouseOrderPo wareHouseOrderPo = new WareHouseOrderPo();
            wareHouseOrderPo.setWarehouseOrderNo(wareHouseOrderVos.get(0).getWarehouseOrderNo());
            wareHouseOrderPo.setWareHouseCreateTime(wareHouseOrderVos.get(0).getCreateTime());
            wareHouseOrderPo.setWareHouseOrderVo(wareHouseOrderVos);
            wareHouseOrderPo1.add(wareHouseOrderPo);
        }
        return wareHouseOrderPo1;
    }

    //根据状态查找门店列表
    @Override
    public Object warehouseOrderGoodsStatus(String status, Integer currentPage, Integer pageSize) {
        IPage<WarehouseOrder> page = new Page<WarehouseOrder>(currentPage, pageSize);
        IPage<WarehouseOrder> warehouseOrders = warehouseOrdemapper.warehouseOrderGoodsStatus(page, status);
        List<WareHouseOrderPo> wareHouseOrderPo1 = new ArrayList<>();
        for (WarehouseOrder wareHouseOrderVos1 : warehouseOrders.getRecords()) {
            Long id = wareHouseOrderVos1.getId();
            List<WareHouseOrderVo> wareHouseOrderVos = warehouseOrdemapper.warehouseOrderGoodsCreateTimeAll(id);
            WareHouseOrderPo wareHouseOrderPo = new WareHouseOrderPo();
            wareHouseOrderPo.setWarehouseOrderNo(wareHouseOrderVos.get(0).getWarehouseOrderNo());
            wareHouseOrderPo.setWareHouseCreateTime(wareHouseOrderVos.get(0).getCreateTime());
            wareHouseOrderPo.setWareHouseOrderVo(wareHouseOrderVos);
            wareHouseOrderPo1.add(wareHouseOrderPo);
        }
        return wareHouseOrderPo1;
    }

    //根据备货员查找备货列表
    @Override
    public Object selectWareHouseOrderStaffId(Long staffId, Integer currentPage, Integer pageSize) {
        IPage<WarehouseOrder> page = new Page<WarehouseOrder>(currentPage, pageSize);
        IPage<WarehouseOrder> warehouseOrders = warehouseOrdemapper.selectWareHouseOrderStaffId(page, staffId);

        List<WareHouseOrderPo> wareHouseOrderPo1 = new ArrayList<>();
        for (WarehouseOrder wareHouseOrderVos1 : warehouseOrders.getRecords()) {
            Long id = wareHouseOrderVos1.getId();
            List<WareHouseOrderVo> wareHouseOrderVos = warehouseOrdemapper.warehouseOrderGoodsCreateTimeAll(id);
            WareHouseOrderPo wareHouseOrderPo = new WareHouseOrderPo();
            wareHouseOrderPo.setWarehouseOrderNo(wareHouseOrderVos.get(0).getWarehouseOrderNo());
            wareHouseOrderPo.setWareHouseCreateTime(wareHouseOrderVos.get(0).getCreateTime());
            wareHouseOrderPo.setWareHouseOrderVo(wareHouseOrderVos);
            wareHouseOrderPo1.add(wareHouseOrderPo);
        }
        return wareHouseOrderPo1;


    }

    @Override
    public Integer updateWareHouseOrderStatusAll(Long wareHouseOrderid, String status,Long userId) {
        return warehouseOrdemapper.updateWareHouseOrderStatusAll(wareHouseOrderid, status,userId);
    }




    //    后台获取还没有备货的订单信息
    @Override
    public List<BackendWarehouseOrder> selectReadyWareHouse() {
        List<BackendWarehouseOrder> bb = new ArrayList<>();
        List<BackendWarehouseOrder> end = warehouseOrdemapper.selectReadyWareHouse();

        for (BackendWarehouseOrder e : end) {
            String sku = e.getSku();
            String str = "";
            List<BackendWarehouseOrder> key = warehouseOrdemapper.selectAttrKey(sku);
            for (BackendWarehouseOrder k : key) {
                if (str.equals("")) {
                    str = k.getAttrValueName() + k.getAttrKeyName();
                } else {
                    str = str + "/" + k.getAttrValueName() + k.getAttrKeyName();
                }
            }
            e.setAttrValue(str);
            bb.add(e);
        }

        return bb;
    }


    //    后台获取已备货信息
    @Override
    public List<BackendWarehouseOrder> selectReadyWareHouseTrue() {
        return warehouseOrdemapper.selectReadyWareHouseTrue();
    }


    @Override
    public List<BackendStockWarehouse> selectStockWarehouse(Long subId) {
        return warehouseOrdemapper.selectStockWareHouse(subId);
    }

    @Override
    public Object returnStockReady(Long storeId) {
//        List<StockOrderPo> Done = warehousePackOrderGoodsMapper.selectWarehouseDone(storeId);
//       // System.out.println(Done);
//        //  Map<String,List<StockOrderPo>> map =new HashMap<>();
//        List<List<BackendStockWarehouse>> BackendStockWarehouseList = new ArrayList();
//        for (StockOrderPo d : Done) {
//            Long subId = d.getOrderSubId();
//            List<BackendStockWarehouse> st = warehouseOrdemapper.selectStockWareHouse(subId);
//            BackendStockWarehouseList.add(st);
//        }
        return "";
    }

    //计算子表message信息
    public static String splitModel(List<WarehousePo> modelList) {

        Set<String> unitSet = new HashSet<String>();
        for (WarehousePo m : modelList) {
            if (m.getUnit() != null) {
                unitSet.add(m.getUnit());
            }

        }
        String message = "";

        for (String unit : unitSet) {
            int sum = 0;
            for (WarehousePo model : modelList) {
                if (unit.equals(model.getUnit())) {
                    sum += model.getNum();
                }
            }
            message += sum + unit + ",";
        }
        return message.substring(0, message.lastIndexOf(","));
    }

    //将备货订单表里相同sku的商品重量汇总起来
    public String splitGoods(List<OrderGoods> goodsList) {
        String message = "";
        int sum = 0;
        for (OrderGoods model : goodsList) {
            sum += model.getNum();
        }
        message = sum + goodsList.get(0).getUnit();

        return message;
    }


}

