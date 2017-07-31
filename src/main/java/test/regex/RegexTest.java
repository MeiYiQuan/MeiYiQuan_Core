package test.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
	
	/**
	 * 验证11位手机号
	 */
	public static boolean isMobile(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }  
	
	public static void main(String[] args) {
		System.out.println(isMobile("17710663181"));
	}
	
}













