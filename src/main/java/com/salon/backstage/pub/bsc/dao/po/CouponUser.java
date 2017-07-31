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
 * 优惠券用户表
 */
@Entity
@Table(name = "tb_coupon_user")
public class CouponUser extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;

	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '用户ID'"))
	private String user_id = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '优惠券领取时间'") )
	private Long get_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '优惠券到期时间'"))
	private Long expire_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '优惠券到期提前提醒时间'"))
	private Long warn_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '优惠券使用时间'"))
	private Long use_time = 0L;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '优惠券ID'"))
	private String coupon_id = "";
	
	@Column(columnDefinition = ("int(2) default 0 comment '优惠券可应用于哪种类型(0.购买所有,1.购买课程,2.购买活动)，作废'"))
	private Integer use_type = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '优惠券使用时的订单ID'"))
	private String use_type_id = "";
	
	@Column(columnDefinition = ("int(1) default 1 comment '优惠券是否可用，1---可用，2---不可用，当优惠券被使用掉以后状态变为不可用'"))
	private Integer status = 1;
	
	/**
	 * 优惠券类型：0抵用券,1打折券
	 */
	@Column(columnDefinition = ("int(1) default 0 comment '优惠券类型：0抵用券,1打折券'"))
	private Integer coupon_type = null;
	
	/**
	 * 优惠券面额，如果是抵用券，表示抵用金额，如果是打折券，则是一个0-1之间的小数
	 */
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '优惠券面额，如果是抵用券，表示抵用金额，如果是打折券，则是一个0-1之间的小数'"))
	private Double denomination = null;
	
	/**
	 * 该优惠券是否为永久，1---是，2---否
	 */
	@Column(columnDefinition = ("int(1) default 2 comment '该优惠券是否为永久，1---是，2---否'"))
	private Integer isForever = 2;
	
	@Column(columnDefinition = ("varchar(32) default '' comment '优惠券号'"))
	private String number = null;
	
	@Column(columnDefinition = ("int(1) default 0 comment '发放类型(0平台发放,1系统自动发放)'"))
	private Integer coupon_provide_type = null;
	
	@Column(columnDefinition = ("int(2) default 0 comment '具体获得类型(1 课程点赞...)'"))
	private Integer get_type = 1;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '获得优惠券途径具体ID，作废'"))
	private String get_type_id = "";
	
	@Override
	public Serializable getId() {
		return id;
	}

	@Override
	public String toString() {
		return "CouponUser [id=" + id + ", user_id=" + user_id + ", get_time=" + get_time + ", expire_time="
				+ expire_time + ", warn_time=" + warn_time + ", use_time=" + use_time + ", coupon_id=" + coupon_id
				+ ", use_type=" + use_type + ", use_type_id=" + use_type_id + ", status=" + status + ", number="
				+ number + ", coupon_provide_type=" + coupon_provide_type + ", get_type=" + get_type + ", get_type_id="
				+ get_type_id + "]";
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Long getGet_time() {
		return get_time;
	}

	public Integer getIsForever() {
		return isForever;
	}

	public void setIsForever(Integer isForever) {
		this.isForever = isForever;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setGet_time(Long get_time) {
		this.get_time = get_time;
	}

	public Long getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(Long expire_time) {
		this.expire_time = expire_time;
	}

	public Long getWarn_time() {
		return warn_time;
	}

	public void setWarn_time(Long warn_time) {
		this.warn_time = warn_time;
	}

	public Long getUse_time() {
		return use_time;
	}

	public void setUse_time(Long use_time) {
		this.use_time = use_time;
	}

	public String getCoupon_id() {
		return coupon_id;
	}

	public void setCoupon_id(String coupon_id) {
		this.coupon_id = coupon_id;
	}

	public Integer getUse_type() {
		return use_type;
	}

	public void setUse_type(Integer use_type) {
		this.use_type = use_type;
	}

	public String getUse_type_id() {
		return use_type_id;
	}

	public void setUse_type_id(String use_type_id) {
		this.use_type_id = use_type_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getCoupon_provide_type() {
		return coupon_provide_type;
	}

	public void setCoupon_provide_type(Integer coupon_provide_type) {
		this.coupon_provide_type = coupon_provide_type;
	}

	public Integer getGet_type() {
		return get_type;
	}

	public void setGet_type(Integer get_type) {
		this.get_type = get_type;
	}

	public String getGet_type_id() {
		return get_type_id;
	}

	public void setGet_type_id(String get_type_id) {
		this.get_type_id = get_type_id;
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
	
}









