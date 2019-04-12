package com.zsyc.warehouse.task;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsyc.store.entity.StoreInfo;
import com.zsyc.store.mapper.StoreInfoMapper;
import com.zsyc.store.vo.StoreInfoVo;
import com.zsyc.warehouse.entity.WarehouseOrderGoods;
import com.zsyc.warehouse.service.WarehouseOrderService;
import com.zsyc.warehouse.task.util.ScheduleUtils;
import com.zsyc.warehouse.task.util.ScheduleUtils.Job;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lijian
 * 定时器初始化
 */
@Slf4j
@Component
public class TaskJobInit {


	@Autowired
	private WarehouseOrderService warehouseOrderService;

	@Autowired
    private StoreInfoMapper storeInfoMapper;

	/*@Scheduled(cron="0/10 * *  * * ? ")
	public void print() {
		System.out.println("print.......");
	}*/

	@PostConstruct
	public void init() {
		log.debug("zsyc-service TaskJobInit init start:");
		try {
			List<StoreInfo> storeList = storeInfoMapper.getIntervalStore();
			Job job =null;
			for(StoreInfo store :storeList) {
				job = new Job();
		        job.setJobName(store.getId().toString());
		        //cron表达式
		        job.setCron("* */"+store.getIntervalTime()+" * * * *");
		        job.setParas(new Object[] {store.getId(),store.getPresetTime(),warehouseOrderService});
		        job.setStatus(store.getIntervalState());

		        ScheduleUtils.add(job);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("zsyc-service TaskJobInit init error:",e);
		}

    	log.debug("zsyc-service TaskJobInit init end:");
	}
}
