package com.hongkuncheng.vcoin.action.member;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.DataHelper;
import com.hongkuncheng.vcoin.helper.MathHelper;
import com.hongkuncheng.vcoin.helper.PageHelper;
import com.hongkuncheng.vcoin.helper.StringHelper;
import com.hongkuncheng.vcoin.helper.VcoinHelper;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.MemberVirtualcoin;
import com.hongkuncheng.vcoin.model.Vcoinextractrecord;
import com.hongkuncheng.vcoin.model.VcoinextractrecordState;
import com.hongkuncheng.vcoin.model.Vcoinputinrecord;
import com.hongkuncheng.vcoin.model.Virtualcoin;

/**
 * 虚拟币
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MbvcoinAction extends BaseAction {

	//转入
	public String putin() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return null;
		}
		int virtualcoinId = request.getParameter("virtualcoinid") == null ? Integer.parseInt(factory.getCurrentSession().createCriteria(Virtualcoin.class).add(Restrictions.eq("enabled", true)).setProjection(Projections.id()).setMaxResults(1).setCacheable(true).uniqueResult().toString()) : Integer.parseInt(request.getParameter("virtualcoinid"));
		Virtualcoin virtualcoin = (Virtualcoin) factory.getCurrentSession().get(Virtualcoin.class, virtualcoinId);
		MemberVirtualcoin membervirtualcoin = null;
		Object mvObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("virtualcoin.id", virtualcoinId)).setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true).uniqueResult();
		if (mvObjId == null) {
			membervirtualcoin = new MemberVirtualcoin(virtualcoin, member, 0, 0, null);
			factory.getCurrentSession().save(membervirtualcoin);
		} else {
			membervirtualcoin = (MemberVirtualcoin) factory.getCurrentSession().get(MemberVirtualcoin.class, Integer.parseInt(mvObjId.toString()), LockMode.PESSIMISTIC_WRITE);
		}
		if (membervirtualcoin.getWalletaddress() == null && virtualcoin.isCanputin()) VcoinHelper.createAddress(membervirtualcoin);
		request.setAttribute("membervirtualcoin", membervirtualcoin);
		frontpageCommon();
		return succeed();
	}
	
	//转入记录
	public String putinrecord() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return null;
		}
		Criteria criteriaVcoinputinrecord = factory.getCurrentSession().createCriteria(Vcoinputinrecord.class).add(Restrictions.eq("member.id", member.getId()));
		int pageIndex = request.getParameter("pageindex") == null ? 1 : Integer.parseInt(request.getParameter("pageindex"));
		int datasize = 15;
		List<Vcoinputinrecord> vcoinputinrecords = criteriaVcoinputinrecord.addOrder(Order.desc("putintime")).setFirstResult((pageIndex - 1) * datasize).setMaxResults(datasize).setCacheable(true).list();
		request.setAttribute("vcoinputinrecords", vcoinputinrecords);
		request.setAttribute("pageindex", pageIndex);
		int recordcount = vcoinputinrecords.size() == 0 ? 0 : Integer.parseInt(criteriaVcoinputinrecord.setProjection(Projections.rowCount()).setFirstResult(0).setCacheable(true).uniqueResult().toString());
		request.setAttribute("recordcount", recordcount);
		int pagecount = DataHelper.calcPageCount(recordcount, datasize);
		request.setAttribute("pagecount", pagecount);
		List<MemberVirtualcoin> membervirtualcoins = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.isNotNull("walletaddress")).setCacheable(true).list();
		List<Integer> membervirtualcoinIds = new ArrayList<Integer>();
		for (MemberVirtualcoin membervirtualcoin : membervirtualcoins) if (membervirtualcoin.getVirtualcoin().isEnabled() && membervirtualcoin.getWalletaddress() != null && (membervirtualcoin.getCountactive() > 0 || membervirtualcoin.getCountdisable() > 0)) membervirtualcoinIds.add(membervirtualcoin.getId());
		request.setAttribute("membervirtualcoinids", membervirtualcoinIds);
		frontpageCommon();
		return succeed();
	}	
	
	//提币
	public String extract() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return null;
		}
		if (member.getIdcardpiccheckId() < 3) {
			PageHelper.alertAndRedirect("该操作需要您通过高级实名认证才能进行哦，快去认证吧！", "../certification/advanced");
			return null;
		}
		List<MemberVirtualcoin> membervirtualcoins = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).createAlias("virtualcoin", "virtualcoin").add(Restrictions.eq("member.id", member.getId())).add(Restrictions.gt("countactive", 0.0)).addOrder(Order.desc("virtualcoin.canextract")).addOrder(Order.desc("id")).setCacheable(true).list();
		List<Virtualcoin> extractvirtualcoins = new ArrayList<Virtualcoin>();
		for (MemberVirtualcoin membervirtualcoin : membervirtualcoins) extractvirtualcoins.add(membervirtualcoin.getVirtualcoin());
		request.setAttribute("extractvirtualcoins", extractvirtualcoins);
		if (extractvirtualcoins.size() > 0) {
			int virtualcoinId = request.getParameter("virtualcoinid") == null ? 0 : Integer.parseInt(request.getParameter("virtualcoinid"));
			MemberVirtualcoin membervirtualcoin = null;
			if (virtualcoinId == 0) {
				membervirtualcoin = membervirtualcoins.get(0);
			} else {
				for (MemberVirtualcoin mvcon : membervirtualcoins){
					if (mvcon.getVirtualcoin().getId() == virtualcoinId) {
						membervirtualcoin = mvcon;
						break;
					}
				}
			}
			request.setAttribute("membervirtualcoin", membervirtualcoin);
		}
		frontpageCommon();
		return succeed();
	}
	
	//提币处理
	public void extractdo() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return;
		}
		String remark = StringHelper.escapeHtml(request.getParameter("remark"));
		if (member.getIdcardpiccheckId() < 3 || remark.length() > 30) {
			print("error");
			return;
		}
		int virtualcoinId = Integer.parseInt(request.getParameter("virtualcoinid"));
		Virtualcoin virtualcoin = (Virtualcoin) factory.getCurrentSession().get(Virtualcoin.class, virtualcoinId);
		if (!virtualcoin.isCanextract()) {
			PageHelper.alertAndRedirect("对不起，该币已暂停提币", "extract?virtualcoinid=" + virtualcoinId);
			return;
		}
		double count = Double.parseDouble(request.getParameter("count"));
		if (count < virtualcoin.getExtractfloor() || count > virtualcoin.getExtractupper()) {
			PageHelper.alertAndRedirect("提币数量低于下限或高于上限", "extract?virtualcoinid=" + virtualcoinId);
			return;
		}
		Object mvObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("virtualcoin.id", virtualcoinId)).setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true).uniqueResult();
		if (mvObjId == null) {
			PageHelper.alertAndRedirect("未找到您的" + virtualcoin.getCname(), "extract?virtualcoinid=" + virtualcoinId);
			return;
		}
		MemberVirtualcoin membervirtualcoin = (MemberVirtualcoin) factory.getCurrentSession().get(MemberVirtualcoin.class, Integer.parseInt(mvObjId.toString()), LockMode.PESSIMISTIC_WRITE);
		if (membervirtualcoin.getCountactive() < count) {
			PageHelper.alertAndRedirect("您的" + virtualcoin.getCname() + "余额不足", "extract?virtualcoinid=" + virtualcoinId);
			return;
		}
		String tradepassword = request.getParameter("tradepassword");
		if (!member.getTradepassword().equals(StringHelper.md5(tradepassword))) {
			PageHelper.alertAndRedirect("交易密码错误", "extract?virtualcoinid=" + virtualcoinId);
			return;
		}
		String mobilevcode = request.getParameter("mobilevcode");
		String mobilecheckcode = session.getAttribute("mobilecheckcode").toString();
		Long mobileexpiretime = Long.parseLong(mobilecheckcode.substring(7));
		mobilecheckcode = mobilecheckcode.substring(0, 6);
		session.removeAttribute("mobilecheckcode");
		if (!mobilevcode.equals(mobilecheckcode)) {
			PageHelper.alertAndRedirect("短信验证码错误，请重新输入。", "extract?virtualcoinid=" + virtualcoinId);
			return;
		}
		if (new Date().getTime() - mobileexpiretime > 600000) {
			PageHelper.alertAndRedirect("短信验证码已过期，请重新登录并获取短信验证码。", "login");
			return;
		}
		VcoinextractrecordState vconextractrecordState = (VcoinextractrecordState) factory.getCurrentSession().load(VcoinextractrecordState.class, 1);
		double fee = MathHelper.mul(count, virtualcoin.getExtractrate(), 5);
		if (fee < virtualcoin.getExtractfeefloor()) fee = virtualcoin.getExtractfeefloor();
		double countactual = MathHelper.places(MathHelper.sub(count, fee, 5), virtualcoin.getExtractdecimalsize());
		if (countactual < 0) {
			PageHelper.alertAndRedirect("实际提币数量低于或等于0", "extract?virtualcoinid=" + virtualcoinId);
			return;
		}
		String address = StringHelper.escapeHtml(request.getParameter("address").trim());
		Vcoinextractrecord vconextractrecord = new Vcoinextractrecord(null, virtualcoin, member, vconextractrecordState, address, count, fee, countactual, null, new Date(), remark, null);
		factory.getCurrentSession().save(vconextractrecord);
		membervirtualcoin.setCountactive(MathHelper.sub(membervirtualcoin.getCountactive(), count, 5));
		membervirtualcoin.setCountdisable(MathHelper.add(membervirtualcoin.getCountdisable(), count, 5));
		recordMemberLog(virtualcoin.getCname() + "提币，数量：" + count);
		PageHelper.alertAndRedirect("提交成功，请等待处理。", "extractrecord");
	}
	
	//提币记录
	public String extractrecord() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return null;
		}
		Criteria criteriaVcoinextractrecord = factory.getCurrentSession().createCriteria(Vcoinextractrecord.class).add(Restrictions.eq("member.id", member.getId()));
		int pageIndex = request.getParameter("pageindex") == null ? 1 : Integer.parseInt(request.getParameter("pageindex"));
		int datasize = 15;
		List<Vcoinextractrecord> vcoinextractrecords = criteriaVcoinextractrecord.addOrder(Order.desc("extracttime")).setFirstResult((pageIndex - 1) * datasize).setMaxResults(datasize).setCacheable(true).list();
		request.setAttribute("vcoinextractrecords", vcoinextractrecords);
		request.setAttribute("pageindex", pageIndex);
		int recordcount = vcoinextractrecords.size() == 0 ? 0 : Integer.parseInt(criteriaVcoinextractrecord.setProjection(Projections.rowCount()).setFirstResult(0).setCacheable(true).uniqueResult().toString());
		request.setAttribute("recordcount", recordcount);
		int pagecount = DataHelper.calcPageCount(recordcount, datasize);
		request.setAttribute("pagecount", pagecount);
		frontpageCommon();
		return succeed();
	}
	
	//提币撤销
	public void extractrevocation() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return;
		}
		int vcoinextractrecordId = Integer.parseInt(request.getParameter("vcoinextractrecordid"));
		Vcoinextractrecord vcoinextractrecord = (Vcoinextractrecord) factory.getCurrentSession().get(Vcoinextractrecord.class, vcoinextractrecordId, LockMode.PESSIMISTIC_WRITE);
		if (vcoinextractrecord == null) {
			PageHelper.alertAndGoBack("您的当前提币记录已删除");
			return;
		}
		if (vcoinextractrecord.getVcoinextractrecordState().getId() > 1) {
			PageHelper.alertAndRedirect("您的当前提币记录处理中或已处理，无法再撤销。", "extractrecord");
			return;
		}
		if (vcoinextractrecord.getMember().getId() != member.getId()) {
			print("error");
			return;
		}
		Object mvObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("virtualcoin.id", vcoinextractrecord.getVirtualcoin().getId())).setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true).uniqueResult();
		MemberVirtualcoin membervirtualcoin = (MemberVirtualcoin) factory.getCurrentSession().load(MemberVirtualcoin.class, Integer.parseInt(mvObjId.toString()), LockMode.PESSIMISTIC_WRITE);
		membervirtualcoin.setCountactive(MathHelper.add(membervirtualcoin.getCountactive(), membervirtualcoin.getCountdisable() < vcoinextractrecord.getCount() ? membervirtualcoin.getCountdisable() : vcoinextractrecord.getCount(), 5));
		membervirtualcoin.setCountdisable(membervirtualcoin.getCountdisable() < vcoinextractrecord.getCount() ? 0 : MathHelper.sub(membervirtualcoin.getCountdisable(), vcoinextractrecord.getCount(), 5));
		factory.getCurrentSession().delete(vcoinextractrecord);
		recordMemberLog("撤销" + vcoinextractrecord.getVirtualcoin().getCname() + "提币，数量：" + vcoinextractrecord.getCount());
		PageHelper.alertAndRedirect("撤销成功", "extractrecord");
	}
}
