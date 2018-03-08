package com.hongkuncheng.vcoin.action.member;

import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.DataHelper;
import com.hongkuncheng.vcoin.helper.DateHelper;
import com.hongkuncheng.vcoin.helper.MathHelper;
import com.hongkuncheng.vcoin.helper.PageHelper;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.MemberVirtualcoin;
import com.hongkuncheng.vcoin.model.Orderform;

/**
 * 挂单管理
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MborderformAction extends BaseAction {

	//挂单管理
	public String index() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return null;
		}
		Criteria cirteriaOrderform = factory.getCurrentSession().createCriteria(Orderform.class).add(Restrictions.eq("member.id", member.getId()));
		StringBuilder callbackparams = new StringBuilder();
		int virtualcoinId = request.getParameter("virtualcoinid") == null ? 0 : Integer.parseInt(request.getParameter("virtualcoinid"));
		if (virtualcoinId > 0) {
			cirteriaOrderform.add(Restrictions.eq("virtualcoin.id", virtualcoinId));
			callbackparams.append("&virtualcoinid=" + virtualcoinId);
		}
		request.setAttribute("virtualcoinid", virtualcoinId);
		String dt1 = request.getParameter("dt1") == null ? "" : request.getParameter("dt1");
		if (!dt1.isEmpty()) {
			Date date1 = DateHelper.convertToDatetime(dt1);
			cirteriaOrderform.add(Restrictions.ge("placetime", date1));
			request.setAttribute("dt1", date1);
			callbackparams.append("&dt1=" + dt1);
		}
		String dt2 = request.getParameter("dt2") == null ? "" : request.getParameter("dt2");
		if (!dt2.isEmpty()) {
			Date date2 = DateHelper.convertToDatetime(dt2);
			cirteriaOrderform.add(Restrictions.le("placetime", date2));
			request.setAttribute("dt2", date2);
			callbackparams.append("&dt2=" + dt2);
		}
		if (callbackparams.length() > 0) callbackparams.deleteCharAt(0);
		request.setAttribute("callbackparams", callbackparams);
		int pageIndex = request.getParameter("pageindex") == null || request.getParameter("callbackparams") != null && !callbackparams.toString().equals(request.getParameter("callbackparams")) ? 1 : Integer.parseInt(request.getParameter("pageindex"));
		int datasize = 15;
		List<Orderform> orderforms = cirteriaOrderform.addOrder(Order.desc("placetime")).setFirstResult((pageIndex - 1) * datasize).setMaxResults(datasize).setCacheable(true).list();
		request.setAttribute("orderforms", orderforms);
		request.setAttribute("pageindex", pageIndex);
		int recordcount = orderforms.size() == 0 ? 0 : Integer.parseInt(cirteriaOrderform.setProjection(Projections.rowCount()).setFirstResult(0).setCacheable(true).uniqueResult().toString());
		request.setAttribute("recordcount", recordcount);
		int pagecount = DataHelper.calcPageCount(recordcount, datasize);
		request.setAttribute("pagecount", pagecount);
		frontpageCommon();
		return succeed();
	}
	
	//撤销挂单
	public void revocation() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return;
		}
		int orderformId = Integer.parseInt(request.getParameter("orderformid"));
		Object orderformMemberObjId = factory.getCurrentSession().createCriteria(Orderform.class).add(Restrictions.eq("id", orderformId)).setProjection(Projections.property("member.id")).setCacheable(true).uniqueResult();
		if (orderformMemberObjId == null || Integer.parseInt(orderformMemberObjId.toString()) != member.getId()) {
			PageHelper.alertAndRedirect("您的该笔挂单可能已被成交或撤销", "./");
			return;
		}
		Orderform orderform = (Orderform) factory.getCurrentSession().get(Orderform.class, orderformId, LockMode.PESSIMISTIC_WRITE);
		if (orderform == null) {
			PageHelper.alertAndRedirect("您的该笔挂单可能已被成交或撤销", "./");
			return;
		}
		member = (Member) factory.getCurrentSession().get(Member.class, member.getId(), LockMode.PESSIMISTIC_WRITE);
		if (orderform.isBuy()) {
			double money = MathHelper.mul(orderform.getPrice(), orderform.getCount(), 2);
			member.setBalanceactive(MathHelper.add(member.getBalanceactive(), member.getBalancedisable() < money ? member.getBalancedisable() : money, 2));
			member.setBalancedisable(member.getBalancedisable() < money ? 0 : MathHelper.sub(member.getBalancedisable(), money, 2));
		} else {
			Object mvObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("virtualcoin.id", orderform.getVirtualcoin().getId())).setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true).uniqueResult();
			MemberVirtualcoin membervirtualcoin = (MemberVirtualcoin) factory.getCurrentSession().load(MemberVirtualcoin.class, Integer.parseInt(mvObjId.toString()), LockMode.PESSIMISTIC_WRITE);
			membervirtualcoin.setCountactive(MathHelper.add(membervirtualcoin.getCountactive(), membervirtualcoin.getCountdisable() < orderform.getCount() ? membervirtualcoin.getCountdisable() : orderform.getCount(), 5));
			membervirtualcoin.setCountdisable(membervirtualcoin.getCountdisable() < orderform.getCount() ? 0 : MathHelper.sub(membervirtualcoin.getCountdisable(), orderform.getCount(), 5));
		}
		factory.getCurrentSession().delete(orderform);
		recordMemberLog("撤销" + (orderform.isBuy() ? "买" : "卖") + "单，虚拟币：" + orderform.getVirtualcoin().getCname() + "，价格：" + orderform.getPrice()  + "，数量：" + orderform.getCount());
		PageHelper.alertAndRedirect("撤销成功", "./");
	}
	
}
