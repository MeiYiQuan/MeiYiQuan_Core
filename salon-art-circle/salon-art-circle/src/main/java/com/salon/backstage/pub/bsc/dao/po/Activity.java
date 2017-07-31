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
 * 活动表
 */

@Entity
@Table(name = "tb_activity")
public class Activity extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间'") )
	private Long create_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '修改时间'"))
	private Long update_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '活动时间'"))
	private Long activity_time = 0L;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '创建人ID'"))
	private String creater_id = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '关联讲师ID'"))
	private String teacher_id = ""; 
	
	@Column(columnDefinition = ("int(2) default 0 comment '状态(012345)，已经作废，具体状态由tb_activity_status表里的数据推算得出'"))
	private Integer status = 0;
	
	@Column(columnDefinition = ("int(2) default 0 comment '活动状态(0 结束,1 正在进行,作废，具体状态由tb_activity_status表里的数据推算得出)'"))
	private Integer activity_status = 0;
	
	@Column(columnDefinition = ("varchar(50) default '' comment '标题'"))
	private String title = ""; 
	
	@Column(columnDefinition = ("varchar(150) default '' comment '简要介绍'"))
	private String description = ""; 
	
	@Column(columnDefinition = ("longtext() default0 comment '详细介绍'"))
	private String remark = ""; 
	
	@Column(columnDefinition = ("varchar(50) default '' comment '主办方'"))
	private String organiser = ""; 
	
	@Column(columnDefinition = ("varchar(20) default '' comment '区域(省市等)'"))
	private String district = ""; 
	
	@Column(columnDefinition = ("varchar(50) default '' comment '详细地址'"))
	private String address = ""; 
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '价格(单位:元)'"))
	private double price = 0;
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '运营成本(单位:元)'"))
	private double cost = 0;
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '讲师嘉宾出场费(单位:元)，作废'"))
	private double appearance = 0;
	
	@Column(columnDefinition = ("int(1) default 2 comment '是否能使用优惠券(1 是 ，2 否 )'"))
	private Integer canUseCoupon = 2;
	
	@Column(columnDefinition = ("int(2) default 0 comment '宣传类型(0 图片,1 视频)'"))
	private Integer show_type = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '宣传图片访问地址'"))
	private String show_pic_url = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '宣传视频访问地址'"))
	private String show_video_url = "";

	/**
	 * 如果宣传类型是视频，则应该给一个视频封面地址
	 */
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '如果宣传类型是视频，则应该给一个视频封面地址'"))
	private String show_video_picurl = "";
	
	/**
	 * 分享链接
	 */
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '分享链接地址'"))
	private String share_url = "";
	
	@Column(columnDefinition = ("int(10) default 0 comment '活动的参与人数'"))
	private Integer part_num = 0;
	
	@Column(columnDefinition = ("int(10) default 0 comment '活动最大人数'"))
	private Integer most_man = 0;
	
	@Override
	public Serializable getId() {
		return id;
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

	public String getShow_video_picurl() {
		return show_video_picurl;
	}

	public void setShow_video_picurl(String show_video_picurl) {
		this.show_video_picurl = show_video_picurl;
	}

	public void setUpdate_time(Long update_time) {
		this.update_time = update_time;
	}

	public Long getActivity_time() {
		return activity_time;
	}

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public void setActivity_time(Long activity_time) {
		this.activity_time = activity_time;
	}

	public String getCreater_id() {
		return creater_id;
	}

	public void setCreater_id(String creater_id) {
		this.creater_id = creater_id;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getActivity_status() {
		return activity_status;
	}

	public void setActivity_status(Integer activity_status) {
		this.activity_status = activity_status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrganiser() {
		return organiser;
	}

	public void setOrganiser(String organiser) {
		this.organiser = organiser;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public Integer getShow_type() {
		return show_type;
	}

	public void setShow_type(Integer show_type) {
		this.show_type = show_type;
	}

	public String getShow_pic_url() {
		return show_pic_url;
	}

	public void setShow_pic_url(String show_pic_url) {
		this.show_pic_url = show_pic_url;
	}

	public String getShow_video_url() {
		return show_video_url;
	}

	public void setShow_video_url(String show_video_url) {
		this.show_video_url = show_video_url;
	}

	public Integer getPart_num() {
		return part_num;
	}

	public void setPart_num(Integer part_num) {
		this.part_num = part_num;
	}

	public Integer getMost_man() {
		return most_man;
	}

	public void setMost_man(Integer most_man) {
		this.most_man = most_man;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Activity [id=" + id + ", create_time=" + create_time
				+ ", update_time=" + update_time + ", activity_time="
				+ activity_time + ", creater_id=" + creater_id
				+ ", teacher_id=" + teacher_id + ", status=" + status
				+ ", activity_status=" + activity_status + ", title=" + title
				+ ", description=" + description + ", remark=" + remark
				+ ", organiser=" + organiser + ", district=" + district
				+ ", address=" + address + ", price=" + price + ", cost="
				+ cost + ", show_type=" + show_type + ", show_pic_url="
				+ show_pic_url + ", show_video_url=" + show_video_url
				+ ", part_num=" + part_num + ", most_man=" + most_man + "]";
	}

	public double getAppearance() {
		return appearance;
	}

	public void setAppearance(double appearance) {
		this.appearance = appearance;
	}

	public Integer getCanUseCoupon() {
		return canUseCoupon;
	}

	public void setCanUseCoupon(Integer canUseCoupon) {
		this.canUseCoupon = canUseCoupon;
	}

	
}







