package com.salon.backstage.common.aliyun.api.gateway.demo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.salon.backstage.common.aliyun.api.gateway.demo.constant.Constants;
import com.salon.backstage.common.aliyun.api.gateway.demo.enums.Method;

public class SendMsg {
	private final static String APP_KEY = "23446840"; // AppKey从控制台获取
	private final static String APP_SECRET = "c0eee634729ba4f13fb84cea64e258b0"; // AppSecret从控制台获取
	private final static String SIGN_NAME = "机械城"; // 签名名称从控制台获取，必须是审核通过的
	private final static String TEMPLATE_CODE = "SMS_14220700"; // 模板CODE从控制台获取，必须是审核通过的
	private final static String HOST = "sms.market.alicloudapi.com"; // API域名从控制台获取
	
	// @phoneNum: 目标手机号，多个手机号可以逗号分隔;
	// @params: 短信模板中的变量，数字必须转换为字符串，如短信模板中变量为${no}",则参数params的值为{"no":"123456"}
	public String sendMsg(String phoneNum, String params) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("/singleSendSms?");
		stringBuffer.append("SignName=" + SIGN_NAME);
		stringBuffer.append("&TemplateCode=" + TEMPLATE_CODE);
		stringBuffer.append("&RecNum=" + phoneNum);
		stringBuffer.append("&ParamString=" + params);
		Request request = new Request(Method.GET, "http://" + HOST + stringBuffer.toString(), APP_KEY, APP_SECRET,
				Constants.DEFAULT_TIMEOUT);
		try {
			HttpResponse response = Client.execute(request);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	public static void main(String agrs[]) {
		SendMsg msg = new SendMsg();
		String res = msg.sendMsg("17710663181", "{'msg':'1111'}");
		System.out.print(res);
	}
}
