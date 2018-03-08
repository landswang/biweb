package com.hongkuncheng.vcoin.ueditor.upload;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.hongkuncheng.vcoin.ueditor.define.State;

public class Uploader {
	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;

	public Uploader(HttpServletRequest request, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
	}

	public final State doExec(boolean isImage, HttpServletRequest request) {
		String filedName =(String) this.conf.get("fieldName");
		State state = null;
		if("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request.getParameter(filedName), this.conf, isImage, request);
		} else {
			state = BinaryUploader.save(this.request, this.conf, isImage);
		}
		return state;
	}
}
