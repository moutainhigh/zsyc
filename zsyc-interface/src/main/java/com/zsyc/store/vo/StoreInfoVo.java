package com.zsyc.store.vo;

import com.zsyc.store.entity.StoreInfo;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StoreInfoVo extends StoreInfo {

    /**
     * 店铺开盘时间
     */
    private LocalDateTime openTime;

    /**
     * 店铺收盘时间
     */
    private LocalDateTime closeTime;

    /**
     * 店铺商品id
     */
    private Long goodId;

    /**
     * 店铺商品名称
     */
    private String goodName;


}
