package com.hongkuncheng.vcoin.action.index;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.OrderForSql;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.DataHelper;
import com.hongkuncheng.vcoin.helper.MathHelper;
import com.hongkuncheng.vcoin.helper.PageHelper;
import com.hongkuncheng.vcoin.helper.StringHelper;
import com.hongkuncheng.vcoin.model.VoteReMember;
import com.hongkuncheng.vcoin.model.Vote;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.MemberVirtualcoin;
import com.hongkuncheng.vcoin.model.Vcoinapply;
import com.hongkuncheng.vcoin.model.VcoinapplyState;
import com.hongkuncheng.vcoin.model.Vcoinrecord;
import com.hongkuncheng.vcoin.model.VcoinrecordType;
import com.hongkuncheng.vcoin.model.Virtualcoin;

@Controller
@Scope("prototype")
public class IvoteAction extends BaseAction {

	//提票列表
	public String index() {
		List<Vote> votes = factory.getCurrentSession().createCriteria(Vote.class).add(Restrictions.eq("puton", true)).addOrder(OrderForSql.desc("(approvecount-opposecount)")).addOrder(Order.desc("id")).setCacheable(true).list();
		request.setAttribute("votes", votes);
		frontpageCommon();
		return succeed();
	}

	private int shibiguId = 2;   //什币股ID
	
	//投票处理
	public void dispose() throws Exception {
		Member member = (Member) session.getAttribute("member");
		int voteId = Integer.parseInt(request.getParameter("voteid"));
		int type = Integer.parseInt(request.getParameter("type"));
		if (type != 1 && type != 2) {
			print("error");
			return;
		}
		if (member == null) {
			response.sendRedirect("/member/login?callbackurl=" + URLEncoder.encode("/vote/dispose?voteid=" + voteId + "&type=" + type));
			return;
		}
		int votevcoincount = 1;   //投票使用币数
		Object mvObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("virtualcoin.id", shibiguId)).setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true).uniqueResult();
		if (mvObjId == null) {
			PageHelper.alertAndRedirect("您的什币股数量不足" + votevcoincount + "个，无法投票，快去转入或购买吧！", "./");
			return;
		}
		MemberVirtualcoin membervirtualcoin = (MemberVirtualcoin) factory.getCurrentSession().get(MemberVirtualcoin.class, Integer.parseInt(mvObjId.toString()), LockMode.PESSIMISTIC_WRITE);
		if (membervirtualcoin.getCountactive() < votevcoincount) {
			PageHelper.alertAndRedirect("您的什币股数量不足" + votevcoincount + "个，无法投票，快去转入或购买吧！", "./");
			return;
		}
		Vote vote = (Vote) factory.getCurrentSession().get(Vote.class, voteId, LockMode.PESSIMISTIC_WRITE);
		if (vote == null || !vote.isPuton() || !vote.isVoting()) {
			response.sendRedirect("./");
			return;
		}
		Date date = new Date();
		int votecounttoday = Integer.parseInt(factory.getCurrentSession().createCriteria(VoteReMember.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.sqlRestriction("date_format(votetime, '%Y')=?", date.getYear() + 1900, Hibernate.INTEGER)).add(Restrictions.sqlRestriction("date_format(votetime, '%m')=?", date.getMonth() + 1, Hibernate.INTEGER)).add(Restrictions.sqlRestriction("date_format(votetime, '%d')=?", date.getDate(), Hibernate.INTEGER)).setProjection(Projections.count("id")).setCacheable(true).uniqueResult().toString());
		if (votecounttoday >= 10) {
			PageHelper.alertAndRedirect("您今日投票已满，请明日再来吧！", "./");
			return;
		}
		if (type == 1) vote.setApprovecount(vote.getApprovecount() + 1);
		else vote.setOpposecount(vote.getOpposecount() + 1);
		VoteReMember vrm = new VoteReMember(member, vote, type == 1, new Date());
		factory.getCurrentSession().save(vrm);
		//修改币数
		membervirtualcoin.setCountactive(membervirtualcoin.getCountactive() < votevcoincount ? 0 : MathHelper.sub(membervirtualcoin.getCountactive(), votevcoincount, 5));
		//币流水记录
		Virtualcoin virtualcoin = (Virtualcoin) factory.getCurrentSession().load(Virtualcoin.class, shibiguId);
		VcoinrecordType vcoinrecordType = (VcoinrecordType) factory.getCurrentSession().load(VcoinrecordType.class, 9);
		Vcoinrecord vcoinrecord = new Vcoinrecord(vcoinrecordType, virtualcoin, member, -votevcoincount, 0, new Date());
		factory.getCurrentSession().save(vcoinrecord);
		response.sendRedirect("./");
	}
	
	//投票记录
	public String record() throws Exception {
		List<Vote> votes = factory.getCurrentSession().createCriteria(Vote.class).add(Restrictions.eq("puton", true)).addOrder(OrderForSql.desc("(approvecount+opposecount)")).addOrder(Order.desc("id")).setCacheable(true).list();
		if (votes.size() == 0) {
			PageHelper.alertAndGoBack("目前暂无进行中的投票活动记录");
			return null;
		}
		Member member = (Member) session.getAttribute("member");
		if (member == null) {
			response.sendRedirect("/member/login?callbackurl=" + URLEncoder.encode("/vote/record"));
			return null;
		}
		Criteria criteriaVoteReMember = factory.getCurrentSession().createCriteria(VoteReMember.class);
		StringBuilder callbackparams = new StringBuilder();
		request.setAttribute("votes", votes);
		int voteId = request.getParameter("voteid") == null ? 0 : Integer.parseInt(request.getParameter("voteid"));
		if (voteId > 0) {
			criteriaVoteReMember.add(Restrictions.eq("vote.id", voteId));
			callbackparams.append("&voteid=" + voteId);
		} else {
			List<Integer> voteIds = new ArrayList<Integer>();
			for (Vote vote : votes) voteIds.add(vote.getId());
			criteriaVoteReMember.add(Restrictions.in("vote.id", voteIds));
		}
		request.setAttribute("voteid", voteId);
		int type = request.getParameter("type") == null ? -1 : Integer.parseInt(request.getParameter("type"));
		if (type > -1) {
			criteriaVoteReMember.add(Restrictions.eq("approve", type == 1));
			callbackparams.append("&type=" + type);
		}
		request.setAttribute("type", type);
		boolean onlyme = request.getParameter("onlyme") != null;
		if (onlyme) {
			criteriaVoteReMember.add(Restrictions.eq("member.id", member.getId()));
			callbackparams.append("&onlyme=1");
		}
		request.setAttribute("onlyme", onlyme);
		if (callbackparams.length() > 0) callbackparams.deleteCharAt(0);
		request.setAttribute("callbackparams", callbackparams);
		int pageIndex = request.getParameter("pageindex") == null || request.getParameter("callbackparams") != null && !callbackparams.toString().equals(request.getParameter("callbackparams")) ? 1 : Integer.parseInt(request.getParameter("pageindex"));
		int datasize = 20;
		List<VoteReMember> records = criteriaVoteReMember.addOrder(Order.desc("votetime")).setFirstResult((pageIndex - 1) * datasize).setMaxResults(datasize).setCacheable(true).list();
		request.setAttribute("records", records);
		request.setAttribute("pageindex", pageIndex);
		int recordcount = records.size() == 0 ? 0 : Integer.parseInt(criteriaVoteReMember.setProjection(Projections.rowCount()).setFirstResult(0).setCacheable(true).uniqueResult().toString());
		request.setAttribute("recordcount", recordcount);
		int pagecount = DataHelper.calcPageCount(recordcount, datasize);
		request.setAttribute("pagecount", pagecount);
		frontpageCommon();
		return succeed();
	}
	
	//上币请求
	public String apply() throws Exception {
		Member member = (Member) session.getAttribute("member");
		if (member == null) {
			response.sendRedirect("/member/login?callbackurl=" + URLEncoder.encode("/vote/apply"));
			return null;
		}
		List<Vcoinapply> vcoinapplys = factory.getCurrentSession().createCriteria(Vcoinapply.class).add(Restrictions.eq("member.id", member.getId())).setCacheable(true).list();
		request.setAttribute("vcoinapplys", vcoinapplys);
		frontpageCommon();
		return succeed();
	}
	
	//上币请求处理
	public void applydispose() throws Exception {
		Member member = (Member) session.getAttribute("member");
		if (member == null) {
			response.sendRedirect("/member/login?callbackurl=" + URLEncoder.encode("/vote/apply"));
			return;
		}
		int vcoinapplycount = 300;   //申请使用币数
		Object mvObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("virtualcoin.id", shibiguId)).setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true).uniqueResult();
		if (mvObjId == null) {
			PageHelper.alertAndRedirect("您的什币股数量不足" + vcoinapplycount + "个，无法申请上币，快去转入或购买吧！", "./apply");
			return;
		}
		MemberVirtualcoin membervirtualcoin = (MemberVirtualcoin) factory.getCurrentSession().get(MemberVirtualcoin.class, Integer.parseInt(mvObjId.toString()), LockMode.PESSIMISTIC_WRITE);
		if (membervirtualcoin.getCountactive() < vcoinapplycount) {
			PageHelper.alertAndRedirect("您的什币股数量不足" + vcoinapplycount + "个，无法申请上币，快去转入或购买吧！", "./apply");
			return;
		}
		String vcoinname = StringHelper.escapeHtml(request.getParameter("vcoinname"));
		String teamname = StringHelper.escapeHtml(request.getParameter("teamname"));
		String telphone = StringHelper.escapeHtml(request.getParameter("telphone"));
		if (vcoinname.isEmpty() || teamname.isEmpty() || telphone.isEmpty()) {
			print("error");
			return;
		}
		String description = request.getParameter("description");
		VcoinapplyState vcoinapplyState = (VcoinapplyState) factory.getCurrentSession().load(VcoinapplyState.class, 1);
		Vcoinapply vcoinapply = new Vcoinapply(member, vcoinapplyState, vcoinname, teamname, telphone, description, new Date(), null);
		factory.getCurrentSession().save(vcoinapply);
		//修改币数
		membervirtualcoin.setCountactive(MathHelper.sub(membervirtualcoin.getCountactive(), vcoinapplycount, 5));
		membervirtualcoin.setCountdisable(MathHelper.add(membervirtualcoin.getCountdisable(), vcoinapplycount, 5));
		PageHelper.alertAndRedirect("您已成功提交上币申请，我们会尽快审核处理", "./apply");
	}
	
}
