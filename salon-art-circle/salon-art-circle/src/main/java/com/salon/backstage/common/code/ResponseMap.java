package com.salon.backstage.common.code;

import java.util.HashMap;
import java.util.Map;

import com.salon.backstage.common.error_code.ApiErrorCode;

public class ResponseMap {

	/**
	 * 错误码
	 */
	private int code;

	/**
	 * 错误信息
	 */
	private String message;

	/**
	 * 返回数据
	 */
	private Object data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ResponseMap(int code, String message, Object data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public ResponseMap() {
		super();
	}

	public Map<String, Object> generateMap() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(ProjectConstants.RESPONSE_JSON_CODE, this.code);
		returnMap.put(ProjectConstants.RESPONSE_JSON_MESSAGE, this.message);
		returnMap.put(ProjectConstants.RESPONSE_JSON_DATA, this.data);
		return returnMap;
	}

	public ResponseMap(ApiErrorCode aec) {
		this.code = aec.getCode();
		this.message = aec.getErrorMessage();
		this.data = null;
	}
	
	public ResponseMap(ApiErrorCode aec, Object obj) {
		this.code = aec.getCode();
		this.message = aec.getErrorMessage();
		this.data = obj;
	}
}
