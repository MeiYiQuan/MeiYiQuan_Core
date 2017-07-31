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
 * 类说明：
 */
@Entity
@Table(name = "tb_role")
public class Role extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(unique = true,nullable = false,columnDefinition = ("varchar(20) default '' comment '角色名称'"))
	private String roleName;

	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
