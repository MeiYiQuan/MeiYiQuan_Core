package com.salon.backstage.pub.bsc.dao.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.salon.backstage.core.PO;

/**
 * 作者：温尉棨
 * 创建日期：2017年2月7日
 * 类说明：反馈类别表
 */
@Entity
@Table(name = "tb_suggestion_type")
public class SuggestionType extends PO{
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = 50)
	private String id;
	
	@Column(columnDefinition = ("int(2) default 0 comment '分类类别 1 视频相关2活动相关3软件bug相关4讲师相关5普通用户相关6支付相关7其他' "))
	private Integer genre = 0; 

	@Column(columnDefinition = ("varchar(50) default '' comment '名字'"))
	private String name = ""; 
	
	@Column(columnDefinition = ("int(2) default 1 comment '状态 1启用2禁用' "))
	private Integer status = 1; 
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getGenre() {
		return genre;
	}

	public void setGenre(Integer genre) {
		this.genre = genre;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
