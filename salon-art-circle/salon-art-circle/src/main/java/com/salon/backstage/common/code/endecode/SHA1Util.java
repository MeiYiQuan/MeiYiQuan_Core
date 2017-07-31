package com.salon.backstage.common.code.endecode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1Util {

	
	/**
	 * SHA1加密方法
	 * @param str 要加密的文字
	 * @return
	 */
	public static String SHA1(String str) {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.update(str.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
}
