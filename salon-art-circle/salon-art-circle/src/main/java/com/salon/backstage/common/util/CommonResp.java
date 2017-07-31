package com.salon.backstage.common.util;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.alibaba.fastjson.JSONObject;

public class CommonResp {
	public static final int code_success = 0;

	private Integer code = null;
	private String errorMessage = null;
	private Object response = null;

	public CommonResp() {
	}

	public CommonResp(Integer code, String errorMessage) {
		this.code = code;
		this.errorMessage = errorMessage;
	}

	public CommonResp(Integer code, String errorMessage, Object response) {
		this.code = code;
		this.errorMessage = errorMessage;
		this.response = response;
	}

	public static CommonResp buildErrorResp(Throwable throwable) {
		return new CommonResp(BackendErrorCode.exception.getCode(), ExceptionUtils.getStackTrace(throwable));
	}

	public static CommonResp buildErrorResp(ErrorCode errorCode) {
		return new CommonResp(errorCode.getCode(), errorCode.getErrorMessage());
	}

	public static CommonResp buildSuccessResp(Object response) {
		return new CommonResp(code_success, null, response);
	}

	public static CommonResp buildSuccessResp() {
		return new CommonResp(code_success, null, null);
	}

	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
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
