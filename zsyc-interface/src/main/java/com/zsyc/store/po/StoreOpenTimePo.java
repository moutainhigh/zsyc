package com.zsyc.store.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StoreOpenTimePo extends BaseBean {

    private String openTime;

    private String closeTime;

    private Long storeId;

}
