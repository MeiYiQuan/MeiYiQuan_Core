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
 * 类说明：与订单表同步，这里记录的每个订单讲师与平台各自的收益情况
 */
@Entity
@Table(name = "tb_teacher_order")
public class TeacherOrder extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间，与订单的支付时间对应(不是创建时间，而是支付时间)'"))
	private long create_time = 0L;
	
	@Column(columnDefinition = ("int(1) default 1 comment '是否可用，1--可用，2--不可用。在计算讲师收益时需要附件这个条件判断，可以将非法订单的收益不计入讲师收益。备用字段，但是查询语句里需要这个判断'"))
	private int status = 1;
	
	@Column(columnDefinition = ("varchar(20) default '' comment '本条收益信息所对应的订单号'") )
	private String order_number = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '讲师ID，对应的是讲师表里的 teacher_id 字段'"))
	private String teacher_id = "";
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '本订单的总收益，即平台收益与讲师收益的和，对应了订单表的   平台获得金额 achieve 字段'"))
	private double sum_money = 0;
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '本订单中平台的收益，单位：元'"))
	private double server_achieve = 0;
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '本订单中讲师的收益，单位：元'"))
	private double teacher_achieve = 0;
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '本订单中讲师的提成比例，是个0-1之间数。'"))
	private double teacher_percent = 0;
	
	@Column(columnDefinition = ("int(1) default 1 comment '表示本订单中的讲师提成比例是使用了讲师自定义比例还是平台默认比例，1--讲师自定义比例，2--平台默认比例。可以反应购买时讲师有没有设置提成比例'"))
	private int percent_type = 1;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public double getSum_money() {
		return sum_money;
	}

	public void setSum_money(double sum_money) {
		this.sum_money = sum_money;
	}

	public double getServer_achieve() {
		return server_achieve;
	}

	public void setServer_achieve(double server_achieve) {
		this.server_achieve = server_achieve;
	}

	public double getTeacher_achieve() {
		return teacher_achieve;
	}

	public void setTeacher_achieve(double teacher_achieve) {
		this.teacher_achieve = teacher_achieve;
	}

	public double getTeacher_percent() {
		return teacher_percent;
	}

	public void setTeacher_percent(double teacher_percent) {
		this.teacher_percent = teacher_percent;
	}

	public int getPercent_type() {
		return percent_type;
	}

	public void setPercent_type(int percent_type) {
		this.percent_type = percent_type;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}
	
	
}
