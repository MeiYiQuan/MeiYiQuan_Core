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
 * 课程表
 * @author CXY
 *
 */
@Entity
@Table(name="tb_course")
public class Course extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(50) default '' comment '标题'"))
	private String title = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间'"))
	private Long create_time= 0L;

	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '课程上映时间'"))
	private Long playing_time= 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '修改时间'"))
	private Long update_time= 0L;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '创建人ID'"))
	private String creater_id = "";
	
	@Column(columnDefinition = ("varchar(500) default '' comment '描述'"))
	private String remark = "";
	
	@Column(columnDefinition = ("int(1) default 0 comment '所属频道分类 0不放入频道,1 创业开店,2潮流技术'"))
	private int belong_type = 0;
	
	@Column(columnDefinition = ("int(1) default 1 comment '是否显示到首页开店创业或潮流技术(2不显示,1 显示)'"))
	private int homepage_show = 1;
	
	@Column(columnDefinition = ("int(1) default 1 comment '是否在首页开店创业或潮流技术显示大图(2不显示,1 显示)，作废'"))
	private int show_big = 1;
	
	@Column(columnDefinition = ("int(1) default 1 comment '是否推荐到首页(2否,1 是)'"))
	private int to_home = 1;
	
	@Column(columnDefinition = ("int(1) default 1 comment '状态(2 禁用,1 启用)'"))
	private int status = 1;
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '课程宣传大图'"))
	private String pic_big_url = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '课程宣传小图'"))
	private String pic_small_url = "";
	
	@Column(columnDefinition = ("varchar(50) default '' comment '视频简要介绍'"))
	private String description = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '讲师ID'"))
	private String teacher_id = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '相关推荐视频ID，已作废'"))
	private String video_id = "";
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '总成本'"))
	private double cost = 0;
	
	@Column(columnDefinition = ("int(1) default 0 comment '课程类型(0：平台制作-即将上映，1：平台制作-已上映，2：由求课程转变而来-已经上映)'"))
	private int playing = 0;
	
	@Column(columnDefinition = ("int(1) default 0 comment '课程宣传(课程详情页)封面类型(0图片,1视频)'"))
	private int course_compaign_type = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '课程宣传(课程详情页)视频访问地址'"))
	private String course_compaign_video_url = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '-1' comment '视频对应的频道ID(不放入频道此处对应-1)'"))
	private String channel_id = "-1";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '对应的专题ID'"))
	private String subject_id = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '分享地址'"))
	private String share_url = "";
	
	@Override
	public Serializable getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", title=" + title + ", create_time=" + create_time + ", playing_time="
				+ playing_time + ", update_time=" + update_time + ", creater_id=" + creater_id + ", remark=" + remark
				+ ", belong_type=" + belong_type + ", homepage_show=" + homepage_show + ", show_big=" + show_big
				+ ", to_home=" + to_home + ", status=" + status + ", pic_big_url=" + pic_big_url + ", pic_small_url="
				+ pic_small_url + ", description=" + description + ", teacher_id=" + teacher_id + ", video_id="
				+ video_id + ", cost=" + cost + ", playing=" + playing + ", course_compaign_type="
				+ course_compaign_type + ", course_compaign_video_url=" + course_compaign_video_url + ", channel_id="
				+ channel_id + ", subject_id=" + subject_id + "]";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public Long getPlaying_time() {
		return playing_time;
	}

	public void setPlaying_time(Long playing_time) {
		this.playing_time = playing_time;
	}

	public Long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Long update_time) {
		this.update_time = update_time;
	}

	public String getCreater_id() {
		return creater_id;
	}

	public void setCreater_id(String creater_id) {
		this.creater_id = creater_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getBelong_type() {
		return belong_type;
	}

	public void setBelong_type(int belong_type) {
		this.belong_type = belong_type;
	}

	public int getHomepage_show() {
		return homepage_show;
	}

	public void setHomepage_show(int homepage_show) {
		this.homepage_show = homepage_show;
	}

	public int getShow_big() {
		return show_big;
	}

	public void setShow_big(int show_big) {
		this.show_big = show_big;
	}

	public int getTo_home() {
		return to_home;
	}

	public void setTo_home(int to_home) {
		this.to_home = to_home;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPic_big_url() {
		return pic_big_url;
	}

	public void setPic_big_url(String pic_big_url) {
		this.pic_big_url = pic_big_url;
	}

	public String getPic_small_url() {
		return pic_small_url;
	}

	public void setPic_small_url(String pic_small_url) {
		this.pic_small_url = pic_small_url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}

	public String getVideo_id() {
		return video_id;
	}

	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}


	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getPlaying() {
		return playing;
	}

	public void setPlaying(int playing) {
		this.playing = playing;
	}

	public int getCourse_compaign_type() {
		return course_compaign_type;
	}

	public void setCourse_compaign_type(int course_compaign_type) {
		this.course_compaign_type = course_compaign_type;
	}

	public String getCourse_compaign_video_url() {
		return course_compaign_video_url;
	}

	public void setCourse_compaign_video_url(String course_compaign_video_url) {
		this.course_compaign_video_url = course_compaign_video_url;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}















