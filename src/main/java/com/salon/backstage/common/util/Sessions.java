package com.salon.backstage.common.util;

import javax.servlet.http.HttpSession;

import cn.jpush.api.report.UsersResult.User;

public class Sessions {
	public static String attr_name_logined_user = "loginedUser";

	public static void setLoginedUser(Object user, HttpSession session) {
		session.setAttribute(attr_name_logined_user, user);
	}
	public static void removeLoginedUser(HttpSession session) {
		session.removeAttribute(attr_name_logined_user);
	}

	public static User getLoginedUser(HttpSession session) {
		return (User) session.getAttribute(attr_name_logined_user);
	}
	public static boolean isLogined(HttpSession session) {
		return getLoginedUser(session) != null;
	}
}
