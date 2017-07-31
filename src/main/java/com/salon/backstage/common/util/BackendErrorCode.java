package com.salon.backstage.common.util;

public enum BackendErrorCode implements ErrorCode {
	vercode_error(11, "验证码不正确"), //
	already_exist(10, "已存在/不可重复"), //
	not_exist(9, "不存在/或已删除"), //
	missing_parameter(8, "缺少参数/或为空"), //
	exception(7, null), //
	unknown_error(6, "未知错误"), //
	database_error(5, "数据库错误"), //
	account_exist(4, "帐号已存在"), //
	account_not_exist(3, "帐号不存在"), //
	account_disable(2, "帐号被禁用"), //
	account_password_not_match(1, "密码错误"); //
	
	private int code = 0;
	private String errorMessage = null;

	private BackendErrorCode(int code, String errorMessage) {
		this.code = code;
		this.errorMessage = errorMessage;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}
