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
 * 类说明：职业表
 */
@Entity
@Table(name = "tb_job")
public class Job extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(50) default '' comment '职业'"))
	private String job_name = ""; 
	
	@Column(columnDefinition = ("int(8) default 0 comment '排序规则，前段展示从小到大排序'"))
	private int orderInt;
	
	/**
	 * 创建时间
	 */
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间'") )
	private Long create_time = 0L;
	
	/**
	 * 修改时间
	 */
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '修改时间'"))
	private Long update_time = 0L;
	
	/**
	 * 公共参数类型
	 */
	@Column(columnDefinition = ("int(2) default 0 comment '类型 ：1职业 2讲师等级3年龄'"))
	private Integer type;
	
	@Override
	public Serializable getId() {
		return id;
	}
	@Override
	public String toString() {
		return "Job [id=" + id + ", job_name=" + job_name + "]";
	}
	public String getJob_name() {
		return job_name;
	}
	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}
	public void setId(String id) {
		this.id = id;
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
	public int getOrderInt() {
		return orderInt;
	}
	public void setOrderInt(int orderInt) {
		this.orderInt = orderInt;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
