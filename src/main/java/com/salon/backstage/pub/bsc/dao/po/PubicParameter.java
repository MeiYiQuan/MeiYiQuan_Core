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
 * 创建日期：2017年2月12日
 * 类说明：
 */
@Entity
@Table(name = "tb_public")
public class PubicParameter  extends PO {
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = 32)
	private String id;
	
	@Column(columnDefinition = ("varchar(32) default '' comment '参数名称'"))
	private String parameter_name = ""; 
	
	@Column(columnDefinition = ("int(3) default 0 comment '参数编号'"))
	private Integer parameter_num = 0;
	
	@Column(columnDefinition = ("int(1) default 0 comment '类型1文字0图片'"))
	private Integer type = 0;
	
	@Column(columnDefinition = ("longtext comment '图片url'"))
	private String pic_url = ""; 
	
	@Column(columnDefinition = ("varchar(100) default '' comment '参数内容'"))
	private String content = ""; 

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParameter_name() {
		return parameter_name;
	}

	public void setParameter_name(String parameter_name) {
		this.parameter_name = parameter_name;
	}
	

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getParameter_num() {
		return parameter_num;
	}

	public void setParameter_num(Integer parameter_num) {
		this.parameter_num = parameter_num;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "PubicParameter [id=" + id + ", parameter_name=" + parameter_name + ", parameter_num=" + parameter_num
				+ ", type=" + type + "]";
	}
	
	
}
