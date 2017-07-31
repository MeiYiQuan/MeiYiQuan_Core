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
 * 创建日期：2016年12月12日
 * 类说明：用于引导图
 */
@Entity
@Table(name = "tb_guidpic")
public class GuidePic extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	/**
	 * 图片url
	 */
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '图片url'"))
	private String url;
	
	/**
	 * 图片展示顺序(搜索规则：inde in (1,2,3) order by index asc )
	 */
	@Column(columnDefinition = ("int(2) default 0 comment '图片展示顺序(搜索规则：inde in (1,2,3) order by index asc )'"))
	private int inde;
	
	/**
	 * 创建图片时间
	 */
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建图片时间'") )
	private long createTime;

	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	

	public int getInde() {
		return inde;
	}

	public void setInde(int inde) {
		this.inde = inde;
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
