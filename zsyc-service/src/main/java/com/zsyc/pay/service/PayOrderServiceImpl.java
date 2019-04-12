package com.zsyc.pay.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zsyc.admin.service.BaseService;
import com.zsyc.common.AssertExt;
import com.zsyc.common.SnowFlakeUtil;
import com.zsyc.pay.entity.PayOrder;
import com.zsyc.pay.entity.PayTrade;
import com.zsyc.pay.mapper.PayOrderMapper;
import com.zsyc.pay.mapper.PayTradeMapper;
import com.zsyc.pay.vo.MiniPayInfoVo;
import com.zsyc.pay.vo.PayOrderVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 微信（小程序）支付
 * Created by lcs on 2019-04-07.
 */
@Service
@Slf4j
@Transactional
public class PayOrderServiceImpl extends BaseService implements PayOrderService {
	private final static String TRADE_TYPE = "JSAPI";
	private final static String FEE_TYPE = "CNY";
	private final static String SIGN_TYPE = "MD5";
	private final static String SUCCESS = "SUCCESS";
	private final static String API_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	private final static String API_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	private Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

	@Value("${zsyc.pay.wechat.appId}")
	private String appId;

	@Value("${zsyc.pay.wechat.apiKey}")
	private String apiKey;

	@Value("${zsyc.pay.wechat.mchId}")
	private String mchId;

	@Value("${zsyc.pay.wechat.callback.pay}")
	private String callback;

	@Autowired
	private PayOrderMapper payOrderMapper;

	@Autowired
	private PayTradeMapper payTradeMapper;

	@Reference
	private InnerCallbackService payCallbackService;

	@Override
	public PayOrderVo payOrder(PayOrderVo payOrder) {
		AssertExt.notBlank(payOrder.getOrderNo(), "业务单号为空");
		AssertExt.notBlank(payOrder.getDataSource(), "未填系统来源");
		AssertExt.notNull(payOrder.getTotalFee(), "未填支付金额");
		AssertExt.notBlank(payOrder.getBody(), "没有商品描述");
		AssertExt.notBlank(payOrder.getOpenid(), "无效openid");
		AssertExt.notBlank(payOrder.getSpbillCreateIp(), "无效SpbillCreateIp");

		PayOrder payOrderDB = this.payOrderMapper.selectOne(newQueryWrapper(PayOrder.class).eq("order_no", payOrder.getOrderNo()));

		AssertExt.isTrue(payOrderDB == null, "不能重复发起支付");

		payOrder.setMerchantId(this.mchId);
		payOrder.setMerchantKey(this.appId);
		payOrder.setTradeType(TRADE_TYPE);
		payOrder.setTimeStart(LocalDateTime.now());

		try {
			payOrder.setNotifyUrl(URLEncoder.encode(this.callback, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		payOrder.setNotifyUrl(this.callback);
		payOrder.setFeeType(FEE_TYPE);

		HashMap param = this.gson.fromJson(gson.toJson(payOrder), HashMap.class);

		for (String kry : "order_no,merchant_key,data_source,meta_data,merchant_id".split(",")) {
			param.remove(kry);
		}

		payOrder.setPayFlowNo(genPayFlowNo());
		param.put("total_fee", payOrder.getTotalFee());
		param.put("trade_type", payOrder.getTradeType());
		param.put("mch_id", payOrder.getMerchantId());
		param.put("appid", payOrder.getMerchantKey());
		param.put("openid", payOrder.getOpenid());
		param.put("out_trade_no", payOrder.getPayFlowNo());
		param.put("spbill_create_ip", payOrder.getSpbillCreateIp());
		param.put("time_start", payOrder.getTimeStart().format(DATE_TIME_FORMATTER));
		param.put("nonce_str", RandomStringUtils.random(32, true, true).toUpperCase());

		Map<String, Object> response = httpPost(API_UNIFIED_ORDER, param);

		if(SUCCESS.equals(response.get("return_code")) && SUCCESS.equals(response.get("result_code")) ){
			payOrder.setStatus(PayOrder.EOrderStatus.PENDING.val());
			payOrder.setPrepayId(response.get("prepay_id").toString());
			MiniPayInfoVo miniPayInfoVo = MiniPayInfoVo.builder()
					.appId(payOrder.getMerchantKey())
					.nonceStr(RandomStringUtils.random(32, true, true).toUpperCase())
					.timeStamp(System.currentTimeMillis() / 1000)
					.signType(SIGN_TYPE)
					.packageData(String.format("prepay_id=%s", payOrder.getPrepayId()))
					.build();
			miniPayInfoVo.setPaySign(this.sign(miniPayInfoVo));
			payOrder.setMiniPayInfoVo(miniPayInfoVo);
		}else{
			payOrder.setStatus(PayOrder.EOrderStatus.FAIL.val());
			payOrder.setMetaData("error_message",response.get("return_msg"));
		}

		payOrder.setCreateTime(LocalDateTime.now());
		payOrder.setUpdateTime(LocalDateTime.now());
		this.payOrderMapper.insert(payOrder);

		this.payTradeMapper.insert(PayTrade.builder()
				.payOrderId(payOrder.getId())
				.request(mapToXml(param))
				.payFlowNo(payOrder.getPayFlowNo())
				.response(mapToXml(response))
				.merchantId(payOrder.getMerchantId())
				.status(payOrder.getStatus())
				.createTime(LocalDateTime.now()).build());

		return payOrder;
	}

	@Override
	public PayOrder callback(String xmlData) {
		this.checkXmlData(xmlData);
		Map<String, String> data = xmlToMap(xmlData);
		if(!SUCCESS.equals(data.get("return_code"))) return null;
		String payFlowNo = data.get("out_trade_no");
		PayOrder payOrder = this.payOrderMapper.selectOne(newQueryWrapper(PayOrder.class).eq("pay_flow_no", payFlowNo));
		AssertExt.notNull(payOrder, "无效订单[%s]", payFlowNo);

		payOrder.setStatus(PayOrder.EOrderStatus.SUCCESS.val());
		payOrder.setTimeEnd(LocalDateTime.now());
		payOrder.setUpdateTime(LocalDateTime.now());

		this.payOrderMapper.updateById(payOrder);

		this.payTradeMapper.insert(PayTrade.builder()
				.response(xmlData)
				.payOrderId(payOrder.getId())
				.merchantId(payOrder.getMerchantId())
				.status(payOrder.getStatus())
				.createTime(LocalDateTime.now()).build());

		try{
			payCallbackService.payCallback(payOrder);
		}finally {
		}
		return null;
	}

	@Override
	public PayOrder queryOrder(String orderNo) {
		AssertExt.notBlank(orderNo, "无效订单号");
		PayOrder payOrder = this.payOrderMapper.selectOne(newQueryWrapper(PayOrder.class).eq("order_no", orderNo));
		AssertExt.notNull(payOrder, "无效订单[%s]",orderNo);
		this.payTradeMapper.selectList(newQueryWrapper(PayTrade.class).eq("pay_order_id", payOrder.getId()));
		return payOrder;
	}

	@Override
	public PayOrder refund(String orderNo, Integer refundFree) {
		AssertExt.notBlank(orderNo, "业务单号为空");

		PayOrder payOrderDB = this.payOrderMapper.selectOne(newQueryWrapper(PayOrder.class).eq("order_no", orderNo));
		AssertExt.notNull(payOrderDB, "没有支付信息");
		AssertExt.isTrue(PayOrder.EOrderStatus.SUCCESS.val().equals(payOrderDB.getStatus()), "支付完成支付");

		String outRefundNo = genPayRefunNo();
		Integer refundFee = payOrderDB.getTotalFee();
		payOrderDB.setRefundFee(payOrderDB.getTotalFee());
		Map<String, Object> param = new HashMap<>();

		param.put("appid", payOrderDB.getMerchantKey());
		param.put("mch_id", payOrderDB.getMerchantId());
		param.put("nonce_str", RandomStringUtils.random(32, true, true));
		param.put("sign_type", SIGN_TYPE);
		param.put("out_trade_no", payOrderDB.getPayFlowNo());
		param.put("out_refund_no", outRefundNo);
		param.put("total_fee", payOrderDB.getTotalFee());
		param.put("refund_fee", refundFee);
		param.put("refund_fee_type", payOrderDB.getFeeType());

		Map response = httpPost(API_REFUND, param);

		if(SUCCESS.equals(response.get("return_code")) && SUCCESS.equals(response.get("result_code")) ){
			payOrderDB.setStatus(PayOrder.EOrderStatus.REFUND.val());

		}else{
			payOrderDB.setStatus(PayOrder.EOrderStatus.REFUND_FAIL.val());
		}
		this.payOrderMapper.updateById(payOrderDB);
		this.payTradeMapper.insert(PayTrade.builder()
				.payOrderId(payOrderDB.getId())
				.request(mapToXml(param))
				.payFlowNo(payOrderDB.getPayFlowNo())
				.response(mapToXml(response))
				.merchantId(payOrderDB.getMerchantId())
				.status(payOrderDB.getStatus())
				.refundNo(outRefundNo)
				.refunFee(refundFee)
				.createTime(LocalDateTime.now()).build());

		return payOrderDB;
	}

	private Map<String,String> httpPost( String api,Map<String,Object> param){
		param.put("sign_type", SIGN_TYPE);
		param.put("sign", this.sign(param));
		String requestData = mapToXml(param);
		String res = null;
		try {
			HttpRequest request = HttpRequest.post(api);
			request.contentType(HttpRequest.CONTENT_TYPE_JSON).contentType("application/xml", "UTF-8").send(requestData);
			res = request.body();
			this.checkXmlData(res);
			return xmlToMap(res);
		}finally {
			log.info(" api [{}], param : {} \n response {}", api, requestData, res);
		}

	}

	/**
	 * map to xml
	 * @param data
	 * @return
	 */
	private static String mapToXml(Map<String, Object> data)  {
		return data.entrySet().stream()
				.map(entry -> String.format("<%s><![CDATA[%s]]></%s>", entry.getKey(), entry.getValue(), entry.getKey()))
				.collect(Collectors.joining("\n", "<xml>\n", "\n</xml>"));
	}

	/**
	 * xml to map
	 * @param strXML
	 * @return
	 */
	private static Map<String, String> xmlToMap(String strXML)  {
		try {
			Map<String, String> data = new HashMap<>();
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
			Document doc = documentBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			for (int idx = 0; idx < nodeList.getLength(); ++idx) {
				Node node = nodeList.item(idx);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					data.put(element.getNodeName(), element.getTextContent());
				}
			}
			try {
				stream.close();
			} catch (Exception ex) {
				// do nothing
			}
			return data;
		} catch (Exception e) {
			log.error("xmlToMap error",e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * md5 签名
	 * @param parameters
	 * @return
	 */
	private  String sign(Map<String, Object> parameters) {
		String paramStr = parameters.entrySet().stream()
				.filter(entry -> entry.getValue() != null && StringUtils.isNotBlank(entry.getValue().toString()))
				.map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue().toString()))
				.sorted()
				.collect(Collectors.joining("&"));
		paramStr = String.format("%s&key=%s", paramStr, this.apiKey);
		log.debug("sign string {}", paramStr);
		return DigestUtils.md5Hex(paramStr).toUpperCase();
	}

	private  String sign(MiniPayInfoVo miniPayInfoVo) {
		Map<String, Object> data = new HashMap<>();
		data.put("appId", miniPayInfoVo.getAppId());
		data.put("timeStamp", miniPayInfoVo.getTimeStamp());
		data.put("nonceStr", miniPayInfoVo.getNonceStr());
		data.put("package", miniPayInfoVo.getPackageData());
		data.put("signType", miniPayInfoVo.getSignType());
		return sign(data);
	}

	/**
	 * 校验报文
	 *
	 * @param xmlData
	 * @return
	 */
	private void checkXmlData(String xmlData) {
		Map data = xmlToMap(xmlData);
		String sign = data.get("sign").toString();
		data.remove("sign");
		AssertExt.isTrue(sign(data).equals(sign), "校验报文失败");
	}

	private static SnowFlakeUtil snowFlakeUtil = new SnowFlakeUtil(0, 0);
	/**
	 * 生成支付订单号
	 *
	 * @return
	 */
	private static String genPayFlowNo() {
		String date = String.format("%TF", new Date()).replaceAll("-", "");
		return String.format("ZSYC_%s%019d", date, snowFlakeUtil.nextId());

	}

	/**
	 * 生成退款单号
	 *
	 * @return
	 */
	private static String genPayRefunNo() {
		String date = String.format("%TF", new Date()).replaceAll("-", "");
		return String.format("ZSYC_REF_%s%019d", date, snowFlakeUtil.nextId());

	}

}
