package com.zsyc.warehouse.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zsyc.warehouse.service.IWarehouseOrderServiceImpl;
import com.zsyc.warehouse.service.WarehouseOrderService;
import com.zsyc.warehouse.task.util.SpringContextUtil;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * 定时任务 根据店铺ID和店铺设置的提前时间 定时生成备货表
 * @author lijian
 *
 */
@Slf4j
public class TaskJob {


	public void run(Long storeId,Integer minute,IWarehouseOrderServiceImpl warehouseOrderService) {
		log.debug("zsyc-service TaskJob start storeId:"+storeId);
		try {
			warehouseOrderService.createWarehouseByStoreId(storeId, minute);
		} catch (Exception e) {
			log.error("zsyc-service TaskJob error:",e);
		}
		log.debug("zsyc-service TaskJob end");
	}

}
