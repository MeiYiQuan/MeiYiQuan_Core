package com.salon.backstage.qcproject.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.dao.support.UserVideoRequestSupport;
import com.salon.backstage.qcproject.service.UserVideoRequestServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Enums.RequestStatus;
import com.salon.backstage.qcproject.util.Sql;

/**
 * 作者：齐潮
 * 创建日期：2016年12月27日
 * 类说明：
 */
@Service
public class UserVideoRequestServiceImplNEW implements UserVideoRequestServiceNEW {

	@Autowired
	private ObjectDao od;
	
	@Override
	public Code getRequestsByUserId(String userId, int page, int size) {
		Sql sql = UserVideoRequestSupport.getRequestsByUserId(userId);
		int start = (page-1)*size;
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), start, size);
		if(list!=null){
			for(Map<String,Object> map:list){
				int status = Integer.parseInt(map.get("status").toString());
				map.put("statusStr", RequestStatus.getRequestStatus(status).getMessage());
			}
		}
		Code result = Code.init(true, 0, "", (list==null?new ArrayList():list));
		return result;
	}

}
