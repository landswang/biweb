package com.hongkuncheng.vcoin.action;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;
import com.hongkuncheng.vcoin.freemaker.string.EscapeHtmlMethod;
import com.hongkuncheng.vcoin.freemaker.string.SubstringMethod;
import com.hongkuncheng.vcoin.freemaker.string.UrlEncodeMethod;
import com.hongkuncheng.vcoin.helper.SystemHelper;
import com.hongkuncheng.vcoin.model.Admin;
import com.hongkuncheng.vcoin.model.AdminLog;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.MemberLog;
import com.hongkuncheng.vcoin.model.Virtualcoin;
import com.hongkuncheng.vcoin.model.Webinfo;
import com.opensymphony.xwork2.ActionSupport;

@Transactional
public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

	protected SessionFactory factory;
	
	protected HttpServletRequest request;
	
	protected HttpServletResponse response;
	
	protected HttpSession session;
	
	protected ServletContext application;

	@Resource
	public void setFactory(SessionFactory factory) {
		this.factory = factory;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		this.session = request.getSession();
		this.application = request.getSession().getServletContext();
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	//系统分页大小
	public int getDataSize(){
		return getDataSize(34);
	}
	
	//系统分页大小
	public int getDataSize(int rowheight){
		int windowheight = Integer.parseInt(session.getAttribute("windowheight").toString());
		return (windowheight - 215 - 52) / rowheight;
	}
	
	//向页面打印文字
	public void print(Object msg){
		print(msg, "text/html");
	}
	
	//向页面打印文字
	public void print(Object msg, String type){
		response.setContentType(type + "; charset=UTF-8");
    	PrintWriter printer = null;
    	try {
    		printer = this.response.getWriter();
    		printer.print(msg);  
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			printer.flush();
			printer.close();
		}
	}
	
	//向页面打印文字并换行
	public void println(Object msg){
		print(msg + "\n");
	}
	
	//向页面打印文字并换行
	public void println(Object msg, String ContentType){
		print(msg + "\n", ContentType);
	}
	
	//写入管理员操作日志
	public void recordAdminLog(String operation){
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin != null) {
//			System.out.println("管理员日志：" + admin.getAccount() + " " + operation);
			AdminLog adminlog = new AdminLog(admin, operation, new Date(), SystemHelper.getIpAddr(request));
			factory.getCurrentSession().save(adminlog);
		}
	}
	
	//写入会员操作日志
	public void recordMemberLog(String operation){
		Member member = (Member) session.getAttribute("member");
		if (member != null) {
			System.out.println("会员日志：" + member.getId() + " " + operation);
			MemberLog memberlog = new MemberLog(member, operation, new Date(), SystemHelper.getIpAddr(request));
			factory.getCurrentSession().save(memberlog);
		}
	}
	
	//返回成功
	public String succeed(){
		return succeed(true);
	}
	
	//返回成功
	public String succeed(boolean compress){
		if (request.getAttribute("viewcontentpath") == null) {
			String namespace = ServletActionContext.getActionMapping().getNamespace();
			String actionName = ServletActionContext.getActionMapping().getName();
			String viewContentPath = "contents" + namespace;
			if (!viewContentPath.endsWith("/")) viewContentPath += "/";
			viewContentPath += actionName;
			if (viewContentPath.endsWith("/")) viewContentPath += "index";
			viewContentPath += ".html";
			request.setAttribute("viewcontentpath", viewContentPath);
			request.setAttribute("device", SystemHelper.getDevice(request));
		}
		Object alertmessage = session.getAttribute("alertmessage");
		if (alertmessage != null && !alertmessage.toString().isEmpty()) {
			request.setAttribute("alertmessage", alertmessage.toString());
			session.removeAttribute("alertmessage");
		}
		return compress ? "compress" : "normal";
	}
	
	////////////////////////////////////////     前台公共函数           ////////////////////////////////////////
	
	protected List<Virtualcoin> virtualcoins = null;
	
	
	//前台公共方法，返回模块名
	protected void frontpageCommon(){
		//网站信息
		Webinfo webinfo = (Webinfo) factory.getCurrentSession().get(Webinfo.class, 1);
		request.setAttribute("web", webinfo);
		//登录用户
		Member member = (Member) session.getAttribute("member");
		request.setAttribute("member", member);
		//当前URI
		request.setAttribute("requestURI", request.getRequestURI());
		//虚拟币种
		if (virtualcoins == null) virtualcoins = factory.getCurrentSession().createCriteria(Virtualcoin.class).add(Restrictions.eq("enabled", true)).addOrder(Order.desc("sort")).addOrder(Order.desc("id")).setCacheable(true).list();
		request.setAttribute("virtualcoins", virtualcoins);
		//其它...
	}
	
	////////////////////////////////////////  Freemaker自定义函数    ////////////////////////////////////////
	

	protected UrlEncodeMethod urlEncode;

	protected EscapeHtmlMethod escapeHtml;
	
	protected SubstringMethod substring;
	
	public UrlEncodeMethod getUrlEncode() {
		return urlEncode;
	}

	@Resource
	public void setUrlEncode(UrlEncodeMethod urlEncode) {
		this.urlEncode = urlEncode;
	}
	
	public EscapeHtmlMethod getEscapeHtml() {
		return escapeHtml;
	}

	@Resource
	public void setEscapeHtml(EscapeHtmlMethod escapeHtml) {
		this.escapeHtml = escapeHtml;
	}

	public SubstringMethod getSubstring() {
		return substring;
	}

	@Resource
	public void setSubstring(SubstringMethod substring) {
		this.substring = substring;
	}
	
	
}
