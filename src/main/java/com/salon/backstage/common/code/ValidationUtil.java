package com.salon.backstage.common.code;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

	/**
	 * 验证是否为国内手机号码
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isChinaPhoneNumber(String phoneNumber) {
		/*
		 * 短信运营商提供的号段 130;131;132;133;134;135;136;137;138;139; 141;145;147;
		 * 150;151;152;153;155;156;157;158;159; 170;171;173;176;177;178;
		 * 180;181;182;183;184;185;186;187;188;189
		 */
		Pattern p = Pattern.compile("^((13[0-9])|(14[157])|(15[^4,\\D])|(17[013678])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(phoneNumber);
		return m.matches();
	}

	/**
	 * 判断对象是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) return true;
		if (obj instanceof String) {
			String temp = (String) obj;
			if (temp.trim().length() < 1) {
				return true;
			} else {
				return false;
			}
		} else if (obj instanceof  Integer){
			try{
				Integer.parseInt(obj+"");
				return false;
			}catch (Exception e){
				return true;
			}
		} else if (obj instanceof Collection){
			Collection<?> temp = (Collection<?>)obj;
			if(temp.isEmpty()){
				return true;
			}else{
				return false;
			}
		} else if (obj instanceof Map){
			Map<?,?> temp = (Map<?,?>) obj;
			if(temp.isEmpty()){
				return true;
			}else{
				return false;
			}
		} else{
			return false;
		}
	}
	
	/**
	 * 如果为null，则转为空字符串，如果非null，则原样返回
	 * @param str
	 * @return
	 */
	public static String nullStrToEmptyStr(String str){
		if(str == null){
			return "";
		}else{
			return str;
		}
	}

	/**
	 * 传入对象，判断是否为标准的数字
	 * @param obj
	 * @return
	 */
	public static boolean isInteger(Object obj){
		if(obj == null){
			return false;
		}
		try {
			String objStr1 = obj.toString();
			String objStr2 = obj + "";
			int objInt1 = Integer.parseInt(objStr1);
			int objInt2 = Integer.parseInt(objStr2);
		} catch (NumberFormatException e) {
			return false;
		}
		return false;
	}

}
