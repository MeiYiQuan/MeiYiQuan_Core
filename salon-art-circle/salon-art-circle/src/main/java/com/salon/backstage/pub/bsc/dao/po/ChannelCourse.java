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
 * 课程和频道关系表
 * @author CXY
 *`本表作废：2017-01-16
 */

@Entity
@Table(name = "tb_channel_course")
public class ChannelCourse extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '课程ID'"))
	private String course_id = "";

	@Column(columnDefinition = ("int(1) default 0 comment '所属频道的类型(0 开店创业1 学潮流技术)'"))
	private int belong_type = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '频道ID'"))
	private String channel_id = "";
	
	@Column(columnDefinition = ("int(1) default 1 comment '是否显示到首页开店创业或潮流技术(2 不显示,1 显示)'"))
	private int homepage_show = 1;

	@Override
	public Serializable getId() {
		return id;
	}

	@Override
	public String toString() {
		return "CourseChannel [id=" + id + ", course_id=" + course_id + ", belong_type=" + belong_type + ", channel_id="
				+ channel_id + ", homepage_show=" + homepage_show + "]";
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getCourse_id() {
		return course_id;
	}
	
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public int getBelong_type() {
		return belong_type;
	}

	public void setBelong_type(int belong_type) {
		this.belong_type = belong_type;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public int getHomepage_show() {
		return homepage_show;
	}

	public void setHomepage_show(int homepage_show) {
		this.homepage_show = homepage_show;
	}

}











