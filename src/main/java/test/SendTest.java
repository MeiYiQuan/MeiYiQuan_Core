package test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import com.alibaba.fastjson.JSONObject;
import com.qc.util.DateFormate;
import com.qc.util.MathUtil;
import com.salon.backstage.common.util.MD5Util;
import com.salon.backstage.common.util.PrimaryKeyUtil;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.DESUtil;
import com.salon.backstage.qcproject.util.Send;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 作者：齐潮
 * 创建日期：2017年1月10日
 * 类说明：
 */
public class SendTest {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		/*
		String s = "{\"content\":\"WlmadSiOYJu+USV4+z/Fvoy51Dy/cm8vcie7yCVnUkgMTn+X1h53HnQ4S4H3DWSlD0tYUtc43TOg7l8kmOVwu2ZMNaZvQLnL+pYZbhy9O5h7wot0aPemaBC/PRqH/nF1QvA/ipa3IIDOPW05KY4DPkq3iX2SLprKz+HzmEsc6ceRF4+9vH/wepZ2F9tRnkrhsLnl/lOTzUHk8vvOxm2+Gp7hHSMOUZ6o\"}";
		Map<String,Object> json = JSONObject.parseObject(s, Map.class);
		String content = json.get(Send.JSON_NAME).toString();
		byte[] baseByte = new BASE64Decoder().decodeBuffer(content);
		byte[] desByte = DESUtil.decrypt(baseByte, Send.DES_KEY);
		String desStr = new String(desByte,"utf-8");
		System.out.println(desStr);
		Map<String,Object> map = JSONObject.parseObject(desStr, Map.class);
		*/
//		Properties ps = System.getProperties();
//		Set<String> set = ps.stringPropertyNames();
//		for(String s:set){
//			System.out.println(s + "-------:" + System.getProperty(s));
//		}
//		String s = "{\"timestamp\":1484136769510}";
//		System.out.println("s----" + s);
//		System.out.println(JSONObject.toJSONString(s));
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("ddd", s);
//		String ss = JSONObject.toJSONString(map);
//		System.out.println(ss);
//		Map<String,Object> m = JSONObject.parseObject(ss, Map.class);
//		System.out.println(m.get("ddd").getClass().getName());
//		String sign = Send.createSign(map, "c07c337f88bc477c8cbe6c404208d764", 1484102372821L);
//		System.out.println(sign);
//		System.out.println(args.length);
		
		
		
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("name", "张三");
//		map.put("age", 12);
//		Map<String,Object> role = new HashMap<String,Object>();
//		role.put("roleName", "系统管理员");
//		role.put("remark", "负责本项目所有的工作");
//		map.put("role", role);
//		long now = 1484270984725L;
//		String sign = Send.createSign(map, Send.DEFAULT_TOKEN, now);
//		System.out.println(sign);
//		String s = (String) Send.send(map, Send.DEFAULT_TOKEN);
//		System.out.println(s);
		/*
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("age", 12);
		long now = 1484270984725L;
		String sign = Send.createSign(map, Send.DEFAULT_TOKEN, now);
		System.out.println("SIGN:" + sign);
		map.put(Send.SIGN_NAME, sign);
		map.put(Send.TIMESTAMP_NAME, now);
		String mResult = JSONObject.toJSONString(map);
		byte[] mByte = null;
		try {
			mByte = mResult.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] desByte = DESUtil.encrypt(mByte, Send.DES_KEY);
		String descStr = new String(desByte);
		System.out.println("DES:" + descStr);
		String sendCentent = new BASE64Encoder().encode(desByte).replace("\r\n", "");
		System.out.println("BASE64:" + sendCentent);
		*/
		
//		Object ob = new Object();
//		System.out.println(System.currentTimeMillis());
//		FutureTask<Object> ft = new FutureTask((Callable) ob);
		
//		System.out.println("asdffdgGFDSdsf".toUpperCase());
//		
//		System.out.println(UUID.randomUUID().toString().replace("-", ""));
//		System.out.println(System.currentTimeMillis());
		/*
		System.out.println(PrimaryKeyUtil.getOrderNum(null));
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("a19672d230fa46f9825a92096647d4a4", 1);		// 1
//		map.put("a2c4d4d0c54846ac929fe09f656f73a9", 10);	// 5
//		map.put("c532190d3fcc4800822bd2d740b46d84", 1);		// 10
//		map.put("4aba791682ce40f8913999549a086896", 1);		// 20
//		map.put("4594249df07e45c894d97d1521a294b4", 1);		// 50
		
		System.out.println(JSONObject.toJSONString(map));
		*/
		long now = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);
		
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		calendar.set(Calendar.MONTH, 0);
		

		
		System.out.println(DateFormate.getDateFormateCH(1511835793352L));
		
		
		
	}
}
