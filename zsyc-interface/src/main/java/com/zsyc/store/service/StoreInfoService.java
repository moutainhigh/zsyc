package com.zsyc.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.goods.vo.GoodCustomVo;
import com.zsyc.goods.vo.GoodsPriceInfoVO;
import com.zsyc.member.entity.MemberAddress;
import com.zsyc.order.vo.StockVo;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.warehouse.entity.WarehouseStaff;

import java.util.List;
import java.util.Map;

public interface StoreInfoService {

    /**
     * 查询门店
     * @param storeName
     * @param storeNo
     * @param currentPage
     * @param pageSize
     * @param storeType
     * @param beginTime
     * @param endTime
     * @return
     */
    IPage<StoreInfo> getStoreList(String storeName, String storeNo, Integer currentPage, Integer pageSize,
                                  Long storeType, String beginTime, String endTime, Integer accode, Long loginUserId);


    /**
     * 新增门店
     * @param storeName
     * @param telephone
     * @param carriage
     * @param rent
     * @param longitude
     * @param Latitude
     * @param address
     * @param storeType
     * @param scope
     * @param picture
     * @param radius
     * @return
     */
    int addStore(String storeName, String telephone, Integer carriage, Integer rent, Double longitude, Double Latitude,
                 String address, Long storeType, String scope, List<String> picture, String radius, Integer adcode, Long loginUserId);


    /**
     * 新增门店
     * @param storeName
     * @param telephone
     * @param carriage
     * @param rent
     * @param longitude
     * @param Latitude
     * @param address
     * @param storeType
     * @param scope
     * @param picture
     * @param radius
     * @return
     */
    int updateStore(String storeName, String telephone, Integer carriage, Integer rent, Double longitude, Double Latitude,
                 String address, Long storeType, String scope, List<String> picture, String radius, Integer adcode, Long id, Long loginUserId);

    /**
     * 删除门店
     * @param ids
     * @return
     */
    int deleteStore(List<Long> ids, Long loginUserId);

    /**
     * 店铺组合商品更改库存
     * @param stockVos
     * @param type
     * @return
     */
    Integer storeGoodStock(List<StockVo> stockVos, Integer type, Long loginUserId);


    /**
     * 添加门店配送员
     * @param storeId
     * @param name
     * @return
     */
    Integer addDelivery(Long storeId, String name, String phone, String type, List<String> papers, Long loginUserId);

    /**
     * 修改配送员
     * @param deliveryId
     * @param name
     * @return
     */
    int updateDelivery(Long deliveryId, String name, String phone,  List<String> papers, Long loginUserId);


    /**
     * 删除配送员
     * @param ids
     * @return
     */
    int deleteDelivery(List<Long> ids, Long loginUserId);

    /**
     * 添加门店配货分拣员
     * @param name
     * @param storeId
     * @return
     */
    Integer addWarehouseStaff(String name, Long storeId, String phone, String idCard, String idCardImg, String stockType, Long loginUserId);


    /**
     * 修改分拣备货员
     * @param warehouseStaffId
     * @param name
     * @return
     */
    int updateWarehouseStaff(Long warehouseStaffId, String name, String phone, String idCard, String idCardImg, String stockType, Long loginUserId);


    /**
     * 门店删除分拣员
     * @param ids
     * @return
     */
    Integer deleteWarehouseStaff(List<Long> ids, Long loginUserId);

    /**
     * 店铺开盘时间
     * @param storeId
     * @param openTime
     * @param closeTime
     * @return
     */
    Integer storeOpenTime(Long storeId, String openTime, String closeTime);


    /**
     * 通过id查询门店
     * @param storeId
     * @return
     */
    StoreInfo getStoreById(Long storeId);


    /**
     * 门店分拣备货员查询
     * @param storeId
     * @return
     */
    IPage<WarehouseStaff> getWarehouseStaff(Long storeId, String name, String phone, Integer currentPage, Integer pageSize, String stockType);

    /**
     * 查询备货分拣员详情
     * @param warehouseStaffId
     * @return
     */
    WarehouseStaff getWarehouseStaffById(Long warehouseStaffId);


    /**
     * 查询门店根据地址自定义价格的商品
     * @return
     */
    IPage<GoodCustomVo> getGoodsCustom(Long storeId, Long categoryId, Integer currentPage, Integer pageSize, Long addressId);


    /**
    * 门店用户地址列表接口
     */
    IPage<MemberAddress> getAddressByStoreId(Long storeId, Integer currentPage, Integer pageSize, String consignee,
                                             String telephone, String locationAddress, Long adcode);


    /**
     * 修改用户地址类型
     * @param addressId
     * @param addressType
     * @param isCustom
     * @return
     */
    int updateUserType(Integer addressId, String addressType, Integer isCustom);

    /**
     * 门店添加自定义价格商品
     * @return
     */
    int addCustomGoods(Long storeId, Long addressId, Long goodsId, Integer price, String sku, Long loginUserId);


    /**
     * 门店修改自定义价格商品
     * @return
     */
    int updateCustomGoods(String sku, Integer price, Long addressId, Long loginUserId);

    /**
     * 添加vip价格
     * @param sku
     * @param vipPrice
     * @return
     */
    int addVipPrice(String sku, Integer vipPrice, Long loginUserId);


    /**
     * 用户扫二维码绑定门店
     */
    Map userBindingStore(Long addressId, Long storeId);

}
