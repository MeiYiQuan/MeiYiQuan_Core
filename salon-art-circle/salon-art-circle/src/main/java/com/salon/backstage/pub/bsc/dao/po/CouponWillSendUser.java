package com.salon.backstage.pub.bsc.dao.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.salon.backstage.core.PO;
import com.salon.backstage.qcproject.util.Mysql;

/**
 * 作者：齐潮
 * 创建日期：2017年2月18日
 * 类说明：用于后台管理系统，将要给某用户发送优惠券，将这个申请记录在本表中
 */
@Entity
@Table(name = "tb_coupon_willsend_user")
public class CouponWillSendUser extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = 50)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '用户ID'"))
	private String user_id = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '优惠券ID'"))
	private String coupon_id = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '申请人ID，这里是指是由谁发起的申请，admin表的id'"))
	private String req_admin_id = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '审批人ID，这里是指是由谁审批，admin表的id，在审批之前这里为空，审批时记录管理员id'"))
	private String resp_admin_id = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '发起申请的时间'") )
	private Long req_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '审核的时间'") )
	private Long resp_time = 0L;
	
	@Column(columnDefinition = ("int(1) default 0 comment '状态：1--刚申请，2--审核通过，3--审核不通过'"))
	private Integer use_type = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCoupon_id() {
		return coupon_id;
	}

	public void setCoupon_id(String coupon_id) {
		this.coupon_id = coupon_id;
	}

	public String getReq_admin_id() {
		return req_admin_id;
	}

	public void setReq_admin_id(String req_admin_id) {
		this.req_admin_id = req_admin_id;
	}

	public String getResp_admin_id() {
		return resp_admin_id;
	}

	public void setResp_admin_id(String resp_admin_id) {
		this.resp_admin_id = resp_admin_id;
	}

	public Long getReq_time() {
		return req_time;
	}

	public void setReq_time(Long req_time) {
		this.req_time = req_time;
	}

	public Long getResp_time() {
		return resp_time;
	}

	public void setResp_time(Long resp_time) {
		this.resp_time = resp_time;
	}

	public Integer getUse_type() {
		return use_type;
	}

	public void setUse_type(Integer use_type) {
		this.use_type = use_type;
	}
	
	
	
}
