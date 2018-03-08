package com.hongkuncheng.vcoin.helper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.util.StringUtils;

/**
 * 字符串帮助类
 * @author 洪坤成
 *
 */
public class StringHelper {
	
	/**
	 * MD5加密
	 * @param input
	 * @return
	 */
	public static String md5(String input){
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = input.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
	/**
	 * 拼接对象数组
	 * @param array
	 * @param separator
	 * @return
	 */
	public static String join(Object[] array, String separator) {
	    if (array == null) return "";
	    if (separator == null) separator = "";
	    StringBuffer buf = new StringBuffer();
	    for (int i = 0; i < array.length; ++i) {
			buf.append(array[i]);
			if (i >= array.length - 1) continue; buf.append(separator);
	    }
	    return buf.toString();
	}
	
	/**
	 * 过滤Html标签
	 * @param input
	 * @return
	 */
	public static String escapeHtml(String input) {
		if (input == null) return "";
		String result = escapeXss(input);
		Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(result);
		result = m_html.replaceAll("");
		Pattern p_space = Pattern.compile("\\s|　", Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(result);
		result = m_space.replaceAll("");
		Pattern p_special = Pattern.compile("\\&[a-zA-Z]{1,10};", Pattern.CASE_INSENSITIVE);
		Matcher m_special = p_special.matcher(result);
		result = m_special.replaceAll("");
		Pattern p_ueditor = Pattern.compile("_ueditor_page_break_tag_", Pattern.CASE_INSENSITIVE);
		Matcher m_ueditor = p_ueditor.matcher(result);
		result = m_ueditor.replaceAll("");
		return result;
	}
	
	/**
	 * 过滤XSS注入标签
	 * @param input
	 * @return
	 */
	public static String escapeXss(String input) {
		if (input == null || input.isEmpty()) return "";
		String result = "";
		input = StringEscapeUtils.unescapeHtml(input);
		Pattern p_script1 = Pattern.compile("<script[^>]*?>[\\s\\S]*?<\\/script>", Pattern.CASE_INSENSITIVE);
		Matcher m_script1 = p_script1.matcher(input);
		result = m_script1.replaceAll("");
		Pattern p_script2 = Pattern.compile("<script[^>]*?>", Pattern.CASE_INSENSITIVE);
		Matcher m_script2 = p_script2.matcher(result);
		result = m_script2.replaceAll("");
		Pattern p_script3 = Pattern.compile("<\\/script>", Pattern.CASE_INSENSITIVE);
		Matcher m_script3 = p_script3.matcher(result);
		result = m_script3.replaceAll("");
		Pattern p_style1 = Pattern.compile("<style[^>]*?>[\\s\\S]*?<\\/style>", Pattern.CASE_INSENSITIVE);
		Matcher m_style1 = p_style1.matcher(result);
		result = m_style1.replaceAll("");
		Pattern p_style2 = Pattern.compile("<style[^>]*?>", Pattern.CASE_INSENSITIVE);
		Matcher m_style2 = p_style2.matcher(result);
		result = m_style2.replaceAll("");
		Pattern p_style3 = Pattern.compile("<\\/style>", Pattern.CASE_INSENSITIVE);
		Matcher m_style3 = p_style3.matcher(result);
		result = m_style3.replaceAll("");
		Pattern p_iframe1 = Pattern.compile("<iframe[^>]*?>[\\s\\S]*?<\\/iframe>", Pattern.CASE_INSENSITIVE);
		Matcher m_iframe1 = p_iframe1.matcher(result);
		result = m_iframe1.replaceAll("");
		Pattern p_iframe2 = Pattern.compile("<iframe[^>]*?>", Pattern.CASE_INSENSITIVE);
		Matcher m_iframe2 = p_iframe2.matcher(result);
		result = m_iframe2.replaceAll("");
		Pattern p_iframe3 = Pattern.compile("<\\/iframe>", Pattern.CASE_INSENSITIVE);
		Matcher m_iframe3 = p_iframe3.matcher(result);
		result = m_iframe3.replaceAll("");
		Pattern p_comments = Pattern.compile("<!--.*?-->", Pattern.CASE_INSENSITIVE);
		Matcher m_comments = p_comments.matcher(result);
		result = m_comments.replaceAll("");
		return result;
	}
	
	/**
	 * 过滤SQL注入字符
	 * @param input
	 * @return
	 */
	public static String escapeSql(String input) {
		return StringUtils.replace(input, "'", "''");
	}
	
	/**
	 * 截取字符串
	 * @param input
	 * @param length
	 * @return
	 */
	public static String substring(String input, int length){
		if (input.length() <= length || length <= 2) return input;
		else return input.substring(0, length - 2) + "..";
	}
	
	/**
	 * 检测是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		if (str.isEmpty() || str.length() > 10) return false;
		Pattern pattern = Pattern.compile("^-?[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if(!isNum.matches()) return false;
		return true;
	}	
	
	/**
	 * 字符串转Unicode编码
	 * @param input
	 * @return
	 */
	public static String enUnicode(String input) {       
		char[] utfBytes = input.toCharArray();             
		String unicodeBytes = "";              
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
			String hexB = Integer.toHexString(utfBytes[byteIndex]);                     
			if (hexB.length() <= 2) hexB = "00" + hexB;                                      
			unicodeBytes = unicodeBytes + "\\u" + hexB;                 
		}               
		return unicodeBytes;
	}
	
	/**
	 * 将Unicode编码转为字符串
	 * @param input
	 * @return
	 */
	public static String deUnicode(String input) {              
		int start = 0;                
		int end = 0;              
		final StringBuffer buffer = new StringBuffer();                
		while (start > -1) {                   
		    end = input.indexOf("\\u", start + 2);                    
		    String charStr = end == -1 ? input.substring(start + 2, input.length()) : input.substring(start + 2, end);       
		    char letter = (char) Integer.parseInt(charStr, 16);
		    buffer.append(new Character(letter).toString());                  
		    start = end;                
		}                
		return buffer.toString();           
	}
	
	/**
	 * 转换中文字符
	 * @param innput
	 * @return
	 */
	public static String cnEncode(String input) {
		if (ServletActionContext.getRequest().getMethod().equalsIgnoreCase("get")) {
			if (input == null) return null;
			if (input.isEmpty()) return "";
			try {
				return new String(input.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return input;
		}
	}
	
	/**
	 * 某一字符串在另一字符串中出现的次数
	 * @param srcText
	 * @param findText
	 * @return
	 */
	public static int appearNumber(String srcText, String findText) {
		int count = 0;
	    int index = 0;
	    while ((index = srcText.indexOf(findText, index)) != -1) {
	        index = index + findText.length();
	        count++;
	    }
	    return count;
	}
	
}
