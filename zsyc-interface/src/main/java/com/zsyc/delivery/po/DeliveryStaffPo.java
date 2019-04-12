package com.zsyc.delivery.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 配送结算
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeliveryStaffPo extends BaseBean {

    private String masterType;

    private Long id;

    private String masterName;

    private String phone;

    private Long storeId;

    private Integer currentPage;

    private Integer pageSize;

    private List<String> papers;
}
