package test.date;

import java.util.Calendar;
import java.util.UUID;

import com.qc.util.DateFormate;

/**
 * 作者：齐潮
 * 创建日期：2017年1月9日
 * 类说明：
 */
public class DateTest {

	public static void main(String[] args) {
//		long now = System.currentTimeMillis();
//		System.out.println("当前时间：" + DateFormate.getDateFormateCH(now));
//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(now);
//		c.add(Calendar.MONTH, -7);
//		long result = c.getTimeInMillis();
//		System.out.println("一周前时间：" + DateFormate.getDateFormateCH(result));
//		char[] cs = {'s','d','2'};
//		String s = new String(cs);
//		System.out.println(s);
//		System.out.println(cs.toString());
//		String now = System.currentTimeMillis() + "";
//		System.out.println(now);
//		System.out.println(now.length());
		String id = UUID.randomUUID().toString().replace("-", "");
		System.out.println(id);
		System.out.println(id.length());
	}
	
}
