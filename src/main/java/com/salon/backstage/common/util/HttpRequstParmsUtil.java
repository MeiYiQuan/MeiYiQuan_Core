package com.salon.backstage.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salon.backstage.qcproject.util.Send;

import net.sf.json.JSONObject;

/**
 * 将HttpServletRequest传递过来的值
 * 通过流的方式读取
 * 并且封装进JSONObject对象中
 */
public class HttpRequstParmsUtil {
	static Logger logger = LoggerFactory.getLogger("RequstParms>>>>>>>>>>");

	public static JSONObject getParmsForJson(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		InputStream inputStream = null;
		Reader input = null;
		Writer output = new StringWriter();
		try {
			inputStream = request.getInputStream();
			input = new InputStreamReader(inputStream);
			char[] buffer = new char[1024 * 4];
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
			System.out.println(output.toString());
			json = JSONObject.fromObject(output.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 将request中的信息取出，适用于controller
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getParmsForMap(HttpServletRequest request) {
		return getAttri(request);
	}
	
	/**
	 * 将request中的信息取出，适用于拦截器
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getRequestBodyForMap(HttpServletRequest request) {
		return getBody(request);
	}
	
	
	
	private static Map<String, Object> getAttri(HttpServletRequest request){
		Map<String, Object> map = (Map<String, Object>) request.getAttribute(Send.JSON_NAME);
		return map;
	}
	
	private static Map<String, Object> getBody(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		InputStream inputStream = null;
		Reader input = null;
		Writer output = new StringWriter();
		try {
			inputStream = request.getInputStream();
			input = new InputStreamReader(inputStream);
			char[] buffer = new char[1024 * 4];
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
			if(StringUtil.isNullOrBlank(output.toString()))
				return map;
			JSONObject jsonMap = JSONObject.fromObject(output.toString());
			Iterator<String> it = jsonMap.keys();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object obj = jsonMap.get(key);
				map.put(key, obj);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
}












