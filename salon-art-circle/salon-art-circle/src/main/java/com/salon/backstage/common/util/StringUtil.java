package com.salon.backstage.common.util;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;



/**
 * 字符串操作的工具类
 * @author cxw
 */
public class StringUtil {
	
	
	/**
	 * 判断字符串是否为null或空恪
	 * @param str
	 * @return
	 */
	public static boolean isNullOrBlank(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		}
		return false;
	}
	/**
	 * 判断字符串是否为null或空恪
	 * @param str
	 * @return
	 */
	public static boolean isNullOrBlank(Object str) {
		if (str == null ) {
			return true;
		}
		String s = str.toString();
		if(s.trim().equals("")){
			return true;
		}
		return false;
	}
	/**
	 * 判断字符串是否为null或空恪
	 * @param str
	 * @return
	 */
	public static boolean isNullOrBlank(BigDecimal str) {
		if (str == null || str.equals("")) {
			return true;
		}
		return false;
	}
	/**
	 * 判断字符串是否为null或空恪
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrBlank(Integer str) {
		if (str == null || str.equals(null)) {
			return true;
		}
		return false;
	}
		/**
	   * 重写equals方法 参数 2个字符串 若参数等于空返回TRUE
	   * */ 
	  public static boolean equals(String s1, String s2) {
	      if (null == s1 && null == s2) {
	          return true;
	      } else if (null == s1 || null == s2) {
	          return false;
	      }
	      // 获取并行实例 引用比较方法compare
	      Collator myCollator = Collator.getInstance();
	      boolean b = (0 == myCollator.compare(s1, s2));
	      return b;
	  }
	
	/**
	 * 判断字符串是否为正数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPlusNumeric(String str) {

		Pattern pattern = Pattern.compile("^(\\+)?\\d+\\.?\\d*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断字符串是否为手机号
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPhone(String str) {

		Pattern pattern = Pattern
				.compile("^1\\d{10}$");//^1[358]\\d{9}$
		return pattern.matcher(str).matches();
	}
	/**
	 * 判断用户名只能输入5-20个以字母开头、可带数字、“_”、“.”的字符!
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isUsername(String str) {

		Pattern pattern = Pattern
				.compile("^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){4,19}$");
		return pattern.matcher(str).matches();
	}
	/**
	 * 判断密码至少为6位的字母和数字
	 * 
	 * @param password
	 * @return
	 */
	public static boolean isCorrectPwd(String password) {
		String regex = "^[a-zA-Z0-9]{6,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}
	/**
	 * 判断字符串是否为电话号
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {

		Pattern pattern = Pattern
				.compile("^((\\(\\d{2,3}\\))|(\\d{3}\\-))?1\\d{10}$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (str == null || str.trim().equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile("^(-|\\+)?\\d+\\.?\\d*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是否正确email格式
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isCorrectEmail(String email) {
		String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		// ^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	/**
	 * 判断是否正确url格式
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isCorrectUrl(String url) {
		// String regex = "^http://([w-]+.)+[w-]+(/[w-./?%\\&=]*)?$";
		String regex = "^http:\\/\\/[A-Za-z0-9]+\\.[A-Za-z0-9]+[\\/=\\?%\\-&_~`@[\\\\]\\':+!]*([^<>\\\"\\\"])*$";
		// ^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}

	/**
	 * 判断是否正确的正整数
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isCorrectSignlessIntegral(String val) {
		String regex = "^[+]?(\\d{0,9})$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(val);
		return matcher.matches();
	}

	/**
	 * 判断字符串是否符合"yyyyMM"的格式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isFormatDate(String str) {
		Pattern pattern = Pattern.compile("\\d{6}");
		return pattern.matcher(str).matches();
	}

	/**
	 * 只去掉首尾的空格
	 * 
	 * @param str
	 * @return
	 */
	public static String leftOrRightTrim(String str) {
		if (str == null) {
			return null;
		}
		return str.replaceAll("(^[ |　]*|[ |　]*$)", "");
	}

	/**
	 * 替换字符串
	 * 
	 * @param strSc
	 *            需要进行替换的字符串
	 * @param oldStr
	 *            源字符串
	 * @param newStr
	 *            替换后的字符串
	 * @return 替换后对应的字符串
	 */
	public static String replace(String strSc, String oldStr, String newStr) {
		String ret = strSc;
		if (ret != null && oldStr != null && newStr != null) {
			ret = strSc.replaceAll(oldStr, newStr);
		}
		return ret;
	}

	/**
	 * 替换字符串，修复java.lang.String类的replaceAll方法时第一参数是字符串常量正则时(如："address".
	 * replaceAll("dd","$");)的抛出异常：java.lang.StringIndexOutOfBoundsException:
	 * String index out of range: 1的问题。
	 * 
	 * @param strSc
	 *            需要进行替换的字符串
	 * @param oldStr
	 *            源字符串
	 * @param newStr
	 *            替换后的字符串
	 * @return 替换后对应的字符串
	 */
	public static String replaceAll(String strSc, String oldStr, String newStr) {
		int i = -1;
		while ((i = strSc.indexOf(oldStr)) != -1) {
			strSc = new StringBuffer(strSc.substring(0, i)).append(newStr)
					.append(strSc.substring(i + oldStr.length())).toString();
		}
		return strSc;
	}

	/**
	 * 将字符串转换成HTML格式的字符串
	 * 
	 * @param str
	 *            需要进行转换的字符串
	 * @return 转换后的字符串
	 */
	public static String toHtml(String str) {
		String html = str;
		if (str == null || str.length() == 0) {
			return "";
		} else {
			html = replace(html, "&", "&amp;");
			html = replace(html, "<", "&lt;");
			html = replace(html, ">", "&gt;");
			html = replace(html, "\r\n", "\n");
			html = replace(html, "\n", "<br>\n");
			html = replace(html, "\"", "&quot;");
			html = replace(html, " ", "&nbsp;");
			return html;
		}
	}

	/**
	 * 将 HTML格式的字符串转换成常规显示的字符串
	 * 
	 * @param str
	 *            需要进行转换的字符串
	 * @return 转换后的字符串
	 */
	public static String toText(String str) {
		String text = str;
		if (str == null || str.length() == 0) {
			return "";
		} else {
			text = replace(text, "&amp;", "&");
			text = replace(text, "&lt;", "<");
			text = replace(text, "&gt;", ">");
			text = replace(text, "<br>\n", "\n");
			text = replace(text, "<br>", "\n");
			text = replace(text, "&quot;", "\"");
			text = replace(text, "&nbsp;", " ");
			return text;
		}
	}

	/**
	 * 将一字符串数组以某特定的字符串作为分隔来变成字符串
	 * 
	 * @param strs
	 *            字符串数组
	 * @param token
	 *            分隔字符串
	 * @return 以token为分隔的字符串
	 */
	public static String join(String[] strs, String token) {
		if (strs == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strs.length; i++) {
			if (i != 0)
				sb.append(token);
			sb.append(strs[i]);
		}
		return sb.toString();
	}

	/**
	 * 将一字符串以某特定的字符串作为分隔来变成字符串数组
	 * 
	 * @param str
	 *            需要拆分的字符串("@12@34@56")
	 * @param token
	 *            分隔字符串("@")
	 * @return 以token为分隔的拆分开的字符串数组
	 */
	public static String[] split(String str, String token) {
		String temp = str.substring(1, str.length());
		return temp.split(token);
	}

	/**
	 * 验证字符串合法性
	 * 
	 * @param str
	 *            需要验证的字符串
	 * @param test
	 *            非法字符串（如："~!#$%^&*()',;:?"）
	 * @return true:非法;false:合法
	 */
	public static boolean check(String str, String test) {
		if (str == null || str.equals(""))
			return true;
		boolean flag = false;
		for (int i = 0; i < test.length(); i++) {
			if (str.indexOf(test.charAt(i)) != -1) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 将数值型字符串转换成Integer型
	 * 
	 * @param str
	 *            需要转换的字符型字符串
	 * @param ret
	 *            转换失败时返回的值
	 * @return 成功则返回转换后的Integer型值；失败则返回ret
	 */
	public static Integer String2Integer(String str, Integer ret) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return ret;
		}
	}

	/**
	 * 将数值型转换成字符串
	 * 
	 * @param it
	 *            需要转换的Integer型值
	 * @param ret
	 *            转换失败的返回值
	 * @return 成功则返回转换后的字符串；失败则返回ret
	 */
	public static String Integer2String(Integer it, String ret) {
		try {
			return Integer.toString(it);
		} catch (NumberFormatException e) {
			return ret;
		}
	}

	/**
	 * 比较两字符串大小(ASCII码顺序)
	 * 
	 * @param str1
	 *            参与比较的字符串1
	 * @param str2
	 *            参与比较的字符串2
	 * @return str1>str2:1;str1<str2:-1;str1=str2:0
	 */
	public static int compare(String str1, String str2) { //
		if (str1.equals(str2)) {
			return 0;
		}
		int str1Length = str1.length();
		int str2Length = str2.length();
		int length = 0;
		if (str1Length > str2Length) {
			length = str2Length;
		} else {
			length = str1Length;
		}
		for (int i = 0; i < length; i++) {
			if (str1.charAt(i) > str2.charAt(i)) {
				return 1;
			}
		}
		return -1;
	}

	/**
	 * 将阿拉伯数字的钱数转换成中文方式
	 * 
	 * @param num
	 *            需要转换的钱的阿拉伯数字形式
	 * @return 转换后的中文形式
	 */
	public static String num2Chinese(double num) {
		String result = "";
		String str = Double.toString(num);
		if (str.contains(".")) {
			String begin = str.substring(0, str.indexOf("."));
			String end = str.substring(str.indexOf(".") + 1, str.length());
			byte[] b = begin.getBytes();
			int j = b.length;
			for (int i = 0, k = j; i < j; i++, k--) {
				result += getConvert(begin.charAt(i));
				if (!"零".equals(result.charAt(result.length() - 1) + "")) {
					result += getWei(k);
				}
				System.out.println(result);

			}
			for (int i = 0; i < result.length(); i++) {
				result = result.replaceAll("零零", "零");
			}
			if ("零".equals(result.charAt(result.length() - 1) + "")) {
				result = result.substring(0, result.length() - 1);
			}
			result += "元";
			byte[] bb = end.getBytes();
			int jj = bb.length;
			for (int i = 0, k = jj; i < jj; i++, k--) {
				result += getConvert(end.charAt(i));
				if (bb.length == 1) {
					result += "角";
				} else if (bb.length == 2) {
					result += getFloat(k);
				}
			}
		} else {
			byte[] b = str.getBytes();
			int j = b.length;
			for (int i = 0, k = j; i < j; i++, k--) {
				result += getConvert(str.charAt(i));
				result += getWei(k);
			}
		}
		return result;
	}

	private static String getConvert(char num) {
		if (num == '0') {
			return "零";
		} else if (num == '1') {
			return "一";
		} else if (num == '2') {
			return "二";
		} else if (num == '3') {
			return "三";
		} else if (num == '4') {
			return "四";
		} else if (num == '5') {
			return "五";
		} else if (num == '6') {
			return "六";
		} else if (num == '7') {
			return "七";
		} else if (num == '8') {
			return "八";
		} else if (num == '9') {
			return "九";
		} else {
			return "";
		}
	}

	private static String getFloat(int num) {
		if (num == 2) {
			return "角";
		} else if (num == 1) {
			return "分";
		} else {
			return "";
		}
	}

	private static String getWei(int num) {
		if (num == 1) {
			return "";
		} else if (num == 2) {
			return "十";
		} else if (num == 3) {
			return "百";
		} else if (num == 4) {
			return "千";
		} else if (num == 5) {
			return "万";
		} else if (num == 6) {
			return "十";
		} else if (num == 7) {
			return "百";
		} else if (num == 8) {
			return "千";
		} else if (num == 9) {
			return "亿";
		} else if (num == 10) {
			return "十";
		} else if (num == 11) {
			return "百";
		} else if (num == 12) {
			return "千";
		} else if (num == 13) {
			return "兆";
		} else {
			return "";
		}
	}

	/**
	 * 将字符串的首字母改为大写
	 * 
	 * @since 1.2
	 * @param str
	 *            需要改写的字符串
	 * @return 改写后的字符串
	 */
	public static String firstToUpper(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	//转进一个字母，返回对应的位置数字  a-z  1-26
	public static int resultInt(String zimu)
	{
		char a=zimu.toUpperCase().charAt(0);
		return (int)a-64;
	}
	//转进一个数字，返回对应的字母
	public static String resultChar(int shuzi)
	{
		shuzi+=64;
		
		char a=(char)shuzi;
		return String.valueOf(a);
	}
	/**
	 * 判断字符串是否为有字母、下划线、数字组成，且首位是字母
	 * @param str
	 */
	public static boolean existC_N(String str){
		String regex = "^\\w+$";
//		String regex = "/^[a-zA-Z]+[a-zA-Z0-9_]*$/";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	/**
	 * 判断字符串是否为有字母、下划线、数字组成，且首位是字母
	 * @param str
	 */
	public static JSONObject setStrForNull(Map map){
		/*for(String str:map){*/
			
		//
		/*String regex = "^\\w+$";
//		String regex = "/^[a-zA-Z]+[a-zA-Z0-9_]*$/";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);*/
		return null;
	}
	
	/**
	 * 判断字符串是否为有字母、下划线、数字组成，且首位是字母
	 * @param str
	 */
	public static String getErrorMessage(String code ,String errorMessage){
		JSONObject json=new JSONObject();
		json.put("code", code);
		json.put("errorMessage", errorMessage);
		return json.toString();
	}
	/**
	 * 
	 * @param str
	 */
	public static String getSuccessMessage(Object obj){
		JSONObject json=new JSONObject();
		json.put("code", "0");
		json.put("response", obj);
		return json.toString();
	}
	/**
	 * 
	 * @param str
	 */
	public static String getSuccessMessage(){
		JSONObject json=new JSONObject();
		json.put("code", "0");
		return json.toString();
	}
}
