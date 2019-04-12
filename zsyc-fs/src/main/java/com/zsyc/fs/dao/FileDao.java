package com.zsyc.fs.dao;

import com.zsyc.fs.entity.ZSYCFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by lcs on 2019-02-01.
 */

@Repository
public interface FileDao extends JpaRepository<ZSYCFile, Long>, JpaSpecificationExecutor<ZSYCFile> {
	ZSYCFile findByCode(String code);
}
