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
 * 反馈表
 */

@Entity
@Table(name = "tb_suggestion")
public class Suggestion extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = 50)
	private String id;
	
	@Column(columnDefinition = ("varchar(50) default '' comment '用户ID'"))
	private String user_id = ""; 
	
	@Column(columnDefinition = ("varchar(1000) default '' comment '内容'"))
	private String content = ""; 
	
	@Column(nullable = false,columnDefinition = ("bigint(15)  default 0 comment '手机号'"))
	private Long phone_num;
	
	@Column(columnDefinition = ("int(2) default 0 comment '分类类别 1 视频相关2活动相关3软件bug相关4讲师相关5普通用户相关6支付相关7其他' "))
	private Integer genre = 0; 
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '反馈时间'") )
	private Long back_time = 0L;

	@Column(columnDefinition = ("int(1) default 0 comment '状态 0未处理 1已处理'"))
	private Integer status = 0;
	
	@Column(columnDefinition = ("varchar(50) default '' comment '回复消息ID'"))
	private String recount_id = ""; 
	
	@Override
	public Serializable getId() {
		return id ;
	}
	public String getUser_id() {
		return user_id;
	}
	
	public String getRecount_id() {
		return recount_id;
	}
	public void setRecount_id(String recount_id) {
		this.recount_id = recount_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getPhone_num() {
		return phone_num;
	}
	public void setPhone_num(Long phone_num) {
		this.phone_num = phone_num;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Integer getGenre() {
		return genre;
	}
	public void setGenre(Integer genre) {
		this.genre = genre;
	}
	public Long getBack_time() {
		return back_time;
	}
	public void setBack_time(Long back_time) {
		this.back_time = back_time;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Suggestion [id=" + id + ", user_id=" + user_id + ", content="
				+ content + ", phone_num=" + phone_num + "]";
	}
}







