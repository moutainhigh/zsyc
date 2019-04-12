package com.zsyc.pay.service;

import com.zsyc.pay.entity.PayOrder;

/**
 * 内部回调
 * Created by lcs on 2019-04-08.
 */
public interface InnerCallbackService {
	/**
	 * 支付成功回调
	 *
	 * @param payOrder
	 */
	default void payCallback(PayOrder payOrder) {
	}


	/**
	 * 退款成功回调
	 *
	 * @param payOrder
	 */
	default void refundCallback(PayOrder payOrder) {
	}
}
