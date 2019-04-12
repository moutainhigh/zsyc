package com.zsyc.test;

import com.zsyc.account.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * Created by lcs on 2019-01-09.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {
		Config.class,
		AnnotationConfigContextLoader.class,
})
@Slf4j
public class AccountMapperTest {
	@Autowired
	private AccountMapper accountMapper;

	@Test
	public void test(){
		log.info("getAccounts size {}", this.accountMapper.getAccounts(2).size());
	}
}
