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

@Entity
@Table(name = "tb_activity_status")
public class ActivityStatus extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间'") )
	private Long create_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '报名开始时间'") )
	private Long enroll_begin_time = 0L;

	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '报名结束时间'") )
	private Long enroll_end_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '准备开始时间'") )
	private Long prepare_start_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '准备结束时间'") )
	private Long prepare_end_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '活动开始时间'") )
	private Long activity_start_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '活动结束时间'") )
	private Long activity_end_time = 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '取消活动时间'") )
	private Long cancel_time = 0L;
	
	@Column(columnDefinition = ("int(1) default 2 comment '是否已经取消活动，1--是，2--否'") )
	private int isCancel = 2;

	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '活动ID'"))
	private String activity_id = "";
	
	@Override
	public Serializable getId() {
		return id;
	}

	@Override
	public String toString() {
		return "ActivityStatus [id=" + id + ", create_time=" + create_time
				+ ", enroll_begin_time=" + enroll_begin_time
				+ ", enroll_end_time=" + enroll_end_time
				+ ", prepare_start_time=" + prepare_start_time
				+ ", prepare_end_time=" + prepare_end_time
				+ ", activity_start_time=" + activity_start_time
				+ ", activity_end_time=" + activity_end_time + ", cancel_time="
				+ cancel_time + ", activity_id=" + activity_id + "]";
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	
	public int getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(int isCancel) {
		this.isCancel = isCancel;
	}
	
	public Long getEnroll_begin_time() {
		return enroll_begin_time;
	}

	public void setEnroll_begin_time(Long enroll_begin_time) {
		this.enroll_begin_time = enroll_begin_time;
	}

	public Long getEnroll_end_time() {
		return enroll_end_time;
	}

	public void setEnroll_end_time(Long enroll_end_time) {
		this.enroll_end_time = enroll_end_time;
	}

	public Long getPrepare_start_time() {
		return prepare_start_time;
	}

	public void setPrepare_start_time(Long prepare_start_time) {
		this.prepare_start_time = prepare_start_time;
	}

	public Long getPrepare_end_time() {
		return prepare_end_time;
	}

	public void setPrepare_end_time(Long prepare_end_time) {
		this.prepare_end_time = prepare_end_time;
	}

	public Long getActivity_start_time() {
		return activity_start_time;
	}

	public void setActivity_start_time(Long activity_start_time) {
		this.activity_start_time = activity_start_time;
	}

	public Long getActivity_end_time() {
		return activity_end_time;
	}

	public void setActivity_end_time(Long activity_end_time) {
		this.activity_end_time = activity_end_time;
	}

	public Long getCancel_time() {
		return cancel_time;
	}

	public void setCancel_time(Long cancel_time) {
		this.cancel_time = cancel_time;
	}

	public String getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	
}






















