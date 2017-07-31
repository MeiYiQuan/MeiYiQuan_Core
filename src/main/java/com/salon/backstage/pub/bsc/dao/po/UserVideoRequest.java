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
 * 活动表。
 * 用于求课程
 */

@Entity
@Table(name = "tb_user_video_request")
public class UserVideoRequest extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '用户ID'") )
	private String user_id = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '发起视频请求的时间'"))
	private Long request_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '反馈时间'"))
	private Long feedback_time = 0L;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '频道ID，这里是指具体频道的id'"))
	private String channel_id = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '频道ID，这里是指顶级频道的id'"))
	private String top_channel_id = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '请求讲师ID'"))
	private String teacher_id = ""; 
	
	@Column(columnDefinition = ("longtext comment '学员困惑'"))
	private String question="";
	
	@Column(columnDefinition = ("int(9) default 0 comment '被请求教程的投票数'"))
	private Integer vote = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '虚拟被请求教程的投票数'"))
	private Integer virtualvote = 0;
	
	@Column(columnDefinition = ("varchar(20) default '' comment '课程名称'"))
	private String course_name = "";
	
	@Column(columnDefinition = ("varchar(1000) default '' comment '客服反馈'"))
	private String feedback = "";
	
	@Column(columnDefinition = ("int(2) default 0 comment '反馈状态，具体见enums里的RequestStatus'"))
	private int feedback_status = 0;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '该课程制作完成时间'"))
	private long createdTime = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '该求课程的封面图片地址'"))
	private String pic_url = "";
	
	@Column(columnDefinition = ("int(2) default 2 comment '是否是平台添加'"))
	private Integer add_type = 2;
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '分享地址'"))
	private String share_url = "";
	
	@Column(columnDefinition = ("int(2) default 0 comment '来源：1首页  2视频3 讲师'"))
	private Integer from_type = 0;
	
	@Column(columnDefinition = ("int(2) default 0 comment '来源：1是 0不是'"))
	private Integer top_type = 0;
	
	@Override
	public Serializable getId() {
		return id;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public Long getRequest_time() {
		return request_time;
	}


	public Integer getVirtualvote() {
		return virtualvote;
	}


	public void setVirtualvote(Integer virtualvote) {
		this.virtualvote = virtualvote;
	}


	public void setRequest_time(Long request_time) {
		this.request_time = request_time;
	}


	public Long getFeedback_time() {
		return feedback_time;
	}


	public void setFeedback_time(Long feedback_time) {
		this.feedback_time = feedback_time;
	}


	public Integer getTop_type() {
		return top_type;
	}


	public void setTop_type(Integer top_type) {
		this.top_type = top_type;
	}


	public String getChannel_id() {
		return channel_id;
	}


	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}


	public String getTeacher_id() {
		return teacher_id;
	}


	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}


	public Integer getAdd_type() {
		return add_type;
	}


	public void setAdd_type(Integer add_type) {
		this.add_type = add_type;
	}


	public String getPic_url() {
		return pic_url;
	}


	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}


	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}


	public Integer getVote() {
		return vote;
	}


	public void setVote(Integer vote) {
		this.vote = vote;
	}


	public String getCourse_name() {
		return course_name;
	}


	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}


	public String getFeedback() {
		return feedback;
	}


	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}


	public int getFeedback_status() {
		return feedback_status;
	}


	public void setFeedback_status(int feedback_status) {
		this.feedback_status = feedback_status;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getTop_channel_id() {
		return top_channel_id;
	}


	public void setTop_channel_id(String top_channel_id) {
		this.top_channel_id = top_channel_id;
	}

	public long getCreatedTime() {
		return createdTime;
	}


	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}


	@Override
	public String toString() {
		return "UserVideoRequest [id=" + id + ", user_id=" + user_id
				+ ", request_time=" + request_time + ", feedback_time="
				+ feedback_time + ", channel_id=" + channel_id
				+ ", teacher_id=" + teacher_id + ", question=" + question
				+ ", vote=" + vote + ", course_name=" + course_name
				+ ", feedback=" + feedback + ", feedback_status="
				+ feedback_status + "]";
	}


	public String getShare_url() {
		return share_url;
	}


	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}


	public Integer getFrom_type() {
		return from_type;
	}


	public void setFrom_type(Integer from_type) {
		this.from_type = from_type;
	}
	
	
}







