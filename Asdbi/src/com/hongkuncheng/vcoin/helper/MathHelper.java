package com.hongkuncheng.vcoin.helper;

import java.math.BigDecimal;

/**
 * 数学帮助类
 * @author 洪坤成
 *
 */
public class MathHelper {
    
    /**
     * 加法运算
     * @param v1 被加数  
     * @param v2 加数 
     * @param scale 精度
     * @return
     */
    public static double add(double v1, double v2, int scale) {    
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.add(b2).setScale(scale, BigDecimal.ROUND_FLOOR).doubleValue();
    }
    
    /**
     * 减法运算
     * @param v1 被减数
     * @param v2 减数
     * @param scale
     * @return
     */
    public static double sub(double v1, double v2, int scale) {    
    	BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.subtract(b2).setScale(scale, BigDecimal.ROUND_FLOOR).doubleValue();
    }
    
    /**
     * 乘法运算
     * @param v1 被乘数
     * @param v2 乘数
     * @param scale
     * @return
     */
    public static double mul(double v1, double v2, int scale) {    
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_FLOOR).doubleValue();
    }
    
    /**   
     * 除法运算
     * @param v1 被除数   
     * @param v2 除数   
     * @param scale
     * @return 
     */    
    public static double div(double v1, double v2, int scale) {    
        if(v2 < 0) throw new IllegalArgumentException("The scale must be a positive integer or zero");
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_FLOOR).doubleValue();
    }    
  
    /**   
     * 四舍五入
     * @param v
     * @param scale
     * @return
     */    
    public static double round(double v, int scale) {
        if(scale < 0) throw new IllegalArgumentException("The scale must be a positive integer or zero");
        BigDecimal b1 = new BigDecimal(String.valueOf(v));
        BigDecimal b2 = new BigDecimal("1");
        return b1.divide(b2, scale, BigDecimal.ROUND_FLOOR).doubleValue();
    }    
    
    /**
     * 精确小数位
     * @param v
     * @param scale
     * @return
     */
    public static double places(double v, int scale) {
        if(scale < 0) throw new IllegalArgumentException("The scale must be a positive integer or zero");
		BigDecimal b1 = new BigDecimal(String.valueOf(v));
        BigDecimal b2 = b1.setScale(scale, b1.ROUND_DOWN);
		return b2.doubleValue();
    }
    
    /**   
     * 转换Float
     * @param v
     * @return 
     */    
    public static float convertsToFloat(double v) {
        BigDecimal b = new BigDecimal(String.valueOf(v));
        return b.floatValue();
    }    
       
    /**   
    * 转换Int
    * @param v  
    * @return
    */    
    public static int convertsToInt(double v) {
        BigDecimal b = new BigDecimal(String.valueOf(v));
        return b.intValue();
    }    
  
    /**   
    * 转换Long  
    * @param v
    * @return
    */    
    public static long convertsToLong(double v) {
        BigDecimal b = new BigDecimal(String.valueOf(v));
        return b.longValue();
    }    
  
    /**   
    * 返回大值   
    * @param v1
    * @param v2
    * @return
    */    
    public static double returnMax(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.max(b2).doubleValue();
    }    
  
    /**   
    * 返回小值   
    * @param v1
    * @param v2
    * @return
    */    
    public static double returnMin(double v1, double v2) {    
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.min(b2).doubleValue();
    }    
  
    /**   
    * 对比数字   
    * @param v1
    * @param v2
    * @return
    */    
    public static int compareTo(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.compareTo(b2);
    }
	
}
