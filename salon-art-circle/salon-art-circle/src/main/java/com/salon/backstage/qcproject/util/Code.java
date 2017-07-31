package com.salon.backstage.qcproject.util;

import java.util.List;
import java.util.Map;

/**
 * 作者：齐潮
 * 创建日期：2016年12月8日
 * 类说明：用于传递信息
 */
public class Code {

	private boolean result;
	
	private int code;
	
	private String errorMessage;
	
	private Object response;
	
	private Code(){}
	
	public static Code init(boolean result,int code,String message){
		Code sc = new Code();
		sc.result = result;
		sc.code = code;
		sc.errorMessage = message;
		return sc;
	}
	
	public static Code init(boolean result,int code,String message,Object response){
		Code sc = new Code();
		sc.result = result;
		sc.code = code;
		sc.errorMessage = message;
		sc.response = response;
		return sc;
	}
	
	
	public int getCode() {
		// TODO Auto-generated method stub
		return code;
	}

	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return errorMessage;
	}

	public boolean isResult() {
		return result;
	}

	@SuppressWarnings("rawtypes")
	public List toListData() {
		// TODO Auto-generated method stub
		return (List) response;
	}

	@SuppressWarnings("rawtypes")
	public Map toMapData() {
		// TODO Auto-generated method stub
		return (Map) response;
	}

	public Object getResponse() {
		// TODO Auto-generated method stub
		return response;
	}

}
