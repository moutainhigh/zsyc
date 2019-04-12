//package com.demo.test;
//
//import com.demo.DemoWebApplication;
//import com.demo.bean.Demo;
//import com.demo.service.DemoService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//
//import java.util.Date;
//import java.util.stream.Collectors;
//
///**
// * Created by lcs on 2018/9/27.
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Import(DemoWebApplication.class)
//@ContextConfiguration(classes = {
//		AnnotationConfigContextLoader.class,
//})
//public class DemoApplicationTest {
//
//	@Autowired
//	private DemoService demoService;
//
//	@Test
//	public void testAdd(){
//		this.demoService.add(Demo.builder().id(101L).age(1).name("shit").createTime(new Date()).build());
//	}
//	@Test
//	public void testPage(){
//		System.out.println(this.demoService.getPage(1,2)
//				.getRecords()
//				.stream()
//				.map(r->r.toString())
//				.collect(Collectors.joining("\n")));;
//	}
//	@Test
//	public void testXml(){
//		System.out.println(this.demoService.getNames()
//				.stream()
//				.collect(Collectors.joining("\n")));;
//	}
//	@Test
//	public void testGet(){
//		System.out.println(this.demoService.get(100L).toString());
//	}
//}
