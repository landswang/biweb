package com.hongkuncheng.vcoin.helper;

import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;

/**
 * HTTP帮助类
 * @author 洪坤成
 *
 */
public class HttpHelper {
	
	//协议
	public static String protocol = ConfigHelper.getValue("app.protocol") + "://";

	/**
	 * 获取当前IP或域名+端口
	 * @param request
	 * @return
	 */
	public static String getHost(HttpServletRequest request) {
		String host = protocol + request.getServerName();
		int port = request.getServerPort();
		if (port != 80 && port != 443) host += ":" + port;
		return host;
	}

	/**
	 * 发送GET提交
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		return get(url, null, "UTF-8");
	}

	/**
	 * 发送GET提交
	 * @param url
	 * @return
	 */
	public static String get(String url, Map<String, String> headers) {
		return get(url, headers, "UTF-8");
	}

	/**
	 * 发送GET提交
	 * @param url
	 * @return
	 */
	public static String get(String url, String charset) {
		return get(url, null, charset);
	}

	/**
	 * 发送GET提交
	 * @param url
	 * @param charset
	 * @return
	 */
	public static String get(String url, Map<String, String> headers, String charset) {
		HttpURLConnection connection = null;
		InputStreamReader streamReader = null;
		StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0)");
			if (headers != null) for (String key : headers.keySet()) connection.setRequestProperty(key, headers.get(key));
			connection.setConnectTimeout(60000);
			connection.setReadTimeout(60000);
			connection.connect();  
			String line;
			streamReader = new InputStreamReader(connection.getResponseCode() == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream(), charset);
			BufferedReader responseReader = new BufferedReader(streamReader);
			while ((line = responseReader.readLine()) != null) result.append(line).append("\n");
			if (result.length() > 0) result.delete(result.length() - 1, result.length());
			responseReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
        	try {
				if (streamReader != null) streamReader.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				connection.disconnect();
			}
        }
        return result.toString();
	}
	
	/**
	 * 发送POST提交
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, String params) {
		return post(url, null, params);
	}
	
	/**
	 * 发送POST提交
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, String params) {
		return post(url, headers, params, "UTF-8");
	}
	
	/**
	 * 发送POST提交
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, String params, String charset) {
		HttpURLConnection connection = null;
		InputStreamReader streamReader = null;
		StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0)");
			if (headers != null) for (String key : headers.keySet()) connection.setRequestProperty(key, headers.get(key));
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000);
			connection.setReadTimeout(60000);
			connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), charset.toUpperCase());
            out.append(params);
            out.flush();
            out.close();
            String line;
			streamReader = new InputStreamReader(connection.getResponseCode() == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream(), charset);
			BufferedReader responseReader = new BufferedReader(streamReader);
			while ((line = responseReader.readLine()) != null) result.append(line).append("\n");
			if (result.length() > 0) result.delete(result.length() - 1, result.length());
			responseReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
        	try {
				if (streamReader != null) streamReader.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				connection.disconnect();
			}
        }
        return result.toString();
	}
	
	/**
	 * 发送POST提交
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, Object> params) {
		return post(url, null, params, "UTF-8");
	}
	
	/**
	 * 发送POST提交
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, Map<String, Object> params) {
		return post(url, headers, params, "UTF-8");
	}

	/**
	 * 发送POST提交
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, Map<String, Object> params, String charset) {
		StringBuilder contentType = new StringBuilder();
		String boundary = StringHelper.md5(String.valueOf(DataHelper.getRandom(10000))).substring(0, 10).toLowerCase();
		int urlLength = 0;
		boolean havaBinaryData = false;
		boolean isMultipart = false;
		for (String key : params.keySet()) {
			urlLength += key.length() + params.get(key).toString().length() + 1;
			if (!havaBinaryData && params.get(key).getClass().equals(File.class)) havaBinaryData = true;
		}
		if (--urlLength <= 2048 && !havaBinaryData) {
			contentType.append("application/x-www-form-urlencoded;charset=" + charset);
		} else {
			contentType.append("multipart/form-data;charset=" + charset);
			contentType.append("; boundary=" + boundary);
			isMultipart = true;
		}
		HttpURLConnection connection = null;
		InputStreamReader streamReader = null;
		StringBuilder result = new StringBuilder();
		try {
			URL realUrl = new URL(url);
			connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestProperty("Content-Type", contentType.toString());
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0)");
			if (headers != null) for (String key : headers.keySet()) connection.setRequestProperty(key, headers.get(key));
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000);
			connection.setReadTimeout(60000);
			StringBuilder httpContent = new StringBuilder();
			if (!isMultipart) {
				PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
				for (String key : params.keySet()) {
					String value = params.get(key).toString();
					httpContent.append("&" + key + "=" + URLEncoder.encode(value));
				}
				httpContent.deleteCharAt(0);
				printWriter.print(httpContent.toString());
				printWriter.flush();
				printWriter.close();
			} else {
				OutputStream outputStream = connection.getOutputStream();
				for (String key : params.keySet()) {
					httpContent.append("--" + boundary + "\r\n");
					if (!params.get(key).getClass().equals(File.class)) {
						String value = params.get(key).toString();
						httpContent.append("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n");
						httpContent.append(value + "\r\n");
						outputStream.write(httpContent.toString().getBytes());
					} else {
						File value = (File) params.get(key);
						httpContent.append("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + value.getName() + "\"\r\n");
						httpContent.append("Content-Type: application/octet-stream\r\n");
						httpContent.append("Content-Transfer-Encoding: binary\r\n\r\n");
						outputStream.write(httpContent.toString().getBytes());
						outputStream.write(FileHelper.getBytes(value));
						outputStream.write(("\r\n").getBytes());
					}
				}
				outputStream.write(("--" + boundary + "--\r\n").getBytes());
				outputStream.flush();
				outputStream.close();
			}
			String line;
			streamReader = new InputStreamReader(connection.getResponseCode() == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream(), charset);
			BufferedReader responseReader = new BufferedReader(streamReader);
			while ((line = responseReader.readLine()) != null) result.append(line).append("\n");
			if (result.length() > 0) result.delete(result.length() - 1, result.length());
			responseReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (streamReader != null) streamReader.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				connection.disconnect();
			}
		}
		return result.toString();
	}
		
	/**
	 * 获取请求流，返回字符串
	 * @param request
	 * @return
	 */
	public static String getRequestStream(HttpServletRequest request) {
        int contentLength = request.getContentLength();
        if(contentLength < 0) return null;
        try {
        	byte buffer[] = new byte[contentLength];
            for (int i = 0; i < contentLength;) {
                int readlen = request.getInputStream().read(buffer, i, contentLength - i);
                if (readlen == -1) break;
                i += readlen;
            }
            String charEncoding = request.getCharacterEncoding();
            if (charEncoding == null) charEncoding = "UTF-8";
            return new String(buffer, charEncoding);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
	
}
