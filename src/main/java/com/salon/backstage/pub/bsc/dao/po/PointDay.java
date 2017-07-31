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
 * 连续签到天数对应当日获得积分表
 */
@Entity
@Table(name = "tb_point_day")
public class PointDay extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("int(2) default 0 comment '连续签到天数'"))
	private Integer day = 0;
	
	@Column(columnDefinition = ("int(3) default 0 comment '连续签到天数对应获得的积分'"))
	private Integer day_point = 10;
	
	@Override
	public Serializable getId() {
		return id;
	}

	@Override
	public String toString() {
		return "PointDay [id=" + id + ", day=" + day + ", day_point=" + day_point + "]";
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getDay_point() {
		return day_point;
	}

	public void setDay_point(Integer day_point) {
		this.day_point = day_point;
	}
	
}
















