package com.salon.backstage.common.error_code;

public enum ApiErrorCode implements ErrorCode {
	login_retry(22,"重新登录"),
	parameter_illegal(21,"参数不合法"),
	no_user_id(20,"未传用户ID"),
	no_accept(19,"权限不足"),
	sign_error(18,"签名错误"),
	database_no_data(17,"无数据"),
	sms_send_too_fast(16,"短信发送过于频繁"),
	account_no_auth_code(15,"无短信验证码信息"),
	auth_code_error(14,"短信验证码错误"),
	sms_other_error(13,"短信平台错误"),
	sms_network_error(12,"短信平台网络异常"),
	phone_number_error(11,"手机号码格式错误"),
	already_exist(10, "已存在/不可重复"), //
	not_exist(9, "不存在/或已删除"), //
	missing_parameter(8, "缺少参数/或为空"), //
	exception(7, "异常"), //
	unknown_error(6, "未知错误"), //
	database_error(5, "数据库错误"), //
	account_exist(4, "帐号已存在"), //
	account_not_exist(3, "帐号不存在"), //
	account_disable(2, "帐号被禁用"), //
	account_password_not_match(1, "密码错误"), //
	success(0,"数据正常返回");
	private int code = 0;
	private String errorMessage = null;

	private ApiErrorCode(int code, String errorMessage) {
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
