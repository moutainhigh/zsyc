package com.zsyc.store.po;

import com.zsyc.framework.base.BaseBean;
import com.zsyc.store.entity.StoreInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StoreInfoPo extends BaseBean {

    private Long id;

    private String storeName;

    private String storeNo;

    private Integer currentPage;

    private Integer pageSize;

    private String scopeBusiness;

    private String beginTime;

    private String endTime;

    private String phone;

    private Integer carriage;

    private Integer rent;

    private Double longitude;

    private Double latitude;

    private String address;

    private Long storeTypeId;

    private List<String> picture;

    private String radius;

    private Integer adcode;

}
