package com.zsyc.fs.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.MessageDigest;

/**
 * 文件系统相关
 * @author lcs
 *
 */
public class FileUtil {
	
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * 计算文件指纹的算法
	 */
	private static final String ALGORITHM = "SHA-1";

	/**
	 * 保存上传的文件
	 * @param file 文件
	 * @param name 存放的文件名
	 * @param path 16进制的相对路径
	 * @param diskPath 应用根路径
	 */
	public static void upload(MultipartFile file, String name, String path, String diskPath){
		StringBuffer uploadPath = new StringBuffer(diskPath);
		uploadPath.append("/").append(path);
		File disk = new File(uploadPath.toString());
		if(!disk.exists()){
			disk.mkdirs();
		}
		uploadPath.append("/").append(name);
		File toFile =new File(uploadPath.toString());
		if (!toFile.exists()){
			try {
				file.transferTo(toFile);
			} catch (IOException e) {
				throw new RuntimeException("文件写入失败");
			}

		}
	}

	/**
	 * 输出指定文件
	 * @param outputStream
	 * @param realPath
	 */
	public static void download(OutputStream outputStream, String realPath){
		File file = new File(realPath.toString());
		if(file.exists()){
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
				byte[] buffer = new byte[1024];
				int bytesRead = -1;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				outputStream.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					inputStream.close();
				}catch (IOException ex) {}
				try {
					outputStream.close();
				}catch (IOException ex) {
				}
			}
		}
	};

	/**
	 * 获取文件名的后缀
	 * @param fileName
	 * @return
	 */
	public static String getSuffix(String fileName){
		int point = fileName.lastIndexOf(".");
		if(point == -1){
			return "";
		}
		return fileName.substring(point + 1);
	}

	/**
	 * 获取文件存放的路径
	 * @param fingerprint
	 * @return
	 */
	public static String getPath(String fingerprint){
		StringBuffer path = new StringBuffer("/");
		path.append(fingerprint.substring(0, 3));
		return path.toString();
	}

	/**
	 * 计算文件指纹
	 * <p>16-11-28 上午10:57</p>
	 * @author raymond
	 * @param path
	 * @return
	 */
	public static String getFileCode(String path){
		if(StringUtils.isBlank(path)){
			return null;
		}
		
		return FileUtil.getFileCode( new File(path) );
	}

	/**
	 * 计算文件指纹
	 * <p>16-11-28 上午10:57</p>
	 * @author raymond
	 * @param file
	 * @return
	 */
	public static String getFileCode(File file){
		if(file == null){
			return null;
		}

		try {
			return FileUtil.getFileCode(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * 计算文件指纹
	 * <p>16-11-28 上午10:54</p>
	 * @author raymond
	 * @param file
	 * @return
	 */
	public static String getFileCode(MultipartFile file){
		if(file == null){
			return null;
		}
		
		try {
			return FileUtil.getFileCode(file.getInputStream());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 计算文件指纹
	 * <p>16-11-28 上午10:54</p>
	 * @author raymond
	 * @param in
	 * @return
	 */
	public static String getFileCode(InputStream in){
		if(in == null){
			return null;
		}
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance(FileUtil.ALGORITHM);
			byte[] buffer = new byte[1024 * 1024 * 10];
			int len = 0;

			while ((len = in.read(buffer)) > 0) {
				messageDigest.update(buffer, 0, len);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					logger.error("流关闭出错", e);
				}
			}
		}
		return messageDigest == null ? null : byte2hex(messageDigest.digest());
	}

	/**
	 * 将二进制数组转换成16进制（大写）
	 * @param array
	 * @return
	 */
	public static String byte2hex(byte[] array) {
		if (array == null || array.length == 0) {
			return null;
		}

		StringBuffer stringBuffer = new StringBuffer();
		for (byte num : array) {
			String temp = Integer.toHexString(num & 0xFF);
			if (temp.length() < 2) {
				stringBuffer.append(0);
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString().toUpperCase();
	}
}
