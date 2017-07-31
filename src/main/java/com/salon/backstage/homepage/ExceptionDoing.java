package com.salon.backstage.homepage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.qcproject.util.Send;

/**
 * 作者：齐潮
 * 创建日期：2016年12月26日
 * 类说明：异常处理
 */
public class ExceptionDoing extends SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
//		Associate.exception();
		response.setHeader("Content-type", "application/json");
		response.setCharacterEncoding("UTF-8");
		ex.printStackTrace();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			MobileMessage result = MobileMessageCondition.addCondition(false, 1, "系统异常！", null);
			out.print(Send.sendForInterceptor(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(out!=null){
			out.flush();
			out.close();
		}
		return null;
	}
	
}
