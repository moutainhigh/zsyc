package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;


import com.zsyc.api.AccountHelper;
import com.zsyc.warehouse.po.PackPo;
import com.zsyc.warehouse.po.StockPackPo;
import com.zsyc.warehouse.po.StockPo;
import com.zsyc.warehouse.service.WarehouseOrderGoodsService;
import com.zsyc.warehouse.service.WarehouseOrderService;
import com.zsyc.warehouse.service.WarehousePackOrdersGoodsService;
import com.zsyc.warehouse.service.WarehousePackOrdersService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/BackendWarehouseController")
@Api
public class BackendWarehouseController {

    @Reference
    public WarehouseOrderService selectReadyWarehouse;
    @Reference
    public WarehousePackOrdersGoodsService warehousePackOrdersGoodsService;
    @Reference
    public WarehousePackOrdersService warehousePackOrdersService;
    @Reference
    public WarehouseOrderGoodsService warehouseOrderGoodsService;
    @Reference
    public  WarehouseOrderService warehouseOrderService;

    @Autowired
    public AccountHelper accountHelper;




    @ApiOperation("根据sku获取所有规格")
    @ApiResponse(code = 200, message = "success", response = List.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sku", value = "商品的sku", required = true, dataType = "String")
    })
    @GetMapping(value = "selectAttKeyAll")
    public Object selectAttKeyAll(String sku) {

        return selectReadyWarehouse.selectAttKeyAll(sku);
    }
//    @ApiOperation("后台查找待分拣单")
//    @ApiResponse(code = 200, message = "success", response = List.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "sku", value = "商品的sku", required = true, dataType = "String")
//    })
//    @GetMapping(value = "returnSelectBackendWarehousePackOrder")
//    public Object returnSelectBackendWarehousePackOrder(){
//        return  warehousePackOrdersService.returnSelectBackendWarehousePackOrder();
//
//    }
//
//    @ApiOperation("后台查找已分拣单")
//    @ApiResponse(code = 200, message = "success", response = List.class)
//
//    @GetMapping(value = "returnSelectBackendWarehousePackOrderDone")
//    public Object returnSelectBackendWarehousePackOrderDone(){
//
//        return  warehousePackOrdersService.returnSelectBackendWarehousePackOrderDone();
//
//    }
//
//
//    @ApiOperation("后台查找未有备货的备货信息(待分拣)")
//    @ApiResponse(code = 200, message = "success", response = List.class)
//    @GetMapping(value = "selectReadyWarehouse")
//    public Object selectReadyWarehouse() {
//
//        return selectReadyWarehouse.selectReadyWareHouse();
//    }


//    @ApiOperation("后台根据备货单号查询备货列表")
//    @ApiResponse(code = 200, message = "success")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "warehouseOrderNo", value = "备货单号", required = true, dataType = "String")
//    })
//    @GetMapping(value = "warehouseOrderGoodsOrderNo")
//    public Object warehouseOrderGoodsOrderNo(String warehouseOrderNo){
//
//        return selectReadyWarehouse.warehouseOrderGoodsOrderNo(warehouseOrderNo);
//
//    }

//    @ApiOperation("后台根据备货时间范围查询备货列表(已分页)")
//    @ApiResponse(code = 200, message = "success")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "startTime", value = "开始时间传入格式(yyyy-MM-dd HH:mm:ss)", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "endTime", value = "结束时间传入格式(yyyy-MM-dd HH:mm:ss)", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "currentPage", value = "页数", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, dataType = "String")
//    })
//    @GetMapping(value = "warehouseOrderGoodsCreateTime")
//    public Object warehouseOrderGoodsCreateTime(String startTime,String endTime,Integer currentPage, Integer pageSize){
//
//        return    selectReadyWarehouse.warehouseOrderGoodsCreateTime(startTime,endTime,currentPage,pageSize);
//
//    }


    @ApiOperation("后台显示门店")
    @ApiResponse(code = 200, message = "success")
    @GetMapping(value = "warehouseOrderGoodsStoreId")
    public Object warehouseOrderGoodsStoreId(){

        return    selectReadyWarehouse.selectStoreInfo();

    }

//    @ApiOperation("后台根据门店id查找备货列表(已分页)")
//    @ApiResponse(code = 200, message = "success")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "storeId", value = "门店id", required = true, dataType = "Long"),
//            @ApiImplicitParam(name = "currentPage", value = "页数", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, dataType = "String")
//               })
//    @GetMapping(value = "warehouseOrderGoodsStoreIdAll")
//    public Object warehouseOrderGoodsStoreIdAll(Long storeId,Integer currentPage, Integer pageSize){
//
//        return    selectReadyWarehouse.warehouseOrderGoodsStoreIdAll(storeId,currentPage,pageSize);
//
//    }

//    @ApiOperation("后台根据状态查找备货列表(已分页)")
//    @ApiResponse(code = 200, message = "success")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "status", value = "备货状态（传1是“准备中”，传2是“备货中”,传0是“确认备货”）", required = true, dataType = "Long"),
//            @ApiImplicitParam(name = "currentPage", value = "页数", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, dataType = "String")
//    })
//    @GetMapping(value = "warehouseOrderGoodsStatus")
//    public Object warehouseOrderGoodsStatus(Long status,Integer currentPage, Integer pageSize){
//            if(status==1){
//                String status1="READY";
//                return  selectReadyWarehouse.warehouseOrderGoodsStatus(status1,currentPage,pageSize);
//            }else if(status==2){
//                String status1="STOCKING";
//                return  selectReadyWarehouse.warehouseOrderGoodsStatus(status1,currentPage,pageSize);
//            }else{
//                String status1="DONE";
//                return  selectReadyWarehouse.warehouseOrderGoodsStatus(status1,currentPage,pageSize);
//            }
//    }


//    @ApiOperation("后台根据备货员id查找备货列表(已分页)")
//    @ApiResponse(code = 200, message = "success")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "staffId", value = "备货员id", required = true, dataType = "Long"),
//            @ApiImplicitParam(name = "currentPage", value = "页数", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, dataType = "String")
//    })
//    @GetMapping(value = "selectWareHouseOrderStaffId")
//    public Object selectWareHouseOrderStaffId(Long staffId,Integer currentPage, Integer pageSize){
//
//        return selectReadyWarehouse.selectWareHouseOrderStaffId(staffId,currentPage,pageSize);
//
//    }


//    @ApiOperation("后台根据订单编号查询分拣信息")
//    @ApiResponse(code = 200, message = "success")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "orderNo", value = "订单编号", required = true, dataType = "String"),
//
//    })
//    @GetMapping(value = "selectOrderPackOrderId")
//   public Object selectOrderPackOrderId(String orderNo){
//
//        return warehousePackOrdersService.selectOrderPackOrderId(orderNo);
//    }

//    @ApiOperation("后台根据分拣员id查询分拣信息(已分页)")
//    @ApiResponse(code = 200, message = "success")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "staffId", value = "分拣员id", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "currentPage", value = "页数", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, dataType = "String")
//    })
//    @GetMapping(value = "selectStaffId")
//    public Object selectStaffId(Long staffId,Integer currentPage,Integer pageSize){
//
//        return warehousePackOrdersService.selectStaffId(staffId,currentPage,pageSize);
//    }

//
//    @ApiOperation("后台根据分拣完成时间范围查询(已分页)")
//    @ApiResponse(code = 200, message = "success")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "startTime", value = "开始时间传入格式(yyyy-MM-dd HH:mm:ss)", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "endTime", value = "结束时间传入格式(yyyy-MM-dd HH:mm:ss)", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "currentPage", value = "页数", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, dataType = "String")
//    })
//    @GetMapping(value = "selectDoneTime")
//    public Object selectDoneTime(String startTime,String endTime,Integer currentPage,Integer pageSize){
//
//        return warehousePackOrdersService.selectPackOrderGoodsDoneTime(startTime,endTime,currentPage,pageSize);
//    }


//    @ApiOperation("后台根据分拣状态")
//    @ApiResponse(code = 200, message = "success")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "status", value = "分拣状态(传入1是”待分拣状态,传入2是”分拣中“，传入0是“分拣完成”)", required = true, dataType = "Long"),
//            @ApiImplicitParam(name = "currentPage", value = "页数", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, dataType = "String")
//    })
//    @GetMapping(value = "selectPackOrderGoodsDoneStatus")
//    public Object selectPackOrderGoodsDoneStatus(Long  status,Integer currentPage,Integer pageSize){
//        if(status==1){
//            String status1="PENDDING";
//            return warehousePackOrdersService.selectPackOrderGoodsDoneStatus(status1,currentPage,pageSize);
//        }else if(status==2){
//            String status2="RECEIVED";
//            return warehousePackOrdersService.selectPackOrderGoodsDoneStatus(status2,currentPage,pageSize);
//        }else{
//            String status0="DONE";
//            return warehousePackOrdersService.selectPackOrderGoodsDoneStatus(status0,currentPage,pageSize);
//        }
//
//
//
//    }



    @ApiOperation("后台备货条件查询")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "StockPo.storeId", value = "门店id(必传)", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "StockPo.startTime", value = "开始时间(必须和结束时间一起传)(格式：yyyy-MM-dd HH:mm:ss)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "StockPo.endTime", value = "结束时间(必须和开始时间一起传)(格式：yyyy-MM-dd HH:mm:ss)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "StockPo.status", value = "状态(传'READY'准备中的备货单，传‘STOCKING’备货中的备货单，传‘DONE’备货完成的备货单)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "StockPo.staffId", value = "备货员id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "StockPo.warehouseOrderNo", value = "备货单号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "StockPo.currentPage", value = "页数(必传)", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "StockPo.pageSize", value = "条数(必传)", required = true, dataType = "Integer")
    })
    @PostMapping(value = "selectStock", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object selectStock(@RequestBody StockPo stockPo) {

        return selectReadyWarehouse.selectStock(stockPo);
    }



    @ApiOperation("后台分拣条件查询")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PackPo.storeId", value = "门店id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "PackPo.startTime", value = "开始时间(必须和结束时间一起传)(格式：yyyy-MM-dd HH:mm:ss)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "PackPo.endTime", value = "结束时间(必须和开始时间一起传)(格式：yyyy-MM-dd HH:mm:ss)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "PackPo.status", value = "状态(传'PENDDING'等待分拣的分拣单，传‘RECEIVED’分拣中的分拣单，传‘DONE’分拣完成的分拣单)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "PackPo.staffId", value = "分拣员id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "PackPo.orderNo", value = "子订单编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "PackPo.currentPage", value = "页数(必传)", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "PackPo.pageSize", value = "条数(必传)", required = true, dataType = "Integer")
    })
        @PostMapping(value = "selectPack", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object selectPack(@RequestBody PackPo packPo) {

        return warehousePackOrdersService.selectPack(packPo);
    }


    @ApiOperation("后台更改备货主表状态")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态(传'READY'准备中的备货单，传‘STOCKING’备货中的备货单，传‘DONE’备货完成的备货单)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "wareHouseOrderid", value = "主备货单id", required = true, dataType = "String")
                })
    @GetMapping(value = "updateWarehouseOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public  Object updateWarehouseOrder(Long wareHouseOrderid,String status){
        Long userId=accountHelper.getUser().getId();
        return selectReadyWarehouse.updateWareHouseOrderStatusAll(wareHouseOrderid,status,userId);

    }

    @ApiOperation("后台更改备货子表状态")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态(传'DONE'已完成，传‘SHORTAGE’缺货，传‘PARTIAL’部分缺货)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "warehouseOrderGoodsId", value = "子备货单id", required = true, dataType = "String")
    })
    @GetMapping(value = "updateWarehouseOrderGoods", produces = MediaType.APPLICATION_JSON_VALUE)
    public  Object updateWarehouseOrderGoods(Long warehouseOrderGoodsId,String status){
        Long userId=accountHelper.getUser().getId();
        return warehouseOrderGoodsService.updateWarehouseOrderGoods(warehouseOrderGoodsId,status,userId);

    }



    @ApiOperation("后台更改分拣主表状态")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态(传'PENDDING'等待分拣的分拣单，传‘RECEIVED’分拣中的分拣单，传‘DONE’分拣完成的分拣单)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "packId", value = "分拣主表id", required = true, dataType = "String")
    })
    @GetMapping(value = "updatePackOrderStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    public  Object updatePackOrderStatus(Long packId,String status){
        Long userId=accountHelper.getUser().getId();
        return warehousePackOrdersService.updatePackOrderStatus(packId,status,userId);

    }

    @ApiOperation("后台更改分拣子表状态")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态(传'DONE'已完成，传‘SHORTAGE’缺货)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "warehouseOrderPackGoodsId", value = "子分拣单id", required = true, dataType = "String")
    })
    @GetMapping(value = "updateWarehouseOrderGoodsStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    public  Object updatePackOrderGoodsStatus(Long warehouseOrderPackGoodsId,String status){
        Long userId=accountHelper.getUser().getId();
        return warehousePackOrdersService.updatePackOrderGoodsStatus(warehouseOrderPackGoodsId,status,userId);

    }

    @ApiOperation("后台按条件查询分拣备货人员名单")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "StockPackPo.stockPackName", value = "分拣备货员名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "StockPackPo.phone", value = "人员电话", required = true, dataType = "String"),
            @ApiImplicitParam(name = "StockPackPo.stockType", value = "人员类型（STOCK-备货，SORT-分拣）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "StockPackPo.idNumber", value = "身份证号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "StockPackPo.storeId", value = "门店Id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "StockPackPo.currentPage", value = "页数(必传)", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "StockPackPo.pageSize", value = "条数(必传)", required = true, dataType = "Integer")
    })
    @PostMapping(value = "selectStockPackMaster", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object selectStockPackMaster(@RequestBody  StockPackPo stockPackPo){
        return warehousePackOrdersGoodsService.selectStockPack(stockPackPo);
    }





    @ApiOperation("后台分拣备货删除按钮")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "staffId", value = "备货分拣员id", required = true, dataType = "String"),
    })
    @GetMapping(value = "delStockPack", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object delStockPack(Integer staffId){
        return warehousePackOrdersGoodsService.delStockPack(staffId);
    }


    @ApiOperation("后台分拣备货人员更改资料")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "StockPackPo.id", value = "备货分拣员主表id(必传)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "StockPackPo.stockPackName", value = "更改的姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "StockPackPo.phone", value = "人员电话", required = true, dataType = "String"),
            @ApiImplicitParam(name = "StockPackPo.IdNumber", value = "身份证号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "StockPackPo.idCardImg", value = "身份证照片(逗号分隔)", required = true, dataType = "String"),

    })
    @PostMapping(value = "updateStockPackName", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateStockPackName(@RequestBody  StockPackPo stockPackPo){
        return warehousePackOrdersService.updateStockPackName(stockPackPo);
    }


    @ApiOperation("后台更改备货列表备注按钮")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主备货表Id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "remark", value = "备注信息", required = true, dataType = "String"),
    })
    @GetMapping(value = "updateBackendStockRemark", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateBackendRemark(Long id,String remark){

        return  warehouseOrderGoodsService.updateWarehouseOrderRemark(id,remark);

    }


    @ApiOperation("后台更改分拣列表备注按钮")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subId", value = "主备货表Id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "remark", value = "备注信息", required = true, dataType = "String"),
    })
    @GetMapping(value = "updateBackendPackOrderRemark", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateBackendPackOrderRemark(Long subId,String remark){
       return warehousePackOrdersService.updateWarehousePackOrderRemark(subId,remark);


    }








}
