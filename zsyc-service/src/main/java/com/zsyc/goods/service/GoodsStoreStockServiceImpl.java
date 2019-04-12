package com.zsyc.goods.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.common.AssertExt;
import com.zsyc.common.CommonConstant;
import com.zsyc.goods.entity.GoodsInfo;
import com.zsyc.goods.entity.GoodsStorePrice;
import com.zsyc.goods.entity.GoodsStoreStock;
import com.zsyc.goods.entity.GoodsStoreStockLog;
import com.zsyc.goods.mapper.GoodsInfoMapper;
import com.zsyc.goods.mapper.GoodsStorePriceMapper;
import com.zsyc.goods.mapper.GoodsStoreStockLogMapper;
import com.zsyc.goods.mapper.GoodsStoreStockMapper;
import com.zsyc.goods.vo.GoodsStoreStockListVO;
import com.zsyc.goods.vo.GoodsStoreStockVO;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.store.mapper.StoreInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品库存表
 服务实现类
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Service
@Slf4j
public class GoodsStoreStockServiceImpl implements GoodsStoreStockService {

    @Autowired
    private GoodsStoreStockMapper goodsStoreStockMapper;

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;

    @Autowired
    private GoodsStorePriceMapper goodsStorePriceMapper;

    @Autowired
    private GoodsStoreStockLogMapper goodsStoreStockLogMapper;

    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Override
    public IPage<GoodsStoreStockListVO> getGoodsStoreStockList(Page page, Long storeId) {
        AssertExt.isFalse(storeId == null|| storeId<=0,"店铺id不能为空");
        IPage<GoodsStoreStockListVO> goodsStoreStockIPage=goodsStoreStockMapper.selectGoodsStoreStockList(page,storeId,CommonConstant.IsDel.IS_NOT_DEL);
        if(goodsStoreStockIPage.getRecords().size()>0){
            List<StoreInfo> storeInfos=storeInfoMapper.selectList(new QueryWrapper<StoreInfo>().in("id",goodsStoreStockIPage.getRecords().stream().map(GoodsStoreStockListVO::getStoreId).collect(Collectors.toList())));
            for(GoodsStoreStockListVO goodsStoreStockListVO:goodsStoreStockIPage.getRecords()){
                for(StoreInfo storeInfo:storeInfos){
                    if(goodsStoreStockListVO.getStoreId().equals(storeInfo.getId())){
                        goodsStoreStockListVO.setStoreName(storeInfo.getStoreName());
                        break;
                    }
                }
            }
        }
        return goodsStoreStockIPage;
    }

    @Override
    public GoodsStoreStockVO getGoodsStoreStock(Long storeId, String sku) {
        checkSkuAndId(sku,storeId);
        AssertExt.isTrue(getGoodsPriceCount(storeId,sku)==1,"该商品不存在");
        GoodsStoreStockVO goodsStoreStock=goodsStoreStockMapper.selectGoodsStoreStock(storeId,CommonConstant.IsDel.IS_NOT_DEL,sku);
        AssertExt.notNull(goodsStoreStock,"该商品库存不存在失败");
        return goodsStoreStock;
    }

    private Integer getGoodsInfo(String sku){
        Integer goodsInfoCount=goodsInfoMapper.selectCount(new QueryWrapper<GoodsInfo>().eq("sku",sku).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        if(goodsInfoCount>1){
            log.error("存在相同sku码商品,sku:%s",sku);
        }
        return goodsInfoCount;
    }

    private void updateGoodsStoreStockByOperate(GoodsStoreStock goodsStoreStock,Long id,String operate){
        checkSkuAndId(goodsStoreStock.getSku(),goodsStoreStock.getStoreId());
        AssertExt.hasId(goodsStoreStock.getTotal(),"备货库存数量不能为空");
        List<GoodsStoreStock> goodsStoreStockList=goodsStoreStockMapper.selectList(new QueryWrapper<GoodsStoreStock>().eq("sku",goodsStoreStock.getSku()).eq("store_id",goodsStoreStock.getStoreId()));
        AssertExt.isTrue(goodsStoreStockList.size()==1,"商品库存数据有误");
        GoodsStoreStock goodsStock=goodsStoreStockList.get(0);
        AssertExt.isFalse(goodsStock.getTotal()<0,"商品库存数据有误");
        //暂定,需设置锁保证数据一致
        GoodsStoreStock goodsStoreStock1=new GoodsStoreStock();
        if(operate.equals(CommonConstant.Operate.ADD)){
            goodsStoreStock1.setTotal(goodsStoreStock.getTotal()+goodsStock.getTotal());
        }else if(operate.equals(CommonConstant.Operate.REDUCE)){
            goodsStoreStock1.setTotal(goodsStock.getTotal()-goodsStoreStock.getTotal());
        }
        Integer integer=goodsStoreStockMapper.update(goodsStoreStock1,new QueryWrapper<GoodsStoreStock>().eq("store_id",goodsStoreStock.getStoreId()).eq("sku",goodsStoreStock.getSku()));
        AssertExt.isFalse(integer!=1,"店铺库存修改失败");
        GoodsStoreStockLog goodsStockLog=new GoodsStoreStockLog();
        goodsStockLog.setOperate(operate);
        goodsStockLog.setNum(goodsStoreStock.getTotal());
        goodsStockLog.setSku(goodsStoreStock.getSku());
        goodsStockLog.setStoreId(goodsStoreStock.getStoreId());
        goodsStockLog.setCreateTime(LocalDateTime.now());
        goodsStockLog.setCreateUserId(id);
        Integer integer2=goodsStoreStockLogMapper.insert(goodsStockLog);
        AssertExt.isFalse(integer2!=1,"商品库存日志插入失败");
    }

    private void checkSkuAndId(String sku,Long storeId){
        AssertExt.isFalse(storeId == null|| storeId<=0,"店铺id不能为空");
        AssertExt.notBlank(sku,"商品sku码为空");
        AssertExt.isFalse(getGoodsInfo(sku)!=1,"该商品不存在");
    }

    private Integer getGoodsPriceCount(Long storeId,String sku){
        Integer integer=goodsStorePriceMapper.selectCount(new QueryWrapper<GoodsStorePrice>().eq("store_id",storeId).eq("sku",sku).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).eq("status",CommonConstant.GoodsPriceStatus.ON_SALE));
        if(integer>1){
            log.error("该店铺有重复商品sku价格,店铺id为%s,sku为%s",storeId,sku);
        }
        return integer;
    }

    @Override
    @Transactional
    public void updateGoodsStoreStock(GoodsStoreStock goodsStoreStock,Long id) {
        checkSkuAndId(goodsStoreStock.getSku(),goodsStoreStock.getStoreId());
        AssertExt.hasId(goodsStoreStock.getTotal(),"库存总数不能为空");
        GoodsStoreStock goodsStoreStock1=new GoodsStoreStock();
        goodsStoreStock1.setTotal(goodsStoreStock.getTotal());
        List<GoodsStoreStock> goodsStoreStockList=goodsStoreStockMapper.selectList(new QueryWrapper<GoodsStoreStock>().eq("sku",goodsStoreStock.getSku()).eq("store_id",goodsStoreStock.getStoreId()));
        AssertExt.isTrue(goodsStoreStockList.size()==1,"商品库存数据有误");
        Integer integer=goodsStoreStockMapper.update(goodsStoreStock1,new QueryWrapper<GoodsStoreStock>().eq("store_id",goodsStoreStock.getStoreId()).eq("sku",goodsStoreStock.getSku()));
        AssertExt.isFalse(integer!=1,"店铺库存修改失败");
        GoodsStoreStock goodsStock=goodsStoreStockList.get(0);
        GoodsStoreStockLog goodsStockLog=new GoodsStoreStockLog();
        if(goodsStoreStock.getTotal()>goodsStock.getTotal()){
            goodsStockLog.setOperate(CommonConstant.Operate.ADD);
            goodsStockLog.setNum(goodsStoreStock.getTotal()-goodsStock.getTotal());
        }else {
            goodsStockLog.setOperate(CommonConstant.Operate.REDUCE);
            goodsStockLog.setNum(goodsStock.getTotal()-goodsStoreStock.getTotal());
        }
        goodsStockLog.setSku(goodsStoreStock.getSku());
        goodsStockLog.setStoreId(goodsStoreStock.getStoreId());
        goodsStockLog.setCreateTime(LocalDateTime.now());
        goodsStockLog.setCreateUserId(id);
        Integer integer2=goodsStoreStockLogMapper.insert(goodsStockLog);
        AssertExt.isFalse(integer2!=1,"商品库存日志插入失败");
    }

    @Override
    public void addStoreStockByStoreId(GoodsStoreStock goodsStoreStock,Long id) {
        updateGoodsStoreStockByOperate(goodsStoreStock,id,CommonConstant.Operate.ADD);
    }

    @Override
    public void declineStockByStoreId(GoodsStoreStock goodsStoreStock,Long id) {
        updateGoodsStoreStockByOperate(goodsStoreStock,id,CommonConstant.Operate.REDUCE);
    }
}
