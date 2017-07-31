package com.salon.backstage.common.code;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSONObject;
import com.salon.backstage.common.code.endecode.DESUtil;
import com.salon.backstage.common.error_code.ApiErrorCode;

public class DESInterceptor implements HandlerInterceptor {

	@Autowired
	private static RedisUtil ru;

	public static RedisUtil getRu() {
		return ru;
	}

	public static void setRu(RedisUtil ru) {
		DESInterceptor.ru = ru;
	}

	@SuppressWarnings("restriction")
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// String deviceId = "";
		String userId = "";
		// String ip = NetworkUtil.getIpAddr(request);
		try {
			// 获取json字符串(base64未解码)
			Map map = request.getParameterMap();
			String json = request.getParameter(ProjectConstants.BEFORE);
			byte[] base = new BASE64Decoder().decodeBuffer(json);
			// 解密获得result
			byte[] bs = DESUtil.decrypt(base, ProjectConstants.TRANSFER_KEY);
			String result = new String(bs, "UTF-8");
			// 转化成jsonobject对象
			JSONObject obj = null;
			try {
				obj = JSONObject.parseObject(result);
				userId = obj.getString(ProjectConstants.USER_ID);
				// deviceId = obj.getString(ProjectConstants.DEVICE_ID);
				if (ValidationUtil.isEmpty(userId)) {
					PrintWriter pw = response.getWriter();
					pw.print(SendUtil.getJsonString(new ResponseMap(ApiErrorCode.no_user_id).generateMap(), userId));
					pw.flush();
					pw.close();
					return false;
				}
			} catch (Exception e) {
				PrintWriter pw = response.getWriter();
				pw.print(SendUtil.getJsonString(new ResponseMap(ApiErrorCode.exception).generateMap(), userId));
				pw.flush();
				pw.close();
				return false;
			}

			String remoteToken = obj.get("token") + "";
			String realtoken = ru.getHashValue(userId, "token");

			if ((!remoteToken.equals(realtoken)) && (!remoteToken.equalsIgnoreCase("token"))) {
				PrintWriter pw = response.getWriter();
				pw.print(SendUtil.getJsonString(new ResponseMap(ApiErrorCode.login_retry).generateMap(), userId));
				pw.flush();
				pw.close();
				return false;
			}
			if (obj != null && !obj.isEmpty() && SendUtil.checkSign(obj, userId)) {
				Set<Entry<String, Object>> set = obj.entrySet();
				for (Entry<String, Object> en : set) {
					request.setAttribute(en.getKey(), en.getValue());
				}
				request.setAttribute(ProjectConstants.USER_ID, userId);
				return true;
			} else {
				PrintWriter pw = response.getWriter();
				pw.print(SendUtil.getJsonString(new ResponseMap(ApiErrorCode.sign_error).generateMap(), userId));
				pw.flush();
				pw.close();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			PrintWriter pw = response.getWriter();
			pw.print(SendUtil.getJsonString(new ResponseMap(ApiErrorCode.exception).generateMap(), userId));
			pw.flush();
			pw.close();
			return false;
		}
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
