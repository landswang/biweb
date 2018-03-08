package com.hongkuncheng.vcoin.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期帮助类
 * @author 洪坤成
 *
 */
public class DateHelper {
	
	/**
	 * 获取目录名
	 * @return
	 */
	public static String getDirectory(){
		Date date = new Date();
		return (date.getYear() + 1900) + "" + String.format("%02d", date.getMonth() + 1) + "" + String.format("%02d", date.getDate());
	}
	
	/**
	 * 获取文件名
	 * @return
	 */
	public static String getFilename(){
		return getFilename("");
	}
	
	/**
	 * 获取文件名（带后缀）
	 * @param suffix
	 * @return
	 */
	public static String getFilename(String suffix){
		Date date = new Date();
		return String.format("%02d", date.getHours()) + "" + String.format("%02d", date.getMinutes()) + "" + String.format("%02d", date.getSeconds()) + "" + DataHelper.getRandom(100000000, 999999999) + suffix;
	}
	
	/**
	 * 获取目录+/+文件名
	 * @return
	 */
	public static String getFilepath() {
		return getFilepath("");
	}
	
	/**
	 * 获取目录+/+文件名（带后缀）
	 * @param suffix
	 * @return
	 */
	public static String getFilepath(String suffix) {
		Date date = new Date();
		return (date.getYear() + 1900) + "" + String.format("%02d", date.getMonth() + 1) + "" + String.format("%02d", date.getDate()) + "/" + String.format("%02d", date.getHours()) + "" + String.format("%02d", date.getMinutes()) + "" + String.format("%02d", date.getSeconds()) + "" + DataHelper.getRandom(100000000, 999999999) + suffix;
	}
	
	/**
	 * 将字符串转换为日期
	 * @param date
	 * @return
	 */
	public static Date convertToDate(String date) {
		Date retValue = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			retValue = sdf.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retValue;
	}
	
	/**
	 * 将字符串转换为日期+时间
	 * @param date
	 * @return
	 */
	public static Date convertToDatetime(String date) {
		Date retValue = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			retValue = sdf.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retValue;
	}
	
	/**
	 * 获取某年某月的天数
	 * @param year
	 * @param month
	 * @return
	 */
	public static int daysInMonth(int year, int month){
		int[] months = {31,28,31,30,31,30,31,31,30,31,30,31};
		if((year%4==0 && year%100 != 0)|| year%400==0 ) months[1]++;
		return months[--month];
	}
	
}
