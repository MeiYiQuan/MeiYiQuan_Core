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
 * 频道表
 * @author CXY
 *
 */

@Entity
@Table(name = "tb_channel")
public class Channel extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(20) default '' comment '名称'"))
	private String name = "";

	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment 'PID'"))
	private String pid = "";
	
	@Column(columnDefinition = ("int(1) default 1 comment '是否启用(2不启用,1 启用)'"))
	private int enable = 1;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间'"))
	private long create_time = 0L;;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '修改时间'"))
	private long update_time = 0L;;

	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '修改人ID'"))
	private String update_admin_id = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment 'logo图片的访问地址'"))
	private String logo_url = "";
	
	@Override
	public Serializable getId() {
		return id;
	}
	
	public String getStringId(){
		return id;
	}
	
	
	@Override
	public String toString() {
		return "Channel [id=" + id + ", name=" + name + ", pid=" + pid + ", enable=" + enable + ", create_time="
				+ create_time + ", update_time=" + update_time + ", update_admin_id=" + update_admin_id + ", logo_url="
				+ logo_url + "]";
	}

	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}

	public String getUpdate_admin_id() {
		return update_admin_id;
	}

	public void setUpdate_admin_id(String update_admin_id) {
		this.update_admin_id = update_admin_id;
	}

	
	
}












