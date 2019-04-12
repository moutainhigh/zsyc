package com.zsyc.fs.controller;

import com.zsyc.fs.entity.ZSYCFile;
import com.zsyc.fs.service.FileService;
import com.zsyc.fs.util.FileUtil;
import com.zsyc.fs.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;


/**
 * Created by lcs on 2019-02-01.
 */
@CrossOrigin(value="*", methods = {RequestMethod.GET, RequestMethod.POST})
@Controller
public class FileController {
	@Autowired
	private FileService fileBiz;

	@Value("${zsyc.file.path}")
	private String path;

	/**
	 * 上件
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("upload")
	@ResponseBody
	public ZSYCFile upload(MultipartFile file) throws IOException {
		return fileBiz.addFile(file, path);
	}
	/**
	 * 下载
	 */
	@RequestMapping(value="download/**/{path}/{fingerprint}.{suffix}", method = RequestMethod.GET)
	public void download(@PathVariable("path") String path, @PathVariable("fingerprint") String fingerprint, @PathVariable("suffix") String suffix,
						 HttpServletRequest request,
						 HttpServletResponse response){
		StringBuffer realPath = new StringBuffer(this.path);
		realPath.append("/").append(path).append("/").append(fingerprint);
		ZSYCFile file = fileBiz.get(fingerprint);
		if(file == null){
			return;
		}
		String userAgent = request.getHeader("User-Agent");
		response.setHeader("Content-Type", file.getContentType());
		try {
			response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", new String(userAgent.contains("MSIE") ? file.getName().getBytes() : file.getName().getBytes("UTF-8"), "ISO-8859-1")));
			FileUtil.download(response.getOutputStream(), realPath.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断文件是否存在
	 *
	 * @return
	 */
	@RequestMapping("exist")
	@ResponseBody
	public Object exist(String code) {
		ZSYCFile file = this.fileBiz.get(code);
		if (file == null) return new HashMap<>();
		return file;
	}

	/**
	 * 图片放缩，基于图片中心进行放缩裁剪
	 *
	 * @param code
	 * @param resize
	 * @return
	 */
	@RequestMapping(value = {"{resize}/{code}", "{resize}/{code}.png"}, produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public FileSystemResource image(@PathVariable("code") String code, @PathVariable("resize") ImageSize resize) throws IOException {
		ZSYCFile uploadFile = this.fileBiz.get(code);

		if(uploadFile == null) return null;

		if( resize == ImageSize.origin ){
			return new FileSystemResource(new java.io.File(this.path, uploadFile.getPath()));
		}

		java.io.File file = new java.io.File(this.path, uploadFile.getPath() + "_" + resize.name());
		if (file.exists()) {
			return new FileSystemResource(file);
		}

		BufferedImage bufferedImage = ImageIO.read(new java.io.File(this.path, uploadFile.getPath()));
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		int x1, y1, xWidth, yHeight;

		if (1f * resize.getWidth() / resize.getHeight() > 1f * width / height) {
			yHeight = (int) (1f * width * resize.getHeight() / resize.getWidth());
			xWidth = width;
			x1 = 0;
			y1 = (height - yHeight) / 2;
		} else {
			xWidth = (int) (1f * height * resize.getWidth() / resize.getHeight());
			yHeight = height;
			x1 = (width - xWidth) / 2;
			y1 = 0;
		}

		bufferedImage = ImageUtils.cropImage(bufferedImage, x1, y1, xWidth, yHeight);

		bufferedImage = ImageUtils.scaleImage(bufferedImage, resize.getWidth(), resize.getHeight());

		ImageIO.write(bufferedImage, uploadFile.getSuffix(), file);
		return new FileSystemResource(file);
	}

	public enum ImageSize {
		origin(0,0),small(155, 100), middle(245, 245),;

		private int width, height;

		ImageSize(int w, int h) {
			this.width = w;
			this.height = h;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}
	}
}
