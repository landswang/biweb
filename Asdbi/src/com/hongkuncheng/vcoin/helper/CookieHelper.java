package com.hongkuncheng.vcoin.helper;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie帮助类
 * @author 洪坤成
 *
 */
public class CookieHelper {
	
	private static String namePrefix = "com_hongkuncheng_vcoin_";
	
	static {
		namePrefix += ConfigHelper.getValue("app.project.name") + "_";
	}
	
	public static void SetCookie(HttpServletResponse response, String name, String value, boolean isEncrypt){
		Cookie cookie = new Cookie(URLEncoder.encode(URLEncoder.encode(namePrefix + name)), !isEncrypt ? value : DesHelper.encrypt(value));
		cookie.setPath("/");
        response.addCookie(cookie);
	}

	public static void SetCookie(HttpServletResponse response, String name, String value, int expiry, boolean isEncrypt){
		Cookie cookie = new Cookie(URLEncoder.encode(namePrefix + name), !isEncrypt ? value : DesHelper.encrypt(value));
		cookie.setMaxAge(expiry);
		cookie.setPath("/");
        response.addCookie(cookie);
	}

	public static void SetCookie(HttpServletResponse response, String name, String value, String path, boolean isEncrypt){
		Cookie cookie = new Cookie(URLEncoder.encode(namePrefix + name), !isEncrypt ? value : DesHelper.encrypt(value));
		cookie.setPath(path);
        response.addCookie(cookie);
	}

	public static void SetCookie(HttpServletResponse response, String name, String value, int expiry, String path, boolean isEncrypt){
		Cookie cookie = new Cookie(URLEncoder.encode(namePrefix + name), !isEncrypt ? value : DesHelper.encrypt(value));
		cookie.setMaxAge(expiry);
		cookie.setPath(path);
        response.addCookie(cookie);
	}
	
	public static void RemoveCookie(HttpServletResponse response, String name){
		Cookie cookie = new Cookie(URLEncoder.encode(namePrefix + name), null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public static void RemoveCookie(HttpServletResponse response, String name, String path){
		Cookie cookie = new Cookie(URLEncoder.encode(namePrefix + name), null);
		cookie.setMaxAge(0);
		cookie.setPath(path);
		response.addCookie(cookie);
	}
	
	public static String GetCookie(HttpServletRequest request, String name, boolean isDecrypt){
		String value = null;
		Cookie[] cookies = request.getCookies();
		for (int i = 0; cookies != null && i < cookies.length; i++) { 
			if (cookies[i].getName().equals(URLEncoder.encode(namePrefix + name))) {
				value = cookies[i].getValue();
				if (isDecrypt) value = DesHelper.decrypt(value);
				break;
			}
		}
		return value;
	}
	
}
