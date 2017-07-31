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
 * 创建日期：2017年1月4日
 * 类说明：推送信息表，本表只有在后台管理系统中才进行增删改，在app接口端只对本表中的内容进行查询
 */
@Entity
@Table(name = "tb_push")
public class Push extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	/**
	 * 标题
	 */
	@Column(columnDefinition = ("varchar(50) default '' comment '标题'"))
	private String title;
	
	/**
	 * 内容
	 */
	@Column(columnDefinition = ("varchar(1500) default '' comment '内容'"))
	private String content;
	
	/**
	 * 推送时携带的map
	 */
	@Column(columnDefinition = ("varchar(1000) default '{}' comment '推送中携带的map，是json的格式'"))
	private String mapjson;
	
	/**
	 * 本推送信息的类型，分为即时推送和后台推送。
	 * 人为推送：2，
	 * 即时推送：1
	 */
	@Column(columnDefinition = ("int(2) default 0 comment '本推送信息的类型，分为即时推送和后台推送。人为推送：2，即时推送：1'"))
	private int type;
	
	/**
	 * 本推送信息是否启用，当不启用时，不能通过后台管理系统人为推送，即时推送也会失效
	 */
	@Column(columnDefinition = ("int(1) default 1 comment '本推送信息是否启用，1--启用，2--不启用，当不启用时，不能通过后台管理系统人为推送，即时推送也会失效'"))
	private int status;

	/**
	 * 创建时间
	 */
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间'") )
	private long createTime;
	
	/**
	 * 修改时间
	 */
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '修改时间'") )
	private long updateTime;

	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMapjson() {
		return mapjson;
	}

	public void setMapjson(String mapjson) {
		this.mapjson = mapjson;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public void setId(String id) {
		this.id = id;
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
