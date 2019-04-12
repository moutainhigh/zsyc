package com.zsyc.fs.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lcs on 2019-02-01.
 */
@Entity
@Table(name = "FS_FILE")
@Data
public class ZSYCFile implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "SUFFIX")
	private String suffix;

	@Column(name = "CODE")
	private String code;

	@Column(name = "PATH")
	private String path;

	@Column(name = "CONTENT_TYPE")
	private String contentType;

	@Column(name = "CREATE_TIME")
	private Date createTime;
}
