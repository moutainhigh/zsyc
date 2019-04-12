package com.zsyc.platform.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsyc.common.AssertExt;
import com.zsyc.common.CommonConstant;
import com.zsyc.platform.entity.PlatformNews;
import com.zsyc.platform.mapper.PlatformNewsMapper;
import com.zsyc.store.entity.StoreInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class PlatformNewsImpl implements PlatformNewsService{

    @Autowired
    private PlatformNewsMapper platformNewsMapper;


    @Override
    @Transactional
    public int addNews(String title, String content, String imgUrl, Long loginUserId) {

        AssertExt.notBlank(title, "title为空");
        AssertExt.notBlank(content, "content为空");
        AssertExt.notBlank(imgUrl, "imgUrl为空");

        LocalDateTime now = LocalDateTime.now();

        PlatformNews platformNews = new PlatformNews();
        platformNews.setTitle(title);
        platformNews.setContent(content);
        platformNews.setImgUrl(imgUrl);
        platformNews.setCreateTime(now);
        platformNews.setCreateUserId(loginUserId);
        platformNews.setUpdateTime(now);
        platformNews.setUpdateUserId(loginUserId);
        platformNews.setIsDel(CommonConstant.IsDel.IS_NOT_DEL);
//        QueryWrapper<PlatformNews> queryWrapper = new QueryWrapper<>();
//        queryWrapper.and(wrapper -> wrapper.like("title", "sbsbs").or().like("img_url", 00000));
//        List<PlatformNews> platformNews1 = platformNewsMapper.selectList(queryWrapper);

        return platformNewsMapper.insert(platformNews);
    }

    @Override
    @Transactional
    public int updateNews(Long id, String title, String content, String imgUrl, Long loginUserId) {

        AssertExt.notBlank(title, "title为空");
        AssertExt.notBlank(content, "content为空");
        AssertExt.notBlank(imgUrl, "imgUrl为空");

        LocalDateTime now = LocalDateTime.now();

        PlatformNews platformNews = new PlatformNews();
        UpdateWrapper<PlatformNews> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("title", title);
        updateWrapper.set("content", content);
        updateWrapper.set("img_url", imgUrl);
        updateWrapper.set("update_time", now);
        updateWrapper.set("update_user_id", loginUserId);

        return platformNewsMapper.update(platformNews, updateWrapper);
    }

    @Override
    @Transactional
    public int deleteStore(List<Long> ids, Long loginUserId) {
        return platformNewsMapper.deleteNewsByIds(ids, loginUserId);
    }

    @Override
    public IPage<PlatformNews> getPlatformNews(Integer currentPage, Integer pageSize) {

        IPage<PlatformNews> page = new Page<PlatformNews>(currentPage, pageSize);
        IPage<PlatformNews> platformNewsPage = platformNewsMapper.getPlatformNewsList(page);
        return platformNewsPage;
    }

    @Override
    public PlatformNews getNews(Long id) {

        AssertExt.notNull(id, "id为空");
        return platformNewsMapper.selectById(id);
    }
}
