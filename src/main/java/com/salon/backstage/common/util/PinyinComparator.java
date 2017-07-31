package com.salon.backstage.common.util;

import java.util.Arrays;
import java.util.Comparator;

import net.sourceforge.pinyin4j.PinyinHelper;

public class PinyinComparator implements Comparator<String> {
	@Override
	public int compare(String o1, String o2) {
		return toPinyinString(o1).compareTo(toPinyinString(o2));
	}

	private String toPinyinString(String str) {
		StringBuilder sb = new StringBuilder();
		String[] arr = null;
		for (int i = 0; i < str.length(); i++) {
			arr = PinyinHelper.toHanyuPinyinStringArray(str.charAt(i));
			if (arr != null && arr.length > 0) {
				for (String string : arr) {
					sb.append(string);
				}
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String[] arr = { "王二六", "张三", "李四", "王五", "赵六", "JAVA", "123", "怡情", "北京", "上海" };
		Arrays.sort(arr, new PinyinComparator());
		for (String string : arr) {
			System.out.println(string);
		}
	}
}
