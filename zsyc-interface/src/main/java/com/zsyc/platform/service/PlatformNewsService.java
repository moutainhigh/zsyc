package com.zsyc.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.platform.entity.PlatformNews;

import java.util.List;

public interface PlatformNewsService {

    /**
     * 添加新闻
     * @param title
     * @param content
     * @param imgUrl
     * @return
     */
    int addNews(String title, String content, String imgUrl, Long loginUserId);

    /**
     * 更改新闻
     * @param title
     * @param content
     * @param imgUrl
     * @return
     */
    int updateNews(Long id, String title, String content, String imgUrl, Long loginUserId);

    /**
     * 删除新闻
     * @param ids
     * @return
     */
    int deleteStore(List<Long> ids, Long loginUserId);

    /**
     * 获取新闻列表
     * @return
     */
    IPage<PlatformNews> getPlatformNews(Integer currentPage, Integer pageSize);

    /**
     * 获取新闻
     * @param id
     * @return
     */
    PlatformNews getNews(Long id);
}
