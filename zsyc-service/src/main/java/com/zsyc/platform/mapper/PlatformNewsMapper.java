package com.zsyc.platform.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.platform.entity.PlatformNews;

import java.util.List;

/**
 * <p>
 * 店铺详情表 Mapper 接口
 * </p>
 *
 * @author MP
 * @since 2019-01-14
 */
public interface PlatformNewsMapper extends BaseMapper<PlatformNews> {

    /**
     * 通过ids删除
     * @param
     * @return
     */

    int deleteNewsByIds(List<Long> ids, Long loginUserId);


    /**
     * 查询新闻列表
     * @param page
     * @return
     */
    IPage<PlatformNews> getPlatformNewsList(IPage<PlatformNews> page);

}
