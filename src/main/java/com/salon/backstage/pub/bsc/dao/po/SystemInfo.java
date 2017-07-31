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
 * 系统消息表
 */
@Entity
@Table(name = "tb_system_information")
public class SystemInfo extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '系统头像访问地址'"))
	private String system_pic_url = "";
	
	@Column(columnDefinition = ("varchar(50) default '' comment '标题'"))
	private String title = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '系统消息推送消息时间'") )
	private Long get_info_time = 0L;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '用户ID'"))
	private String user_id = "";
	
	@Column(columnDefinition = ("varchar(5000) default '' comment '系统消息内容'"))
	private String content = "";
	
	@Column(columnDefinition = ("int(1) default 2 comment '是否已经被阅读(2未读,1已读)'"))
	private Integer status = 2;
	
	@Column(columnDefinition = ("varchar(1000) default '{}' comment '推送中携带的map，是json的格式'"))
	private String mapjson = "";
	
	@Column(columnDefinition = ("int(2) default 0 comment '推送类型，分为即时推送和后台人为推送，即时推送还分为两种，以需不需要查询数据库为准'"))
	private int type = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '本推送信息的id。后台人为推送时是通过后台来选取相应的pushId，即时推送时是通过静态量里已经设置好的几个固定的id来推送的'"))
	private String pushId = "";
	
	@Override
	public Serializable getId() {
		return id;
	}

	@Override
	public String toString() {
		return "SystemInfo [id=" + id + ", system_pic_url=" + system_pic_url + ", title=" + title + ", get_info_time="
				+ get_info_time + ", user_id=" + user_id + ", content=" + content + ", status=" + status + "]";
	}

	public String getPushId() {
		return pushId;
	}

	public void setPushId(String pushId) {
		this.pushId = pushId;
	}

	public String getMapjson() {
		return mapjson;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setMapjson(String mapjson) {
		this.mapjson = mapjson;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSystem_pic_url() {
		return system_pic_url;
	}

	public void setSystem_pic_url(String system_pic_url) {
		this.system_pic_url = system_pic_url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getGet_info_time() {
		return get_info_time;
	}

	public void setGet_info_time(Long get_info_time) {
		this.get_info_time = get_info_time;
	}

	public String getUser_id() {
		return user_id;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}









