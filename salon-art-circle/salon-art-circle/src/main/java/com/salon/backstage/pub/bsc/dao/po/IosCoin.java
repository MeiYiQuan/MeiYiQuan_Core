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
 * 创建日期：2017年2月13日
 * 类说明：用于存储IOS内购的价格对应关系
 */
@Entity
@Table(name = "tb_ios_coin")
public class IosCoin extends PO{
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	/**
	 * 用户购买ios币需要支付的金额，即真实金额，人民币
	 */
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '用户购买ios币需要支付的金额，即真实金额，人民币'"))
	private double payAmount = 0;
	
	/**
	 * 用户购买之后获取到的ios币数量
	 */
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '用户购买之后获取到的ios币数量'"))
	private double iosAmount = 0;
	
	/**
	 * 赠送百分比，当平台有活动(如xx月xx日-yy月yy日期间购买ios币即送30%，这个值就是0.3)时，只需要修改这个值即可，这个值是个大于0的数
	 */
	@Column(columnDefinition = ("decimal(5,3) default 0 comment '赠送百分比，当平台有活动(如xx月xx日-yy月yy日期间购买ios币即送30%，这个值就是0.3)时，只需要修改这个值即可，这个值是个大于0的数'"))
	private double multiple = 0;
	
	/**
	 * 是否可以使用(1--是，2--否)
	 */
	@Column(columnDefinition = ("int(2) default 1 comment '是否可以使用(1--是，2--否)'"))
	private int isDoing = 1;
	
	/**
	 * 创建时间
	 */
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间'") )
	private Long create_time = 0L;
	
	/**
	 * 修改时间
	 */
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '修改时间'"))
	private Long update_time = 0L;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(double payAmount) {
		this.payAmount = payAmount;
	}

	public double getIosAmount() {
		return iosAmount;
	}

	public void setIosAmount(double iosAmount) {
		this.iosAmount = iosAmount;
	}

	public double getMultiple() {
		return multiple;
	}

	public void setMultiple(double multiple) {
		this.multiple = multiple;
	}

	public int getIsDoing() {
		return isDoing;
	}

	public void setIsDoing(int isDoing) {
		this.isDoing = isDoing;
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
	
}
