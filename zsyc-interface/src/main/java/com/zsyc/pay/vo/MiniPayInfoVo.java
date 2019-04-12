package com.zsyc.pay.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 小程序调起支付API信息
 * @link https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
 * Created by lcs on 2019-04-09.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MiniPayInfoVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String appId;
	private Long timeStamp;
	private String nonceStr;
	private String packageData;
	private String signType;
	private String paySign;
}
