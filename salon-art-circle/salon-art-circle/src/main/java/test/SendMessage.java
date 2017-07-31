package test;

import com.salon.backstage.qcproject.util.SmsUtil;
import com.taobao.api.ApiException;


/**
 * 作者：齐潮
 * 创建日期：2017年1月17日
 * 类说明：
 */
public class SendMessage {

	public static void main(String[] args) throws ApiException {
		boolean result = SmsUtil.send("18237881820", "1234");
		System.out.println(result);
	}
}
