package com.salon.backstage.qcproject.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * 作者：齐潮
 * 创建日期：2017年1月17日
 * 类说明：用于发送短信验证码
 */
public class SmsUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(SmsUtil.class);

	private static String serverUrl = null;
	
	private static String appKey = null;
	
	private static String appSecret = null;
	
	private static String smsFreeSignName = null;
	
	private static String product = null;
	
	private static String smsTemplateCode = null;
	
	private static TaobaoClient client = null;
	
	static{
		InputStream inputStream = SmsUtil.class.getClassLoader().getResourceAsStream("config.properties");
		Properties p = new Properties();
		try {
			p.load(new InputStreamReader(inputStream, "utf-8"));
			serverUrl = p.getProperty("sms.serverUrl");
			appKey = p.getProperty("sms.appKey");
			appSecret = p.getProperty("sms.appSecret");
			smsFreeSignName = p.getProperty("sms.smsFreeSignName");
			product = p.getProperty("sms.smsParam.product");
			smsTemplateCode = p.getProperty("sms.smsTemplateCode");
			client = new DefaultTaobaoClient(serverUrl, appKey, appSecret);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 向指定的手机号码发送验证码，返回true表示发送成功，返回false表示发送失败
	 * @param phone
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final static boolean send(String phone,String code){
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName(smsFreeSignName);
		req.setSmsParamString("{\"code\":\"" + code + "\",\"product\":\"" + product + "\"}");
		req.setRecNum(phone);
		req.setSmsTemplateCode(smsTemplateCode);
		String result = null;
		try {
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			result = rsp.getBody();
		} catch (ApiException e) {
			e.printStackTrace();
			PrintUtil.printException("发送验证码异常", logger, e);
		}
		if(result==null||result.trim().equals(""))
			return false;
		Map<String,Object> map = JSONObject.parseObject(result, Map.class);
		Object succesObj = map.get("alibaba_aliqin_fc_sms_num_send_response");
		if(succesObj==null)
			return false;
		map = JSONObject.parseObject(succesObj.toString(), Map.class);
		map = JSONObject.parseObject(map.get("result").toString(), Map.class);
		boolean b = Boolean.parseBoolean(map.get("success").toString());
		return b;
	}
	public static void main(String[] args) {
		SmsUtil.send("17600717226",  "1234");
	}
	
}
