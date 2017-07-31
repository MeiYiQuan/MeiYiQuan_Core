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
 * 视频表
 */

@Entity
@Table(name = "tb_video")
public class Video extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(50) default '' comment '标题'"))
	private String title = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间'") )
	private long create_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '上映时间'") )
	private long play_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '修改时间'"))
	private long update_time = 0L;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '创建人ID'"))
	private String creater_id = "";

	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '关联课程详情ID'"))
	private String course_id = "";
	
	@Column(columnDefinition = ("int(9) default 0 comment '时长(单位:秒)'"))
	private int time_long = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '视频存储访问路径'"))
	private String video_save_url = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '课程详情页封面图片访问地址(每个视频对应各自的一个)'"))
	private String video_pic_url = "";
	
	@Column(columnDefinition = ("varchar(1000) default '' comment '视频的详细介绍，作废'"))
	private String remark = "";
	
	@Column(columnDefinition = ("int(1) default 0 comment '是否启用(0 未上映1 即将上映2 过期的 )，已经作废'"))
	private int status = 0;
	
	@Column(columnDefinition = ("int(8) default 0 comment '视频排序(0放入课程详情页面顶部)，从大到小排序'"))
	private int order_num = 0;
	
	@Column(columnDefinition = ("int(1) default 2 comment '是否免费(2不免费1 免费)'"))
	private int free = 2;
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '价格(单位:元)'"))
	private double per_cost = 0;
	
	@Column(columnDefinition = ("int(1) default 1 comment '是否能使用优惠券(1 是 ，2 否 )'"))
	private Integer canUseCoupon = 1;
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '分享地址'"))
	private String share_url = "";
	
	/**
	 * cms项目添加，2017-03-02
	 */
	@Column(columnDefinition = ("varchar(100) default '0' comment '视频大小'"))
	private String video_size = "0";
	
	/**
	 * 免费观看时间，以毫秒为单位
	 */
	@Column(columnDefinition = ("int(9) default 0 comment '免费观看时间，以毫秒为单位'"))
	private Integer freeTime = 0;
	
	@Override
	public Serializable getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "Video [id=" + id + ", title=" + title + ", create_time=" + create_time + ", update_time=" + update_time
				+ ", creater_id=" + creater_id + ", course_id=" + course_id + ", time_long=" + time_long
				+ ", video_save_url=" + video_save_url + ", status=" + status + ", order_num=" + order_num + ", free="
				+ free + ", per_cost=" + per_cost + "]";
	}

	public String getStringId(){
		return id;
	}

	public long getPlay_time() {
		return play_time;
	}

	public void setPlay_time(long play_time) {
		this.play_time = play_time;
	}

	public String getVideo_pic_url() {
		return video_pic_url;
	}

	public void setVideo_pic_url(String video_pic_url) {
		this.video_pic_url = video_pic_url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}

	public String getCreater_id() {
		return creater_id;
	}

	public void setCreater_id(String creater_id) {
		this.creater_id = creater_id;
	}

	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public int getTime_long() {
		return time_long;
	}

	public void setTime_long(int time_long) {
		this.time_long = time_long;
	}

	public String getVideo_save_url() {
		return video_save_url;
	}

	public void setVideo_save_url(String video_save_url) {
		this.video_save_url = video_save_url;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getOrder_num() {
		return order_num;
	}

	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}

	public int getFree() {
		return free;
	}

	public void setFree(int free) {
		this.free = free;
	}

	public double getPer_cost() {
		return per_cost;
	}

	public void setPer_cost(double per_cost) {
		this.per_cost = per_cost;
	}

	public Integer getFreeTime() {
		return freeTime;
	}

	public void setFreeTime(Integer freeTime) {
		this.freeTime = freeTime;
	}

	public Integer getCanUseCoupon() {
		return canUseCoupon;
	}

	public void setCanUseCoupon(Integer canUseCoupon) {
		this.canUseCoupon = canUseCoupon;
	}

	public String getVideo_size() {
		return video_size;
	}

	public void setVideo_size(String video_size) {
		this.video_size = video_size;
	}
	
	
}
























