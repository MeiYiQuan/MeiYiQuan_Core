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
 * 精彩专题表
 */

@Entity
@Table(name = "tb_subject")
public class Subject extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '创建人ID'"))
	private String create_admin_id = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间'") )
	private long create_time = 0L;

	@Column(columnDefinition = ("int(1) default 0 comment '宣传封面类型 0图片,1视频'"))
	private int cover_type = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '宣传封面(图片)访问地址'"))
	private String cover_pic_url = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '宣传封面视频访问地址(封面为图片时没有)'"))
	private String cover_video_url = "";
	
	@Column(columnDefinition = ("varchar(20) default '' comment '名称'"))
	private String name = "";
	
	@Column(columnDefinition = ("varchar(50) default '' comment '简介'"))
	private String introduction = "";
	
	@Column(columnDefinition = ("varchar(500) default '' comment '详细信息'"))
	private String remark = "";
	
	@Column(columnDefinition = ("int(1) default 1 comment '2禁用,1启用'"))
	private int status = 1;
	
	@Column(columnDefinition = ("int(8) default 0 comment '排列顺序'"))
	private int sort_num = 0;
	
	@Override
	public Serializable getId() {
		return id;
	}
	
	public String getStringId(){
		return id;
	}

	@Override
	public String toString() {
		return "Subject [id=" + id + ", create_admin_id=" + create_admin_id + ", create_time=" + create_time
				+ ", cover_type=" + cover_type + ", cover_pic_url=" + cover_pic_url + ", cover_video_url="
				+ cover_video_url + ", name=" + name + ", introduction=" + introduction + ", remark=" + remark
				+ ", status=" + status + "]";
	}

	public String getCover_pic_url() {
		return cover_pic_url;
	}

	public void setCover_pic_url(String cover_pic_url) {
		this.cover_pic_url = cover_pic_url;
	}

	public String getCover_video_url() {
		return cover_video_url;
	}

	public void setCover_video_url(String cover_video_url) {
		this.cover_video_url = cover_video_url;
	}

	public String getCreate_admin_id() {
		return create_admin_id;
	}

	public void setCreate_admin_id(String create_admin_id) {
		this.create_admin_id = create_admin_id;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public int getCover_type() {
		return cover_type;
	}

	public void setCover_type(int cover_type) {
		this.cover_type = cover_type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}



