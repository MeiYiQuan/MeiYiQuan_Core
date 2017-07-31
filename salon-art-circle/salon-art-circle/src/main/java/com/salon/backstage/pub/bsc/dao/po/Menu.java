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
 * 创建日期：2016年12月7日
 * 类说明：后台菜单
 */
@Entity
@Table(name = "tb_menu")
public class Menu extends PO {

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "id", insertable = true, updatable = true, nullable = false, length = Mysql.ID_LENGTH)
	private String id;
	
	@Column(columnDefinition = ("varchar(20) default '' comment '菜单名称'"))
	private String menuName;
	
	/**
	 * 父级菜单的parentId为静态量TOPMENU_PARENTID
	 */
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment '父级id，父级菜单的parentId为静态量TOPMENU_PARENTID'"))
	private String parentId;

	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '创建时间'"))
	private long createTime;
	
	@Column(columnDefinition = ("bigint(" + Mysql.TIMESTAMP_LENGTH + ") default 0 comment '修改时间'"))
	private long updateTime;
	
	@Column(columnDefinition = ("varchar(" + Mysql.ID_LENGTH + ") default '' comment 'url的id'"))
	private String urlId;
	
	@Column(columnDefinition = ("varchar(" + Mysql.PHOTO_VIDEO_URL_LENGTH + ") default '' comment '菜单的链接地址，当是父菜单时，这里为空'"))
	private String url;

	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return id;
	}
	
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setId(String id) {
		this.id = id;
	}
}
