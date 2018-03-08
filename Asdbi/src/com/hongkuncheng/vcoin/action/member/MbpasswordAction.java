package com.hongkuncheng.vcoin.action.member;

import java.util.Date;

import org.hibernate.LockMode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.CookieHelper;
import com.hongkuncheng.vcoin.helper.PageHelper;
import com.hongkuncheng.vcoin.helper.StringHelper;
import com.hongkuncheng.vcoin.model.Member;

/**
 * 修改密码
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MbpasswordAction extends BaseAction {

	//密码修改
	public String login() {
		frontpageCommon();
		return succeed();
	}
	
	//密码修改
	public String trade() {
		frontpageCommon();
		return succeed();
	}
	
	//登录密码修改处理
	public void updatelogin(){
		String mobilevcode = request.getParameter("mobilevcode");
		String mobilecheckcode = session.getAttribute("mobilecheckcode").toString();
		Long mobileexpiretime = Long.parseLong(mobilecheckcode.substring(7));
		mobilecheckcode = mobilecheckcode.substring(0, 6);
		session.removeAttribute("mobilecheckcode");
		if (!mobilevcode.equals(mobilecheckcode)) {
			PageHelper.alertAndRedirect("短信验证码错误，请重新输入。", "login");
			return;
		}
		if (new Date().getTime() - mobileexpiretime > 600000) {
			PageHelper.alertAndRedirect("短信验证码已过期，请重新登录并获取短信验证码。", "login");
			return;
		}
		Member member = (Member) session.getAttribute("member");
		String passwordNew = StringHelper.escapeHtml(request.getParameter("passwordNew"));
		member = (Member) factory.getCurrentSession().load(Member.class, member.getId(), LockMode.PESSIMISTIC_WRITE);
		member.setPassword(StringHelper.md5(passwordNew));
		recordMemberLog("修改登录密码");
		CookieHelper.RemoveCookie(response, "member_password_" + member.getMobile());
		PageHelper.alertAndRedirect("保存成功", "login");
	}
	
	//交易密码修改处理
	public void updatetrade(){
		String mobilevcode = request.getParameter("mobilevcode");
		String mobilecheckcode = session.getAttribute("mobilecheckcode").toString();
		Long mobileexpiretime = Long.parseLong(mobilecheckcode.substring(7));
		mobilecheckcode = mobilecheckcode.substring(0, 6);
		session.removeAttribute("mobilecheckcode");
		if (!mobilevcode.equals(mobilecheckcode)) {
			PageHelper.alertAndRedirect("短信验证码错误，请重新输入。", "trade");
			return;
		}
		if (new Date().getTime() - mobileexpiretime > 600000) {
			PageHelper.alertAndRedirect("短信验证码已过期，请重新登录并获取短信验证码。", "trade");
			return;
		}
		Member member = (Member) session.getAttribute("member");
		String password = StringHelper.escapeHtml(request.getParameter("password"));
		member = (Member) factory.getCurrentSession().load(Member.class, member.getId(), LockMode.PESSIMISTIC_WRITE);
		if (StringHelper.md5(password).equals(member.getPassword())) {
			PageHelper.alertAndGoBack("交易密码不能和登录密码一样，请重新输入。");
			return;
		}
		member.setTradepassword(StringHelper.md5(password));
		recordMemberLog("修改交易密码");
		session.removeAttribute("tradepassword");
		PageHelper.alertAndRedirect("保存成功", "trade");
	}
	
}
