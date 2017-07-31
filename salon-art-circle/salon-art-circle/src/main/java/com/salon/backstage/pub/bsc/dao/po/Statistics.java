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
 * 数据统计表
 */
@Entity
@Table(name = "tb_statistics")
public class Statistics extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("int(2) default 0 comment '关联类型(1课程,2活动,3视频,4.讲师)'"))
	private Integer type = null;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '关联ID'"))
	private String type_id = "";
	
	@Column(columnDefinition = ("int(2) default 0 comment '连续签到天数'"))
	private Integer signdays = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '优惠券数量'"))
	private Integer coupon_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '点击量'"))
	private Integer click_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '点击虚拟量'"))
	private Integer click_expect_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '收藏量'"))
	private Integer collect_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '收藏虚拟量'"))
	private Integer collect_expect_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '评论量'"))
	private Integer comment_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '评论虚拟量'"))
	private Integer comment_expect_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '点倒赞量'"))
	private Integer dislike_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '倒赞虚拟量'"))
	private Integer dislike_expect_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '点赞量'"))
	private Integer like_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '点赞虚拟量'"))
	private Integer like_expect_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '分享量'"))
	private Integer share_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '分享虚拟量'"))
	private Integer share_expect_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '播放量'"))
	private Integer play_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '播放虚拟量'"))
	private Integer play_expect_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '关注量'"))
	private Integer follow_count = 0;
	
	@Column(columnDefinition = ("int(9) default 0 comment '关注虚拟量'"))
	private Integer follow_expect_count = 0;
	
	@Override
	public Serializable getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Statistics [id=" + id + ", type=" + type + ", type_id=" + type_id + ", signdays=" + signdays
				+ ", coupon_count=" + coupon_count + ", click_count=" + click_count + ", click_expect_count="
				+ click_expect_count + ", collect_count=" + collect_count + ", collect_expect_count="
				+ collect_expect_count + ", comment_count=" + comment_count + ", comment_expect_count="
				+ comment_expect_count + ", dislike_count=" + dislike_count + ", dislike_expect_count="
				+ dislike_expect_count + ", like_count=" + like_count + ", like_expect_count=" + like_expect_count
				+ ", share_count=" + share_count + ", share_expect_count=" + share_expect_count + ", play_count="
				+ play_count + ", play_expect_count=" + play_expect_count + ", follow_count=" + follow_count
				+ ", follow_expect_count=" + follow_expect_count + "]";
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public Integer getSigndays() {
		return signdays;
	}

	public void setSigndays(Integer signdays) {
		this.signdays = signdays;
	}

	public Integer getCoupon_count() {
		return coupon_count;
	}

	public void setCoupon_count(Integer coupon_count) {
		this.coupon_count = coupon_count;
	}

	public Integer getClick_count() {
		return click_count;
	}

	public void setClick_count(Integer click_count) {
		this.click_count = click_count;
	}

	public Integer getClick_expect_count() {
		return click_expect_count;
	}

	public void setClick_expect_count(Integer click_expect_count) {
		this.click_expect_count = click_expect_count;
	}

	public Integer getCollect_count() {
		return collect_count;
	}

	public void setCollect_count(Integer collect_count) {
		this.collect_count = collect_count;
	}

	public Integer getCollect_expect_count() {
		return collect_expect_count;
	}

	public void setCollect_expect_count(Integer collect_expect_count) {
		this.collect_expect_count = collect_expect_count;
	}

	public Integer getComment_count() {
		return comment_count;
	}

	public void setComment_count(Integer comment_count) {
		this.comment_count = comment_count;
	}

	public Integer getComment_expect_count() {
		return comment_expect_count;
	}

	public void setComment_expect_count(Integer comment_expect_count) {
		this.comment_expect_count = comment_expect_count;
	}

	public Integer getDislike_count() {
		return dislike_count;
	}

	public void setDislike_count(Integer dislike_count) {
		this.dislike_count = dislike_count;
	}

	public Integer getDislike_expect_count() {
		return dislike_expect_count;
	}

	public void setDislike_expect_count(Integer dislike_expect_count) {
		this.dislike_expect_count = dislike_expect_count;
	}

	public Integer getLike_count() {
		return like_count;
	}

	public void setLike_count(Integer like_count) {
		this.like_count = like_count;
	}

	public Integer getLike_expect_count() {
		return like_expect_count;
	}

	public void setLike_expect_count(Integer like_expect_count) {
		this.like_expect_count = like_expect_count;
	}

	public Integer getShare_count() {
		return share_count;
	}

	public void setShare_count(Integer share_count) {
		this.share_count = share_count;
	}

	public Integer getShare_expect_count() {
		return share_expect_count;
	}

	public void setShare_expect_count(Integer share_expect_count) {
		this.share_expect_count = share_expect_count;
	}

	public Integer getPlay_count() {
		return play_count;
	}

	public void setPlay_count(Integer play_count) {
		this.play_count = play_count;
	}

	public Integer getPlay_expect_count() {
		return play_expect_count;
	}

	public void setPlay_expect_count(Integer play_expect_count) {
		this.play_expect_count = play_expect_count;
	}

	public Integer getFollow_count() {
		return follow_count;
	}

	public void setFollow_count(Integer follow_count) {
		this.follow_count = follow_count;
	}

	public Integer getFollow_expect_count() {
		return follow_expect_count;
	}

	public void setFollow_expect_count(Integer follow_expect_count) {
		this.follow_expect_count = follow_expect_count;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}


























