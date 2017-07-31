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
 * 作者：齐潮
 * 创建日期：2016年12月7日
 * 类说明：管理员表
 */
@Entity
@Table(name = "tb_admin")
public class Admin extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(unique = true,nullable = false,columnDefinition = ("varchar(20) default '' comment '账号'"))
	private String loginname;
	
	@Column(unique = false,nullable = false,columnDefinition = ("varchar(20) default '' comment '密码'"))
	private String password;
	
	@Column(unique = false,nullable = true,columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '最后更新时间，一般为修改密码'"))
	private long updateTime;
	
	@Column(unique = false,nullable = false,columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '头像图片地址'"))
	private String headUrl;
	
	@Column(unique = false,nullable = false,columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '角色id'"))
	private String roleId;
	
	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setId(String id) {
		this.id = id;
	}

}
