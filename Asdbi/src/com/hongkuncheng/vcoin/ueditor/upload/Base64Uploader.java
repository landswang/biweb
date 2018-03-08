package com.hongkuncheng.vcoin.ueditor.upload;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.hongkuncheng.vcoin.ueditor.PathFormat;
import com.hongkuncheng.vcoin.ueditor.define.AppInfo;
import com.hongkuncheng.vcoin.ueditor.define.BaseState;
import com.hongkuncheng.vcoin.ueditor.define.FileType;
import com.hongkuncheng.vcoin.ueditor.define.State;

import org.apache.commons.codec.binary.Base64;

public final class Base64Uploader {

	public static State save(String content, Map<String, Object> conf, boolean isImage, HttpServletRequest request) {
		byte[] data = decode(content);
		long maxSize =((Long) conf.get("maxSize")).longValue();
		if(!validSize(data, maxSize)) {
			return new BaseState(false, AppInfo.MAX_SIZE);
		}
		String suffix = FileType.getSuffix("JPG");
		String rootPath =(String) conf.get("rootPath");
		String savePath = PathFormat.parse((String) conf.get("savePath"),
				(String) conf.get("filename"), request);
		savePath = savePath + suffix;
		String physicalPath = rootPath + savePath;
		State storageState = StorageManager.saveBinaryFile(data, rootPath, physicalPath, isImage);
		if(storageState.isSuccess()) {
			storageState.putInfo("url", PathFormat.format(savePath));
			storageState.putInfo("type", suffix);
			storageState.putInfo("original", "");
		}
		return storageState;
	}

	private static byte[] decode(String content) {
		return Base64.decodeBase64(content);
	}

	private static boolean validSize(byte[] data, long length) {
		return data.length <= length;
	}
	
}