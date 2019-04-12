package com.zsyc.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zsyc.pay.service.PayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lcs on 2019-04-08.
 */
@RestController
@RequestMapping("pay/wechat")
@Slf4j
public class WechatPayController {
	@Reference
	private PayOrderService payOrderService;

	@RequestMapping("pay-notify")
	public String payNotify(@RequestBody String xmlData) {
		log.info("pay notify data {}", xmlData);
		this.payOrderService.callback(xmlData);
		return "<xml>\n" +
				"  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
				"  <return_msg><![CDATA[OK]]></return_msg>\n" +
				"</xml>";
	}

	@RequestMapping("refund-notify")
	public String refundNotify(@RequestBody String xmlData) {
		log.info("pay notify data {}", xmlData);
		throw new NotImplementedException("refund-notify");
	}
}
