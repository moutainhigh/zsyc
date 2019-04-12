package com.zsyc.store.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.admin.entity.Role;
import com.zsyc.admin.entity.UserRole;
import com.zsyc.admin.mapper.RoleMapper;
import com.zsyc.admin.mapper.RoleStoreMapper;
import com.zsyc.admin.mapper.UserRoleMapper;
import com.zsyc.common.AssertExt;
import com.zsyc.common.CommonConstant;
import com.zsyc.delivery.entity.DeliveryStaff;
import com.zsyc.delivery.mapper.DeliveryStaffMapper;
import com.zsyc.goods.bo.GoodsAttributeValueBO;
import com.zsyc.goods.entity.*;
import com.zsyc.goods.mapper.*;
import com.zsyc.goods.service.GoodsStorePriceService;
import com.zsyc.goods.vo.GoodCustomVo;
import com.zsyc.goods.vo.GoodsPriceInfoVO;
import com.zsyc.member.entity.MemberAddress;
import com.zsyc.member.entity.MemberAddressStore;
import com.zsyc.member.mapper.MemberAddressMapper;
import com.zsyc.member.mapper.MemberAddressStoreMapper;
import com.zsyc.member.service.MemberInfoService;
import com.zsyc.member.vo.MemberAddressStoreVo;
import com.zsyc.member.vo.MemberAddressVo;
import com.zsyc.order.vo.StockVo;
import com.zsyc.store.entity.StoreDeliveryRelation;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.store.entity.StoreOpenTime;
import com.zsyc.store.entity.StoreWarehouseStaffRelation;
import com.zsyc.store.mapper.StoreDeliveryRelationMapper;
import com.zsyc.store.mapper.StoreInfoMapper;

import com.zsyc.store.mapper.StoreOpenTimeMapper;
import com.zsyc.store.mapper.StoreWarehouseStaffRelationMapper;
import com.zsyc.store.vo.StoreInfoVo;
import com.zsyc.warehouse.entity.WarehouseStaff;
import com.zsyc.warehouse.mapper.WarehouseStaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.zsyc.common.LocationUtils.getmeter;



@Service
public class StoreInfoImpl implements StoreInfoService{

    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private MemberAddressMapper memberAddressMapper;

    @Autowired
    private MemberAddressStoreMapper memberAddressStoreMapper;

    @Autowired
    private DeliveryStaffMapper deliveryStaffMapper;

    @Autowired
    private StoreDeliveryRelationMapper storeDeliveryRelationMapper;

    @Autowired
    private StoreWarehouseStaffRelationMapper storeWarehouseStaffRelationMapper;

    @Autowired
    private WarehouseStaffMapper warehouseStaffMapper;

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;

    @Autowired
    private StoreOpenTimeMapper storeOpenTimeMapper;

    @Autowired
    private GoodsStoreStockLogMapper goodsStoreStockLogMapper;

    @Autowired
    private GoodsAttributeRelationMapper goodsAttributeRelationMapper;

    @Autowired
    private GoodsCustomPriceMapper goodsCustomPriceMapper;

    @Autowired
    private GoodsStorePriceMapper goodsStorePriceMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleStoreMapper roleStoreMapper;

    @Autowired
    private MemberInfoService memberInfoService;

    @Value("${zsyc.admin.role-ids}")
    private List<Long> adminRoleIds;

    @Override
    public IPage<StoreInfo> getStoreList(String storeName,  String storeNo, Integer currentPage, Integer pageSize,
                                         Long storeType, String beginTime, String endTime, Integer adcode, Long loginUserId) {

        if(StringUtils.isBlank(storeName))storeName = null;
        if(StringUtils.isBlank(storeNo) )storeNo = null;
        if(currentPage == null)currentPage = 1;
        if(pageSize == null)pageSize = 10;


        //获取用户的角色关联表
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", loginUserId);
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);

        //把角色id拿出
        List<Long> rolesdIds = new ArrayList<>();
        for(UserRole userRole: userRoles){
            rolesdIds.add(userRole.getRoleId());
        }

        IPage<StoreInfo> page = new Page<StoreInfo>(currentPage, pageSize);
        IPage<StoreInfo> storeInfoVoPage = storeInfoMapper.getStoreList(page, storeName, storeNo, storeType, beginTime, endTime, adcode, rolesdIds);
        return storeInfoVoPage;
    }

    @Override
    @Transactional
    public int addStore(String storeName, String telephone, Integer carriage, Integer rent, Double longitude, Double latitude,
                        String address, Long storeType, String scope, List<String> picture1, String radius1, Integer adcode, Long loginUserId) {

        AssertExt.notBlank(storeName, "店铺名称为空");
        AssertExt.notBlank(telephone, "电话为空");
        AssertExt.notNull(carriage, "carriage为空");
        AssertExt.notNull(rent, "rent为空");
        AssertExt.notNull(longitude, "longitude为空");
        AssertExt.notNull(latitude, "latitude为空");
        AssertExt.notBlank(address, "address为空");
        AssertExt.notNull(storeType, "storeTypeId为空");
        AssertExt.notBlank(scope, "scope为空");
        AssertExt.notBlank(radius1, "radius1为空");
        AssertExt.notNull(adcode, "acdode为空");

        String picture = new String();
        for(int i = 0; i < picture1.size(); i ++){
            picture = picture + picture1.get(i) + ",";
        }


        StoreInfo storeInfo = new StoreInfo();
        storeInfo.setStoreName(storeName);
        storeInfo.setPhone(telephone);
        storeInfo.setCarriage(carriage);
        storeInfo.setRent(rent);
        storeInfo.setLongitude(longitude);
        storeInfo.setLatitude(latitude);
        storeInfo.setAddress(address);
        storeInfo.setStoreTypeId(storeType);
        storeInfo.setScopeBusiness(scope);
        storeInfo.setPicture(picture);
        storeInfo.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
        storeInfo.setRadius(Integer.valueOf(radius1)*1000);
        storeInfo.setPricing(StoreInfo.Pricing.CAN.name());
        storeInfo.setAdcode(adcode);
        storeInfoMapper.insert(storeInfo);

        LocalDateTime now = LocalDateTime.now();

        //判断有没用户地址没绑定门店的
        List<MemberAddress> memberAddressList = memberAddressMapper.getMemberAddressNoStore(storeType);
        if(memberAddressList.size() > 0){
            MemberAddressStore memberAddressStore = null;
            List<MemberAddressStore> memberAddressStoreList = new ArrayList<>();
            for(MemberAddress memberAddress: memberAddressList){

                //判断用户地址距离在不在服务范围,单位米
                double radius = getmeter(memberAddress.getLat(),memberAddress.getLng(), storeInfo.getLatitude(), storeInfo.getLongitude());
                if(storeInfo.getRadius() >= (int)radius){
                    memberAddressStore = new MemberAddressStore();
                    memberAddressStore.setAddressId(memberAddress.getId());
                    memberAddressStore.setStoreId(storeInfo.getId());
                    memberAddressStore.setCreateTime(now);
                    memberAddressStore.setAddressType(MemberAddressStore.AddressType.NORMAL.name());
                    memberAddressStore.setIsDel(0);
                    memberAddressStore.setUpdateTime(now);
                    memberAddressStore.setCreateUserId(loginUserId);
                    memberAddressStore.setUpdateUserId(loginUserId);
                    memberAddressStore.setIsCustom(0);

                    memberAddressStoreList.add(memberAddressStore);
                }

            }
            if(memberAddressStoreList.size() > 0){
                memberAddressStoreMapper.insertList(memberAddressStoreList);
            }
        }

        //给这个用户的所有角色绑定门店
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", loginUserId);
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);

        //把角色id拿出
        List<Long> rolesdIds = new ArrayList<>();
        for(UserRole userRole: userRoles){
            rolesdIds.add(userRole.getRoleId());
        }

        //添加超级管理员角色id
        for(Long roleId: adminRoleIds){
            rolesdIds.add(roleId);
        }

        //去重
        HashSet h = new HashSet(rolesdIds);
        rolesdIds.clear();
        rolesdIds.addAll(h);

        //批量把一个用户的所有角色绑定一个门店
        roleStoreMapper.insertList(rolesdIds, storeInfo.getId());

        return 1;
    }


    @Override
    @Transactional
    public int updateStore(String storeName, String telephone, Integer carriage, Integer rent, Double longitude, Double latitude,
                        String address, Long storeType, String scope, List<String> picture1, String radius1, Integer adcode, Long storeId, Long loginUserId) {

        AssertExt.notBlank(storeName, "店铺名称为空");
        AssertExt.notBlank(telephone, "电话为空");
        AssertExt.notNull(carriage, "carriage为空");
        AssertExt.notNull(storeId, "id为空");
        AssertExt.notNull(rent, "rent为空");
        AssertExt.notNull(longitude, "longitude为空");
        AssertExt.notNull(latitude, "latitude为空");
        AssertExt.notBlank(address, "address为空");
        AssertExt.notNull(storeType, "storeTypeId为空");
        AssertExt.notBlank(scope, "scope为空");
        AssertExt.notBlank(radius1, "radius1为空");
        AssertExt.notNull(adcode, "acdode为空");


        String picture = new String();
        for(int i = 0; i < picture1.size(); i ++){
            picture = picture + picture1.get(i) + ",";
        }

        LocalDateTime now = LocalDateTime.now();

        StoreInfo storeInfo = new StoreInfo();
        UpdateWrapper<StoreInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", storeId);
        updateWrapper.set("store_name", storeName);
        updateWrapper.set("phone", telephone);
        updateWrapper.set("carriage", carriage);
        updateWrapper.set("rent", rent);
        updateWrapper.set("longitude", longitude);
        updateWrapper.set("latitude", latitude);
        updateWrapper.set("address", address);
        updateWrapper.set("store_type_id", storeType);
        updateWrapper.set("scope_business", scope);
        updateWrapper.set("picture", picture);
        updateWrapper.set("radius", radius1);
        updateWrapper.set("adcode", adcode);
        updateWrapper.set("update_time", now);
        updateWrapper.set("update_user_id", loginUserId);

        storeInfoMapper.update(storeInfo, updateWrapper);
        return 1;
    }



    @Override
    @Transactional
    public int deleteStore(List<Long> ids, Long loginUserId) {
        return storeInfoMapper.deleteStoreByIds(ids, loginUserId);
    }

    @Override
    @Transactional
    public Integer storeGoodStock(List<StockVo> stockVos, Integer type, Long loginUserId) {

        LocalDateTime now = LocalDateTime.now();
        AssertExt.isTrue((type.intValue() == 1 || type.intValue() == 2), "类型错误");

        //组合商品更改库存，子商品数量一般都是几个
        for(int i = 0; i<stockVos.size(); i++){
            StockVo entity = JSON.parseObject(JSON.toJSONString(stockVos.get(i)), StockVo.class);
            storeInfoMapper.storeGoodStock(entity.getSku(), entity.getNum(), type, entity.getStoreId());

            //插入库存日志
            GoodsStoreStockLog goodsStoreStockLog = new GoodsStoreStockLog();
            goodsStoreStockLog.setSku(entity.getSku());
            goodsStoreStockLog.setStoreId(entity.getStoreId());
            goodsStoreStockLog.setNum(Long.valueOf(entity.getNum()));
            goodsStoreStockLog.setOperate(type.intValue() == 1 ? "add" : "reduce");
            goodsStoreStockLog.setCreateUserId(loginUserId);
            goodsStoreStockLog.setCreateTime(now);
            goodsStoreStockLogMapper.insert(goodsStoreStockLog);
        }

        return 1;
    }

    @Override
    @Transactional
    public Integer addDelivery(Long storeId, String name, String phone, String type, List<String> papers, Long loginUserId) {
        AssertExt.notBlank(name, "masterName为空");
        AssertExt.notNull(storeId, "storeId为空");
        AssertExt.notBlank(phone, "phone为空");

        String picture = new String();
        for(int i = 0; i < papers.size(); i ++){
            picture = picture + papers.get(i) + ",";
        }

        LocalDateTime now = LocalDateTime.now();

        DeliveryStaff deliveryStaff = new DeliveryStaff();
        deliveryStaff.setMasterName(name);
        deliveryStaff.setAccountId(1l);
        deliveryStaff.setPhone(phone);
        deliveryStaff.setPapers(picture);
        deliveryStaff.setParentId(0l);
        deliveryStaff.setIsLeader(1);
        deliveryStaff.setMasterType(storeId == null ? type : "STORE");
        deliveryStaff.setCreateTime(now);
        deliveryStaff.setCreateUserId(loginUserId);
        deliveryStaff.setUpdateTime(now);
        deliveryStaff.setUpdateUserId(loginUserId);
        deliveryStaff.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
        deliveryStaffMapper.insert(deliveryStaff);

        //添加门店和配送员关系
        if(storeId != null ){
            StoreDeliveryRelation storeDeliveryRelation = new StoreDeliveryRelation();
            storeDeliveryRelation.setMasterId(deliveryStaff.getId());
            storeDeliveryRelation.setStoreId(storeId);
            storeDeliveryRelation.setCreateTime(now);
            storeDeliveryRelation.setCreateUserId(loginUserId);
            storeDeliveryRelationMapper.insert(storeDeliveryRelation);
        }

        return 1;
    }

    @Override
    @Transactional
    public int updateDelivery(Long deliveryId, String name, String phone, List<String> papers, Long loginUserId) {
        AssertExt.notNull(deliveryId, "id为空");
        AssertExt.notBlank(name, "masterName为空");
        AssertExt.notBlank(phone, "phone为空");

        String picture = new String();
        for(int i = 0; i < papers.size(); i ++){
            picture = picture + papers.get(i) + ",";
        }


        LocalDateTime now = LocalDateTime.now();

        DeliveryStaff deliveryStaff = new DeliveryStaff();
        UpdateWrapper<DeliveryStaff> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", deliveryId);
        updateWrapper.set("phone", phone);
        updateWrapper.set("papers", picture);
        updateWrapper.set("master_name", name);
        updateWrapper.set("update_time", now);
        updateWrapper.set("update_user_id", loginUserId);

        return deliveryStaffMapper.update(deliveryStaff, updateWrapper);
    }

    @Override
    @Transactional
    public int deleteDelivery(List<Long> ids, Long loginUserId) {
        return deliveryStaffMapper.deleteDelivery(ids, loginUserId);
    }

    @Override
    @Transactional
    public Integer addWarehouseStaff(String name, Long storeId, String phone, String idCard, String idCardImg, String stockType, Long loginUserId) {
        AssertExt.notBlank(name, "stockName为空");
        AssertExt.notBlank(phone, "phone为空");
        AssertExt.notBlank(idCard, "idCard为空");
        AssertExt.notBlank(idCardImg, "idCardImg为空");
        AssertExt.notBlank(stockType, "stockType为空");
        AssertExt.notNull(storeId, "storeId为空");

        LocalDateTime now = LocalDateTime.now();

        WarehouseStaff warehouseStaff = new WarehouseStaff();
        warehouseStaff.setStockName(name);
        warehouseStaff.setPhone(phone);
        warehouseStaff.setIdCard(idCard);
        warehouseStaff.setIdCardImg(idCardImg);
        warehouseStaff.setStockType(stockType);
        warehouseStaff.setCreateTime(now);
        warehouseStaff.setCreateUserId(loginUserId);
        warehouseStaff.setUpdateTime(now);
        warehouseStaff.setUpdateUserId(loginUserId);
        warehouseStaff.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
        warehouseStaffMapper.insert(warehouseStaff);

        //关联表
        StoreWarehouseStaffRelation storeWarehouseStaffRelation = new StoreWarehouseStaffRelation();
        storeWarehouseStaffRelation.setWarehouseStaffId(warehouseStaff.getId());
        storeWarehouseStaffRelation.setStoreId(storeId);
        storeWarehouseStaffRelation.setCreateTime(now);
        storeWarehouseStaffRelation.setCreateUserId(0l);
        storeWarehouseStaffRelationMapper.insert(storeWarehouseStaffRelation);
        return 1;
    }

    @Override
    @Transactional
    public int updateWarehouseStaff(Long warehouseStaffId, String name, String phone, String idCard, String idCardImg, String stockType, Long loginUserId) {
        AssertExt.notBlank(name, "stockName为空");
        AssertExt.notBlank(phone, "phone为空");
        AssertExt.notBlank(idCard, "idCard为空");
        AssertExt.notBlank(idCardImg, "idCardImg为空");
        AssertExt.notBlank(stockType, "stockType为空");
        AssertExt.notNull(warehouseStaffId, "id为空");

        LocalDateTime now = LocalDateTime.now();

        WarehouseStaff warehouseStaff = new WarehouseStaff();
        UpdateWrapper<WarehouseStaff> warehouseStaffUpdateWrapper = new UpdateWrapper<>();
        warehouseStaffUpdateWrapper.eq("id", warehouseStaffId);
        warehouseStaffUpdateWrapper.set("stock_name", name);
        warehouseStaffUpdateWrapper.set("phone", phone);
        warehouseStaffUpdateWrapper.set("id_card", idCard);
        warehouseStaffUpdateWrapper.set("id_card_img", idCardImg);
        warehouseStaffUpdateWrapper.set("stock_type", stockType);
        warehouseStaffUpdateWrapper.set("update_time", now);
        warehouseStaffUpdateWrapper.set("update_user_id", loginUserId);

        return warehouseStaffMapper.update(warehouseStaff, warehouseStaffUpdateWrapper);
    }

    @Override
    @Transactional
    public Integer deleteWarehouseStaff(List<Long> ids, Long loginUserId) {

        return warehouseStaffMapper.deleteWarehouseStaff(ids, loginUserId);
    }


    @Override
    @Transactional
    public Integer storeOpenTime(Long storeId, String openTime, String closeTime) {
        AssertExt.notBlank(openTime, "openTime为空");
        AssertExt.notBlank(closeTime, "closeTime为空");
        AssertExt.notNull(storeId, "storeId为空");

        StoreOpenTime storeOpenTime = new StoreOpenTime();
        UpdateWrapper<StoreOpenTime> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("store_id", storeId);
        updateWrapper.set("open_time", openTime);
        updateWrapper.set("close_time", closeTime);

        storeOpenTimeMapper.update(storeOpenTime, updateWrapper);
        return 1;
    }

    @Override
    public StoreInfo getStoreById(Long storeId) {
        AssertExt.notNull(storeId, "storeId为空");
        return storeInfoMapper.selectById(storeId);
    }

    @Override
    public IPage<WarehouseStaff> getWarehouseStaff(Long storeId, String name, String phone, Integer currentPage, Integer pageSize, String stockType) {
        if(StringUtils.isBlank(name))name = null;
        if(StringUtils.isBlank(stockType))name = null;
        if(StringUtils.isBlank(phone) )phone = null;
        if(currentPage == null)currentPage = 1;
        if(pageSize == null)pageSize = 10;

        IPage<WarehouseStaff> page = new Page<WarehouseStaff>(currentPage, pageSize);
        IPage<WarehouseStaff> iPage = warehouseStaffMapper.getWarehouseStaff(page, storeId, name, phone, stockType);

        return iPage;
    }

    @Override
    public WarehouseStaff getWarehouseStaffById(Long warehouseStaffId) {
        AssertExt.notNull(warehouseStaffId, "warehouseStaffId为空");
        return warehouseStaffMapper.selectById(warehouseStaffId);
    }

    @Override
    public IPage<GoodCustomVo> getGoodsCustom(Long storeId, Long categoryId, Integer currentPage, Integer pageSize, Long addressId) {
        AssertExt.notNull(storeId, "storeId为空");
        AssertExt.notNull(addressId, "addressId为空");
        if(currentPage == null)currentPage = 1;
        if(pageSize == null)pageSize = 10;

        IPage<GoodCustomVo> page = new Page<GoodCustomVo>(currentPage, pageSize);

        //获取该类型门店下的自定义价格商品
        IPage<GoodCustomVo>  goodsInfos = goodsInfoMapper.getGoodsCustom(page, storeId, categoryId, addressId);

        //查询并添加商品规格
        List<GoodsAttributeValueBO> goodsAttributeValueBOS = null;
        for(GoodCustomVo goodsInfo: goodsInfos.getRecords()){
            goodsAttributeValueBOS = goodsAttributeRelationMapper.selectValueNameListById(goodsInfo.getSku());
            goodsInfo.setGoodsAttributeValueBOS(goodsAttributeValueBOS);
        }

        return goodsInfos;
    }

    @Override
    public IPage<MemberAddress> getAddressByStoreId(Long storeId, Integer currentPage, Integer pageSize, String consignee,
                                                    String telephone, String locationAddress, Long adcode) {
        AssertExt.notNull(storeId, "storeId为空");
        if(currentPage == null)currentPage = 1;
        if(pageSize == null)pageSize = 10;

        IPage<MemberAddress> page = new Page<>(currentPage, pageSize);
        IPage<MemberAddress> storeInfoVoPage = memberAddressMapper.getAddressByStoreId(page, storeId, consignee, telephone, locationAddress, adcode);

        return storeInfoVoPage;
    }

    @Override
    @Transactional
    public int updateUserType(Integer addressId, String addressType, Integer isCustom){
        AssertExt.notNull(addressId, "addressId为空");
        AssertExt.notBlank(addressType, "addressType为空");
        AssertExt.notNull(isCustom, "isCustom为空");

        memberAddressStoreMapper.updateUserType(addressId, addressType, isCustom);

        return 1;
    }

    @Override
    @Transactional
    public int addCustomGoods(Long storeId, Long addressId, Long goodsId, Integer price, String sku, Long loginUserId){

        LocalDateTime now = LocalDateTime.now();

        //插入自定义商品关联表
        GoodsCustomPrice goodsCustomPrice = new GoodsCustomPrice();
        goodsCustomPrice.setSku(sku);
        goodsCustomPrice.setStoreId(storeId);
        goodsCustomPrice.setAddressId(addressId);
        goodsCustomPrice.setPrice(price);
        goodsCustomPrice.setIsDel(0);
        goodsCustomPrice.setCreateTime(now);
        goodsCustomPrice.setUpdateTime(now);
        goodsCustomPrice.setCreateUserId(loginUserId);
        goodsCustomPrice.setUpdateUserId(loginUserId);

        goodsCustomPriceMapper.insert(goodsCustomPrice);
        return 1;
    }

    @Override
    @Transactional
    public int updateCustomGoods(String sku, Integer price, Long addressId, Long loginUserId){

        LocalDateTime now = LocalDateTime.now();

        GoodsCustomPrice goodsCustomPrice = new GoodsCustomPrice();
        UpdateWrapper<GoodsCustomPrice> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("sku", sku);
        updateWrapper.eq("address_id", addressId);
        updateWrapper.set("price", price);
        updateWrapper.set("update_time", now);
        updateWrapper.set("update_user_id", loginUserId);

        goodsCustomPriceMapper.update(goodsCustomPrice, updateWrapper);
        return 1;
    }

    @Override
    @Transactional
    public int addVipPrice(String sku, Integer vipPrice, Long loginUserId){

        LocalDateTime now = LocalDateTime.now();

        GoodsStorePrice goodsStorePrice = new GoodsStorePrice();
        UpdateWrapper<GoodsStorePrice> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("sku", sku);
        updateWrapper.set("vip_price", vipPrice);
        updateWrapper.set("update_time", now);
        updateWrapper.set("update_user_id", loginUserId);

        goodsStorePriceMapper.update(goodsStorePrice, updateWrapper);
        return 1;
    }

    @Override
    @Transactional
    public Map userBindingStore(Long addressId, Long storeId) {
        AssertExt.notNull(addressId, "addressId为空");
        AssertExt.notNull(storeId, "storeId为空");

        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        //查出门店
        StoreInfo storeInfo = storeInfoMapper.selectById(storeId);

        //查询出该地址的门店关联表
        List<MemberAddressStoreVo> memberAddressStoreList = memberAddressStoreMapper.getMemberAddressStoreAndSoreType(addressId);

        Long addressStoreId = 0l;
        for (MemberAddressStoreVo memberAddressStoreVo: memberAddressStoreList){
            if(memberAddressStoreVo.getStoreTypeId().longValue() == storeInfo.getStoreTypeId().longValue()){
                addressStoreId = memberAddressStoreVo.getId();
            }
        }

        if(addressStoreId.longValue() != 0l){
            map.put("code", 1);
            map.put("msg", "地址绑定已该类型的门店，不能再绑定");
        }else {

            MemberAddressStore memberAddressStore = new MemberAddressStore();
            memberAddressStore.setAddressId(addressId);
            memberAddressStore.setStoreId(storeInfo.getId());
            memberAddressStore.setCreateTime(now);
            memberAddressStore.setAddressType(MemberAddressStore.AddressType.NORMAL.name());
            memberAddressStore.setIsDel(0);
            memberAddressStore.setUpdateTime(now);
            memberAddressStore.setCreateUserId(0l);
            memberAddressStore.setUpdateUserId(0l);
            memberAddressStoreMapper.insert(memberAddressStore);


            long storeType = storeInfo.getStoreTypeId().longValue();
            map.put("store", storeInfo);
            map.put("storeType",  storeType == 0 ? "gas" : (storeType == 1 ? "water" : (storeType == 2 ? "fastfood" : (storeType == 3 ? "market" : "marketAndFastfood"))));
            map.put("code", 2);
            map.put("msg", "绑定成功");
        }
        return map;

    }

    /**
     * 自动开盘和收盘,每分钟扫描一次
     */
    @Transactional
//    @Scheduled(cron="00 0/1 * * * ?")
    public  void  setOpenTimeAndCloseTime(){
        /**
         * second（秒）, minute（分）, hour（时）, day of month（日）, month（月）, day of week（周几）.
         * 例子：
         *  【0 0/5 14,18 * * ?】 每天14点整，和18点整，每隔5分钟执行一次
         *  【0 15 10 ? * 1-6】 每个月的周一至周六10:15分执行一次
         *  【0 0 2 ? * 6L】每个月的最后一个周六凌晨2点执行一次
         *  【0 0 2 LW * ?】每个月的最后一个工作日凌晨2点执行一次
         *  【0 0 2-4 ? * 1#1】每个月的第一个周一凌晨2点到4点期间，每个整点都执行一次；
         */
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<Long> goodOnSaleIds = new ArrayList<>();
        List<Long> goodOffSaleIds = new ArrayList<>();
        Map<String, Object> onSaleMap = new HashMap<>();
        Map<String, Object> offSaleMap = new HashMap<>();

        //获取没有删除的，正常的门店开盘收盘时间，商品id
        List<StoreInfoVo> storeInfoList = storeInfoMapper.getOpenTimeAndCloseTime();
        for(StoreInfoVo storeInfoVo: storeInfoList){

            //商品上架
            if (storeInfoVo.getOpenTime() != null){
                if(df.format(storeInfoVo.getOpenTime()).equals(df.format(now))){
                    if(storeInfoVo.getGoodId() != null){
                        goodOnSaleIds.add(storeInfoVo.getGoodId());
                    }
                }
            }

            //商品下架
            if (storeInfoVo.getCloseTime() != null){
                if(df.format(storeInfoVo.getCloseTime()).equals(df.format(now))){
                    if(storeInfoVo.getGoodId() != null){
                        goodOffSaleIds.add(storeInfoVo.getGoodId());
                    }
                }
            }
        }

        if(goodOnSaleIds.size() > 0){
            onSaleMap.put("msg","onSale");
            onSaleMap.put("ids", goodOnSaleIds);
            goodsInfoMapper.updateGoodsInfo(onSaleMap);
        }

       if(goodOffSaleIds.size() > 0){
           offSaleMap.put("msg","offSale");
           offSaleMap.put("ids", goodOffSaleIds);
           goodsInfoMapper.updateGoodsInfo(offSaleMap);
       }

        System.out.println(new Date() + "每60秒执行一次");

    }

}
