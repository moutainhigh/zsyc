package com.zsyc.warehouse.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.goods.entity.GoodsInfo;
import com.zsyc.goods.entity.GoodsStorePrice;
import com.zsyc.goods.service.GoodsStorePriceService;
import com.zsyc.goods.vo.GoodsAttributeValueVO;
import com.zsyc.goods.vo.GoodsPriceInfoVO;
import com.zsyc.order.entity.OrderGoods;
import com.zsyc.order.entity.OrderInfo;
import com.zsyc.order.entity.OrderSubInfo;
import com.zsyc.order.mapper.OrderSubInfoMapper;
import com.zsyc.warehouse.entity.*;
import com.zsyc.warehouse.mapper.*;
import com.zsyc.warehouse.po.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


@Service
public class IWarehousePackOrdersGoodsServiceImpl implements WarehousePackOrdersGoodsService {
    @Autowired
    private WarehousePackOrderGoodsMapper warehousePackOrderGoodsMapper;
    @Autowired
    private WarehousePackOrderMapper warehousePackOrderMapper;
    @Autowired
    private WarehouseOrderMapper warehouseOrderMapper;
    @Autowired
    private WarehouseOrderGoodsMapper warehouseOrderGoodsMapper;
    @Autowired
    private OrderSubInfoMapper orderSubInfoMapper;

    @Autowired
    private WarehouseStaffMapper warehouseStaffMapper;

    @Autowired
    public GoodsStorePriceService goodsStorePriceService;



    @Override
    public List<WarehouseGoods> selectDoneOrderGoods(Long subId) {
        return warehousePackOrderGoodsMapper.selectDoneOrderGoods(subId);
    }


    //    小程序显示已备好货状态的子订单数据(店铺id)(分拣数据)
    @Override
    public Object returnOrderGoodsDone() {
        List<WarehousePackOrder> Done = warehousePackOrderGoodsMapper.selectWarehouseDone();
        System.out.println(Done);
        //  Map<String,List<StockOrderPo>> map =new HashMap<>();
        List<SortingPo> sortingPos = new ArrayList<>();

     //   List<List<WarehouseGoods>> p = new ArrayList();

        for (WarehousePackOrder d : Done) {
            Long subId = d.getOrderId();
            Long storeId=d.getStoreId();
            List<OrderGoods> goods = warehousePackOrderGoodsMapper.selectGoods(subId);
            List<WarehouseGoods> list = new ArrayList<>();

            OrderSubInfo oIn = warehousePackOrderGoodsMapper.selectOrderInfoNo(subId);
            for (OrderGoods goods1 : goods) {

                GoodsInfo goodsInfo = warehousePackOrderMapper.selectSkuGoodsInfo(goods1.getSku());
                //查找该sku是否组合商品
                WarehouseGoods warehouseGoods = new WarehouseGoods();
                //如果不是组合商品走正常流程
                if (goodsInfo.getGoodsType() == 2) {
                    //如果是组合商品的话（获得组合商品的子商品信息）
                    List<OrderGoodsKuai> shenqi =new ArrayList<>();
                    GoodsPriceInfoVO goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(storeId, oIn.getAddressId(), goods1.getSku());
                    //删除当前快捷菜的sku对象
//                        goodsList.remove(goods);
                    //查子商品信息
                    for (GoodsPriceInfoVO goodsPriceInfoVO1 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                        //查子商品的冗余单位(斤)
                        for (GoodsAttributeValueVO goodsAttributeValueVO : goodsPriceInfoVO1.getGoodsAttributeValueVOS()) {
                            if (goodsAttributeValueVO.getIsSale() == 1) {
                                //拿到子商品的冗余单位
                                OrderGoodsKuai orderGoods1 = new OrderGoodsKuai();
                                orderGoods1.setSku(goodsPriceInfoVO1.getSku());
                                orderGoods1.setNum(1*goods1.getNum());
                                orderGoods1.setTotalPrice(goodsPriceInfoVO1.getPrice()*orderGoods1.getNum());
                                //  orderGoods.setId(goods.getId());
                                 orderGoods1.setOrderSubId(subId);
                                //大佬说暂时快捷菜的子商品都是一个

                                GoodsInfo goodsInfo1 = warehousePackOrderMapper.selectSkuGoodsInfo(goodsPriceInfoVO1.getSku());
                                orderGoods1.setGoodsName(goodsInfo1.getGoodsName());
                                //orderGoods1.setCreateTime(order.getCreateTime());
                                orderGoods1.setUnit(goodsAttributeValueVO.getAttrKeyName());
                                orderGoods1.setImg(goodsInfo1.getPicture());
                                shenqi.add(orderGoods1);
                            }
                        }
                    }
                    GoodsInfo goodsInfo2 = warehousePackOrderMapper.selectSkuGoodsInfo(goods1.getSku());
                    warehouseGoods.setGoodsName(goodsInfo2.getGoodsName());
                    warehouseGoods.setNum(goods1.getNum());
                    warehouseGoods.setUnit(goods1.getUnit());
                    warehouseGoods.setOrderSubId(subId);
                    warehouseGoods.setKuaijie(shenqi);
                    warehouseGoods.setSku(goods1.getSku());

                    warehouseGoods.setImg(goodsInfo2.getPicture());
                    BigDecimal e =new BigDecimal(goods1.getPrice());
                    BigDecimal t =new BigDecimal(100);
                    // Double d= Double.valueOf(pricee*num);
                    BigDecimal a =e.divide(t);
                    BigDecimal b =new BigDecimal(goods1.getNum());
                    BigDecimal d1=a.multiply(b);
                    warehouseGoods.setTotalPrice(d1);
                    list.add(warehouseGoods);
                }else {
                    GoodsInfo goodsInfo2 = warehousePackOrderMapper.selectSkuGoodsInfo(goods1.getSku());
                    warehouseGoods.setGoodsName(goodsInfo2.getGoodsName());
                    BigDecimal e =new BigDecimal(goods1.getPrice());
                    BigDecimal t =new BigDecimal(100);
                    // Double d= Double.valueOf(pricee*num);
                    BigDecimal a =e.divide(t);
                    BigDecimal b =new BigDecimal(goods1.getNum());
                    BigDecimal d1=a.multiply(b);
                    warehouseGoods.setTotalPrice(d1);
                    warehouseGoods.setSku(goods1.getSku());
                    warehouseGoods.setImg(goodsInfo2.getPicture());
                    warehouseGoods.setNum(goods1.getNum());
                    warehouseGoods.setUnit(goods1.getUnit());
                    warehouseGoods.setOrderSubId(subId);
                    list.add(warehouseGoods);
                }




            }



            SortingPo sortingPos1 = new SortingPo();
            sortingPos1.setOrderNo(oIn.getOrderNo());
            sortingPos1.setBookStartTime(oIn.getBookStartTime());
            sortingPos1.setId(d.getId());
            sortingPos1.setConsigneeAddress(oIn.getConsigneeAddress());

            sortingPos1.setList(list);
            sortingPos.add(sortingPos1);

        }
        return sortingPos;
    }


    //    后台没有还没备货完成按钮
    @Override
    public Integer updateWarehouseStauts(Long subId) {
        return warehousePackOrderGoodsMapper.updateWarehouseStauts(subId);
    }

    //显示分拣完成分拣单
    @Override
    public List<ReturnMinWareHousePo> packOrderDone(Long staffId) {
        // IPage<WarehouseGoods> page = new Page<WarehouseGoods>(currentPage, pageSize);
        List<WarehouseGoods> warehouseGoods1= warehousePackOrderGoodsMapper.packOrderDone(staffId);
        List<ReturnMinWareHousePo> returnMinWareHousePos= new ArrayList<>();
        for(WarehouseGoods warehouseGoods2:warehouseGoods1){

            ReturnMinWareHousePo rem =staffIdDoneAll(staffId, warehouseGoods2.getId());
            returnMinWareHousePos.add(rem);



        }
        return returnMinWareHousePos;
    }

    //配送接单
    @Override
    public Integer distribution(Long subId, Long postmanId) {
        return warehousePackOrderGoodsMapper.distribution(subId, postmanId);
    }

    @Override
    public Integer distributionDone(Long subId, Long postmanId) {
        return warehousePackOrderGoodsMapper.distributionDone(subId, postmanId);
    }

    @Override
    public Integer DoneTime(Long subId) {
        //        当前时间
        LocalDateTime time = LocalDateTime.now();
        Long l = time.toInstant(ZoneOffset.of("+8")).toEpochMilli();
//        最后一次催单时间
        OrderSubInfo o = warehousePackOrderGoodsMapper.urgeTime(subId);
        if (o.getUrgeTime() == null) {
            int priorityNu = 0;
            priorityNu = priorityNu + 1;
            warehousePackOrderGoodsMapper.updateUrgeTime(time, subId, priorityNu);
            return 1;
        } else {
            LocalDateTime lll = o.getUrgeTime();
            Long urtim = lll.toInstant(ZoneOffset.of("+8")).toEpochMilli();
            Long num = l - urtim;
            if (num > 3000) {
                int priorityNu = o.getPriority();
                priorityNu = priorityNu + 1;
                warehousePackOrderGoodsMapper.updateUrgeTime(time, subId, priorityNu);
                return 1;
            } else {
                return 0;
            }


        }


    }

    @Override
    public List<WarehousePackOrder> warehouse_pack_orderStatus(Long subId) {
        return warehousePackOrderGoodsMapper.wareHousePackOrderStatus(subId);
    }

    //显示完成备货备货单详情
    @Override
    public ReturnMinWareHousePo checkGoodsinfo(Long wareHouseId) {

        List<ReturnMinWareHousePo> re = new ArrayList<ReturnMinWareHousePo>();

        List<WarehouseOrderGoods> warehouseOrderGoods = warehouseOrderGoodsMapper.selectWareHouseOrderGoods(wareHouseId);
        List<WarehouseOrder> warehouseOrders = warehouseOrderMapper.selectList(new QueryWrapper<WarehouseOrder>().eq("id", wareHouseId));
        ReturnMinWareHousePo returnMinWareHousePo = new ReturnMinWareHousePo();
        List<SortingOrderPo> sortingOrderPo = new ArrayList<>();

        for (WarehouseOrderGoods warehouseOrderGoods1 : warehouseOrderGoods) {

            SortingOrderPo sortingOrderPo1 = new SortingOrderPo();
            sortingOrderPo1.setMessage(warehouseOrderGoods1.getMessage());
            GoodsInfo goodsInfo = warehousePackOrderMapper.selectSkuGoodsInfo(warehouseOrderGoods1.getSku());
            sortingOrderPo1.setGoodsName(goodsInfo.getGoodsName());
            sortingOrderPo1.setId(warehouseOrderGoods1.getId());
            sortingOrderPo1.setSku(warehouseOrderGoods1.getSku());
            GoodsInfo goodsInfo1 =warehousePackOrderMapper.selectSkuGoodsInfo(warehouseOrderGoods1.getSku());
            sortingOrderPo1.setImg(goodsInfo1.getPicture());
            String str_string = sortingOrderPo1.getMessage().replaceAll("[^0-9]", "");//  \d 为正则表达式表示[0-9]数字
            Integer num =Integer.valueOf(str_string);
            GoodsStorePrice goodsStorePrice =warehouseOrderMapper.selectPriceSku(warehouseOrderGoods1.getSku(),warehouseOrders.get(0).getStoreId());
            BigDecimal goodsPrice =new BigDecimal(goodsStorePrice.getPrice());
            BigDecimal t =new BigDecimal(100);
            // Double d= Double.valueOf(pricee*num);
            BigDecimal a =goodsPrice.divide(t);
            BigDecimal b =new BigDecimal(num);
            BigDecimal d=a.multiply(b);
            sortingOrderPo1.setTotalPrice(d);
            sortingOrderPo.add(sortingOrderPo1);


        }
        returnMinWareHousePo.setRemark(warehouseOrders.get(0).getRemark());
        returnMinWareHousePo.setCreateTime(warehouseOrders.get(0).getCreateTime());
        returnMinWareHousePo.setList(sortingOrderPo);
        returnMinWareHousePo.setWarehouseOrderNo(warehouseOrders.get(0).getWarehouseOrderNo());
        re.add(returnMinWareHousePo);

        return returnMinWareHousePo;
    }

    //分拣显示该分拣员接了的分拣单
    @Override
    public Object selectPackOrder(Long staffId) {
        List<ReturnMinWareHousePo> re = new ArrayList<ReturnMinWareHousePo>();


        //获得该分拣员的子订单id
        List<WarehousePackOrder> warehousePOG = warehousePackOrderGoodsMapper.selectPackOrder(staffId);

        for (WarehousePackOrder warehousePOG1 : warehousePOG) {
            Long orderId = warehousePOG1.getOrderId();
            Long storeId=warehousePOG1.getStoreId();
            OrderSubInfo orderSubInfo = warehousePackOrderGoodsMapper.selectOrderInfoNo(orderId);
            //利用子订单id查分拣字表
            List<SortingOrderPo> sortingOrderPos = new ArrayList<>();
            List<WarehousePackOrderGoods> warehousePackOrderGoods = warehousePackOrderGoodsMapper.selectPackid(warehousePOG1.getId());
            for (WarehousePackOrderGoods warehousePackOrderGoods1 : warehousePackOrderGoods) {


                SortingOrderPo sortingOrderPo = new SortingOrderPo();
                OrderGoods orderGoods = warehousePackOrderGoodsMapper.selectOrderGoodsAll(warehousePackOrderGoods1.getOrderGoodsId());
                GoodsInfo goodsInfo = warehousePackOrderMapper.selectSkuGoodsInfo(orderGoods.getSku());
                //查找该sku是否组合商品

                //如果不是组合商品走正常流程
                if (goodsInfo.getGoodsType() == 2) {
                    List<OrderGoodsKuai> shenqi =new ArrayList<>();
                    //如果是组合商品的话（获得组合商品的子商品信息）
                    GoodsPriceInfoVO goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(storeId, orderSubInfo.getAddressId(), orderGoods.getSku());
                    //删除当前快捷菜的sku对象
//                        goodsList.remove(goods);
                    //查子商品信息
                    for (GoodsPriceInfoVO goodsPriceInfoVO1 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                        //查子商品的冗余单位(斤)
                        for (GoodsAttributeValueVO goodsAttributeValueVO : goodsPriceInfoVO1.getGoodsAttributeValueVOS()) {
                            if (goodsAttributeValueVO.getIsSale() == 1) {
                                //拿到子商品的冗余单位
                                OrderGoodsKuai orderGoods1 = new OrderGoodsKuai();
                                orderGoods1.setSku(goodsPriceInfoVO1.getSku());
                                orderGoods1.setNum(1*orderGoods.getNum());
                                orderGoods1.setTotalPrice(goodsPriceInfoVO1.getPrice()*orderGoods1.getNum());
                                //  orderGoods.setId(goods.getId());
                                orderGoods1.setOrderSubId(orderGoods.getOrderSubId());
                                //大佬说暂时快捷菜的子商品都是一个

                                GoodsInfo goodsInfo1 = warehousePackOrderMapper.selectSkuGoodsInfo(goodsPriceInfoVO1.getSku());
                                orderGoods1.setGoodsName(goodsInfo1.getGoodsName());
                                //orderGoods1.setCreateTime(order.getCreateTime());
                                orderGoods1.setUnit(goodsAttributeValueVO.getAttrKeyName());
                                orderGoods1.setImg(goodsInfo1.getPicture());
                                shenqi.add(orderGoods1);
                            }
                        }
                    }
                    sortingOrderPo.setNum(warehousePackOrderGoods1.getNum().toString());
                    sortingOrderPo.setUnit(warehousePackOrderGoods1.getUnit());
                    sortingOrderPo.setId(warehousePackOrderGoods1.getId());
                    sortingOrderPo.setGoodsName(goodsInfo.getGoodsName());
                    sortingOrderPo.setSku(orderGoods.getSku());



                    sortingOrderPo.setImg(goodsInfo.getPicture());
                    BigDecimal e =new BigDecimal(orderGoods.getPrice());
                    BigDecimal t =new BigDecimal(100);
                    // Double d= Double.valueOf(pricee*num);
                    BigDecimal a =e.divide(t);
                    BigDecimal b =new BigDecimal(orderGoods.getNum());
                    BigDecimal d1=a.multiply(b);
                    sortingOrderPo.setTotalPrice(d1);



                    sortingOrderPos.add(sortingOrderPo);
                    sortingOrderPo.setKuaijie(shenqi);
                }else {
                    sortingOrderPo.setNum(warehousePackOrderGoods1.getNum().toString());
                    sortingOrderPo.setUnit(warehousePackOrderGoods1.getUnit());
                    sortingOrderPo.setId(warehousePackOrderGoods1.getId());
                    sortingOrderPo.setGoodsName(goodsInfo.getGoodsName());
                    sortingOrderPo.setSku(orderGoods.getSku());
                    sortingOrderPo.setImg(goodsInfo.getPicture());
                    BigDecimal e =new BigDecimal(orderGoods.getPrice());
                    BigDecimal t =new BigDecimal(100);
                    // Double d= Double.valueOf(pricee*num);
                    BigDecimal a =e.divide(t);
                    BigDecimal b =new BigDecimal(orderGoods.getNum());
                    BigDecimal d1=a.multiply(b);
                    sortingOrderPo.setTotalPrice(d1);
                    sortingOrderPos.add(sortingOrderPo);

                }


            }


            ReturnMinWareHousePo returnMinWareHousePo = new ReturnMinWareHousePo();
            returnMinWareHousePo.setList(sortingOrderPos);
            returnMinWareHousePo.setAddressId(orderSubInfo.getAddressId());
            returnMinWareHousePo.setAddress(orderSubInfo.getConsigneeAddress());
            returnMinWareHousePo.setWarehouseOrderNo(orderSubInfo.getOrderNo());
            returnMinWareHousePo.setCreateTime(orderSubInfo.getBookStartTime());
            returnMinWareHousePo.setWarehousePackOrderID(warehousePOG1.getId());
            returnMinWareHousePo.setId(orderSubInfo.getId());

            //利用子订单id查快照表
            re.add(returnMinWareHousePo);

        }


        return re;
    }

    //查找该备货员的子订单信息
    @Override
    public List<WarehousePackOrder> selectPackStaffId(Long staffId) {
        return warehousePackOrderGoodsMapper.selectPackStaffId(staffId);
    }

    @Override
    public Object selectStaffIdDone(Long staffId, Long subId) {


        return warehousePackOrderGoodsMapper.selectStaffIdDone(staffId, subId);
    }

    //查看分拣单详细信息
    @Override
    public ReturnMinWareHousePo staffIdDoneAll(Long staffId, Long subId) {
        List<WarehousePackOrderGoods> ss = warehousePackOrderGoodsMapper.selectStaffIdDone(staffId, subId);

        List<ReturnMinWareHousePo> remWhPo = new ArrayList<>();
        List<SortingOrderPo> sortingOrderPos = new ArrayList<>();
        ReturnMinWareHousePo rem = new ReturnMinWareHousePo();
        WarehousePackOrder warehousePackOrder = warehousePackOrderMapper.selectWarehouseAll(subId);

        OrderSubInfo orderSubInfo = warehousePackOrderGoodsMapper.selectOrderInfoNo(warehousePackOrder.getOrderId());
        for (WarehousePackOrderGoods s : ss) {
            SortingOrderPo sortingOrderPo = new SortingOrderPo();
            OrderGoods orderGoods = warehousePackOrderGoodsMapper.selectOrderGoodsAll(s.getOrderGoodsId());
            GoodsInfo goodsInfo = warehousePackOrderMapper.selectSkuGoodsInfo(orderGoods.getSku());
            if (goodsInfo.getGoodsType() == 2) {
                List<OrderGoodsKuai> shenqi=new ArrayList<>();
                //如果是组合商品的话（获得组合商品的子商品信息）
                GoodsPriceInfoVO goodsPriceInfoVO = goodsStorePriceService.getGoodsPriceInfo(warehousePackOrder.getStoreId(), orderSubInfo.getAddressId(), orderGoods.getSku());
                //删除当前快捷菜的sku对象
//                        goodsList.remove(goods);
                //查子商品信息
                for (GoodsPriceInfoVO goodsPriceInfoVO1 : goodsPriceInfoVO.getGoodsPriceInfoVOS()) {
                    //查子商品的冗余单位(斤)
                    for (GoodsAttributeValueVO goodsAttributeValueVO : goodsPriceInfoVO1.getGoodsAttributeValueVOS()) {
                        if (goodsAttributeValueVO.getIsSale() == 1) {
                            //拿到子商品的冗余单位
                            OrderGoodsKuai orderGoods1 = new OrderGoodsKuai();
                            orderGoods1.setSku(goodsPriceInfoVO1.getSku());
                            //大佬说暂时快捷菜的子商品都是一个
                            orderGoods1.setNum(1*orderGoods.getNum());
                            orderGoods1.setTotalPrice(goodsPriceInfoVO1.getPrice()*orderGoods1.getNum());
                            //  orderGoods.setId(goods.getId());
                            orderGoods1.setOrderSubId(orderGoods.getOrderSubId());


                            GoodsInfo goodsInfo1 = warehousePackOrderMapper.selectSkuGoodsInfo(goodsPriceInfoVO1.getSku());
                            orderGoods1.setGoodsName(goodsInfo1.getGoodsName());
                            //orderGoods1.setCreateTime(order.getCreateTime());
                            orderGoods1.setUnit(goodsAttributeValueVO.getAttrKeyName());
                            orderGoods1.setImg(goodsInfo1.getPicture());
                            shenqi.add(orderGoods1);
                        }
                    }
                }
                sortingOrderPo.setGoodsName(goodsInfo.getGoodsName());
                sortingOrderPo.setUnit(s.getUnit());
                sortingOrderPo.setKuaijie(shenqi);

                sortingOrderPo.setNum(s.getNum().toString());
                sortingOrderPo.setAddress(orderSubInfo.getOrderNo());
                sortingOrderPo.setCreateTime(orderSubInfo.getBookStartTime());
                sortingOrderPo.setImg(goodsInfo.getPicture());
                BigDecimal e =new BigDecimal(orderGoods.getPrice());
                BigDecimal t =new BigDecimal(100);
                // Double d= Double.valueOf(pricee*num);
                BigDecimal a =e.divide(t);
                BigDecimal b =new BigDecimal(orderGoods.getNum());
                BigDecimal d1=a.multiply(b);
                sortingOrderPo.setTotalPrice(d1);
                sortingOrderPos.add(sortingOrderPo);
            }else {
                sortingOrderPo.setGoodsName(goodsInfo.getGoodsName());
                sortingOrderPo.setUnit(s.getUnit());
                sortingOrderPo.setNum(s.getNum().toString());
                sortingOrderPo.setAddress(orderSubInfo.getOrderNo());
                sortingOrderPo.setCreateTime(orderSubInfo.getBookStartTime());
                sortingOrderPo.setImg(goodsInfo.getPicture());
                BigDecimal e =new BigDecimal(orderGoods.getPrice());
                BigDecimal t =new BigDecimal(100);
                // Double d= Double.valueOf(pricee*num);
                BigDecimal a =e.divide(t);
                BigDecimal b =new BigDecimal(orderGoods.getNum());
                BigDecimal d1=a.multiply(b);
                sortingOrderPo.setTotalPrice(d1);
                sortingOrderPos.add(sortingOrderPo);
            }

        }
        rem.setList(sortingOrderPos);
        rem.setAddress(orderSubInfo.getConsigneeAddress());
        rem.setRemark(warehousePackOrder.getRemark());
        rem.setCreateTime(orderSubInfo.getBookStartTime());
        rem.setWarehouseOrderNo(orderSubInfo.getOrderNo());
        remWhPo.add(rem);
        return rem;
    }

    @Override
    public Integer wareHousePackStatusDone(Long subId, Long warehousePackOrderGoodsId) {
        return warehousePackOrderGoodsMapper.wareHousePackStatusDone(subId, warehousePackOrderGoodsId);
    }

    //分拣缺货按钮
    @Override
    public Integer packOrderShortage(Long subId, Long warehousePackOrderGoodsId) {
        return warehousePackOrderGoodsMapper.packOrderShortage(subId, warehousePackOrderGoodsId);
    }

    @Override
    public OrderInfo selectAddress(Long subId) {
        return warehousePackOrderGoodsMapper.selectAddress(subId);
    }


    //后台按条件查询分拣备货人员名单
    @Override
    public Object selectStockPack(StockPackPo stockPackPo) {
        String idNumber=stockPackPo.getIdNumber();
        String  phone=stockPackPo.getPhone();
        String StockType =stockPackPo.getStockType();
        String  PackName=stockPackPo.getStockPackName();
        Long storeId=stockPackPo.getStoreId();
        if(storeId==null){
            storeId=0L;
        }
        IPage<WarehouseStaff> page = new Page<WarehouseStaff>(stockPackPo.getCurrentPage(), stockPackPo.getPageSize());
        if(StringUtils.isBlank(idNumber))idNumber = null;
        if(StringUtils.isBlank(phone))phone = null;
        if(StringUtils.isBlank(StockType))StockType = null;
        if(StringUtils.isBlank(PackName))PackName = null;

        IPage<WarehouseStaff> warehousePackOrderIPage =warehouseStaffMapper.selectStockPack(page,PackName,phone,StockType,idNumber,storeId);

        return warehousePackOrderIPage ;
    }
    //后台分拣备货删除按钮
    @Override
    public Integer delStockPack(Integer staffId) {

        return warehousePackOrderGoodsMapper.delStockPack(staffId);
    }

    @Override
    public Integer updateStockPackName(String stockPackName, Long id) {
        return warehousePackOrderGoodsMapper.updateStockPackName(stockPackName,id);
    }


    @Override
    public OrderSubInfo urgeTime(Long subId) {
        return warehousePackOrderGoodsMapper.urgeTime(subId);
    }


}
