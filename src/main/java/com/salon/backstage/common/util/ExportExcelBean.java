package com.salon.backstage.common.util;

import java.util.List;

import net.sf.json.JSONObject;

	/*** 
	 * @author liudan 
	 */  
	public class ExportExcelBean {  
	 public String[]  title={"辅导日期","TSR姓名","所属城市、项目","录音时长"};
	 public String[]  content={"6月6号","任盈盈","上海招商银行","16分钟"};
	 public String[]  title2={"阶段","录音提现的内容","客户","TSR的话"};
	 public List<JSONObject> listContent;
	 
	 public ExportExcelBean(String[] content,List<JSONObject> list){
		 this.content=content;
		 this.listContent=list;
	 }

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String[] getContent() {
		return content;
	}

	public void setContent(String[] content) {
		this.content = content;
	}

	public String[] getTitle2() {
		return title2;
	}

	public void setTitle2(String[] title2) {
		this.title2 = title2;
	}

	public List<JSONObject> getListContent() {
		return listContent;
	}

	public void setListContent(List<JSONObject> listContent) {
		this.listContent = listContent;
	}
	 
}
