package com.salon.backstage.common.util;

import org.apache.commons.io.FilenameUtils;

public class Tool {
	public static String getFileSuffix(String fileName) {
		return FilenameUtils.getExtension(fileName);
	}

	public static void main(String[] args) {
		System.out.println(getFileSuffix("xyz.png.tmp"));
	}
}
