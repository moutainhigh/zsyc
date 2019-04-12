package com.zsyc.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zsyc.admin.entity.Permission;
import com.zsyc.admin.entity.Role;
import com.zsyc.admin.entity.User;
import com.zsyc.admin.service.AdminService;
import com.zsyc.admin.service.PermissionService;
import com.zsyc.admin.vo.UserVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.*;

/**
 * Created by lcs on 2019-01-30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {
		Config.class,
		AnnotationConfigContextLoader.class,
})
@Slf4j
public class AdminServiceTest {

	@Autowired
	private AdminService adminService;
	@Autowired
	private PermissionService permissionService;

	@Test
	public void addUser() {
		UserVo u = new UserVo();
		u.setUserName("lcslcslcslcs");
		u.setName("lcs");
		u.setAccountPassword("zsyc2019");
		this.adminService.addUser(u, 0l);
	}

	@Test
	public void addRole(){
		Role role = new Role();
		role.setName("admin");
		this.adminService.addRole(role, 0l);
	}

	@Test
	public void addPermission(){
		Permission permission = new Permission();
		permission.setName("root");
		permission.setType("root");
		permission.setValue("root");
		this.permissionService.addPermission(this.adminService.getUser(5L), permission);
	}

	@Test
	public void updateRolePermission(){
		this.adminService.updateRolePermission(1L, Arrays.asList(this.permissionService.getPermission(1L)), 0l);
	}

	@Test
	public void updateUserRole(){
		this.adminService.updateUserRole(5L, this.adminService.getAllRoles(0L), 0l);
	}

	@Test
	public void initMenu(){
		String data = "[{\"name\":\"经营大数据\",\"icon\":\"icon-dashuju\",\"path\":\"/datas\"},{\"name\":\"菜单管理\",\"icon\":\"icon-huiyuan\",\"children\":[{\"path\":\"/RoleAdmin\",\"name\":\"角色管理\"},{\"path\":\"/MemberAccountList\",\"name\":\"权限管理\"},{\"path\":\"/MemberChargingList\",\"name\":\"后台账户添加\"},{\"path\":\"/MemberAddressList\",\"name\":\"账户绑定角色\"},{\"path\":\"/MemberChargingLogList\",\"name\":\"角色绑定权限\"}]},{\"name\":\"经营中心\",\"icon\":\"icon-jingying\",\"children\":[{\"path\":\"/StoreManager\",\"name\":\"店铺管理\"},{\"path\":\"/GroupManagement\",\"name\":\"团队管理\"},{\"path\":\"/1\",\"name\":\"报表统计\"}]},{\"name\":\"会员中心\",\"icon\":\"icon-huiyuan\",\"children\":[{\"path\":\"/MemberList\",\"name\":\"会员列表\"},{\"path\":\"/MemberAccountList\",\"name\":\"账号列表\"},{\"path\":\"/MemberChargingList\",\"name\":\"会员充值列表\"},{\"path\":\"/MemberAddressList\",\"name\":\"门店用户地址列表\"},{\"path\":\"/MemberChargingLogList\",\"name\":\"会员充值记录列表\"}]},{\"name\":\"商品中心\",\"icon\":\"icon-shangpin\",\"children\":[{\"path\":\"/Goods\",\"name\":\"商品列表\"},{\"path\":\"/GroupGoods\",\"name\":\"组合商品列表\"},{\"path\":\"/GoodsAdd\",\"name\":\"添加商品\"},{\"path\":\"/GoodsInsert\",\"name\":\"新增商品\"},{\"path\":\"/GoodsCategoryManage\",\"name\":\"商品分类\"},{\"path\":\"/GoodsBrandList\",\"name\":\"品牌列表\"},{\"path\":\"/GoodsAttribute\",\"name\":\"商品属性\"},{\"path\":\"/GoodsVIPPrice\",\"name\":\"vip价格列表\"}]},{\"name\":\"订单中心\",\"icon\":\"icon-jiaoyi\",\"children\":[{\"path\":\"/OrderList\",\"name\":\"订单列表\"},{\"path\":\"/AgreementList\",\"name\":\"协议列表\"},{\"path\":\"/DataForm\",\"name\":\"报表列表\"}]},{\"name\":\"备货中心\",\"icon\":\"icon-beihuoxiadan\",\"children\":[{\"path\":\"/PrepareList\",\"name\":\"备货单列表\"},{\"path\":\"/Sweep\",\"name\":\"统一扫货\"},{\"path\":\"/Sorting\",\"name\":\"分拣列表\"},{\"path\":\"/Production\",\"name\":\"统一生产\"}]},{\"name\":\"配送中心\",\"icon\":\"icon-peisong\",\"children\":[{\"path\":\"/6\",\"name\":\"送货订单\"},{\"path\":\"/6\",\"name\":\"自提订单\"},{\"path\":\"/6\",\"name\":\"运力池\"}]},{\"name\":\"客服中心\",\"icon\":\"icon-kefu\",\"children\":[{\"path\":\"/Followup\",\"name\":\"缺货退换货跟进\"},{\"path\":\"/CustomerService\",\"name\":\"售后信息\"}]},{\"name\":\"账号信息\",\"icon\":\"icon-zhanghaoxinxi_normal\",\"path\":\"/8\"},{\"name\":\"帮助与反馈\",\"icon\":\"icon-tuceng-bangzhufankui\",\"path\":\"/9\"}]";
		List<Item> list = new Gson().fromJson(data, new TypeToken<ArrayList<Item>>() {}.getType());
		add(this.adminService.getUser(5L), Permission.builder().id(1L).build(), list);

	}

	private void add(User user , Permission parent, List<Item> children) {
		if(children == null) return;
		if(children.isEmpty()) return;
		children.forEach(item -> {
			log.info("data>>>>>>{}", item);
			Permission p = this.permissionService.addPermission(user, Permission.builder()
					.parentId(parent.getId())
					.name(item.getName())
					.value(item.getPath())
					.type("menu")
					.icon(item.getIcon()).build());
			add(user, p, item.getChildren());
		});
	}

	@Test
	public void testGetRolePermission() {
		log.info("{}", this.adminService.getPermissionByRoleId(1L, Permission.PermissionType.MENU));
	}


	@Data
	public static class Item{
		private String name;
		private String icon;
		private String path;
		private List<Item> children;
	}
}
