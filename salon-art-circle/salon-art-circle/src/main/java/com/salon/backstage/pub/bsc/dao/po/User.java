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
 * 用户表
 */
@Entity
@Table(name = "tb_user")
public class User extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(50) default '' comment '用户名'"))
	private String username = "";
	
	@Column(columnDefinition = ("varchar(50) default '' comment '用户密码'"))
	private String password = "";
	
	@Column(columnDefinition = ("int(1) default 0 comment '性别 0未知,1女,2男'"))
	private Integer gender = 0;
	
	@Column(nullable = false,columnDefinition = ("bigint(15) default 0 comment '手机号'"))
	private Long phone;
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '头像存储访问地址'"))
	private String pic_save_url = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间'") )
	private Long create_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '修改时间'"))
	private Long update_time = 0L;
	
	@Column(nullable = false,columnDefinition = ("varchar(50) default '' comment '注册时填写的地址，如果是第三方，则需要app端去获取用户在第三方软件上的地址，以省份开头'"))
	private String district = "北京"; 
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '生日'"))
	private Long birthday = 0L;
	
	@Column(columnDefinition = ("varchar(20) default '' comment '职业'"))
	private String job = "";
	
	@Column(columnDefinition = ("varchar(5) default '' comment '星座，app不再使用，app直接用生日推算出星座'"))
	private String zodiac = "";
	
	@Column(columnDefinition = ("int(1) default 0 comment '用户最后登录设备类型(0 Android,1 IOS)'"))
	private Integer equi_type = 0;
	
	@Column(columnDefinition = ("int(1) default 0 comment 'openID(0 普通登录用户,1 微信登录用户)'"))
	private Integer vchat_iden = 0;
	
	@Column(columnDefinition = ("varchar(150) default '' comment '推送令牌'"))
	private String push_token = "";
	
	@Column(columnDefinition = ("int(1) default 1 comment '用户状态(1 可用,2 拉黑)'"))
	private Integer user_state = 1;
	
	@Column(columnDefinition = ("int(1) default 0 comment '用户类型(0学员,1讲师)'"))
	private Integer user_type = 0;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '最后一次登录时间(每日首次登录自动签到)'"))
	private Long latest_login_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '最后一次签到时间'"))
	private Long latest_sign_time = 0L;
	
	@Column(columnDefinition = ("int(2) default 0 comment '连续签到天数'"))
	private Integer continue_day = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '购买视频次数'"))
	private Integer buy_count = 0;
	
	@Column(columnDefinition = ("varchar(100) default '' comment '第三方登陆的唯一标识，如微信的openid'"))
	private String ssoId = "";
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment 'ios币'"))
	private double iosCoints = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '年龄段ID，对应job表'"))
	private String ageId = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '职业ID，对应job表，方便将来批量修改职业名称'"))
	private String jobId = "";
	
	@Column(columnDefinition = ("int(2) default -1 comment '星座的数字对应关系，详见静态量，没有设置生日此处为-1，表示没有星座'"))
	private int zodiacIndex = -1;
	
	@Column(columnDefinition = ("int(1) default 1 comment '1 可用,2不可评论'"))
	private Integer isComment = 1;
	
	@Override
	public Serializable getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public String getPic_save_url() {
		return pic_save_url;
	}

	public void setPic_save_url(String pic_save_url) {
		this.pic_save_url = pic_save_url;
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

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Long getBirthday() {
		return birthday;
	}

	public void setBirthday(Long birthday) {
		this.birthday = birthday;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getZodiac() {
		return zodiac;
	}

	public void setZodiac(String zodiac) {
		this.zodiac = zodiac;
	}

	public Integer getEqui_type() {
		return equi_type;
	}

	public void setEqui_type(Integer equi_type) {
		this.equi_type = equi_type;
	}

	public Integer getVchat_iden() {
		return vchat_iden;
	}

	public void setVchat_iden(Integer vchat_iden) {
		this.vchat_iden = vchat_iden;
	}

	public String getPush_token() {
		return push_token;
	}

	public void setPush_token(String push_token) {
		this.push_token = push_token;
	}

	public Integer getUser_state() {
		return user_state;
	}

	public void setUser_state(Integer user_state) {
		this.user_state = user_state;
	}

	public Integer getUser_type() {
		return user_type;
	}

	public void setUser_type(Integer user_type) {
		this.user_type = user_type;
	}

	public Long getLatest_login_time() {
		return latest_login_time;
	}

	public void setLatest_login_time(Long latest_login_time) {
		this.latest_login_time = latest_login_time;
	}

	public Long getLatest_sign_time() {
		return latest_sign_time;
	}

	public void setLatest_sign_time(Long latest_sign_time) {
		this.latest_sign_time = latest_sign_time;
	}

	public Integer getContinue_day() {
		return continue_day;
	}

	public void setContinue_day(Integer continue_day) {
		this.continue_day = continue_day;
	}

	public Integer getBuy_count() {
		return buy_count;
	}

	public void setBuy_count(Integer buy_count) {
		this.buy_count = buy_count;
	}

	public String getSsoId() {
		return ssoId;
	}

	public void setSsoId(String ssoId) {
		this.ssoId = ssoId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getIosCoints() {
		return iosCoints;
	}

	public void setIosCoints(double iosCoints) {
		this.iosCoints = iosCoints;
	}

	public String getAgeId() {
		return ageId;
	}

	public void setAgeId(String ageId) {
		this.ageId = ageId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public int getZodiacIndex() {
		return zodiacIndex;
	}

	public void setZodiacIndex(int zodiacIndex) {
		this.zodiacIndex = zodiacIndex;
	}

	public Integer getIsComment() {
		return isComment;
	}

	public void setIsComment(Integer isComment) {
		this.isComment = isComment;
	}
	
	

}
