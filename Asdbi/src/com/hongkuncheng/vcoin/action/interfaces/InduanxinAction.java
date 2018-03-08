package com.hongkuncheng.vcoin.action.interfaces;

import java.util.Date;
import java.util.regex.Pattern;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.CookieHelper;
import com.hongkuncheng.vcoin.helper.DataHelper;
import com.hongkuncheng.vcoin.helper.DuanxinHelper;
import com.hongkuncheng.vcoin.helper.StringHelper;
import com.hongkuncheng.vcoin.helper.SystemHelper;
import com.hongkuncheng.vcoin.model.Admin;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.Smssendrecord;

/**
 * 短信接口
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class InduanxinAction extends BaseAction {
	
	//发送短信（会员）
	public void send() {
		String message = "";
		Member member = (Member) session.getAttribute("member");
		if (member != null) {
			Date date = new Date();
			long curtime = date.getTime();
			boolean timeexpire = Integer.parseInt(factory.getCurrentSession().createCriteria(Smssendrecord.class).add(Restrictions.eq("mobile", member.getMobile())).add(Restrictions.gt("sendtime", new Date(curtime - 60000))).setProjection(Projections.countDistinct("id")).setCacheable(true).uniqueResult().toString()) > 0;  //是否过期
			if (!timeexpire) {
				String mobilecheckcode = String.valueOf(DataHelper.getRandom(100000, 999999));
				boolean result = DuanxinHelper.send(member.getMobile(), "您的短信验证码为：" + mobilecheckcode + "，打死都不要泄露给其他人，并请于10分钟之内使用，过期无效，谢谢您的参与！");
				if (result) {
					session.setAttribute("mobilecheckcode", mobilecheckcode + "_" + date.getTime());
					Smssendrecord smssendrecord = new Smssendrecord(member.getMobile(), date, SystemHelper.getIpAddr(request));
					factory.getCurrentSession().save(smssendrecord);
				} else {
					message = "短信接口异常，请联系管理员";
				}
			} else {
				message = "发送间隔短于60秒，请稍后再试";
			}
		} else {
			message = "用户未登录";
		}
		if (!message.isEmpty()) print(message);
	}

	//发送短信（会员登录专用）
	public void sendmblog() {
		String message = "";
		Member member = (Member) session.getAttribute("member_temp");
		if (member != null) {
			Date date = new Date();
			long curtime = date.getTime();
			boolean timeexpire = Integer.parseInt(factory.getCurrentSession().createCriteria(Smssendrecord.class).add(Restrictions.eq("mobile", member.getMobile())).add(Restrictions.gt("sendtime", new Date(curtime - 60000))).setProjection(Projections.countDistinct("id")).setCacheable(true).uniqueResult().toString()) > 0;  //是否过期
			if (!timeexpire) {
				String mobilecheckcode = String.valueOf(DataHelper.getRandom(100000, 999999));
				boolean result = DuanxinHelper.send(member.getMobile(), "您的登录短信验证码为：" + mobilecheckcode + "，打死都不要泄露给其他人，并请于10分钟之内使用，过期无效，谢谢您的参与！");
				if (result) {
					session.setAttribute("mblogmobilecheckcode", mobilecheckcode + "_" + date.getTime());
					Smssendrecord smssendrecord = new Smssendrecord(member.getMobile(), date, SystemHelper.getIpAddr(request));
					factory.getCurrentSession().save(smssendrecord);
				} else {
					message = "短信接口异常，请联系管理员";
				}
			} else {
				message = "发送间隔短于60秒，请稍后再试";
			}
			
		} else {
			message = "用户未在登录中状态";
		}
		if (!message.isEmpty()) print(message);
	}
	
	//发送短信（会员注册专用）
	public void sendreg() {
		String message = "";
		String mobile = StringHelper.escapeHtml(request.getParameter("mobile"));
		if (mobile == null || mobile.isEmpty()) {
			message = "手机号码不能为空";
			print(message);
			return;
		}
		if (!Pattern.compile("^(1)\\d{10}").matcher(mobile).matches()) {
			message = "手机号码格式错误";
			print(message);
			return;
		}
		String vcode = StringHelper.escapeHtml(request.getParameter("vcode"));
		if (vcode == null || vcode.isEmpty()) {
			message = "图形验证码不能为空";
			print(message);
			return;
		}
		String checkcode = CookieHelper.GetCookie(request, "checkcode", true);
		if (checkcode == null) {
			message = "验证码加载失败";
			print(message);
			return;
		}
		if (!vcode.equalsIgnoreCase(checkcode)) {
			message = "图形验证码错误，请重新输入。";
			print(message);
			return;
		}
		Date date = new Date();
		long curtime = date.getTime();
		boolean timeexpire = Integer.parseInt(factory.getCurrentSession().createCriteria(Smssendrecord.class).add(Restrictions.eq("mobile", mobile)).add(Restrictions.gt("sendtime", new Date(curtime - 60000))).setProjection(Projections.countDistinct("id")).setCacheable(true).uniqueResult().toString()) > 0;  //是否过期
		if (!timeexpire) {
			boolean exists = Integer.parseInt(factory.getCurrentSession().createCriteria(Member.class).add(Restrictions.eq("mobile", mobile)).setProjection(Projections.count("id")).uniqueResult().toString()) > 0;
			if (!exists) {
				String mobilecheckcode = String.valueOf(DataHelper.getRandom(100000, 999999));
				boolean result = DuanxinHelper.send(mobile, "您的注册短信验证码为：" + mobilecheckcode + "，打死都不要泄露给其他人，并请于10分钟之内使用，过期无效，谢谢您的参与！");
				if (result) {
					session.setAttribute("regmobilecheckcode", mobilecheckcode + "_" + date.getTime());
					Smssendrecord smssendrecord = new Smssendrecord(mobile, date, SystemHelper.getIpAddr(request));
					factory.getCurrentSession().save(smssendrecord);
				} else {
					message = "短信接口异常，请联系管理员";
				}
			} else {
				message = "该手机号码已被注册，请使用其它手机号码";
			}
		} else {
			message = "发送间隔短于60秒，请稍后再试";
		}
		if (!message.isEmpty()) print(message);
	}
	
	//发送短信（会员找回密码专用）
	public void sendfindpwd() {
		String message = "";
		String mobile = StringHelper.escapeHtml(request.getParameter("mobile"));
		if (mobile == null || mobile.isEmpty()) {
			message = "手机号码不能为空";
			print(message);
			return;
		}
		if (!Pattern.compile("^(1)\\d{10}").matcher(mobile).matches()) {
			message = "手机号码格式错误";
			print(message);
			return;
		}
		String vcode = StringHelper.escapeHtml(request.getParameter("vcode"));
		if (vcode == null || vcode.isEmpty()) {
			message = "图形验证码不能为空";
			print(message);
			return;
		}
		String checkcode = CookieHelper.GetCookie(request, "checkcode", true);
		if (checkcode == null) {
			message = "验证码加载失败";
			print(message);
			return;
		}
		if (!vcode.equalsIgnoreCase(checkcode)) {
			message = "图形验证码错误，请重新输入。";
			print(message);
			return;
		}
		Date date = new Date();
		long curtime = date.getTime();
		boolean timeexpire = Integer.parseInt(factory.getCurrentSession().createCriteria(Smssendrecord.class).add(Restrictions.eq("mobile", mobile)).add(Restrictions.gt("sendtime", new Date(curtime - 60000))).setProjection(Projections.countDistinct("id")).setCacheable(true).uniqueResult().toString()) > 0;  //是否过期
		if (!timeexpire) {
			boolean exists = Integer.parseInt(factory.getCurrentSession().createCriteria(Member.class).add(Restrictions.eq("mobile", mobile)).setProjection(Projections.count("id")).uniqueResult().toString()) > 0;
			if (exists) {
				String mobilecheckcode = String.valueOf(DataHelper.getRandom(100000, 999999));
				boolean result = DuanxinHelper.send(mobile, "您的找密短信验证码为：" + mobilecheckcode + "，打死都不要泄露给其他人，并请于10分钟之内使用，过期无效，谢谢您的参与！");
				if (result) {
					session.setAttribute("fpwdmobilecheckcode", mobilecheckcode + "_" + date.getTime());
					Smssendrecord smssendrecord = new Smssendrecord(mobile, date, SystemHelper.getIpAddr(request));
					factory.getCurrentSession().save(smssendrecord);
				} else {
					message = "短信接口异常，请联系管理员";
				}
			} else {
				message = "该手机号码未注册，您可以直接使用该手机号码注册账号。";
			}
		} else {
			message = "发送间隔短于60秒，请稍后再试";
		}
		if (!message.isEmpty()) print(message);
	}

	//发送短信（管理员登录专用）
	public void sendadmlog() {
		String message = "";
		Admin admin_temp = (Admin) session.getAttribute("admin_temp");
		if (admin_temp != null) {
			Date date = new Date();
			long curtime = date.getTime();
			boolean timeexpire = Integer.parseInt(factory.getCurrentSession().createCriteria(Smssendrecord.class).add(Restrictions.eq("mobile", admin_temp.getMobile())).add(Restrictions.gt("sendtime", new Date(curtime - 60000))).setProjection(Projections.countDistinct("id")).setCacheable(true).uniqueResult().toString()) > 0;  //是否过期
			if (!timeexpire) {
				String mobilecheckcode = String.valueOf(DataHelper.getRandom(100000, 999999));
				boolean result = DuanxinHelper.send(admin_temp.getMobile(), "您的后台登录短信验证码为：" + mobilecheckcode + "，打死都不要泄露给其他人，并请于10分钟之内使用，过期无效，谢谢您的参与！");
				if (result) {
					session.setAttribute("adlogmobilecheckcode", mobilecheckcode);
					Smssendrecord smssendrecord = new Smssendrecord(admin_temp.getMobile(), date, SystemHelper.getIpAddr(request));
					factory.getCurrentSession().save(smssendrecord);
				} else {
					message = "短信接口异常，请联系管理员";
				}
			} else {
				message = "发送间隔短于60秒，请稍后再试";
			}
			
		} else {
			message = "手机号码不能为空";
		}
		if (!message.isEmpty()) print(message);
	}
	
}
