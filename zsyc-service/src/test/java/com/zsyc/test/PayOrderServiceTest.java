package com.zsyc.test;

import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import com.zsyc.pay.service.PayOrderService;
import com.zsyc.pay.service.PayOrderServiceImpl;
import com.zsyc.pay.vo.PayOrderVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lcs on 2019/4/07.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {
		Config.class,
		AnnotationConfigContextLoader.class,
})
@Slf4j
public class PayOrderServiceTest {

	@Autowired
	private PayOrderService payOrderService;

	@Test
	public void testPayOrder(){
		PayOrderVo payOrderVo = new PayOrderVo();
		payOrderVo.setTotalFee(100);
		payOrderVo.setBody("body");
		payOrderVo.setOpenid("ov7Y-5WolGLe21xuBdEKulqRxjrk");
		payOrderVo.setTimeStart(LocalDateTime.now());
		payOrderVo.setOrderNo("NO_" + System.currentTimeMillis());
		payOrderVo.setDataSource("test");
//		payOrderVo.setSpbillCreateIp(JSON.parseObject(HttpRequest.get("http://ipinfo.io").acceptJson().body()).getString("ip"));
		payOrderVo.setSpbillCreateIp("59.41.146.64");

		log.info("{}",this.payOrderService.payOrder(payOrderVo));
	}

	@Test
	public void testSign(){
		Map<String, Object> data = new HashMap<>();
		data.put("appid","wxd930ea5d5a258f4f");
		data.put("mch_id","10000100");
		data.put("device_info","1000");
		data.put("body","test");
		data.put("nonce_str","ibuaiVcKdpRxkhJA");
//		System.out.println(this.payOrderService.testSign(data));
	}

	@Test
	public void checkSign(){
		String data = "<xml><return_code><![CDATA[SUCCESS]]></return_code>\n" +
				"<return_msg><![CDATA[OK]]></return_msg>\n" +
				"<appid><![CDATA[wxdb5387c270bc68ff]]></appid>\n" +
				"<mch_id><![CDATA[1530323281]]></mch_id>\n" +
				"<nonce_str><![CDATA[hyPpLFuGaWM4KFNN]]></nonce_str>\n" +
				"<sign><![CDATA[4915935A6E08A04B6C4FC3865BBEDF98]]></sign>\n" +
				"<result_code><![CDATA[FAIL]]></result_code>\n" +
				"<err_code><![CDATA[SYSTEMERROR]]></err_code>\n" +
				"<err_code_des><![CDATA[system error]]></err_code_des>\n" +
				"</xml>";
//		this.payOrderService.checkSign(data);

	}


}
