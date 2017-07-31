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
 * 课程相关推荐表
 */

@Entity
@Table(name = "tb_course_recommend")
public class CourseRecommend extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '本位课程ID'"))
	private String course_id = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '相关推荐ID'"))
	private String recommend_course_id = "";

	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '关联时间'") )
	private Long recommend_time = 0L;

	@Override
	public String toString() {
		return "CourseRecommend [id=" + id + ", course_id=" + course_id + ", recommend_course_id=" + recommend_course_id
				+ ", recommend_time=" + recommend_time + "]";
	}

	@Override
	public Serializable getId() {
		return id;
	}

	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public String getRecommend_course_id() {
		return recommend_course_id;
	}

	public void setRecommend_course_id(String recommend_course_id) {
		this.recommend_course_id = recommend_course_id;
	}

	public Long getRecommend_time() {
		return recommend_time;
	}

	public void setRecommend_time(Long recommend_time) {
		this.recommend_time = recommend_time;
	}

	public void setId(String id) {
		this.id = id;
	}

	
}













