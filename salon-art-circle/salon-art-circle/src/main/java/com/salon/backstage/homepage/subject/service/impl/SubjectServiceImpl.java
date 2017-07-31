package com.salon.backstage.homepage.subject.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.common.util.StringUtil;
import com.salon.backstage.homepage.subject.service.ISubjectService;
import com.salon.backstage.pub.bsc.domain.Constant;

@Service
public class SubjectServiceImpl implements ISubjectService {
	
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@SuppressWarnings("all")
	@Override
	public List<Map> queryHomepage(Map json) {
		//pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("select id,cover_pic_url,introduction,name from tb_subject where status = " + Constant.YES_INT);
		List<Map> homepageList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		sql.append(" LIMIT ");
		if(pageNoNull && pagesizeNull){
			sql.append(Constant.SUBJECT_PAGENO+","+Constant.SUBJECT_PAGESIZE);
		}else if(pageNoNull){
			sql.append(Constant.SUBJECT_PAGENO+","+Integer.valueOf((String)json.get("pagesize")));
		}else if(pagesizeNull){
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			if((pageNo-1)*Constant.SUBJECT_PAGESIZE > homepageList.size()){
				int max = homepageList.size()/Constant.SUBJECT_PAGESIZE;
				int start = max*Constant.SUBJECT_PAGESIZE;
				sql.append(start+","+Constant.SUBJECT_PAGESIZE);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * Constant.SUBJECT_PAGESIZE;
				sql.append(start+","+Constant.SUBJECT_PAGESIZE);
			}else{
				sql.append(Constant.SUBJECT_PAGENO+","+Constant.SUBJECT_PAGESIZE);
			}
		}else{
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			int pagesize = Integer.valueOf((String)json.get("pagesize"));
			if((pageNo-1)*pagesize > homepageList.size()){
				int max = homepageList.size()/pagesize;
				int start = max*pagesize;
				sql.append(start+","+pagesize);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * pagesize;
				int end = pageNo * pagesize;
				sql.append(start+","+end);
			}else{
				sql.append(Constant.SUBJECT_PAGENO+","+pagesize);
			}
		}
		List<Map> homepagePageList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		return homepagePageList;
	}

}







