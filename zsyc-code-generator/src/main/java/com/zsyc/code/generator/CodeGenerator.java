package com.zsyc.code.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.ResourceBundle;
import java.util.Scanner;


/**
 * Created by lcs on 2018-12-25.
 */
public class CodeGenerator {


	public static void main(String[] args) {
		// 代码生成器


		AutoGenerator mpg = new AutoGenerator();

		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		String projectPath = getString("out.path");
		gc.setOutputDir(projectPath + "/src/main/java");
		gc.setAuthor("MP");
		gc.setOpen(false);
		gc.setFileOverride(true);
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUrl(getString("jdbc.url"));
		dsc.setDriverName("com.mysql.cj.jdbc.Driver");
		dsc.setUsername(getString("jdbc.username"));
		dsc.setPassword(getString("jdbc.password"));
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setModuleName(getString("module"));
		pc.setParent(getString("package"));
		mpg.setPackageInfo(pc);

		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				// to do nothing
			}
		};

		mpg.setCfg(cfg);

		// 配置模板+-
		TemplateConfig templateConfig = new TemplateConfig();

		templateConfig.setXml(null);
		mpg.setTemplate(templateConfig);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);

		if(StringUtils.isNoneBlank(getString("tablePrefix"))){
			strategy.setTablePrefix(getStringArray("tablePrefix"));
		}

		strategy.setSuperEntityClass(getString("entity.supper-class"));
//		strategy.setSuperEntityColumns(getStringArray("entity.supper-class.fields"));
		strategy.setEntityLombokModel(true);
//		strategy.setRestControllerStyle(true);
//		strategy.setSuperControllerClass(rb.getString("entity.supper-class"));
//		strategy.setInclude(scanner("表名"));

		strategy.setControllerMappingHyphenStyle(true);
		mpg.setStrategy(strategy);
		mpg.setTemplateEngine(new FreemarkerTemplateEngine());
		mpg.execute();
	}

	static ResourceBundle rb =  ResourceBundle.getBundle("applications");
	private static String getString(String key){
		return rb.getString(key);
	}

	private static String[] getStringArray(String key){
		String val =  rb.getString(key);
		if(val == null)return null;
		return val.split(",");
	}

}
