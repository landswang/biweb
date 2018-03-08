package com.hongkuncheng.vcoin.helper;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES加密解密类
 * @author 洪坤成
 *
 */
public class DesHelper {

	//默认key
	private static final String key = "hongkuncheng1987";
	
	/**
	 * 加密
	 * @param input
	 * @return
	 */
	public static String encrypt(String input) { 
		return encrypt(input, key);
	}
	
	/**
	 * 加密
	 * @param input
	 * @param key
	 * @return
	 */
	public static String encrypt(String input, String key) { 
		String result = "";
		if (input != null) {
			try{
				SecureRandom random = new SecureRandom();
				DESKeySpec desKey = new DESKeySpec(key.getBytes());
				SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
				SecretKey securekey = keyFactory.generateSecret(desKey);
				Cipher cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
				result = new sun.misc.BASE64Encoder().encode(cipher.doFinal(input.getBytes())).replace("\r", "\\r").replace("\n", "\\n");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 解密
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String input) {
		return decrypt(input, key);
	}
		
	/**
	 * 解密
	 * @param input
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String input, String key) {
		String result = "";
		if (input != null && input.length() % 4 == 0) {
			try {
				SecureRandom random = new SecureRandom();
				DESKeySpec desKey = new DESKeySpec(key.getBytes());
				SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
				SecretKey securekey = keyFactory.generateSecret(desKey);
				Cipher cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.DECRYPT_MODE, securekey, random);
				result = new String(cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(input.replace("\\r", "\r").replace("\\n", "\n"))));  
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
