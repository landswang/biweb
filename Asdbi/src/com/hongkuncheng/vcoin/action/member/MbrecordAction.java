package com.hongkuncheng.vcoin.action.member;

import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.DataHelper;
import com.hongkuncheng.vcoin.helper.DateHelper;
import com.hongkuncheng.vcoin.helper.PageHelper;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.Rmbrecord;
import com.hongkuncheng.vcoin.model.RmbrecordType;
import com.hongkuncheng.vcoin.model.Traderecord;
import com.hongkuncheng.vcoin.model.Vcoinrecord;
import com.hongkuncheng.vcoin.model.VcoinrecordType;

/**
 * 记录查询
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MbrecordAction extends BaseAction {

	//交易记录
	public String trade() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return null;
		}
		Criteria cirteriaTraderecord = factory.getCurrentSession().createCriteria(Traderecord.class).add(Restrictions.or(Restrictions.eq("memberBySellmemberId.id", member.getId()), Restrictions.eq("memberByBuymemberId.id", member.getId())));
		StringBuilder callbackparams = new StringBuilder();
		int virtualcoinId = request.getParameter("virtualcoinid") == null ? 0 : Integer.parseInt(request.getParameter("virtualcoinid"));
		if (virtualcoinId > 0) {
			cirteriaTraderecord.add(Restrictions.eq("virtualcoin.id", virtualcoinId));
			callbackparams.append("&virtualcoinid=" + virtualcoinId);
		}
		request.setAttribute("virtualcoinid", virtualcoinId);
		String dt1 = request.getParameter("dt1") == null ? "" : request.getParameter("dt1");
		if (!dt1.isEmpty()) {
			Date date1 = DateHelper.convertToDatetime(dt1);
			cirteriaTraderecord.add(Restrictions.ge("tradetime", date1));
			request.setAttribute("dt1", date1);
			callbackparams.append("&dt1=" + dt1);
		}
		String dt2 = request.getParameter("dt2") == null ? "" : request.getParameter("dt2");
		if (!dt2.isEmpty()) {
			Date date2 = DateHelper.convertToDatetime(dt2);
			cirteriaTraderecord.add(Restrictions.le("tradetime", date2));
			request.setAttribute("dt2", date2);
			callbackparams.append("&dt2=" + dt2);
		}
		if (callbackparams.length() > 0) callbackparams.deleteCharAt(0);
		request.setAttribute("callbackparams", callbackparams);
		int pageIndex = request.getParameter("pageindex") == null || request.getParameter("callbackparams") != null && !callbackparams.toString().equals(request.getParameter("callbackparams")) ? 1 : Integer.parseInt(request.getParameter("pageindex"));
		int datasize = 15;
		List<Traderecord> traderecords = cirteriaTraderecord.addOrder(Order.desc("tradetime")).addOrder(Order.desc("id")).setFirstResult((pageIndex - 1) * datasize).setMaxResults(datasize).setCacheable(true).list();
		request.setAttribute("traderecords", traderecords);
		request.setAttribute("pageindex", pageIndex);
		int recordcount = traderecords.size() == 0 ? 0 : Integer.parseInt(cirteriaTraderecord.setProjection(Projections.rowCount()).setFirstResult(0).setCacheable(true).uniqueResult().toString());
		request.setAttribute("recordcount", recordcount);
		int pagecount = DataHelper.calcPageCount(recordcount, datasize);
		request.setAttribute("pagecount", pagecount);
		frontpageCommon();
		return succeed();
	}

	//账流水记录
	public String rmb() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return null;
		}
		Criteria cirteriaRmbrecord = factory.getCurrentSession().createCriteria(Rmbrecord.class).add(Restrictions.eq("member.id", member.getId()));
		StringBuilder callbackparams = new StringBuilder();
		int rmbrecordtypeId = request.getParameter("rmbrecordtypeid") == null ? 0 : Integer.parseInt(request.getParameter("rmbrecordtypeid"));
		if (rmbrecordtypeId > 0) {
			cirteriaRmbrecord.add(Restrictions.eq("rmbrecordType.id", rmbrecordtypeId));
			callbackparams.append("&rmbrecordtypeid=" + rmbrecordtypeId);
		}
		request.setAttribute("rmbrecordtypeid", rmbrecordtypeId);
		String dt1 = request.getParameter("dt1") == null ? "" : request.getParameter("dt1");
		if (!dt1.isEmpty()) {
			Date date1 = DateHelper.convertToDatetime(dt1);
			cirteriaRmbrecord.add(Restrictions.ge("placetime", date1));
			request.setAttribute("dt1", date1);
			callbackparams.append("&dt1=" + dt1);
		}
		String dt2 = request.getParameter("dt2") == null ? "" : request.getParameter("dt2");
		if (!dt2.isEmpty()) {
			Date date2 = DateHelper.convertToDatetime(dt2);
			cirteriaRmbrecord.add(Restrictions.le("placetime", date2));
			request.setAttribute("dt2", date2);
			callbackparams.append("&dt2=" + dt2);
		}
		if (callbackparams.length() > 0) callbackparams.deleteCharAt(0);
		request.setAttribute("callbackparams", callbackparams);
		int pageIndex = request.getParameter("pageindex") == null || request.getParameter("callbackparams") != null && !callbackparams.toString().equals(request.getParameter("callbackparams")) ? 1 : Integer.parseInt(request.getParameter("pageindex"));
		int datasize = 15;
		List<Rmbrecord> rmbrecords = cirteriaRmbrecord.addOrder(Order.desc("recordtime")).addOrder(Order.desc("id")).setFirstResult((pageIndex - 1) * datasize).setMaxResults(datasize).setCacheable(true).list();
		request.setAttribute("rmbrecords", rmbrecords);
		request.setAttribute("pageindex", pageIndex);
		int recordcount = rmbrecords.size() == 0 ? 0 : Integer.parseInt(cirteriaRmbrecord.setProjection(Projections.rowCount()).setFirstResult(0).setCacheable(true).uniqueResult().toString());
		request.setAttribute("recordcount", recordcount);
		int pagecount = DataHelper.calcPageCount(recordcount, datasize);
		request.setAttribute("pagecount", pagecount);
		List<RmbrecordType> rmbrecordTypes = factory.getCurrentSession().createCriteria(RmbrecordType.class).setCacheable(true).list();
		request.setAttribute("rmbrecordTypes", rmbrecordTypes);
		frontpageCommon();
		return succeed();
	}

	//币流水记录
	public String vcoin() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return null;
		}
		Criteria cirteriaVcoinrecord = factory.getCurrentSession().createCriteria(Vcoinrecord.class).add(Restrictions.eq("member.id", member.getId()));
		StringBuilder callbackparams = new StringBuilder();
		int vcoinrecordtypeId = request.getParameter("vcoinrecordtypeid") == null ? 0 : Integer.parseInt(request.getParameter("vcoinrecordtypeid"));
		if (vcoinrecordtypeId > 0) {
			cirteriaVcoinrecord.add(Restrictions.eq("vcoinrecordType.id", vcoinrecordtypeId));
			callbackparams.append("&vcoinrecordtypeid=" + vcoinrecordtypeId);
		}
		request.setAttribute("vcoinrecordtypeid", vcoinrecordtypeId);
		int virtualcoinId = request.getParameter("virtualcoinid") == null ? 0 : Integer.parseInt(request.getParameter("virtualcoinid"));
		if (virtualcoinId > 0) {
			cirteriaVcoinrecord.add(Restrictions.eq("virtualcoin.id", virtualcoinId));
			callbackparams.append("&virtualcoinid=" + virtualcoinId);
		}
		request.setAttribute("virtualcoinid", virtualcoinId);
		String dt1 = request.getParameter("dt1") == null ? "" : request.getParameter("dt1");
		if (!dt1.isEmpty()) {
			Date date1 = DateHelper.convertToDatetime(dt1);
			cirteriaVcoinrecord.add(Restrictions.ge("placetime", date1));
			request.setAttribute("dt1", date1);
			callbackparams.append("&dt1=" + dt1);
		}
		String dt2 = request.getParameter("dt2") == null ? "" : request.getParameter("dt2");
		if (!dt2.isEmpty()) {
			Date date2 = DateHelper.convertToDatetime(dt2);
			cirteriaVcoinrecord.add(Restrictions.le("placetime", date2));
			request.setAttribute("dt2", date2);
			callbackparams.append("&dt2=" + dt2);
		}
		if (callbackparams.length() > 0) callbackparams.deleteCharAt(0);
		request.setAttribute("callbackparams", callbackparams);
		int pageIndex = request.getParameter("pageindex") == null || request.getParameter("callbackparams") != null && !callbackparams.toString().equals(request.getParameter("callbackparams")) ? 1 : Integer.parseInt(request.getParameter("pageindex"));
		int datasize = 15;
		List<Vcoinrecord> vcoinrecords = cirteriaVcoinrecord.addOrder(Order.desc("recordtime")).addOrder(Order.desc("id")).setFirstResult((pageIndex - 1) * datasize).setMaxResults(datasize).setCacheable(true).list();
		request.setAttribute("vcoinrecords", vcoinrecords);
		request.setAttribute("pageindex", pageIndex);
		int recordcount = vcoinrecords.size() == 0 ? 0 : Integer.parseInt(cirteriaVcoinrecord.setProjection(Projections.rowCount()).setFirstResult(0).setCacheable(true).uniqueResult().toString());
		request.setAttribute("recordcount", recordcount);
		int pagecount = DataHelper.calcPageCount(recordcount, datasize);
		request.setAttribute("pagecount", pagecount);
		List<VcoinrecordType> vcoinrecordTypes = factory.getCurrentSession().createCriteria(VcoinrecordType.class).setCacheable(true).list();
		request.setAttribute("vcoinrecordTypes", vcoinrecordTypes);
		frontpageCommon();
		return succeed();
	}
	
}
