package com.zsyc.fs.service;

import com.zsyc.fs.dao.FileDao;
import com.zsyc.fs.entity.ZSYCFile;
import com.zsyc.fs.util.FileUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lcs on 2019-02-01.
 */
@Service
@Transactional
public class FileService {
	@Autowired
	private FileDao fileDao;

	public ZSYCFile add(ZSYCFile file) {
		this.fileDao.save(file);
		return file;
	}

	public ZSYCFile addFile(MultipartFile uploadFile, String relatePath){
		String code = FileUtil.getFileCode(uploadFile);
		ZSYCFile uploadedFile = fileDao.findByCode(code);

		if (uploadedFile != null){
			return uploadedFile;
		}

		ZSYCFile file = new ZSYCFile();
		file.setName(uploadFile.getOriginalFilename());
		file.setSuffix(FileUtil.getSuffix(file.getName()));
		file.setCode(FileUtil.getFileCode(uploadFile));
		String path = FileUtil.getPath(file.getCode());
		file.setPath(path + "/" + file.getCode());
		file.setContentType(uploadFile.getContentType());
		FileUtil.upload(uploadFile, file.getCode(), path, relatePath);
		fileDao.save(file);
		return file;
	}


	@Transactional(readOnly = true)
	public ZSYCFile get(Long fileId){
		return fileDao.getOne(fileId);
	}

	@Transactional(readOnly = true)
	public ZSYCFile  get(String code ){
		return fileDao.findByCode(code);
	}

	@Transactional(readOnly = true)
	public List<ZSYCFile> getFiles(String[] ids){
		if(ids == null) return Collections.emptyList();
		if(ids.length == 0) return Collections.emptyList();

		return fileDao.findAllById(Arrays.stream(ids).map(Long::valueOf).collect(Collectors.toSet()));
	}
}
