package com.salon.backstage.homepage.pay.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.model.Charge;
import com.qc.util.Associate;
import com.qc.util.MathUtil;
import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.common.util.HttpConnecter;
import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.common.util.NetworkUtil;
import com.salon.backstage.common.util.Validate;
import com.salon.backstage.homepage.pay.service.PayService;
import com.salon.backstage.pub.bsc.dao.po.CouponUser;
import com.salon.backstage.pub.bsc.dao.po.Order;
import com.salon.backstage.pub.bsc.dao.po.Sys;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.pub.bsc.domain.Constant.PayStyle;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.Statics;


/**
 * 作者：齐潮
 * 创建日期：2016年12月21日
 * 类说明：处理支付请求
 */
@Controller
@RequestMapping(value="pay")
public class PayAction {
	
	private final static Logger logger = LoggerFactory.getLogger(PayAction.class);

//	@Resource(name="projectProperties")
//	private Properties properties;
	
	@Autowired
	private PayService ps;
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	
	/**
	 * 代表是否已经对app的一些参数进行了赋值
	 */
//	private static boolean index = false;
	
	/**
	 * Ping++上设置的appId
	 */
	private static String appId;
	
	/**
	 * 币种类型，一般为cny
	 */
	private static String currency;
	
	/**
	 * 订单名称(暂时统一)
	 */
	private static String orderName;
	
	/**
	 * 订单body内容(暂时统一)
	 */
	private static String orderBody;
	
	/**
	 * 是否可以使用IOS内购，1表示可以，2表示不可以
	 */
	public static int isIosBuy = 1;
	
	static{
		InputStream inputStream = PayAction.class.getClassLoader().getResourceAsStream("config.properties");
		Properties p = new Properties();
		try {
			p.load(new InputStreamReader(inputStream, "utf-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isIosBuy = (p.getProperty("isIosBuy")==null||p.getProperty("isIosBuy").equals(""))?1:Integer.parseInt(p.getProperty("isIosBuy"));
		Pingpp.apiKey = "sk_live_KCWTyLiHC4O8XzrDu94ev9eL";
//		Pingpp.apiKey = "sk_test_XXrDuTvjD8uTrH8a1C54KOOO";
//		Pingpp.privateKeyPath = "D:\\qc\\apache-tomcat-7.0.73\\webapps\\salon-art-circle\\pingPrivateKey.pem";
		Pingpp.privateKeyPath = "D:\\salon\\pingPrivateKey.pem";
		PayAction.appId = "app_SuLC0CnLqjX9GS0C";
		PayAction.currency = "cny";
		PayAction.orderName = "美艺圈订单";
		PayAction.orderBody = "欢迎购买美艺圈教程";
	}
	
	/**
	 * 将传入的map补充完整，给其附加上app和currency参数。只用于ping++
	 * @param map
	 */
	private void pingPPpacking(Map<String,Object> map){
		if(map==null)
			return;
//		if(!PayAction.index){
//			Pingpp.apiKey = properties.getProperty("ping.apiKey");
//			Pingpp.privateKeyPath = properties.getProperty("ping.privateKeyPath");
//			PayAction.appId = properties.getProperty("ping.appId");
//			PayAction.currency = properties.getProperty("ping.currency");
//			PayAction.orderName = properties.getProperty("ping.orderName");
//			PayAction.orderBody = properties.getProperty("ping.orderBody");
//			PayAction.index = true;
//		}
		Map<String, String> app = new HashMap<String, String>();
		app.put("id", PayAction.appId);
		map.put("app", app);
		map.put("currency",PayAction.currency);
		map.put("subject", PayAction.orderName);
		map.put("body", PayAction.orderBody);
	}
	
	/**
	 * 即将支付，获取订单，ping++返回charge信息，支付宝，ios返回相应信息
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="willPay",method=RequestMethod.POST)
	public Object willPay(HttpServletRequest request) throws Exception{
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(MobileMessageCondition.addCondition(false, 100, "未知错误！", null));
		if(Associate.isDoing(ip, "pay_willPay"))
			return Send.send(MobileMessageCondition.addCondition(false, 101, "正在生成订单，请稍后...", null));
		
		Associate.save(ip, "pay_willPay");
		
		MobileMessage result = willPaySupport(ip,request);
		
		Associate.clear(ip, "pay_willPay");
		
		return Send.send(result);
	}
	
	/**
	 * 立即支付的核心方法，即直接产生订单并返回给前台相应信息，已经对支付方式进行了判断
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	private MobileMessage willPaySupport(String userIp,HttpServletRequest request) throws Exception{
		
		MobileMessage result = paySupport(request);
		
		if(!result.isResult())
			return result;
		Order order = JSONObject.parseObject(result.getResponse().toString(), Order.class);
		// 0元订单的处理方法
		if(order.getPrice()==0){
			result = ps.iosHook(order);
			if(!result.isResult())
				return result;
			Map<String,Object> response = new HashMap<String,Object>();
			response.put("index", 1);
			response.put("charge", "");
			result.setResponse(response);
		}
		PayStyle payStyle = PayStyle.getPayStyle(order.getPay_type());
		result = payStyleDoing(userIp,order, payStyle);
		return result;
	}
	
	/**
	 * 传入订单和支付方式，来进行不同的操作
	 * @param order
	 * @param payStyle
	 * @return
	 */
	private MobileMessage payStyleDoing(String userIp,Order order,PayStyle payStyle){
		int payType = payStyle.getPayType();
		MobileMessage result = null;
		
		
		try {
			switch(payType){
				case Constant.PAY_PINGPP:
					// ping++支付方式
					result = payPingPP(userIp,order, payStyle);
					break;
				case Constant.PAY_ALIPAY:
					// 支付宝原生支付方式
					result = payAliPay(order, payStyle);
					break;
				case Constant.PAY_IOS:
					// ios内购方式
					result = payIOS(order, payStyle);
					break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return MobileMessageCondition.addCondition(false, 170, "支付异常！", null);
		}
		return result;
	}
	
	/**
	 * ping++方式支付的处理机制
	 * @param userIp
	 * @param order
	 * @param payStyle
	 * @return
	 * @throws ChannelException 
	 * @throws APIException 
	 * @throws APIConnectionException 
	 * @throws InvalidRequestException 
	 * @throws AuthenticationException 
	 */
	private MobileMessage payPingPP(String userIp,Order order,PayStyle payStyle) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException{
		Map<String,Object> chargeMap = new HashMap<String,Object>();
		int fen = (int) (order.getPrice()*100);
		chargeMap.put("amount",fen);
		chargeMap.put("order_no",order.getOrder_num());
		chargeMap.put("channel",payStyle.getValue());
		chargeMap.put("client_ip", userIp);
		if(order.getPrice()==0){
			Map<String,Object> response = new HashMap<String,Object>();
			response.put("index", 3);
			return MobileMessageCondition.addCondition(true, 0, null, response);
		}
		if(order.getType()==Statics.ORDER_TYPE_ACTIVITY){
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MILLISECOND, Integer.parseInt(Statics.ORDER_ACTIVITY_EXTIME+""));
			long timestamp = c.getTime().getTime();
			chargeMap.put("time_expire",timestamp/1000);
			System.out.println(chargeMap.get("time_expire").toString());
		}
		
		
		pingPPpacking(chargeMap);
		System.out.println(chargeMap.get("time_expire").toString());
		Charge charge = Charge.create(chargeMap);
		Map<String,String> metadata = new HashMap<String,String>();
		metadata.put("orderType", order.getType() + "");
		metadata.put("shopId", order.getVideo_id());
		charge.setMetadata(metadata);
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("index", 2);
		response.put("charge", charge);
		return MobileMessageCondition.addCondition(true, 0, null, response);
	}
	
	/**
	 * 原生支付宝方式支付的处理机制
	 * @param order
	 * @param payStyle
	 * @return
	 */
	private MobileMessage payAliPay(Order order,PayStyle payStyle){
		return null;
	}
	
	/**
	 * ios专有方式支付的处理机制
	 * @param order
	 * @param payStyle
	 * @return
	 */
	private MobileMessage payIOS(Order order,PayStyle payStyle){
		return null;
	}
	
	/**
	 * ping++支付回调函数
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="webHook",method=RequestMethod.POST)
	public void webHook(HttpServletRequest request,HttpServletResponse response) throws Exception{
		InputStream is = request.getInputStream();
        String jsonStr = new String();
        byte[] byteArray = new byte[1024];
        int readLength = -1;
        while((readLength=(is.read(byteArray))) != -1){
            jsonStr += new String(byteArray,0,readLength,"utf-8");
        }
        net.sf.json.JSONObject responseObj = net.sf.json.JSONObject.fromObject(jsonStr);
        String typeStr = responseObj.getString("type");
        JSONObject dataObj = JSONObject.parseObject(responseObj.get("data").toString());
        if(typeStr.equalsIgnoreCase("charge.succeeded")){
        	JSONObject charge = JSONObject.parseObject(dataObj.get("object").toString());
            
        	Double	amount = MathUtil.toAny(2, charge.getInteger("amount")/100.0);
            String orderNo = charge.getString("order_no");
        	MobileMessage result = ps.webHook(orderNo, amount);
            if(result.isResult()){
                response.setStatus(HttpStatus.SC_OK);
                return;
            }
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * ios内购支付回调
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="iosHook",method=RequestMethod.POST)
	public Object iosBuyHook(HttpServletRequest request) throws Exception{
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(MobileMessageCondition.addCondition(false, 100, "未知错误！", ""));
		if(Associate.isDoing(ip, "iosBuyHook"))
			return Send.send(MobileMessageCondition.addCondition(false, 101, "正在购买...", ""));
		
		Associate.save(ip, "iosBuyHook");
		
		MobileMessage result = iosBuyHookSupport(request);
		
		Associate.clear(ip, "iosBuyHook");
		
		return Send.send(result);
	}
	
	/**
	 * ios内购支付回调核心方法
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	private MobileMessage iosBuyHookSupport(HttpServletRequest request) throws Exception{
		MobileMessage result = paySupport(request);
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		/*System.out.println(json.get("useCoupon"));
		System.out.println(json.get("couponId"));
		if(json.get("couponId").toString()!=null){
			CouponUser couponUser = extraSpringHibernateTemplate.findFirstOneByPropEq(CouponUser.class, "id", json.get("couponId"));
			couponUser.setStatus(2);
			extraSpringHibernateTemplate.getHibernateTemplate().update(couponUser);
			
		}*/
		if(!result.isResult())
			return result;
		Order order = JSONObject.parseObject(result.getResponse().toString(), Order.class);
		result = ps.iosHook(order);
		return result;
	}
	
	/**
	 * 去访问生成订单的接口
	 * @param request
	 * @return
	 */
	private MobileMessage paySupport(HttpServletRequest request){
		
		String projectPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/order/create.do";
		
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		
		Map<String,String> params = new HashMap<String,String>();
		
		params.put("shopId", (json.get("shopId")==null?null:(json.get("shopId").toString())));
		params.put("type", (json.get("type")==null?null:(json.get("type").toString())));
		params.put("payType", (json.get("payType")==null?null:(json.get("payType").toString())));
		params.put("userid", (json.get("userid")==null?null:(json.get("userid").toString())));
		params.put("useCoupon", (json.get("useCoupon")==null?null:(json.get("useCoupon").toString())));
		params.put("couponId", (json.get("couponId")==null?null:(json.get("couponId").toString())));
		params.put("token", (json.get("token")==null?null:(json.get("token").toString())));
		
		MobileMessage result = null;
		
		
		try {
			result = HttpConnecter.post(projectPath, params, Send.MUST_LOGIN_SIGN);
		} catch (Exception e) {
			e.printStackTrace();
			return MobileMessageCondition.addCondition(false, 160, "内部链接异常！", null);
		}
//		if(!result.isResult())
//			return result;
//		Order order = JSONObject.parseObject(result.getResponse().toString(), Order.class);
		return result;
	}
	
	/**
	 * 此接口只用于IOS端，android端不需要访问。
	 * 点击立即支付，来返回一个标识，告诉IOS端要不要显示出IOS内购的支付方式。
	 * 返回1表示需要显示出IOS内购的方式，并且IOS内购有效。
	 * 返回2表示不需要显示出IOS内购的方式，此时强硬使用IOS内购会提示失败
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="isIosBuy",method=RequestMethod.POST)
	public Object getPayTypes(HttpServletRequest request){
		Code result = Code.init(true, 0, "", isIosBuy);
		return Send.send(result);
	}
	
	/**
	 * 点击IOS内购，来获得点券列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getIOSTypes",method=RequestMethod.POST)
	public Object getIOSTypes(HttpServletRequest request){
		Code result = ps.getIosTypeList();
		return Send.send(result);
	}
	
	/**
	 * ios充值成功后的回调，如果没有充值成功，则不会访问这个接口
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="iosWebHook",method=RequestMethod.POST)
	public Object iosWebHook(HttpServletRequest request){
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(MobileMessageCondition.addCondition(false, 100, "未知错误！", ""));
		if(Associate.isDoing(ip, "iosWebHook"))
			return Send.send(MobileMessageCondition.addCondition(false, 101, "正在充值...", ""));
		
		Associate.save(ip, "iosWebHook");
		
		Code result = iosWebHookSupport(request);
		
		Associate.clear(ip, "iosWebHook");
		
		return Send.send(result);
	}
	
	/**
	 * ios充值成功回调的核心方法
	 * @param request
	 * @return
	 */
	private Code iosWebHookSupport(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "coinId");
		if(!vali)
			return Code.init(false, 1, "参数丢失！","");
		String coinId = json.get("coinId").toString();
		String userId = json.get(Send.USERID_NAME).toString();
		if(isIosBuy!=1){
			logger.warn("在ios内购关闭的情况下接收到了一个内购回调，用户Id:" + userId + " coinId：" + coinId);
			return Code.init(false, 9, "非法操作！", "");
		}
		Code result = ps.iosWebHook(userId, coinId);
		return result;
	}
	
	
}
