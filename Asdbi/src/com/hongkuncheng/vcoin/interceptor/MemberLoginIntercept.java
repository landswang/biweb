package com.hongkuncheng.vcoin.interceptor;

import java.net.URLEncoder;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import com.hongkuncheng.vcoin.helper.PageHelper;
import com.hongkuncheng.vcoin.model.Member;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

@Component
public class MemberLoginIntercept extends MethodFilterInterceptor {

	private static SessionFactory factory;
	
	@Resource
	public void setFactory(SessionFactory factory) {
		this.factory = factory;
	}
	
	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		String namespace = ServletActionContext.getActionMapping().getNamespace();
		Member member = (Member) session.getAttribute("member");
		if (member == null){
			String redirectUrl = "/member/login";
			if (!namespace.equals("/member")) {
				String callbackUrl = request.getRequestURI();
				if (request.getQueryString() != null) callbackUrl += "?" + request.getQueryString();
				redirectUrl += "?callbackurl=" + URLEncoder.encode(callbackUrl);
			}
			response.sendRedirect(redirectUrl);
			return null;
		} else {
			ProjectionList plMember = Projections.projectionList();
			plMember.add(Projections.property("mobile"));
			plMember.add(Projections.property("password"));
			plMember.add(Projections.property("tradepassword"));
			plMember.add(Projections.property("face"));
			plMember.add(Projections.property("name"));
			plMember.add(Projections.property("idcard"));
			plMember.add(Projections.property("idcardpicfront"));
			plMember.add(Projections.property("idcardpicback"));
			plMember.add(Projections.property("idcardpiconhand"));
			plMember.add(Projections.property("idcardpiccheckId"));
			plMember.add(Projections.property("idcardpiccheckdtcause"));
			plMember.add(Projections.property("balanceactive"));
			plMember.add(Projections.property("balancedisable"));
			plMember.add(Projections.property("enabled"));
			plMember.add(Projections.property("sessionId"));
			Object[] memberObjs = (Object[]) factory.getCurrentSession().createCriteria(Member.class).add(Restrictions.eq("id", member.getId())).setProjection(plMember).setMaxResults(1).uniqueResult();
			if (!Boolean.parseBoolean(memberObjs[13].toString())) {
				session.invalidate();
				PageHelper.alertAndRedirect("您的账号已被冻结，如有疑问请联系客服。", "/member/login");
				session.invalidate();
				return null;
			} else if (!memberObjs[14].toString().equals(session.getId())){
				session.invalidate();
				response.sendRedirect("/member/login");
				return null;
			} else {
				member.setMobile(memberObjs[0].toString());
				member.setPassword(memberObjs[1].toString());
				member.setTradepassword(memberObjs[2] == null ? null : memberObjs[2].toString());
				member.setFace(memberObjs[3] == null ? null : memberObjs[3].toString());
				member.setName(memberObjs[4] == null ? null : memberObjs[4].toString());
				member.setIdcard(memberObjs[5] == null ? null : memberObjs[5].toString());
				member.setIdcardpicfront(memberObjs[6] == null ? null : memberObjs[6].toString());
				member.setIdcardpicback(memberObjs[7] == null ? null : memberObjs[7].toString());
				member.setIdcardpiconhand(memberObjs[8] == null ? null : memberObjs[8].toString());
				member.setIdcardpiccheckId(Integer.parseInt(memberObjs[9].toString()));
				member.setIdcardpiccheckdtcause(memberObjs[10] == null ? null : memberObjs[10].toString());
				member.setBalanceactive(Double.parseDouble(memberObjs[11].toString()));
				member.setBalancedisable(Double.parseDouble(memberObjs[12].toString()));
				invocation.invoke();
				return "success";
			}
		}
	}

}
