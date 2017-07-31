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
 * 订单表。
 * ios内购平台收入算法：除去优惠券之后的价格 * (1 - 美国税率) * (1 - 充值时IOS抽取的比例)；
 * 普通购买平台收入算法：除去优惠券之后的价格 * (1 - 中国税率)
 */
@Entity
@Table(name = "tb_order")
public class Order extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(20) default '' comment '订单号'") )
	private String order_num = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '用户ID'"))
	private String user_id = "";
	
	@Column(columnDefinition = ("int(1) default 0 comment '状态(0未支付,1已支付,2已发送,已发送已作废)'"))
	private int status = 0;
	
	@Column(columnDefinition = ("int(2) default 0 comment '支付类型(1ping++支付宝,2ping++微信,4IOS内购)'"))
	private int pay_type = 0;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '下单时间'") )
	private long create_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '支付时间'") )
	private long pay_time = 0L;
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '结算金额(单位:元)，当是内购时，这个字段表示点券数额'"))
	private double price = 0;
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '平台获得金额，单位:元'"))
	private double achieve = 0;
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '税率，是个小于1的数。当是苹果内购时，这个表示充值时的税率'"))
	private double tax = 0;
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment 'IOS内购时，这个字段表示在苹果平台充值时扣除比例，是个小于1的数'"))
	private double ios_deduct = 0;
	
	/**
	 * 原价(去除优惠券金额之前)
	 */
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '商品原价，去除优惠券金额之前(单位:元)'"))
	private double oldPrice = 0;
	
	/**
	 * 是否使用了优惠券
	 */
	@Column(columnDefinition = ("int(1) default 0 comment '是否使用了优惠券，1---是，2---否'"))
	private int isUseCoupon = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '视频ID或者活动id'"))
	private String video_id = "";
	
	/**
	 * 订单类型：1---视频订单，2---活动订单
	 */
	@Column(columnDefinition = ("int(2) default 0 comment '订单类型：1---视频订单，2---活动订单'"))
	private int type;
	
	
	
	@Override
	public Serializable getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", order_num=" + order_num + ", user_id=" + user_id + ", status=" + status
				+ ", pay_type=" + pay_type + ", create_time=" + create_time + ", pay_time=" + pay_time + ", price="
				+ price + ", video_id=" + video_id + "]";
	}

	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public long getPay_time() {
		return pay_time;
	}

	public void setPay_time(long pay_time) {
		this.pay_time = pay_time;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getVideo_id() {
		return video_id;
	}

	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(double oldPrice) {
		this.oldPrice = oldPrice;
	}

	public int getIsUseCoupon() {
		return isUseCoupon;
	}

	public void setIsUseCoupon(int isUseCoupon) {
		this.isUseCoupon = isUseCoupon;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getAchieve() {
		return achieve;
	}

	public void setAchieve(double achieve) {
		this.achieve = achieve;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public double getIos_deduct() {
		return ios_deduct;
	}

	public void setIos_deduct(double ios_deduct) {
		this.ios_deduct = ios_deduct;
	}

}

















