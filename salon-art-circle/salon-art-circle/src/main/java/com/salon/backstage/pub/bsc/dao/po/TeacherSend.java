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
 * 创建日期：2017年2月24日
 * 类说明：用于讲师提现记录，后台管理员对讲师发起申请以及对申请的审核都是对本表的操作
 */
@Entity
@Table(name = "tb_teacher_send")
public class TeacherSend extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '申请时间'"))
	private long apply_time = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '申请管理员的id'"))
	private String apply_admin_id = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '审核时间'"))
	private long verify_time = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '审核管理员的id'"))
	private String verify_admin_id = "";
	
	@Column(columnDefinition = ("int(1) default 1 comment '该申请信息的状态：1--审核中(刚刚申请)，2--通过(已经向讲师汇款完毕)，3--驳回(没有向讲师汇款)'"))
	private int status = 1;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '收款讲师的id'"))
	private String teacher_id = "";
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '本次提现金额，单位：元'"))
	private double send_money = 0;
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '本次提现后剩余的金额(这个只是一个参考值，表示发起申请时的数据信息，当审核时真正的值可能会比这个值大，因为在申请期间可能会产生新的订单)，单位：元'"))
	private double shengyu = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getApply_time() {
		return apply_time;
	}

	public void setApply_time(long apply_time) {
		this.apply_time = apply_time;
	}

	public String getApply_admin_id() {
		return apply_admin_id;
	}

	public void setApply_admin_id(String apply_admin_id) {
		this.apply_admin_id = apply_admin_id;
	}

	public long getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(long verify_time) {
		this.verify_time = verify_time;
	}

	public String getVerify_admin_id() {
		return verify_admin_id;
	}

	public void setVerify_admin_id(String verify_admin_id) {
		this.verify_admin_id = verify_admin_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}

	public double getSend_money() {
		return send_money;
	}

	public void setSend_money(double send_money) {
		this.send_money = send_money;
	}

	public double getShengyu() {
		return shengyu;
	}

	public void setShengyu(double shengyu) {
		this.shengyu = shengyu;
	}
	
}
