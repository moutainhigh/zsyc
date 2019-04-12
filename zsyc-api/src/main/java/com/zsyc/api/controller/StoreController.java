package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.kevinsawicki.http.HttpRequest;
import com.zsyc.api.AccountHelper;
import com.zsyc.delivery.po.DeliveryStaffPo;
import com.zsyc.goods.bo.GoodCustomBo;
import com.zsyc.goods.bo.GoodVipBo;
import com.zsyc.goods.entity.GoodsInfo;
import com.zsyc.goods.vo.GoodCustomVo;
import com.zsyc.goods.vo.GoodsPriceInfoVO;
import com.zsyc.member.entity.MemberAddress;
import com.zsyc.member.po.MemberAddressPo;
import com.zsyc.member.po.MemberAddressStorePo;
import com.zsyc.order.vo.StockVo;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.store.po.StoreInfoPo;
import com.zsyc.store.po.StoreOpenTimePo;
import com.zsyc.store.service.StoreInfoService;
import com.zsyc.warehouse.entity.WarehouseStaff;
import com.zsyc.warehouse.po.WarehousePo;
import com.zsyc.warehouse.po.WarehousePo2;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.zsyc.common.QRCodeUtil.*;

@RestController
@RequestMapping("api/store")
@Api
public class StoreController {

    @Reference
    private StoreInfoService storeInfoService;

    @Autowired
    private AccountHelper accountHelper;

    @ApiOperation("获取门店列表---后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeName", value = "门店名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "storeNo", value = "店铺编码", required = true, dataType = "string"),
            @ApiImplicitParam(name = "currentPage", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "storeTypeId", value = "门店类型", required = true, dataType = "int"),
            @ApiImplicitParam(name = "adcode", value = "最后一级地区编码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "beginTime", value = "开始时间", required = true, dataType = "string"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "string")})
    @GetMapping(value = "getStoreList",produces = MediaType.APPLICATION_JSON_VALUE)
    public IPage<StoreInfo> getStoreList(StoreInfoPo storeInfoPo) {

        return storeInfoService.getStoreList(storeInfoPo.getStoreName(),
                                            storeInfoPo.getStoreNo(),
                                            storeInfoPo.getCurrentPage(),
                                            storeInfoPo.getPageSize(),
                                            storeInfoPo.getStoreTypeId(),
                                            storeInfoPo.getBeginTime(),
                                            storeInfoPo.getEndTime(),
                                            storeInfoPo.getAdcode(),
                                            accountHelper.getUser().getId());
    }


    @ApiOperation("获取门店地址经纬度---后台")
    @ApiImplicitParam(name = "addressName", value = "地址名称", required = true, dataType = "string")
    @GetMapping(value = "getStoreAddress", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getStoreAddress(String addressName) {
        HttpRequest httpRequest = HttpRequest.get("http://api.map.baidu.com/geocoder/v2/?ak=eIxDStjzbtH0WtU50gqdXYCz" +
                "&output=json&address=" + addressName);

        return httpRequest.body();
    }

    @ApiOperation("新增门店----后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeName", value = "门店名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "carriage", value = "楼层运费", required = true, dataType = "int"),
            @ApiImplicitParam(name = "picture", value = "门店图片", required = true, dataType = "list"),
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "string"),
            @ApiImplicitParam(name = "address", value = "详细地址", required = true, dataType = "string"),
            @ApiImplicitParam(name = "rent", value = "门店租金", required = true, dataType = "int"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = true, dataType = "double"),
            @ApiImplicitParam(name = "adcode", value = "最后一级地区编码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true, dataType = "double"),
            @ApiImplicitParam(name = "scopeBusiness", value = "经营范围", required = true, dataType = "string"),
            @ApiImplicitParam(name = "radius", value = "配送范围", required = true, dataType = "string"),
            @ApiImplicitParam(name = "storeTypeId", value = "门店类型", required = true, dataType = "long")})
    @PostMapping(value = "addStore", produces = MediaType.APPLICATION_JSON_VALUE)
    public int addStore(@RequestBody StoreInfoPo storeInfoPo) {
        return storeInfoService.addStore(storeInfoPo.getStoreName(),
                                        storeInfoPo.getPhone(),
                                        storeInfoPo.getCarriage(),
                                        storeInfoPo.getRent(),
                                        storeInfoPo.getLongitude(),
                                        storeInfoPo.getLatitude(),
                                        storeInfoPo.getAddress(),
                                        storeInfoPo.getStoreTypeId(),
                                        storeInfoPo.getScopeBusiness(),
                                        storeInfoPo.getPicture(),
                                        storeInfoPo.getRadius(),
                                        storeInfoPo.getAdcode(),
                                        accountHelper.getUser().getId());
    }

    @ApiOperation("修改门店----后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeName", value = "门店名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "carriage", value = "楼层运费", required = true, dataType = "int"),
            @ApiImplicitParam(name = "picture", value = "门店图片", required = true, dataType = "list"),
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "string"),
            @ApiImplicitParam(name = "address", value = "详细地址", required = true, dataType = "string"),
            @ApiImplicitParam(name = "rent", value = "门店租金", required = true, dataType = "int"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = true, dataType = "double"),
            @ApiImplicitParam(name = "adcode", value = "最后一级地区编码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true, dataType = "double"),
            @ApiImplicitParam(name = "scopeBusiness", value = "经营范围", required = true, dataType = "string"),
            @ApiImplicitParam(name = "radius", value = "配送范围", required = true, dataType = "string"),
            @ApiImplicitParam(name = "id", value = "门店id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "storeTypeId", value = "门店类型", required = true, dataType = "long")})
    @PostMapping(value = "updateStore", produces = MediaType.APPLICATION_JSON_VALUE)
    public int updateStore(@RequestBody StoreInfoPo storeInfoPo) {
        return storeInfoService.updateStore(storeInfoPo.getStoreName(),
                storeInfoPo.getPhone(),
                storeInfoPo.getCarriage(),
                storeInfoPo.getRent(),
                storeInfoPo.getLongitude(),
                storeInfoPo.getLatitude(),
                storeInfoPo.getAddress(),
                storeInfoPo.getStoreTypeId(),
                storeInfoPo.getScopeBusiness(),
                storeInfoPo.getPicture(),
                storeInfoPo.getRadius(),
                storeInfoPo.getAdcode(),
                storeInfoPo.getId(),
                accountHelper.getUser().getId());
    }

    @ApiOperation("删除门店---后台")
    @PostMapping(value = "deleteStore", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "ids", value = "门店ids", required = true)
    public int deleteStore(@RequestBody Map<String, Object> map) {
        List<Long> ids = (List<Long>)map.get("ids");
        return storeInfoService.deleteStore(ids, accountHelper.getUser().getId());
    }

    @ApiOperation("通过id查询门店---后台")
    @GetMapping(value = "getStoreById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "storeId", value = "门店id", required = true)
    public StoreInfo getStoreById(Long storeId) {
        return storeInfoService.getStoreById(storeId);
    }


    @ApiOperation("门店商品库存接口----后台")
    @PostMapping(value = "storeGoodStock", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stockVos", value = "组合商品list", required = true, dataType = "list"),
            @ApiImplicitParam(name = "type", value = "类型：1增加，2减少", required = true, dataType = "int")})
    public Integer storeGoodStock(@RequestBody Map<String, Object> map){
        List<StockVo> stockVos = (List<StockVo>)map.get("stockVos");
        Integer type = (Integer)map.get("type");
        return storeInfoService.storeGoodStock(stockVos, type, accountHelper.getUser().getId());
    }

    @ApiOperation("添加配送员接口----后台")
    @PostMapping(value = "addDelivery", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "papers", value = "证件图片，身份证正反面，从业资格证", required = true, dataType = "list"),
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "string"),
            @ApiImplicitParam(name = "masterType", value = "配送员类型（有门店id就不用这个字段）'STORE-店铺配送，PLATFORM-平台配送'", required = true, dataType = "int"),
            @ApiImplicitParam(name = "masterName", value = "名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "storeId", value = "店铺id", required = true, dataType = "long")})
    public Integer addDelivery(@RequestBody DeliveryStaffPo deliveryStaffPo){
        return storeInfoService.addDelivery(deliveryStaffPo.getStoreId(), deliveryStaffPo.getMasterName(), deliveryStaffPo.getPhone(),
                deliveryStaffPo.getMasterType(), deliveryStaffPo.getPapers(), accountHelper.getUser().getId());
    }

    @ApiOperation("修改配送员接口----后台")
    @PostMapping(value = "updateDelivery", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "papers", value = "证件图片，身份证正反面，从业资格证", required = true, dataType = "list"),
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "string"),
            @ApiImplicitParam(name = "masterName", value = "名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "id", value = "配送员id", required = true, dataType = "long")})
    public Integer updateDelivery(@RequestBody DeliveryStaffPo deliveryStaffPo){
        return storeInfoService.updateDelivery(deliveryStaffPo.getId(), deliveryStaffPo.getMasterName(), deliveryStaffPo.getPhone(),
                                 deliveryStaffPo.getPapers(), accountHelper.getUser().getId());
    }

    @ApiOperation("门店删除配送员接口----后台")
    @PostMapping(value = "deleteDelivery", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "配送员ids", required = true, dataType = "list")})
    public Integer deleteDelivery(@RequestBody Map<String, Object> map){
        List<Long> ids = (List<Long>)map.get("ids");
        return storeInfoService.deleteDelivery(ids, accountHelper.getUser().getId());
    }


    @ApiOperation("门店添加备货分拣员接口----后台")
    @PostMapping(value = "addWarehouseStaff", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stockName", value = "名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "string"),
            @ApiImplicitParam(name = "stockType", value = "人员类型 STOCK-备货，SORT-分拣", required = true, dataType = "string"),
            @ApiImplicitParam(name = "idCard", value = "身份证证件号", required = true, dataType = "string"),
            @ApiImplicitParam(name = "idCardImg", value = "身份证正反面图片，逗号分割", required = true, dataType = "string"),
            @ApiImplicitParam(name = "storeId", value = "店铺id", required = true, dataType = "long")})
    public Integer addWarehouseStaff(@RequestBody WarehousePo2 warehousePo2){
        return storeInfoService.addWarehouseStaff(warehousePo2.getStockName(), warehousePo2.getStoreId(), warehousePo2.getPhone(),
                warehousePo2.getIdCard(), warehousePo2.getIdCardImg(), warehousePo2.getStockType(), accountHelper.getUser().getId());
    }


    @ApiOperation("门店修改分拣员接口----后台")
    @PostMapping(value = "updateWarehouseStaff", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stockName", value = "名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "string"),
            @ApiImplicitParam(name = "stockType", value = "人员类型 STOCK-备货，SORT-分拣", required = true, dataType = "string"),
            @ApiImplicitParam(name = "idCard", value = "身份证证件号", required = true, dataType = "string"),
            @ApiImplicitParam(name = "idCardImg", value = "身份证正反面图片，逗号分割", required = true, dataType = "string"),
            @ApiImplicitParam(name = "deliveryId", value = "分拣员id", required = true, dataType = "long")})
    public Integer updateWarehouseStaff(@RequestBody WarehousePo2 warehousePo2){
        return storeInfoService.updateWarehouseStaff(warehousePo2.getId(), warehousePo2.getStockName(), warehousePo2.getPhone(),
                warehousePo2.getIdCard(), warehousePo2.getIdCardImg(), warehousePo2.getStockType(), accountHelper.getUser().getId());
    }


    @ApiOperation("门店删除分拣员接口----后台")
    @PostMapping(value = "deleteWarehouseStaff", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "分拣员ids", required = true, dataType = "list")})
    public Integer deleteWarehouseStaff(@RequestBody Map<String, Object> map){
        List<Long> ids = (List<Long>)map.get("ids");
        return storeInfoService.deleteWarehouseStaff(ids, accountHelper.getUser().getId());
    }

    @ApiOperation("门店查询分拣员接口----后台")
    @GetMapping(value = "getWarehouseStaff", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "stockType", value = "类型STOCK-备货，SORT-分拣'", required = true, dataType = "string"),
            @ApiImplicitParam(name = "stockName", value = "名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "string"),
            @ApiImplicitParam(name = "storeId", value = "门店id(不传就是查所有，超级管理员权限)", required = true, dataType = "long")})
    public IPage<WarehouseStaff> getWarehouseStaff(WarehousePo2 warehousePo2){
        return storeInfoService.getWarehouseStaff(warehousePo2.getStoreId(), warehousePo2.getStockName(), warehousePo2.getPhone(),
                warehousePo2.getCurrentPage(), warehousePo2.getPageSize(), warehousePo2.getStockType());
    }


    @ApiOperation("通过id查询分拣员接口----后台")
    @GetMapping(value = "getWarehouseStaffById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "warehouseStaffId", value = "分拣员id", required = true, dataType = "long")})
    public WarehouseStaff getWarehouseStaffById(Long warehouseStaffId){
        return storeInfoService.getWarehouseStaffById(warehouseStaffId);
    }


    @ApiOperation("门店开盘收盘时间设定接口----后台")
    @PostMapping(value = "storeOpenTime", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openTime", value = "开始时间", required = true, dataType = "string"),
            @ApiImplicitParam(name = "closeTime", value = "结束时间", required = true, dataType = "string"),
            @ApiImplicitParam(name = "storeId", value = "店铺id", required = true, dataType = "long")})
    public Integer storeOpenTime(@RequestBody StoreOpenTimePo storeOpenTimePo){

        return storeInfoService.storeOpenTime(storeOpenTimePo.getStoreId(),
                                                storeOpenTimePo.getOpenTime(),
                                                storeOpenTimePo.getCloseTime());
    }

    @ApiOperation("查询门店根据地址自定义价格的商品----小程序")
    @GetMapping(value = "getGoodsCustom", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "addressId", value = "地址id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "categoryId", value = "商品类型", required = true, dataType = "int"),
            @ApiImplicitParam(name = "storeId", value = "门店id", required = true, dataType = "long")})
    public IPage<GoodCustomVo> getGoodsCustom(GoodCustomBo goodCustomBo){

        return storeInfoService.getGoodsCustom(goodCustomBo.getStoreId(),
                                                goodCustomBo.getCategoryId(),
                                                goodCustomBo.getCurrentPage(),
                                                goodCustomBo.getPageSize(),
                                                goodCustomBo.getAddressId());
    }

    @ApiOperation("门店用户地址列表接口----后台")
    @GetMapping(value = "getAddressByStoreId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "门店id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "currentPage", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "consignee", value = "收货人", required = true, dataType = "string"),
            @ApiImplicitParam(name = "telephone", value = "收货人电话", required = true, dataType = "string"),
            @ApiImplicitParam(name = "adcode", value = "最后一级区域编码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页数", required = true, dataType = "int"),})
    public IPage<MemberAddress> getAddressByStoreId(MemberAddressPo memberAddressPo){
        return storeInfoService.getAddressByStoreId(memberAddressPo.getStoreId(),
                                                    memberAddressPo.getCurrentPage(),
                                                    memberAddressPo.getPageSize(),
                                                    memberAddressPo.getConsignee(),
                                                    memberAddressPo.getTelephone(),
                                                    memberAddressPo.getLocationAddress(),
                                                    memberAddressPo.getAdcode());
    }


    @ApiOperation("门店设置用户地址类型----后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addressId", value = "地址id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "addressType", value = "（'NORMAL':普通会员,'VIP':vip会员）", required = true, dataType = "string"),
            @ApiImplicitParam(name = "isCustom", value = "是否为自定义，0不是1是", required = true, dataType = "int"),})
    @PostMapping(value = "updateUserType", produces = MediaType.APPLICATION_JSON_VALUE)
    public int updateUserType(@RequestBody MemberAddressStorePo memberAddressStorePo){
        return storeInfoService.updateUserType(memberAddressStorePo.getAddressId(), memberAddressStorePo.getAddressType(),
                                        memberAddressStorePo.getIsCustom());
    }

    @ApiOperation("门店添加自定义价格商品----后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "门店id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "sku", value = "商品sku", required = true, dataType = "string"),
            @ApiImplicitParam(name = "addressId", value = "地址id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataType = "int"),})
    @PostMapping(value = "addCustomGoods", produces = MediaType.APPLICATION_JSON_VALUE)
    public int addCustomGoods(@RequestBody GoodCustomBo goodCustomBo){
        return storeInfoService.addCustomGoods(goodCustomBo.getStoreId(), goodCustomBo.getAddressId(), goodCustomBo.getGoodsId(),
                goodCustomBo.getPrice(), goodCustomBo.getSku(), accountHelper.getUser().getId());
    }

    @ApiOperation("门店修改自定义价格商品----后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sku", value = "商品sku", required = true, dataType = "string"),
            @ApiImplicitParam(name = "addressId", value = "地址id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataType = "int"),})
    @PostMapping(value = "updateCustomGoods", produces = MediaType.APPLICATION_JSON_VALUE)
    public int updateCustomGoods(@RequestBody GoodCustomBo goodCustomBo){
        return storeInfoService.updateCustomGoods(goodCustomBo.getSku(), goodCustomBo.getPrice(), goodCustomBo.getAddressId(), accountHelper.getUser().getId());
    }

    @ApiOperation("门店添加商品vip价格----后台")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "currentPage", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "VipPrice", value = "vip价格", required = true, dataType = "int"),})
    @PostMapping(value = "addVipPrice", produces = MediaType.APPLICATION_JSON_VALUE)
    public int addVipPrice(@RequestBody GoodVipBo goodVipBo){
        return storeInfoService.addVipPrice(goodVipBo.getSku(), goodVipBo.getVipPrice(), accountHelper.getUser().getId());
    }

//    @ApiOperation("门店二维码生成----后台")
//    @GetMapping(value = "QRCode", produces = MediaType.APPLICATION_JSON_VALUE)
//    public String QRCode(){
//        String url = "192.168.0.117:8088/api/store/UserBindingStore?storeId=144";//二维码内容
//        int width = 500; // 二维码图片的宽
//        int height = 500; // 二维码图片的高
//        String format = "png"; // 二维码图片的格式
//
//        String QRCodeUrl = null;
//        try {
//            QRCodeUrl = createQRCode(url, width, height, format);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return QRCodeUrl;
//    }

    @ApiOperation("用户扫二维码绑定门店接口--小程序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "门店id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "addressId", value = "默认地址id", required = true, dataType = "long"),})
    @ApiResponses(value = {
            @ApiResponse(code = 1, message = "地址已绑定该类型的门店"),
            @ApiResponse(code = 2, message = "绑定成功")})
    @PostMapping(value = "userBindingStore", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map userBindingStore(@RequestBody Map<String, Object> map){
        Long addressId = Long.valueOf(map.get("addressId").toString());
        Long storeId = Long.valueOf(map.get("storeId").toString());
        return storeInfoService.userBindingStore(addressId, storeId);
    }

}
