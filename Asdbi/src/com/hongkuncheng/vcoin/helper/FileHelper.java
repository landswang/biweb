package com.hongkuncheng.vcoin.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 文件帮助类
 * @author 洪坤成
 *
 */
public class FileHelper {

	private static BASE64Encoder base64Encoder;
	private static BASE64Decoder base64Decoder;
	
	static {
		base64Encoder = new BASE64Encoder();
		base64Decoder = new BASE64Decoder();
	}
	
	
	/**
	 * 检测路径文件是否存在
	 * @param path
	 * @return
	 */
	public static boolean Exists(String path){
		File file = new File(path);
		return file.exists();
	}
	
	/**
	 * 读取文件
	 * @param path
	 * @return
	 */
	public static String Read(String path) {
        File file = new File(path);
        return Read(file);
    }

	/**
	 * 读取文件
	 * @param file
	 * @return
	 */
	public static String Read(File file) {
        if(!file.exists()||file.isDirectory()) return null;
        if (file.length() == 0) {
			return "";
		} else {
			BufferedReader br;
	        StringBuffer result = new StringBuffer();
	        FileInputStream inputStream = null;
	        InputStreamReader streamReader = null;
			try {
				inputStream = new FileInputStream(file);
				streamReader = new InputStreamReader(inputStream, "UTF-8");
				br = new BufferedReader(streamReader);
				String temp = br.readLine();
		        while(temp != null){
		        	result.append(temp + "\n");
		            temp = br.readLine();
		        }
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					streamReader.close();
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	        return result.toString();
		}
    }
	
	/**
	 * 写入文件
	 * @param path
	 * @param contents
	 */
	public static void write(String path, String contents) {
		File dir = new File(path.substring(0, path.replace("\\", "/").lastIndexOf("/")));
		if (!dir.exists()) dir.mkdirs();
		File file = new File(path);
		write(file, contents);
    }
	
	/**
	 * 写入文件
	 * @param file
	 * @param contents
	 */
	public static void write(File file, String contents) {
		FileOutputStream outputStream = null;
		OutputStreamWriter streamWrite = null;
		try {
			outputStream = new FileOutputStream(file);
			streamWrite = new OutputStreamWriter(outputStream, "UTF-8");
			BufferedWriter bufferWritter = new BufferedWriter(streamWrite); 
	        bufferWritter.write(contents);
	        bufferWritter.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
	
	/**
	 * 获取文件列表
	 * @param path
	 * @return
	 */
	public static List<File> getFiles(String path) {
		File file = new File(path);
		return getFiles(file);
	}
	
	/**
	 * 获取文件列表
	 * @param file
	 * @return
	 */
	public static List<File> getFiles(File file) {
		List<File> files = new ArrayList<File>();
		File[] temps = file.listFiles();
		for (File temp : temps) if (temp.isFile()) files.add(temp);
		return files;
	}
	
	/**
	 * 获取所有子目录文件列表
	 * @param path
	 * @return
	 */
	public static List<File> getAllFiles(String path){
		File file = new File(path);
		return getAllFiles(file);
	}
	
	/**
	 * 获取所有子目录文件列表
	 * @param file
	 * @return
	 */
	public static List<File> getAllFiles(File file){
		List<File> files = new ArrayList<File>();
		File[] temps = file.listFiles();
		for (File temp : temps) {
			if (temp.isFile()) {
				files.add(temp);
			} else {
				files.addAll(Arrays.asList(temp.listFiles()));
			}
		}
		return files;
	}
	
	/**
	 * 获取文件夹列表
	 * @param path
	 * @return
	 */
	public static List<File> getDirectorys(String path) {
		File file = new File(path);
		return getDirectorys(file);
	}
	
	/**
	 * 获取文件夹列表
	 * @param file
	 * @return
	 */
	public static List<File> getDirectorys(File file) {
		List<File> files = new ArrayList<File>();
		File[] temps = file.listFiles();
		for (File temp : temps) if (temp.isDirectory()) files.add(temp);
		return files;
	}
	
	/**
	 * 获取文件二进制流
	 * @param path
	 * @return
	 */
	public static byte[] getBytes(String path){
		try {
			return getBytes(new FileInputStream(path));
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取文件二进制流
	 * @param file
	 * @return
	 */
	public static byte[] getBytes(File file){
		try {
			return getBytes(new FileInputStream(file));
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取文件二进制流
	 * @param stream
	 * @return
	 */
	private static byte[] getBytes(FileInputStream stream){
		try {
			byte[] bytes = new byte[stream.available()];
			stream.read(bytes);
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取文件Base64编码
	 * @param path
	 * @return
	 */
	public static String getBase64(String path){
		return base64Encoder.encode(getBytes(path)).replace("\r", "").replace("\n", "");
	}
	
	/**
	 * 获取文件Base64编码
	 * @param file
	 * @return
	 */
	public static String getBase64(File file){
		return base64Encoder.encode(getBytes(file)).replace("\r", "").replace("\n", "");
	}
	
	/**
	 * 根据Base64保存并获取文件
	 * @param base64
	 * @param path
	 * @return
	 */
	public static File saveByBase64(String base64, String path){
		File dir = new File(path.substring(0, path.replace("\\", "/").lastIndexOf("/")));
		if (!dir.exists()) dir.mkdirs();
		try {
			byte[] buffer = base64Decoder.decodeBuffer(base64);
			FileOutputStream out = new FileOutputStream(path);
			out.write(buffer);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(path);
		return file.exists() ? file : null;
	}
	
	/**
	 * 下载文件本地
	 * @param fileurl
	 * @param path
	 */
	public static void  download(String fileurl, String path){  
		try {
			URL url = new URL(fileurl);
	        HttpURLConnection connection = (HttpURLConnection)url.openConnection();    
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0)");
	        InputStream inputStream = connection.getInputStream();    
	        byte[] buffer = new byte[inputStream.available()];    
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	        int len = 0;   
	        while((len = inputStream.read(buffer)) != -1) bos.write(buffer, 0, len);
	        inputStream.close();
	        bos.close();
	        connection.disconnect();
	        path = path.replace("\\", "/");
			File dir = new File(path.substring(0, path.lastIndexOf("/")));
			if (!dir.exists()) dir.mkdirs(); 
	        File file = new File(path);      
	        FileOutputStream outputStream = new FileOutputStream(file);       
	        outputStream.write(bos.toByteArray());
	        outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
    }   
	
	/**
	 * 删除文件
	 * @param path
	 */
	public static void delete(String path){
		File file = new File(path);
		file.delete();
	}
	
	/**
	 * 删除文件夹及其所有子文件
	 * @param path
	 */
	public static void deleteDir(String path) {
		File file = new File(path);
		deleteDir(file);
    }
	
	/**
	 * 删除文件夹及其所有子文件
	 * @param path
	 */
	public static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                deleteDir(new File(dir, children[i]));
            }
        }
        dir.delete();
    }
	
}
