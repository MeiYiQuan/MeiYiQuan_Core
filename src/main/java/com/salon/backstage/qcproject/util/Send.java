package com.salon.backstage.qcproject.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSONObject;
import com.qc.util.MathUtil;
import com.qc.util.MathUtil.IsInt;

import sun.misc.BASE64Encoder;

/**
 * 作者：齐潮
 * 创建日期：2017年1月9日
 * 类说明：用于发送信息和对信息的接收以及验证
 */
public class Send {

	/**
	 * 表示不需要登陆就能访问的接口，这里只进行没有登陆时默认token的认证，如果传入了登陆后的token会不通过
	 */
	public final static int NO_LOGIN_SIGN = 1;
	
	/**
	 * 表示需要登陆才能访问的接口，必须传入登陆后的token
	 */
	public final static int MUST_LOGIN_SIGN = 2;
//	
//	/**
//	 * 表示可以传未登陆或者登陆的token，均可以通过验证
//	 */
//	public final static int ALL_SIGN = 3;
	
	/**
	 * 用于表示userId的字段
	 */
	public final static String USERID_NAME = "userid";
	
	/**
	 * 用于保存签名的字段
	 */
	public final static String SIGN_NAME = "sign";
	
	/**
	 * 用于保存最终json信息的字段，直接可以用来发送
	 */
	public final static String JSON_NAME = "content";
	
	/**
	 * 用于表示时间搓的字段
	 */
	public final static String TIMESTAMP_NAME = "timestamp";
	
	/**
	 * 用于表示token的字段
	 */
	public final static String TOKEN_NAME = "token";
	
	/**
	 * 当访问的接口不需要登陆时，需要传入这个token
	 */
	public final static String DEFAULT_TOKEN = "f714765d-faf8-4ef8-8cb3-5350bb9d248b";
	
	/**
	 * 双方约定的签名密钥
	 */
	private final static String SIGN_KEY = "1e86d6bd10de48b59d80afdb6c98ddaf";
	
	/**
	 * 双方约定的des解密密钥
	 */
	public final static String DES_KEY = "a2578c19dd824a9aa8ec38f6f17e1edb";
	
	/**
	 * 发送信息的最大时长，单位毫秒。超过这个时间就报链接超时错误
	 */
	public final static long SEND_MAX_TIME = 1000 * 60 * 10;
	
	/**
	 * 跟据服务器类型来选择不同的换行符
	 */
	private static String CLIENT_HUANHANG = null;
	
	/**
	 * 信息传输模式，1---加密传输，2---普通传输(不安全，用于测试)。在properties文件中定义。默认加密传输
	 */
	public static int TYPE = Send.TYPE_JIAMI;
	
	/**
	 * 表示传输类型是加密传输
	 */
	public final static int TYPE_JIAMI = 1;
	
	/**
	 * 表示传输类型是普通传输(不安全)
	 */
	public final static int TYPE_GERNER = 2;
	
//	/**
//	 * redis客户端
//	 */
//	@Autowired
//	private static RedisClient redis;
	
	static{
		InputStream inputStream = Send.class.getClassLoader().getResourceAsStream("config.properties");
		Properties p = new Properties();
		try {
			p.load(inputStream);
			Send.CLIENT_HUANHANG = p.getProperty("client.huanhang");
			String type = p.getProperty("send.type");
			IsInt intType = MathUtil.isToInteger(type);
			if(type!=null&&intType.is&&(intType.value==Send.TYPE_JIAMI||intType.value==Send.TYPE_GERNER))
				Send.TYPE = intType.value;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * 验证签名。这个方法自动去除map里的签名和时间搓字段。
	 * map中必须有时间搓字段Send.TIMESTAMP_NAME和签名字段Send.SIGN_NAME。
	 * @param map
	 * @param type
	 * @return
	 */
	public final static boolean validateSign(Map<String,Object> map,String token) {
		boolean result = false;
		long timestamp = 0L;
		String oldSign = null;
		try {
			timestamp = Long.parseLong(map.get(Send.TIMESTAMP_NAME).toString());
			oldSign = map.get(Send.SIGN_NAME).toString();
			map.remove(Send.TIMESTAMP_NAME);
			map.remove(Send.SIGN_NAME);
			String systemSign = Send.createSign(map, token, timestamp);
			if(systemSign.equals(oldSign))
				result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 生成签名。
	 * 需要传入要进行签名的map，用户的token，用于写入签名的当前时间(没有在方法中直接生成是要提高调用灵活性)
	 * @param map
	 * @param token
	 * @param now
	 * @return
	 */
	public final static String createSign(Map<String,Object> map,String token,long now){
		List<String> list = new ArrayList<String>();
		
		String mapStr = JSONObject.toJSONString(map);
		mapStr = mapStr.replace("{", ",").replace("}", ",");
		String[] strs = mapStr.split(",");
		for(String str:strs){
			list.add(str);
		}
		Collections.sort(list);
		StringBuffer sortStr = new StringBuffer();
		for(int i=0;i<list.size();i++){
			sortStr.append(list.get(i));
		}
		String sortResult = sortStr.toString();
		
		String sortMD = MD5Util.GetMD5Code(sortResult);
		String tokenMD = MD5Util.GetMD5Code(token + sortMD + now);
		String sign = MD5Util.GetMD5Code(tokenMD + Send.SIGN_KEY);
		
		return sign;
	}
	
	/**
	 * 用于产生发送信息。
	 * 这里使用的token是双方约定的固定的token
	 * @param obj
	 * @return
	 */
	public final static Object send(Object obj){
		if(Send.TYPE==Send.TYPE_GERNER)
			return generSend(obj);
		return jiamiSend(obj,null);
	}
	
	/**
	 * 用于产生发送信息。
	 * 这里可以使用自定义token，token传null就会使用系统token
	 * @param obj
	 * @return
	 */
	public final static Object send(Object obj,String token){
		if(Send.TYPE==Send.TYPE_GERNER)
			return generSend(obj);
		return jiamiSend(obj,token);
	}
	
	/**
	 * 这个send适用于用PrintWriter发送信息的方式，多用于interceptor和exceptiondoing
	 * @param obj
	 * @return
	 */
	public final static String sendForInterceptor(Object obj){
		if(Send.TYPE==Send.TYPE_GERNER)
			return JSONObject.toJSONString(obj);
		return jiamiSend(obj,null);
	}
	
	/**
	 * 普通send
	 * @param obj
	 * @return
	 */
	public final static Object generSend(Object obj){
		return obj;
	}
	
	/**
	 * 加密时的send
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private final static String jiamiSend(Object obj,String token){
		long now = System.currentTimeMillis();
		String mStr = null;
		if(obj==null){
			Code c = Code.init(false, 1, "未知错误！");
			mStr = JSONObject.toJSONString(c);
		}else{
			mStr = JSONObject.toJSONString(obj);
		}
		
		Map<String,Object> m = JSONObject.parseObject(mStr, Map.class);
		String myToken = token==null?Send.DEFAULT_TOKEN:token;
		String sign = Send.createSign(m, myToken, now);
		m.put(Send.SIGN_NAME, sign);
		m.put(Send.TIMESTAMP_NAME, now);
		String mResult = JSONObject.toJSONString(m);
		byte[] mByte = null;
		try {
			mByte = mResult.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] desByte = DESUtil.encrypt(mByte, Send.DES_KEY);
		String sendCentent = new BASE64Encoder().encode(desByte).replace(Send.CLIENT_HUANHANG, "");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put(Send.JSON_NAME, sendCentent);
//		String result = "{\"" + Send.JSON_NAME + "\":\"" + sendCentent + "\"}";
		return JSONObject.toJSONString(resultMap);
	}
	
	public static void main(String[] args){
//		System.out.println(UUID.randomUUID().toString());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userid", "asdfasdfadsf");
		Map<String,Object> child = new HashMap<String,Object>();
		child.put("value", 123);
		map.put("childs", child);
		
		System.out.println(JSONObject.toJSONString(map));
		
		String result = Send.send(map).toString();
		System.out.println(result);
		
		/*
		
		String token = Send.DEFAULT_TOKEN;
		long now = System.currentTimeMillis();
		
		String sign = Send.createSign(map, token, now);
		System.out.println(sign);
		
		map.put(Send.SIGN_NAME, sign);
		map.put(Send.TIMESTAMP_NAME, now);
		
		boolean validate = Send.validateSign(map, Send.MUST_LOGIN_SIGN);
		System.out.println(validate);
		*/
		
	}
	
}
