package com.zsyc.goods.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.common.AssertExt;
import com.zsyc.common.CommonConstant;
import com.zsyc.goods.bo.GoodsAttributeValueBO;
import com.zsyc.goods.entity.GoodsCustomPrice;
import com.zsyc.goods.entity.GoodsStorePrice;
import com.zsyc.goods.mapper.GoodsAttributeRelationMapper;
import com.zsyc.goods.mapper.GoodsCustomPriceMapper;
import com.zsyc.goods.mapper.GoodsStorePriceMapper;
import com.zsyc.goods.vo.*;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.store.mapper.StoreInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 周凯俊 on 2019/2/27.
 * @Explain:商品自定义价格 服务实现类
 */
@Service
@Slf4j
public class GoodsCustomPriceServiceImpl implements GoodsCustomPriceService {

    @Autowired
    private GoodsCustomPriceMapper goodsCustomPriceMapper;

    @Autowired
    private GoodsStorePriceMapper goodsStorePriceMapper;

    @Autowired
    private GoodsAttributeRelationMapper goodsAttributeRelationMapper;

    @Autowired
    private StoreInfoMapper storeInfoMapper;
    @Override
    public IPage<GoodsCustomPriceListVO> getGoodsCustomPriceList(Page page, GoodsCustomPriceSearchVO goodsCustomPriceSearchVO) {
        AssertExt.hasId(goodsCustomPriceSearchVO.getStoreId(),"店铺id不能为空");
        List<GoodsStorePrice> goodsStorePrices= goodsStorePriceMapper.selectList(new QueryWrapper<GoodsStorePrice>().eq("store_id",goodsCustomPriceSearchVO.getStoreId()).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL));
        List<String> skus=goodsStorePrices.stream().map(GoodsStorePrice::getSku).collect(Collectors.toList());
        BigDecimal bigDecimal=new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT);
        Integer changeBeforePrice=null;
        Integer changeAfterPrice=null;
        if(goodsCustomPriceSearchVO.getBeforePrice()!=null){
            changeBeforePrice=goodsCustomPriceSearchVO.getBeforePrice().multiply(bigDecimal).intValue();
        }
        if(goodsCustomPriceSearchVO.getAfterPrice()!=null){
            changeAfterPrice=goodsCustomPriceSearchVO.getAfterPrice().multiply(bigDecimal).intValue();
        }
        IPage<GoodsCustomPriceListVO> goodsCustomPriceListVOIPage=goodsCustomPriceMapper.selectCustomPriceList(page,goodsCustomPriceSearchVO.getStoreId(), CommonConstant.IsDel.IS_NOT_DEL,skus,goodsCustomPriceSearchVO.getAddressId(),goodsCustomPriceSearchVO.getSku(),changeBeforePrice,changeAfterPrice,goodsCustomPriceSearchVO.getBeforeTime(),goodsCustomPriceSearchVO.getAfterTime());
        if(goodsCustomPriceListVOIPage.getRecords().size()>0){
            List<StoreInfo> storeInfos=storeInfoMapper.selectList(new QueryWrapper<StoreInfo>().in("id",goodsCustomPriceListVOIPage.getRecords().stream().map(GoodsCustomPriceListVO::getStoreId).collect(Collectors.toList())));
            for(GoodsCustomPriceListVO goodsCustomPriceListVO:goodsCustomPriceListVOIPage.getRecords()){
                for(StoreInfo storeInfo:storeInfos){
                    if(goodsCustomPriceListVO.getStoreId().equals(storeInfo.getId())){
                        goodsCustomPriceListVO.setStoreName(storeInfo.getStoreName());
                        break;
                    }
                }
            }
        }
        return goodsCustomPriceListVOIPage;
    }

    @Override
    public GoodsCustomPriceVO getGoodsCustomPrice(Long storeId, Long addressId) {
        checkStoreAndAddress(storeId,addressId);
        GoodsCustomPriceVO goodsCustomPriceVO=goodsCustomPriceMapper.selectCustomPriceInfo(storeId,addressId,CommonConstant.IsDel.IS_NOT_DEL);
        AssertExt.notNull(goodsCustomPriceVO,"该商品自定义价格不存在不存在");
        List<GoodsAttributeValueBO> goodsAttributeValueBOS=goodsAttributeRelationMapper.selectValueNameListById(goodsCustomPriceVO.getSku());
        AssertExt.notEmpty(goodsAttributeValueBOS,"商品属性不存在");
        List<GoodsAttributeValueVO> goodsAttributeValueVOS=new ArrayList<>();
        for(GoodsAttributeValueBO goodsAttributeValueBO:goodsAttributeValueBOS){
            GoodsAttributeValueVO goodsAttributeValueVO=new GoodsAttributeValueVO();
            goodsAttributeValueVO.setAttrKey(goodsAttributeValueBO.getAttrKey());
            goodsAttributeValueVO.setAttrValueKey(goodsAttributeValueBO.getAttrValueKey());
            goodsAttributeValueVO.setAttrKeyName(goodsAttributeValueBO.getAttrKeyName());
            goodsAttributeValueVO.setAttrValueName(goodsAttributeValueBO.getAttrValueName());
            goodsAttributeValueVOS.add(goodsAttributeValueVO);
        }
        goodsCustomPriceVO.setGoodsAttributeValueVOS(goodsAttributeValueVOS);
        return goodsCustomPriceVO;
    }

    private void checkStoreAndAddress(Long storeId, Long addressId){
        AssertExt.hasId(storeId,"店铺id不能为空");
        AssertExt.hasId(addressId,"地址id不能为空");
    }

    private void checkAttribute(Long storeId, Long addressId,String sku,BigDecimal price){
        checkStoreAndAddress(storeId,addressId);
        AssertExt.notBlank(sku,"商品sku为空");
        AssertExt.isFalse(price == null ||price.compareTo(new BigDecimal("0"))<=0,"商品价格不能为空");
    }

    @Override
    public void addGoodsCustomPrice(GoodsCustomPriceInsertVO goodsCustomPriceInsertVO, Long id) {
        checkAttribute(goodsCustomPriceInsertVO.getStoreId(),goodsCustomPriceInsertVO.getAddressId(),goodsCustomPriceInsertVO.getSku(),goodsCustomPriceInsertVO.getPrice());
        Integer integer=goodsCustomPriceMapper.selectCount(new QueryWrapper<GoodsCustomPrice>().eq("store_id",goodsCustomPriceInsertVO.getStoreId()).eq("address_id",goodsCustomPriceInsertVO.getAddressId()).eq("sku",goodsCustomPriceInsertVO.getSku()));
        if(integer>1){
            log.error("存在多条自定义价格信息,sku为{},storeId为{},addressId为{}",goodsCustomPriceInsertVO.getSku(),goodsCustomPriceInsertVO.getStoreId(),goodsCustomPriceInsertVO.getAddressId());
        }
        AssertExt.isFalse(integer>=1,"已存在该自定义商品价格");
        GoodsCustomPrice goodsCustomPrice=new GoodsCustomPrice();
        BeanUtils.copyProperties(goodsCustomPriceInsertVO,goodsCustomPrice);
        goodsCustomPrice.setCreateTime(LocalDateTime.now());
        goodsCustomPrice.setCreateUserId(id);
        goodsCustomPrice.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
        BigDecimal bigDecimal=new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT);
        goodsCustomPrice.setPrice(goodsCustomPriceInsertVO.getPrice().multiply(bigDecimal).intValue());
        Integer integer1=goodsCustomPriceMapper.insert(goodsCustomPrice);
        AssertExt.isFalse(integer1!=1,"自定义价格失败");
    }

    @Override
    public void updateGoodsCustomPrice(GoodsCustomPriceInsertVO goodsCustomPriceInsertVO,Long id) {
        checkAttribute(goodsCustomPriceInsertVO.getStoreId(),goodsCustomPriceInsertVO.getAddressId(),goodsCustomPriceInsertVO.getSku(),goodsCustomPriceInsertVO.getPrice());
        GoodsCustomPrice goodsCustomPrice=new GoodsCustomPrice();
        BeanUtils.copyProperties(goodsCustomPriceInsertVO,goodsCustomPrice);
        goodsCustomPrice.setUpdateTime(LocalDateTime.now());
        goodsCustomPrice.setUpdateUserId(id);
        goodsCustomPrice.setUpdateTime(LocalDateTime.now());
        goodsCustomPrice.setId(null);
        BigDecimal bigDecimal=new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT);
        goodsCustomPrice.setPrice(goodsCustomPriceInsertVO.getPrice().multiply(bigDecimal).intValue());
        Integer integer=goodsCustomPriceMapper.update(goodsCustomPrice,new QueryWrapper<GoodsCustomPrice>().eq("store_id",goodsCustomPriceInsertVO.getStoreId()).eq("address_id",goodsCustomPriceInsertVO.getAddressId()).eq("sku",goodsCustomPriceInsertVO.getSku()).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.isFalse(integer!=1,"自定义价格修改失败");
    }

    @Override
    public void deleteGoodsCustomPrice(GoodsCustomPriceInsertVO goodsCustomPriceInsertVO,Long id) {
        checkStoreAndAddress(goodsCustomPriceInsertVO.getStoreId(),goodsCustomPriceInsertVO.getAddressId());
        AssertExt.notBlank(goodsCustomPriceInsertVO.getSku(),"商品sku为空");
        GoodsCustomPrice goodsCustomPrice=new GoodsCustomPrice();
        goodsCustomPrice.setUpdateTime(LocalDateTime.now());
        goodsCustomPrice.setUpdateUserId(id);
        goodsCustomPrice.setIsDel(CommonConstant.IsDel.IS_DEL);
        Integer integer=goodsCustomPriceMapper.update(goodsCustomPrice,new QueryWrapper<GoodsCustomPrice>().eq("store_id",goodsCustomPriceInsertVO.getStoreId()).eq("address_id",goodsCustomPriceInsertVO.getAddressId()).eq("sku",goodsCustomPriceInsertVO.getSku()).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.isFalse(integer!=1,"自定义价格删除失败");
    }
}
