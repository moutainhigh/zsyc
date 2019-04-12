package com.zsyc.member.po;

import com.zsyc.framework.base.BaseBean;
import com.zsyc.member.entity.MemberInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberInfoPo extends BaseBean {

    private Long storeId;

    private Long id;

    private String name;

    private String beginTime;

    private String endTime;

    private Integer currentPage;

    private Integer pageSize;

    private String telephone;

    private String status;
}
