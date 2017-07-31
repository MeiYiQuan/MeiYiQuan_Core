package com.salon.backstage.common.util;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSONObject;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.DESUtil;
import com.salon.backstage.qcproject.util.Send;

import sun.misc.BASE64Decoder;

/**
 * 作者：齐潮
 * 创建日期：2016年12月23日
 * 类说明：用于链接外部接口
 */
public class HttpConnecter {

	
	/**
	 * 内部用post方法，只能访问本项目的接口
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static MobileMessage post(String url,Map<String,String> params,int type) throws Exception{
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
//        post.addRequestHeader("Content-Type",
//                "application/x-www-form-urlencoded;charset=utf-8");// 在头文件中设置转码  
		/*
        Set<Entry<String, String>> set = params.entrySet();
        NameValuePair[] data = new NameValuePair[params.size()];
        int i = 0;
        for(Entry<String, String> entry:set){
        	data[i] = new NameValuePair(entry.getKey(), entry.getValue());
        	i++;
        }
        */
		post.addRequestHeader("Content-Type",
              "application/json; charset=utf-8");
		String jsonSend = null;
		if(Send.TYPE==Send.TYPE_GERNER){
			jsonSend = JSONObject.toJSONString(params);
		}else{
			if(type==Send.NO_LOGIN_SIGN){
				jsonSend = Send.send(params).toString();
			}else if(type==Send.MUST_LOGIN_SIGN){
				jsonSend = Send.send(params,params.get(Send.TOKEN_NAME)).toString();
			}else{
				throw new Exception("使用内部post请求时传入了无法识别的type参数");
			}
			
		}
        post.setRequestBody(jsonSend);
        
        client.executeMethod(post);
        String str = new String(post.getResponseBodyAsString().getBytes("utf-8"));
        post.releaseConnection();
        if(str==null||str.trim().equals(""))
        	return MobileMessageCondition.addCondition(false, 150, "内部链接错误！", "");
        if(!str.trim().substring(0, 1).equals("{"))
        	return MobileMessageCondition.addCondition(false, 999, "无服务！", "");
        
        if(Send.TYPE==Send.TYPE_GERNER)
        	return JSONObject.parseObject(str, MobileMessage.class);
        
        Map<String,Object> map = JSONObject.parseObject(str, Map.class);
        String content = map.get(Send.JSON_NAME).toString();
        byte[] baseByte = new BASE64Decoder().decodeBuffer(content);
		byte[] desByte = DESUtil.decrypt(baseByte, Send.DES_KEY);
		String desStr = new String(desByte,"utf-8");
		Map<String,Object> contentMap = JSONObject.parseObject(desStr, Map.class);
		long now = System.currentTimeMillis();
		long timestamp = Long.parseLong(contentMap.get(Send.TIMESTAMP_NAME).toString());
		if(now-timestamp>Send.SEND_MAX_TIME)
			return MobileMessageCondition.addCondition(false, 151, "内部链接超时！", "");
		boolean vali = Send.validateSign(contentMap, Send.DEFAULT_TOKEN);
		if(!vali)
			return MobileMessageCondition.addCondition(false, 152, "内部链接签名错误！", "");
		
		String resultStr = JSONObject.toJSONString(contentMap);
        MobileMessage result = JSONObject.parseObject(resultStr, MobileMessage.class);
		return result;
	}
	
}
