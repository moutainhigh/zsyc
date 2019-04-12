package com.zsyc.member.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 地址类型门店关联表
 * </p>
 *
 * @author MP
 * @since 2019-02-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberAddressPo extends BaseBean {

    private String addressType;

    private Integer isCustom;

    private Integer isElevator;

    private String tag;

    private String locationAddress;

    private String consignee;

    private Integer sex;

    private Long memberId;

    private String telephone;

    private String address;

    private Integer storey;

    private Double lng;

    private Double lat;

    private Integer isDefault;

    private Long adcode;

    private Long addressId;

    private Integer currentPage;

    private Integer pageSize;

    private Long storeId;

    private Integer storeType;
}
