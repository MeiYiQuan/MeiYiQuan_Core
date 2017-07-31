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
 * 各途径可获取积分数量上限表
 */
@Entity
@Table(name = "tb_point_way_most")
public class PointMost extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(50) default '' comment '获取积分途径名称'"))
	private String way_name = "";
	
	@Column(columnDefinition = ("int(2) default 0 comment '获取积分途径类型，这个类型在项目静态量里有专门的对应关系'"))
	private Integer way_type = 0;
	
	@Column(columnDefinition = ("int(4) default 0 comment '单日获取积分的上限'"))
	private Integer way_most = 0;
	
	@Column(columnDefinition = ("int(4) default 0 comment '单次获得积分数量'"))
	private Integer way_single = 0;
	
	@Column(columnDefinition = ("varchar(500) default '' comment '途径的说明'"))
	private String remark = "";
	
	@Override
	public Serializable getId() {
		return id;
	}
	
	
	
	public Integer getWay_type() {
		return way_type;
	}



	public void setWay_type(Integer way_type) {
		this.way_type = way_type;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getWay_name() {
		return way_name;
	}
	
	public void setWay_name(String way_name) {
		this.way_name = way_name;
	}

	public Integer getWay_most() {
		return way_most;
	}

	public void setWay_most(Integer way_most) {
		this.way_most = way_most;
	}

	public Integer getWay_single() {
		return way_single;
	}

	public void setWay_single(Integer way_single) {
		this.way_single = way_single;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}



















