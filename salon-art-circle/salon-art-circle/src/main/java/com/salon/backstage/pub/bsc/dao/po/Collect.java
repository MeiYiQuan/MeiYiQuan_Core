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
 * 收藏表 
 */
@Entity
@Table(name = "tb_collect")
public class Collect extends PO{
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '用户ID'"))
	private String user_id = "";
	
	@Column(columnDefinition = ("int(2) default 0 comment '被关注的类型(1活动,2讲师,3视频)'"))
	private int collect_type = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '被关注的ID'"))
	private String collect_type_id = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '收藏时间'") )
	private long collect_time = 0L;

	@Override
	public Serializable getId() {
		return id;
	}
	
	public String getStringId(){
		return id;
	}

	@Override
	public String toString() {
		return "Collect [id=" + id + ", user_id=" + user_id + ", collect_type=" + collect_type + ", collect_type_id="
				+ collect_type_id + ", collect_time=" + collect_time + "]";
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getCollect_type() {
		return collect_type;
	}

	public void setCollect_type(int collect_type) {
		this.collect_type = collect_type;
	}

	public String getCollect_type_id() {
		return collect_type_id;
	}

	public void setCollect_type_id(String collect_type_id) {
		this.collect_type_id = collect_type_id;
	}

	public long getCollect_time() {
		return collect_time;
	}

	public void setCollect_time(long collect_time) {
		this.collect_time = collect_time;
	}
	
}




















