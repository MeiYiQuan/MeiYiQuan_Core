package com.salon.backstage.pub.bsc.dao.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.salon.backstage.core.PO;
import com.salon.backstage.qcproject.util.Mysql;

/**
 * 作者：齐潮
 * 创建日期：2016年12月20日
 * 类说明：存储一些系统设置
 */
@Entity
@Table(name = "tb_system")
public class Sys extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	/**
	 * 存储的key
	 */
	@Column(columnDefinition = ("varchar(50) default '' comment '存储的key'"))
	private String sys_key;
	
	/**
	 * 存储的value
	 */
	@Column(columnDefinition = ("varchar(500) default '' comment '存储的value'"))
	private String sys_value;
	
	/**
	 * 标题
	 */
	@Column(columnDefinition = ("varchar(500) default '' comment '标题'"))
	private String title;
	
	/**
	 * 类型
	 */
	@Column(columnDefinition = ("int(2) default 0 comment '类型，具体见静态量'"))
	private int type;
	
	/**
	 * 是否启用
	 */
	@Column(columnDefinition = ("int(1) default 1 comment '是否启用，1--启用，2--不启用'"))
	private int status;

	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getSys_key() {
		return sys_key;
	}

	public void setSys_key(String sys_key) {
		this.sys_key = sys_key;
	}

	public String getSys_value() {
		return sys_value;
	}

	public void setSys_value(String sys_value) {
		this.sys_value = sys_value;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
