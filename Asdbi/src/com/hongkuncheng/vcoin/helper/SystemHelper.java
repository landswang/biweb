package com.hongkuncheng.vcoin.helper;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;


/**
 * 系统帮助类
 * @author 洪坤成
 *
 */
public class SystemHelper {
	
	//Agent关键词
	private static String[] keywords = {"Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser"};
	
	/**
	 * 获取设备类型
	 * @return
	 */
	public static String getDevice(HttpServletRequest request) {
		String result = "";
		String agent = request.getHeader("User-Agent");
		if (!agent.contains("Windows NT") || (agent.contains("Windows NT") && agent.contains("compatible; MSIE 9.0;"))) {
			if (!agent.contains("Windows NT") && !agent.contains("Macintosh")) {
				for (String item : keywords) {
                    if (agent.contains(item)) {
                    	result = "_m";
                        break;
                    }  
                }  
            }
        }
		return result;
	}
	
	
	/**
	 * 获取当前网络ip
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request){
		String ipAddress = request.getHeader("x-forwarded-for");
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
				//根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (Exception e) {
					e.printStackTrace();
				}
				ipAddress= inet.getHostAddress();
			}
		}
		if(ipAddress != null && ipAddress.length() > 15){
			if(ipAddress.indexOf(",") > 0){
				ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
			}
		}
		return ipAddress; 
	}
	
}
