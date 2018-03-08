package com.hongkuncheng.vcoin.action.member;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
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
import com.hongkuncheng.vcoin.model.MemberBankcard;

/**
 *  银行卡号
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MbbankcardAction extends BaseAction {
	
	//接口密钥
	private String appcode = ConfigHelper.getValue("certification.appcode");
	
	// 银行卡号列表
	public String index() {
		Member member = (Member) session.getAttribute("member");
		List<MemberBankcard> bankcards = factory.getCurrentSession().createCriteria(MemberBankcard.class).add(Restrictions.eq("member.id", member.getId())).setCacheable(true).list();
		request.setAttribute("bankcards", bankcards);
		frontpageCommon();
		return succeed();
	}
	
	//编辑 银行卡号
	public String edit() {
		MemberBankcard bankcard = new MemberBankcard();
		request.setAttribute("bankcard", bankcard);
		frontpageCommon();
		return succeed();
	}
	
	//编辑银行卡号处理
	public void editdo() {
		Member member = (Member) session.getAttribute("member");
		String bankname =  StringHelper.escapeHtml(request.getParameter("bankname"));
		String number = StringHelper.escapeHtml(request.getParameter("number"));
//		Map<String, String> headers = new HashMap<String, String>();
//		headers.put("Authorization", "APPCODE " + appcode);
//		String json = HttpHelper.get("http://lundroid.market.alicloudapi.com/lianzhuo/verifi?acct_name=" + member.getName() + "&acct_pan=" + number, headers);
//		JSONObject obj = new JSONObject(json);
//		if (obj.getJSONObject("resp").getInt("code") != 0) {
//			PageHelper.alertAndGoBack("您绑定的银行卡与您的当前用户姓名不匹配，故无法完成绑定");
//			return;
//		}
		if (bankname.isEmpty() || number.isEmpty()) {
			PageHelper.alertAndGoBack("银行名称和银行卡号不能为空");
			return;
		}
		String mobilevcode = request.getParameter("mobilevcode");
		String mobilecheckcode = session.getAttribute("mobilecheckcode").toString();
		Long mobileexpiretime = Long.parseLong(mobilecheckcode.substring(7));
		mobilecheckcode = mobilecheckcode.substring(0, 6);
		session.removeAttribute("mobilecheckcode");
		if (!mobilevcode.equals(mobilecheckcode)) {
			PageHelper.alertAndRedirect("短信验证码错误，请重新输入。", "transfer");
			return;
		}
		if (new Date().getTime() - mobileexpiretime > 600000) {
			PageHelper.alertAndRedirect("短信验证码已过期，请重新登录并获取短信验证码。", "login");
			return;
		}
		MemberBankcard bankcard = new MemberBankcard(member, bankname, number, StringHelper.escapeHtml(request.getParameter("addprovince")), StringHelper.escapeHtml(request.getParameter("addcity")), StringHelper.escapeHtml(request.getParameter("branch")));
		factory.getCurrentSession().save(bankcard);
		recordMemberLog("新增银行卡，" + bankname + bankcard.getNumber());
		PageHelper.alertAndRedirect("保存成功", "./");
	}
	
	//删除所选 银行卡号
	public void delete() {
		Member member = (Member) session.getAttribute("member");
		int count = Integer.parseInt(factory.getCurrentSession().createCriteria(MemberBankcard.class).add(Restrictions.eq("member.id", member.getId())).setProjection(Projections.count("id")).uniqueResult().toString());
		if (count < 2) {
			print("error");
			return;
		}
		int bankcardId = Integer.parseInt(request.getParameter("bankcardid"));
		MemberBankcard bankcard = (MemberBankcard) factory.getCurrentSession().get(MemberBankcard.class, bankcardId, LockMode.PESSIMISTIC_WRITE);
		if (bankcard.getMember().getId() != member.getId()) {
			print("error");
			return;
		}
		factory.getCurrentSession().delete(bankcard);
		recordMemberLog("删除银行卡《" + bankcard.getNumber() + "》");
		PageHelper.alertAndRedirect("删除成功", "./");
	}
	
	//验证银行卡，Ajax调用
	public void validatebankcard() {
		String number = StringHelper.escapeHtml(request.getParameter("number"));
		if (!number.isEmpty()) {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", "APPCODE " + appcode);
			String json = HttpHelper.get("http://cardinfo.market.alicloudapi.com/lianzhuo/querybankcard?bankno=" + number, headers);
			JSONObject obj = new JSONObject(json);
			if (obj.getJSONObject("resp").getInt("code") == 0) {
				String bankname = obj.getJSONObject("data").getString("bank_name");
				if (bankname.equals("工商银行") || bankname.equals("农业银行") || bankname.equals("建设银行")) {
					bankname = "中国" + bankname;
				} else if (bankname.equals("邮储银行")) {
					bankname = "中国邮政储蓄银行";
				}
				print(bankname);
			} else {
				print("未找到银行");
			}
		}
	}
	
}
