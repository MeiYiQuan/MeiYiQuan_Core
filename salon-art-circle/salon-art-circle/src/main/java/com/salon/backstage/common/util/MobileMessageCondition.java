package com.salon.backstage.common.util;

import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;

/**
 * 赋值MobileMessage中的属性
 * @author CXY
 *
 */
public class MobileMessageCondition {
	
	private static MobileMessage mobileMessage =null;
	
	/**
	 * 功能:向给前台返回的对象中添加数据
	 */
	public static MobileMessage addCondition(boolean result,int code,String errorMessage,Object response){
		mobileMessage = new MobileMessage();
		mobileMessage.setResult(result);
		mobileMessage.setCode(code);
		mobileMessage.setErrorMessage(errorMessage);
		mobileMessage.setResponse(response);
		return mobileMessage;
	}
	
}
