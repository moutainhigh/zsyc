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

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Created by 周凯俊 on 2019/2/22.
 * @Explain:商品属性管理
 */
@Service
@Slf4j
public class GoodsAttributeValueServiceImpl implements GoodsAttributeValueService{

    @Autowired
    private GoodsAttributeValueMapper goodsAttributeValueMapper;

    @Autowired
    private GoodsAttributeRelationMapper goodsAttributeRelationMapper;

    @Autowired
    private GoodsAttributeMapper goodsAttributeMapper;

    @Override
    public IPage<GoodsAttributeValue> getGoodsAttributeValueList(Page page, Long id) {
        return goodsAttributeValueMapper.selectPage(page,new QueryWrapper<GoodsAttributeValue>().eq("attr_key",id).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
    }

    @Override
    public void addGoodsAttributeValue(GoodsAttributeValue goodsAttributeValue, Long id) {
        GoodsAttribute goodsAttribute1=goodsAttributeMapper.selectOne(new QueryWrapper<GoodsAttribute>().eq("attr_key",goodsAttributeValue.getAttrKey()).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.notNull(goodsAttribute1,"该商品属性不存在");
        AssertExt.hasId(goodsAttributeValue.getAttrKey(),"商品属性id为空");
        AssertExt.notBlank(goodsAttributeValue.getAttrValueName(),"商品属性值名称为空");
        goodsAttributeValue.setCreateUserId(id);
        goodsAttributeValue.setAttrValueKey(Long.valueOf((long)(Math.random() * 10000)+""+LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"))));      //暂时设置
        goodsAttributeValue.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
        Integer integer=goodsAttributeValueMapper.insert(goodsAttributeValue);
        AssertExt.isFalse(integer!=1,"商品属性值插入失败");
    }

    @Override
    public void updateGoodsAttributeValue(GoodsAttributeValue goodsAttributeValue) {
        AssertExt.hasId(goodsAttributeValue.getId(),"商品属性值id为空");
        AssertExt.notBlank(goodsAttributeValue.getAttrValueName(),"商品属性值名称为空");
        GoodsAttributeValue goodsAttributeValue1=new GoodsAttributeValue();
        goodsAttributeValue1.setAttrValueName(goodsAttributeValue.getAttrValueName());
        Integer integer=goodsAttributeValueMapper.update(goodsAttributeValue1,new QueryWrapper<GoodsAttributeValue>().eq("id",goodsAttributeValue.getId()).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.isFalse(integer!=1,"商品属性值修改失败");
    }

    @Override
    public void deleteGoodsAttributeValue(Long id) {
        AssertExt.hasId(id,"商品属性值id为空");
        GoodsAttributeValue goodsAttributeValue1=goodsAttributeValueMapper.selectOne(new QueryWrapper<GoodsAttributeValue>().eq("id",id).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.notNull(goodsAttributeValue1,"该商品属性值不存在");
        Integer counts=goodsAttributeRelationMapper.selectCount(new QueryWrapper<GoodsAttributeRelation>().eq("attr_value_key",goodsAttributeValue1.getAttrValueKey()));
        AssertExt.isFalse(counts != null && counts>0,"存在商品,商品属性值删除失败");
        GoodsAttributeValue goodsAttributeValue=new GoodsAttributeValue();
        goodsAttributeValue.setIsDel(CommonConstant.IsDel.IS_DEL);
        Integer integer=goodsAttributeValueMapper.update(goodsAttributeValue,new QueryWrapper<GoodsAttributeValue>().eq("id",id).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.isFalse(integer!=1,"商品属性值删除失败");
    }

    @Override
    public GoodsAttributeValue getGoodsAttributeValue(Long id) {
        GoodsAttributeValue goodsAttributeValue=goodsAttributeValueMapper.selectOne(new QueryWrapper<GoodsAttributeValue>().eq("id",id).eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.notNull(goodsAttributeValue,"该商品属性值为空");
        return goodsAttributeValue;
    }
}
