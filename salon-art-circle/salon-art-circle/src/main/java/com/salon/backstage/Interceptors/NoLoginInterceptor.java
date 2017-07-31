package com.salon.backstage.Interceptors;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.DESUtil;
import com.salon.backstage.qcproject.util.Send;

import sun.misc.BASE64Decoder;

/**
 * 作者：齐潮
 * 创建日期：2017年1月10日
 * 类说明：不需要登陆就可以访问的拦截器，这里可以传入系统默认的token
 */
public class NoLoginInterceptor implements HandlerInterceptor {
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(Send.TYPE==Send.TYPE_GERNER){
			Map<String, Object> map = HttpRequstParmsUtil.getRequestBodyForMap(request);
			request.setAttribute(Send.JSON_NAME, map);
			return true;
		}
		
		Map<String, Object> json = HttpRequstParmsUtil.getRequestBodyForMap(request);
		String content = json.get(Send.JSON_NAME).toString();
		if(content==null||content.trim().equals("")){
			print(response, Code.init(false, 5, "无数据"));
            return false;
		}
		byte[] baseByte = new BASE64Decoder().decodeBuffer(content);
		byte[] desByte = DESUtil.decrypt(baseByte, Send.DES_KEY);
		String desStr = new String(desByte,"utf-8");
		Map<String,Object> map = JSONObject.parseObject(desStr, Map.class);
		long now = System.currentTimeMillis();
		long timestamp = Long.parseLong(map.get(Send.TIMESTAMP_NAME).toString());
		if(now-timestamp>Send.SEND_MAX_TIME){
			print(response, Code.init(false, 2, "链接超时！"));
            return false;
		}
		boolean vali = Send.validateSign(map, Send.DEFAULT_TOKEN);
		if(vali){
			request.setAttribute(Send.JSON_NAME, map);
			return true;
		}
		print(response, Code.init(false, 1, "签名错误！"));
        return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	
	
	}
	
	/**
	 * 输出信息
	 * @param response
	 * @param code
	 * @throws IOException 
	 */
	private void print(HttpServletResponse response,Code code) throws IOException{
		response.setHeader("Content-type", "application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
        pw.print(Send.sendForInterceptor(code));
        pw.flush();
        pw.close();
	}

}
