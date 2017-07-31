package com.salon.backstage.qcproject.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qc.util.Associate;
import com.qc.util.MathUtil;
import com.qc.util.MathUtil.IsInt;
import com.salon.backstage.common.cache.RedisClient;
import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.common.util.NetworkUtil;
import com.salon.backstage.common.util.StringUtil;
import com.salon.backstage.common.util.Validate;
import com.salon.backstage.homepage.district.service.IDistrictService;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.service.UserServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.user.IUserService;

/**
 * 作者：齐潮
 * 创建日期：2017年1月3日
 * 类说明：用于处理关于用户的请求
 */
@Controller
@RequestMapping(value="usernew")
public class UserActionNEW {

	@Autowired
	private UserServiceNEW us;
	
	@Autowired
	IDistrictService districtService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private MobileMessage mobileMessage;
	
	/**
	 * 返回用户注册时需要的省份信息 的接口
	 */
	@ResponseBody
	@RequestMapping(value="register",method=RequestMethod.POST)
	public Object provRegister(HttpServletRequest request) throws Exception{
		 List<Map> distList = districtService.queryProv();
		 return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", distList));
	}
	
	/**
	 * 忘记密码之后修改密码操作
	 * phone:手机号
	 * idenCode:验证码
	 * firstPassword:首次密码
	 * secondPassword:第二次密码
	 */
	@RequestMapping("/forgetPasswordd.do")
	@ResponseBody
	public Object editPasswordd(HttpServletRequest request) {
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
	 * 返回用户注册时需要的详细信息 的接口
	 */
	@ResponseBody
	@RequestMapping(value="registerDetail",method=RequestMethod.POST)
	public Object districtRegister(HttpServletRequest request) throws Exception{
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		String districtId = (String)json.get("districtId");
		List<Map> distList = districtService.queryDetail(districtId);
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", distList));
	}
	
	
	/**
	 * 修改用户个人信息
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="update",method=RequestMethod.POST)
	public Object getCollectsByType(HttpServletRequest request) throws Exception{
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(Code.init(false, 100, "未知错误！"));
		if(Associate.isDoing(ip, "user_update"))
			return Send.send(Code.init(false, 101, "正在修改，请稍后..."));
		
		Associate.save(ip, "user_update");
		
		Code result = updateSupport(request);
		
		Associate.clear(ip, "user_update");
		
		return Send.send(result);
	}
	
	
	/**
	 * 修改用户信息的核心方法
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	private Code updateSupport(HttpServletRequest request) throws Exception{
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "gender", "district", "username");
		if(!vali)
			return Code.init(false, 1, "用户名,性别,地区不能为空！");
		IsInt gender = MathUtil.isToInteger(json.get("gender").toString());
		String district = json.get("district").toString();
		String username = json.get("username").toString();
		String userid = json.get("userid").toString();
//		String picSaveUrl = json.get("picSaveUrl")==null?"":json.get("picSaveUrl").toString();
		long birthDay = (json.get("birthDay")==null||json.get("birthDay").equals(""))?0:Long.parseLong(json.get("birthDay").toString());
		String job = json.get("job")==null?"":json.get("job").toString();
		String jobId = json.get("job_id")==null?"":json.get("job_id").toString();
		String zodiac = json.get("zodiac")==null?"":json.get("zodiac").toString();
		int zodiacIndex = (json.get("zodiacIndex")==null||json.get("zodiacIndex").equals(""))?-1:Integer.parseInt(json.get("zodiacIndex").toString());
		long now = System.currentTimeMillis();
		if(now<birthDay||!gender.is||(gender.value!=Constant.USER_SEX_MAN&&gender.value!=Constant.USER_SEX_WOMEN&&gender.value!=Constant.USER_SEX_UNKNOW))
			return Code.init(false, 2, "参数不合法！");
		
		Map<String,Object> settings = new HashMap<String,Object>();
		settings.put("username", username);
		settings.put("gender", gender.value);
//		settings.put("pic_save_url", picSaveUrl);
		settings.put("update_time", now);
		settings.put("district", district);
		settings.put("birthday", birthDay);
		settings.put("job", job);
		settings.put("jobId", jobId);
		settings.put("zodiac", zodiac);
		settings.put("zodiacIndex", zodiacIndex);
		
		Code result = us.updateUser(userid, settings, now, birthDay);
		
		return result;
	}
	
	/**
	 * 关于我们
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="aboutus",method=RequestMethod.POST)
	public Object aboutUs(HttpServletRequest request) throws Exception{
		Code result = us.getAboutUs();
		return Send.send(result);
	}
	
	/**
	 * 获取讲师收益列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="incomes",method=RequestMethod.POST)
	public Object teacherIncomes(HttpServletRequest request) throws Exception{
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "pageNo", "pageSize");
		if(!vali)
			return Code.init(false, 1, "参数缺失！");
		IsInt page = MathUtil.isToInteger(json.get("pageNo").toString());
		IsInt size = MathUtil.isToInteger(json.get("pageSize").toString());
		if(!page.is||!size.is||page.value<1||size.value<1)
			return Code.init(false, 1, "参数不合法！");
		String teacherId = json.get(Send.USERID_NAME).toString();
		Code code = us.getTeacherSends(teacherId, page.value, size.value);
		return Send.send(code);
	}
	
}
