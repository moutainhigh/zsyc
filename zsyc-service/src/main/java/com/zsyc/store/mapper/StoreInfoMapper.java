package com.zsyc.store.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.store.vo.StoreInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 店铺详情表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-14
 */
public interface StoreInfoMapper extends BaseMapper<StoreInfo> {

    /**
     * 查询门店
     * @param page
     * @param storeName
     * @param storeNo
     * @param storeType
     * @param beginTime
     * @param endTime
     * @return
     */
    IPage<StoreInfo> getStoreList(IPage<StoreInfo> page,
                                  @Param("storeName") String storeName,
                                  @Param("storeNo") String storeNo,
                                  @Param("storeType")Long storeType,
                                  @Param("beginTime")String beginTime,
                                  @Param("endTime")String endTime,
                                  @Param("adcode")Integer adcode,
                                  @Param("list")List<Long> rolesdIds);


    /**
     * 获取一个角色没绑定的门店
     * @param page
     * @return
     */
    IPage<StoreInfo> getStoreList2(IPage<StoreInfo> page,
                                  @Param("list")List<Long> rolesdIds,
                                  @Param("adcode")Integer adcode,
                                  @Param("storeTypeId")Integer storeTypeId);

    /**
     * 获取所有的门店
     * @param page
     * @return
     */
    IPage<StoreInfo> getStoreList3(IPage<StoreInfo> page,
                                   @Param("adcode")Integer adcode,
                                   @Param("storeTypeId")Integer storeTypeId);


    /**
     * 门店商品库存操作
     * @param sku
     * @param num
     * @param type
     * @param storeId
     * @return
     */
    Integer storeGoodStock(String sku, Integer num, Integer type, Long storeId);


    /**
     * 新增门店
     * @param storeInfo
     * @return
     */
    @Override
    int insert(StoreInfo storeInfo);

    /**
     * 通过ids删除
     * @param ids
     * @return
     */
    int deleteStoreByIds(List<Long> ids, Long loginUserId);


    /**
     * 通过ids查询
     * @param ids
     * @return
     */
    List<StoreInfo> getStoreByIds(List<Long> ids);

    /**
     * 获取没有删除的，正常的门店开盘收盘时间，商品id
     * @return
     */
    List<StoreInfoVo> getOpenTimeAndCloseTime();

    /**
     * 获取设置了定时生成备货订单的店铺
     * @return
     */
    List<StoreInfo> getIntervalStore();

}
