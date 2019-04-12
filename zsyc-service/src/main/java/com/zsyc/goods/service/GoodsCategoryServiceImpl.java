package com.zsyc.goods.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.common.AssertExt;
import com.zsyc.common.CommonConstant;
import com.zsyc.goods.entity.GoodsCategory;
import com.zsyc.goods.entity.GoodsCategoryRelation;
import com.zsyc.goods.mapper.GoodsCategoryMapper;
import com.zsyc.goods.mapper.GoodsCategoryRelationMapper;
import com.zsyc.goods.vo.GoodsCategoryListVO;
import com.zsyc.goods.vo.GoodsCategoryTreeListVO;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.store.mapper.StoreInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 周凯俊 on 2019/2/22.
 * @Explain:商品分类
 */
@Service
@Slf4j
public class GoodsCategoryServiceImpl implements GoodsCategoryService {

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private GoodsCategoryRelationMapper goodsCategoryRelationMapper;


    @Override
    public IPage<GoodsCategory> getGoodsCategoryList(Page page,Long id) {
        IPage<GoodsCategory> goodsCategoryList= goodsCategoryMapper.selectPage(page,new QueryWrapper<GoodsCategory>().eq("parent_id",(id ==null||id==0L)?-1L:id).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        return goodsCategoryList;
    }

    @Override
    public IPage<GoodsCategoryTreeListVO> getGoodsCategoryTreeList(Page page,Long id,String categoryName) {
        if((id!=null&&id>0)||!StringUtils.isEmpty(categoryName)){
            List<GoodsCategoryTreeListVO> goodsCategoryTreeListVOS=goodsCategoryMapper.selectGoodsCategoryList(CommonConstant.IsDel.IS_NOT_DEL);
            AssertExt.isFalse(goodsCategoryTreeListVOS.size()<=0,"商品类型列表为空");
            List<GoodsCategoryTreeListVO> goodsCategoryTreeListVOS1=goodsCategoryMapper.selectGoodsCategorySearchList(id,categoryName,CommonConstant.IsDel.IS_NOT_DEL);
            if(goodsCategoryTreeListVOS.size()<=0||goodsCategoryTreeListVOS1.size()<=0){
                return new Page<GoodsCategoryTreeListVO>(page.getCurrent(),page.getSize());
            }
            Set<String> treeSet=new HashSet<>();
            for(GoodsCategoryTreeListVO goodsCategoryTreeListVO:goodsCategoryTreeListVOS1){
                treeSet.addAll(Arrays.asList(goodsCategoryTreeListVO.getTreePath().split("-")));
            }
            List<GoodsCategoryTreeListVO> goodsCategoryTreeListVOS3=goodsCategoryTreeListVOS.stream().filter(x->treeSet.contains(x.getId().toString())).collect(Collectors.toList());
            List<GoodsCategoryTreeListVO> goodsCategoryTreeListVOS2=goodsCategoryTreeListVOS3.stream().filter(x->x.getParentId().equals(CommonConstant.GoodsInfo.IS_PARENT)).collect(Collectors.toList());
            for(GoodsCategoryTreeListVO goodsCategoryTreeListVO:goodsCategoryTreeListVOS2){
                goodsCategoryTreeListVO.setGoodsCategoryTreeListVOS(getChildGoodsCategoryList(goodsCategoryTreeListVOS3,goodsCategoryTreeListVO.getId()));
            }
            IPage<GoodsCategory> goodsCategoryIPage=goodsCategoryMapper.selectPage(page,new QueryWrapper<GoodsCategory>().in("id",goodsCategoryTreeListVOS2.stream().map(GoodsCategoryTreeListVO::getId).collect(Collectors.toList())).eq("parent_id",CommonConstant.GoodsInfo.IS_PARENT));
            return new Page<GoodsCategoryTreeListVO>(goodsCategoryIPage.getCurrent(),goodsCategoryIPage.getSize(),goodsCategoryIPage.getTotal()).setRecords(goodsCategoryTreeListVOS2);
        }else {
            List<GoodsCategoryTreeListVO> goodsCategoryTreeListVOS=goodsCategoryMapper.selectCategoryChildList(CommonConstant.GoodsInfo.IS_PARENT,CommonConstant.IsDel.IS_NOT_DEL);
            AssertExt.isFalse(goodsCategoryTreeListVOS.size()<=0,"商品类型子列表为空");
            IPage<GoodsCategoryTreeListVO> goodsCategoryTreeListVOPage=goodsCategoryMapper.selectCategoryParentList(page,CommonConstant.GoodsInfo.IS_PARENT,CommonConstant.IsDel.IS_NOT_DEL);
            AssertExt.isFalse(goodsCategoryTreeListVOPage.getRecords().size()<=0,"商品类型为空");
            for(GoodsCategoryTreeListVO goodsCategoryTreeListVO:goodsCategoryTreeListVOPage.getRecords()){
                goodsCategoryTreeListVO.setGoodsCategoryTreeListVOS(getChildGoodsCategoryList(goodsCategoryTreeListVOS,goodsCategoryTreeListVO.getId()));
            }
            return goodsCategoryTreeListVOPage;
        }
    }


    @Override
    @Transactional
    public void addGoodsCategory(GoodsCategory goodsCategory, Long id) {
        AssertExt.notBlank(goodsCategory.getCategoryName(),"类型名称为空");
        AssertExt.notBlank(goodsCategory.getPicture(),"类型图片为空");
        if(goodsCategory.getSort() == null || goodsCategory.getSort() < 0){
            goodsCategory.setSort(0);
        }
        goodsCategory.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
        goodsCategory.setCreateUserId(id);
        if(goodsCategory.getParentId() == null || goodsCategory.getParentId() ==0L){
            GoodsCategory goodsCategory1=goodsCategoryMapper.selectOne(new QueryWrapper<GoodsCategory>().eq("parent_id",CommonConstant.GoodsInfo.IS_PARENT).eq("category_type",CommonConstant.CategoryType.MARKET).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
            goodsCategory.setParentId(goodsCategory1.getId());
            goodsCategory.setIsLeaf(CommonConstant.GoodsInfo.IS_LEAF);
            goodsCategory.setCategoryType(CommonConstant.CategoryType.MARKET);
            Integer integer=goodsCategoryMapper.insert(goodsCategory);
            AssertExt.isFalse(integer!=1,"类型插入失败");
            goodsCategory.setTreePath(goodsCategory1.getTreePath()+"-"+goodsCategory.getId().toString());
        }else {
            GoodsCategory goodsCategory1=goodsCategoryMapper.selectOne(new QueryWrapper<GoodsCategory>().eq("id",goodsCategory.getParentId()).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
            AssertExt.notNull(goodsCategory1,"类型父级信息为空");
            goodsCategory.setIsLeaf(CommonConstant.GoodsInfo.IS_LEAF);
            goodsCategory.setCategoryType(goodsCategory1.getCategoryType());
            Integer integer=goodsCategoryMapper.insert(goodsCategory);
            goodsCategory.setTreePath(goodsCategory1.getTreePath()+"-" + goodsCategory.getId());
            AssertExt.isFalse(integer!=1,"类型插入失败");
            if(goodsCategory1.getIsLeaf()== CommonConstant.GoodsInfo.IS_LEAF){
                goodsCategory1.setIsLeaf(CommonConstant.GoodsInfo.IS_NOT_LEAF);
                Integer integer1=goodsCategoryMapper.updateById(goodsCategory1);
                AssertExt.isFalse(integer1!=1,"类型修改失败");
            }
        }
        Integer integer2=goodsCategoryMapper.updateById(goodsCategory);
        AssertExt.isFalse(integer2!=1,"类型修改失败");
    }

    private List<GoodsCategoryTreeListVO> getChildGoodsCategoryList(List<GoodsCategoryTreeListVO> childList,Long id){
        List<GoodsCategoryTreeListVO> goodsCategoryTreeListVOS=new ArrayList<>();
        for(GoodsCategoryTreeListVO goodsCategoryTreeListVO:childList){
            if(goodsCategoryTreeListVO.getParentId().equals(id)){
                goodsCategoryTreeListVO.setGoodsCategoryTreeListVOS(getChildGoodsCategoryList(childList,goodsCategoryTreeListVO.getId()));
                goodsCategoryTreeListVOS.add(goodsCategoryTreeListVO);
            }
        }
        return goodsCategoryTreeListVOS;
    }

    private List<String> getCategoryTypeByStoreId(Long storeTypeId){
        List<String> list=new ArrayList<>();
        if(storeTypeId.equals(CommonConstant.StoreType.MARKET)){
            list.add(CommonConstant.CategoryType.MARKET);
            return list;
        }
        if(storeTypeId.equals(CommonConstant.StoreType.GAS)){
            list.add(CommonConstant.CategoryType.GAS);
            return list;
        }
        if(storeTypeId.equals(CommonConstant.StoreType.WATER)){
            list.add(CommonConstant.CategoryType.WATER);
            return list;
        }
        if(storeTypeId.equals(CommonConstant.StoreType.QUICKFOOD)){
            list.add(CommonConstant.CategoryType.QUICKFOOD);
            return list;
        }
        if(storeTypeId.equals(CommonConstant.StoreType.MARKETANDQUICKFOOD)){
            list.add(CommonConstant.CategoryType.MARKET);
            list.add(CommonConstant.CategoryType.QUICKFOOD);
            return list;
        }
        return list;
    }

    @Override
    public void updateGoodsCategory(GoodsCategory goodsCategory) {
        AssertExt.hasId(goodsCategory.getId(),"分类id为空");
        AssertExt.notBlank(goodsCategory.getCategoryName(),"类型名称为空");
        AssertExt.notBlank(goodsCategory.getPicture(),"类型图片为空");
        GoodsCategory goodsCategory1=new GoodsCategory();
        goodsCategory1.setCategoryName(goodsCategory.getCategoryName());
        goodsCategory1.setPicture(goodsCategory.getPicture());
        Integer integer=goodsCategoryMapper.update(
                goodsCategory1,
                new QueryWrapper<GoodsCategory>().eq("id",goodsCategory.getId()).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.isFalse(integer!=1,"类型修改失败");
    }

    private StoreInfo checkStoreType(Long storeId){
        AssertExt.hasId(storeId,"店铺id为空");
        StoreInfo storeInfo=storeInfoMapper.selectOne(new QueryWrapper<StoreInfo>().eq("id",storeId).eq("is_del",0));
        AssertExt.notNull(storeInfo,"该店铺不存在");
        AssertExt.notNull(storeInfo.getStoreTypeId(),"该店铺分类不存在");
        return storeInfo;
    }

    @Override
    @Transactional
    public void deleteGoodsCategory(Long id) {
        AssertExt.hasId(id,"分类id为空");
        GoodsCategory goodsCategory=goodsCategoryMapper.selectOne(new QueryWrapper<GoodsCategory>().eq("id",id).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.isFalse(goodsCategory == null,"该类型不存在");
        AssertExt.isFalse(goodsCategory.getIsLeaf().equals(CommonConstant.GoodsInfo.IS_NOT_LEAF),"该分类包含子分类,无法删除");
        Integer integer1=goodsCategoryRelationMapper.selectCount(new QueryWrapper<GoodsCategoryRelation>().eq("category_id",id));
        AssertExt.isTrue(integer1<=0,"该分类已被使用,无法删除");
        List<GoodsCategory> goodsCategoryList=goodsCategoryMapper.selectList(new QueryWrapper<GoodsCategory>().eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        List<GoodsCategory> categories=new ArrayList<>();
        goodsCategoryList.stream().forEach(x->{
            String[] ids=x.getTreePath().split("-");
            if(Arrays.asList(ids).contains(id.toString())){
                GoodsCategory goodsCategory1=new GoodsCategory();
                goodsCategory1.setId(x.getId());
                goodsCategory1.setIsDel(CommonConstant.IsDel.IS_DEL);
                categories.add(goodsCategory1);
            }
        });
        Integer integer=goodsCategoryMapper.deleteByIdList(categories, CommonConstant.IsDel.IS_NOT_DEL);
        AssertExt.isFalse(integer<=0,"类型类型删除失败");
        Integer integer2=goodsCategoryMapper.selectCount(new QueryWrapper<GoodsCategory>().eq("parent_id",goodsCategory.getParentId()));
        if(integer2<=0){
            GoodsCategory goodsCategory1=new GoodsCategory();
            goodsCategory1.setIsLeaf(CommonConstant.GoodsInfo.IS_LEAF);
            Integer integer3=goodsCategoryMapper.update(goodsCategory1,new QueryWrapper<GoodsCategory>().eq("id",goodsCategory.getParentId()).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL));
            AssertExt.isFalse(integer3<=0,"类型树节点修改失败");
        }
    }

    private Boolean checkStoreTypeInfo(Long storeTypeId,String categoryType){
        AssertExt.isTrue(storeTypeId.equals(CommonConstant.StoreType.MARKET)||
                storeTypeId.equals(CommonConstant.StoreType.MARKETANDQUICKFOOD)||
                        storeTypeId.equals(CommonConstant.StoreType.GAS)||
                                storeTypeId.equals(CommonConstant.StoreType.WATER)||
                                        storeTypeId.equals(CommonConstant.StoreType.QUICKFOOD),"该店铺分类不存在");
        switch (categoryType){
            case CommonConstant.CategoryType.GAS:
                if(storeTypeId.equals(CommonConstant.StoreType.GAS)){
                    return true;
                }else {
                    return false;
                }
            case CommonConstant.CategoryType.WATER:
                if(storeTypeId.equals(CommonConstant.StoreType.WATER)){
                    return true;
                }else {
                    return false;
                }
            case CommonConstant.CategoryType.QUICKFOOD:
                if(storeTypeId.equals(CommonConstant.StoreType.QUICKFOOD)||storeTypeId.equals(CommonConstant.StoreType.MARKETANDQUICKFOOD)){
                    return true;
                }else {
                    return false;
                }
            case CommonConstant.CategoryType.MARKET:
                if(storeTypeId.equals(CommonConstant.StoreType.MARKET)||storeTypeId.equals(CommonConstant.StoreType.MARKETANDQUICKFOOD)){
                    return true;
                }else {
                    return false;
                }
            default:
                return false;
        }
    }

    @Override
    public GoodsCategoryListVO getMiniGoodsCategoryMenuList(Long storeId) {
        StoreInfo storeInfo=checkStoreType(storeId);
        AssertExt.isTrue(storeInfo.getStoreTypeId().equals(CommonConstant.StoreType.MARKET)||storeInfo.getStoreTypeId().equals(CommonConstant.StoreType.MARKETANDQUICKFOOD),"该店铺不存在菜市场分类");
        List<GoodsCategory> goodsCategoryList= goodsCategoryMapper.selectList(new QueryWrapper<GoodsCategory>().eq("category_type",CommonConstant.CategoryType.MARKET).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL).eq("display",CommonConstant.Display.IS_DISPLAY));
        AssertExt.notEmpty(goodsCategoryList,"菜市场类型列表为空");
        Map<Long,List<GoodsCategory>> categoryListMap=goodsCategoryList.stream().collect(Collectors.groupingBy(GoodsCategory::getParentId));
        AssertExt.isFalse(categoryListMap.size()<2,"菜市场类型信息为空");
        List<GoodsCategory> goodsCategoryList1=categoryListMap.get(CommonConstant.CategoryType.PARENT_NODE);
        List<GoodsCategory> goodsCategoryList2=categoryListMap.get(CommonConstant.CategoryType.MARKET_CHILD_NODE);
        GoodsCategoryListVO goodsCategoryListVO=new GoodsCategoryListVO();
        BeanUtils.copyProperties(goodsCategoryList1.get(0),goodsCategoryListVO);
        List<GoodsCategoryListVO> goodsCategoryListVOS=new ArrayList<>();
        for(GoodsCategory goodsCategory:goodsCategoryList2){
            GoodsCategoryListVO goodsCategoryListVO1=new GoodsCategoryListVO();
            BeanUtils.copyProperties(goodsCategory,goodsCategoryListVO1);
            goodsCategoryListVOS.add(goodsCategoryListVO1);
        }
        return goodsCategoryListVO.setGoodsCategoryListVOS(goodsCategoryListVOS);
    }

    @Override
    public List<GoodsCategory> getMiniGoodsCategoryList(Long storeId,String categoryType) {
        StoreInfo storeInfo=checkStoreType(storeId);
        Boolean categoryTypes=checkStoreTypeInfo(storeInfo.getStoreTypeId(),categoryType);
        AssertExt.isTrue(categoryTypes,"该店铺不存在该分类");
        List<GoodsCategory> goodsCategoryList= goodsCategoryMapper.selectList(new QueryWrapper<GoodsCategory>().eq("category_type",categoryType).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL).eq("display",CommonConstant.Display.IS_DISPLAY).eq("is_leaf",CommonConstant.GoodsInfo.IS_LEAF));
        return goodsCategoryList;
    }

    @Override
    public List<GoodsCategory> getGoodsCategoryListByStoreId(Long storeId,Long parentId) {
        StoreInfo storeInfo=checkStoreType(storeId);
        List<String> categoryType=getCategoryTypeByStoreId(storeInfo.getStoreTypeId());
        if(CollectionUtils.isEmpty(categoryType)){
            return new ArrayList<>();
        }
        return goodsCategoryMapper.selectList(new QueryWrapper<GoodsCategory>().eq("parent_id",parentId).in("category_type",categoryType));
    }
}
