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
 * 讲师表
 */
@Entity
@Table(name = "tb_teacher")
public class Teacher extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(10) default '' comment '讲师姓名'"))
	private String name = "";

	@Column(columnDefinition = ("varchar(10) default '' comment '讲师等级'"))
	private String level = "";
	
	@Column(columnDefinition = ("varchar(150) default '' comment '讲师简介'"))
	private String introduction = "";
	
	@Column(columnDefinition = ("varchar(500) default '' comment '讲师详细介绍'"))
	private String remark = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间'"))
	private long create_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '修改时间'"))
	private long update_time = 0L;
	
	@Column(columnDefinition = ("char(1) default '' comment '讲师昵称首字母'"))
	private String first_word = "";
	
	@Column(columnDefinition = ("int(1) default 0 comment '讲师宣传图类型(0 图片,1 视频)(名人详情页)'"))
	private int top_type = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '讲师宣传图片访问地址(讲师详情)'"))
	private String top_pic_url = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '对应讲师宣传视频访问地址'"))
	private String top_video_url = "";
	
	@Column(columnDefinition = ("int(1) default 1 comment '是否推荐到首页(2否 1是)'"))
	private int put_home = 1;
	
	@Column(columnDefinition = ("int(1) default 1 comment '状态(2 禁用,1 启用)'"))
	private int status = 1;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '创建人ID'"))
	private String creater_id = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '讲师ID'"))
	private String teacher_id = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '讲师头像存储访问路径'"))
	private String head_url = "";
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '讲师身价'"))
	private double worth = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ")  default '' comment '讲师等级ID，关联job表'"))
	private String level_id = "";
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '讲师提成，是个0-1之间的数。如果使用平台默认的讲师提成，则填-1'"))
	private double percent = -1;
	
	@Column(columnDefinition = ("int(2) default 3 comment '讲师收款工具类型：1--微信，2--QQ，3--支付宝，4--银行卡，5--京东钱包。默认3'"))
	private int make_type = 3;
	
	@Column(columnDefinition = ("varchar(100) default '' comment '讲师收款账号'"))
	private String make_account = "";
	
	@Override
	public Serializable getId() {
		return id;
	}
	
	public String getStringId(){
		return id;
	}
	
	@Override
	public String toString() {
		return "Teacher [id=" + id + ", name=" + name + ", level=" + level + ", introduction=" + introduction
				+ ", create_time=" + create_time + ", update_time=" + update_time + ", first_word=" + first_word
				+ ", top_type=" + top_type + ", top_pic_url=" + top_pic_url + ", top_video_url=" + top_video_url
				+ ", put_home=" + put_home + ", status=" + status + ", creater_id=" + creater_id + ", head_url="
				+ head_url + "]";
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
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

	public String getFirst_word() {
		return first_word;
	}

	public void setFirst_word(String first_word) {
		this.first_word = first_word;
	}

	public int getTop_type() {
		return top_type;
	}

	public void setTop_type(int top_type) {
		this.top_type = top_type;
	}

	public String getTop_pic_url() {
		return top_pic_url;
	}

	public void setTop_pic_url(String top_pic_url) {
		this.top_pic_url = top_pic_url;
	}

	public String getTop_video_url() {
		return top_video_url;
	}

	public void setTop_video_url(String top_video_url) {
		this.top_video_url = top_video_url;
	}

	public int getPut_home() {
		return put_home;
	}

	public void setPut_home(int put_home) {
		this.put_home = put_home;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreater_id() {
		return creater_id;
	}

	public void setCreater_id(String creater_id) {
		this.creater_id = creater_id;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}

	public double getWorth() {
		return worth;
	}

	public void setWorth(double worth) {
		this.worth = worth;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLevel_id() {
		return level_id;
	}

	public void setLevel_id(String level_id) {
		this.level_id = level_id;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public int getMake_type() {
		return make_type;
	}

	public void setMake_type(int make_type) {
		this.make_type = make_type;
	}

	public String getMake_account() {
		return make_account;
	}

	public void setMake_account(String make_account) {
		this.make_account = make_account;
	}
	
	
}



