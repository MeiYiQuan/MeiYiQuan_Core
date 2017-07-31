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
 * 播放记录表
 */
@Entity
@Table(name = "tb_playrecord")
public class Playrecord extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '播放视频ID'"))
	private String video_id = "";
	
	@Column(columnDefinition = ("varchar(50) default '' comment '视频标题'"))
	private String video_title = null;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '课程ID'"))
	private String course_id = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '播放时间'"))
	private long play_time = 0L;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '用户ID'"))
	private String user_id = "";
	
	@Column(columnDefinition = ("varchar(20) default '' comment '上次播放的位置(原样接收，原样返回)'"))
	private String continue_time = "";
	
	/**
	 * 时长，秒
	 */
	@Column(columnDefinition = ("int(6) default 0 comment '播放时长，秒'"))
	private int time_long = 0;
	
	@Override
	public String toString() {
		return "Playrecord [id=" + id + ", video_id=" + video_id + ", course_id=" + course_id + ", play_time="
				+ play_time + ", user_id=" + user_id + "]";
	}

	@Override
	public Serializable getId() {
		return id;
	}
	
	public String getStringId(){
		return id;
	}
	
	public String getVideo_id() {
		return video_id;
	}

	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}

	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public long getPlay_time() {
		return play_time;
	}

	public void setPlay_time(long play_time) {
		this.play_time = play_time;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContinue_time() {
		return continue_time;
	}

	public void setContinue_time(String continue_time) {
		this.continue_time = continue_time;
	}

	public String getVideo_title() {
		return video_title;
	}

	public void setVideo_title(String video_title) {
		this.video_title = video_title;
	}

	public int getTime_long() {
		return time_long;
	}

	public void setTime_long(int time_long) {
		this.time_long = time_long;
	}
	
	
}
