package com.zsyc.pay.service;

import com.zsyc.pay.entity.PayOrder;
import com.zsyc.pay.vo.PayOrderVo;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by lcs on 2019-04-03.
 */
public interface PayOrderService {
	/**
	 * 支付下单
	 * @param payOrder
	 * @return
	 */
	PayOrderVo payOrder(PayOrderVo payOrder);

	/**
	 * 重复支付，对支付失败的订单进行重复支付
	 *
	 * @param payOrder
	 * @return
	 */
	default PayOrderVo rePayOrder(PayOrderVo payOrder) {
		throw new NotImplementedException("rePayOrder NotImplementedException");
	}

	/**
	 * 支付结果通知
	 * @param xmlData
	 * @return
	 */
	PayOrder callback(String xmlData);


	/**
	 * 获取支付订单
	 * @param orderNo
	 * @return
	 */
	PayOrder queryOrder(String orderNo);

	/**
	 * 支付退款
	 *
	 * @param orderNo
	 * @return
	 */
	PayOrder refund(String orderNo, Integer refundFree);

}
