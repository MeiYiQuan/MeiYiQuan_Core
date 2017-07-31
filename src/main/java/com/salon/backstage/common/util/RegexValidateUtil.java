//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.salon.backstage.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidateUtil {
    static boolean flag = false;
    static String regex = "";

    public RegexValidateUtil() { }

    public static boolean check(String str, String regex) {
        try {
            Pattern e = Pattern.compile(regex);
            Matcher matcher = e.matcher(str);
            flag = matcher.matches();
        } catch (Exception var4) {
            flag = false;
        }
        return flag;
    }
    
    public static boolean checkNotEmputy(String notEmputy) {
        regex = "^\\s*$";
        return !check(notEmputy, regex);
    }

    public static boolean checkEmail(String email) {
        String regex = "^\\w+[-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$ ";
        return check(email, regex);
    }
    
    public static boolean checkCellphone(String cellphone) {
        String regex = "^((13[0-9])|(14[5-7])|(15([0-9]))|(17([0-9]))|(18[0-9]))\\d{8}$";
        return check(cellphone, regex);
    }

    public static boolean checkTelephone(String telephone) {
        String regex = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
        return check(telephone, regex);
    }
    
    public static boolean checkFax(String fax) {
        String regex = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
        return check(fax, regex);
    }
    
    public static boolean checkQQ(String QQ) {
        String regex = "^[1-9][0-9]{4,} $";
        return check(QQ, regex);
    }
    
    public static boolean checkHasNum(String str) {
        String regex = ".*[0-9]+.*";
        Matcher m2 = Pattern.compile(regex).matcher(str);
        return m2.matches();
    }
    
    public static boolean checkHasLetter(String str) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m2 = Pattern.compile(regex).matcher(str);
        return m2.matches();
    }
    
    public static boolean checkIsOnlyNum(String str) {
        boolean result = str.matches("[0-9]+");
        return result;
    }
}