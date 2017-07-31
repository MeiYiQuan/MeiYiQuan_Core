package com.salon.backstage.homepage.order.action;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qc.util.Associate;
import com.qc.util.MathUtil;
import com.qc.util.MathUtil.IsInt;
import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.common.util.NetworkUtil;
import com.salon.backstage.common.util.Validate;
import com.salon.backstage.homepage.order.service.OrderService;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.pub.bsc.domain.Constant.PayStyle;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.Statics;
/**
 * 作者：齐潮
 * 创建日期：2016年12月23日
 * 类说明：处理有关订单的请求
 */
@Controller
@RequestMapping(value="order")
public class OrderAction {

	@Autowired
	private OrderService ord;
	
	/**
	 * 创建一个订单
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="create",method=RequestMethod.POST)
	public Object createOrder(HttpServletRequest request){
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(MobileMessageCondition.addCondition(false, 100, "未知错误！", null));
		if(Associate.isDoing(ip, "order_create",request.getServerName()))
			return Send.send(MobileMessageCondition.addCondition(false, 101, "正在创建，请稍后...", null));
		
		Associate.save(ip, "order_create",request.getServerName());
		
		MobileMessage result = orderSupport(request);
		
		Associate.clear(ip, "order_create",request.getServerName());
		
		return Send.send(result);
	}
	
	/**
	 * 获取订单的实际方法
	 * @param request
	 * @return
	 */
	private MobileMessage orderSupport(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean b = Validate.validate(json, "shopId","type","payType","useCoupon");
		if(!b)
			return MobileMessageCondition.addCondition(false, 99, "参数缺失！", "");
		String shopId = json.get("shopId").toString();
		String type = json.get("type").toString();
		String payType = json.get("payType").toString();
		String userId = json.get("userid").toString();
		String useCoupon = json.get("useCoupon").toString();
		String couponId = json.get("couponId")==null?"":json.get("couponId").toString();
		PayStyle style = PayStyle.getPayStyle(Integer.parseInt(payType));
		
		IsInt isUseCoupon = MathUtil.isToInteger(useCoupon);
		IsInt isType = MathUtil.isToInteger(type);
		
		if(style==null||!isUseCoupon.is||(isUseCoupon.value!=Constant.YES_INT&&isUseCoupon.value!=Constant.NO_INT)
				||(isUseCoupon.value==Constant.YES_INT&&couponId.trim().equals(""))
				||!isType.is||(isType.value!=Statics.ORDER_TYPE_ACTIVITY&&isType.value!=Statics.ORDER_TYPE_VIDEO))
			return MobileMessageCondition.addCondition(false, 98, "参数不合法！", "");
		MobileMessage result = ord.createOrder(userId, isUseCoupon.value, couponId, shopId, isType.value, style);
		return result;
	}
	
}
