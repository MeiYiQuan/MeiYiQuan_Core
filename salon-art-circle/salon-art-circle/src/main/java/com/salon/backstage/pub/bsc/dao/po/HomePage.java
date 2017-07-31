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
 * 作者：温尉棨
 * 创建日期：2017年1月4日
 * 类说明：
 */
@Entity
@Table(name = "tb_homepage")
public class HomePage extends PO{
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("int(2) default 0 comment '推荐类型'"))
	private Integer type = null;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '关联id'"))
	private String relation_id = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '首页图片'"))
	private String pic_url = "";
	
	@Column(columnDefinition = ("int(1) default 1 comment '首页启用（1是/2否）'"))
	private Integer status = null;
	
	@Column(columnDefinition = ("int(8) default 0 comment '置顶数值'"))
	private Integer top_num = null;
	
	@Column(columnDefinition = ("varchar(50) default '' comment '首页名称'"))
	private String name = "";
	
	/**
	 * 创建时间
	 */
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '添加时间'"))
	private long creatTime = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '最后修改时间'"))
	private long updateTime = 0L;
	
	@Override
	public Serializable getId() {
		return id;
	}
	public String getStringId() {
		return id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getRelation_id() {
		return relation_id;
	}
	public void setRelation_id(String relation_id) {
		this.relation_id = relation_id;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getTop_num() {
		return top_num;
	}
	@Override
	public String toString() {
		return "HomePage [id=" + id + ", type=" + type + ", relation_id=" + relation_id + ", pic_url=" + pic_url
				+ ", status=" + status + ", top_num=" + top_num + ", name=" + name + "]";
	}
	public void setTop_num(Integer top_num) {
		this.top_num = top_num;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(long creatTime) {
		this.creatTime = creatTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

}
