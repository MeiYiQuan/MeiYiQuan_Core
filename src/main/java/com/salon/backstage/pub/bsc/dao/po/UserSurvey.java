package com.salon.backstage.pub.bsc.dao.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.salon.backstage.core.PO;

/**
 * 用户调查表
 */
@Entity
@Table(name = "tb_user_survey")
public class UserSurvey extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = 50)
	private String id;
	@Column(columnDefinition = ("varchar(50) default '' comment '用户名'"))
	private String userId = "";
	@Column(columnDefinition = ("varchar(50) default '' comment '管理员名册'"))
	private String adminId = "";
	@Column(columnDefinition = ("bigint(15) default 0 comment '创建时间'"))
	private Long create_time = 0L;
	@Column(columnDefinition = ("varchar(500) default '' comment '备注'"))
	private String remark = "";
	@Override
	public Serializable getId() {
		return id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
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
