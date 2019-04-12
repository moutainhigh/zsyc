package com.zsyc.platform.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author MP
 * @since 2019-03-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PlatformNewsPo extends BaseBean {

    private String title;

    private String content;

    private String imgUrl;

    private Long id;
}
