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
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "tb_banner")
public class Banner extends PO {
	
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(50) default '' comment '轮播图名称'"))
	private String name = ""; 
	
	// order_num小的首先播放
	@Column(columnDefinition = ("int(8) default 0 comment '录播图播放顺序'"))
	private int order_num= 0; 
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '轮播图存放访问地址'"))
	private String pic_save_url = ""; 
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '轮播图跳转地址，已作废'"))
	private String pic_redirect_url = ""; 
	
	@Column(columnDefinition = ("varchar(500) default '' comment '标注'"))
	private String remark = ""; 
	
	@Column(columnDefinition = ("int(1) default 1 comment '状态(2为禁用,1为启用)'"))
	private int status = 1; 
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间'"))
	private Long create_time= 0L;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '修改时间'"))
	private Long update_time= 0L;
	
	@Column(columnDefinition = ("int(2) default 0 comment '轮播图关联类型(0 课程,1 讲师,2 活动)'"))
	private int jump_type= 0; 
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '轮播图关联类型ID'"))
	private String jump_id= "";

	/**
	 * 代表该banner是首页栏目还是发现栏目
	 */
	@Column(columnDefinition = ("int(2) default 0 comment '轮播图所在页面(1首页 2发现页面)'"))
	private Integer showtype = null;

	@Override
	public Serializable getId() {
		return id;
	} 

	@Override
	public String toString() {
		return "Banner [id=" + id + ", name=" + name + ", order_num=" + order_num + ", pic_save_url=" + pic_save_url
				+ ", pic_redirect_url=" + pic_redirect_url + ", remark=" + remark + ", status=" + status
				+ ", create_time=" + create_time + ", update_time=" + update_time + ", jump_type=" + jump_type
				+ ", jump_id=" + jump_id + "]";
	}

	public Integer getShowtype() {
		return showtype;
	}

	public void setShowtype(Integer showtype) {
		this.showtype = showtype;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Long update_time) {
		this.update_time = update_time;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrder_num() {
		return order_num;
	}

	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}

	public String getPic_save_url() {
		return pic_save_url;
	}

	public void setPic_save_url(String pic_save_url) {
		this.pic_save_url = pic_save_url;
	}

	public String getPic_redirect_url() {
		return pic_redirect_url;
	}

	public void setPic_redirect_url(String pic_redirect_url) {
		this.pic_redirect_url = pic_redirect_url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public int getJump_type() {
		return jump_type;
	}

	public void setJump_type(int jump_type) {
		this.jump_type = jump_type;
	}

	public String getJump_id() {
		return jump_id;
	}

	public void setJump_id(String jump_id) {
		this.jump_id = jump_id;
	}


	
}



























