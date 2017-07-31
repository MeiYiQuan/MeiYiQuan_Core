package com.salon.backstage.pub.bsc.dao.vo;

import org.springframework.stereotype.Component;

/**
 * 功能:封装向前端传递的数据
 */
@Component
public class  MobileMessage {

	private boolean result;// success 成功,false 失败 
	
	private int code = 0;  // 0 失败,1 成功
	
	private String errorMessage; //成功失败返回的提示信息
	
	private Object response; 
	
	private String token;

	@Override
	public String toString() {
		return "MobileMessage [result=" + result + ", code=" + code + ", errorMessage=" + errorMessage + ", response="
				+ response + ", token=" + token + "]";
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isResult() {
		return result;
	}
	
	public void setResult(boolean result) {
		this.result = result;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}
	
}
