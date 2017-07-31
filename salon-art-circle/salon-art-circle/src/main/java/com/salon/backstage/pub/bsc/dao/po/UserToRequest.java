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
 * 作者：齐潮
 * 创建日期：2016年12月17日
 * 类说明：用户给求课程投票的表
 */
@Entity
@Table(name = "tb_user_request")
public class UserToRequest extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	/**
	 * 投票者的id
	 */
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '投票者的id'"))
	private String userId;
	
	/**
	 * 求课程表的id
	 */
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '求课程表的id'"))
	private String requestId;
	
	/**
	 * 创建时间：投票时间
	 */
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间：投票时间'"))
	private long createTime;

	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
