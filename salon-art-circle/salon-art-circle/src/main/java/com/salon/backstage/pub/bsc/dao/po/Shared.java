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
 * 创建日期：2017年2月14日
 * 类说明：分享记录
 */
@Entity
@Table(name = "tb_shared")
public class Shared extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '分享人ID'"))
	private String userId = "";
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '分享内容ID，如视频id或者活动id，当分享的内容没有id时(如分享app链接)，这个字段可以不用'"))
	private String shareId = "";
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '分享时间'"))
	private long share_time = 0;
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '分享地址'"))
	private String url = "";
	
	@Column(columnDefinition = ("int(2) default 0 comment '分享类型：3(视频)，4(活动)，7(课程)，8(求课程)'"))
	private int type = 0;
	
	@Column(columnDefinition = ("int(2) default 0 comment '分享区域：1(QQ好友)，2(QQ空间)，3(微信好友)，4(微信朋友圈)，5(新浪微博)'"))
	private int district = 0;

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

	public String getShareId() {
		return shareId;
	}

	public void setShareId(String shareId) {
		this.shareId = shareId;
	}

	public long getShare_time() {
		return share_time;
	}

	public void setShare_time(long share_time) {
		this.share_time = share_time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDistrict() {
		return district;
	}

	public void setDistrict(int district) {
		this.district = district;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
