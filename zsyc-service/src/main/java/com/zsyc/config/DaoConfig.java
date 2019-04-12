package com.zsyc.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by lcs on 2018/9/26.
 */

@Configuration
@MapperScan(basePackages = {"com.zsyc.**.dao","com.zsyc.**.mapper",})
public class DaoConfig {

//	@Bean
//	public SqlSessionFactoryBean sqlSessionFactory(PaginationInterceptor paginationInterceptor,DataSource dataSource) throws IOException {
//		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
//		factory.setMapperLocations(
//				new PathMatchingResourcePatternResolver().getResources("classpath*:**/mapper/*.xml")
//		);
//		factory.setDataSource(dataSource);
//		factory.setPlugins(new Interceptor[]{paginationInterceptor});
//		return factory;
//	}

	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

}
