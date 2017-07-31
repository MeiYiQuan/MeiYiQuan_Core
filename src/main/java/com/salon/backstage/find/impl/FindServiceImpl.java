
package com.salon.backstage.find.impl;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;






import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.common.util.MD5Util;
import com.salon.backstage.common.util.StringUtil;
import com.salon.backstage.find.IFindService;
import com.salon.backstage.pub.bsc.dao.po.Collect;
import com.salon.backstage.pub.bsc.dao.po.Playrecord;
import com.salon.backstage.pub.bsc.dao.po.SystemInfo;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.dao.po.UserVideoRequest;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.user.IUserService;

@Service
public class FindServiceImpl implements IFindService {
	
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;

	@Override
	public List<Map> queryProvince() {
		String sql="select id,name from tb_district where pid=0";
		List map = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		return map;
	}

	@Override
	public List<Map> queryCity(Map<String, Object> json) {
		String sql="select id,name from tb_district where pid=?";
		String[] values={(String) json.get("provinceId")};
		List map=extraSpringHibernateTemplate.createSQLQueryFindAll(sql,values);
		return map;
	}

	@Override
	public List<Map> queryStatus() {
		String sql="select DISTINCT activity_status from tb_activity";
		List map = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		return map;
	}
	
	
	
}
