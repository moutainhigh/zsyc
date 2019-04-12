package com.zsyc.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by lcs on 2019-01-02.
 */
@Slf4j
public class TT {

	@Test
	public void testChart(){
		Assert.isTrue(Pattern.matches("^[\\d\\w\\-]{2,4}$", "18996"));
	}

	@Test
	public void testRandomStringUtils(){
		System.out.println("--------------------------");
		System.out.println(RandomStringUtils.random(10));
		System.out.println(RandomStringUtils.random(10, false, true));
		System.out.println(RandomStringUtils.random(10, true, false));
		System.out.println(RandomStringUtils.random(10, true, true));

		System.out.println("--------------------------");
		System.out.println(RandomStringUtils.random(10));
		System.out.println(RandomStringUtils.random(10, false, true));
		System.out.println(RandomStringUtils.random(10, true, false));
		System.out.println(RandomStringUtils.random(10, true, true));

		System.out.println("--------------------------");
		System.out.println(RandomStringUtils.random(10));
		System.out.println(RandomStringUtils.random(10, false, true));
		System.out.println(RandomStringUtils.random(10, true, false));
		System.out.println(RandomStringUtils.random(10, true, true));
	}

	@Test
	public void testLocalDate(){
		System.out.println(LocalDateTime.now());
	}

	@Test
	public void testGson(){
		String data = "[{\"name\":\"经营大数据\",\"icon\":\"el-icon-bell\",\"path\":\"/datas\"},{\"name\":\"经营中心\",\"icon\":\"el-icon-service\",\"children\":[{\"path\":\"/StoreManager\",\"name\":\"店铺管理\",\"icon\":\"el-icon-service\"},{\"path\":\"/Test\",\"name\":\"团队管理\",\"icon\":\"el-icon-service\"},{\"path\":\"/1\",\"name\":\"报表统计\",\"icon\":\"el-icon-service\"}]},{\"name\":\"会员中心\",\"icon\":\"el-icon-view\",\"children\":[{\"path\":\"/MemberList\",\"name\":\"会员列表\",\"icon\":\"el-icon-view\"},{\"path\":\"/MemberGroup\",\"name\":\"会员分群\",\"icon\":\"el-icon-view\"},{\"path\":\"/MemberLeve\",\"name\":\"消费统计\",\"icon\":\"el-icon-view\"},{\"path\":\"/MemberCoupon\",\"name\":\"优惠体系\",\"icon\":\"el-icon-view\"}]},{\"name\":\"商品中心\",\"icon\":\"el-icon-news\",\"children\":[{\"path\":\"/3\",\"name\":\"商品列表\",\"icon\":\"el-icon-news\"},{\"path\":\"/3\",\"name\":\"商品价格管理\",\"icon\":\"el-icon-news\"},{\"path\":\"/3\",\"name\":\"新增商品\",\"icon\":\"el-icon-news\"},{\"path\":\"/3\",\"name\":\"商品属性管理\",\"icon\":\"el-icon-news\"}]},{\"name\":\"交易中心\",\"icon\":\"el-icon-upload\",\"children\":[{\"path\":\"/new_order\",\"name\":\"新订单追踪\",\"icon\":\"el-icon-news\"},{\"path\":\"/booking_order\",\"name\":\"预约订单\",\"icon\":\"el-icon-upload\"},{\"path\":\"/pay_type\",\"name\":\"支付类型\",\"icon\":\"el-icon-upload\"}]},{\"name\":\"备货中心\",\"icon\":\"el-icon-rank\",\"children\":[{\"path\":\"/Sweep\",\"name\":\"统一扫货\",\"icon\":\"el-icon-rank\"},{\"path\":\"/Sorting\",\"name\":\"统一分拣\",\"icon\":\"el-icon-rank\"},{\"path\":\"/Production\",\"name\":\"统一生产\",\"icon\":\"el-icon-rank\"}]},{\"name\":\"配送中心\",\"icon\":\"el-icon-bell\",\"children\":[{\"path\":\"/6\",\"name\":\"送货订单\",\"icon\":\"el-icon-bell\"},{\"path\":\"/6\",\"name\":\"自提订单\",\"icon\":\"el-icon-bell\"},{\"path\":\"/6\",\"name\":\"运力池\",\"icon\":\"el-icon-bell\"}]},{\"name\":\"客服中心\",\"icon\":\"el-icon-bell\",\"children\":[{\"path\":\"/Followup\",\"name\":\"缺货退换货跟进\",\"icon\":\"el-icon-bell\"},{\"path\":\"/CustomerService\",\"name\":\"过期未取跟进\",\"icon\":\"el-icon-bell\"}]},{\"name\":\"账号信息\",\"icon\":\"el-icon-bell\",\"path\":\"/8\"},{\"name\":\"帮助与反馈\",\"icon\":\"el-icon-bell\",\"path\":\"/9\"}]";
		List<AdminServiceTest.Item> list = new Gson().fromJson(data, ArrayList.class);
		log.info(">>>> {}", list.get(0).getIcon());

	}
}
