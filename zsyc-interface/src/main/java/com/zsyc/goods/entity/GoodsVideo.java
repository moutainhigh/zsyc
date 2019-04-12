package com.zsyc.goods.entity;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsVideo extends BaseBean {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 商品编码
     */
    private String sku;
    /**
     * 视频标题
     */
    private String title;
    /**
     * 视频描述
     */
    private String description;
    /**
     * 视频链接
     */
    private String url;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;


}
