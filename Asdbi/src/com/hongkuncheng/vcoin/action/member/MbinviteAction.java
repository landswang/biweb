package com.hongkuncheng.vcoin.action.member;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.DataHelper;
import com.hongkuncheng.vcoin.helper.HttpHelper;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.Rmbrecord;

/**
 * 邀请好友
 * 
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MbinviteAction extends BaseAction {

    // 好友列表
    public String index() {
        Member member = (Member) session.getAttribute("member");
        Criteria criteriaMember = factory.getCurrentSession().createCriteria(Member.class)
                .add(Restrictions.eq("parentId", member.getId()));
        int pageIndex = request.getParameter("pageindex") == null ? 1
                : Integer.parseInt(request.getParameter("pageindex"));
        int datasize = 15;
        List<Member> members = criteriaMember.addOrder(Order.desc("id")).setFirstResult((pageIndex - 1) * datasize)
                .setMaxResults(datasize).setCacheable(true).list();
        request.setAttribute("members", members);
        request.setAttribute("pageindex", pageIndex);
        int recordcount = members.size() == 0 ? 0
                : Integer.parseInt(criteriaMember.setProjection(Projections.rowCount()).setFirstResult(0)
                        .setCacheable(true).uniqueResult().toString());
        request.setAttribute("recordcount", recordcount);
        int pagecount = DataHelper.calcPageCount(recordcount, datasize);
        request.setAttribute("pagecount", pagecount);
        request.setAttribute("host", HttpHelper.getHost(request));
        Object awardTotal = factory.getCurrentSession().createCriteria(Rmbrecord.class)
                .add(Restrictions.eq("rmbrecordType.id", 7)).add(Restrictions.eq("member.id", member.getId()))
                .setProjection(Projections.sum("variable")).setCacheable(true).uniqueResult();
        double awardcountCur = 0.0;
        if (awardTotal != null) {
            awardcountCur = Double.parseDouble(awardTotal.toString());
        }
        request.setAttribute("inviteTAwardNum", awardcountCur);
        frontpageCommon();
        return succeed();
    }

}
