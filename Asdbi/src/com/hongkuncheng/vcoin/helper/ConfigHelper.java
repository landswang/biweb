package com.hongkuncheng.vcoin.helper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件帮助类
 * @author 洪坤成
 *
 */
public class ConfigHelper {

	private static Properties prop;
	
	static {
		prop = new Properties();
		InputStream stream = null;
		try {
			stream = new FileInputStream(System.getProperty("web.root") + "WEB-INF/config.properties");
			prop.load(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public static String getValue(String key){
		return prop.getProperty(key);
	}
	
}
