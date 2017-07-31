package com.salon.backstage.common.util;

import org.apache.commons.lang3.StringUtils;

public class Strings {
	public static String trimAndNotNull(String string) {
		return StringUtils.isBlank(string) ? "" : string.trim();
	}
}
