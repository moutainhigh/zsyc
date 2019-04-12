package com.zsyc.warehouse.BackEndVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsyc.warehouse.entity.WarehouseOrderGoods;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WareHouseOrderVo extends WarehouseOrderGoods {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long warehouseOrderGoodsId;

    /**
     * 备货订单编号
     */
    private String warehouseOrderNo;

    /**
     * 生成备货时间
     */
    private LocalDateTime wareHouseCreateTime;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 备货状态 READY-准备中，STOCKING-备货中，DONE-确认备货
     */
    private String wareHouseOrderStatus;

    /**
     * 备货员id
     */
    private Long warehouseStaffId;

    /**
     * 接收备货时间
     */
    private LocalDateTime receiveTime;

    private String wareHouseStatus;

    private String remark;
    private String goodsName;
    private String img;
    private Long wareHouseOrderid;


}
