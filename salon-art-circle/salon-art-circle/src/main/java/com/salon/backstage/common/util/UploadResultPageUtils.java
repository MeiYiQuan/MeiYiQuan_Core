package com.salon.backstage.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

public class UploadResultPageUtils {
	public static String getUploadResultPage(HttpServletRequest request, int success, String message)
			throws UnsupportedEncodingException {
		String url = String.format("%s/upload_result.jsp?success=%d&message=%s", request.getContextPath(), success,
				message);
		String encodeUrl = URLEncoder.encode(url, "utf-8");
		return String.format("/jump.jsp?url=%s", encodeUrl);
	}
}
