package com.hongkuncheng.vcoin.action.member;

import java.util.List;
import java.util.Date;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.PageHelper;
import com.hongkuncheng.vcoin.helper.StringHelper;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.Repairorder;
import com.hongkuncheng.vcoin.model.RepairorderReply;
import com.hongkuncheng.vcoin.model.RepairorderState;

/**
 *  工单解答
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MbrepairorderAction extends BaseAction {
	
	// 工单解答列表
	public String index() {
		Member member = (Member) session.getAttribute("member");
		List<Repairorder> repairorders = factory.getCurrentSession().createCriteria(Repairorder.class).add(Restrictions.eq("member.id", member.getId())).addOrder(Order.desc("refreshtime")).addOrder(Order.desc("id")).setCacheable(true).list();
		request.setAttribute("repairorders", repairorders);
		frontpageCommon();
		return succeed();
	}
	
	//回复列表
	public String reply() {
		int repairorderId = request.getParameter("repairorderid") == null ? 0 : Integer.parseInt(request.getParameter("repairorderid"));
		if (repairorderId > 0) {
			Repairorder repairorder = (Repairorder) factory.getCurrentSession().get(Repairorder.class, repairorderId);
			request.setAttribute("repairorder", repairorder);
			List<RepairorderReply> repairorderreplys = factory.getCurrentSession().createCriteria(RepairorderReply.class).add(Restrictions.eq("repairorder.id", repairorderId)).setCacheable(true).list();
			request.setAttribute("repairorderreplys",repairorderreplys);
		}
		frontpageCommon();
		return succeed();
	}
	
	//编辑工单解答处理
	public void replydo() throws Exception {
		Member member = (Member) session.getAttribute("member");
		String contents = StringHelper.escapeXss(request.getParameter("contents"));
		if (contents.isEmpty()) {
			PageHelper.alertAndGoBack("内容无法为空");
			return;
		}
		int repairorderId = Integer.parseInt(request.getParameter("repairorderid"));
		Repairorder repairorder = repairorderId == 0 ? new Repairorder() : (Repairorder) factory.getCurrentSession().load(Repairorder.class, repairorderId);
		RepairorderState repairorderState = (RepairorderState) factory.getCurrentSession().load(RepairorderState.class, 1);
		if (repairorderId == 0) {
			String title = StringHelper.escapeHtml(request.getParameter("title"));
			repairorder = new Repairorder(member, repairorderState, title, new Date());
			factory.getCurrentSession().save(repairorder);
		} else {
			repairorder = (Repairorder) factory.getCurrentSession().load(Repairorder.class, repairorderId);
			if (repairorder.getMember().getId() != member.getId()) {
				print("error");
				return;
			}
			repairorder.setRepairorderState(repairorderState);
			repairorder.setRefreshtime(new Date());
		}
		RepairorderReply reply = new RepairorderReply(repairorder, contents, new Date());
		factory.getCurrentSession().save(reply);
		response.sendRedirect("reply?repairorderid=" + repairorder.getId());
	}
	
	public void complate() throws Exception {
		Member member = (Member) session.getAttribute("member");
		int repairorderId = Integer.parseInt(request.getParameter("repairorderid"));
		Repairorder repairorder = repairorderId == 0 ? new Repairorder() : (Repairorder) factory.getCurrentSession().load(Repairorder.class, repairorderId);
		if (repairorderId > 0) {
			if (repairorder.getMember().getId() != member.getId()) {
				print("error");
				return;
			}
		}
		RepairorderState repairorderState = (RepairorderState) factory.getCurrentSession().load(RepairorderState.class, 4);
		repairorder.setRepairorderState(repairorderState);
		response.sendRedirect("./");
	}
	
}
