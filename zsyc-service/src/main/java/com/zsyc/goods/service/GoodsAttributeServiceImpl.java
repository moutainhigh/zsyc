package com.zsyc.goods.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.common.AssertExt;
import com.zsyc.common.CommonConstant;
import com.zsyc.goods.entity.GoodsAttribute;
import com.zsyc.goods.entity.GoodsAttributeRelation;
import com.zsyc.goods.entity.GoodsAttributeValue;
import com.zsyc.goods.mapper.GoodsAttributeMapper;
import com.zsyc.goods.mapper.GoodsAttributeRelationMapper;
import com.zsyc.goods.mapper.GoodsAttributeValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Created by 周凯俊 on 2019/2/22.
 * @Explain:商品属性
 */
@Service
@Slf4j
public class GoodsAttributeServiceImpl implements GoodsAttributeService{

    @Autowired
    private GoodsAttributeMapper goodsAttributeMapper;

    @Autowired
    private GoodsAttributeRelationMapper goodsAttributeRelationMapper;

    @Autowired
    private GoodsAttributeValueMapper goodsAttributeValueMapper;

    @Override
    public IPage<GoodsAttribute> getGoodsAttributeList(Page page,String attrKeyName) {
        return goodsAttributeMapper.selectPage(page,new QueryWrapper<GoodsAttribute>().eq("is_del", CommonConstant.IsDel.IS_NOT_DEL).like("attr_key_name",attrKeyName==null?null:attrKeyName));
    }

    @Override
    public void addGoodsAttribute(GoodsAttribute goodsAttribute, Long id) {
        AssertExt.isFalse(goodsAttribute.getIsSale()==null,"是否售卖单位不能为空");
        AssertExt.isTrue(goodsAttribute.getIsSale().equals(CommonConstant.GoodsAttributeType.IS_NOT_SALE)||
                goodsAttribute.getIsSale().equals(CommonConstant.GoodsAttributeType.IS_SALE),"是否售卖单位不能为空");
        checkAttrKeyName(goodsAttribute.getAttrKeyName());
        goodsAttribute.setCreateUserId(id);
        goodsAttribute.setAttrKey(Long.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"))+""+(long)(Math.random() * 10000)));
        goodsAttribute.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
        Integer integer=goodsAttributeMapper.insert(goodsAttribute);
        AssertExt.isFalse(integer!=1,"商品属性插入失败");
    }

    @Override
    public void updateGoodsAttribute(GoodsAttribute goodsAttribute) {
        AssertExt.hasId(goodsAttribute.getId(),"商品属性id为空");
        checkAttrKeyName(goodsAttribute.getAttrKeyName());
        GoodsAttribute goodsAttribute1=new GoodsAttribute();
        goodsAttribute1.setId(goodsAttribute.getId());
        goodsAttribute1.setAttrKeyName(goodsAttribute.getAttrKeyName());
        Integer integer=goodsAttributeMapper.update(goodsAttribute1,new QueryWrapper<GoodsAttribute>().eq("id",goodsAttribute.getId()).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.isFalse(integer!=1,"商品属性修改失败");
    }

    private void checkAttrKeyName(String attrKeyName){
        AssertExt.notBlank(attrKeyName,"商品属性名称为空");
        Integer integer1=goodsAttributeMapper.selectCount(new QueryWrapper<GoodsAttribute>().eq("attr_key_name",attrKeyName).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL));
        if(integer1>1){
            log.error("存在多个属性名称,属性名称为{}",attrKeyName);
        }
        AssertExt.isFalse(integer1>=1,"该属性名称已存在");
    }

    @Override
    @Transactional
    public void deleteGoodsAttribute(Long id) {
        AssertExt.hasId(id,"商品属性id为空");
        GoodsAttribute goodsAttribute1=goodsAttributeMapper.selectOne(new QueryWrapper<GoodsAttribute>().eq("id",id).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.notNull(goodsAttribute1,"该商品属性不存在");
        Integer counts=goodsAttributeRelationMapper.selectCount(new QueryWrapper<GoodsAttributeRelation>().eq("attr_key",goodsAttribute1.getAttrKey()));
        AssertExt.isFalse(counts != null && counts>0,"存在商品,商品属性删除失败");
        GoodsAttribute goodsAttribute=new GoodsAttribute();
        goodsAttribute.setIsDel(CommonConstant.IsDel.IS_DEL);
        Integer integer=goodsAttributeMapper.update(goodsAttribute,new QueryWrapper<GoodsAttribute>().eq("id",id).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.isFalse(integer!=1,"商品属性删除失败");
        GoodsAttributeValue goodsAttributeValue=new GoodsAttributeValue();
        goodsAttributeValue.setIsDel(CommonConstant.IsDel.IS_DEL);
        goodsAttributeValueMapper.update(goodsAttributeValue,new QueryWrapper<GoodsAttributeValue>().eq("attr_key",goodsAttribute1.getAttrKey()));
    }

    @Override
    public GoodsAttribute getGoodsAttribute(Long id) {
        GoodsAttribute goodsAttribute=goodsAttributeMapper.selectOne(new QueryWrapper<GoodsAttribute>().eq("id",id).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.notNull(goodsAttribute,"该商品属性为空");
        return goodsAttribute;
    }
}
