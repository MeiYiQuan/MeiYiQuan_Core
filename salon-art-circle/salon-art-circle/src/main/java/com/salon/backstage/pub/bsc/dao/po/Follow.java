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
 * 关注表
 */
@Entity
@Table(name = "tb_follow")
public class Follow extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '用户ID'"))
	private String user_id = "";
	
	@Column(columnDefinition = ("int(2) default 0 comment '关注的类型(0课程,1课程,2活动,3讲师,4...)'"))
	private int follow_type = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '被关注的ID'"))
	private String follow_type_id = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '关注时间'") )
	private long follow_time = 0L;

	@Override
	public Serializable getId() {
		return id;
	}
	
	public String getStringId(){
		return id;
	}

	@Override
	public String toString() {
		return "Follow [id=" + id + ", user_id=" + user_id + ", follow_type=" + follow_type + ", follow_type_id="
				+ follow_type_id + ", follow_time=" + follow_time + "]";
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getFollow_type() {
		return follow_type;
	}

	public void setFollow_type(int follow_type) {
		this.follow_type = follow_type;
	}

	public String getFollow_type_id() {
		return follow_type_id;
	}

	public void setFollow_type_id(String follow_type_id) {
		this.follow_type_id = follow_type_id;
	}

	public long getFollow_time() {
		return follow_time;
	}

	public void setFollow_time(long follow_time) {
		this.follow_time = follow_time;
	}
}














