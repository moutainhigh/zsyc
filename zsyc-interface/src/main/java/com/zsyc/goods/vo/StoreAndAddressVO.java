package com.zsyc.goods.vo;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 店铺地址
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StoreAndAddressVO extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 地址ID
     */
    private Long addressId;

    /**
     * 分类ID
     */
    private Long categoryId;

}
