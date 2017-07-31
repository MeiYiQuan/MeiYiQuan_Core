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
 * 积分表
 */
@Entity
@Table(name = "tb_point")
public class Point extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '用户ID'"))
	private String user_id = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '积分领取时间'") )
	private Long get_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '积分到期时间'"))
	private Long expire_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '积分到期提前提醒时间'"))
	private Long warn_time = 0L;
	
	@Column(columnDefinition = ("int(4) default 0 comment '本次获得积分数量'"))
	private Integer point_quantum = null;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' COMMENT '获得积分途径ID'"))
	private String point_way_id = "";
	
	@Column(columnDefinition = ("int(1) default 1 comment '积分状态(2禁用,1可用)'"))
	private Integer status = 1;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '积分的最后一次使用时间'"))
	private Long use_time = 0L;
	
	@Column(columnDefinition = ("int(4) default 0 comment '积分的使用量'"))
	private Integer point_used = 0;
	
	@Column(columnDefinition = ("int(4) default 0 comment '积分的剩余量'"))
	private Integer point_unused = 0;
	
	@Override
	public Serializable getId() {
		return id;
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

	public Integer getPoint_quantum() {
		return point_quantum;
	}

	public void setPoint_quantum(Integer point_quantum) {
		this.point_quantum = point_quantum;
	}

	public String getPoint_way_id() {
		return point_way_id;
	}

	public void setPoint_way_id(String point_way_id) {
		this.point_way_id = point_way_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUse_time() {
		return use_time;
	}

	public void setUse_time(Long use_time) {
		this.use_time = use_time;
	}

	public Integer getPoint_used() {
		return point_used;
	}

	public void setPoint_used(Integer point_used) {
		this.point_used = point_used;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getPoint_unused() {
		return point_unused;
	}

	public void setPoint_unused(Integer point_unused) {
		this.point_unused = point_unused;
	}
	
	
}




