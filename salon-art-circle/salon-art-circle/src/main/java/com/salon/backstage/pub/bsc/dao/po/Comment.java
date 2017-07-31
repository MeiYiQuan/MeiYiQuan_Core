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
 * 评论表
 */

@Entity
@Table(name = "tb_comment")
public class Comment extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '用户ID'"))
	private String user_id = "";

	@Column(columnDefinition = ("int(1) default 1 comment '评论级别(1.一级评论  2.二级评论)'"))
	private int commed_type = 1;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '父ID'"))
	private String commed_id = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '评论时间'") )
	private long comm_time = 0L;

	@Column(columnDefinition = ("varchar(500) default '' comment '评论内容'"))
	private String comm_content = "";
	
	@Column(columnDefinition = ("int(2) default 0 comment '评论类型(见静态量)'"))
	private int type;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '被评论的内容ID'"))
	private String comm_content_id = "";

	@Column(columnDefinition = ("int(2) default 2 comment '是否拉黑 1是 2否'"))
	private Integer status = 2;
	
	@Override
	public String toString() {
		return "Comment [id=" + id + ", user_id=" + user_id + ", commed_type=" + commed_type + ", commed_id="
				+ commed_id + ", comm_time=" + comm_time + ", comm_content=" + comm_content + ", comm_content_id="
				+ comm_content_id + "]";
	}

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

	public int getCommed_type() {
		return commed_type;
	}

	public void setCommed_type(int commed_type) {
		this.commed_type = commed_type;
	}

	public String getCommed_id() {
		return commed_id;
	}

	public void setCommed_id(String commed_id) {
		this.commed_id = commed_id;
	}

	public long getComm_time() {
		return comm_time;
	}

	public void setComm_time(long comm_time) {
		this.comm_time = comm_time;
	}

	public String getComm_content() {
		return comm_content;
	}

	public void setComm_content(String comm_content) {
		this.comm_content = comm_content;
	}

	public String getComm_content_id() {
		return comm_content_id;
	}

	public void setComm_content_id(String comm_content_id) {
		this.comm_content_id = comm_content_id;
	}

	public String getStringId(){
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}















