package com.hongkuncheng.vcoin.action.member;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.MemberVirtualcoin;

/**
 * 我的虚拟币资产
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MbvcoinassetsAction extends BaseAction {

	//虚拟币资产列表
	public String index() {
		Member member = (Member) session.getAttribute("member");
		if (member.getName() != null && member.getIdcard() != null) {
			List<MemberVirtualcoin> membervirtualcoins = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).createAlias("virtualcoin", "virtualcoin").addOrder(Order.desc("virtualcoin.enabled")).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.or(Restrictions.gt("countactive", 0.0), Restrictions.gt("countdisable", 0.0))).addOrder(Order.desc("id")).setCacheable(true).list();
			request.setAttribute("membervirtualcoins", membervirtualcoins);
		}
		frontpageCommon();
		return succeed();
	}
	
}
