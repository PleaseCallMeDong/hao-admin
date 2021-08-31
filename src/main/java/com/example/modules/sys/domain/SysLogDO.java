package com.example.modules.sys.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志
 *
 * @author dj
 */
@Data
@Document("sys_log")
public class SysLogDO implements Serializable {

	@Id
	@JSONField(name = "_id")
	private String id;

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 用户操作
	 */
	private String operation;

	/**
	 * 请求方法
	 */
	private String method;

	/**
	 * 请求参数
	 */
	private String params;

	/**
	 * 执行时长(毫秒)
	 */
	private Long runTime;

	/**
	 * IP地址
	 */
	private String ip;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createDate;

	public SysLogDO() {
		this.createDate = new Date();
	}

}
