package com.zsyc.member.vo;

import com.zsyc.member.entity.MemberInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberInfoVo extends MemberInfo {

    private String address;
}
