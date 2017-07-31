package test.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
//import org.junit.Test;
//import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.salon.backstage.common.util.HttpConnecter;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.qcproject.util.Send;

/**
 * 作者：齐潮
 * 创建日期：2017年1月5日
 * 类说明：测试接口
 */
public class TestHttp {

	/**
	 * 测试接口的返回体
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Map<String,String> params = new HashMap<String,String>();
		// url是接口地址，测试时只需要修改这个值和参数即可
		String url = "requestnew/getBanner.do";
		params.put("token", "93adff438b5543878159feac9125c7f4");
		params.put(Send.USERID_NAME, "402858815a829517015a842c147b0008");
//		params.put("shopId", "c6025c1376a74b768ca5c716717e19da");
//		params.put("type", "1");
//		params.put("payType", "2");
//		params.put("useCoupon", "2");
		
		
		
		String address = "http://127.0.0.1:8080/salon-art-circle/";
		MobileMessage result = HttpConnecter.post(address + url, params, Send.MUST_LOGIN_SIGN);
		System.out.println(JSONObject.toJSONString(result));
	}
	
	
	
//	@Test
	public void testyanzhengma() throws Exception{
		Map<String,String> params = new HashMap<String,String>();
		// url是接口地址，测试时只需要修改这个值和参数即可
		String url = "user/getIdenCode.do";
//		params.put("idenCode", "1987");
		params.put("phone", "13689022140");
		params.put("whoNeedIdenCode", "0");
//		params.put("password", "123456");
//		params.put("token", "197884a0-1b5d-4fd6-8979-e8c7c2aa4941");
//		params.put("userid", "40285881593a2b0301593a2b4a4d0000");
//		params.put("pageNo", "1");
//		params.put("pagesize", "10");
//		params.put("channelName", "火山");
		
		
		
		String address = "http://127.0.0.1:8080/salon-art-circle/";
		MobileMessage result = HttpConnecter.post(address + url, params, Send.MUST_LOGIN_SIGN);
		System.out.println(JSONObject.toJSONString(result));
	}
	
	
//	@Test
	public void testZhuce() throws Exception{
		Map<String,String> params = new HashMap<String,String>();
		// url是接口地址，测试时只需要修改这个值和参数即可
		String url = "user/register.do";
		params.put("idenCode", "5059");
		params.put("phone", "13689022140");
		params.put("district", "北京市朝阳区国贸");
		params.put("password", "123456");
//		params.put("token", "197884a0-1b5d-4fd6-8979-e8c7c2aa4941");
//		params.put("userid", "40285881593a2b0301593a2b4a4d0000");
//		params.put("pageNo", "1");
//		params.put("pagesize", "10");
//		params.put("channelName", "火山");
		
		
		
		String address = "http://127.0.0.1:8080/salon-art-circle/";
		MobileMessage result = HttpConnecter.post(address + url, params, Send.MUST_LOGIN_SIGN);
		System.out.println(JSONObject.toJSONString(result));
	}
	
//	@Test
	public void testLogin() throws Exception{
		Map<String,String> params = new HashMap<String,String>();
		// url是接口地址，测试时只需要修改这个值和参数即可
		String url = "user/login.do";
		params.put("equi_type", "0");
		params.put("phone", "18237881820");
		params.put("registId", "极光id");
		params.put("password", "123456");
//		params.put("token", "197884a0-1b5d-4fd6-8979-e8c7c2aa4941");
//		params.put("userid", "40285881593a2b0301593a2b4a4d0000");
//		params.put("pageNo", "1");
//		params.put("pagesize", "10");
//		params.put("channelName", "火山");
		
		
		
		String address = "http://127.0.0.1:8080/salon-art-circle/";
		MobileMessage result = HttpConnecter.post(address + url, params, Send.MUST_LOGIN_SIGN);
		System.out.println(JSONObject.toJSONString(result));
	}
	
	
	
	
	
}
