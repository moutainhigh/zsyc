package com.zsyc.member.po;

import com.zsyc.framework.base.BaseBean;
import com.zsyc.member.entity.MemberAddressStore;
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
public class MemberAddressStorePo extends BaseBean {

        private Integer addressId;

        private String addressType;

        private Integer isCustom;
}
