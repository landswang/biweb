package com.hongkuncheng.vcoin.action.index;

import java.util.Date;
import java.util.List;
import java.net.URLEncoder;
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
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.MemberBankcard;
import com.hongkuncheng.vcoin.model.Rmbextractrecord;
import com.hongkuncheng.vcoin.model.RmbextractrecordState;
import com.hongkuncheng.vcoin.model.Rmbextractsetup;
import com.hongkuncheng.vcoin.model.Rmbputinrecord;
import com.hongkuncheng.vcoin.model.RmbputinrecordState;
import com.hongkuncheng.vcoin.model.Rmbputinsetup;

@Controller
@Scope("prototype")
public class Itradec2cAction extends BaseAction {

	//交易页面
	public String index() {
		Member member = (Member) session.getAttribute("member");
		Rmbputinsetup rmbputinsetup = (Rmbputinsetup) factory.getCurrentSession().get(Rmbputinsetup.class, 1);
		request.setAttribute("rmbputinsetup", rmbputinsetup);
		Rmbextractsetup rmbextractsetup = (Rmbextractsetup) factory.getCurrentSession().get(Rmbextractsetup.class, 1);
		request.setAttribute("rmbextractsetup", rmbextractsetup);
		if (member != null) {
			List<Rmbputinrecord> rmbputinrecords = factory.getCurrentSession().createCriteria(Rmbputinrecord.class).add(Restrictions.eq("member.id", member.getId())).addOrder(Order.desc("putintime")).setMaxResults(20).setCacheable(true).list();
			request.setAttribute("rmbputinrecords", rmbputinrecords);
			List<Rmbextractrecord> rmbextractrecords = factory.getCurrentSession().createCriteria(Rmbextractrecord.class).add(Restrictions.eq("member.id", member.getId())).addOrder(Order.desc("extracttime")).setMaxResults(20).setCacheable(true).list();
			request.setAttribute("rmbextractrecords", rmbextractrecords);
			
		}		
		frontpageCommon();
		return succeed();
	}
	
	String[] array = {"a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "3", "4", "5", "6", "7", "8", "9"};
	
	//交易处理
	public void dispose() throws Exception {
		Member member = (Member) session.getAttribute("member");
		if (member == null) {
			response.sendRedirect("/member/login?callbackurl=" + URLEncoder.encode("/tradec2c/"));
			return;
		}
		if (member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("您还没有设置交易密码，请前往会员中心密码修改功能里面设置您的交易密码，再回来交易吧！", "/member/");
			return;
		}
		MemberBankcard mbankcard  = (MemberBankcard) factory.getCurrentSession().createCriteria(MemberBankcard.class).add(Restrictions.eq("member.id", member.getId())).setMaxResults(1).uniqueResult();
		if (mbankcard == null) {
			PageHelper.alertAndRedirect("您还没有添加银行卡，请前往会员中心银行卡号功能里面添加您的银行卡，再回来交易吧！", "/member/");
			return;
		}
		String tradepassword = request.getParameter("tradepassword");
		if (!member.getTradepassword().equals(StringHelper.md5(tradepassword))) {
			PageHelper.alertAndRedirect("交易密码错误，交易失败。", "./");
			return;
		}
		double count = Double.parseDouble(request.getParameter("count"));
		boolean isbuy = Integer.parseInt(request.getParameter("buy")) == 1;
		Rmbputinsetup rmbputinsetup = (Rmbputinsetup) factory.getCurrentSession().get(Rmbputinsetup.class, 1);
		Rmbextractsetup rmbextractsetup = (Rmbextractsetup) factory.getCurrentSession().get(Rmbextractsetup.class, 1);
		if (isbuy) {
			if (count < rmbputinsetup.getFloor()) {
				PageHelper.alertAndRedirect("买入数量不能低于" + rmbputinsetup.getFloor() + "枚", "./");
				return;
			}
			if (count > 100000) {
				PageHelper.alertAndRedirect("买入数量不能高于" + 100000 + "枚", "./");
				return;
			}
			int waitcheckcount = Integer.parseInt(factory.getCurrentSession().createCriteria(Rmbputinrecord.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("rmbputinrecordState.id", 1)).setProjection(Projections.rowCount()).setCacheable(true).uniqueResult().toString());
			if (waitcheckcount == 3) {
				PageHelper.alertAndRedirect("您已3条未处理购买记录，您需要等待处理完之后再提交新的购买", "./");
				return;
			}
			RmbputinrecordState rmbputinrecordState = (RmbputinrecordState) factory.getCurrentSession().load(RmbputinrecordState.class, 1);
			String unicode = StringHelper.join(DataHelper.getRandom(array, 5).toArray(), "");
			Rmbputinrecord rmbputinrecord = new Rmbputinrecord(member, rmbputinrecordState, mbankcard.getBankname(), mbankcard.getNumber(), count, 0, unicode, new Date());
			factory.getCurrentSession().save(rmbputinrecord);
			recordMemberLog("CNT充值，金额：" + count);
			response.sendRedirect("putin?money=" + count + "&unicode=" + unicode);
		} else {
			if (count < rmbextractsetup.getFloor() || count > rmbextractsetup.getUpper()) {
				PageHelper.alertAndRedirect("卖出数量不能低于" + rmbextractsetup.getFloor() + "枚且不能高于" + rmbextractsetup.getUpper() + "枚", "./");
				return;
			}
			if (count > member.getBalanceactive()) {
				PageHelper.alertAndRedirect("卖出数量已超出您的余额", "./");
				return;
			}
			RmbextractrecordState rmbextractrecordState = (RmbextractrecordState) factory.getCurrentSession().load(RmbextractrecordState.class, 1);
			double moneyactual = MathHelper.mul(count, rmbextractsetup.getPrice(), 2);
			Rmbextractrecord rmbextractrecord = new Rmbextractrecord(member, rmbextractrecordState, mbankcard.getBankname(), mbankcard.getNumber(), count, moneyactual, new Date());
			factory.getCurrentSession().save(rmbextractrecord);
			member = (Member) factory.getCurrentSession().get(Member.class, member.getId(), LockMode.PESSIMISTIC_WRITE);
			member.setBalanceactive(MathHelper.sub(member.getBalanceactive(), count, 2));
			member.setBalancedisable(MathHelper.add(member.getBalancedisable(), count, 2));
			PageHelper.alertAndRedirect("交易成功", "./");
		}
		
	}
	
	//汇款订单
	public String putin() {
		double money = Double.parseDouble(request.getParameter("money"));
		request.setAttribute("money", money);
		Rmbputinsetup rmbputinsetup = (Rmbputinsetup) factory.getCurrentSession().get(Rmbputinsetup.class, 1);
		double moneyactual = MathHelper.mul(money, rmbputinsetup.getPrice(), 3);
		request.setAttribute("moneyactual", moneyactual);
		request.setAttribute("unicode", request.getParameter("unicode"));
		frontpageCommon();
		return succeed();
	}
	
	//充值撤销
	public void putinrevocation() {
		Member member = (Member) session.getAttribute("member");
		int rmbputinrecordId = Integer.parseInt(request.getParameter("rmbputinrecordid"));
		Rmbputinrecord rmbputinrecord = (Rmbputinrecord) factory.getCurrentSession().get(Rmbputinrecord.class, rmbputinrecordId, LockMode.PESSIMISTIC_WRITE);
		if (rmbputinrecord == null) {
			PageHelper.alertAndGoBack("您的当前充值记录已删除");
			return;
		}
		if (rmbputinrecord.getRmbputinrecordState().getId() > 1) {
			PageHelper.alertAndRedirect("您的当前充值记录已处理，无法再撤销。", "putinrecord?" + request.getParameter("callbackparams"));
			return;
		}
		if (rmbputinrecord.getMember().getId() != member.getId()) {
			print("error");
			return;
		}
		factory.getCurrentSession().delete(rmbputinrecord);
		recordMemberLog("撤销CNT充值，金额：" + rmbputinrecord.getMoney());
		PageHelper.alertAndRedirect("撤销成功", "./");
	}
	
	//提现撤销
	public void extractrevocation() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return;
		}
		member = (Member) factory.getCurrentSession().get(Member.class, member.getId(), LockMode.PESSIMISTIC_WRITE);
		int rmbextractrecordId = Integer.parseInt(request.getParameter("rmbextractrecordid"));
		Rmbextractrecord rmbextractrecord = (Rmbextractrecord) factory.getCurrentSession().get(Rmbextractrecord.class, rmbextractrecordId, LockMode.PESSIMISTIC_WRITE);
		if (rmbextractrecord == null) {
			PageHelper.alertAndRedirect("您的当前提现记录已删除", "extractrecord");
			return;
		}
		if (rmbextractrecord.getRmbextractrecordState().getId() > 1) {
			PageHelper.alertAndRedirect("您的当前提现记录处理中或已处理，无法再撤销。", "extractrecord");
			return;
		}
		if (rmbextractrecord.getMember().getId() != member.getId()) {
			print("error");
			return;
		}
		member.setBalanceactive(MathHelper.add(member.getBalanceactive(), member.getBalancedisable() < rmbextractrecord.getMoney() ? member.getBalancedisable() : rmbextractrecord.getMoney(), 2));
		member.setBalancedisable(member.getBalancedisable() < rmbextractrecord.getMoney() ? 0 : MathHelper.sub(member.getBalancedisable(), rmbextractrecord.getMoney(), 2));
		factory.getCurrentSession().delete(rmbextractrecord);
		recordMemberLog("撤销CNT提现，金额：" + rmbextractrecord.getMoney());
		PageHelper.alertAndRedirect("撤销成功", "./");
	}
	
}
