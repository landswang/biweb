package com.hongkuncheng.vcoin.action.member;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.ConfigHelper;
import com.hongkuncheng.vcoin.helper.HttpHelper;
import com.hongkuncheng.vcoin.helper.PageHelper;
import com.hongkuncheng.vcoin.helper.StringHelper;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.MemberAwardsetup;
import com.hongkuncheng.vcoin.model.MemberVirtualcoin;
import com.hongkuncheng.vcoin.model.Vcoinrecord;
import com.hongkuncheng.vcoin.model.VcoinrecordType;
import com.hongkuncheng.vcoin.model.Virtualcoin;

/**
 * 实名认证
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MbcertificationAction extends BaseAction {

	//接口密钥
	private String appcode = ConfigHelper.getValue("certification.appcode");
	
	
	//普通实名认证
	public String normal() {
		frontpageCommon();
		return succeed();
	}
	
	//普通实名认证处理
	public void normaldispose() {
		String mobilevcode = request.getParameter("mobilevcode");
		String mobilecheckcode = session.getAttribute("mobilecheckcode").toString();
		Long mobileexpiretime = Long.parseLong(mobilecheckcode.substring(7));
		mobilecheckcode = mobilecheckcode.substring(0, 6);
		session.removeAttribute("mobilecheckcode");
		if (!mobilevcode.equals(mobilecheckcode)) {
			PageHelper.alertAndRedirect("短信验证码错误，请重新输入。", "normal");
			return;
		}
		if (new Date().getTime() - mobileexpiretime > 600000) {
			PageHelper.alertAndRedirect("短信验证码已过期，请重新登录并获取短信验证码。", "normal");
			return;
		}
		String name = StringHelper.escapeHtml(request.getParameter("name"));
		String idcard = StringHelper.escapeHtml(request.getParameter("idcard"));
		boolean exists = Integer.parseInt(factory.getCurrentSession().createCriteria(Member.class).add(Restrictions.eq("idcard", idcard)).setProjection(Projections.rowCount()).setCacheable(true).uniqueResult().toString()) > 0;
		if (exists) {
			PageHelper.alertAndRedirect("身份证号码已存在，请重新输入。", "normal");
			return;
		}
		Date birthday = new Date(Integer.parseInt(idcard.substring(6, 10)) - 1900, Integer.parseInt(idcard.substring(10, 12)) - 1, Integer.parseInt(idcard.substring(12, 14)));
		long cha = new Date().getTime() - birthday.getTime();
		if (cha / 60 / 60 / 24 / 365 / 1000 < 18) {
			PageHelper.alertAndRedirect("不好意思，您还属于未成年，无法通过认证。", "normal");
			return;
		}
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "APPCODE " + appcode);
		String result = HttpHelper.get("http://idcard.market.alicloudapi.com/lianzhuo/idcard?name=" + name + "&cardno=" + idcard, headers);
		JSONObject jsonObj = new JSONObject(result);
		if (Integer.parseInt(jsonObj.getJSONObject("resp").get("code").toString()) == 0) {
			Member member = (Member) session.getAttribute("member");
			member = (Member) factory.getCurrentSession().load(Member.class, member.getId(), LockMode.PESSIMISTIC_WRITE);
			member.setName(name);
			member.setIdcard(idcard);
			String msg = "实名认证成功";
			MemberAwardsetup memberAwardsetup = (MemberAwardsetup) factory.getCurrentSession().get(MemberAwardsetup.class, 1);
			if (memberAwardsetup.getCertification() > 0) {
				//奖励什币股
				Virtualcoin virtualcoin = (Virtualcoin) factory.getCurrentSession().load(Virtualcoin.class, 2); //什币股
				MemberVirtualcoin membervirtualcoin = new MemberVirtualcoin(virtualcoin, member, memberAwardsetup.getCertification(), 0);
				factory.getCurrentSession().save(membervirtualcoin);
				//币流水记录
				VcoinrecordType vcoinrecordType = (VcoinrecordType) factory.getCurrentSession().load(VcoinrecordType.class, 7);
				Vcoinrecord vcoinrecord = new Vcoinrecord(vcoinrecordType, virtualcoin, member, memberAwardsetup.getCertification(), 0, new Date());
				factory.getCurrentSession().save(vcoinrecord);
				msg += "，并赠送了您10个什币股。";
			}			
			recordMemberLog(msg);
			PageHelper.alertAndRedirect(msg, "normal");
		} else {
			PageHelper.alertAndGoBack("实名认证失败，请核对您的身份信息。");
		}
	}

	//高级实名认证
	public String advanced() {
		Member member = (Member) session.getAttribute("member");
		if (member.getIdcard() == null || member.getIdcard().isEmpty()) {
			PageHelper.alertAndRedirect("请您先通过普通实名认证吧", "normal");
		}
		frontpageCommon();
		return succeed();
	}
	
	//高级实名认证处理
	public void advanceddispose() {
		Member member = (Member) session.getAttribute("member");
		member = (Member) factory.getCurrentSession().load(Member.class, member.getId(), LockMode.PESSIMISTIC_WRITE);
		member.setIdcardpicfront(StringHelper.escapeHtml(request.getParameter("idcardpicfront")));
		member.setIdcardpicback(StringHelper.escapeHtml(request.getParameter("idcardpicback")));
		member.setIdcardpiconhand(StringHelper.escapeHtml(request.getParameter("idcardpiconhand")));
		member.setIdcardpiccheckId(1);
		PageHelper.alertAndRedirect("已提交资料，并等待平台审核。审核周期大概是1-2天之内，敬请期待。", "advanced");
	}
}
