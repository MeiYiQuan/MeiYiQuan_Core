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
 * 优惠券表
 */
@Entity
@Table(name = "tb_coupon")
public class Coupon extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;

	@Column(columnDefinition = ("varchar(50) default '' comment '优惠券名称'"))
	private String name = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '优惠券背景图'"))
	private String background_pic_url = "";
	
	@Column(columnDefinition = ("int(1) default 0 comment '优惠券属性类型(0抵用券,1打折券)'"))
	private Integer coupon_type = null;
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '优惠券面额，如果是抵用券，表示抵用金额，如果是打折券，则是一个0-1之间的小数'"))
	private Double denomination = null;
	
	@Column(columnDefinition = ("int(1) default 0 comment '发放类型(0平台发放,1系统自动发放)'"))
	private Integer coupon_provide_type = null;
	
	@Column(columnDefinition = ("int(1) default 1 comment '状态(2禁用,1开启)'"))
	private Integer status = 1;
	
	@Column(columnDefinition = ("bigint(11) default '0' comment '优惠券号'"))
	private String number = "0";

	@Column(columnDefinition = ("varchar(50) default '' comment '优惠券描述'"))
	private String introduction = "";
	
	@Column(columnDefinition = ("int(9) default 0 comment '到期数量，作废'"))
	private Integer expire_count = null;
	
	/**
	 * 优惠券有效期类型：1---按月计算，2---按天计算，3---永久
	 */
	@Column(columnDefinition = ("int(2) default 1 comment '优惠券有效期类型：1---按月计算，2---按天计算，3---永久'"))
	private Integer expire_type = null;
	
	/**
	 * 优惠券有效期：数值，要根据上面的类型来取。当是永久时这个字段作废
	 */
	@Column(columnDefinition = ("int(3) default 0 comment '优惠券有效期：数值，要根据上面的类型来取。当是永久时这个字段作废'"))
	private Integer expire_time = null;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '最后编辑人ID'"))
	private String admin_id = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '最后编辑时间'") )
	private Long latest_update_time = 0L;
	
	@Override
	public Serializable getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Coupon [id=" + id + ", name=" + name + ", background_pic_url=" + background_pic_url + ", coupon_type="
				+ coupon_type + ", denomination=" + denomination + ", coupon_provide_type=" + coupon_provide_type
				+ ", status=" + status + ", introduction=" + introduction + ", expire_count=" + expire_count
				+ ", admin_id=" + admin_id + ", latest_update_time=" + latest_update_time + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBackground_pic_url() {
		return background_pic_url;
	}

	public void setBackground_pic_url(String background_pic_url) {
		this.background_pic_url = background_pic_url;
	}

	public Integer getCoupon_type() {
		return coupon_type;
	}

	public void setCoupon_type(Integer coupon_type) {
		this.coupon_type = coupon_type;
	}

	public Double getDenomination() {
		return denomination;
	}

	public void setDenomination(Double denomination) {
		this.denomination = denomination;
	}

	public Integer getCoupon_provide_type() {
		return coupon_provide_type;
	}

	public void setCoupon_provide_type(Integer coupon_provide_type) {
		this.coupon_provide_type = coupon_provide_type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Integer getExpire_count() {
		return expire_count;
	}

	public void setExpire_count(Integer expire_count) {
		this.expire_count = expire_count;
	}

	public String getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}

	public Long getLatest_update_time() {
		return latest_update_time;
	}

	public void setLatest_update_time(Long latest_update_time) {
		this.latest_update_time = latest_update_time;
	}
	
	

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getExpire_type() {
		return expire_type;
	}

	public void setExpire_type(Integer expire_type) {
		this.expire_type = expire_type;
	}

	public Integer getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(Integer expire_time) {
		this.expire_time = expire_time;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}













