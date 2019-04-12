package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.delivery.entity.DeliveryStaff;
import com.zsyc.delivery.entity.DeliveryStaffBill;
import com.zsyc.delivery.po.DeliVeryStaffAo;
import com.zsyc.delivery.po.DeliveryStaffBillPo;
import com.zsyc.delivery.po.DeliveryStaffPo;
import com.zsyc.delivery.service.DeliveryService;
import com.zsyc.delivery.vo.DeliveryStaffVo;
import com.zsyc.store.po.StoreInfoPo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/delivery")
@Api
public class DeliveryController {

    @Reference
    private DeliveryService deliveryService;


    @ApiOperation("通过id查询配送员---后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deliveryId", value = "配送员id", required = true, dataType = "long")})
    @GetMapping(value = "getDeliveryById",produces = MediaType.APPLICATION_JSON_VALUE)
    public DeliveryStaff getDeliveryById(Long deliveryId){
        return deliveryService.getDeliveryById(deliveryId);
    }



    @ApiOperation("门店配送员列表查询---后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "门店id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "currentPage", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "masterName", value = "配送员名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "配送员电话", required = true, dataType = "long"),
            @ApiImplicitParam(name = "masterType", value = "传(STORE-店铺配送)(PLATFORM-平台配送)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "页数", required = true, dataType = "int"),})
    @GetMapping(value = "getDeliveryList",produces = MediaType.APPLICATION_JSON_VALUE)
    public IPage<DeliveryStaff> getDeliveryList(DeliveryStaffPo deliveryStaffPo){
        return deliveryService.getDeliveryList(deliveryStaffPo.getStoreId(), deliveryStaffPo.getCurrentPage(), deliveryStaffPo.getMasterType(),
                            deliveryStaffPo.getPageSize(), deliveryStaffPo.getMasterName(), deliveryStaffPo.getPhone());
    }














    @ApiOperation("后台按条件查找配送员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "masterName", value = "配送员名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "配送员电话", required = true, dataType = "long"),
            @ApiImplicitParam(name = "masterType", value = "传(STORE-店铺配送)(PLATFORM-平台配送)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "currentPage", value = "页数(必传)", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "条数(必传)", required = true, dataType = "Integer")

    })
    @GetMapping(value = "selectDelivery",produces = MediaType.APPLICATION_JSON_VALUE)
    public Object selectDelivery(String masterName,String phone,String masterType,Integer currentPage,Integer pageSize){

        return deliveryService.selectDeliVeryStaff(masterName,phone,masterType,currentPage,pageSize);
    }


    @ApiOperation("后台设置删除该配送员接口")
    @ApiImplicitParams({
                      @ApiImplicitParam(name = "id", value = "配送员表id", required = true, dataType = "long"),
    })
    @GetMapping(value = "setIsDel",produces = MediaType.APPLICATION_JSON_VALUE)
    public Object setIsDel(Long id){

        return deliveryService.setIsDel(id);
    }

    @ApiOperation("后台修改配送员资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "DeliVeryStaffAo.newMasterName", value = "更改的姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "DeliVeryStaffAo.id", value = "配送表id(必传)", required = true, dataType = "long"),
            @ApiImplicitParam(name = "DeliVeryStaffAo.phone", value = "更改的电话", required = true, dataType = "String"),
            @ApiImplicitParam(name = "DeliVeryStaffAo.papers", value = "修改配送员证件图片，身份证正反面，从业资格证", required = true, dataType = "String"),
            @ApiImplicitParam(name = "DeliVeryStaffAo.isLeader", value = "后台修改配送员负责人(0是1否)", required = true, dataType = "String"),

    })
    @PostMapping (value = "updateDeliverAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateDeliverAll(@RequestBody DeliVeryStaffAo deliVeryStaffAo){
        return deliveryService.updateDeliverAll(deliVeryStaffAo);

    }








//    @ApiOperation("后台修改配送员名字")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "newMasterName", value = "更改的姓名", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "id", value = "配送表id", required = true, dataType = "long"),
//    })
//    @GetMapping(value = "updateMasterName",produces = MediaType.APPLICATION_JSON_VALUE)
//    public Object updateMasterName(String newMasterName,Long id ){
//        return deliveryService.updateMasterName(newMasterName,id);
//    }
//
//    @ApiOperation("后台修改配送员名字")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "phone", value = "更改的电话", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "id", value = "配送表id", required = true, dataType = "long"),
//    })
//    @GetMapping(value = "updateMasterPhone",produces = MediaType.APPLICATION_JSON_VALUE)
//    public Object updateMasterPhone(String phone,Long id ){
//
//        return deliveryService.updateMasterPhone(phone,id);
//    }

//    @ApiOperation("后台修改配送员证件图片，身份证正反面，从业资格证")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "papers", value = "图片的core", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "id", value = "配送表id", required = true, dataType = "long"),
//    })
//    @GetMapping(value = "updatePapers",produces = MediaType.APPLICATION_JSON_VALUE)
//    public Object updatePapers(String papers,Long id ){
//        return deliveryService.updatePapersPapers(papers,id);
//    }

//    @ApiOperation("后台修改配送员负责人")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "isLeader", value = "0是1否", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "id", value = "配送表id", required = true, dataType = "long"),
//    })
//    @GetMapping(value = "updateIsLeader",produces = MediaType.APPLICATION_JSON_VALUE)
//    public Object updateIsLeader(int isLeader,Long id ){
//        return deliveryService.updateIsLeader(isLeader,id);
//    }










    @ApiOperation("后台按条件查找配送员工资列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "masterName", value = "配送员名字", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "配送员电话", required = true, dataType = "String"),
            @ApiImplicitParam(name = "billStatus ", value = "状态 DONE-已结算，UNPAID-未结算", required = true, dataType = "String"),
            @ApiImplicitParam(name = "turnInStatus ", value = "已上缴-DONE，未上缴-UNPAID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "storeId ", value = "门店Id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "currentPage", value = "页数(必传)", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "条数(必传)", required = true, dataType = "Integer")
    })
    @GetMapping(value = "deliveryStaffBill",produces = MediaType.APPLICATION_JSON_VALUE)
    public Object deliveryStaffBill(String masterName,String phone,String billStatus,String turnInStatus,Integer currentPage,Integer pageSize,Long storeId){
        return  deliveryService.deliveryStaffBill(masterName,phone,billStatus,turnInStatus,currentPage,pageSize,storeId);
    }








}

