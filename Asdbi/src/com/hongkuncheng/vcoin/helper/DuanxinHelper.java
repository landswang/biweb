package com.hongkuncheng.vcoin.helper;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * 短信帮助类
 * @author 洪坤成
 *
 */
public class DuanxinHelper {

//	public static String account = ConfigHelper.getValue("duanxin.account");
//	public static String password = ConfigHelper.getValue("duanxin.password");
	public static String apikey = ConfigHelper.getValue("duanxin.apikey");
	
	//发送短信
	public static boolean send(String mobile, String contents) { 
		//凌凯短信
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("CorpID", account);
//		params.put("Pwd", password);
//		params.put("Mobile", mobile);
//		params.put("Content", contents);
//		String result = HttpHelper.post("https://sdk2.028lk.com/sdk2/BatchSend2.aspx", params);
//		return Integer.parseInt(result) > 0;
		//铁壳短信
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Basic " + new String(Base64.encode(new String("api:" + apikey).getBytes())));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", mobile);
		params.put("message", contents + "【bi网】");
		String json = HttpHelper.post("https://sms-api.luosimao.com/v1/send.json", headers, params);
		JSONObject jsonObj = new JSONObject(json);
		return jsonObj.getInt("error") == 0;
	}
	
	//获取余额
	public static int balance() {
		//铁壳短信
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Basic " + new String(Base64.encode(new String("api:" + apikey).getBytes())));
		String json = HttpHelper.get("http://sms-api.luosimao.com/v1/status.json", headers);
		JSONObject jsonObj = new JSONObject(json);
		return jsonObj.getInt("deposit");
	}
	
}
