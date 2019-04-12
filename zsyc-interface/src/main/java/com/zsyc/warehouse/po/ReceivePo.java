package com.zsyc.warehouse.po;

import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ReceivePo extends BaseBean {
  private Long id;
  public  Long subId[];
  public  Long staffId;
  private LocalDateTime createTime;
}
