package com.hongkuncheng.vcoin.action.member;

import java.util.Date;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.PageHelper;
import com.hongkuncheng.vcoin.helper.StringHelper;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.MemberChangemobile;
import com.hongkuncheng.vcoin.model.MemberChangemobileState;

/**
 * 个人资料
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MbaccountAction extends BaseAction {

	//个人资料
	public String index() {
		frontpageCommon();
		return succeed();
	}
	
	//修改个人资料
	public void update() {
		Member member = (Member) session.getAttribute("member");
		member = (Member) factory.getCurrentSession().load(Member.class, member.getId(), LockMode.PESSIMISTIC_WRITE);
		member.setFace(StringHelper.escapeHtml(request.getParameter("face")));
		recordMemberLog("修改个人资料");
		PageHelper.alertAndRedirect("保存成功", "./");
	}
		
	//修改手机号码
	public String changemobile() throws Exception {
		Member member = (Member) session.getAttribute("member");
		List<MemberChangemobile> memberchangemobiles = factory.getCurrentSession().createCriteria(MemberChangemobile.class).add(Restrictions.eq("member.id", member.getId())).addOrder(Order.desc("id")).setCacheable(true).list();
		if (memberchangemobiles.size() > 0) {
			request.setAttribute("memberchangemobiles", memberchangemobiles);
			frontpageCommon();
			return succeed();
		} else {
			response.sendRedirect("changemobileadd");
			return null;
		}
	}
	
	//修改手机号码提交
	public String changemobileadd() {
		Member member = (Member) session.getAttribute("member");
		boolean added = Integer.parseInt(factory.getCurrentSession().createCriteria(MemberChangemobile.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("memberChangemobileState.id", 1)).setProjection(Projections.rowCount()).setCacheable(true).uniqueResult().toString()) > 0;
		if (added) {
			PageHelper.alertAndGoBack("您目前已有待审核的申请，无法重复申请。");
			return null;
		}
		frontpageCommon();
		return succeed();
	}
	
	//修改手机号码提交处理
	public void changemobileadddo() {
		String mobile = StringHelper.escapeHtml(request.getParameter("mobile"));
		if (mobile.isEmpty()) {
			PageHelper.alertAndGoBack("新手机号码不为空");
			return;
		}
		Member member = (Member) session.getAttribute("member");
		int count = Integer.parseInt(factory.getCurrentSession().createCriteria(Member.class).add(Restrictions.eq("mobile", mobile)).setProjection(Projections.rowCount()).setCacheable(true).uniqueResult().toString());
		if (count > 0) {
			PageHelper.alertAndGoBack("手机号码已存在，请重新输入");
			return;
		}
		MemberChangemobileState mcmstate = (MemberChangemobileState) factory.getCurrentSession().load(MemberChangemobileState.class, 1);
		MemberChangemobile memberchangemobile = new MemberChangemobile(member, mcmstate, mobile, request.getParameter("chagemobilepic"), new Date());
		factory.getCurrentSession().save(memberchangemobile);
		PageHelper.alertAndRedirect("提交成功，请耐心等待客服人员审核。", "changemobile");
	}
	
	
}
