package com.salon.backstage.qcproject.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 用于处理MD5加密
 * @author Administrator
 */
public class MD5Util {
    
	private final static Logger logger = LoggerFactory.getLogger(MD5Util.class);
	
    // 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public MD5Util() {
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * 将传进来的参数进行MD5加密
     * @param strObj
     * @return
     */
    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            //resultString = new String(strObj.getBytes("UTF-8"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException ex) {
            PrintUtil.printException("在进行MD5加密时出现了异常，加密的文字为：\"" + strObj + "\"", logger, ex);
        } catch (UnsupportedEncodingException e) {
			PrintUtil.printException("在进行MD5加密时出现了异常，加密的文字为：\"" + strObj + "\"", logger, e);
		} catch (Exception exce) {
			PrintUtil.printException("在进行MD5加密时出现了异常，加密的文字为：\"" + strObj + "\"", logger, exce);
		}
        return resultString;
    }

}