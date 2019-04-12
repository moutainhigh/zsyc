package com.zsyc.member.vo;

import com.zsyc.framework.base.BaseBean;
import com.zsyc.member.entity.MemberAddressStore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 地址类型门店关联表
 * </p>
 *
 * @author MP
 * @since 2019-02-02
 */
@Data
public class MemberAddressStoreVo extends MemberAddressStore {

    private Long storeTypeId;

    private String storeName;

    private String phone;

    private String address;
}
