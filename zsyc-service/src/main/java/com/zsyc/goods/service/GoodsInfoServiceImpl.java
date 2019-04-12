package com.zsyc.goods.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.common.AssertExt;
import com.zsyc.common.CommonConstant;
import com.zsyc.common.SnowFlakeUtil;
import com.zsyc.goods.bo.GoodsAttributeValueBO;
import com.zsyc.goods.bo.GoodsInfoListBO;
import com.zsyc.goods.dto.GoodsAttributeDTO;
import com.zsyc.goods.entity.*;
import com.zsyc.goods.mapper.*;
import com.zsyc.goods.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 周凯俊 on 2019/2/22.
 * @Explain:商品总表 服务实现类
 */
@Service
@Slf4j
public class GoodsInfoServiceImpl implements GoodsInfoService {

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;

    @Autowired
    private GoodsBrandRelationMapper goodsBrandRelationMapper;

    @Autowired
    private GoodsCategoryRelationMapper goodsCategoryRelationMapper;

    @Autowired
    private GoodsAttributeRelationMapper goodsAttributeRelationMapper;

    @Autowired
    private GoodsBrandMapper goodsBrandMapper;

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Autowired
    private GoodsGroupMapper goodsGroupMapper;

    @Autowired
    private GoodsAttributeMapper goodsAttributeMapper;

    @Autowired
    private GoodsStorePriceMapper goodsStorePriceMapper;


    public static final String BRANDCODE="121111111111";    //暂定默认品牌

    @Override
    public IPage<GoodsInfoListVO> getGoodsInfoList(Page page, GoodsInfoListSearchVO goodsInfoListSearchVO) {
        if(goodsInfoListSearchVO.getCategoryId()!=null){
            List<GoodsCategoryRelation> goodsCategoryRelations=goodsCategoryRelationMapper.selectList(new QueryWrapper<GoodsCategoryRelation>().eq("category_id",goodsInfoListSearchVO.getCategoryId()));
            goodsInfoListSearchVO.setSkus(goodsCategoryRelations.size()>0?goodsCategoryRelations.stream().map(GoodsCategoryRelation::getSku).collect(Collectors.toList()):new ArrayList<String>(Arrays.asList("0")));
        }
        IPage<GoodsInfoListBO> goodsInfo=goodsInfoMapper.selectGoodInfoList(page, CommonConstant.IsDel.IS_NOT_DEL,goodsInfoListSearchVO,CommonConstant.GoodsType.NORMAL);
        return getNewPages(goodsInfo);
    }

    @Override
    public IPage<GoodsInfoListVO> getGoodsInfoTypeList(Page page){
        IPage<GoodsInfoListBO> goodsInfoListBOIPage=goodsInfoMapper.selectGoodInfoTypeList(page,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsType.NORMAL);
        return getNewPages(goodsInfoListBOIPage);
    }

    @Override
    public IPage<GoodsGroupInfoListVO> getGoodsGroupInfoList(Page page, String sku) {
        IPage<GoodsInfo> goodsInfoIPage=goodsInfoMapper.selectGoodsInfoBySku(page,CommonConstant.GoodsType.Group,CommonConstant.IsDel.IS_NOT_DEL,sku);
        if(goodsInfoIPage.getRecords().size()<=0){
            return new Page<GoodsGroupInfoListVO>(page.getCurrent(),page.getSize(),page.getTotal());
        }
        //获取查询父级sku
        List<String> skus=goodsInfoIPage.getRecords().stream().map(GoodsInfo::getSku).collect(Collectors.toList());
        //获取组合商品sku
        List<GoodsGroup> goodsGroups=goodsGroupMapper.selectList(new QueryWrapper<GoodsGroup>().in("sku",skus));
        Set<String> groupSkus=new HashSet<>();
        List<String> groupSubSku=new ArrayList<>();
        for(GoodsGroup goodsGroup:goodsGroups){
            groupSkus.add(goodsGroup.getSku());
            groupSubSku.add(goodsGroup.getSubSku());
        }
        List<String> newSku=new ArrayList<>();
        for(String groupSku:skus){
            if(!groupSkus.contains(groupSku)){
                newSku.add(groupSku);
            }
        }
        groupSubSku.addAll(newSku);
        List<GoodsInfoListBO> goodsInfoListBOS=goodsInfoMapper.selectChildGoodsInfoList(groupSubSku,CommonConstant.IsDel.IS_NOT_DEL,CommonConstant.GoodsPriceStatus.ON_SALE);
        List<GoodsGroupInfoListVO> goodsGroupInfoListVOS=new ArrayList<>();
        for(GoodsInfo goodsInfo:goodsInfoIPage.getRecords()){
            GoodsGroupInfoListVO goodsGroupInfoListVO=new GoodsGroupInfoListVO();
            goodsGroupInfoListVO.setSku(goodsInfo.getSku());
            goodsGroupInfoListVO.setGoodsName(goodsInfo.getGoodsName());
            goodsGroupInfoListVO.setPicture(goodsInfo.getPicture());
            List<GoodsInfoListVO> goodsInfoListVOList=new ArrayList<>();
            if(groupSkus.contains(goodsInfo.getSku())){
                for(GoodsGroup goodsGroup:goodsGroups){
                    if(goodsGroup.getSku().equals(goodsInfo.getSku())){
                        for(GoodsInfoListBO goodsInfoListBO:goodsInfoListBOS){
                            if(goodsGroup.getSubSku().equals(goodsInfoListBO.getSku())){
                                GoodsInfoListVO goodsInfoListVO=new GoodsInfoListVO();
                                BeanUtils.copyProperties(goodsInfoListBO,goodsInfoListVO);
                                goodsInfoListVOList.add(goodsInfoListVO);
                                break;
                            }
                        }
                    }
                }
                goodsGroupInfoListVO.setChildType(CommonConstant.GroupGoodsType.IS_CHILD_LIST);
                goodsGroupInfoListVO.setGoodsInfoListVOS(goodsInfoListVOList);
            }else {
                for(GoodsInfoListBO goodsInfoListBO:goodsInfoListBOS){
                    if(goodsInfo.getSku().equals(goodsInfoListBO.getSku())){
                        GoodsInfoListVO goodsInfoListVO=new GoodsInfoListVO();
                        BeanUtils.copyProperties(goodsInfoListBO,goodsInfoListVO);
                        goodsInfoListVOList.add(goodsInfoListVO);
                        break;
                    }
                }
                goodsGroupInfoListVO.setChildType(CommonConstant.GroupGoodsType.IS_NOT_CHILD_LIST);
                goodsGroupInfoListVO.setGoodsInfoListVOS(goodsInfoListVOList);
            }
            goodsGroupInfoListVOS.add(goodsGroupInfoListVO);
        }
        return new Page<GoodsGroupInfoListVO>(page.getCurrent(),page.getSize(),page.getTotal()).setRecords(goodsGroupInfoListVOS);
    }

    @Override
    public IPage<GoodsInfoListVO> getGoodsChildInfoList(Page page, Long id) {
        GoodsCategory goodsCategory=getGoodsCategory(id);
        List<Integer> goods_styles=getGoodsStyleByCategory(goodsCategory);
        if(goods_styles==null||CollectionUtils.isEmpty(goods_styles)){
            return new Page<GoodsInfoListVO>(page.getCurrent(),page.getSize());
        }
        IPage<GoodsInfoListBO> goodsInfoListBOIPage=goodsInfoMapper.selectGoodInfoListByStyle(page,CommonConstant.GoodsType.NORMAL,CommonConstant.IsDel.IS_NOT_DEL,goods_styles);
        return getNewPages(goodsInfoListBOIPage);
    }

    @Override
    public IPage<GoodsInfoListVO> getGoodsIngredientInfoList(Page page, Long id) {
        GoodsCategory goodsCategory=getGoodsCategory(id);
        if(goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.QUICKFOOD)){
            IPage<GoodsInfoListBO> goodsInfoListBOIPage=goodsInfoMapper.selectGoodInfoListByStyle(page,CommonConstant.GoodsType.NORMAL,CommonConstant.IsDel.IS_NOT_DEL,Arrays.asList(CommonConstant.GoodsStyle.INGREDIENT));
            return getNewPages(goodsInfoListBOIPage);
        }
        return new Page<GoodsInfoListVO>(page.getCurrent(),page.getSize());
    }

    private List<GoodsInfoListVO> getAttributeValueKey(List<GoodsInfoListBO> goodsInfoListBOS){
        List<String> skus=goodsInfoListBOS.stream().map(GoodsInfoListBO ::getSku).collect(Collectors.toList());
        List<GoodsAttributeValueBO> goodsAttributeValueBS=goodsAttributeRelationMapper.selectValueNameList(skus);
        List<GoodsInfoListVO> goodsInfoListVOList=new ArrayList<>();
        for(GoodsInfoListBO goodsInfos:goodsInfoListBOS){
            GoodsInfoListVO goodsInfoListVO=new GoodsInfoListVO();
            BeanUtils.copyProperties(goodsInfos,goodsInfoListVO);
            goodsInfoListVO.setGoodsAttributeValueVOS(getGoodsAttributeValueList(goodsAttributeValueBS,goodsInfos.getSku()));
            goodsInfoListVOList.add(goodsInfoListVO);
        }
        return goodsInfoListVOList;
    }

    private void checkGoodsAttribute(List<Long> attrKeys){
        Integer integer=goodsAttributeMapper.selectCount(new QueryWrapper<GoodsAttribute>().in("attr_key",attrKeys).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).eq("is_sale",CommonConstant.GoodsAttributeType.IS_SALE));
        AssertExt.isTrue(integer==1,"只能选择一个售价单位");
    }

    private void checkGoodsStyle(Integer integer){
        if(!integer.equals(CommonConstant.GoodsStyle.NORMAL)||
                !integer.equals(CommonConstant.GoodsStyle.BOTTLE)||
                !integer.equals(CommonConstant.GoodsStyle.GAS)||
                !integer.equals(CommonConstant.GoodsStyle.WATER)||
                !integer.equals(CommonConstant.GoodsStyle.INGREDIENT)||
                !integer.equals(CommonConstant.GoodsStyle.BUCKET)
        ){
            AssertExt.isFalse(false,"该商品类型不存在");
        }
    }

    private IPage<GoodsInfoListVO> getNewPages(IPage<GoodsInfoListBO> goodsInfo){
        if(goodsInfo.getRecords().size()<=0){
            return new Page<GoodsInfoListVO>(goodsInfo.getCurrent(),goodsInfo.getSize(),goodsInfo.getTotal());
        }
        return new Page<GoodsInfoListVO>(goodsInfo.getCurrent(),goodsInfo.getSize(),goodsInfo.getTotal()).setRecords(getAttributeValueKey(goodsInfo.getRecords()));
    }

    @Override
    @Transactional
    public void addGoodsInfo(GoodsInfoInsertVO goodsInfoVO, Long id) {
        AssertExt.notBlank(goodsInfoVO.getGoodsName(),"商品名称为空");
        AssertExt.isFalse(goodsInfoVO.getGoodsStyle()==null ||goodsInfoVO.getGoodsStyle()<0,"商品类型不能为空");
        AssertExt.notBlank(goodsInfoVO.getPicture(),"商品图片为空");
        AssertExt.isTrue(goodsInfoVO.getGoodsType() != GoodsInfo.GoodsInfoTypeEnum.GENERAL_GOODS.type() || goodsInfoVO.getGoodsType() != GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type(),"商品类型为空");
        checkGoodsStyle(goodsInfoVO.getGoodsStyle());
        if(goodsInfoVO.getBrandCode() == null){
            goodsInfoVO.setBrandCode(BRANDCODE);
        }
        AssertExt.notBlank(goodsInfoVO.getBrandCode(),"品牌编码为空");
        AssertExt.hasId(goodsInfoVO.getCategoryId(),"分类id为空");
        GoodsCategory goodsCategory=checkBrandAndCategory(goodsInfoVO.getBrandCode(),goodsInfoVO.getCategoryId(),goodsInfoVO.getGoodsType());
        if(goodsInfoVO.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type())&&
                goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.QUICKFOOD)){
            AssertExt.isTrue(goodsInfoVO.getType().equals(CommonConstant.GroupType.MULTI)||
                    goodsInfoVO.getType().equals(CommonConstant.GroupType.ONE),"组合商品分类类型不能为空");
        }
        if(goodsInfoVO.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GENERAL_GOODS.type())){
            AssertExt.notEmpty(goodsInfoVO.getGoodsAttributeListVOS(),"商品属性列表为空");
            checkGoodsAttribute(goodsInfoVO.getGoodsAttributeListVOS().stream().map(GoodsAttributeListVO::getAttrKey).collect(Collectors.toList()));
        }
        if(goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.QUICKFOOD)
                &&goodsInfoVO.getType().equals(CommonConstant.GroupType.MULTI)){
            //判断组合商品是否存在子商品信息
            AssertExt.isFalse(goodsInfoVO.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type())&&CollectionUtils.isEmpty(goodsInfoVO.getGoodsGroupInsertVOS()),"子商品不能为空");
        }
        if(goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.QUICKFOOD)
                &&goodsInfoVO.getType().equals(CommonConstant.GroupType.ONE)){
            AssertExt.isFalse(goodsInfoVO.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type())&&CollectionUtils.isEmpty(goodsInfoVO.getGoodsAttributeListVOS()),"商品属性列表为空");
            checkGoodsAttribute(goodsInfoVO.getGoodsAttributeListVOS().stream().map(GoodsAttributeListVO::getAttrKey).collect(Collectors.toList()));
            goodsInfoVO.setGoodsGroupInsertVOS(new ArrayList<>());
        }
        if(goodsInfoVO.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type())){
            AssertExt.isFalse(goodsInfoVO.getGoodsStyle()!=0,"组合商品只能设置为普通商品");
            if(goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.QUICKFOOD)&&goodsInfoVO.getType().equals(CommonConstant.GroupType.MULTI)
            ){
                getGoodsInfoSkuList(goodsInfoVO.getGoodsGroupInsertVOS());
            }
        }
        SnowFlakeUtil snowFlake = new SnowFlakeUtil(2, 3);
        String spu=String.valueOf(snowFlake.nextId());
        List<GoodsInfo> goodsInfos=new ArrayList<>();
        List<GoodsAttributeRelation> goodsAttributeRelations=new ArrayList<>();
        List<GoodsBrandRelation> goodsBrandRelations=new ArrayList<>();
        List<GoodsCategoryRelation> goodsCategoryRelations=new ArrayList<>();
        List<GoodsGroup> goodsGroups=new ArrayList<>();
        //普通商品
        if(goodsInfoVO.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GENERAL_GOODS.type())
        ||(goodsInfoVO.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type())&&
        !CollectionUtils.isEmpty(goodsInfoVO.getGoodsAttributeListVOS())&&CollectionUtils.isEmpty(goodsInfoVO.getGoodsGroupInsertVOS())&&
                goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.QUICKFOOD))&&goodsInfoVO.getType().equals(CommonConstant.GroupType.ONE)){
            List<List<Long>> list2=getList(goodsInfoVO.getGoodsAttributeListVOS());
            List<GoodsAttributeDTO> goodsAttributeDTOS=getAttributeList(goodsInfoVO.getGoodsAttributeListVOS());
            for(List<Long> attrValueKeys:list2){
                String sku=String.valueOf(snowFlake.nextId());
                LocalDateTime localDateTime=LocalDateTime.now();
                getGoodsInfoList(goodsInfoVO,spu,sku,goodsInfos,goodsBrandRelations,goodsCategoryRelations,localDateTime,id);
                for(Long attrValueKey:attrValueKeys){
                    GoodsAttributeRelation goodsAttributeRelation=new GoodsAttributeRelation();
                    goodsAttributeRelation.setAttrValueKey(attrValueKey);
                    goodsAttributeRelation.setCreateTime(LocalDateTime.now());
                    goodsAttributeRelation.setCreateUserId(id);
                    goodsAttributeRelation.setSku(sku);
                    for(GoodsAttributeDTO goodsAttributeDTO:goodsAttributeDTOS){
                        if(attrValueKey.equals(goodsAttributeDTO.getAttrValueKey())){
                            goodsAttributeRelation.setAttrKey(goodsAttributeDTO.getAttrKey());
                            break;
                        }
                    }
                    goodsAttributeRelations.add(goodsAttributeRelation);
                }
            }
        }else {
            String sku=String.valueOf(snowFlake.nextId());
            LocalDateTime localDateTime=LocalDateTime.now();
            getGoodsInfoList(goodsInfoVO,spu,sku,goodsInfos,goodsBrandRelations,goodsCategoryRelations,localDateTime,id);
            if(goodsInfoVO.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type())&&!CollectionUtils.isEmpty(goodsInfoVO.getGoodsGroupInsertVOS())){
                if(goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.QUICKFOOD)){
                    if(!CollectionUtils.isEmpty(goodsInfoVO.getGoodsIngredientVOs())){
                        goodsInfoVO.getGoodsGroupInsertVOS().addAll(goodsInfoVO.getGoodsIngredientVOs());
                    }
                }
                getGoodsGroupList(goodsInfoVO.getGoodsGroupInsertVOS(),goodsGroups,sku,id,localDateTime);
            }
        }
        Integer goodsInfoInteger=goodsInfoMapper.insertGoodsList(goodsInfos);
        AssertExt.isFalse(goodsInfoInteger == null || goodsInfoInteger ==0,"商品插入失败");
        Integer goodsBrandInteger=goodsBrandRelationMapper.insertBrandList(goodsBrandRelations);
        AssertExt.isFalse(goodsBrandInteger == null || goodsBrandInteger ==0,"商品品牌插入失败");
        Integer goodsCategortInteger=goodsCategoryRelationMapper.insertCategoryList(goodsCategoryRelations);
        AssertExt.isFalse(goodsCategortInteger == null || goodsCategortInteger ==0,"商品分类插入失败");
        if(goodsInfoVO.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GENERAL_GOODS.type())
        ||(goodsInfoVO.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type())&&
                CollectionUtils.isEmpty(goodsInfoVO.getGoodsAttributeListVOS())==false&&CollectionUtils.isEmpty(goodsInfoVO.getGoodsGroupInsertVOS())==true&&
                goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.QUICKFOOD)&&goodsInfoVO.getType().equals(CommonConstant.GroupType.ONE))){
            Integer goodsAttributeRelationInteger=goodsAttributeRelationMapper.insertGoodsAttributeRelationList(goodsAttributeRelations);
            AssertExt.isFalse(goodsAttributeRelationInteger == null || goodsAttributeRelationInteger ==0,"商品属性插入失败");
        }
        if(goodsInfoVO.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type())&&!CollectionUtils.isEmpty(goodsInfoVO.getGoodsGroupInsertVOS())){
            Integer goodsGroupCount=goodsGroupMapper.insertGoodsGroupList(goodsGroups);
            AssertExt.isFalse(goodsGroupCount == null || goodsGroupCount ==0,"子商品插入失败");
        }
    }

    private GoodsCategory getGoodsCategory(Long id){
        AssertExt.hasId(id,"分类id为空");
        GoodsCategory goodsCategory=goodsCategoryMapper.selectOne(new QueryWrapper<GoodsCategory>().eq("id",id).eq("is_leaf", CommonConstant.GoodsInfo.IS_LEAF).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.notNull(goodsCategory,"该商品分类不存在");
        return goodsCategory;
    }

    private GoodsCategory checkBrandAndCategory(String brand ,Long categoryId,Integer goodsType){
        Integer brandCount=goodsBrandMapper.selectCount(new QueryWrapper<GoodsBrand>().eq("brand_code",brand).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.isFalse(brandCount == null || brandCount <= 0,"该品牌不存在");
        GoodsCategory goodsCategory=goodsCategoryMapper.selectOne(new QueryWrapper<GoodsCategory>().eq("id",categoryId).eq("is_leaf", CommonConstant.GoodsInfo.IS_LEAF).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.notNull(goodsCategory,"该商品分类不存在");
        if(goodsType.equals(GoodsInfo.GoodsInfoTypeEnum.GENERAL_GOODS.type())){
            AssertExt.isFalse((goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.GAS)&&goodsCategory.getCategoryName().equals(CommonConstant.CategoryName.RENT_BOTTLES))
                    ||goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.QUICKFOOD)
                    ||(goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.WATER)&&goodsCategory.getCategoryName().equals(CommonConstant.CategoryName.RENT_BUCKET)),"普通商品不能选择此分类");
        }
        if(goodsType.equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type())){
            AssertExt.isTrue(
                    goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.QUICKFOOD)||
                            goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.WATER)||
                            (goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.GAS)&&goodsCategory.getCategoryName().equals(CommonConstant.CategoryName.RENT_BOTTLES)),"组合商品不能选择此分类");
        }
        return goodsCategory;
    }

    private List<List<Long>> getList(List<GoodsAttributeListVO> goodsAttributeListVOS){
        List<List<Long>> lists=new ArrayList<>();
        List<List<Long>> list2=new ArrayList<>();
        for(GoodsAttributeListVO goodsAttributeListVOList:goodsAttributeListVOS){
            lists.add(goodsAttributeListVOList.getAttrValueKeys());
        }
        getlistId(lists,list2,0,new ArrayList<Long>());
        return list2;
    }

    private void getGoodsInfoList(GoodsInfoInsertVO goodsInfoVO,String spu,String sku,List<GoodsInfo> goodsInfos,List<GoodsBrandRelation> goodsBrandRelations,List<GoodsCategoryRelation> goodsCategoryRelations,LocalDateTime localDateTime,Long id){
        GoodsInfo goodsInfo=new GoodsInfo();
        BeanUtils.copyProperties(goodsInfoVO, goodsInfo);
        goodsInfo.setStatus(GoodsInfo.GoodsInfoEnum.ON_SALE.name());
        goodsInfo.setSpu(spu);
        goodsInfo.setCreateTime(localDateTime);
        goodsInfo.setCreateUserId(id);
        goodsInfo.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
        goodsInfo.setSku(sku);
        goodsInfo.setGoodsStyle(goodsInfoVO.getGoodsStyle());
        goodsInfos.add(goodsInfo);
        GoodsBrandRelation goodsBrandRelation=new GoodsBrandRelation();
        goodsBrandRelation.setBrandCode(goodsInfoVO.getBrandCode());
        goodsBrandRelation.setSku(sku);
        goodsBrandRelation.setCreateTime(localDateTime);
        goodsBrandRelation.setCreateUserId(id);
        goodsBrandRelations.add(goodsBrandRelation);
        GoodsCategoryRelation goodsCategoryRelation=new GoodsCategoryRelation();
        goodsCategoryRelation.setCategoryId(goodsInfoVO.getCategoryId());
        goodsCategoryRelation.setSku(sku);
        goodsCategoryRelation.setCreateTime(localDateTime);
        goodsCategoryRelation.setCreateUserId(id);
        goodsCategoryRelations.add(goodsCategoryRelation);
    }

    private List<Integer> getGoodsStyleByCategory(GoodsCategory goodsCategory){
        List<Integer> integers=null;
        switch (goodsCategory.getCategoryType()){
            case CommonConstant.CategoryType.GAS:
                integers=goodsCategory.getCategoryName().equals(CommonConstant.CategoryName.RENT_BOTTLES)?Arrays.asList(CommonConstant.GoodsStyle.GAS,CommonConstant.GoodsStyle.BOTTLE):null;
                break;
            case CommonConstant.CategoryType.WATER:
                integers=goodsCategory.getCategoryName().equals(CommonConstant.CategoryName.RENT_BUCKET)?integers=Arrays.asList(CommonConstant.GoodsStyle.WATER,CommonConstant.GoodsStyle.BUCKET):null;
                break;
            case CommonConstant.CategoryType.QUICKFOOD:
                integers=Arrays.asList(CommonConstant.GoodsStyle.NORMAL);
                break;
            default:
                integers=null;
                break;
        }
        return integers;
    }

    private List<GoodsAttributeValueVO> getGoodsAttributeValueList(List<GoodsAttributeValueBO> goodsAttributeValueBS,String sku){
        List<GoodsAttributeValueVO> goodsAttributeValueVOS=new ArrayList<>();
        for(GoodsAttributeValueBO goodsAttributeValueBO:goodsAttributeValueBS){
            if(sku.equals(goodsAttributeValueBO.getSku())){
                GoodsAttributeValueVO goodsAttributeValueVO=new GoodsAttributeValueVO();
                goodsAttributeValueVO.setAttrKey(goodsAttributeValueBO.getAttrKey());
                goodsAttributeValueVO.setAttrValueKey(goodsAttributeValueBO.getAttrValueKey());
                goodsAttributeValueVO.setAttrKeyName(goodsAttributeValueBO.getAttrKeyName());
                goodsAttributeValueVO.setAttrValueName(goodsAttributeValueBO.getAttrValueName());
                goodsAttributeValueVO.setIsSale(goodsAttributeValueBO.getIsSale());
                goodsAttributeValueVOS.add(goodsAttributeValueVO);
            }
        }
        return goodsAttributeValueVOS;
    }

    private List<GoodsAttributeDTO> getAttributeList(List<GoodsAttributeListVO> goodsAttributeListVOS){
        List<GoodsAttributeDTO> goodsAttributeDTOS=new ArrayList<>();
        for(GoodsAttributeListVO goodsAttributeListVO:goodsAttributeListVOS){
            for(Long attrValueKeys:goodsAttributeListVO.getAttrValueKeys()){
                GoodsAttributeDTO goodsAttributeDTO=new GoodsAttributeDTO();
                goodsAttributeDTO.setAttrKey(goodsAttributeListVO.getAttrKey());
                goodsAttributeDTO.setAttrValueKey(attrValueKeys);
                goodsAttributeDTOS.add(goodsAttributeDTO);
            }
        }
        return goodsAttributeDTOS;
    }

    private void getGoodsGroupList(List<GoodsGroupInsertVO> goodsGroupInsertVOS,List<GoodsGroup> goodsGroups,String sku,Long id,LocalDateTime time){
        for (GoodsGroupInsertVO goodsGroupInsertVO:goodsGroupInsertVOS){
            GoodsGroup goodsGroup=new GoodsGroup();
            goodsGroup.setSku(sku);
            goodsGroup.setSubSku(goodsGroupInsertVO.getSubSku());
            goodsGroup.setCreateTime(time);
            goodsGroup.setCreateUserId(id);
            goodsGroup.setSort(goodsGroupInsertVO.getSort()==null?0:goodsGroupInsertVO.getSort());
            goodsGroups.add(goodsGroup);
        }
    }

    private void getGoodsInfoSkuList(List<GoodsGroupInsertVO> goodsGroupInsertVOS){
        List<String> skus=new ArrayList<>();
        for(GoodsGroupInsertVO goodsGroup:goodsGroupInsertVOS){
            skus.add(goodsGroup.getSubSku());
        }
        Integer goodsInfos=goodsInfoMapper.selectCount(new QueryWrapper<GoodsInfo>().eq("is_del", CommonConstant.IsDel.IS_NOT_DEL).in("sku",skus));
        AssertExt.isFalse(skus.size()!=goodsInfos,"不存在该商品");
    }


    private void getlistId(List<List<Long>> attributeIds, List<List<Long>> ids, int layer, List<Long> curList) {
        if (layer < attributeIds.size() - 1) {
            if (attributeIds.get(layer).size() == 0) {
                getlistId(attributeIds, ids, layer + 1, curList);
            } else {
                for (int i = 0; i < attributeIds.get(layer).size(); i++) {
                    List<Long> list = new ArrayList<Long>(curList);
                    list.add(attributeIds.get(layer).get(i));
                    getlistId(attributeIds, ids, layer + 1, list);
                }
            }
        } else if (layer == attributeIds.size() - 1) {
            if (attributeIds.get(layer).size() == 0) {
                ids.add(curList);
            } else {
                for (int i = 0; i < attributeIds.get(layer).size(); i++) {
                    List<Long> list = new ArrayList<Long>(curList);
                    list.add(attributeIds.get(layer).get(i));
                    ids.add(list);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateGoodsInfo(GoodsInfoUpdateVO goodsInfoUpdateVO,Long id) {
        AssertExt.notBlank(goodsInfoUpdateVO.getGoodsName(),"商品名称为空");
        AssertExt.notBlank(goodsInfoUpdateVO.getSku(),"商品sku为空");
        AssertExt.notBlank(goodsInfoUpdateVO.getPicture(),"商品图片为空");
        GoodsInfo goodsInfo=goodsInfoMapper.selectOne(new QueryWrapper<GoodsInfo>().eq("sku",goodsInfoUpdateVO.getSku()).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.notNull(goodsInfo ==null,"该商品不存在");
        GoodsCategory goodsCategory=checkBrandAndCategory(goodsInfoUpdateVO.getBrandCode(),goodsInfoUpdateVO.getCategoryId(),goodsInfo.getGoodsType());
        GoodsCategory goodsCategory1=goodsCategoryMapper.selectGoodsCategoryInfoBySku(goodsInfoUpdateVO.getSku());
        AssertExt.isFalse(!goodsCategory.getCategoryType().equals(goodsCategory1.getCategoryType()),"商品分类只能选择相同类型分类");
        // 查询是否包含子商品信息
        Integer groupCount=goodsGroupMapper.selectCount(new QueryWrapper<GoodsGroup>().eq("sku",goodsInfoUpdateVO.getSku()));
        Integer goodsInfoType=groupCount>0?CommonConstant.GroupType.MULTI:CommonConstant.GroupType.ONE;
        //直接生成组合商品
        Boolean goodsCategoryType=goodsInfo.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type())&&goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.QUICKFOOD)
                &&goodsInfoType.equals(CommonConstant.GroupType.ONE);
        if(goodsInfo.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GENERAL_GOODS.type())){
            AssertExt.notEmpty(goodsInfoUpdateVO.getGoodsAttributeDTOS(),"商品属性列表为空");
            checkGoodsAttribute(goodsInfoUpdateVO.getGoodsAttributeDTOS().stream().map(GoodsAttributeDTO::getAttrKey).collect(Collectors.toList()));
        }
        if(goodsCategoryType){
            AssertExt.isFalse(CollectionUtils.isEmpty(goodsInfoUpdateVO.getGoodsAttributeDTOS()),"商品属性列表为空");
            checkGoodsAttribute(goodsInfoUpdateVO.getGoodsAttributeDTOS().stream().map(GoodsAttributeDTO::getAttrKey).collect(Collectors.toList()));
        }
        Boolean goodsType=goodsInfo.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type());
        GoodsInfo goodsInfo1=new GoodsInfo();
        goodsInfo1.setGoodsName(goodsInfoUpdateVO.getGoodsName());
        goodsInfo1.setPicture(goodsInfoUpdateVO.getPicture());
        goodsInfo1.setUpdateUserId(id);
        goodsInfo1.setDescription(goodsInfoUpdateVO.getDescription());
        GoodsBrandRelation goodsBrandRelation=new GoodsBrandRelation();
        goodsBrandRelation.setBrandCode(goodsInfoUpdateVO.getBrandCode());
        GoodsCategoryRelation goodsCategoryRelation=new GoodsCategoryRelation();
        goodsCategoryRelation.setCategoryId(goodsInfoUpdateVO.getCategoryId());
        List<GoodsAttributeRelation> goodsAttributeRelations=new ArrayList<>();
        for(GoodsAttributeDTO goodsAttributeDTO:goodsInfoUpdateVO.getGoodsAttributeDTOS()){
            GoodsAttributeRelation goodsAttributeRelation=new GoodsAttributeRelation();
            goodsAttributeRelation.setAttrKey(goodsAttributeDTO.getAttrKey());
            goodsAttributeRelation.setAttrValueKey(goodsAttributeDTO.getAttrValueKey());
            goodsAttributeRelation.setCreateTime(LocalDateTime.now());
            goodsAttributeRelation.setCreateUserId(id);
            goodsAttributeRelation.setSku(goodsInfoUpdateVO.getSku());
            goodsAttributeRelations.add(goodsAttributeRelation);
        }
        Integer integer=goodsInfoMapper.update(goodsInfo1,new QueryWrapper<GoodsInfo>().eq("sku",goodsInfoUpdateVO.getSku()).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.isFalse(integer!=1,"商品信息修改失败");
        if(!goodsInfoUpdateVO.getGoodsName().equals(goodsInfo.getGoodsName())){
            GoodsInfo goodsInfo2=new GoodsInfo();
            goodsInfo2.setGoodsName(goodsInfoUpdateVO.getGoodsName());
            Integer integer1=goodsInfoMapper.update(goodsInfo2,new QueryWrapper<GoodsInfo>().eq("spu",goodsInfo.getSpu()));
            AssertExt.isFalse(integer1<1,"商品品牌修改失败");
        }
        Integer goodsBrandCount=goodsBrandRelationMapper.update(goodsBrandRelation,new QueryWrapper<GoodsBrandRelation>().eq("sku",goodsInfoUpdateVO.getSku()));
        AssertExt.isFalse(goodsBrandCount!=1,"商品品牌修改失败");
        Integer goodsCategoryCount=goodsCategoryRelationMapper.update(goodsCategoryRelation,new QueryWrapper<GoodsCategoryRelation>().eq("sku",goodsInfoUpdateVO.getSku()));
        AssertExt.isFalse(goodsCategoryCount!=1,"商品类型修改失败");
        if(goodsInfo.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GENERAL_GOODS.type())||goodsCategoryType){
            Integer goodsAttributeCount=goodsAttributeRelationMapper.delete(new QueryWrapper<GoodsAttributeRelation>().eq("sku",goodsInfoUpdateVO.getSku()));
            AssertExt.isFalse(goodsAttributeCount<=0,"商品属性删除失败");
            Integer integer1=goodsAttributeRelationMapper.insertGoodsAttributeRelationList(goodsAttributeRelations);
            AssertExt.isFalse(integer1 == null || integer1 ==0,"商品属性插入失败");
        }
        if(goodsType){
            if(goodsCategoryType){
                return;
            }
            List<GoodsGroup> goodsGroups=new ArrayList<>();
            if(goodsInfo.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type())&&goodsCategory.getCategoryType().equals(CommonConstant.CategoryType.QUICKFOOD)
                    &&goodsInfoType.equals(CommonConstant.GroupType.MULTI)){
                if(!CollectionUtils.isEmpty(goodsInfoUpdateVO.getGoodsIngredientVOs())){
                    goodsInfoUpdateVO.getGoodsGroupInsertVOS().addAll(goodsInfoUpdateVO.getGoodsIngredientVOs());
                }
            }
            getGoodsInfoSkuList(goodsInfoUpdateVO.getGoodsGroupInsertVOS());
            getGoodsGroupList(goodsInfoUpdateVO.getGoodsGroupInsertVOS(),goodsGroups,goodsInfoUpdateVO.getSku(),id,LocalDateTime.now());
            Integer integer2=goodsGroupMapper.delete(new QueryWrapper<GoodsGroup>().eq("sku",goodsInfoUpdateVO.getSku()));
            AssertExt.isFalse(integer2<=0,"子商品修改失败");
            Integer goodsGroupCount=goodsGroupMapper.insertGoodsGroupList(goodsGroups);
            AssertExt.isFalse(goodsGroupCount == null || goodsGroupCount ==0,"子商品插入失败");
        }
    }

    @Override
    @Transactional
    public void deleteGoodsInfo(Long id,String sku) {
        AssertExt.notBlank(sku,"商品sku为空");
        Integer integer5=goodsStorePriceMapper.selectCount(new QueryWrapper<GoodsStorePrice>().eq("sku",sku).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.isFalse(integer5>0,"商品已被店铺使用,无法删除");
        GoodsInfo goodsInfo=new GoodsInfo();
        goodsInfo.setIsDel(CommonConstant.IsDel.IS_DEL);
        goodsInfo.setUpdateUserId(id);
        goodsInfo.setUpdateTime(LocalDateTime.now());
        Integer integer=goodsInfoMapper.update(goodsInfo,new QueryWrapper<GoodsInfo>().eq("sku",sku).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.isFalse(integer == null || integer ==0,"商品删除失败");
        Integer integer1=goodsBrandRelationMapper.delete(new QueryWrapper<GoodsBrandRelation>().eq("sku",sku));
        AssertExt.isFalse(integer1 == null || integer1 ==0,"商品品牌删除失败");
        Integer integer2=goodsCategoryRelationMapper.delete(new QueryWrapper<GoodsCategoryRelation>().eq("sku",sku));
        AssertExt.isFalse(integer2 == null || integer2 ==0,"商品分类删除失败");
        Integer integer3=goodsAttributeRelationMapper.delete(new QueryWrapper<GoodsAttributeRelation>().eq("sku",sku));
        AssertExt.isFalse(integer3 == null || integer3 ==0,"商品属性删除失败");
        Integer integer4=goodsGroupMapper.delete(new QueryWrapper<GoodsGroup>().eq("sku",sku));
    }

    @Override
    public GoodsInfoVO getGoodsInfo(String sku) {
        AssertExt.notBlank(sku,"商品sku为空");
        GoodsInfoListBO goodsInfo=goodsInfoMapper.selectGoodsInfo(sku, CommonConstant.IsDel.IS_NOT_DEL);
        AssertExt.notNull(goodsInfo,"该商品不存在");
        GoodsInfoVO goodsInfoVO=new GoodsInfoVO();
        BeanUtils.copyProperties(goodsInfo,goodsInfoVO);
        List<GoodsAttributeValueBO> goodsAttributeValueBOS=goodsAttributeRelationMapper.selectValueNameListById(sku);
        if(goodsInfo.getGoodsType().equals(CommonConstant.GoodsType.NORMAL)){
            AssertExt.notEmpty(goodsAttributeValueBOS,"商品属性不存在");
        }
        GoodsCategory goodsCategory=goodsCategoryMapper.selectGoodsCategoryInfoBySku(sku);
        if(goodsCategory!=null){
            goodsInfoVO.setCategoryId(goodsCategory.getId());
            goodsInfoVO.setCategoryName(goodsCategory.getCategoryName());
            goodsInfoVO.setCategoryType(goodsCategory.getCategoryType());
        }
        if(goodsInfo.getGoodsType().equals(GoodsInfo.GoodsInfoTypeEnum.GOODS_OF_JOINT.type())){
            List<GoodsGroupSkuNameVO> goodsGroupSkuNameVOS=goodsGroupMapper.selectGoodsSkuNameInfo(sku);
            goodsInfoVO.setGoodsGroupSkuNameVOS(goodsGroupSkuNameVOS.size()>0?goodsGroupSkuNameVOS:null);
        }
        goodsInfoVO.setGoodsAttributeValueVOS(getGoodsAttributeValueList(goodsAttributeValueBOS,sku));
        return goodsInfoVO;
    }
}
