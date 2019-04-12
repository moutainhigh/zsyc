package com.zsyc.test;

import com.zsyc.account.service.LoginService;
import com.zsyc.account.vo.AccountVo;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * Created by lcs on 2018/9/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {
		Config.class,
		AnnotationConfigContextLoader.class,
})
@Slf4j
public class AccountServiceTest {

	@Autowired
	private LoginService loginService;

	@Test
	public void testRegistry(){
		val a = new AccountVo();
		a.setPasswordConfirm("test1234");
		a.setPassword("test1234");
		a.setAccount("admin123");
		log.info(this.loginService.registry(a).toString());
	}

	@Test
	public void testLogin(){
		val a = new AccountVo();
		a.setPassword("test1234");
		a.setAccount("admin123");
		log.info(this.loginService.login(a).toString());
	}

}
