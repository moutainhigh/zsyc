package com.zsyc.warehouse.service;


import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.zsyc.warehouse.entity.WarehouseOrderGoods;
import com.zsyc.warehouse.mapper.WarehouseOrderGoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class IWarehouseOrderGoodsServiceImpl implements WarehouseOrderGoodsService {
    @Autowired
    public WarehouseOrderGoodsMapper whoGMapper;

    //    更新子备货单status状态（0是缺货1是完成）
    @Override
    public Integer updateOrderSubStauts(int orderSubId, String stauts) {
        return whoGMapper.updateOrderSubStauts(orderSubId, stauts);
    }

    //备货单(主完成按钮)
    @Override
    public Integer wareHouseDone(Long id,String remark) {
        whoGMapper.updateWareHousePackOrder(id);

        if(remark.equals("null"))remark = null;
        whoGMapper.updateWarehouseOrderRemark(id,remark);
        return whoGMapper.wareHouseDone(id);
    }

    @Override
    public Integer updateWarehouseOrderGoods(Long warehouseOrderGoodsId, String status,Long userId) {
        return whoGMapper.updateWarehouseOrderGoods(warehouseOrderGoodsId,status,userId);
    }

    @Override
    public Integer updateWarehouseOrderRemark(Long id, String remark) {
        return whoGMapper.updateWarehouseOrderRemark(id,remark);
    }
}
