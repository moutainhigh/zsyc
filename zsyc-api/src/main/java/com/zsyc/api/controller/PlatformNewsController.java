package com.zsyc.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsyc.api.AccountHelper;
import com.zsyc.platform.entity.PlatformNews;
import com.zsyc.platform.po.PlatformNewsPo;
import com.zsyc.platform.service.PlatformNewsService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by lcs on 2019-01-09.
 */
@RestController
@RequestMapping("api/news")
@Api
public class PlatformNewsController {

	@Reference
	private PlatformNewsService platformNewsService;
	@Autowired
	private AccountHelper accountHelper;


	@ApiOperation("添加新闻----后台")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "title", value = "标题"),
			@ApiImplicitParam(name = "content", value = "内容"),
			@ApiImplicitParam(name = "imgUrl", value = "图片")
	})
	@ApiResponse(code = 200, message = "")
	@PostMapping(value = "addNews", produces = MediaType.APPLICATION_JSON_VALUE)
	public int addNews(@RequestBody PlatformNewsPo platformNewsPo) {
		return platformNewsService.addNews(platformNewsPo.getTitle(), platformNewsPo.getContent(), platformNewsPo.getImgUrl(),
											accountHelper.getUser().getId());
	}


	@ApiOperation("修改新闻----后台")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "title", value = "标题"),
			@ApiImplicitParam(name = "content", value = "内容"),
			@ApiImplicitParam(name = "imgUrl", value = "图片")
	})
	@ApiResponse(code = 200, message = "")
	@PostMapping(value = "updateNews", produces = MediaType.APPLICATION_JSON_VALUE)
	public int updateNews(@RequestBody PlatformNewsPo platformNewsPo) {
		return platformNewsService.updateNews(platformNewsPo.getId(), platformNewsPo.getTitle(), platformNewsPo.getContent(),
											platformNewsPo.getImgUrl(), accountHelper.getUser().getId());
	}

	@ApiOperation("删除新闻----后台")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids", value = "新闻ids")
	})
	@ApiResponse(code = 200, message = "")
	@PostMapping(value = "deleteNews", produces = MediaType.APPLICATION_JSON_VALUE)
	public int deleteNews(@RequestBody Map<String, Object> map) {
		List<Long> ids = (List<Long>)map.get("ids");
		return platformNewsService.deleteStore(ids, accountHelper.getUser().getId());
	}

	@ApiOperation("获取新闻列表----后台")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "currentPage", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(name = "pageSize", value = "页数", required = true, dataType = "int"),})
	@GetMapping(value = "getNewsList",produces = MediaType.APPLICATION_JSON_VALUE)
	public IPage<PlatformNews> getNewsList(Integer currentPage, Integer pageSize){
		return platformNewsService.getPlatformNews(currentPage, pageSize);
	}

	@ApiOperation("获取新闻----后台")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "新闻id", required = true, dataType = "int")})
	@GetMapping(value = "getNews",produces = MediaType.APPLICATION_JSON_VALUE)
	public PlatformNews getNews(Long id){
		return platformNewsService.getNews(id);
	}

}
