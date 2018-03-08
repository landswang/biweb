package com.hongkuncheng.vcoin.helper;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;

/**
 * 页面帮助类
 * @author 洪坤成
 *
 */
public class PageHelper {
	
	/**
	 * 打印页面内容
	 * @param sbHtml
	 */
	private static void printPage(StringBuilder sbHtml){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
    	PrintWriter print = null;
    	try {
    		print = response.getWriter();
    		print.append(sbHtml);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
	    	print.flush();
	    	print.close();
		}
	}
	
	/**
	 * 页面弹框并转向
	 * @param msg
	 * @param replaceUrl
	 * @throws Exception 
	 */
	public static void alertAndRedirect(String msg, String url) {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		request.getSession().setAttribute("alertmessage", msg);
		try {
			response.sendRedirect(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 页面弹框并返回到上一页
	 * @param $msg
	 */
	public static void alertAndGoBack(String msg){
		StringBuilder sbHtml = new StringBuilder();
		sbHtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
        sbHtml.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        sbHtml.append("<script type=\"text/javascript\">\n");
        sbHtml.append("window.history.go(-1);\n");
        sbHtml.append("alert(\"" + msg + "\");\n");
        sbHtml.append("</script>\n");
        sbHtml.append("</html>"); 
    	printPage(sbHtml);
	}
	
	/**
	 * 页面弹框并关闭页面
	 * @param msg
	 */
	public static void alertAndClose(String msg){
		StringBuilder sbHtml = new StringBuilder();
		sbHtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
        sbHtml.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        sbHtml.append("<script type=\"text/javascript\">\n");
        sbHtml.append("alert(\"" + msg + "\");\n");
        sbHtml.append("window.close();\n");
        sbHtml.append("</script>\n");
        sbHtml.append("</html>");
    	printPage(sbHtml);
	}
	
	/**
	 * 页面弹框并提示选择
	 * @param msg
	 * @param url
	 */
	public static void confirmAndRedirect(String msg, String url1, String url2) {
		StringBuilder sbHtml = new StringBuilder();
		sbHtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
        sbHtml.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        sbHtml.append("<script type=\"text/javascript\">\n");
        sbHtml.append("if(confirm(\"" + msg + "\")){window.location.replace(\"" + url1 + "\");}else{window.location.replace(\"" + url2 + "\");}\n");
        sbHtml.append("</script>\n");
        sbHtml.append("</html>");
    	printPage(sbHtml);
	}
	
	/**
	 * 页面转向最顶部窗口
	 * @param $msg
	 * @param $replaceUrl
	 */
	public static void redirectToTop(String url){
		StringBuilder sbHtml = new StringBuilder();
        sbHtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
        sbHtml.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        sbHtml.append("<script type=\"text/javascript\">\n");
        sbHtml.append("if(self.location != top.location){top.location.href=\"" + url + "\";}else{self.location.href=\"" + url + "\"};\n");
        sbHtml.append("</script>\n");
        sbHtml.append("</html>");
    	printPage(sbHtml);
	}	
	
}
