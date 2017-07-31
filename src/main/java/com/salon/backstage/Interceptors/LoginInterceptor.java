package com.salon.backstage.Interceptors;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.salon.backstage.common.cache.RedisClient;
import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.DESUtil;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.Statics;

import sun.misc.BASE64Decoder;

/**
 * 作者：齐潮
 * 创建日期：2017年1月11日
 * 类说明：需要登陆以后才能访问的接口拦截
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private RedisClient redis;
	
	@Autowired
	private ObjectDao od;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(Send.TYPE==Send.TYPE_GERNER){
			// 一般模式，这里不存在解密操作
			Map<String, Object> map = HttpRequstParmsUtil.getRequestBodyForMap(request);
			Object userId = map.get(Send.USERID_NAME);
			Object userToken = map.get(Send.TOKEN_NAME);
			if(userId==null||userToken==null||userId.equals("")||userToken.equals("")){
				print(response, Code.init(false, 3, "系统参数丢失！"));
	            return false;
			}
			String systemUserToken = redis.getHash(Statics.REDIS_TOKEN_HASH, userId.toString());
			if(systemUserToken==null||systemUserToken.trim().equals("")){
				print(response, Code.init(false, 333, "登陆已经过期！"));
	            return false;
			}
			if(systemUserToken.equals(userToken)){
				User user = od.getObjById(User.class, userId.toString());
				if(user==null){
					print(response, Code.init(false, 333, "该用户不存在，请重新登陆！"));
					return false;
				}
				if(user.getUser_state()!=Constant.YES_INT){
					print(response, Code.init(false, 333, "该用户已经被拉黑，请联系管理员！"));
					return false;
				}
				request.setAttribute(Send.JSON_NAME, map);
				return true;
			}
			print(response, Code.init(false, 333, "该账户已经在别处登录！"));
            return false;
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
		
		Object userId = map.get(Send.USERID_NAME);
		Object userToken = map.get(Send.TOKEN_NAME);
		if(userId==null||userToken==null||userId.equals("")||userToken.equals("")){
			//request.setAttribute(Send.JSON_NAME, map);
			print(response, Code.init(false, 3, "参数丢失！"));
            return false;
		}
		String systemUserToken = redis.getHash(Statics.REDIS_TOKEN_HASH, userId.toString());
		if(systemUserToken==null||systemUserToken.trim().equals("")){
			print(response, Code.init(false, 333, "登陆已经过期！"));
            return false;
		}
		if(!systemUserToken.equals(userToken)){
			print(response, Code.init(false, 333, "该账户已经在别处登录！"));
            return false;
		}
		
		
		boolean vali = Send.validateSign(map, systemUserToken);
		if(vali){
			User user = od.getObjById(User.class, userId.toString());
			if(user==null){
				print(response, Code.init(false, 333, "该用户不存在，请重新登陆！"));
				return false;
			}
			if(user.getUser_state()!=Constant.YES_INT){
				print(response, Code.init(false, 333, "该用户已经被拉黑，请联系管理员！"));
				return false;
			}
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
