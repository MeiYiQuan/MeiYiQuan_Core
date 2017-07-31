package com.salon.backstage.common.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSONObject;
import com.salon.backstage.common.code.endecode.DESUtil;
import com.salon.backstage.common.code.endecode.MD5Util;

/**
 * 用于将传进来的map转换为json并且加密抛出
 * 
 * @author Administrator
 *
 */
public class SendUtil {

	@Autowired
	private static RedisUtil ru;

	/**
	 * 验证签名
	 * 
	 * @param map
	 */
	public static boolean checkSign(Map<String, Object> map, String deviceId) {
		String remoteSign = (String) map.get("sign");
		Long timestamp = Long.parseLong(map.get("timestamp") + "");
		map.remove("token");
		map.remove("sign");
		map.remove(ProjectConstants.USER_ID);
		if (System.currentTimeMillis() - timestamp > (ProjectConstants.TIMESTEMP))
			return false;
		String sign1 = "";
		String sign2 = "";
		if (map != null && map.size() > 0) {
			map.remove("timestamp");
			Set<String> keySet = map.keySet();
			List<String> list = new ArrayList<String>();
			for (String key : keySet) {
				list.add(key);
			}
			Collections.sort(list);
			StringBuffer strBuffer = new StringBuffer();
			for (String s : list) {
				strBuffer.append(s);
			}
			String argStr = strBuffer.toString();
			
			String token = "token";// 临时设定的token，后面需要改掉
			String firstTimeMD5 = MD5Util.GetMD5Code(argStr+"&" + token + "&" + timestamp);
			String secondContent = firstTimeMD5 + "&" + ProjectConstants.TRANSFER_KEY;
			String secondTimeMD5 = MD5Util.GetMD5Code(secondContent);
			sign1 = secondTimeMD5;

			String realtoken = ru.getHashValue(deviceId, "token");
			if (ValidationUtil.isEmpty(token))
				realtoken = "token";
			String realfirstTimeMD5 = MD5Util.GetMD5Code(argStr+"&" + realtoken + "&" + timestamp);
			String realsecondContent = realfirstTimeMD5 + "&" + ProjectConstants.TRANSFER_KEY;
			String realsecondTimeMD5 = MD5Util.GetMD5Code(realsecondContent);
			sign2 = realsecondTimeMD5;
		} 
		if (sign2.equalsIgnoreCase(remoteSign) || sign1.equalsIgnoreCase(remoteSign)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 生成签名
	 * 
	 * @param map
	 * @return
	 */
	public static String generateSign(Map<String, Object> map, String userId) {
		String sign = "";
		if (map != null && map.size() > 0) {
			String timestamp = map.get("timestamp") + "";
			map.remove("timestamp");
			map.remove(ProjectConstants.USER_ID);
			map.remove("token");
			Set<String> keySet = map.keySet();
			List<String> list = new ArrayList<String>();
			for (String key : keySet) {
				list.add(key);
			}
			Collections.sort(list);
			StringBuffer strBuffer = new StringBuffer();
			for (String s : list) {
				strBuffer.append(s);
			}
			String token;// 临时设定的token，后面需要改掉
			try {
				token = ru.getHashValue(userId, "token");
			} catch (Exception e) {
				token = "token";
			}
			if (ValidationUtil.isEmpty(token))
				token = "token";
			strBuffer.append("&" + token + "&" + timestamp);
			String firstTimeMD5 = MD5Util.GetMD5Code(strBuffer.toString());
			String secondContent = firstTimeMD5 + "&" + ProjectConstants.TRANSFER_KEY;
			String secondTimeMD5 = MD5Util.GetMD5Code(secondContent);
			sign = secondTimeMD5;
		}
		return sign;
	}

	/**
	 * 将传进来的map转换为json并加密抛出
	 * 
	 * @throws @since
	 *             1.8
	 */
	public static String getJsonString(Map<String, Object> map, String userId) {
		// map.put("sign", value)
		try {
			// if (map != null && map.size() > 0) {
			Long currentTime = System.currentTimeMillis();
			map.put("timestamp", currentTime);
			String sign = generateSign(map, userId);
			map.put("sign", sign);
			map.put("timestamp", currentTime);
			map.put("token","token");
//			map.put("token",ru.getHashValue(userId,"token"));
			map.put(ProjectConstants.USER_ID,userId);
			String json = JSONObject.toJSONString(map);
			byte[] byteArray = DESUtil.encrypt(json.getBytes("UTF-8"), ProjectConstants.TRANSFER_KEY);
			Map<String, String> sendMap = new HashMap<String, String>();
			sendMap.put(ProjectConstants.BEFORE, new BASE64Encoder().encode(byteArray).replace("\n", ""));
			return JSONObject.toJSONString(sendMap);
			// }
			// return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static JSONObject decryption(String content) {
		JSONObject obj=new JSONObject();
		try {
			byte[] base = new BASE64Decoder().decodeBuffer(content);
			// 解密获得result
			byte[] bs = DESUtil.decrypt(base, ProjectConstants.TRANSFER_KEY);
			String result = new String(bs, "UTF-8");
			// 转化成jsonobject对象
			obj = JSONObject.parseObject(result);
			obj.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("code", "60");
			obj.put("response", "解码失败");
		}
		return obj;
	}

	public static void main(String[] args) {
//		String json = "{\"merchantId\":\"merchant1\",\"wedId\":\"wed1\"}";
//		Map<String, Object> map = JSONObject.parseObject(json);
//		System.out.println(getJsonString(map, "user1"));
	//	jiemi("ZrgAAOywwIcwTraCsgdk4Q23MqYLIdEyAWVy91wZvG7Gf/Ycl/z5oOBP2KXBIKV+N9YdzZmNNf6sUbLIzXZIM3lJohol8FQqX7wpBfO0mjYgvZ3r+FOMMYMZaxRzdKXg1gFabMLtKCyXpgCIkb6SUbsSUGQds+tTWCzhI9ENz0zGFuVHUliPUznSmjmgpB5H");
		Map a=new HashMap();
		a.put("data", "1234");
		System.out.println(">>>>>>>>>>>>>>>>>>");
		String str =getJsonString(a,"1234");
		JSONObject js=	decryption(net.sf.json.JSONObject.fromObject(str).getString("content"));
		System.out.println(js.toString());
	}

	public static RedisUtil getRu() {
		return ru;
	}

	public static void setRu(RedisUtil ru) {
		SendUtil.ru = ru;
	}

}
