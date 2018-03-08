package com.hongkuncheng.vcoin.ueditor;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

public class PathFormat {
	
	private static final String PATH = "path";
	private static final String TIME = "time";
	private static final String FULL_YEAR = "yyyy";
	private static final String YEAR = "yy";
	private static final String MONTH = "mm";
	private static final String DAY = "dd";
	private static final String HOUR = "hh";
	private static final String MINUTE = "ii";
	private static final String SECOND = "ss";
	private static final String RAND = "rand";
	
	public static String parse(String input, HttpServletRequest request) {
		Pattern pattern = Pattern.compile("\\{([^\\}]+)\\}", Pattern.CASE_INSENSITIVE );
		Matcher matcher = pattern.matcher(input);
		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
			matcher.appendReplacement(sb, PathFormat.getString(matcher.group(1), request));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	/**
	 * 格式化路径, 把windows路径替换成标准路径
	 * @param input 待格式化的路径
	 * @return 格式化后的路径
	 */
	public static String format(String input) {
		return input.replace("\\", "/");
	}

	public static String parse(String input, String filename, HttpServletRequest request) {
		Pattern pattern = Pattern.compile("\\{([^\\}]+)\\}", Pattern.CASE_INSENSITIVE );
		Matcher matcher = pattern.matcher(input);
		String matchStr = null;
		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
			matchStr = matcher.group(1);
			if(matchStr.indexOf("filename") != -1) {
				filename = filename.replace("$", "\\$").replaceAll("[\\/:*?\"<>|]", "");
				matcher.appendReplacement(sb, filename);
			} else {
				matcher.appendReplacement(sb, PathFormat.getString(matchStr, request));
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
		
	private static String getString(String pattern, HttpServletRequest request) {
		pattern = pattern.toLowerCase();
		Date date = new Date();
		if(pattern.indexOf(PathFormat.PATH) != -1) {
			return PathFormat.getPath(request);
		} else if(pattern.indexOf(PathFormat.TIME) != -1) {
			return String.valueOf(date.getTime());
		} else if(pattern.indexOf(PathFormat.FULL_YEAR) != -1) {
			return String.valueOf(date.getYear() + 1900);
		} else if(pattern.indexOf(PathFormat.YEAR) != -1) {
			return String.valueOf(date.getYear() - 100);
		} else if(pattern.indexOf(PathFormat.MONTH) != -1) {
			return String.valueOf(String.format("%02d", date.getMonth() + 1));
		} else if(pattern.indexOf(PathFormat.DAY) != -1) {
			return String.valueOf(String.format("%02d", date.getDate()));
		} else if(pattern.indexOf(PathFormat.HOUR) != -1) {
			return String.valueOf(String.format("%02d", date.getHours()));
		} else if(pattern.indexOf(PathFormat.MINUTE) != -1) {
			return String.valueOf(String.format("%02d", date.getMinutes()));
		} else if(pattern.indexOf(PathFormat.SECOND) != -1) {
			return String.valueOf(String.format("%02d", date.getSeconds()));
		} else if(pattern.indexOf(PathFormat.RAND) != -1) {
			return PathFormat.getRandom(pattern);
		}
		return pattern;
	}
	
	public static String getPath(HttpServletRequest request) {
		Object editorpath = request.getSession().getAttribute("editorpath");
		return editorpath != null ? editorpath.toString() : "visitor";
	}

	/**
	 * 获取随机数
	 * @param pattern
	 * @return
	 */
	public static String getRandom(String pattern) {
		int length = 0;
		pattern = pattern.split(":")[1].trim();
		length = Integer.parseInt(pattern);
		return(Math.random() + "").replace(".", "").substring(0, length);
	}
}
