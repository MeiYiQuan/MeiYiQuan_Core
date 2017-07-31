package com.salon.backstage.user.web.action;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.Now;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qc.util.Associate;
import com.qc.util.DateFormate;
import com.qc.util.MathUtil;
import com.qc.util.MathUtil.IsInt;
import com.salon.backstage.common.cache.RedisClient;
import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.MD5Util;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.common.util.NetworkUtil;
import com.salon.backstage.common.util.StringUtil;
import com.salon.backstage.common.util.Validate;
import com.salon.backstage.homepage.coupon.service.ICouponService;
import com.salon.backstage.homepage.point.service.IPointService;
import com.salon.backstage.homepage.push.service.PushService;
import com.salon.backstage.pub.bsc.dao.po.Point;
import com.salon.backstage.pub.bsc.dao.po.PointMost;
import com.salon.backstage.pub.bsc.dao.po.SuggestionType;
import com.salon.backstage.pub.bsc.dao.po.Sys;
import com.salon.backstage.pub.bsc.dao.po.Teacher;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.pub.bsc.domain.Constant.PointEachType;
import com.salon.backstage.pub.bsc.domain.Constant.UserType;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.service.ActivityServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.SmsUtil;
import com.salon.backstage.qcproject.util.Statics;
import com.salon.backstage.user.IUserService;


@Controller
@SuppressWarnings("all")
@RequestMapping("/user")
public class UserLoginAction {
	Logger log = LoggerFactory.getLogger(UserLoginAction.class);
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private IUserService userService;
	@Autowired
	private ObjectDao od;
	@Autowired
	private MobileMessage mobileMessage;
	@Autowired
	private IPointService pointService;
	@Autowired
	private PushService ps;
	@Autowired
	private ICouponService couponService;
	@Autowired
	private ActivityServiceNEW as;

	/**
	 * 退出登陆
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/exitLogin", method = RequestMethod.POST)
	public Object exitLogin(HttpServletRequest request) throws Exception {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		Map responseMap = new HashMap();
		String useid = json.get("userid").toString();
		if(useid.equals("")){
			responseMap.put("message", "userid为空");
			return Send.send(MobileMessageCondition.addCondition(true, 1, "失败", responseMap));
		}
		redisClient.setHash(Statics.REDIS_TOKEN_HASH, useid, "", 0, TimeUnit.MINUTES);
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", responseMap));
	}
	
	
	/**
	 * 验证登录
	 * @throws Exception 
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/login.do")
	public Object toLogin(HttpServletRequest request) throws Exception {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		/*boolean phoneNull = StringUtil.isNullOrBlank(json.get("phone"));// 判断手机号是否为空
		boolean passwordNull = StringUtil.isNullOrBlank(json.get("password"));// 判断密码是否为空*/
		// 手机号/密码为空不能通过验证
		if (StringUtil.isNullOrBlank(json.get("phone")) || StringUtil.isNullOrBlank(json.get("password"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 1,"您没有输入手机号或者密码", ""));
		}
		/*
		if (StringUtil.isNullOrBlank(json.get("registId"))) {
			return MobileMessageCondition.addCondition(false, 10,"参数缺失", "");
		}
		*/
		boolean b = Validate.validate(json, "equi_type","registId");
		if(!b)
			return Send.send(MobileMessageCondition.addCondition(false, 10,"参数缺失", ""));
		String registId = json.get("registId").toString();
		IsInt type = MathUtil.isToInteger(json.get("equi_type").toString());
		if(!type.is||(type.value!=Constant.USER_PHONETYPE_IOS&&type.value!=Constant.USER_PHONETYPE_ANDROID))
			return Send.send(MobileMessageCondition.addCondition(false, 11,"参数不合法", ""));
		
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("phone", Long.parseLong(json.get("phone").toString()));
		User user = od.getObjByParams(User.class, params);
		if(user==null)
			return Send.send(MobileMessageCondition.addCondition(false, 2, "您的手机号未注册过",""));
		if(!user.getPassword().equals(MD5Util.encrypt(json.get("password").toString())))
			return Send.send(MobileMessageCondition.addCondition(false, 4, "密码不正确",""));
		if(user.getUser_state()!=Constant.YES_INT)
			return Send.send(MobileMessageCondition.addCondition(false, 8, "该用户已经被拉黑，请联系管理员！",""));
		user.setEqui_type(type.value);
		user.setPush_token(registId);
		user.setLatest_login_time(System.currentTimeMillis());
		od.update(user);
		
		//给用户添加积分
		MobileMessage addPointResult = pointService.addPoint(user, PointEachType.DAY_FIRST_POINT,0);
		if(addPointResult.isResult())
			ps.pushEveryOne("", "签到送积分", Statics.PUSH_TYPE_FORTHWITH_NOTABLE, "恭喜您今天首次登陆获得了" + addPointResult.getResponse() + "个积分，当前已经连续签到" + user.getContinue_day() + "天", null, user.getId().toString());
		// 将用户信息放到Redis中,在判断是否为 用户恶意登陆或者 设备登录的时候使用
		String token = UUID.randomUUID().toString().replace("-", "");
		redisClient.setHash(Statics.REDIS_TOKEN_HASH, user.getId().toString(), token, 60 * 24, TimeUnit.MINUTES);
//		redisClient.set(user.getId() + "token", token, 60 * 24, TimeUnit.MINUTES);
//		redisClient.set(Constant.REDIS_USER, user.getId(), 60 * 24, TimeUnit.MINUTES);
		Map responseMap = new HashMap();
		
		
		// 2017-01-03 给用户添加讲师等级信息
		loginSupport(user, responseMap);
		
		
		//客服电话
		Map<String, Object> params1 = new HashMap<String,Object>();
		params1.put("sys_key","service_phone");
		Sys sys=od.getObjByParams(Sys.class, params1);
		String serverTel= sys.getSys_value();
		
		responseMap.put("user", user);
		responseMap.put("token", token);
		responseMap.put("serverTel", serverTel);
		
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", responseMap));
	}
	
	/**
	 * 第三方登陆
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/sso", method = RequestMethod.POST)
	public Object ssoLogin(HttpServletRequest request) throws Exception {
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(MobileMessageCondition.addCondition(false, 100, "未知错误！", null));
		if(Associate.isDoing(ip, "user_ssoLogin"))
			return Send.send(MobileMessageCondition.addCondition(false, 101, "正在登陆，请稍后...", null));
		
		Associate.save(ip, "user_ssoLogin");
		
		MobileMessage result = ssoSupport(request);
		
		Associate.clear(ip, "user_ssoLogin");
		
		return Send.send(result);
		
		
		
	}
	
	private MobileMessage ssoSupport(HttpServletRequest request) throws Exception{
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean va = Validate.validate(json, "type","ssoId","ssoName","sex","phoneType","registId");
		if(!va)
			return MobileMessageCondition.addCondition(false, 99, "参数缺失！", null);
		int sex = Integer.parseInt(json.get("sex").toString());
		if(sex!=Constant.USER_SEX_MAN&&sex!=Constant.USER_SEX_WOMEN&&sex!=Constant.USER_SEX_UNKNOW)
			return MobileMessageCondition.addCondition(false, 98, "参数不合法！", null);
		int type = Integer.parseInt(json.get("type").toString());
		UserType userType = UserType.getUserType(type);
		if(userType==null||userType==UserType.USER_TYPE_GENERAL)
			return MobileMessageCondition.addCondition(false, 98, "参数不合法！", null);
		int phoneType = Integer.parseInt(json.get("phoneType").toString());
		if(phoneType!=Constant.USER_PHONETYPE_ANDROID&&phoneType!=Constant.USER_PHONETYPE_IOS)
			return MobileMessageCondition.addCondition(false, 98, "参数不合法！", null);
		String ssoId = json.get("ssoId").toString();
		String registId = json.get("registId").toString();
		String ssoName = json.get("ssoName").toString();
		String address = json.get("address")==null?"":json.get("address").toString();
		String picUrl = json.get("picUrl")==null?"":json.get("picUrl").toString();
		MobileMessage result = userService.ssoLogin(ssoId, ssoName, address, picUrl, registId, phoneType, phoneType, sex);
		if(!result.isResult())
			return result;
		// 将用户信息放到Redis中,在判断是否为 用户恶意登陆或者 设备登录的时候使用
		User user = (User) result.getResponse();
		Map<String, Object> params1 = new HashMap<String,Object>();
		params1.put("sys_key","service_phone");
		Sys sys=od.getObjByParams(Sys.class, params1);
		String serverTel= sys.getSys_value();
		String token = UUID.randomUUID().toString().replace("-", "");
		redisClient.setHash(Statics.REDIS_TOKEN_HASH, user.getId().toString(), token, 60 * 24, TimeUnit.MINUTES);
//		redisClient.set(user.getId() + "token", token, 60 * 24, TimeUnit.MINUTES);
//		redisClient.set(Constant.REDIS_USER, user.getId(), 60 * 24, TimeUnit.MINUTES);
		Map responseMap = new HashMap();
		responseMap.put("user", user);
		responseMap.put("token", token);
		responseMap.put("serverTel", serverTel);
		// 2017-01-03 给用户添加讲师等级信息
		loginSupport(user, responseMap);
		
		return MobileMessageCondition.addCondition(true, 0, "成功", responseMap);
	}
	

	/**
	 * 2017-01-03 给用户添加讲师等级信息
	 * @param user
	 * @param responseMap
	 */
	private void loginSupport(User user,Map responseMap){
		if(user.getUser_type()==Constant.USER_TYPE_TEACHER){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("teacher_id", user.getId());
			Teacher teacher = od.getObjByParams(Teacher.class, params);
			if(teacher==null){
				Map<String,Object> settings = new HashMap<String,Object>();
				settings.put("user_type", Constant.USER_TYPE_STUDENT);
				od.updateById(User.class, user.getId().toString(), settings);
				user.setUser_type(Constant.USER_TYPE_STUDENT);
				responseMap.put("teacherLevel", "");
			}else{
				responseMap.put("teacherLevel", (teacher.getLevel()==null?"":teacher.getLevel()));
			}
		}else{
			responseMap.put("teacherLevel", "");
		}
	}
	
	/**
	 * 用户注册
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/register.do")
	public Object register(HttpServletRequest request) {
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(MobileMessageCondition.addCondition(false, 100, "未知错误！", null));
		if(Associate.isDoing(ip, "user_create"))
			return Send.send(MobileMessageCondition.addCondition(false, 101, "正在注册，请稍后...", null));
		
		Associate.save(ip, "user_create");
		
		MobileMessage result = registSupport(request);
		
		Associate.clear(ip, "user_create");
		
		return Send.send(result);
	}
	
	/**
	 * 注册的核心方法
	 * @param request
	 * @return
	 */
	private MobileMessage registSupport(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("phone")) || StringUtil.isNullOrBlank(json.get("password"))) {
			return MobileMessageCondition.addCondition(false, 1,"您没有输入手机号或者密码", "");
		}
		String phone = json.get("phone").toString();
		boolean b = Pattern.compile("^1[3|4|5|8|7]\\d{9}$").matcher(phone).find();
		if(!b)
			return MobileMessageCondition.addCondition(false, 3, "您输入的手机号码格式不对！","");
		String password = json.get("password").toString();
		boolean bpass = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]{6,18}").matcher(password).find();
		if(!bpass)
			return MobileMessageCondition.addCondition(false, 4, "密码只能是6-18位的数字和字母的组合！","");
		// 查看该手机号是否为已注册用户
		User user = userService.queryCountByPhone(json);
		if (user != null) {
			return MobileMessageCondition.addCondition(false, 5, "您的手机号已经注册过","");
		}
		// 验证码非空验证
		if (StringUtil.isNullOrBlank(json.get("idenCode"))) {
			mobileMessage = MobileMessageCondition.addCondition(false, 6, "请您输入验证码", "");
			return mobileMessage;
		}

		// 验证验证码
		// 若集合为空,说明该手机号在Redis中没有存放集合也就是没有获得过验证码
		String code = redisClient.getHash("IdentCode", json.get("phone").toString() + "_regist");
		if (code == null || code.trim().equals("")) {
			return MobileMessageCondition.addCondition(false, 7,"您输入的手机号未获得过验证码", "");
		}
		long phoneFromWeb = Long.valueOf((String) json.get("phone"));
		int idenCodeFromRedis = Integer.parseInt(code);
		int idenCodeFromWeb = Integer.valueOf((String) json.get("idenCode"));
		boolean idenCodeRedisWebEqual = idenCodeFromRedis == idenCodeFromWeb;
		if (!idenCodeRedisWebEqual) {
			return MobileMessageCondition.addCondition(false, 8, "您输入的手机号与验证码不统一", "");
		}
		if (StringUtil.isNullOrBlank(json.get("district"))) {
			return MobileMessageCondition.addCondition(false, 9, "请输入注册区域", "");
		}
		// 手机号 密码 注册区域没有问题执行注册操作
		try {
//			User userReg = userService.register(json);
			MobileMessage createResult = userService.createUser(phoneFromWeb,password, json.get("district").toString());
			if(!createResult.isResult())
				return createResult;
			User userReg = (User) createResult.getResponse();
			mobileMessage = MobileMessageCondition.addCondition(true, 0, "MOB美发学堂注册成功", "");
			// 注册成功,删除Redis中的Map(存放手机号 验证码)
			redisClient.del((String) json.get("phone") + "register");
			return mobileMessage;
			// 遇到异常时的处理
		} catch (Exception e) {
			e.printStackTrace();
			mobileMessage = MobileMessageCondition.addCondition(false, 10, "保存注册时遇到异常", "");
			return mobileMessage;
		}
	}
	
	/**
	 * 获取验证码的接口
	 * phone:手机号
	 */
	@RequestMapping("/getIdenCode.do")
	@ResponseBody
	public Object getIdenCode(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		// 判断手机号是否为空 
		if (StringUtil.isNullOrBlank(json.get("phone"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 11, "手机号不能为空", ""));
		}
		String phone = json.get("phone").toString();
		boolean b = Pattern.compile("^1[3|4|5|8|7]\\d{9}$").matcher(phone).find();
		if(!b)
			return MobileMessageCondition.addCondition(false, 3, "您输入的手机号码格式不对！","");
		Integer idenCode = (int) (Math.random() * 9000 + 1000);
		//	Integer idenCode = 1234;
		// whoNeedIdenCode 0:注册, 1:忘记密码
		int judgeType = Integer.valueOf((String) json.get("whoNeedIdenCode"));
		if (0 == judgeType) { // 用户注册
			// 查看该手机号是否为已注册用户
			User user = userService.queryCountByPhone(json);
			if (user != null) {
				return Send.send(MobileMessageCondition.addCondition(false, 5,"您的手机号已经注册过", ""));
			}
			boolean sendResult = SmsUtil.send(json.get("phone").toString().trim(), idenCode + "");
			if(!sendResult)
				return Send.send(MobileMessageCondition.addCondition(false, 5, "发送失败！请联系客服！", ""));
			redisClient.setHash("IdentCode", json.get("phone").toString() + "_regist", idenCode + "", 500L, TimeUnit.MINUTES);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "发送成功", idenCode));
		} else if (1 == judgeType) { // 忘记密码
			User user = userService.queryCountByPhone(json);// 查看该手机号是否为已注册用户
			if (user == null) {
				return Send.send(MobileMessageCondition.addCondition(false, 2, "您的手机号未注册", ""));
			}
			boolean sendResult = SmsUtil.send(json.get("phone").toString().trim(), idenCode + "");
			if(!sendResult)
				return Send.send(MobileMessageCondition.addCondition(false, 5, "发送失败！请联系客服！", ""));
			redisClient.setHash("IdentCode", json.get("phone").toString() + "_forgetPassword", idenCode + "", 500L, TimeUnit.MINUTES);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "发送成功", idenCode));
		}
		return Send.send(MobileMessageCondition.addCondition(false, 12, "服务器没有合适的方法发送验证码", ""));
	}

	/**
	 * 忘记密码之后修改密码操作
	 * phone:手机号
	 * idenCode:验证码
	 * firstPassword:首次密码
	 * secondPassword:第二次密码
	 */
	@RequestMapping("/forgetPassword.do")
	@ResponseBody
	public Object editPassword(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		// 判断手机号是否为空
		if (StringUtil.isNullOrBlank(json.get("phone"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 11, "手机号不能为空", ""));
		}
		String phone = json.get("phone").toString();
		boolean b = Pattern.compile("^1[3|4|5|8|7]\\d{9}$").matcher(phone).find();
		if(!b)
			return MobileMessageCondition.addCondition(false, 3, "您输入的手机号码格式不对！","");
		// 查看该手机号是否为已注册用户
		User user = userService.queryCountByPhone(json);
		if (user == null) {
			return Send.send(MobileMessageCondition.addCondition(false, 2, "您的手机号尚未注册过",""));
		}
		// 判断验证码是否为空
		if (StringUtil.isNullOrBlank(json.get("idenCode"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 6, "请您输入验证码", ""));
		}
		// 判断两次密码是否为空
		if (StringUtil.isNullOrBlank(json.get("firstPassword")) || StringUtil.isNullOrBlank(json.get("secondPassword"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 13, "请输入要修改的密码",""));
		}

		// 判断两次密码是否一致
		String firstPwd = (String) json.get("firstPassword");
		String secondPwd = (String) json.get("secondPassword");
		if (!firstPwd.equals(secondPwd)) {
			return Send.send(MobileMessageCondition.addCondition(false, 14, "两次输入的密码不一致",""));
		}
		
		String password = json.get("firstPassword").toString();
		boolean bpass = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]{6,18}").matcher(password).find();
		if(!bpass)
			return MobileMessageCondition.addCondition(false, 4, "密码只能是6-18位的数字和字母的组合！","");
		// 验证验证码
		@SuppressWarnings("rawtypes")
		// 若集合为空,说明该手机号在Redis中没有存放集合也就是没有获得过验证码
		String code = redisClient.getHash("IdentCode", json.get("phone").toString() + "_forgetPassword");
		if (code == null || code.trim().equals("")) {
			return MobileMessageCondition.addCondition(false, 7,"您输入的手机号未获得过验证码", "");
		}
		long phoneFromWeb = Long.valueOf((String) json.get("phone"));
		int idenCodeFromRedis = Integer.parseInt(code);
		int idenCodeFromWeb = Integer.valueOf((String) json.get("idenCode"));
		boolean idenCodeRedisWebEqual = idenCodeFromRedis == idenCodeFromWeb;
		if (!idenCodeRedisWebEqual) {
			return Send.send(MobileMessageCondition.addCondition(false, 8,"您输入的手机号与验证码不统一", ""));
		}
		// 以上都没有问题时执行修改密码操作
		try {
			json.put("password", (String) json.get("secondPassword"));
			userService.editPassword(json);
			mobileMessage = MobileMessageCondition.addCondition(true, 0,"success", "");
			// 注册成功,删除Redis中的Map(存放手机号 验证码)
			redisClient.del((String) json.get("phone") + "forgetPassword");
			return Send.send(mobileMessage);
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 15,"保存数据时发送异常错误", ""));
		}
	}

	/**
	 * 查询个人资料
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("all")
	@RequestMapping("/persionalData.do")
	@ResponseBody
	public Object persionalData(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登陆", ""));
		}
		try {
			Map user = userService.persionalDataById(json);
			//客服电话
			Map<String, Object> params1 = new HashMap<String,Object>();
			params1.put("sys_key","service_phone");
			Sys sys=od.getObjByParams(Sys.class, params1);
			String serverTel= sys.getSys_value();
			
			user.put("serverTel", serverTel);
			
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", user));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 17,"查询个人资料时出现错误", ""));
		}
	}

	/**
	 * 更换头像
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/replaceHead.do")
	public Object replaceHead(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("picSaveUrl")) || StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 18,"请登陆或选择要上传的头像", ""));
		}
		try {
			userService.replaceHead(json);//上传头像成功
			User user = od.getObjById(User.class, json.get(Send.USERID_NAME).toString());
			if(user!=null){
				MobileMessage addPointResult = pointService.addPoint(user, PointEachType.USER_PIC_POINT, 0);
				if(addPointResult.isResult())
					ps.pushEveryOne("", "上传头像送积分", Statics.PUSH_TYPE_FORTHWITH_NOTABLE, "恭喜您首次上传头像获得了" + addPointResult.getResponse() + "个积分", null, user.getId().toString());
			}
				
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", ""));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 19, "更换头像时出现异常",""));
		}

	}

	/**
	 * 我的收藏
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("all")
	@RequestMapping("/myCollect.do")
	@ResponseBody
	public Object myCollect(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登陆", ""));
		}
		int type = Integer.valueOf((String)json.get("type"));
		List<Map> rankList = null;
		if(type == 1){//活动
			rankList = userService.queryCountByUserIdAndActivity(json);
			for (Map map : rankList) {
				String enrollBeginTime = map.get("enroll_begin_time").toString();//报名开始时间
				long parseLong = Long.parseLong(enrollBeginTime);
				String enrollEndTime = map.get("enroll_end_time").toString();//活动报名结束时间
				long parseLong2 = Long.parseLong(enrollEndTime);
			    Long prepareStartTime = Long.parseLong(map.get("prepare_start_time").toString());//活动准备时间
				Long prepareEndTime = Long.parseLong(map.get("prepare_end_time").toString());//活动准备结束时间
				Long activityStartTime =Long.parseLong( map.get("activity_start_time").toString());//活动开始时间
				Long activityEndTime =Long.parseLong(map.get("activity_end_time").toString());//活动结束时间
				Long cancelTime = Long.parseLong(map.get("cancel_time").toString());//活动取消时间
			    Long nowDate = Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
				String activityId = (String) map.get("id");//活动ID
				List<Map> statusList = userService.queryActivityStatus(activityId,json);
				for (Map map2 : statusList) {
					 String activityStatus = map2.get("status").toString();
					 if(activityStatus.equals("0")){
						 map.put("activityStatus", "未成功"); 
					 }else{
				  if(nowDate>=parseLong && nowDate<=parseLong2){
					  //报名中
					  map.put("activityStatus", "报名中");
				  }else if(nowDate>=prepareStartTime && nowDate<=prepareEndTime){
					  //准备中
					  map.put("activityStatus", "准备中");
				  }else if(nowDate>=activityStartTime && nowDate<=activityEndTime){
					  //进行中
					  map.put("activityStatus", "进行中");
				  }else if(nowDate>=activityEndTime){
					  //已完成
					  map.put("activityStatus", "已完成");
				  }
				}
				}
			  map.remove("enroll_begin_time");
			  map.remove("enroll_end_time");
			  map.remove("prepare_start_time");
			  map.remove("prepare_end_time");
			  map.remove("activity_start_time");
			  map.remove("activity_end_time");
			  map.remove("cancel_time");
			  Long partActivityPersions = userService.queryCountByAcitityId(activityId);
			  map.put("partActivityPersions", partActivityPersions);
		       }
		}else if(type == 2){//名人大咖
			rankList = userService.queryCountByUserIdAndTeacher(json);
		}else {//视频
			rankList = userService.queryCountByUserIdAndVideo(json);
			for (Map map2 : rankList) {
				String createId = map2.get("creater_id").toString();
				Long videoPlayCount = userService.queryVideoPlayCount(createId);
				map2.put("videoPlayCounts", videoPlayCount);
			}
		}
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", rankList));
	}

	/**
	 * 修改收藏
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("all")
	@RequestMapping("/myCollectEdit.do")
	@ResponseBody
	public Object myCollectEdit(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		Object object = json.get("collectTypeIds");
		boolean collectTypeNull = json.get("collectTypes") == null|| "".equals(json.get("collectTypes"));
		if (StringUtil.isNullOrBlank(json.get("collectTypeIds"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 23, "请选择要删除的收藏",""));
		}
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		try {
			userService.deleteCollectByCollectTypeId(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", ""));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 24, "删除收藏时出现异常",""));
		}
	}

	/**
	 * 查询浏览记录
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/playRecord.do")
	public Object playRecord(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		Map<String, List> map = new HashMap<>();
		try {
			// 最近一周的记录
			List<Map> weeks = userService.queryCollectForWeek(json);
			Map week = null;
			List<Map> wk = new ArrayList<>();
			for (int i = 0; i < weeks.size(); i++) {
				week = weeks.get(i);
				Object continueTime = week.get("continue_time");
				week.put("playRecordPercent", continueTime);
				wk.add(week);
			}
			map.put("weeks", wk);
			// 最近一个月的记录
			List<Map> moth = new ArrayList<>();
			List<Map> months = userService.queryCollectForMonth(json);
			Map month = null;
			if(months.size()==0){
				map.put("months", months);
			}else{
			for (int i = 0; i < months.size(); i++) {
				month = months.get(i);
				Object continueTime = month.get("continue_time");
				month.put("playRecordPercent", continueTime);
				moth.add(month);
			}
			map.put("months", moth);}
			List<Map> mothBefore = new ArrayList<>();
			// 一个月以前的记录
			List<Map> monthBefore = userService.queryCollectForMonthBefore(json);
			Map monthBefores = null;
			if(monthBefore.size()==0){
				map.put("monthBefores", monthBefore);
			}else{
			for (int i = 0; i < monthBefore.size(); i++) {
				monthBefores = monthBefore.get(i);
				Object continueTime = monthBefores.get("continue_time");
				monthBefores.put("playRecordPercent", continueTime);
				mothBefore.add(monthBefores);
			}
			map.put("monthBefores", mothBefore);
			}
			// 计算百分比
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", map));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 25, "查詢播放记录时异常",""));
		}
	}

	/**
	 * 修改用戶的所有信息
	 * 
	 * @param request 
	 *        userid：用户ID 
	 *        gender：用户性别
	 *        district：用户地址
	 *        job：用户职业
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/persionalDataEdit.do")
	public Object persionalDataEditGender(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		try {
			if(!StringUtil.isNullOrBlank(json.get("gender"))){
				//修改性别
				userService.editUserGender(json);
			}
			if(!StringUtil.isNullOrBlank(json.get("district"))){
				//修改地址
				userService.editUserDistrict(json);
			}
			if(!StringUtil.isNullOrBlank(json.get("job"))){
				//修改职业
				userService.editUserJob(json);
			}
			/*
			long pointPersionCounts=pointService.pointPersion(json.get("userid").toString(),4);//判断是否之前添加过积分
			if(pointPersionCounts<1){
				pointService.pointEditPersion(json.get("userid").toString());//获得积分
			}
			*/
			User user = od.getObjById(User.class, json.get(Send.USERID_NAME).toString());
			if(user!=null&&user.getGender()!=Constant.USER_SEX_UNKNOW&&user.getUsername()!=""&&user.getBirthday()!=0L){
				MobileMessage addPointResult = pointService.addPoint(user, PointEachType.USER_DETAIL_POINT, 0);
				if(addPointResult.isResult())
					ps.pushEveryOne("", "完善个人信息送积分", Statics.PUSH_TYPE_FORTHWITH_NOTABLE, "恭喜您首次完善个人信息获得了" + addPointResult.getResponse() + "个积分", null, user.getId().toString());
			}
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", ""));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 27,"修改性別的时候出现异常", ""));
		}

	}

	/**
	 * 修改个人资料星座
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("all")
	@RequestMapping("/persionalDataEditZodiac.do")
	@ResponseBody
	public Object persionalDataEditZodiac(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		if (StringUtil.isNullOrBlank(json.get("zodiac"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 28, "修改的星座不能为空",""));
		}
		try {
			userService.editUserZodiac(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", ""));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 29, "修改星座时出现异常",""));
		}

	}

	/**
	 * 浏览记录编辑
	 * 
	 * @param request
	 * 		  userid：用户ID
	 * 		  playRecordId:浏览记录Id
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/playRecordEdit.do")
	public Object playRecordEdit(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		if (StringUtil.isNullOrBlank(json.get("playRecordId"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 30, "浏览记录不能为空",""));
		}
		try {
			userService.editPlayRecord(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", ""));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 31, "编辑浏览记录时异常",""));
		}

	}

	/**
	 * 我的参与
	 * 
	 * @param request
	 * 		  userid:用户ID
	 * 		  manType:参与者类型
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/myParticipation.do")
	public Object myParticipation(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		if (StringUtil.isNullOrBlank(json.get("manType"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 32, "参与者类型不能为空",""));
		}
		try {
			List<Map> myParticipations = userService.queryMyParticipation(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功",myParticipations));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 33, "查询参与活动出现异常",""));
		}
	}

	/**
	 * 我的参与详情
	 * 
	 * @param request
	 * 		  userid:用户ID
	 * 		  activityId：活动ID
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/myParticipationDetail.do")
	public Object myParticipationDetail(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "activityId");
		if(!vali)
			return Send.send(Code.init(false, 1, "参数丢失！"));
		String activityId = json.get("activityId").toString();
		String userId = json.get(Send.USERID_NAME).toString();
		Code result = as.getActivityDetail(activityId, userId);
		return Send.send(result);
		/*
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		if (StringUtil.isNullOrBlank(json.get("activityId"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 34, "活动id不能为空",""));
		}
		Map<String, List> map = new HashMap<>();
		try {
			// 活动信息详情
			List<Map> activityDetail = userService.queryActivityForDetail(json);
			map.put("activityDetail", activityDetail);
			// 参与者详情
			List<Map> activityParticipatorList = userService.queryActivityForparticipatorList(json);
			map.put("activityParticipatorList", activityParticipatorList);
			// 参与者人数
			List<Map> activityParticipatorCount = userService.queryActivityForparticipatorCount(json);
			map.put("activityParticipatorCount", activityParticipatorCount);
			// 嘉宾详情
			List<Map> guestList = userService.queryActivityForGuest(json);
			map.put("guestList", guestList);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", map));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 35,"查询活动详情时出现异常", ""));
		}
		*/
	}

	/**
	 * 求课程详情
	 * 
	 * @param request
	 * 		  userid:用户ID
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/userVideoRequestDetail.do")
	public Object userVideoRequestDetail(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		try {
			Map userVideoRequests = userService.queryUserVideoRequest(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功",userVideoRequests));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 36, "求课程失败", ""));

		}

	}

	/**
	 * 添加求课程困惑
	 * 
	 * @param request
	 * 		  userid:用户ID
	 * 		  courseName:课程名称
	 * 		  question:困惑
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/userVideoRequestAdd.do")
	public Object userVideoRequestAdd(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		if (StringUtil.isNullOrBlank(json.get("courseName"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 37, "课程名称不能为空",""));
		}
		if (StringUtil.isNullOrBlank(json.get("question"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 38, "困惑不能为空", ""));
		}
		try {
			userService.insertUserVideoRequest(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", ""));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 39, "添加困惑时出现异常",""));
		}

	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 *        userid:用户ID
	 *        password：原始密码
	 *        newPassword：新密码
	 *        rePassword:确认密码
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/setUpEditPassword.do")
	public Object setUpEditPassword(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		if (StringUtil.isNullOrBlank(json.get("password"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 40, "原密码不能为空", ""));
		}
		if (StringUtil.isNullOrBlank(json.get("newPassword"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 41, "新密码不能为空", ""));
		}
		if (json.get("newPassword").toString().length() < 6|| json.get("newPassword").toString().length() > 16) {
			return Send.send(MobileMessageCondition.addCondition(false, 42,"新密码限6-16为字母,数字,字符", ""));
		}
		if (StringUtil.isNullOrBlank(json.get("rePassword"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 43, "确认密码不能为空",""));
		}
		try {
			User user = userService.queryUserById(json);
			String encrypt = MD5Util.encrypt((String) json.get("password"));
			if (!user.getPassword().equals(MD5Util.encrypt((String) json.get("password")))) {
				return Send.send(MobileMessageCondition.addCondition(false, 44, "原密码不正确",""));
			}
			Object object = json.get("rePassword");
			if (!json.get("newPassword").equals(json.get("rePassword"))) {
				return Send.send(MobileMessageCondition.addCondition(false, 45,"新密码和确认密码不一致", ""));
			}
			userService.setUpEditPassword(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", ""));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 46, "修改密码时出现异常",""));
		}
	}
	
	/**
	 * 查询系统信息
	 * @param request
	 * 		  userid:用户ID
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/systemInfo.do")
	public Object systemInfo(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		try {
			List<Map> systemInfos = userService.querySystemInfo(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功",systemInfos));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 47, "查询系统消息时异常",""));
		}
	}
	
	/**
	 * 系统信息详情
	 * @param request
	 * 		  userid:用户ID
	 * 		  systemInfoId:系统信息Id
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/systemInfoDetail.do")
	public Object systemInfoDetail(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登陆", ""));
		}
		if (StringUtil.isNullOrBlank(json.get("systemInfoId"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 48, "消息ID不能为空",""));
		}
		try {
			List<Map> systemInfoDetail = userService.querySystemInfoDetail(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功",systemInfoDetail));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 49,"查询消息详情时出现异常", ""));
		}

	}

	/**
	 * 获得积分
	 * @param request
	 * 		  userid:用户ID
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/pointAll.do")
	public Object pointAll(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登陆", ""));
		}
		try {
			List<Map> pointAll = userService.queryPointAll(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", pointAll));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 50, "查询积分时出现异常",""));
		}
	}
	
	/**
	 * 获取优惠券
	 * @param request
	 * 		  userid:用户ID
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/myCoupon.do")
	public Object myCoupon(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean userIdNull = json.get("userid") == null || "".equals(json.get("userid"));
		if(userIdNull){
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登陆", ""));
		}
		try {
			List<Map> coupons = userService.queryCoupon(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", coupons));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 51, "查询优惠券时出现异常", ""));
		}
	}
	
	/**
	 * 线下活动
	 * @param request
	 * 		  userid:用户ID
	 * @return
	 */
	@SuppressWarnings("all")
	@RequestMapping("/offlineActivity.do")
	@ResponseBody
	public Object offlineActivity(HttpServletRequest request) {
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if (StringUtil.isNullOrBlank(json.get("userid"))) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登陆", ""));
		}
		int type = Integer.valueOf((String)json.get("type"));
		List<Map> rankList = null;
		if(type == 1){//全国
			rankList = userService.queryActivityListForCountry(json);
			for (Map map : rankList) {
				String enrollBeginTime = map.get("enroll_begin_time").toString();//报名开始时间
				long parseLong = Long.parseLong(enrollBeginTime);
				String enrollEndTime = map.get("enroll_end_time").toString();//活动报名结束时间
				long parseLong2 = Long.parseLong(enrollEndTime);
			    Long prepareStartTime = Long.parseLong(map.get("prepare_start_time").toString());//活动准备时间
				Long prepareEndTime = Long.parseLong(map.get("prepare_end_time").toString());//活动准备结束时间
				Long activityStartTime =Long.parseLong( map.get("activity_start_time").toString());//活动开始时间
				Long activityEndTime =Long.parseLong(map.get("activity_end_time").toString());//活动结束时间
				Long cancelTime = Long.parseLong(map.get("cancel_time").toString());//活动取消时间
				Long nowDate = Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
				if(nowDate>=parseLong && nowDate<=parseLong2){
					  //报名中
					  map.put("activityStatus", "报名中");
				  }else if(nowDate>=prepareStartTime && nowDate<=prepareEndTime){
					  //准备中
					  map.put("activityStatus", "准备中");
				  }else if(nowDate>=activityStartTime && nowDate<=activityEndTime){
					  //进行中
					  map.put("activityStatus", "进行中");
				  }else if(nowDate>=cancelTime){
					  //已完成
					  map.put("activityStatus", "已完成");
				  }
				  map.remove("enroll_begin_time");
				  map.remove("enroll_end_time");
				  map.remove("prepare_start_time");
				  map.remove("prepare_end_time");
				  map.remove("activity_start_time");
				  map.remove("activity_end_time");
				  map.remove("cancel_time");
				  String activityId = (String) map.get("id");//活动ID
				  Long partActivityPersions = userService.queryCountByAcitityId(activityId);
				  map.put("partActivityPersions", partActivityPersions);
			}
		}else if(type == 2){//地方
			rankList = userService.queryCountByUserIdAndCity(json);
			for (Map map : rankList) {
				String enrollBeginTime = map.get("enroll_begin_time").toString();//报名开始时间
				long parseLong = Long.parseLong(enrollBeginTime);
				String enrollEndTime = map.get("enroll_end_time").toString();//活动报名结束时间
				long parseLong2 = Long.parseLong(enrollEndTime);
			    Long prepareStartTime = Long.parseLong(map.get("prepare_start_time").toString());//活动准备时间
				Long prepareEndTime = Long.parseLong(map.get("prepare_end_time").toString());//活动准备结束时间
				Long activityStartTime =Long.parseLong( map.get("activity_start_time").toString());//活动开始时间
				Long activityEndTime =Long.parseLong(map.get("activity_end_time").toString());//活动结束时间
				Long cancelTime = Long.parseLong(map.get("cancel_time").toString());//活动取消时间
				Long nowDate = Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
				String activityId = (String) map.get("id");//活动ID
				List<Map> queryActivityStatus = userService.queryActivityStatus(activityId, json);
				for (Map map2 : queryActivityStatus) {
					if(map2.get("status").toString().equals("0")){
						//未成功
						map.put("activityStatus", "未成功");
					}else{
						if(nowDate>=parseLong && nowDate<=parseLong2){
							//报名中
							map.put("activityStatus", "报名中");
						}else if(nowDate>=prepareStartTime && nowDate<=prepareEndTime){
							//准备中
							map.put("activityStatus", "准备中");
						}else if(nowDate>=activityStartTime && nowDate<=activityEndTime){
							//进行中
							map.put("activityStatus", "进行中");
						}else if(nowDate>=cancelTime){
							//已完成
							map.put("activityStatus", "已完成");
						}
					}
				}
				map.remove("enroll_begin_time");
				map.remove("enroll_end_time");
				map.remove("prepare_start_time");
				map.remove("prepare_end_time");
				map.remove("activity_start_time");
				map.remove("activity_end_time");
				map.remove("cancel_time");
				Long partActivityPersions = userService.queryCountByAcitityId(activityId);
				map.put("partActivityPersions", partActivityPersions);
			}
		}else if(type==3){//已结束
			List<Map> rankList01 = userService.queryCountByUserIdAndEnd(json);
			for (Map map : rankList01) {
				String enrollBeginTime = map.get("enroll_begin_time").toString();//报名开始时间
				long parseLong = Long.parseLong(enrollBeginTime);
				String enrollEndTime = map.get("enroll_end_time").toString();//活动报名结束时间
				long parseLong2 = Long.parseLong(enrollEndTime);
				Long prepareStartTime = Long.parseLong(map.get("prepare_start_time").toString());//活动准备时间
				Long prepareEndTime = Long.parseLong(map.get("prepare_end_time").toString());//活动准备结束时间
				Long activityStartTime =Long.parseLong( map.get("activity_start_time").toString());//活动开始时间
				Long activityEndTime =Long.parseLong(map.get("activity_end_time").toString());//活动结束时间
				Long cancelTime = Long.parseLong(map.get("cancel_time").toString());//活动取消时间
				Long nowDate = Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
				rankList = rankList01;
				map.put("activityStatus", "已结束");
				map.remove("enroll_begin_time");
				map.remove("enroll_end_time");
				map.remove("prepare_start_time");
				map.remove("prepare_end_time");
				map.remove("activity_start_time");
				map.remove("activity_end_time");
				map.remove("cancel_time");
				String activityId = (String) map.get("id");//活动ID
				Long partActivityPersions = userService.queryCountByAcitityId(activityId);
				map.put("partActivityPersions", partActivityPersions);
		    }
		}
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", rankList));
	}
	
	/**
	 * 活动评论
	 * @param request
	 * 		  userid:用户ID
	 * 		  activityId：活动ID
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/activityComment.do")
	public Object activityComment(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if(StringUtil.isNullOrBlank(json.get("userid"))){
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登陆", ""));
		}
		if(StringUtil.isNullOrBlank(json.get("activityId"))){
			return Send.send(MobileMessageCondition.addCondition(false, 52, "请选择要查看评论的活动", ""));
		}
		try {
			List<Map> activityComments = userService.queryActivityComment(json);//线下活动详情
			Long activityLikeCount = userService.queryActivityLikes(json);//活动的点赞数
			for (Map map : activityComments) {
				String userId = map.get("userId").toString();
				Long commentCounts=userService.queryActivityComment(userId);
				map.put("secondCommentCount", commentCounts);//二次评论
				map.put("activityLikeCount", activityLikeCount);//评论内容
			}
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功",activityComments));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 53, "查看活动评论时出现一次异常", ""));
		}
	}
	
	/**
	 * 活动评论修改
	 * @param request
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping("/activityCommentEdit.do")
	public Object activityCommentEdit(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if(StringUtil.isNullOrBlank(json.get("userid"))){
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登陆",""));
		}
		if(StringUtil.isNullOrBlank(json.get("commentId"))){
			return Send.send(MobileMessageCondition.addCondition(false, 54, "请选择要删除的评论", ""));
		}
		try {
			MobileMessage message = userService.deleteCommentById(json);
			return Send.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 55, "删除评论时出现异常", ""));
		}
	}
	
	/**
	 * 讲师详情页
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/teacherDetail.do")
	public Object teacherDetail(HttpServletRequest request) throws Exception{
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		String teacherId = json.get(Send.USERID_NAME).toString();
		Code code = userService.getTeacherDetail(teacherId);
		return Send.send(code);
		/*
		if(StringUtil.isNullOrBlank(json.get("userid"))){
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登陆", ""));
		}
		Map<String,Object> map=new HashMap<String,Object>();
		Map teacherInfo=userService.queryTeacherInfo(json);//获得讲师信息
		if(teacherInfo==null||teacherInfo.size()<1)
			return Send.send(MobileMessageCondition.addCondition(false,1,"该用户不是讲师！",null));
		Long videoCountAll = userService.queryTeacherDetail(json);//视频总量
		//toUpDate Long videoCounts = userService.queryTeacherVideo(json);//视频总点击量
		Long videoCounts =10L;//视频总点击量
		Long video01 = userService.queryTeacherVideo01(json);//视频点击付费量
		Long fensiCount = userService.queryCountByAcitity(json);//粉丝量
		List<Map> fuFeiMoney=userService.queryCountFuFeiCount(json);//总付费额
		List<Map> list=userService.queryTeacher(json);//身价,出场费
		//本月收益
		//toUpDate Float incomeMonth=userService.queryTeacherIncomeMonth(json);
		Double incomeMonth=300.00;
		map.put("incomeMonth", incomeMonth);
		//上月收益
		//toUpDate	Float beforeIncomeMonth=userService.queryBeforeIncomeMonth(json);
		Double beforeIncomeMonth=200.00;
		map.put("beforeIncomeMonth", beforeIncomeMonth);
		//本季度收益
		//toUpDate	Float currenSeasonIncome=userService.queryCurrentSeasonIncome(json);
		Double currenSeasonIncome=500.00;
		map.put("currenSeasonIncome", currenSeasonIncome);
		//上一季度收益
		//toUpDate Float beforeSeasonIncome= userService.queryBeforeSeasonIncome(json);
		Double beforeSeasonIncome=2500.00;
		map.put("beforeSeasonIncome", beforeSeasonIncome);
		//累计收益
		Double incomeAll=5000.00;
		//toUpDate	Float incomeAll=userService.queryIncomeAll(json);
		map.put("incomeAll", incomeAll);
		for (Map map01 : fuFeiMoney) {
			Object object = map01.get("sum(tb_order.price)");
			map.put("fuFeiMoney",object);
			for (Map map02 : list) {
				map.put("worth", map02.get("worth"));
				map.put("appearanceFee",map02.get("appearance_fee"));
			}
		}
		map.put("videoCountAll", videoCountAll);
		map.put("videoClickRate", video01/videoCounts);
		map.put("fensiCount", fensiCount);
		map.put("totalChargeCount", video01);
		map.put("points",teacherInfo.get("sum(tb_point.point_quantum)"));
		map.put("picSaveUrl", teacherInfo.get("pic_save_url"));//头像地址
		map.put("name", teacherInfo.get("name"));//讲师名称
		map.put("level", teacherInfo.get("level"));//讲师等级
		map.put("job", teacherInfo.get("job"));//讲师等级
		return Send.send(MobileMessageCondition.addCondition(true,0,"成功",map));
		*/
	}
	
	/**
	 * 获得积分
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/points.do")
	public Object points(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if(StringUtil.isNullOrBlank(json.get("userid"))){
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
   		 /*Map map = userService.queryQianDao(json);
		 Long getTime = Long.valueOf(map.get("max(tb_point.get_time)").toString().substring(0,8));
		 Long nowDate = Long.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date()));//获取当前时间年月日
		if(getTime.equals(nowDate)){
			//已经签到了
			map.put("content", "已签到");
		}else{
			map.put("content", "签到");
		}*/
		int points = userService.getPointsByUserId(json.get("userid").toString());
		
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("way_type", PointEachType.DAY_FIRST_POINT.getType());
		PointMost most = od.getObjByParams(PointMost.class, params);
		params = new HashMap<String,Object>();
		params.put("user_id", json.get("userid"));
		params.put("point_way_id", most.getId());
		List<Point> list = od.getPos(Point.class, params, 0, 1, Order.desc("get_time"));
		String yearAndMouth = DateFormate.getDateFormateCH(System.currentTimeMillis()).substring(0, 11);
		Map<String,Object> map = new HashMap<String,Object>();
		if(list!=null&&DateFormate.getDateFormateCH(list.get(0).getGet_time()).contains(yearAndMouth)){
			map.put("isGet", Constant.YES_INT);
		}else{
			map.put("isGet", Constant.NO_INT);
		}
		map.put("coun", points);
		return Send.send(MobileMessageCondition.addCondition(true, 0, null, map));
	}
	
	/**
	 * 签到
	 * @param request
	 * 		  userid：用户ID
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/qianDao.do")
	public Object qianDao(HttpServletRequest request) throws Exception{
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(MobileMessageCondition.addCondition(false, 100, "未知错误！", null));
		if(Associate.isDoing(ip, "user_sign"))
			return Send.send(MobileMessageCondition.addCondition(false, 101, "正在签到，请稍后...", null));
		
		Associate.save(ip, "user_sign");
		
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if(StringUtil.isNullOrBlank(json.get("userid"))){
			Associate.clear(ip, "user_sign");
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		User user = userService.queryUserById(json);
		MobileMessage result = pointService.addPoint(user, PointEachType.DAY_FIRST_POINT,0);
		
		Associate.clear(ip, "user_sign");
		
		return Send.send(result);
	}
	
	/**
	 * 反馈消息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/suggestion.do")
	public Object suggestion(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if(StringUtil.isNullOrBlank(json.get("userid"))){
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		try {
			userService.insertUserSuggest(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", ""));
		}catch (Exception e){
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 61, "添加意见反馈时异常", ""));
		}		
	}
	/**
	 * 反馈消息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/suggestionList.do")
	public Object suggestionList(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if(StringUtil.isNullOrBlank(json.get("userid"))){
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		try {
			List<SuggestionType> list=	userService.suggestionList(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功",list));
		}catch (Exception e){
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 61, "系统异常", ""));
		}		
	}
	/**
	 * 线下活动的轮播图
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/activityBanner.do")
	public Object activityBanner(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if(StringUtil.isNullOrBlank(json.get("userid"))){
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		try {
			List<Map> list=userService.queryActivityBanner();
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", list));
		} catch (Exception e) {
			return Send.send(MobileMessageCondition.addCondition(false, 62, "查询线下活动中的轮播图时出现异常", ""));
		}
	}
	
	/**
	 * 立即购买
	 * @param request
	 * 		  userid:用户ID
	 * 		  courseId:课程ID
	 * 		  couponType:优惠券类型
	 * 		  number:优惠券券号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/buyNow.do")
	public Object buyNow(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if(StringUtil.isNullOrBlank(json.get("userid"))){
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登陆", ""));
		}
		if(StringUtil.isNullOrBlank(json.get("courseId"))){
			return Send.send(MobileMessageCondition.addCondition(false, 63, "课程不能为空", ""));
		}
		if(StringUtil.isNullOrBlank(json.get("couponType"))){
			return Send.send(MobileMessageCondition.addCondition(false, 64, "优惠券使用类型不能为空", ""));
		}
		if(StringUtil.isNullOrBlank(json.get("number"))){
			return Send.send(MobileMessageCondition.addCondition(false, 65, "券号不能为空", ""));
		}
		try {
			Map videos=userService.queryVideoById(json);
			if(json.get("couponType").equals("0")){
				Map couponByNumber=userService.queryCouponByNumber(json);
				//0现金优惠券
				Double  donemination= Double.parseDouble(couponByNumber.get("denomination").toString());
				String perCost01 = videos.get("per_cost").toString();
				Double perCost =  Double.parseDouble(perCost01);
				Double totalCost=perCost-donemination;
				videos.put("donemination", donemination);
				videos.put("totalCost", totalCost);
			}else if(json.get("couponType").equals("1")){
				//1打折券
				Map couponByNumber=userService.queryCouponByNumber(json);
				Double  donemination= Double.parseDouble(couponByNumber.get("denomination").toString());//打得折数
				Double perCost = Double.parseDouble(videos.get("per_cost").toString());//视频总花费
				Double totalCost=donemination * perCost;//总共的钱数
				videos.put("donemination", donemination);
				videos.put("totalCost", totalCost);
			}
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功",videos));
		} catch (Exception e) {
			return Send.send(MobileMessageCondition.addCondition(false, 64, "付款时出现异常", ""));
		}	
	}
	
	/**
	 * 添加评论
	 * @param request
	 * 		  userid:用户ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addComment.do")
	public Object addComment(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if(StringUtil.isNullOrBlank(json.get("userid"))){
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登陆", ""));
		}
		try {
			userService.addCommentsForCourse(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", ""));
		} catch (Exception e) {
			return Send.send(MobileMessageCondition.addCondition(false, 65, "添加评论时出现异常", ""));
		}
	}
	
	/**
	 * 发起的教程
	 * @param request
	 * 		  userid:用户ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/requestCourse.do")
	public Object requestCourse(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if(StringUtil.isNullOrBlank(json.get("userid"))){
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		try {
			List<Map> requestVideos = userService.queryRequestVideo(json);
			//couponService.couponCourse(json);//添加优惠券
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功",requestVideos));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(true, 66, "查询发起的教程时出现异常",""));
		}
	}
	
	/**
	 * 投票
	 * @param request
	 * 		  userid:用户ID
	 * 		  videoId视频ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addVote.do")
	public Object addVote(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if(StringUtil.isNullOrBlank(json.get("userid"))||StringUtil.isNullOrBlank(json.get("videoId"))){
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录或对课程对应的ID为空", ""));
		}
		try {
			userService.addVote(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "投票成功", ""));
		} catch (Exception e) {
			return Send.send(MobileMessageCondition.addCondition(false, 67, "投票失败", ""));
		}
	}
	
	/**
	 * 职业列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/jobList.do")
	public Object jobList(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		try {
			List<Map> jobList=userService.queryJobList();
			return Send.send(MobileMessageCondition.addCondition(true, 0, "", jobList));
		} catch (Exception e) {
			return Send.send(MobileMessageCondition.addCondition(false, 68,"获取职业列表失败", ""));
		}
	}
}
