package com.zsyc.pay.vo;

import com.zsyc.pay.entity.PayOrder;
import com.zsyc.pay.entity.PayTrade;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by lcs on 2019-04-07.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PayOrderVo extends PayOrder {

	private MiniPayInfoVo miniPayInfoVo;
	private enum EMetaData {openid,tradeType,spbillCreateIp,prepayId}
	/**
	 * 交易流水
	 */
	private List<PayTrade> tradeList;

	/**
	 * openid
	 * @return
	 */

	public String getOpenid() {
		return super.getMetaData(EMetaData.openid.name());
	}
	/**
	 * openid
	 * @return
	 */
	public void setOpenid(String openid) {
		super.setMetaData(EMetaData.openid.name(), openid);
	}
	/**
	 * tradeType
	 * @return
	 */
	public String getTradeType() {
		return super.getMetaData(EMetaData.tradeType.name());
	}

	/**
	 * tradeType
	 * @return
	 */
	public void setTradeType(String openid) {
		super.setMetaData(EMetaData.tradeType.name(), openid);
	}

	/**
	 * spbillCreateIp
	 * @return
	 */
	public String getSpbillCreateIp() {
		return super.getMetaData(EMetaData.spbillCreateIp.name());
	}
	/**
	 * spbillCreateIp
	 * @return
	 */
	public void setSpbillCreateIp(String spbillCreateIp) {
		super.setMetaData(EMetaData.spbillCreateIp.name(), spbillCreateIp);
	}

	/**
	 * prepayId
	 * @return
	 */
	public String getPrepayId() {
		return super.getMetaData(EMetaData.prepayId.name());
	}
	/**
	 * prepayId
	 * @return
	 */
	public void setPrepayId(String prepayId) {
		super.setMetaData(EMetaData.prepayId.name(), prepayId);
	}



}
