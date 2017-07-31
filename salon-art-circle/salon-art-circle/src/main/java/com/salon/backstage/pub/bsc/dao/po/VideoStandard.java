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
 * 视频达标表
 */
@Entity
@Table(name = "tb_video_standard")
public class VideoStandard extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '关联id'"))
	private String correlation_id = "";
	
	@Column(columnDefinition = ("int(11) default 0 comment '目标量'"))
	private Integer standard =0;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '预计完成时间'"))
	private long finish_time = 0L ;
	
	@Override
	public String toString() {
		return "VideoStandard [id=" + id + ", correlation_id=" + correlation_id + ", standard=" + standard
				+ ", finish_time=" + finish_time + "]";
	}
	public String getCorrelation_id() {
		return correlation_id;
	}
	public void setCorrelation_id(String correlation_id) {
		this.correlation_id = correlation_id;
	}
	
	public long getFinish_time() {
		return finish_time;
	}
	public void setFinish_time(long finish_time) {
		this.finish_time = finish_time;
	}
	@Override
	public Serializable getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	

}
