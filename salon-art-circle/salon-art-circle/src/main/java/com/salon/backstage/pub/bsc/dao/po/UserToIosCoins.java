package com.salon.backstage.pub.bsc.dao.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.salon.backstage.qcproject.util.Mysql;

/**
 * 作者：齐潮
 * 创建日期：2017年2月13日
 * 类说明：用于去记录用户充值的记录，可以记录成功和失败，并记录失败原因
 */
@Entity
@Table(name = "tb_user_ioscoin")
public class UserToIosCoins {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '用户ID'") )
	private String userId = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment 'IOS点券ID'") )
	private String coinId = "";
	
	@Column(columnDefinition = ("decimal(" + Mysql.PRICE_LENGTHS + ") default 0 comment '用户本次获取到的ios币数量，充值失败这里为0'"))
	private double coin = 0;
	
	@Column(columnDefinition = ("int(1) default 1 comment '1成功，2失败'"))
	private int state = 1;
	
	@Column(columnDefinition = ("varchar(50) default '' comment '充值失败原因，充值成功可以忽略本字段'") )
	private String defeatMessage = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '充值时间'") )
	private long buyTime = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}

	public double getCoin() {
		return coin;
	}

	public void setCoin(double coin) {
		this.coin = coin;
	}

	public long getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(long buyTime) {
		this.buyTime = buyTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getDefeatMessage() {
		return defeatMessage;
	}

	public void setDefeatMessage(String defeatMessage) {
		this.defeatMessage = defeatMessage;
	}
	
	
}
