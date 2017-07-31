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
 * 点赞表
 */
@Entity
@Table(name = "tb_like")
public class Like extends PO {
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '用户ID'"))
	private String user_id = "";

	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '点赞时间'") )
	private long like_time = 0L;

	@Column(columnDefinition = ("int(2) default 0 comment '被点赞类型(1课程,3活动,4讲师,2评论)'"))
	private int like_type = 0;

	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '被点赞的关联ID'"))
	private String like_type_id = "";
	
	@Column(columnDefinition = ("int(1) default 0 comment '0 赞,1 倒赞'"))
	private int like_dislike = 0;

	@Override
	public Serializable getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Like [id=" + id + ", user_id=" + user_id + ", like_time=" + like_time + ", like_type=" + like_type
				+ ", like_type_id=" + like_type_id + ", like_dislike=" + like_dislike + "]";
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public long getLike_time() {
		return like_time;
	}

	public void setLike_time(long like_time) {
		this.like_time = like_time;
	}

	public int getLike_type() {
		return like_type;
	}

	public void setLike_type(int like_type) {
		this.like_type = like_type;
	}

	public String getLike_type_id() {
		return like_type_id;
	}

	public void setLike_type_id(String like_type_id) {
		this.like_type_id = like_type_id;
	}

	public int getLike_dislike() {
		return like_dislike;
	}

	public void setLike_dislike(int like_dislike) {
		this.like_dislike = like_dislike;
	}
	
}
