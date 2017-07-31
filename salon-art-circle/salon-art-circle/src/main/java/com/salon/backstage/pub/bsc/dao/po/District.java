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
 * 地区表
 */
@Entity
@Table(name = "tb_district")
public class District extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(20) default '' comment '名称'"))
	private String name = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment 'PID'"))
	private String pid = "";
	
	@Column(columnDefinition = ("int(1) default 0 comment '区域等级(1省,2市,3县)'"))
	private Integer district_grade = null;
	
	@Column(columnDefinition = ("int(1) default 1 comment '是否启用(2禁用,1启用)'"))
	private Integer status = 1;

	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间'") )
	private Long create_time = 19710101000000L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '修改时间'"))
	private Long update_time = 19710101000000L;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '修改人ID'"))
	private String update_admin_id = "";

	@Override
	public Serializable getId() {
		return id;
	}

	@Override
	public String toString() {
		return "District [id=" + id + ", name=" + name + ", pid=" + pid + ", district_grade=" + district_grade
				+ ", status=" + status + ", create_time=" + create_time + ", update_time=" + update_time
				+ ", update_admin_id=" + update_admin_id + "]";
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

	public Integer getDistrict_grade() {
		return district_grade;
	}

	public void setDistrict_grade(Integer district_grade) {
		this.district_grade = district_grade;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	
	public Long getUpdate_time() {
		return update_time;
	}
	
	public void setUpdate_time(Long update_time) {
		this.update_time = update_time;
	}
	
	public String getUpdate_admin_id() {
		return update_admin_id;
	}
	
	public void setUpdate_admin_id(String update_admin_id) {
		this.update_admin_id = update_admin_id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
