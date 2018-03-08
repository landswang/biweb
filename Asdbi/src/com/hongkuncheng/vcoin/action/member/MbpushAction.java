package com.hongkuncheng.vcoin.action.member;

import java.util.ArrayList;
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
import com.hongkuncheng.vcoin.helper.MathHelper;
import com.hongkuncheng.vcoin.helper.PageHelper;
import com.hongkuncheng.vcoin.helper.StringHelper;
import com.hongkuncheng.vcoin.model.Pushrecord;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.MemberVirtualcoin;
import com.hongkuncheng.vcoin.model.PushrecordState;
import com.hongkuncheng.vcoin.model.Pushsetup;
import com.hongkuncheng.vcoin.model.Rmbrecord;
import com.hongkuncheng.vcoin.model.RmbrecordType;
import com.hongkuncheng.vcoin.model.Vcoinrecord;
import com.hongkuncheng.vcoin.model.VcoinrecordType;
import com.hongkuncheng.vcoin.model.Virtualcoin;

/**
 * PUSH转账
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MbpushAction extends BaseAction {

	//PUSH转账
	public String transfer() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return null;
		}
		List<MemberVirtualcoin> membervirtualcoins = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.gt("countactive", 0.0)).setCacheable(true).list();
		List<Virtualcoin> pushvirtualcoins = new ArrayList<Virtualcoin>();
		for (MemberVirtualcoin membervirtualcoin : membervirtualcoins) pushvirtualcoins.add(membervirtualcoin.getVirtualcoin());
		request.setAttribute("pushvirtualcoins", pushvirtualcoins);
		Pushsetup pushsetup = (Pushsetup) factory.getCurrentSession().get(Pushsetup.class, 1);
		request.setAttribute("pushsetup", pushsetup);
		frontpageCommon();
		return succeed();
	}
	
	//PUSH转账处理
	public void transferdo() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return;
		}
		int receiverId = Integer.parseInt(request.getParameter("receiverid"));
		if (receiverId == member.getId()) {
			PageHelper.alertAndRedirect("对方ID不能是自己的ID", "transfer");
			return;
		}
		double count = Double.parseDouble(request.getParameter("count"));
		if (count <= 0) {
			PageHelper.alertAndRedirect("PUSH数量必须大于0", "transfer");
			return;
		}
		Pushsetup pushsetup = (Pushsetup) factory.getCurrentSession().get(Pushsetup.class, 1);
		double money = Double.parseDouble(request.getParameter("money"));
		if (money <= pushsetup.getMoneyfeefloor()) {
			PageHelper.alertAndRedirect("约定金额不能低于或等于手续下限" + pushsetup.getMoneyfeefloor(), "transfer");
			return;
		}
		String tradepassword = request.getParameter("tradepassword");
		if (!member.getTradepassword().equals(StringHelper.md5(tradepassword))) {
			PageHelper.alertAndRedirect("交易密码错误", "transfer");
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
		int virtualcoinId = Integer.parseInt(request.getParameter("virtualcoinid"));
		Virtualcoin virtualcoin = (Virtualcoin) factory.getCurrentSession().load(Virtualcoin.class, virtualcoinId);
		Member receiver = (Member) factory.getCurrentSession().load(Member.class, receiverId);
		if (receiver == null) {
			PageHelper.alertAndRedirect("未找到对方账号，请核对后重新输入。", "transfer");
			return;
		}
		Object mvObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("virtualcoin.id", virtualcoinId)).setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true).uniqueResult();
		if (mvObjId == null) {
			PageHelper.alertAndRedirect("未找到您的" + virtualcoin.getCname(), "transfer");
			return;
		}
		MemberVirtualcoin membervirtualcoin = (MemberVirtualcoin) factory.getCurrentSession().get(MemberVirtualcoin.class, Integer.parseInt(mvObjId.toString()), LockMode.PESSIMISTIC_WRITE);
		if (membervirtualcoin.getCountactive() < count) {
			PageHelper.alertAndRedirect("您的" + virtualcoin.getCname() + "余额不足", "transfer");
			return;
		}
		PushrecordState pushrecordState = (PushrecordState) factory.getCurrentSession().load(PushrecordState.class, 1);
		Pushrecord pushrecord = new Pushrecord(receiver, member, virtualcoin, pushrecordState, count, money, new Date());
		factory.getCurrentSession().save(pushrecord);
		membervirtualcoin.setCountactive(MathHelper.sub(membervirtualcoin.getCountactive(), count, 5));
		membervirtualcoin.setCountdisable(MathHelper.add(membervirtualcoin.getCountdisable(), count, 5));
		recordMemberLog("发起PUSH转账，数量：" + count + "，金额：" + money);
		PageHelper.alertAndRedirect("发起成功，请等待对方接收确认。", "sendrecord");
	}
	
	//发起的PUSH
	public String sendrecord() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return null;
		}
		Criteria criteriaPushrecord = factory.getCurrentSession().createCriteria(Pushrecord.class).add(Restrictions.eq("memberBySenderId.id", member.getId()));
		int pageIndex = request.getParameter("pageindex") == null ? 1 : Integer.parseInt(request.getParameter("pageindex"));
		int datasize = 15;
		List<Pushrecord> pushrecords = criteriaPushrecord.addOrder(Order.desc("pushtime")).setFirstResult((pageIndex - 1) * datasize).setMaxResults(datasize).setCacheable(true).list();
		request.setAttribute("pushrecords", pushrecords);
		request.setAttribute("pageindex", pageIndex);
		int recordcount = pushrecords.size() == 0 ? 0 : Integer.parseInt(criteriaPushrecord.setProjection(Projections.rowCount()).setFirstResult(0).setCacheable(true).uniqueResult().toString());
		request.setAttribute("recordcount", recordcount);
		int pagecount = DataHelper.calcPageCount(recordcount, datasize);
		request.setAttribute("pagecount", pagecount);
		frontpageCommon();
		return succeed();
	}
	
	//撤销PUSH
	public void sendrevocation() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return;
		}
		int pushrecordId = Integer.parseInt(request.getParameter("pushrecordid"));
		Pushrecord pushrecord = (Pushrecord) factory.getCurrentSession().get(Pushrecord.class, pushrecordId, LockMode.PESSIMISTIC_WRITE);
		if (pushrecord.getMemberBySenderId().getId() != member.getId()) {
			print("error");
			return;
		}
		if (pushrecord.getPushrecordState().getId() > 1) {
			PageHelper.alertAndRedirect("您的当前PUSH记录对方已处理，无法再撤销。", "sendrecord");
			return;
		}
		factory.getCurrentSession().delete(pushrecord);
		Object mvObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("virtualcoin.id", pushrecord.getVirtualcoin().getId())).setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true).uniqueResult();
		MemberVirtualcoin membervirtualcoin = (MemberVirtualcoin) factory.getCurrentSession().load(MemberVirtualcoin.class, Integer.parseInt(mvObjId.toString()), LockMode.PESSIMISTIC_WRITE);
		membervirtualcoin.setCountactive(MathHelper.add(membervirtualcoin.getCountactive(), membervirtualcoin.getCountdisable() < pushrecord.getCount() ? membervirtualcoin.getCountdisable() : pushrecord.getCount(), 5));
		membervirtualcoin.setCountdisable(membervirtualcoin.getCountdisable() < pushrecord.getCount() ? 0 : MathHelper.sub(membervirtualcoin.getCountdisable(), pushrecord.getCount(), 5));
		recordMemberLog("撤销PUSH转账，数量：" + pushrecord.getCount() + "，金额：" + pushrecord.getMoney());
		PageHelper.alertAndRedirect("撤销成功", "sendrecord");
	}
	
	//接收的PUSH
	public String receiverecord() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return null;
		}
		Criteria criteriaPushrecord = factory.getCurrentSession().createCriteria(Pushrecord.class).add(Restrictions.eq("memberByReceiverId.id", member.getId()));
		int pageIndex = request.getParameter("pageindex") == null ? 1 : Integer.parseInt(request.getParameter("pageindex"));
		int datasize = 15;
		List<Pushrecord> pushrecords = criteriaPushrecord.addOrder(Order.desc("pushtime")).setFirstResult((pageIndex - 1) * datasize).setMaxResults(datasize).setCacheable(true).list();
		request.setAttribute("pushrecords", pushrecords);
		request.setAttribute("pageindex", pageIndex);
		int recordcount = pushrecords.size() == 0 ? 0 : Integer.parseInt(criteriaPushrecord.setProjection(Projections.rowCount()).setFirstResult(0).setCacheable(true).uniqueResult().toString());
		request.setAttribute("recordcount", recordcount);
		int pagecount = DataHelper.calcPageCount(recordcount, datasize);
		request.setAttribute("pagecount", pagecount);
		frontpageCommon();
		return succeed();
	}
	
	//接受接收的PUSH
	public void receiveaccep() {
		Member member = (Member) session.getAttribute("member");
		int pushrecordId = Integer.parseInt(request.getParameter("pushrecordid"));
		Pushrecord pushrecord = (Pushrecord) factory.getCurrentSession().get(Pushrecord.class, pushrecordId, LockMode.PESSIMISTIC_WRITE);
		if (pushrecord.getMemberByReceiverId().getId() != member.getId() || pushrecord.getPushrecordState().getId() != 1) {
			print("error");
			return;
		}
		if (member.getBalanceactive() < pushrecord.getMoney()) {
			PageHelper.alertAndGoBack("您的账户余额不足");
			return;
		}
		Member sender = (Member) factory.getCurrentSession().get(Member.class, pushrecord.getMemberBySenderId().getId(), LockMode.PESSIMISTIC_WRITE);
		Member receiver = (Member) factory.getCurrentSession().get(Member.class, pushrecord.getMemberByReceiverId().getId(), LockMode.PESSIMISTIC_WRITE);
		Pushsetup pushsetup = (Pushsetup) factory.getCurrentSession().get(Pushsetup.class, 1);
		double moneyfee = MathHelper.mul(pushrecord.getMoney(), pushsetup.getMoneyrate(), 2);
		if (moneyfee < pushsetup.getMoneyfeefloor()) moneyfee = pushsetup.getMoneyfeefloor();
		sender.setBalanceactive(MathHelper.sub(MathHelper.add(sender.getBalanceactive(), pushrecord.getMoney(), 2), moneyfee, 2));
		Object mvSenderObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", sender.getId())).add(Restrictions.eq("virtualcoin.id", pushrecord.getVirtualcoin().getId())).setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true).uniqueResult();
		MemberVirtualcoin membervirtualcoinSender = (MemberVirtualcoin) factory.getCurrentSession().load(MemberVirtualcoin.class, Integer.parseInt(mvSenderObjId.toString()), LockMode.PESSIMISTIC_WRITE);
		membervirtualcoinSender.setCountdisable(MathHelper.sub(membervirtualcoinSender.getCountdisable(), pushrecord.getCount(), 5));
		receiver.setBalanceactive(MathHelper.sub(receiver.getBalanceactive(), pushrecord.getMoney(), 2));
		double virtualcoinfee = MathHelper.mul(pushrecord.getCount(), pushsetup.getVcoinrate(), 5);
		if (virtualcoinfee < pushsetup.getVcoinfeefloor()) virtualcoinfee = pushsetup.getVcoinfeefloor();
		Object mvReceiverObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", receiver.getId())).add(Restrictions.eq("virtualcoin.id", pushrecord.getVirtualcoin().getId())).setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true).uniqueResult();
		if (mvReceiverObjId != null) {
			MemberVirtualcoin membervirtualcoinReceiver = (MemberVirtualcoin) factory.getCurrentSession().load(MemberVirtualcoin.class, Integer.parseInt(mvReceiverObjId.toString()), LockMode.PESSIMISTIC_WRITE);
			membervirtualcoinReceiver.setCountactive(MathHelper.sub(MathHelper.add(membervirtualcoinReceiver.getCountactive(), pushrecord.getCount(), 5), virtualcoinfee, 5));
		} else {
			MemberVirtualcoin membervirtualcoinReceiver = new MemberVirtualcoin(pushrecord.getVirtualcoin(), receiver, MathHelper.sub(pushrecord.getCount(), virtualcoinfee, 5), 0, null);
			factory.getCurrentSession().save(membervirtualcoinReceiver);
		}
		//PUSH记录
		PushrecordState pushrecordState = (PushrecordState) factory.getCurrentSession().load(PushrecordState.class, 2);
		pushrecord.setPushrecordState(pushrecordState);
		//账流水记录
		RmbrecordType rmbrecordTypeSender = (RmbrecordType) factory.getCurrentSession().load(RmbrecordType.class, 4);
		Rmbrecord rmbrecordSender = new Rmbrecord(rmbrecordTypeSender, sender, MathHelper.sub(pushrecord.getMoney(), moneyfee, 2), moneyfee, sender.getBalanceactive(), sender.getBalancedisable(), new Date());
		factory.getCurrentSession().save(rmbrecordSender);
		RmbrecordType rmbrecordTypeReceiver = (RmbrecordType) factory.getCurrentSession().load(RmbrecordType.class, 3);
		Rmbrecord rmbrecordReceiver = new Rmbrecord(rmbrecordTypeReceiver, receiver, -pushrecord.getMoney(), 0, receiver.getBalanceactive(), receiver.getBalancedisable(), new Date());
		factory.getCurrentSession().save(rmbrecordReceiver);
		//币流水记录
		VcoinrecordType vcoinrecordTypeSender = (VcoinrecordType) factory.getCurrentSession().load(VcoinrecordType.class, 4);
		Vcoinrecord vcoinrecordSender = new Vcoinrecord(vcoinrecordTypeSender, pushrecord.getVirtualcoin(), sender, -pushrecord.getCount(), 0, new Date());
		factory.getCurrentSession().save(vcoinrecordSender);
		VcoinrecordType vcoinrecordTypeReceiver = (VcoinrecordType) factory.getCurrentSession().load(VcoinrecordType.class, 3);
		Vcoinrecord vcoinrecordReceiver = new Vcoinrecord(vcoinrecordTypeReceiver, pushrecord.getVirtualcoin(), receiver, MathHelper.sub(pushrecord.getCount(), virtualcoinfee, 5), virtualcoinfee, new Date());
		factory.getCurrentSession().save(vcoinrecordReceiver);
		recordMemberLog("接受PUSH转账，数量：" + pushrecord.getCount() + "，金额：" + pushrecord.getMoney());
		PageHelper.alertAndRedirect("操作成功", "receiverecord");
	}
	
	//拒绝接收的PUSH
	public void receivereject() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() == null || member.getTradepassword() == null) {
			PageHelper.alertAndRedirect("请先完成交易密码设置和实名认证才能使用该功能哟！", "../");
			return;
		}
		int pushrecordId = Integer.parseInt(request.getParameter("pushrecordid"));
		Pushrecord pushrecord = (Pushrecord) factory.getCurrentSession().get(Pushrecord.class, pushrecordId, LockMode.PESSIMISTIC_WRITE);
		if (pushrecord.getMemberByReceiverId().getId() != member.getId() || pushrecord.getPushrecordState().getId() != 1) {
			print("error");
			return;
		}
		Member sender = pushrecord.getMemberBySenderId();
		Object mvSenderObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", sender.getId())).add(Restrictions.eq("virtualcoin.id", pushrecord.getVirtualcoin().getId())).setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true).uniqueResult();
		MemberVirtualcoin membervirtualcoinSender = (MemberVirtualcoin) factory.getCurrentSession().load(MemberVirtualcoin.class, Integer.parseInt(mvSenderObjId.toString()), LockMode.PESSIMISTIC_WRITE);
		membervirtualcoinSender.setCountactive(MathHelper.add(membervirtualcoinSender.getCountactive(), pushrecord.getCount(), 5));
		membervirtualcoinSender.setCountdisable(MathHelper.sub(membervirtualcoinSender.getCountdisable(), pushrecord.getCount(), 5));
		PushrecordState pushrecordState = (PushrecordState) factory.getCurrentSession().load(PushrecordState.class, 3);
		pushrecord.setPushrecordState(pushrecordState);
		recordMemberLog("拒绝PUSH转账，数量：" + pushrecord.getCount() + "，金额：" + pushrecord.getMoney());
		PageHelper.alertAndRedirect("操作成功", "receiverecord");
	}
	
}
