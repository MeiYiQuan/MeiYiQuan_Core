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
 * 活动用户关系表
 */
@Entity
@Table(name = "tb_activity_user_relationship")
public class ActivityUser extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '用户ID'"))
	private String user_id = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '活动ID'"))
	private String activity_id = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '报名时间'") )
	private Long enter_time = 0L;
	
	@Column(columnDefinition = ("int(2) default 0 comment '参与者类型,普通用户和讲师的状态码直接去拿user里的码即可(0普通 1讲师 2嘉宾)'"))
	private Integer man_type = 0;
	
	@Column(columnDefinition = ("int(1) default 0 comment '状态 (2 不可用,1可用),此字段已废除，不再使用'"))
	private Integer status = 0;

	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '讲师嘉宾出场费(单位:元)，当时普通用户的话，表示用户参加这个活动所花的钱(除去优惠券以后的)'"))
	private double appearance = 0;
	
	@Override
	public Serializable getId() {
		return id;
	}
	
	public double getAppearance() {
		return appearance;
	}



	public void setAppearance(double appearance) {
		this.appearance = appearance;
	}



	@Override
	public String toString() {
		return "ActivityUser [id=" + id + ", user_id=" + user_id + ", activity_id=" + activity_id + ", enter_time="
				+ enter_time + ", man_type=" + man_type + ", status=" + status + "]";
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}

	public Long getEnter_time() {
		return enter_time;
	}

	public void setEnter_time(Long enter_time) {
		this.enter_time = enter_time;
	}

	public Integer getMan_type() {
		return man_type;
	}

	public void setMan_type(Integer man_type) {
		this.man_type = man_type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}





















