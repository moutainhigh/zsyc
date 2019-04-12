package com.zsyc.goods.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.common.AssertExt;
import com.zsyc.common.CommonConstant;
import com.zsyc.goods.bo.*;
import com.zsyc.goods.dto.GoodsCustomPriceDTO;
import com.zsyc.goods.entity.*;
import com.zsyc.goods.mapper.*;
import com.zsyc.goods.vo.*;
import com.zsyc.member.entity.MemberAddressStore;
import com.zsyc.member.mapper.MemberAddressStoreMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品价格表 服务实现类
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Service
@Slf4j
public class GoodsStorePriceServiceImpl implements GoodsStorePriceService {

    @Autowired
    private GoodsStorePriceMapper goodsPriceMapper;

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;

    @Autowired
    private GoodsStoreStockMapper goodsStockMapper;

    @Autowired
    private GoodsStoreStockLogMapper goodsStockLogMapper;

    @Autowired
    private GoodsAttributeRelationMapper goodsAttributeRelationMapper;

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Autowired
    private MemberAddressStoreMapper memberAddressStoreMapper;

    @Autowired
    private GoodsCustomPriceMapper goodsCustomPriceMapper;

    @Autowired
    private GoodsGroupMapper goodsGroupMapper;


    @Override
    public IPage<GoodsStorePriceListVO> getGoodsPriceList(Page page,Long storeId) {
        AssertExt.isFalse(storeId == null|| storeId<=0,"店铺id不能为空");
        IPage<GoodsStorePriceListBO> goodsStore=goodsPriceMapper.selectGoodsPriceAndName(page,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE,storeId);
        if(goodsStore.getRecords().size()<=0){
            return new Page<GoodsStorePriceListVO>(page.getCurrent(),page.getSize(),page.getTotal());
        }
        return new Page<GoodsStorePriceListVO>(goodsStore.getCurrent(),goodsStore.getSize(),goodsStore.getTotal()).setRecords(getGoodsStorePriceListVO(goodsStore.getRecords()));
    }

    @Override
    public IPage<GoodsStorePriceListVO> getGoodsChildPriceList(Page page, Long storeId) {
        AssertExt.isFalse(storeId == null|| storeId<=0,"店铺id不能为空");
        IPage<GoodsStorePriceListBO> goodsStorePriceListBOIPage=goodsPriceMapper.selectGoodsChildPriceAndName(page,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE,storeId,CommonConstant.GoodsType.NORMAL,CommonConstant.GoodsStyle.INGREDIENT);
        if(goodsStorePriceListBOIPage.getRecords().size()<=0){
            return new Page<GoodsStorePriceListVO>(page.getCurrent(),page.getSize(),page.getTotal());
        }
        return new Page<GoodsStorePriceListVO>(goodsStorePriceListBOIPage.getCurrent(),goodsStorePriceListBOIPage.getSize(),goodsStorePriceListBOIPage.getTotal()).setRecords(getGoodsStorePriceListVO(goodsStorePriceListBOIPage.getRecords()));
    }

    private static List<GoodsStorePriceListBO> changeGoodsStoreBO(GoodsStorePriceListVO goodsStorePriceListVOS){
        List<GoodsStorePriceListBO> goodsStorePriceBOS=new ArrayList<>();
            GoodsStorePriceListBO goodsStorePriceBO=new GoodsStorePriceListBO();
            BeanUtils.copyProperties(goodsStorePriceListVOS,goodsStorePriceBO);
            goodsStorePriceBOS.add(goodsStorePriceBO);
        return goodsStorePriceBOS;
    }

    @Override
    public IPage<GoodsStorePriceListVO> getGoodsIngredientPriceList(Page page, Long storeId) {
        AssertExt.isFalse(storeId == null|| storeId<=0,"店铺id不能为空");
        IPage<GoodsStorePriceListBO> goodsStorePriceListBOIPage=goodsPriceMapper.selectGoodsIngredientPriceAndName(page,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE,storeId,CommonConstant.GoodsType.NORMAL,CommonConstant.GoodsStyle.INGREDIENT);
        if(goodsStorePriceListBOIPage.getRecords().size()<=0){
            return new Page<GoodsStorePriceListVO>(page.getCurrent(),page.getSize(),page.getTotal());
        }
        return new Page<GoodsStorePriceListVO>(goodsStorePriceListBOIPage.getCurrent(),goodsStorePriceListBOIPage.getSize(),goodsStorePriceListBOIPage.getTotal()).setRecords(getGoodsStorePriceListVO(goodsStorePriceListBOIPage.getRecords()));
    }

    @Override
    public IPage<GoodsStorePriceListVO> getGoodsVipPriceList(Page page, GoodsStorePriceSearchVO goodsStorePriceSearchVO) {
        AssertExt.isFalse(goodsStorePriceSearchVO.getStoreId() == null|| goodsStorePriceSearchVO.getStoreId()<=0,"店铺id不能为空");
        AssertExt.isTrue(goodsStorePriceSearchVO.getGoodsType() != null,"商品类型为空");
        AssertExt.isTrue(goodsStorePriceSearchVO.getGoodsType() != GoodsInfo.GoodsInfoTypeEnum.GENERAL_GOODS.type() || goodsStorePriceSearchVO.getGoodsType() != GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type(),"商品类型为空");
        BigDecimal bigDecimal=new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT);
        if(goodsStorePriceSearchVO.getBeforePrice()!=null){
            goodsStorePriceSearchVO.setChangeBeforePrice(goodsStorePriceSearchVO.getBeforePrice().multiply(bigDecimal).intValue());
        }
        if(goodsStorePriceSearchVO.getAfterPrice()!=null){
            goodsStorePriceSearchVO.setChangeAfterPrice(goodsStorePriceSearchVO.getAfterPrice().multiply(bigDecimal).intValue());
        }
        if(goodsStorePriceSearchVO.getGoodsType().equals(CommonConstant.GoodsType.NORMAL)){
            IPage<GoodsStorePriceListBO> goodsStorePriceListBOIPage=goodsPriceMapper.getGoodsVipPriceList(page,goodsStorePriceSearchVO,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsType.NORMAL);
            if(goodsStorePriceListBOIPage.getRecords().size()<=0){
                return new Page<GoodsStorePriceListVO>(page.getCurrent(),page.getSize(),page.getTotal());
            }
            return new Page<GoodsStorePriceListVO>(goodsStorePriceListBOIPage.getCurrent(),goodsStorePriceListBOIPage.getSize(),goodsStorePriceListBOIPage.getTotal()).setRecords(getGoodsStorePriceListVO(goodsStorePriceListBOIPage.getRecords()));
        }else if(goodsStorePriceSearchVO.getGoodsType().equals(CommonConstant.GoodsType.Group)){
            IPage<GoodsStorePriceListBO> goodsStorePriceListBOIPage=goodsPriceMapper.getGoodsVipPriceList(page,goodsStorePriceSearchVO,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsType.Group);
            if(goodsStorePriceListBOIPage.getRecords().size()<=0){
                return new Page<GoodsStorePriceListVO>(page.getCurrent(),page.getSize(),page.getTotal());
            }
            List<String> skus=goodsStorePriceListBOIPage.getRecords().stream().map(GoodsStorePriceListBO::getSku).collect(Collectors.toList());
            List<GoodsGroup> goodsGroups=goodsGroupMapper.selectList(new QueryWrapper<GoodsGroup>().in("sku",skus));
            Set<String> groupSkus=new HashSet<>();
            List<String> groupSubSku=new ArrayList<>();
            for(GoodsGroup goodsGroup:goodsGroups){
                groupSkus.add(goodsGroup.getSku());
                groupSubSku.add(goodsGroup.getSubSku());
            }
            //无子商品组合商品
            List<String> newSku=new ArrayList<>();
            for(String sku:skus){
                if(!groupSkus.contains(sku)){
                    newSku.add(sku);
                }
            }
            List<GoodsStorePrice> goodsStorePrices=goodsPriceMapper.selectList(new QueryWrapper<GoodsStorePrice>().eq("store_id",goodsStorePriceSearchVO.getStoreId()).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).eq("status",CommonConstant.GoodsPriceStatus.ON_SALE).in("sku",groupSubSku));
            List<GoodsStorePriceListVO> goodsStorePriceListVOS=new ArrayList<>();
            for(GoodsStorePriceListBO goodsStorePriceListBO:goodsStorePriceListBOIPage.getRecords()){
                GoodsStorePriceListVO goodsStorePriceListVO=new GoodsStorePriceListVO();
                BeanUtils.copyProperties(goodsStorePriceListBO,goodsStorePriceListVO);
                if(newSku.contains(goodsStorePriceListBO.getSku())){
                    goodsStorePriceListVO.setPrice(new BigDecimal(goodsStorePriceListBO.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                    goodsStorePriceListVO.setVipPrice(new BigDecimal(goodsStorePriceListBO.getVipPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                    goodsStorePriceListVO.setCostPrice(new BigDecimal(goodsStorePriceListBO.getCostPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                    goodsStorePriceListVOS.add(goodsStorePriceListVO);
                }else {
                    Integer price=0;
                    Integer vipPrice=0;
                    Integer costPrice=0;
                    for(GoodsGroup goodsGroup:goodsGroups){
                        if(goodsGroup.getSku().equals(goodsStorePriceListBO.getSku())){
                            for(GoodsStorePrice goodsStorePrice:goodsStorePrices){
                                if(goodsGroup.getSubSku().equals(goodsStorePrice.getSku())){
                                    price=price+goodsStorePrice.getPrice();
                                    vipPrice=vipPrice+goodsStorePrice.getVipPrice();
                                    costPrice=costPrice+goodsStorePrice.getCostPrice();
                                    break;
                                }
                            }
                        }
                    }
                    goodsStorePriceListVO.setPrice(new BigDecimal(price).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                    goodsStorePriceListVO.setVipPrice(new BigDecimal(vipPrice).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                    goodsStorePriceListVO.setCostPrice(new BigDecimal(costPrice).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                    goodsStorePriceListVOS.add(goodsStorePriceListVO);
                }
            }
            return new Page<GoodsStorePriceListVO>(goodsStorePriceListBOIPage.getCurrent(),goodsStorePriceListBOIPage.getSize(),goodsStorePriceListBOIPage.getTotal()).setRecords(goodsStorePriceListVOS);
        }else {
            return new Page<GoodsStorePriceListVO>(page.getCurrent(),page.getSize());
        }
    }

    @Override
    public void addGoodsPrice(GoodsPriceInsertVO goodsPriceInsertVO, Long id) {
        AssertExt.isTrue(goodsPriceInsertVO.getIsBargainPrice() != null&&
                (goodsPriceInsertVO.getIsBargainPrice().equals(CommonConstant.BargainPrice.IS_BARGAINPRICE)||goodsPriceInsertVO.getIsBargainPrice().equals(CommonConstant.BargainPrice.IS_NOT_BARGAINPRICE)),"是否特价不能为空");
        AssertExt.isTrue(goodsPriceInsertVO.getIsRecommend() != null&&
                (goodsPriceInsertVO.getIsRecommend().equals(CommonConstant.Recommend.IS_RECOMMEND)||goodsPriceInsertVO.getIsRecommend().equals(CommonConstant.Recommend.IS_NOT_RECOMMEND)),"是否推荐不能为空");
        GoodsStorePrice goodsStorePrice=getGoodsPrice(goodsPriceInsertVO);
        GoodsInfo goodsInfo=goodsInfoMapper.selectOne(new QueryWrapper<GoodsInfo>().eq("is_del", CommonConstant.IsDel.IS_NOT_DEL).in("sku",goodsPriceInsertVO.getSku()));
        AssertExt.notNull(goodsInfo,"该商品不存在");
        AssertExt.isTrue(getGoodsPriceCount(goodsPriceInsertVO.getStoreId(),goodsPriceInsertVO.getSku())!=1,"存在该商品价格");
        List<GoodsStoreStock> goodsStoreStockList=goodsStockMapper.selectList(new QueryWrapper<GoodsStoreStock>().eq("sku",goodsPriceInsertVO.getSku()).eq("store_id",goodsPriceInsertVO.getStoreId()));
        AssertExt.isTrue(goodsStoreStockList.size()==0,"商品库存数据有误");
        //false 单个组合商品
        Boolean flag=checkGoodsGroup(goodsInfo.getGoodsType(),goodsPriceInsertVO.getSku());
        LocalDateTime localDateTime=LocalDateTime.now();
        goodsStorePrice.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
        goodsStorePrice.setCreateTime(localDateTime);
        goodsStorePrice.setCreateUserId(id);
        goodsStorePrice.setStatus(CommonConstant.GoodsPriceStatus.ON_SALE);
        if(goodsInfo.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type())&&flag){
            goodsStorePrice.setPrice(null);
            goodsStorePrice.setVipPrice(null);
            goodsStorePrice.setCostPrice(null);
            goodsStorePrice.setBouns(null);
        }
        Integer integer=goodsPriceMapper.insert(goodsStorePrice);
        AssertExt.isFalse(integer!=1,"商品价格新增失败");
        if(goodsInfo.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GENERAL_GOODS.type())||!flag){
            GoodsStoreStock goodsStock=new GoodsStoreStock();
            goodsStock.setSku(goodsPriceInsertVO.getSku());
            goodsStock.setStoreId(goodsPriceInsertVO.getStoreId());
            goodsStock.setTotal(goodsPriceInsertVO.getTotal()==null||goodsPriceInsertVO.getTotal()<=0?0L:goodsPriceInsertVO.getTotal());
            goodsStock.setCreateTime(localDateTime);
            goodsStock.setCreateUserId(id);
            Integer integer1=goodsStockMapper.insert(goodsStock);
            AssertExt.isFalse(integer1!=1,"商品库存新增失败");
            GoodsStoreStockLog goodsStockLog=new GoodsStoreStockLog();
            goodsStockLog.setSku(goodsPriceInsertVO.getSku());
            goodsStockLog.setStoreId(goodsPriceInsertVO.getStoreId());
            goodsStockLog.setOperate(CommonConstant.Operate.ADD);
            goodsStockLog.setNum(goodsPriceInsertVO.getTotal()==null?0L:goodsPriceInsertVO.getTotal());
            goodsStockLog.setCreateTime(localDateTime);
            goodsStockLog.setCreateUserId(id);
            Integer integer2=goodsStockLogMapper.insert(goodsStockLog);
            AssertExt.isFalse(integer2!=1,"商品库存日志新增失败");
        }
    }

    @Override
    @Transactional
    public void updateGoodsPrice(GoodsPriceInsertVO goodsPriceInsertVO, Long id) {
        GoodsInfo goodsInfo=goodsInfoMapper.selectOne(new QueryWrapper<GoodsInfo>().eq("is_del", CommonConstant.IsDel.IS_NOT_DEL).in("sku",goodsPriceInsertVO.getSku()).eq("status",CommonConstant.GoodsPriceStatus.ON_SALE));
        AssertExt.notNull(goodsInfo,"该商品不存在");
        AssertExt.isTrue(getGoodsPriceCount(goodsPriceInsertVO.getStoreId(),goodsPriceInsertVO.getSku())==1,"该商品不存在");
        Boolean flag=checkGoodsGroup(goodsInfo.getGoodsType(),goodsPriceInsertVO.getSku());
        LocalDateTime localDateTime=LocalDateTime.now();
        GoodsStorePrice goodsPrice=getGoodsPrice(goodsPriceInsertVO);
        goodsPrice.setUpdateTime(localDateTime);
        goodsPrice.setUpdateUserId(id);
        goodsPrice.setSku(null);
        goodsPrice.setStoreId(null);
        goodsPrice.setIsRecommend(null);
        goodsPrice.setIsBargainPrice(null);
        if(goodsInfo.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type())&&flag){
            goodsPrice.setPrice(null);
            goodsPrice.setVipPrice(null);
            goodsPrice.setCostPrice(null);
            goodsPrice.setBouns(null);
        }
        Integer integer=goodsPriceMapper.update(goodsPrice,new QueryWrapper<GoodsStorePrice>().eq("sku",goodsPriceInsertVO.getSku()).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).eq("store_id",goodsPriceInsertVO.getStoreId()).eq("status",CommonConstant.GoodsPriceStatus.ON_SALE));
        AssertExt.isFalse(integer!=1,"商品价格修改失败");
        if(goodsInfo.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GENERAL_GOODS.type())||!flag){
            List<GoodsStoreStock> goodsStoreStockList=goodsStockMapper.selectList(new QueryWrapper<GoodsStoreStock>().eq("sku",goodsPriceInsertVO.getSku()).eq("store_id",goodsPriceInsertVO.getStoreId()));
            AssertExt.isTrue(goodsStoreStockList.size()==1,"商品库存数据有误");
            GoodsStoreStock goodsStock=goodsStoreStockList.get(0);
            GoodsStoreStock goodsStock1=new GoodsStoreStock();
            goodsStock1.setTotal(goodsPriceInsertVO.getTotal());
            Integer integer1=goodsStockMapper.update(goodsStock1,new QueryWrapper<GoodsStoreStock>().eq("sku",goodsStock.getSku()).eq("store_id",goodsStock.getStoreId()));
            AssertExt.isFalse(integer1!=1,"商品库存修改失败");
            if(goodsPriceInsertVO.getTotal().equals(goodsStock.getTotal())){
                return;
            }
            GoodsStoreStockLog goodsStockLog=new GoodsStoreStockLog();
            if(goodsPriceInsertVO.getTotal()>goodsStock.getTotal()){
                goodsStockLog.setOperate(CommonConstant.Operate.ADD);
                goodsStockLog.setNum(goodsPriceInsertVO.getTotal()-goodsStock.getTotal());
            }else {
                goodsStockLog.setOperate(CommonConstant.Operate.REDUCE);
                goodsStockLog.setNum(goodsStock.getTotal()-goodsPriceInsertVO.getTotal());
            }
            goodsStockLog.setSku(goodsPriceInsertVO.getSku());
            goodsStockLog.setStoreId(goodsPriceInsertVO.getStoreId());
            goodsStockLog.setCreateTime(localDateTime);
            goodsStockLog.setCreateUserId(id);
            Integer integer2=goodsStockLogMapper.insert(goodsStockLog);
            AssertExt.isFalse(integer2!=1,"商品库存日志插入失败");
        }
    }

    private List<GoodsStorePriceListVO> getGoodsStorePriceListVO(List<GoodsStorePriceListBO> goodsStorePriceListBOS){
        List<GoodsStorePriceListVO> goodsStorePriceListVOS=new ArrayList<>();
        goodsStorePriceListBOS.stream().forEach(x->{
            GoodsStorePriceListVO goodsStorePriceListVO=new GoodsStorePriceListVO();
            BeanUtils.copyProperties(x,goodsStorePriceListVO);
            goodsStorePriceListVO.setPrice(new BigDecimal(x.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
            goodsStorePriceListVO.setVipPrice(new BigDecimal(x.getVipPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
            goodsStorePriceListVO.setCostPrice(new BigDecimal(x.getCostPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
            goodsStorePriceListVOS.add(goodsStorePriceListVO);
        });
        return goodsStorePriceListVOS;
    }

    private GoodsStorePrice getGoodsPrice(GoodsPriceInsertVO goodsPriceInsertVO){
        checkSkuAndId(goodsPriceInsertVO.getSku(),goodsPriceInsertVO.getStoreId());
        AssertExt.isFalse(goodsPriceInsertVO.getTotal() == null || goodsPriceInsertVO.getTotal()<0,"商品库存为空");
        AssertExt.isFalse(goodsPriceInsertVO.getPrice() == null ||goodsPriceInsertVO.getPrice().compareTo(new BigDecimal("0"))<=0,"商品价格不能为空");
        AssertExt.isFalse(goodsPriceInsertVO.getVipPrice() == null ||goodsPriceInsertVO.getVipPrice().compareTo(new BigDecimal("0"))<=0,"商品vip价格不能为空");
        AssertExt.isFalse(goodsPriceInsertVO.getCostPrice() == null ||goodsPriceInsertVO.getCostPrice().compareTo(new BigDecimal("0"))<=0,"商品成本价格不能为空");
        System.out.println(goodsPriceInsertVO.getIsBargainPrice() == null);
        System.out.println(goodsPriceInsertVO.getIsBargainPrice()!=CommonConstant.BargainPrice.IS_BARGAINPRICE);
        System.out.println(goodsPriceInsertVO.getIsBargainPrice()!=CommonConstant.BargainPrice.IS_NOT_BARGAINPRICE);
        GoodsStorePrice goodsStorePrice=new GoodsStorePrice();
        goodsStorePrice.setStoreId(goodsPriceInsertVO.getStoreId());
        goodsStorePrice.setSku(goodsPriceInsertVO.getSku());
        goodsStorePrice.setIsBargainPrice(goodsPriceInsertVO.getIsBargainPrice());
        goodsStorePrice.setIsRecommend(goodsPriceInsertVO.getIsRecommend());
        BigDecimal bigDecimal=new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT);
        goodsStorePrice.setPrice(goodsPriceInsertVO.getPrice().multiply(bigDecimal).intValue());
        goodsStorePrice.setVipPrice(goodsPriceInsertVO.getVipPrice().multiply(bigDecimal).intValue());
        goodsStorePrice.setCostPrice(goodsPriceInsertVO.getCostPrice().multiply(bigDecimal).intValue());
        goodsStorePrice.setBouns(goodsPriceInsertVO.getBouns()==null?0:goodsPriceInsertVO.getBouns().multiply(bigDecimal).intValue());
        return goodsStorePrice;
    }

    private void checkStoreHomePage(Long storeId,Long addressId){
        AssertExt.isFalse(storeId == null|| storeId<=0,"店铺id不能为空");
        AssertExt.isFalse(addressId ==null||addressId<=0,"该地址为空");
    }

    private Integer getGoodsInfo(String sku){
        Integer goodsInfoCount=goodsInfoMapper.selectCount(new QueryWrapper<GoodsInfo>().eq("sku",sku).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        if(goodsInfoCount>1){
            log.error("存在相同sku码商品,sku:%s",sku);
        }
        return goodsInfoCount;
    }

    private void updateGoodsStatue(Long id, String sku, Long storeId,String statue){
        checkSkuAndId(sku,storeId);
        AssertExt.isTrue(getCheckGoodsPrice(storeId,sku)==1,"该商品不存在");
        GoodsStorePrice goodsStorePrice= new GoodsStorePrice();
        goodsStorePrice.setStatus(statue);
        goodsStorePrice.setUpdateTime(LocalDateTime.now());
        goodsStorePrice.setUpdateUserId(id);
        Integer integer=goodsPriceMapper.update(goodsStorePrice,new QueryWrapper<GoodsStorePrice>().eq("sku",sku).eq("store_id",storeId).eq("status",statue.equals(CommonConstant.GoodsPriceStatus.ON_SALE)?CommonConstant.GoodsPriceStatus.OFF_SALE:CommonConstant.GoodsPriceStatus.ON_SALE));
        AssertExt.isFalse(integer!=1,statue.equals(CommonConstant.GoodsPriceStatus.ON_SALE)?"商品上架失败":"商品下架失败");
    }

    private Boolean checkVIPStoreAndAddress(Long storeId, Long addressId){
        MemberAddressStore memberAddressStore=memberAddressStoreMapper.selectOne(new QueryWrapper<MemberAddressStore>().eq("address_id",addressId).eq("store_id",storeId).eq("is_del",0));
        AssertExt.notNull(memberAddressStore,"该用户店铺地址关联店铺不存在,地址id为:%s",addressId);
        Boolean vipType=memberAddressStore.getAddressType().equals(CommonConstant.AddressStore.VIP);
        return vipType;
    }

    private void checkSkuAndId(String sku,Long storeId){
        AssertExt.isFalse(storeId == null|| storeId<=0,"店铺id不能为空");
        AssertExt.notBlank(sku,"商品sku码为空");
        AssertExt.isFalse(getGoodsInfo(sku)!=1,"该商品不存在");
    }

    private Boolean checkGoodsGroup(Integer goodsType,String sku){
        Boolean flag=true;
        if(goodsType.equals(CommonConstant.GoodsType.Group)){
            Integer integer=goodsGroupMapper.selectCount(new QueryWrapper<GoodsGroup>().eq("sku",sku));
            if(integer==0){
                flag=false;
            }
        }
        return flag;
    }

    private GoodsCategory checkAndGetCategoryName(Long storeId,Long addressId,Long categoryId){
        AssertExt.isFalse(storeId == null|| storeId<=0,"店铺id不能为空");
        AssertExt.isFalse(categoryId == null|| categoryId<=0,"分类id不能为空");
        AssertExt.isFalse(addressId == null|| addressId<=0,"地址id不能为空");
        GoodsCategory category=goodsCategoryMapper.selectOne(new QueryWrapper<GoodsCategory>().eq("id",categoryId).eq("is_leaf",CommonConstant.GoodsInfo.IS_LEAF).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.notNull(category,"该分类错误或不存在");
        return category;
    }

    private Integer getCheckGoodsPrice(Long storeId,String sku){
        Integer integer=goodsPriceMapper.selectCount(new QueryWrapper<GoodsStorePrice>().eq("store_id",storeId).eq("sku",sku).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL));
        if(integer>1){
            log.error("该店铺有重复商品sku价格,店铺id为%s,sku为%s",storeId,sku);
        }
        return integer;
    }

    private void checkStoreAndAddress(String sku,Long storeId,Long addressId){
        AssertExt.isFalse(storeId == null|| storeId<=0,"店铺id不能为空");
        AssertExt.notBlank(sku,"商品sku码为空");
        AssertExt.isFalse(addressId ==null||addressId<=0,"该地址为空");
    }

    private void changeRecommend(Long id, String sku, Long storeId,Integer isRecommend){
        checkSkuAndId(sku,storeId);
        AssertExt.isTrue(getGoodsPriceCount(storeId,sku)==1,"该商品不存在");
        GoodsStorePrice goodsStorePrice=new GoodsStorePrice();
        goodsStorePrice.setUpdateTime(LocalDateTime.now());
        goodsStorePrice.setUpdateUserId(id);
        goodsStorePrice.setIsRecommend(isRecommend);
        Integer integer=goodsPriceMapper.update(goodsStorePrice, new QueryWrapper<GoodsStorePrice>().eq("store_id",storeId).eq("sku",sku).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).eq("is_recommend",isRecommend.equals(CommonConstant.Recommend.IS_NOT_RECOMMEND)?CommonConstant.Recommend.IS_RECOMMEND:CommonConstant.Recommend.IS_NOT_RECOMMEND));
        AssertExt.isFalse(integer!=1,isRecommend.equals(CommonConstant.Recommend.IS_NOT_RECOMMEND)?"设置商品不推荐失败":"设置商品推荐失败");
    }

    private List<GoodsGroupBO> getGoodsGroup(Long storeId,GoodsCategory category,List<GoodsAndPriceBO> goodsAndPriceBOS){
        List<GoodsGroupBO> goodsGroupBOS=new ArrayList<>();
        if(category.getCategoryType().equals(CommonConstant.CategoryType.GAS)){
            if(category.getCategoryName().equals(CommonConstant.CategoryName.RENT_BOTTLES)){
                goodsGroupBOS=goodsGroupMapper.selectGoodsPrice(storeId,goodsAndPriceBOS.stream().map(GoodsAndPriceBO::getSku).collect(Collectors.toList()));
                AssertExt.notEmpty(goodsGroupBOS,"子商品为空不能为空");
                return goodsGroupBOS;
            }
        }
        if(category.getCategoryType().equals(CommonConstant.CategoryType.WATER)){
            if(category.getCategoryName().equals(CommonConstant.CategoryName.RENT_BUCKET)){
                goodsGroupBOS=goodsGroupMapper.selectGoodsPrice(storeId,goodsAndPriceBOS.stream().map(GoodsAndPriceBO::getSku).collect(Collectors.toList()));
                AssertExt.notEmpty(goodsGroupBOS,"子商品为空不能为空");
                return goodsGroupBOS;
            }
        }
        if(category.getCategoryType().equals(CommonConstant.CategoryType.QUICKFOOD)){
            goodsGroupBOS=goodsGroupMapper.selectGoodsPrice(storeId,goodsAndPriceBOS.stream().map(GoodsAndPriceBO::getSku).collect(Collectors.toList()));
            return goodsGroupBOS;
        }
        return goodsGroupBOS;
    }

    private GoodsAndPriceListVO getGoodsAndPriceList(GoodsAndPriceBO goodsAndPriceBO,GoodsCustomPriceDTO goodsCustomPriceDTO,List<GoodsAttributeValueBO> goodsAttributeValueBOS){
        GoodsAndPriceListVO goodsAndPriceListVO=new GoodsAndPriceListVO();
        BeanUtils.copyProperties(goodsAndPriceBO,goodsAndPriceListVO);
        if(goodsCustomPriceDTO.getVipType()){
            goodsAndPriceListVO.setPrice(new BigDecimal(goodsAndPriceBO.getVipPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
            if(!CollectionUtils.isEmpty(goodsCustomPriceDTO.getSkus())&&goodsCustomPriceDTO.getSkus().contains(goodsAndPriceBO.getSku())){
                for(GoodsCustomPrice goodsCustomPrice:goodsCustomPriceDTO.getGoodsCustomPrices()){
                    if(goodsCustomPrice.getSku().equals(goodsAndPriceBO.getSku())){
                        goodsAndPriceListVO.setPrice(new BigDecimal(goodsCustomPrice.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                        break;
                    }
                }
            }
        }else {
            goodsAndPriceListVO.setPrice(new BigDecimal(goodsAndPriceBO.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
        }
        List<GoodsAttributeValueVO> goodsAttributeValueVOS=new ArrayList<>();
        for(GoodsAttributeValueBO goodsAttributeValueBO:goodsAttributeValueBOS){
            GoodsAttributeValueVO goodsAttributeValueVO=new GoodsAttributeValueVO();
            BeanUtils.copyProperties(goodsAttributeValueBO,goodsAttributeValueVO);
            goodsAttributeValueVOS.add(goodsAttributeValueVO);
        }
        return goodsAndPriceListVO.setGoodsAttributeValueVOS(goodsAttributeValueVOS);
    }

    private GoodsCustomPriceDTO getCheckAndGoodsCustom(Long storeId, Long addressId){
        GoodsCustomPriceDTO goodsCustomPriceDTO=new GoodsCustomPriceDTO();
        checkStoreHomePage(storeId,addressId);
        Boolean vipType=checkVIPStoreAndAddress(storeId,addressId);
        List<GoodsCustomPrice> goodsCustomPrices=goodsCustomPriceMapper.selectList(new QueryWrapper<GoodsCustomPrice>().eq("address_id",addressId).eq("store_id",storeId).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL));
        List<String> skus=new ArrayList<>();
        if(goodsCustomPrices.size()>0){
            skus=goodsCustomPrices.stream().map(GoodsCustomPrice::getSku).collect(Collectors.toList());
            goodsCustomPriceDTO.setGoodsCustomPrices(goodsCustomPrices);
            goodsCustomPriceDTO.setSkus(skus);
        }
        goodsCustomPriceDTO.setVipType(vipType);
        return goodsCustomPriceDTO;
    }

    private GoodsCustomPriceDTO getCheckAndGoodsCustomBySku(Long storeId, Long addressId,List<String> skus){
        GoodsCustomPriceDTO goodsCustomPriceDTO=new GoodsCustomPriceDTO();
        checkStoreHomePage(storeId,addressId);
        Boolean vipType=checkVIPStoreAndAddress(storeId,addressId);
        List<GoodsCustomPrice> goodsCustomPrices=goodsCustomPriceMapper.selectList(new QueryWrapper<GoodsCustomPrice>().eq("address_id",addressId).eq("store_id",storeId).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).in("sku",skus));
        if(goodsCustomPrices.size()>0){
            goodsCustomPriceDTO.setGoodsCustomPrices(goodsCustomPrices);
            goodsCustomPriceDTO.setSkus(goodsCustomPrices.stream().map(GoodsCustomPrice::getSku).collect(Collectors.toList()));
        }
        goodsCustomPriceDTO.setVipType(vipType);
        return goodsCustomPriceDTO;
    }

    private Integer getGoodsPriceCount(Long storeId,String sku){
        Integer integer=goodsPriceMapper.selectCount(new QueryWrapper<GoodsStorePrice>().eq("store_id",storeId).eq("sku",sku).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).eq("status",CommonConstant.GoodsPriceStatus.ON_SALE));
        if(integer>1){
            log.error("该店铺有重复商品sku价格,店铺id为%s,sku为%s",storeId,sku);
        }
        return integer;
    }

    private List<String> getGroupSkus(List<GoodsGroupBO> goodsGroupBos,List<String> goodsSkus){
        if(goodsGroupBos.size()>0){
            List<String> groupList=goodsGroupBos.stream().map(GoodsGroupBO::getSubSku).collect(Collectors.toList());
            if(goodsSkus.size()>0){
                groupList.addAll(goodsSkus);
            }
            return groupList;
        }
        return null;
    }

    private Boolean checkGroupAndAttribute(String sku){
        Integer integer=goodsGroupMapper.selectCount(new QueryWrapper<GoodsGroup>().eq("sku",sku));
        Integer integer1=goodsAttributeRelationMapper.selectCount(new QueryWrapper<GoodsAttributeRelation>().eq("sku",sku));
        Boolean QUICKFOODTYPE=integer>0&&integer1<=0;
        return QUICKFOODTYPE;
    }

    private GoodsInfo checkGoodsInfo(String sku){
        GoodsInfo goodsInfo=goodsInfoMapper.selectOne(new QueryWrapper<GoodsInfo>().eq("is_del", CommonConstant.IsDel.IS_NOT_DEL).eq("sku",sku).eq("status",CommonConstant.GoodsPriceStatus.ON_SALE));
        AssertExt.notNull(goodsInfo,"该商品不存在为空");
        return goodsInfo;
    }

    @Override
    public void deleteGoodsPrice(Long id, String sku,Long storeId) {
        AssertExt.isFalse(storeId == null|| storeId<=0,"店铺id不能为空");
        AssertExt.notBlank(sku,"商品sku码为空");
        Integer integer1=goodsCustomPriceMapper.selectCount(new QueryWrapper<GoodsCustomPrice>().eq("sku",sku).eq("store_id",storeId).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.isFalse(integer1>0,"该商品已被设置为自定义商品,无法删除");
        GoodsStorePrice goodsPrice=new GoodsStorePrice();
        goodsPrice.setIsDel(CommonConstant.IsDel.IS_DEL);
        goodsPrice.setUpdateTime(LocalDateTime.now());
        goodsPrice.setUpdateUserId(id);
        Integer integer=goodsPriceMapper.update(goodsPrice,new QueryWrapper<GoodsStorePrice>().eq("sku",sku).eq("store_id",storeId).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.isFalse(integer!=1,"商品删除失败");
    }

    @Override
    public GoodsStorePriceVO getGoodsPrice(String sku,Long storeId) {
        checkSkuAndId(sku,storeId);
        GoodsStorePriceBO goodsStorePriceBO=goodsPriceMapper.selectGoodsPriceVO(sku,storeId,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE);
        AssertExt.notNull(goodsStorePriceBO,"该商品不存在为空");
        List<GoodsAttributeValueBO> goodsAttributeValueBOS=goodsAttributeRelationMapper.selectValueNameListById(sku);
        GoodsStoreStock goodsStoreStock=goodsStockMapper.selectOne(new QueryWrapper<GoodsStoreStock>().eq("sku",sku).eq("store_id",storeId));
        AssertExt.notNull(goodsStoreStock,"该商品库存信息不存在为空");
        AssertExt.isTrue(goodsStoreStock.getTotal()>=0,"该商品库存信息有误");
        List<GoodsAttributeValueVO> goodsAttributeValueVOS=new ArrayList<>();
        if(goodsAttributeValueBOS.size()>0){
            for(GoodsAttributeValueBO goodsAttributeValueBO:goodsAttributeValueBOS){
                GoodsAttributeValueVO goodsAttributeValueVO=new GoodsAttributeValueVO();
                BeanUtils.copyProperties(goodsAttributeValueBO,goodsAttributeValueVO);
                goodsAttributeValueVOS.add(goodsAttributeValueVO);
            }
        }
        GoodsStorePriceVO goodsStorePriceVO=new GoodsStorePriceVO();
        BeanUtils.copyProperties(goodsStorePriceBO,goodsStorePriceVO);
        goodsStorePriceVO.setPrice(new BigDecimal(goodsStorePriceBO.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
        goodsStorePriceVO.setVipPrice(new BigDecimal(goodsStorePriceBO.getVipPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
        goodsStorePriceVO.setCostPrice(new BigDecimal(goodsStorePriceBO.getCostPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
        goodsStorePriceVO.setGoodsAttributeValueVOS(goodsAttributeValueVOS);
        goodsStorePriceVO.setTotal(goodsStoreStock.getTotal());
        return goodsStorePriceVO;
    }

    @Override
    public void updateStatusToOnSale(Long id, String sku, Long storeId) {
        updateGoodsStatue(id,sku,storeId,CommonConstant.GoodsPriceStatus.ON_SALE);
    }

    @Override
    public void updateStatusToOffSale(Long id, String sku, Long storeId) {
        updateGoodsStatue(id,sku,storeId,CommonConstant.GoodsPriceStatus.OFF_SALE);
    }

    private Page<GoodsHomePagePriceVO> getGoodsHomePageList(Page page, Long storeId, Long addressId, Integer bargian, Integer recommend){
        GoodsCustomPriceDTO goodsCustomPriceDTO=getCheckAndGoodsCustom(storeId,addressId);
        List<GoodsBarginRecommendPriceBO> goodsBarginPriceVOIPage=goodsPriceMapper.selectStoreGoodsPriceList((page.getCurrent()-1)*page.getSize(),page.getSize(),storeId,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE,bargian,recommend,CommonConstant.GoodsStyle.NORMAL,CommonConstant.GoodsType.NORMAL);
        if(goodsBarginPriceVOIPage.size()<=0){
            return new Page<GoodsHomePagePriceVO>(page.getCurrent(),page.getSize());
        }
        Integer integer=goodsPriceMapper.goodsBarginPriceCount(storeId,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE,bargian,recommend,CommonConstant.GoodsStyle.NORMAL,CommonConstant.GoodsType.NORMAL);
        List<GoodsHomePagePriceVO> goodsHomePagePriceVOS=new ArrayList<>();
        for(GoodsBarginRecommendPriceBO goodsBarginRecommendPriceBO:goodsBarginPriceVOIPage){
            GoodsHomePagePriceVO goodsBarginRecommendPriceVO=new GoodsHomePagePriceVO();
            BeanUtils.copyProperties(goodsBarginRecommendPriceBO,goodsBarginRecommendPriceVO);
            if(goodsCustomPriceDTO.getVipType()){
                goodsBarginRecommendPriceVO.setPrice(new BigDecimal(goodsBarginRecommendPriceBO.getVipPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                if(!CollectionUtils.isEmpty(goodsCustomPriceDTO.getSkus())&&goodsCustomPriceDTO.getSkus().contains(goodsBarginRecommendPriceBO.getSku())){
                    for(GoodsCustomPrice goodsCustomPrice:goodsCustomPriceDTO.getGoodsCustomPrices()){
                        if(goodsCustomPrice.getSku().equals(goodsBarginRecommendPriceBO.getSku())){
                            goodsBarginRecommendPriceVO.setPrice(new BigDecimal(goodsCustomPrice.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                            break;
                        }
                    }
                }
            }else {
                goodsBarginRecommendPriceVO.setPrice(new BigDecimal(goodsBarginRecommendPriceBO.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
            }
            goodsHomePagePriceVOS.add(goodsBarginRecommendPriceVO);
        }
        return new Page<GoodsHomePagePriceVO>(page.getCurrent(),page.getSize(),integer==null?0:integer).setRecords(goodsHomePagePriceVOS);
    }

    @Override
    public IPage<GoodsAndPriceListVO> getMiniGoodsPriceList(Page page, Long storeId,Long addressId,Long categoryId) {
        GoodsCategory category=checkAndGetCategoryName(storeId,addressId,categoryId);
        Boolean gasType=category.getCategoryType().equals(CommonConstant.CategoryType.GAS)&&category.getCategoryName().equals(CommonConstant.CategoryName.RENT_BOTTLES);
        Boolean foodType=category.getCategoryType().equals(CommonConstant.CategoryType.QUICKFOOD);
        Boolean waterType=category.getCategoryType().equals(CommonConstant.CategoryType.WATER)&&category.getCategoryName().equals(CommonConstant.CategoryName.RENT_BUCKET);
        Boolean type=waterType||foodType||gasType;
        Boolean gwTypes=category.getCategoryType().equals(CommonConstant.CategoryType.GAS)||category.getCategoryType().equals(CommonConstant.CategoryType.WATER);
        Boolean gwType=gasType||waterType;
        Boolean vipType=checkVIPStoreAndAddress(storeId,addressId);
        log.error("该用户店铺地址为空,地址id为:%s",addressId);
        IPage<GoodsAndPriceBO> goodsAndPriceBOIPage=gwTypes==true?goodsPriceMapper.selectWaterOrGasPriceList(page,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE,storeId,categoryId):
                goodsPriceMapper.selectGoodsPriceAndNameByCategory(page,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE,storeId,categoryId,category.getCategoryType().equals(CommonConstant.CategoryType.MARKET)?category.getCategoryType():null);
        if(goodsAndPriceBOIPage.getRecords().size()<=0){
            return new Page<GoodsAndPriceListVO>(page.getCurrent(),page.getSize());
        }
        List<String> goodsSkus=goodsAndPriceBOIPage.getRecords().stream().map(GoodsAndPriceBO::getSku).collect(Collectors.toList());
        List<GoodsGroupBO> goodsGroupBos=getGoodsGroup(storeId,category,goodsAndPriceBOIPage.getRecords());
        List<String> skus=goodsGroupBos.stream().map(GoodsGroupBO::getSku).collect(Collectors.toList());
        if(gwType){
            AssertExt.isFalse(goodsGroupBos.size()<=0,"分类子商品信息为空");
        }
        List<String> groupList=getGroupSkus(goodsGroupBos,goodsSkus);
        List<GoodsCustomPrice> goodsCustomPrices=goodsCustomPriceMapper.selectList(new QueryWrapper<GoodsCustomPrice>().eq("address_id",addressId).eq("store_id",storeId).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL));
        List<String> customSkus=goodsCustomPrices.size()>0?goodsCustomPrices.stream().map(GoodsCustomPrice::getSku).collect(Collectors.toList()):null;
        Boolean customType=!CollectionUtils.isEmpty(customSkus);
        List<GoodsAttributeValueBO> goodsAttributeValueBS=goodsAttributeRelationMapper.selectValueNameList(
                type == true&&!CollectionUtils.isEmpty(goodsGroupBos)&&!CollectionUtils.isEmpty(groupList)?groupList:
                        goodsSkus
        );
        List<GoodsAndPriceListVO> goodsStorePriceListVOS=new ArrayList<>();
        goodsAndPriceBOIPage.getRecords().stream().forEach(x->{
            GoodsAndPriceListVO goodsAndPriceListVO=new GoodsAndPriceListVO();
            goodsAndPriceListVO.setId(x.getId());
            goodsAndPriceListVO.setGoodsName(x.getGoodsName());
            goodsAndPriceListVO.setSku(x.getSku());
            goodsAndPriceListVO.setSpu(x.getSpu());
            goodsAndPriceListVO.setPicture(x.getPicture());
            if(gwType){
                for(GoodsGroupBO goodsGroupBO:goodsGroupBos){
                    if(x.getSku().equals(goodsGroupBO.getSku())){
                        if(vipType){
                            if(goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.BOTTLE)||goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.BUCKET)){
                                goodsAndPriceListVO.setDeposit(new BigDecimal(goodsGroupBO.getVipPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                            }
                            if(goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.GAS)||goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.WATER)){
                                goodsAndPriceListVO.setGroupPrice(new BigDecimal(goodsGroupBO.getVipPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                            }
                            if(customType&&customSkus.contains(goodsGroupBO.getSubSku())){
                                int i=0;
                                for(GoodsCustomPrice goodsCustomPrice:goodsCustomPrices){
                                    if(goodsCustomPrice.getSku().equals(goodsGroupBO.getSubSku())){
                                        if(goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.BOTTLE)||goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.BUCKET)){
                                            goodsAndPriceListVO.setDeposit(new BigDecimal(goodsCustomPrice.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                                            i++;
                                        }
                                        if(goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.GAS)||goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.WATER)){
                                            goodsAndPriceListVO.setGroupPrice(new BigDecimal(goodsCustomPrice.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                                            i++;
                                        }
                                    }
                                    //暂定
                                    if(i>=2){
                                        break;
                                    }
                                }
                            }
                        }else {
                            if(goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.BOTTLE)||goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.BUCKET)){
                                goodsAndPriceListVO.setDeposit(new BigDecimal(goodsGroupBO.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                            }
                            if(goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.GAS)||goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.WATER)){
                                goodsAndPriceListVO.setGroupPrice(new BigDecimal(goodsGroupBO.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                            }
                        }
                    }
                }
            }else if(foodType){
                Integer price=0;
                if(!skus.contains(x.getSku())){
                    if(vipType){
                        price=x.getVipPrice();
                        if(customType&&customSkus.contains(x.getSku())){
                            for(GoodsCustomPrice goodsCustomPrice:goodsCustomPrices){
                                if(goodsCustomPrice.getSku().equals(x.getSku())){
                                    price=goodsCustomPrice.getPrice();
                                    break;
                                }
                            }
                        }
                    }else {
                        price=x.getPrice();
                    }
                    goodsAndPriceListVO.setType(CommonConstant.GoodsInfoType.ATTRIBUTES);
                }else {
                    goodsAndPriceListVO.setType(CommonConstant.GoodsInfoType.GROUP);
                    //子商品价格
                    loop: for(GoodsGroupBO goodsGroupBO:goodsGroupBos){
                        if(goodsGroupBO.getSku().equals(x.getSku())){
                            if(vipType){
                                //vip价格
                                if(customType&&customSkus.contains(goodsGroupBO.getSubSku())){
                                    for(GoodsCustomPrice goodsCustomPrice:goodsCustomPrices){
                                        if(goodsGroupBO.getSubSku().equals(goodsCustomPrice.getSku())){
                                            price=price+goodsCustomPrice.getPrice();
                                            continue loop;
                                        }
                                    }
                                }else {
                                    price=price+goodsGroupBO.getVipPrice();
                                }
                            }else {
                                price=price+goodsGroupBO.getPrice();
                            }
                        }
                    }
                }
                goodsAndPriceListVO.setPrice(new BigDecimal(price).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
            }else {
                if(vipType){
                    goodsAndPriceListVO.setPrice(new BigDecimal(x.getVipPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                    if(customType&&customSkus.contains(x.getSku())){
                        for(GoodsCustomPrice goodsCustomPrice:goodsCustomPrices){
                            if(goodsCustomPrice.getSku().equals(x.getSku())){
                                goodsAndPriceListVO.setPrice(new BigDecimal(goodsCustomPrice.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                                break;
                            }
                        }
                    }
                }else {
                    goodsAndPriceListVO.setPrice(new BigDecimal(x.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                }
            }
            List<GoodsAttributeValueVO> goodsAttributeValueVOS=new ArrayList<>();
            if(type == true&&!CollectionUtils.isEmpty(goodsGroupBos)&&skus.contains(x.getSku())){
                for(GoodsGroupBO goodsGroupBO:goodsGroupBos){
                    if(goodsGroupBO.getSku().equals(x.getSku())){
                        for(GoodsAttributeValueBO goodsAttributeValueBO:goodsAttributeValueBS){
                            if(goodsGroupBO.getSubSku().equals(goodsAttributeValueBO.getSku())){
                                GoodsAttributeValueVO goodsAttributeValueVO=new GoodsAttributeValueVO();
                                BeanUtils.copyProperties(goodsAttributeValueBO,goodsAttributeValueVO);
                                goodsAttributeValueVOS.add(goodsAttributeValueVO);
                            }
                        }
                    }
                }
            }else {
                for(GoodsAttributeValueBO goodsAttributeValueBO:goodsAttributeValueBS){
                    if(x.getSku().equals(goodsAttributeValueBO.getSku())){
                        GoodsAttributeValueVO goodsAttributeValueVO=new GoodsAttributeValueVO();
                        BeanUtils.copyProperties(goodsAttributeValueBO,goodsAttributeValueVO);
                        goodsAttributeValueVOS.add(goodsAttributeValueVO);
                    }
                }
            }
            goodsAndPriceListVO.setGoodsAttributeValueVOS(goodsAttributeValueVOS);
            goodsStorePriceListVOS.add(goodsAndPriceListVO);
        });
        return new Page<GoodsAndPriceListVO>(goodsAndPriceBOIPage.getCurrent(),goodsAndPriceBOIPage.getSize(),goodsAndPriceBOIPage.getTotal()).setRecords(goodsStorePriceListVOS);
    }

    @Override
    public IPage<GoodsAndPriceListVO> getRentBottleGoodsPriceList(Page page, Long storeId,Long addressId, Long categoryId) {
        GoodsCategory goodsCategory=checkAndGetCategoryName(storeId,addressId,categoryId);
        if(!goodsCategory.getCategoryName().equals(CommonConstant.CategoryName.RENT_BOTTLES)){
            return new Page<GoodsAndPriceListVO>(page.getCurrent(),page.getSize());
        }
        IPage<GoodsAndPriceBO> goodsAndPriceBOIPage=goodsPriceMapper.selectWaterOrGasPriceList(page,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE,storeId,categoryId);
        if(goodsAndPriceBOIPage.getTotal()<=0){
            return new Page<GoodsAndPriceListVO>(page.getCurrent(),page.getSize());
        }
        List<GoodsGroupBO> goodsGroupBOS=getGoodsGroup(storeId,goodsCategory,goodsAndPriceBOIPage.getRecords());
        List<String> subSkus=goodsGroupBOS.stream().map(GoodsGroupBO::getSubSku).distinct().collect(Collectors.toList());
        List<GoodsCustomPrice> goodsCustomPrices=goodsCustomPriceMapper.selectList(new QueryWrapper<GoodsCustomPrice>().eq("address_id",addressId).eq("store_id",storeId).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).in("sku",subSkus));
        List<String> customSkus=goodsCustomPrices.size()>0?goodsCustomPrices.stream().map(GoodsCustomPrice::getSku).collect(Collectors.toList()):null;
        Boolean customType=!CollectionUtils.isEmpty(customSkus);
        List<GoodsAttributeValueBO> goodsAttributeValueBS=goodsAttributeRelationMapper.selectValueNameList(subSkus);
        Boolean vipType=checkVIPStoreAndAddress(storeId,addressId);
        List<GoodsAndPriceListVO> goodsStorePriceListVOS=new ArrayList<>();
        for(GoodsAndPriceBO goodsAndPriceBO:goodsAndPriceBOIPage.getRecords()){
            GoodsAndPriceListVO goodsAndPriceListVO=new GoodsAndPriceListVO();
            goodsAndPriceListVO.setId(goodsAndPriceBO.getId());
            goodsAndPriceListVO.setGoodsName(goodsAndPriceBO.getGoodsName());
            goodsAndPriceListVO.setSku(goodsAndPriceBO.getSku());
            goodsAndPriceListVO.setSpu(goodsAndPriceBO.getSpu());
            goodsAndPriceListVO.setPicture(goodsAndPriceBO.getPicture());
            for(GoodsGroupBO goodsGroupBO:goodsGroupBOS){
                if(goodsAndPriceBO.getSku().equals(goodsGroupBO.getSku())){
                    if(vipType){
                        if(goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.BOTTLE)){
                            goodsAndPriceListVO.setDeposit(new BigDecimal(goodsGroupBO.getVipPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                        }
                        if(goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.GAS)){
                            goodsAndPriceListVO.setGroupPrice(new BigDecimal(goodsGroupBO.getVipPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                        }
                        if(customType&&customSkus.contains(goodsGroupBO.getSubSku())){
                            int i=0;
                            for(GoodsCustomPrice goodsCustomPrice:goodsCustomPrices){
                                if(goodsCustomPrice.getSku().equals(goodsGroupBO.getSubSku())){
                                    if(goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.BOTTLE)||goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.BUCKET)){
                                        goodsAndPriceListVO.setDeposit(new BigDecimal(goodsCustomPrice.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                                        i++;
                                    }
                                    if(goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.GAS)||goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.WATER)){
                                        goodsAndPriceListVO.setGroupPrice(new BigDecimal(goodsCustomPrice.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                                        i++;
                                    }
                                }
                                //暂定
                                if(i>=2){
                                    break;
                                }
                            }
                        }
                    }else {
                        if(goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.BOTTLE)){
                            goodsAndPriceListVO.setDeposit(new BigDecimal(goodsGroupBO.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                        }
                        if(goodsGroupBO.getGoodsStyle().equals(CommonConstant.GoodsStyle.GAS)){
                            goodsAndPriceListVO.setGroupPrice(new BigDecimal(goodsGroupBO.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                        }
                    }
                }
                List<GoodsAttributeValueVO> goodsAttributeValueVOS=new ArrayList<>();
                for(GoodsAttributeValueBO goodsAttributeValueBO:goodsAttributeValueBS){
                    if(goodsGroupBO.getSubSku().equals(goodsAttributeValueBO.getSku())){
                        GoodsAttributeValueVO goodsAttributeValueVO=new GoodsAttributeValueVO();
                        BeanUtils.copyProperties(goodsAttributeValueBO,goodsAttributeValueVO);
                        goodsAttributeValueVOS.add(goodsAttributeValueVO);
                    }
                }
                goodsAndPriceListVO.setGoodsAttributeValueVOS(goodsAttributeValueVOS);
            }
            goodsStorePriceListVOS.add(goodsAndPriceListVO);
        }
        return new Page<GoodsAndPriceListVO>(goodsAndPriceBOIPage.getCurrent(),goodsAndPriceBOIPage.getSize(),goodsAndPriceBOIPage.getTotal()).setRecords(goodsStorePriceListVOS);
    }

    @Override
    public GoodsPriceInfoVO getGoodsPriceInfo(Long storeId, Long addressId,String sku) {
        checkStoreAndAddress(sku,storeId,addressId);
        GoodsInfo goodsInfo=goodsInfoMapper.selectOne(new QueryWrapper<GoodsInfo>().eq("sku",sku));
        AssertExt.notNull(goodsInfo,"该商品不存在");
        Boolean vipType=checkVIPStoreAndAddress(storeId,addressId);
        Boolean goodsType=goodsInfo.getGoodsType().equals(CommonConstant.GoodsType.Group);
        List<GoodsGroup> goodsGroups=goodsGroupMapper.selectList(new QueryWrapper<GoodsGroup>().eq("sku",sku));
        GoodsPriceInfoVO goodsPriceInfoVO=new GoodsPriceInfoVO();
        if(goodsType&&goodsGroups.size()>0){
            BeanUtils.copyProperties(goodsInfo,goodsPriceInfoVO);
            AssertExt.notNull(goodsGroups,"子商品列表为空");
            List<String> list=goodsGroups.stream().map(GoodsGroup::getSubSku).collect(Collectors.toList());
            List<GoodsInfo> goodsInfos=goodsInfoMapper.selectList(new QueryWrapper<GoodsInfo>().in("sku",list));
            AssertExt.notNull(goodsInfos,"子商品列表为空");
            List<GoodsStorePrice> goodsStorePrice=goodsPriceMapper.selectList(new QueryWrapper<GoodsStorePrice>().in("sku",list).eq("store_id",storeId));
            AssertExt.notNull(goodsStorePrice,"店铺不存在该商品");
            List<GoodsCustomPrice> goodsCustomPrices=goodsCustomPriceMapper.selectList(new QueryWrapper<GoodsCustomPrice>().eq("address_id",addressId).eq("store_id",storeId).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).in("sku",list));
            List<String> customSkus=goodsCustomPrices.size()>0?goodsCustomPrices.stream().map(GoodsCustomPrice::getSku).collect(Collectors.toList()):null;
            List<GoodsAttributeValueBO> goodsAttributeValueBS=goodsAttributeRelationMapper.selectValueNameList(list);
            AssertExt.notNull(goodsAttributeValueBS,"商品属性不存在");
            List<GoodsPriceInfoVO> goodsPriceInfoVOS=new ArrayList<>();
            for(GoodsInfo goodsInfo1:goodsInfos){
                GoodsPriceInfoVO goodsPriceInfoVO1=new GoodsPriceInfoVO();
                BeanUtils.copyProperties(goodsInfo1,goodsPriceInfoVO1);
                //子商品
                for(GoodsStorePrice goodsStorePrice1:goodsStorePrice){
                    if(goodsInfo1.getSku().equals(goodsStorePrice1.getSku())){
                        goodsPriceInfoVO1.setBouns(goodsStorePrice1.getBouns().doubleValue()/100);
                        if(vipType){
                            goodsPriceInfoVO1.setPrice(goodsStorePrice1.getVipPrice().doubleValue()/100);
                            if(!CollectionUtils.isEmpty(customSkus)&&customSkus.contains(goodsInfo1.getSku())){
                                for(GoodsCustomPrice goodsCustomPrice:goodsCustomPrices){
                                    if(goodsCustomPrice.getSku().equals(goodsInfo1.getSku())){
                                        goodsPriceInfoVO1.setPrice(goodsCustomPrice.getPrice().doubleValue()/100);
                                        break;
                                    }
                                }
                            }
                            break;
                        }else {
                            goodsPriceInfoVO1.setPrice(goodsStorePrice1.getPrice().doubleValue()/100);
                            break;
                        }
                    }
                }
                List<GoodsAttributeValueVO> goodsAttributeValueVOS=new ArrayList<>();
                for(GoodsAttributeValueBO goodsAttributeValueBO:goodsAttributeValueBS){
                    if(goodsAttributeValueBO.getSku().equals(goodsInfo1.getSku())){
                        GoodsAttributeValueVO goodsAttributeValueVO=new GoodsAttributeValueVO();
                        BeanUtils.copyProperties(goodsAttributeValueBO,goodsAttributeValueVO);
                        goodsAttributeValueVOS.add(goodsAttributeValueVO);
                    }
                }
                goodsPriceInfoVO1.setGoodsAttributeValueVOS(goodsAttributeValueVOS);
                goodsPriceInfoVOS.add(goodsPriceInfoVO1);
            }
            return goodsPriceInfoVO.setGoodsPriceInfoVOS(goodsPriceInfoVOS);
        }else{
            BeanUtils.copyProperties(goodsInfo,goodsPriceInfoVO);
            GoodsStorePrice goodsStorePrice=goodsPriceMapper.selectOne(new QueryWrapper<GoodsStorePrice>().eq("sku",sku).eq("store_id",storeId));
            AssertExt.notNull(goodsStorePrice,"店铺不存在该商品");
            GoodsCustomPrice goodsCustomPrices=goodsCustomPriceMapper.selectOne(new QueryWrapper<GoodsCustomPrice>().eq("address_id",addressId).eq("store_id",storeId).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).eq("sku",sku));
            List<GoodsAttributeValueBO> goodsAttributeValueBS=goodsAttributeRelationMapper.selectValueNameListById(sku);
            goodsPriceInfoVO.setBouns(goodsStorePrice.getBouns().doubleValue()/100);
            if(vipType){
                goodsPriceInfoVO.setPrice(goodsStorePrice.getVipPrice().doubleValue()/100);
                if(goodsCustomPrices!=null&&sku.equals(goodsCustomPrices.getSku())){
                    goodsPriceInfoVO.setPrice(goodsCustomPrices.getPrice().doubleValue()/100);
                }
            }else {
                goodsPriceInfoVO.setPrice(goodsStorePrice.getPrice().doubleValue()/100);
            }
            List<GoodsAttributeValueVO> goodsAttributeValueVOS=new ArrayList<>();
            if(goodsAttributeValueBS.size()>0){
                for(GoodsAttributeValueBO goodsAttributeValueBO:goodsAttributeValueBS){
                    GoodsAttributeValueVO goodsAttributeValueVO=new GoodsAttributeValueVO();
                    BeanUtils.copyProperties(goodsAttributeValueBO,goodsAttributeValueVO);
                    goodsAttributeValueVOS.add(goodsAttributeValueVO);
                }
            }
            return goodsPriceInfoVO.setGoodsAttributeValueVOS(goodsAttributeValueVOS);
        }
    }

    @Override
    public GoodsAttributePriceInfoVO getGoodsDetailInfo(Long storeId, Long addressId, String sku) {
        checkStoreAndAddress(sku,storeId,addressId);
        GoodsInfo goodsInfo=checkGoodsInfo(sku);
        AssertExt.isTrue(getGoodsPriceCount(storeId,sku)==1,"该商品不存在");
        Boolean goodsType=goodsInfo.getGoodsType().equals(CommonConstant.GoodsType.Group);
        GoodsAttributePriceInfoVO goodsAttributePriceInfoVO=new GoodsAttributePriceInfoVO();
        Boolean QUICKFOODTYPE=checkGroupAndAttribute(sku);
        if(goodsType&&QUICKFOODTYPE){
            List<GoodsNameGroupBO> goodsNameAndGroupBOS=goodsGroupMapper.selectGoodsNameGroupList(sku);
            Boolean vipType=checkVIPStoreAndAddress(storeId,addressId);
            BeanUtils.copyProperties(goodsInfo,goodsAttributePriceInfoVO);
            if(goodsNameAndGroupBOS.size()>0){
                List<String> list=goodsNameAndGroupBOS.stream().map(GoodsNameGroupBO::getSubSku).collect(Collectors.toList());
                List<GoodsAttributeValueBO> goodsAttributeValueBS=goodsAttributeRelationMapper.selectValueNameList(list);
                AssertExt.notEmpty(goodsAttributeValueBS,"商品属性不存在");
                List<GoodsStorePrice>goodsStorePrices=goodsPriceMapper.selectList(new QueryWrapper<GoodsStorePrice>().eq("store_id",storeId).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).eq("status",CommonConstant.GoodsPriceStatus.ON_SALE).in("sku",list));
                AssertExt.notEmpty(goodsStorePrices,"商品价格不存在");
                List<GoodsCustomPrice> goodsCustomPrices=goodsCustomPriceMapper.selectList(new QueryWrapper<GoodsCustomPrice>().eq("address_id",addressId).eq("store_id",storeId).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).in("sku",list));
                List<GoodsNameAndAttributeVO> goodsNameAndAttributeVOS=new ArrayList<>();
                for(GoodsNameGroupBO goodsNameGroupBO:goodsNameAndGroupBOS){
                    GoodsNameAndAttributeVO goodsNameAndAttributeVO=new GoodsNameAndAttributeVO();
                    List<GoodsAttributeValueVO> goodsAttributeValueVOS=new ArrayList<>();
                    for(GoodsAttributeValueBO goodsAttributeValueBO:goodsAttributeValueBS){
                        if(goodsNameGroupBO.getSubSku().equals(goodsAttributeValueBO.getSku())){
                            GoodsAttributeValueVO goodsAttributeValueVO=new GoodsAttributeValueVO();
                            BeanUtils.copyProperties(goodsAttributeValueBO,goodsAttributeValueVO);
                            goodsAttributeValueVOS.add(goodsAttributeValueVO);
                        }
                    }
                    for(GoodsStorePrice goodsStorePrice:goodsStorePrices){
                        if(goodsNameGroupBO.getSubSku().equals(goodsStorePrice.getSku())){
                            if(vipType){
                                goodsNameAndAttributeVO.setPrice(new BigDecimal(goodsStorePrice.getVipPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                                break;
                            }else {
                                goodsNameAndAttributeVO.setPrice(new BigDecimal(goodsStorePrice.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                                break;
                            }
                        }
                    }
                    if(goodsCustomPrices.size()>0){
                        for(GoodsCustomPrice goodsCustomPrice:goodsCustomPrices){
                            if(goodsCustomPrice.getSku().equals(goodsNameGroupBO.getSubSku())){
                                goodsNameAndAttributeVO.setPrice(new BigDecimal(goodsCustomPrice.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                                break;
                            }
                        }
                    }
                    goodsNameAndAttributeVO.setGoodsName(goodsNameGroupBO.getGoodsName());
                    goodsNameAndAttributeVO.setGoodsAttributeValueVOS(goodsAttributeValueVOS);
                    goodsNameAndAttributeVO.setType(goodsNameGroupBO.getGoodsStyle());
                    goodsNameAndAttributeVO.setPicture(goodsNameGroupBO.getPicture());
                    goodsNameAndAttributeVO.setSku(goodsNameGroupBO.getSubSku());
                    goodsNameAndAttributeVOS.add(goodsNameAndAttributeVO);
                }
                goodsAttributePriceInfoVO.setGoodsNameAndAttributeVOS(goodsNameAndAttributeVOS);
                goodsAttributePriceInfoVO.setType(CommonConstant.GoodsInfoType.GROUP);
            }
            return goodsAttributePriceInfoVO;
        }else {
            List<GoodsInfo> goodsInfos=goodsPriceMapper.selectStoreSkuList(goodsInfo.getSpu(),storeId,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE);
            AssertExt.notEmpty(goodsInfos,"该商品不存在为空");
            List<GoodsAttributeValueBO> goodsAttributeValueBS=goodsAttributeRelationMapper.selectValueNameList(goodsInfos.stream().map(GoodsInfo::getSku).collect(Collectors.toList()));
            AssertExt.notEmpty(goodsAttributeValueBS,"商品属性不存在");
            BeanUtils.copyProperties(goodsInfo,goodsAttributePriceInfoVO);
            List<GoodsSkuBO> goodsSkuBOS=new ArrayList<>();
            for(GoodsInfo goodsInfo1:goodsInfos){
                GoodsSkuBO goodsSkuBO=new GoodsSkuBO();
                List<GoodsAttributeValueVO> goodsAttributeValueVOS=new ArrayList<>();
                for(GoodsAttributeValueBO goodsAttributeValueBO:goodsAttributeValueBS){
                    if(goodsInfo1.getSku().equals(goodsAttributeValueBO.getSku())){
                        GoodsAttributeValueVO goodsAttributeValueVO=new GoodsAttributeValueVO();
                        BeanUtils.copyProperties(goodsAttributeValueBO,goodsAttributeValueVO);
                        goodsAttributeValueVOS.add(goodsAttributeValueVO);
                    }
                }
                goodsSkuBO.setSku(goodsInfo1.getSku());
                goodsSkuBO.setGoodsAttributeValueVOS(goodsAttributeValueVOS);
                goodsSkuBOS.add(goodsSkuBO);
            }
            goodsAttributePriceInfoVO.setType(CommonConstant.GoodsInfoType.ATTRIBUTES);
            return goodsAttributePriceInfoVO.setGoodsSkuBOS(goodsSkuBOS);
        }
    }

    @Override
    public GoodsSkuAndPriceVO getMiniGoodsDetailPrice(Long storeId, Long addressId, String sku) {
        checkStoreAndAddress(sku,storeId,addressId);
        GoodsInfo goodsInfo=checkGoodsInfo(sku);
        Boolean vipType=checkVIPStoreAndAddress(storeId,addressId);
        Boolean groupType=goodsInfo.getGoodsType().equals(CommonConstant.GoodsType.Group);
        Boolean normalType=goodsInfo.getGoodsType().equals(CommonConstant.GoodsType.NORMAL);
        Boolean QUICKFOODTYPE=checkGroupAndAttribute(sku);
        GoodsSkuAndPriceVO goodsSkuAndPriceVO=new GoodsSkuAndPriceVO();
        goodsSkuAndPriceVO.setSku(sku);
        Integer price=0;
        if(groupType&&QUICKFOODTYPE){
            List<GoodsNameGroupBO> goodsNameAndGroupBOS=goodsGroupMapper.selectGoodsNameGroupList(sku);
            AssertExt.notNull(goodsNameAndGroupBOS,"子商品列表为空");
            List<String> list=goodsNameAndGroupBOS.stream().map(GoodsNameGroupBO::getSubSku).collect(Collectors.toList());
            List<GoodsStorePrice> goodsStorePrices=goodsPriceMapper.selectList(new QueryWrapper<GoodsStorePrice>().eq("store_id",storeId).in("sku",list).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).eq("status",CommonConstant.GoodsPriceStatus.ON_SALE));
            AssertExt.notEmpty(goodsStorePrices,"子商品价格为空");
            List<GoodsCustomPrice> goodsCustomPrices=goodsCustomPriceMapper.selectList(new QueryWrapper<GoodsCustomPrice>().eq("address_id",addressId).eq("store_id",storeId).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).in("sku",list));
            List<String> customSkus=goodsCustomPrices.size()>0?goodsCustomPrices.stream().map(GoodsCustomPrice::getSku).collect(Collectors.toList()):null;
            loop: for(GoodsStorePrice goodsStorePrice:goodsStorePrices){
                if(vipType){
                    if(!CollectionUtils.isEmpty(customSkus)&&customSkus.contains(goodsStorePrice.getSku())){
                        for(GoodsCustomPrice goodsCustomPrice:goodsCustomPrices){
                            if(goodsStorePrice.getSku().equals(goodsCustomPrice.getSku())){
                                price=price+goodsCustomPrice.getPrice();
                                continue loop;
                            }
                        }
                    }else {
                        price=price+goodsStorePrice.getVipPrice();
                    }
                }else {
                    price=price+goodsStorePrice.getPrice();
                }
            }
        }
        if(normalType||groupType&&!QUICKFOODTYPE){
            GoodsStorePrice goodsStorePrice=goodsPriceMapper.selectOne(new QueryWrapper<GoodsStorePrice>().eq("store_id",storeId).eq("sku",sku).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).eq("status",CommonConstant.GoodsPriceStatus.ON_SALE));
            AssertExt.notNull(goodsStorePrice,"子商品价格为空");
            GoodsCustomPrice goodsCustomPrice=goodsCustomPriceMapper.selectOne(new QueryWrapper<GoodsCustomPrice>().eq("address_id",addressId).eq("store_id",storeId).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).eq("sku",sku));
            if(vipType){
                price=goodsStorePrice.getVipPrice();
                if(goodsCustomPrice!=null&&sku.equals(goodsCustomPrice.getSku())){
                    price=goodsCustomPrice.getPrice();
                }
            }else {
                price=goodsStorePrice.getPrice();
            }
        }
        return goodsSkuAndPriceVO.setPrice(new BigDecimal(price).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
    }

    @Override
    public IPage<GoodsHomePagePriceVO> getMiniGoodsBarginPrice(Page page, Long storeId, Long addressId) {
       return getGoodsHomePageList(page,storeId,addressId,CommonConstant.BargainPrice.IS_BARGAINPRICE,null);
    }

    @Override
    public IPage<GoodsHomePagePriceVO> getMiniGoodsRecommendList(Page page, Long storeId, Long addressId) {
        return getGoodsHomePageList(page,storeId,addressId,null,CommonConstant.Recommend.IS_RECOMMEND);
    }

    @Override
    public IPage<GoodsHomePagePriceVO> getMiniCookBookList(Page page, Long storeId, Long addressId) {
        GoodsCustomPriceDTO goodsCustomPriceDTO=getCheckAndGoodsCustom(storeId,addressId);
        List<GoodsBarginRecommendPriceBO> goodsBarginPriceVOIPage=goodsPriceMapper.selectGoodsCookBookList((page.getCurrent()-1)* page.getSize(), page.getSize(),storeId,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE,CommonConstant.GoodsStyle.NORMAL,CommonConstant.CategoryType.QUICKFOOD,CommonConstant.Recommend.IS_RECOMMEND);
        if(goodsBarginPriceVOIPage.size()<=0){
            return new Page<GoodsHomePagePriceVO>(page.getCurrent(),page.getSize());
        }
        Integer integer=goodsPriceMapper.selectGoodsCookBookCount(storeId,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE,CommonConstant.GoodsStyle.NORMAL,CommonConstant.CategoryType.QUICKFOOD,CommonConstant.Recommend.IS_RECOMMEND);
        AssertExt.isTrue(goodsBarginPriceVOIPage.size()>0&&integer>0,"查询今日菜谱数量失败");
        List<GoodsGroupBO> goodsGroupBOS=goodsGroupMapper.selectGoodsPrice(storeId,goodsBarginPriceVOIPage.stream().map(GoodsBarginRecommendPriceBO::getSku).collect(Collectors.toList()));
        List<String> skus=goodsGroupBOS.stream().map(GoodsGroupBO::getSku).collect(Collectors.toList());
        List<GoodsHomePagePriceVO> goodsHomePagePriceVOS=new ArrayList<>();
        for(GoodsBarginRecommendPriceBO goodsBarginRecommendPriceBO:goodsBarginPriceVOIPage){
            GoodsHomePagePriceVO goodsHomePagePriceVO=new GoodsHomePagePriceVO();
            BeanUtils.copyProperties(goodsBarginRecommendPriceBO,goodsHomePagePriceVO);
            Integer price=0;
            if(skus.contains(goodsBarginRecommendPriceBO.getSku())){
                loop: for(GoodsGroupBO goodsGroupBO:goodsGroupBOS) {
                    if(goodsBarginRecommendPriceBO.getSku().equals(goodsGroupBO.getSku())){
                        goodsHomePagePriceVO.setType(CommonConstant.GoodsInfoType.GROUP);
                        if (goodsCustomPriceDTO.getVipType()) {
                            if(!CollectionUtils.isEmpty(goodsCustomPriceDTO.getGoodsCustomPrices())&&goodsCustomPriceDTO.getSkus().contains(goodsGroupBO.getSubSku())){
                                for (GoodsCustomPrice goodsCustomPrice : goodsCustomPriceDTO.getGoodsCustomPrices()) {
                                    if (goodsGroupBO.getSubSku().equals(goodsCustomPrice.getSku())) {
                                        price = price + goodsCustomPrice.getPrice();
                                        continue loop;
                                    }
                                }
                            }else {
                                price = price + goodsGroupBO.getVipPrice();
                            }
                        } else {
                            price = price + goodsGroupBO.getPrice();
                        }
                    }
                }
            }else {
                goodsHomePagePriceVO.setType(CommonConstant.GoodsInfoType.ATTRIBUTES);
                if(goodsCustomPriceDTO.getVipType()){
                    price=goodsBarginRecommendPriceBO.getVipPrice();
                    if(!CollectionUtils.isEmpty(goodsCustomPriceDTO.getSkus())&&goodsCustomPriceDTO.getSkus().contains(goodsBarginRecommendPriceBO.getSku())){
                        for(GoodsCustomPrice goodsCustomPrice:goodsCustomPriceDTO.getGoodsCustomPrices()){
                            if(goodsCustomPrice.getSku().equals(goodsBarginRecommendPriceBO.getSku())){
                                price=goodsCustomPrice.getPrice();
                                break;
                            }
                        }
                    }
                }else {
                    price=goodsBarginRecommendPriceBO.getPrice();
                }
            }
            goodsHomePagePriceVO.setPrice(new BigDecimal(price).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
            goodsHomePagePriceVOS.add(goodsHomePagePriceVO);
        }
        return new Page<GoodsHomePagePriceVO>(page.getCurrent(),page.getSize(),integer).setRecords(goodsHomePagePriceVOS);
    }

    @Override
    public IPage<GoodsAndPriceListVO> getMiniGoodsPriceListByName(Page page, Long storeId, Long addressId, String goodsName) {
        if(StringUtils.isEmpty(goodsName)){
            return new Page<GoodsAndPriceListVO>(page.getCurrent(),page.getSize());
        }
        GoodsCustomPriceDTO goodsCustomPriceDTO=getCheckAndGoodsCustom(storeId,addressId);
        IPage<GoodsAndPriceBO> goodsAndPriceBOIPage=goodsPriceMapper.selectGoodsListByName(page,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE,storeId,goodsName,CommonConstant.GoodsType.NORMAL,CommonConstant.GoodsStyle.NORMAL);
        if(goodsAndPriceBOIPage.getRecords().size()<=0){
            return new Page<GoodsAndPriceListVO>(page.getCurrent(),page.getSize());
        }
        List<GoodsAttributeValueBO> goodsAttributeValueBOS=goodsAttributeRelationMapper.selectValueNameList(goodsAndPriceBOIPage.getRecords().stream().map(GoodsAndPriceBO::getSku).collect(Collectors.toList()));
        AssertExt.notEmpty(goodsAttributeValueBOS,"商品属性不存在");
        List<GoodsAndPriceListVO> goodsAndPriceListVOS=new ArrayList<>();
        for(GoodsAndPriceBO goodsAndPriceBO:goodsAndPriceBOIPage.getRecords()){
            GoodsAndPriceListVO goodsAndPriceListVO=new GoodsAndPriceListVO();
            BeanUtils.copyProperties(goodsAndPriceBO,goodsAndPriceListVO);
            goodsAndPriceListVOS.add(getGoodsAndPriceList(goodsAndPriceBO,goodsCustomPriceDTO,goodsAttributeValueBOS));
        }
        return new Page<GoodsAndPriceListVO>(page.getCurrent(),page.getSize(),page.getTotal()).setRecords(goodsAndPriceListVOS);
    }

    @Override
    public IPage<GoodsAndPriceListVO> getMiniFastFoodPriceListByName(Page page, Long storeId, Long addressId, String goodsName) {
        if(StringUtils.isEmpty(goodsName)){
            return new Page<GoodsAndPriceListVO>(page.getCurrent(),page.getSize());
        }
        IPage<GoodsAndPriceBO> goodsAndPriceBOIPage=goodsPriceMapper.selectGoodsListByName(page,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE,storeId,goodsName,CommonConstant.GoodsType.Group,CommonConstant.GoodsStyle.NORMAL);
        if(goodsAndPriceBOIPage.getRecords().size()<=0){
            return new Page<GoodsAndPriceListVO>(page.getCurrent(),page.getSize());
        }
        List<String> skus=goodsAndPriceBOIPage.getRecords().stream().map(GoodsAndPriceBO::getSku).collect(Collectors.toList());
        //获取group判断是否单个组合商品
        List<GoodsGroup> goodsGroups=goodsGroupMapper.selectList(new QueryWrapper<GoodsGroup>().in("sku",skus));
        List<GoodsAndPriceListVO> goodsAndPriceListVOS=new ArrayList<>();
        if(goodsGroups.size()<=0){
            //自定义价格
            GoodsCustomPriceDTO goodsCustomPriceDTO=getCheckAndGoodsCustomBySku(storeId,addressId,skus);
            List<GoodsAttributeValueBO> goodsAttributeValueBOS=goodsAttributeRelationMapper.selectValueNameList(skus);
            for(GoodsAndPriceBO goodsAndPriceBO:goodsAndPriceBOIPage.getRecords()){
                GoodsAndPriceListVO goodsAndPriceListVO=getGoodsAndPriceList(goodsAndPriceBO,goodsCustomPriceDTO,goodsAttributeValueBOS);
                goodsAndPriceListVO.setType(CommonConstant.GoodsInfoType.ATTRIBUTES);
                goodsAndPriceListVOS.add(goodsAndPriceListVO);
            }
        }else {
            List<String> subSkus=goodsGroups.stream().map(GoodsGroup::getSubSku).distinct().collect(Collectors.toList());
            List<String> groupSkus=goodsGroups.stream().map(GoodsGroup::getSku).distinct().collect(Collectors.toList());
            List<GoodsGroupBO> goodsGroupBOS=goodsGroupMapper.selectGoodsPrice(storeId,groupSkus);
            //获取子商品价格
            skus.addAll(subSkus);
            //自定义价格
            GoodsCustomPriceDTO goodsCustomPriceDTO=getCheckAndGoodsCustomBySku(storeId,addressId,skus);
            List<GoodsAttributeValueBO> goodsAttributeValueBOS=goodsAttributeRelationMapper.selectValueNameList(skus);
            AssertExt.notEmpty(goodsAttributeValueBOS,"商品属性不存在");
            for(GoodsAndPriceBO goodsAndPriceBO:goodsAndPriceBOIPage.getRecords()){
                GoodsAndPriceListVO goodsAndPriceListVO=new GoodsAndPriceListVO();
                BeanUtils.copyProperties(goodsAndPriceBO,goodsAndPriceListVO);
                if(!groupSkus.contains(goodsAndPriceBO.getSku())){
                    //单个组合商品
                    if(goodsCustomPriceDTO.getVipType()){
                        goodsAndPriceListVO.setPrice(new BigDecimal(goodsAndPriceBO.getVipPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                        if(!CollectionUtils.isEmpty(goodsCustomPriceDTO.getSkus())&&goodsCustomPriceDTO.getSkus().contains(goodsAndPriceBO.getSku())){
                            for(GoodsCustomPrice goodsCustomPrice:goodsCustomPriceDTO.getGoodsCustomPrices()){
                                if(goodsCustomPrice.getSku().equals(goodsAndPriceBO.getSku())){
                                    goodsAndPriceListVO.setPrice(new BigDecimal(goodsCustomPrice.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                                    break;
                                }
                            }
                        }
                    }else {
                        goodsAndPriceListVO.setPrice(new BigDecimal(goodsAndPriceBO.getPrice()).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                    }
                    goodsAndPriceListVO.setType(CommonConstant.GoodsInfoType.ATTRIBUTES);
                }else {
                    goodsAndPriceListVO.setType(CommonConstant.GoodsInfoType.GROUP);
                    Integer price=0;
                    loop: for(GoodsGroupBO goodsGroupBO:goodsGroupBOS){
                        if(goodsGroupBO.getSku().equals(goodsAndPriceBO.getSku())){
                            //vip
                            if(goodsCustomPriceDTO.getVipType()){
                                if(!CollectionUtils.isEmpty(goodsCustomPriceDTO.getSkus())&&goodsCustomPriceDTO.getSkus().contains(goodsGroupBO.getSubSku())){
                                    for(GoodsCustomPrice goodsCustomPrice:goodsCustomPriceDTO.getGoodsCustomPrices()){
                                        if(goodsGroupBO.getSubSku().equals(goodsCustomPrice.getSku())){
                                            price=price+goodsCustomPrice.getPrice();
                                            continue loop;
                                        }
                                    }
                                }else {
                                    price=price+goodsGroupBO.getVipPrice();
                                }
                            }else {
                                price=price+goodsGroupBO.getPrice();
                            }
                        }
                    }
                    goodsAndPriceListVO.setPrice(new BigDecimal(price).divide(new BigDecimal(CommonConstant.GoodsInfo.DECIMAL_POINT),2,BigDecimal.ROUND_HALF_UP));
                }
                List<GoodsAttributeValueVO> goodsAttributeValueVOS=new ArrayList<>();
                if(!CollectionUtils.isEmpty(goodsGroupBOS)&&groupSkus.contains(goodsAndPriceBO.getSku())){
                    for(GoodsGroupBO goodsGroupBO:goodsGroupBOS){
                        if(goodsGroupBO.getSku().equals(goodsAndPriceBO.getSku())){
                            for(GoodsAttributeValueBO goodsAttributeValueBO:goodsAttributeValueBOS){
                                if(goodsGroupBO.getSubSku().equals(goodsAttributeValueBO.getSku())){
                                    GoodsAttributeValueVO goodsAttributeValueVO=new GoodsAttributeValueVO();
                                    BeanUtils.copyProperties(goodsAttributeValueBO,goodsAttributeValueVO);
                                    goodsAttributeValueVOS.add(goodsAttributeValueVO);
                                }
                            }
                        }
                    }
                }else {
                    for(GoodsAttributeValueBO goodsAttributeValueBO:goodsAttributeValueBOS){
                        if(goodsAndPriceBO.getSku().equals(goodsAttributeValueBO.getSku())){
                            GoodsAttributeValueVO goodsAttributeValueVO=new GoodsAttributeValueVO();
                            BeanUtils.copyProperties(goodsAttributeValueBO,goodsAttributeValueVO);
                            goodsAttributeValueVOS.add(goodsAttributeValueVO);
                        }
                    }
                }
                goodsAndPriceListVO.setGoodsAttributeValueVOS(goodsAttributeValueVOS);
                goodsAndPriceListVOS.add(goodsAndPriceListVO);
            }
        }
        return new Page<GoodsAndPriceListVO>(goodsAndPriceBOIPage.getCurrent(),goodsAndPriceBOIPage.getSize(),goodsAndPriceBOIPage.getTotal()).setRecords(goodsAndPriceListVOS);
    }

    @Override
    public void updateIsRecommend(Long id, String sku, Long storeId) {
        changeRecommend(id,sku,storeId,CommonConstant.Recommend.IS_RECOMMEND);
    }

    @Override
    public void updateIsNotRecommend(Long id, String sku, Long storeId) {
        changeRecommend(id,sku,storeId,CommonConstant.Recommend.IS_NOT_RECOMMEND);

    }

}
