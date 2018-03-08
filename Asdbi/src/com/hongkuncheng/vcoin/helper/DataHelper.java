package com.hongkuncheng.vcoin.helper;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/**
 * 数据帮助类
 * @author 洪坤成
 *
 */
public class DataHelper {
	
	/**
	 * 将String数组转为Integer数组
	 * @param inputArray
	 * @return
	 */
	public static Integer[] convertToIntegerArray(String[] inputArray){
		Integer[] result = new Integer[inputArray.length];
		for (int i = 0; i < result.length; i++) result[i] = Integer.parseInt(inputArray[i]);
		return result;
	}
	
	/**
	 * 将Integer数组转为String数组
	 * @param inputArray
	 * @return
	 */
	public static String[] convertToStringArray(Integer[] inputArray){
		String[] result = new String[inputArray.length];
		for (int i = 0; i < result.length; i++) result[i] = inputArray[i].toString();
		return result;
	}
	
	/**
	 * 判断数组元素是否相等（忽略排序）
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static boolean equalsArray(Object[] array1, Object[] array2){
		boolean result = true;
		if (array1.length == array2.length) {
			for (Object obj1 : array1) {
				boolean temp = false;
				for (Object obj2 : array2) {
					if (obj1.toString().equals(obj2.toString())) {
						temp = true;
						break;
					}
				}
				result = result && temp;
				if (temp) continue;
			}
		} else {
			result = false;
		}
		return result;
	}
	
	/**
	 * 设计页数
	 * @param recordcount
	 * @param datasize
	 * @return
	 */
	public static int calcPageCount(int recordcount, int datacount) {
		int pagecount = recordcount / datacount;
		if (recordcount % datacount != 0) pagecount++;
		return pagecount; 
	}
	
	/**
	 * 获取0-end之间的随机数
	 * @param end
	 * @return
	 */
	public static int getRandom(int max) {
		return getRandom(0, max);
	}
	
	/**
	 * 获取start-end之间的随机数
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getRandom(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}
	
	/**
	 * 获取一个数组中的随机数
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> T getRandom(T[] array) {
		return getRandom(array, 1).get(0);
	}
	
	/**
	 * 获取一个数组中的随机数
	 * @param <T>
	 * @param array
	 * @param count
	 * @return
	 */
	public static <T> List<T> getRandom(T[] array, int count) {
		int length = array.length;
		List<T> ts = new ArrayList<T>();
		do {
			T t = array[getRandom(length - 1)];
			if (!ts.contains(t)) ts.add(t);
		} while (ts.size() < count);
		return ts;
	}
	
	/**
	 * 16进制转字符串
	 * @param number
	 * @return
	 */
    public static String encodeHex(Integer number) {
    	return encodeHex(new BigDecimal(String.valueOf(number)));
    }

    /**
	 * 16进制转字符串
	 * @param number
	 * @return
	 */
    public static String encodeHex(Double number) {
    	return encodeHex(new BigDecimal(String.valueOf(number)));
    }

    /**
	 * 16进制转字符串
	 * @param number
	 * @return
	 */
    public static String encodeHex(String number) {
    	return encodeHex(new BigDecimal(number));
    }
    
	/**
	 * 16进制转字符串
	 * @param number
	 * @return
	 */
    public static String encodeHex(BigDecimal number) {
    	String hex = "0x";
    	BigDecimal sixteen = new BigDecimal(String.valueOf(16));
    	for(int i = 0; i >= 0; i++) {
    		BigDecimal powi = new BigDecimal(String.valueOf(Math.pow(16, i)));
    		if(number.divide(powi, 18, BigDecimal.ROUND_FLOOR).compareTo(sixteen) >= 0) {
    			continue;
    		}else {
    			for(int j=i; j >= 0; j--) {
    				BigDecimal powj = new BigDecimal(String.valueOf(Math.pow(16, j)));
    				int num = number.divide(powj, 18, BigDecimal.ROUND_FLOOR).intValue();
        			hex += Integer.toHexString(num);
        			number = number.divideAndRemainder(powj)[1];
    			}
    			break;
			}
    	}
    	return hex;
	}
	
	/**
	 * 字符串转16进度
	 * @param hex
	 * @return
	 */
	public static BigDecimal decodeHex(String hex) {
    	String subHex = hex.substring(2, hex.length());
    	char[] array = subHex.toCharArray();
    	BigDecimal number = new BigDecimal(0.0);
    	for(int i = array.length - 1; i >= 0; i--) {
    		BigDecimal num = new BigDecimal(String.valueOf(Integer.valueOf(String.valueOf(array[i]), 16)));
    		BigDecimal pow = new BigDecimal(String.valueOf(Math.pow(16, array.length - 1 - i)));
    		number = number.add(num.multiply(pow));
    	}
    	return number;
	}
	
}
