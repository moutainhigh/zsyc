package com.zsyc.goods.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.common.AssertExt;
import com.zsyc.common.CommonConstant;
import com.zsyc.goods.entity.GoodsBrand;
import com.zsyc.goods.entity.GoodsBrandRelation;
import com.zsyc.goods.mapper.GoodsBrandMapper;
import com.zsyc.goods.mapper.GoodsBrandRelationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 周凯俊 on 2019/2/22.
 * @Explain:商品品牌
 */
@Service
@Slf4j
public class GoodsBrandServiceImpl implements GoodsBrandService {

    @Autowired
    private GoodsBrandMapper goodsBrandMapper;

    @Autowired
    private GoodsBrandRelationMapper goodsBrandRelationMapper;

    @Override
    public IPage<GoodsBrand> getGoodsBrandList(Page page,String name) {
        if(name!=null){
            return goodsBrandMapper.selectPage(page,new QueryWrapper<GoodsBrand>().eq("is_del", CommonConstant.IsDel.IS_NOT_DEL).and(true,x->x.like("brand_name",name).or().like("brand_code",name)));
        }
        return goodsBrandMapper.selectPage(page,new QueryWrapper<GoodsBrand>().eq("is_del", CommonConstant.IsDel.IS_NOT_DEL));
    }


    @Override
    public void addGoodsBrand(GoodsBrand goodsBrand,Long id) {
        AssertExt.notBlank(goodsBrand.getBrandName(),"品牌名称为空");
        AssertExt.notBlank(goodsBrand.getBrandCode(),"品牌标签为空");
        Integer count=goodsBrandMapper.selectCount(new QueryWrapper<GoodsBrand>().eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).and(true,x->x.eq("brand_name",goodsBrand.getBrandName()).or().eq("brand_code",goodsBrand.getBrandCode())));
        AssertExt.isFalse(count!=0,"品牌名称或标签已存在");
        goodsBrand.setCreateUserId(id);
        goodsBrand.setCreateTime(LocalDateTime.now());
        goodsBrand.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
        Integer integer=goodsBrandMapper.insert(goodsBrand);
        AssertExt.isFalse(integer!=1,"品牌插入失败");
    }

    @Override
    public void updateGoodsBrand(GoodsBrand goodsBrand) {
        AssertExt.hasId(goodsBrand.getId(),"品牌id为空");
        AssertExt.notBlank(goodsBrand.getBrandName(),"品牌名称为空");
        Integer count=goodsBrandMapper.selectCount(new QueryWrapper<GoodsBrand>().eq("is_del",CommonConstant.IsDel.IS_NOT_DEL).ne("id",goodsBrand.getId()).eq("brand_name",goodsBrand.getBrandName()));
        AssertExt.isFalse(count!=0,"品牌名称已存在");
        Map<String,Object> map=new HashMap<>();
        map.put("id",goodsBrand.getId());
        map.put("is_del", CommonConstant.IsDel.IS_NOT_DEL);
        System.out.println(goodsBrand.getCreateTime());
        GoodsBrand goodsBrand1=new GoodsBrand();
        goodsBrand1.setBrandName(goodsBrand.getBrandName());
        Integer integer=goodsBrandMapper.update(goodsBrand1,new QueryWrapper<GoodsBrand>().allEq(map,true));
        AssertExt.isFalse(integer!=1,"品牌修改失败");
    }

    @Override
    public void deleteGoodsBrand(Long id) {
        AssertExt.hasId(id,"品牌id为空");
        GoodsBrand goodsBrand1=goodsBrandMapper.selectOne(new QueryWrapper<GoodsBrand>().eq("id",id).eq("is_del",CommonConstant.IsDel.IS_NOT_DEL));
        AssertExt.notNull(goodsBrand1,"该品牌不存在");
        Integer integer1=goodsBrandRelationMapper.selectCount(new QueryWrapper<GoodsBrandRelation>().eq("brand_code",goodsBrand1.getBrandCode()));
        AssertExt.isTrue(integer1<=0,"该品牌已被使用,无法删除");
        GoodsBrand goodsBrand=new GoodsBrand();
        goodsBrand.setIsDel(CommonConstant.IsDel.IS_DEL);
        Map<String,Object> map=new HashMap<>();
        map.put("id",id);
        map.put("is_del", CommonConstant.IsDel.IS_NOT_DEL);
        Integer integer=goodsBrandMapper.update(goodsBrand,new QueryWrapper<GoodsBrand>().allEq(map,true));
        AssertExt.isFalse(integer!=1,"品牌删除失败");
    }

    @Override
    public GoodsBrand getGoodsBrand(Long id) {
        AssertExt.hasId(id,"品牌id为空");
        Map<String,Object> map=new HashMap<>();
        map.put("id",id);
        map.put("is_del", CommonConstant.IsDel.IS_NOT_DEL);
        GoodsBrand goodsBrand=goodsBrandMapper.selectOne(new QueryWrapper<GoodsBrand>().allEq(map,true));
        AssertExt.notNull(goodsBrand,"该品牌为空");
        return goodsBrand;
    }
}
