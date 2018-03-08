package com.hongkuncheng.vcoin.action.member;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.net.URLEncoder;
import org.hibernate.LockMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.CookieHelper;
import com.hongkuncheng.vcoin.helper.MathHelper;
import com.hongkuncheng.vcoin.helper.PageHelper;
import com.hongkuncheng.vcoin.helper.StringHelper;
import com.hongkuncheng.vcoin.helper.SystemHelper;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.MemberIp;
import com.hongkuncheng.vcoin.model.MemberVirtualcoin;
import com.hongkuncheng.vcoin.model.Rmbrecord;
import com.hongkuncheng.vcoin.model.Vcoinrecord;

/**
 * 会员中心
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MbindexAction extends BaseAction {

	//主页
	public String index() {
		Member member = (Member) session.getAttribute("member");
		Object moneybalanceObj = factory.getCurrentSession().createCriteria(Rmbrecord.class).add(Restrictions.eq("member.id", member.getId())).setProjection(Projections.sum("variable")).uniqueResult();
		if (moneybalanceObj != null) {
			ProjectionList plMember = Projections.projectionList();
			plMember.add(Projections.property("balanceactive"));
			plMember.add(Projections.property("balancedisable"));
			Object[] membalanceObjs = (Object[]) factory.getCurrentSession().createCriteria(Member.class).add(Restrictions.eq("id", member.getId())).setProjection(plMember).uniqueResult();
			double rmbbalance = Double.parseDouble(moneybalanceObj.toString());
			if (MathHelper.add(Double.parseDouble(membalanceObjs[0].toString()), Double.parseDouble(membalanceObjs[1].toString()), 2) > rmbbalance) {
				member = (Member) factory.getCurrentSession().load(Member.class, member.getId(), LockMode.PESSIMISTIC_WRITE);
				member.setEnabled(false);
				PageHelper.alertAndRedirect("您的账号已被冻结，如有疑问请联系客服。", "/member/login");
				return null;
			}
		}
		double totalbalance = MathHelper.add(member.getBalanceactive(), member.getBalancedisable(), 2);
		if (member.getName() != null && member.getIdcard() != null) {
			if (SystemHelper.getDevice(request).equals("")) {
				List<MemberVirtualcoin> membervirtualcoins = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).createAlias("virtualcoin", "virtualcoin").add(Restrictions.eq("member.id", member.getId())).add(Restrictions.or(Restrictions.gt("countactive", 0.0), Restrictions.gt("countdisable", 0.0))).addOrder(Order.desc("virtualcoin.enabled")).addOrder(Order.desc("id")).list();
				request.setAttribute("membervirtualcoins", membervirtualcoins);
			}
			ProjectionList plMembervirtualcoin = Projections.projectionList();
			plMembervirtualcoin.add(Projections.property("virtualcoin.id"));
			plMembervirtualcoin.add(Projections.property("virtualcoin.price"));
			plMembervirtualcoin.add(Projections.property("virtualcoin.enabled"));
			plMembervirtualcoin.add(Projections.property("countactive"));
			plMembervirtualcoin.add(Projections.property("countdisable"));
			plMembervirtualcoin.add(Projections.property("walletaddress"));
			List<Object[]> membervirtualcoin_objs = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).createAlias("virtualcoin", "virtualcoin").add(Restrictions.eq("member.id", member.getId())).setProjection(plMembervirtualcoin).list();
			List<Integer> membervirtualcoinIds = new ArrayList<Integer>();
			for (Object[] membervirtualcoin : membervirtualcoin_objs) {
				Object vcoinbalanceObj = factory.getCurrentSession().createCriteria(Vcoinrecord.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("virtualcoin.id", Integer.parseInt(membervirtualcoin[0].toString()))).setProjection(Projections.sum("variable")).uniqueResult();
				if (vcoinbalanceObj != null) {
					double balance = Double.parseDouble(vcoinbalanceObj.toString());
					if (MathHelper.add(Double.parseDouble(membervirtualcoin[3].toString()), Double.parseDouble(membervirtualcoin[4].toString()), 2) > balance) {
						member = (Member) factory.getCurrentSession().load(Member.class, member.getId(), LockMode.PESSIMISTIC_WRITE);
						member.setEnabled(false);
						PageHelper.alertAndRedirect("您的账号已被冻结，如有疑问请联系客服。", "/member/login");
						return null;
					}
				}
				if (Boolean.parseBoolean(membervirtualcoin[2].toString()) && membervirtualcoin[5] != null && (Double.parseDouble(membervirtualcoin[3].toString()) > 0 || Double.parseDouble(membervirtualcoin[4].toString()) > 0)) membervirtualcoinIds.add(Integer.parseInt(membervirtualcoin[0].toString()));
				totalbalance = MathHelper.add(totalbalance, MathHelper.mul(MathHelper.add(Double.parseDouble(membervirtualcoin[3].toString()), Double.parseDouble(membervirtualcoin[4].toString()), 5), Double.parseDouble(membervirtualcoin[1].toString()), 5), 2);
			}
			request.setAttribute("membervirtualcoinids", membervirtualcoinIds);
		}
		request.setAttribute("totalbalance", totalbalance);
		frontpageCommon();
		return succeed();
	}
	
	//登录
	public String login() {
		String username = CookieHelper.GetCookie(request, "member_username", true);
		if (username != null && !username.isEmpty()) {
			request.setAttribute("username", username);
			request.setAttribute("password", CookieHelper.GetCookie(request, "member_password_" + username, true));
		}
		if (request.getParameter("callbackurl") != null) request.setAttribute("callbackurl", request.getParameter("callbackurl"));
		frontpageCommon();
		return succeed();
	}
	
	//登录处理
	public void logindo() throws Exception {
		String callbackurl = request.getParameter("callbackurl");
		String url = "login";
		if (callbackurl != null && !callbackurl.isEmpty()) url += "?callbackurl=" + URLEncoder.encode(callbackurl);
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username == null || password == null) {
			response.sendRedirect("/errors/500.html");
		} else {
			Object mObjId = factory.getCurrentSession().createCriteria(Member.class).add(Restrictions.eq("mobile", username)).add(Restrictions.eq("password", StringHelper.md5(password))).setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true).uniqueResult();
			if (mObjId != null) {
				Member member = (Member) factory.getCurrentSession().get(Member.class, Integer.parseInt(mObjId.toString()), LockMode.PESSIMISTIC_WRITE);
				boolean ipexists = Integer.parseInt(factory.getCurrentSession().createCriteria(MemberIp.class).add(Restrictions.eq("member.id", Integer.parseInt(mObjId.toString()))).add(Restrictions.eq("address", SystemHelper.getIpAddr(request))).setProjection(Projections.rowCount()).setCacheable(true).uniqueResult().toString()) > 0;
				if (ipexists) {
					member.setSessionId(session.getId());
					session.setAttribute("member", member);
					session.setAttribute("editorpath", "member/" + member.getId());
					CookieHelper.SetCookie(response, "member_username", username, 604800, true);
					if (request.getParameter("rememberpassword") != null) CookieHelper.SetCookie(response, "member_password_" + username, password, 604800, true);
					else CookieHelper.RemoveCookie(response, "member_password_" + username);
					String redirectUrl = callbackurl == null || callbackurl.isEmpty() ? "./" : callbackurl;
					recordMemberLog("登录");
					response.sendRedirect(redirectUrl);
					CookieHelper.RemoveCookie(response, "member_needvalidate");
				} else {
					session.setAttribute("member_temp", member);
					String redirectUrl = "mobilevalidate";
					if (callbackurl != null && !callbackurl.isEmpty()) redirectUrl += "?callbackurl=" + URLEncoder.encode(callbackurl);
					response.sendRedirect(redirectUrl);
				}
			}else {
				CookieHelper.RemoveCookie(response, "member_username");
				CookieHelper.SetCookie(response, "member_needvalidate", "1", 600, false);
				PageHelper.alertAndRedirect("账号或密码错误，请重新输入。", url);
			}
		}
	}
	
	//手机验证
	public String mobilevalidate() throws Exception {
		if (session.getAttribute("member_temp") == null) {
			response.sendRedirect("login");
			return null;
		}
		if (request.getParameter("callbackurl") != null) request.setAttribute("callbackurl", request.getParameter("callbackurl"));
		frontpageCommon();
		return succeed();
	}
	
	//手机验证处理
	public void mobilevalidatedo() throws Exception {
		String mobilevcode = request.getParameter("mobilevcode");
		String mblogmobilecheckcode = session.getAttribute("mblogmobilecheckcode").toString();
		Long mblogmobileexpiretime = Long.parseLong(mblogmobilecheckcode.substring(7));
		mblogmobilecheckcode = mblogmobilecheckcode.substring(0, 6);
		session.removeAttribute("mblogmobilecheckcode");
		if (!mobilevcode.equals(mblogmobilecheckcode)) {
			PageHelper.alertAndRedirect("短信验证码错误，请重新输入。", "login");
			return;
		}
		if (new Date().getTime() - mblogmobileexpiretime > 600000) {
			PageHelper.alertAndRedirect("短信验证码已过期，请重新登录并获取短信验证码。", "login");
			return;
		}
		Member member = (Member) session.getAttribute("member_temp");
		member = (Member) factory.getCurrentSession().load(Member.class, member.getId(), LockMode.PESSIMISTIC_WRITE);
		member.setSessionId(session.getId());
		MemberIp memberip = new MemberIp(member, SystemHelper.getIpAddr(request));
		factory.getCurrentSession().save(memberip);
		session.setAttribute("member", member);
		session.setAttribute("editorpath", "member/" + member.getId());
		session.removeAttribute("member_temp");
		CookieHelper.SetCookie(response, "member_username", member.getMobile(), 604800, true);
		String callbackurl = request.getParameter("callbackurl");
		String redirectUrl = callbackurl == null || callbackurl.isEmpty() ? "./" : callbackurl;
		recordMemberLog("登录");
		response.sendRedirect(redirectUrl);
	} 
	
	//注销
	public void logout() {
		recordMemberLog("注销");
		session.invalidate();
		PageHelper.redirectToTop("login");
	}
	
	//找回密码
	public String findpwd() {
		frontpageCommon();
		return succeed();
	}
	
	//找回密码处理
	public void findpwddo() {
		String mobilevcode = request.getParameter("mobilevcode");
		String fpwdmobilecheckcode = session.getAttribute("fpwdmobilecheckcode").toString();
		Long fpwdmobileexpiretime = Long.parseLong(fpwdmobilecheckcode.substring(7));
		fpwdmobilecheckcode = fpwdmobilecheckcode.substring(0, 6);
		session.removeAttribute("fpwdmobilecheckcode");
		if (!mobilevcode.equals(fpwdmobilecheckcode)) {
			PageHelper.alertAndRedirect("短信验证码错误，请重新输入。", "login");
			return;
		}
		if (new Date().getTime() - fpwdmobileexpiretime > 600000) {
			PageHelper.alertAndRedirect("短信验证码已过期，请重新登录并获取短信验证码。", "login");
			return;
		}
		String mobile = request.getParameter("mobile");
		Object mObjId = factory.getCurrentSession().createCriteria(Member.class).add(Restrictions.eq("mobile", mobile)).setProjection(Projections.property("id")).setMaxResults(1).uniqueResult();
		if (mObjId == null) {
			PageHelper.alertAndGoBack("未找到您输入的手机号码，您可以直接使用该手机号进行新用户注册。");
			return;
		}
		Member member = (Member) factory.getCurrentSession().load(Member.class, Integer.parseInt(mObjId.toString()), LockMode.PESSIMISTIC_WRITE);
		member.setPassword(StringHelper.md5(request.getParameter("password")));
		recordMemberLog("找回密码");
		PageHelper.alertAndRedirect("密码重设成功，请赶快登录吧！", "login");
	}
	
	//注册
	public String register() {
		String invitecode = request.getParameter("invitecode");
		if (invitecode != null && StringHelper.isInteger(invitecode) && Integer.parseInt(invitecode) > 0) request.setAttribute("invitecode", request.getParameter("invitecode"));
		frontpageCommon();
		return succeed();
	}
	
	//注册处理
	public void registerdo() {
		String mobilevcode = request.getParameter("mobilevcode");
		if (session.getAttribute("regmobilecheckcode") == null) {
			PageHelper.alertAndRedirect("短信验证码已过期，请重新登录并获取短信验证码。", "register");
		}
		String regmobilecheckcode = session.getAttribute("regmobilecheckcode").toString();
		Long regmobileexpiretime = Long.parseLong(regmobilecheckcode.substring(7));
		regmobilecheckcode = regmobilecheckcode.substring(0, 6);
		session.removeAttribute("regmobilecheckcode");
		if (!mobilevcode.equals(regmobilecheckcode)) {
			PageHelper.alertAndRedirect("短信验证码错误，请重新输入。", "register");
			return;
		}
		if (new Date().getTime() - regmobileexpiretime > 600000) {
			PageHelper.alertAndRedirect("短信验证码已过期，请重新登录并获取短信验证码。", "register");
			return;
		}
		String mobile = StringHelper.escapeHtml(request.getParameter("mobile"));
		int count = Integer.parseInt(factory.getCurrentSession().createCriteria(Member.class).add(Restrictions.eq("mobile", mobile)).setProjection(Projections.rowCount()).setCacheable(true).uniqueResult().toString());
		if (count > 0) {
			PageHelper.alertAndGoBack("手机号码已存在，请重新输入");
			return;
		}
		String invitecode = request.getParameter("invitecode");
		if (!invitecode.isEmpty()) {
			if (!StringHelper.isInteger(invitecode) || Integer.parseInt(invitecode) <= 0) {
				PageHelper.alertAndGoBack("邀请码格式错误，请重新输入");
				return;
			}
			boolean exists = Integer.parseInt(factory.getCurrentSession().createCriteria(Member.class).add(Restrictions.eq("id", Integer.parseInt(invitecode))).setProjection(Projections.rowCount()).setCacheable(true).uniqueResult().toString()) > 0;
			if (!exists) {
				PageHelper.alertAndGoBack("邀请码内容错误，请重新输入");
				return;
			}
		}
		Member member = new Member();
		member.setMobile(mobile);
		member.setPassword(StringHelper.md5(request.getParameter("password")));
		member.setRegtime(new Date());
		member.setIdcardpiccheckId(0);
		member.setBalanceactive(0);
		member.setBalancedisable(0);
		member.setEnabled(true);
		member.setParentId(StringHelper.isInteger(invitecode) ? Integer.parseInt(invitecode) : 0);
		factory.getCurrentSession().save(member);
		PageHelper.alertAndRedirect("注册成功，赶快登录并进行实名认证吧！", "login");
	}
	
}
