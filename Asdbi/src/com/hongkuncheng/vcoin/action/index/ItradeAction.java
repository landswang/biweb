package com.hongkuncheng.vcoin.action.index;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.ConfigHelper;
import com.hongkuncheng.vcoin.helper.DateHelper;
import com.hongkuncheng.vcoin.helper.MathHelper;
import com.hongkuncheng.vcoin.helper.PageHelper;
import com.hongkuncheng.vcoin.helper.StringHelper;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.MemberAwardsetup;
import com.hongkuncheng.vcoin.model.MemberVirtualcoin;
import com.hongkuncheng.vcoin.model.Orderform;
import com.hongkuncheng.vcoin.model.Rmbrecord;
import com.hongkuncheng.vcoin.model.RmbrecordType;
import com.hongkuncheng.vcoin.model.Traderecord;
import com.hongkuncheng.vcoin.model.Vcoinrecord;
import com.hongkuncheng.vcoin.model.VcoinrecordType;
import com.hongkuncheng.vcoin.model.Virtualcoin;

@Controller
@Scope("prototype")
public class ItradeAction extends BaseAction {

    // private final static int CAL_PRECISION = 9;// 虚拟币交易计算精度

    private final static int RMB_PRECISION = 3;// 交易人民币账户精度
    // 交易页面

    public String index() {
        String eunit = request.getParameter("eunit");
        Virtualcoin virtualcoin = (Virtualcoin) factory.getCurrentSession().createCriteria(Virtualcoin.class)
                .add(Restrictions.eq("eunit", eunit)).setMaxResults(1).uniqueResult();
        if (!virtualcoin.isEnabled()) {
            print("error");
            return null;
        }
        request.setAttribute("virtualcoin", virtualcoin);
        // 最佳买价
        Object bestbuyprice = factory.getCurrentSession().createCriteria(Orderform.class)
                .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId())).add(Restrictions.eq("buy", false))
                .setProjection(Projections.min("price")).setCacheable(true).uniqueResult();
        request.setAttribute("bestbuyprice", bestbuyprice == null ? 0 : Double.parseDouble(bestbuyprice.toString()));
        // 最佳卖价
        Object bestsellprice = factory.getCurrentSession().createCriteria(Orderform.class)
                .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId())).add(Restrictions.eq("buy", true))
                .setProjection(Projections.max("price")).setCacheable(true).uniqueResult();
        request.setAttribute("bestsellprice", bestsellprice == null ? 0 : Double.parseDouble(bestsellprice.toString()));
        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            member = (Member) factory.getCurrentSession().get(Member.class, member.getId());
            if (!member.isEnabled()) {
                session.invalidate();
                PageHelper.alertAndRedirect("您的账号已被冻结，如有疑问请联系客服。", "/trade/" + virtualcoin.getId() + ".html");
                return null;
            } else if (!member.getSessionId().equals(session.getId())) {
                session.invalidate();
                PageHelper.alertAndRedirect("您的账号已在其它地方登录，请确保是否您本人操作，如果非本人操作或许您的密码已泄露，请及时修改您的密码。",
                        "/trade/" + virtualcoin.getId() + ".html");
                return null;
            }
            session.setAttribute("member", member);
            // 可用虚拟币数量
            Object myvirtualcoincountactive = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class)
                    .add(Restrictions.eq("member.id", member.getId()))
                    .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId()))
                    .setProjection(Projections.sum("countactive")).setCacheable(true).uniqueResult();
            request.setAttribute("myvirtualcoincountactive",
                    myvirtualcoincountactive == null ? 0 : Double.parseDouble(myvirtualcoincountactive.toString()));
            // 冻结虚拟币数量
            Object myvirtualcoincountdisable = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class)
                    .add(Restrictions.eq("member.id", member.getId()))
                    .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId()))
                    .setProjection(Projections.sum("countdisable")).setCacheable(true).uniqueResult();
            request.setAttribute("myvirtualcoincountdisable",
                    myvirtualcoincountdisable == null ? 0 : Double.parseDouble(myvirtualcoincountdisable.toString()));
            // 我的最近买单
            List<Orderform> mybuyorderforms = factory.getCurrentSession().createCriteria(Orderform.class)
                    .add(Restrictions.eq("member.id", member.getId()))
                    .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId())).add(Restrictions.eq("buy", true))
                    .addOrder(Order.desc("placetime")).setMaxResults(7).setCacheable(true).list();
            request.setAttribute("mybuyorderforms", mybuyorderforms);
            // 我的最近卖单
            List<Orderform> mysellorderforms = factory.getCurrentSession().createCriteria(Orderform.class)
                    .add(Restrictions.eq("member.id", member.getId()))
                    .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId())).add(Restrictions.eq("buy", false))
                    .addOrder(Order.desc("placetime")).setMaxResults(7).setCacheable(true).list();
            request.setAttribute("mysellorderforms", mysellorderforms);
            // 我的最近交易记录
            List<Traderecord> mylatelytraderecords = factory.getCurrentSession().createCriteria(Traderecord.class)
                    .add(Restrictions.or(Restrictions.eq("memberBySellmemberId.id", member.getId()),
                            Restrictions.eq("memberByBuymemberId.id", member.getId())))
                    .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId())).addOrder(Order.desc("tradetime"))
                    .setMaxResults(20).setCacheable(true).list();
            request.setAttribute("mylatelytraderecords", mylatelytraderecords);
        }
        Date date = new Date();
        Date tradetodaybegintime = DateHelper
                .convertToDate((date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate());
        Date yesterday = new Date(
                date.getTime() - Integer.parseInt(ConfigHelper.getValue("tradestatistical.hour")) * 60 * 60 * 1000);
        Date tradetimebefore24hourtime = DateHelper
                .convertToDatetime((yesterday.getYear() + 1900) + "-" + (yesterday.getMonth() + 1) + "-"
                        + yesterday.getDate() + " " + yesterday.getHours() + ":" + yesterday.getMinutes() + ":0");
        // 今日开盘价
        Object openingprice = factory.getCurrentSession().createCriteria(Traderecord.class)
                .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId()))
                .add(Restrictions.le("tradetime", tradetodaybegintime)).addOrder(Order.desc("id"))
                .setProjection(Projections.property("price")).setMaxResults(1).setCacheable(true).uniqueResult();
        request.setAttribute("openingprice", openingprice == null ? 0 : Double.parseDouble(openingprice.toString()));
        // 当前挂单
        List<Orderform> orderforms = new ArrayList<Orderform>();
        ProjectionList prolistOrderoform = Projections.projectionList();
        prolistOrderoform.add(Projections.property("price"));
        prolistOrderoform.add(Projections.property("count"));
        // 当前买单
        int curcount = 0;
        int precount = 0;
        double precounts = 0;
        int targetcount = 7;
        List<Double> keys = new ArrayList<Double>();
        Map<Double, Double> buyorderformMaps = new HashMap<Double, Double>();
        do {
            List<Object[]> temps = factory.getCurrentSession().createCriteria(Orderform.class)
                    .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId())).add(Restrictions.eq("buy", true))
                    .setProjection(prolistOrderoform).addOrder(Order.desc("price")).addOrder(Order.desc("id"))
                    .setFirstResult(curcount).setMaxResults(20).list();
            for (Object[] temp : temps) {
                Double price = Double.parseDouble(temp[0].toString());
                Double count = Double.parseDouble(temp[1].toString());
                if (buyorderformMaps.containsKey(price)) {
                    buyorderformMaps.put(price, MathHelper.add(buyorderformMaps.get(price), count, 5));
                } else {
                    buyorderformMaps.put(price, count);
                    keys.add(price);
                }
            }
            if (keys.size() == 0)
                break;
            if (keys.size() > 7
                    || keys.size() == precount && buyorderformMaps.get(keys.get(keys.size() - 1)) == precounts) {
                break;
            } else {
                precount = keys.size();
                precounts = buyorderformMaps.get(keys.get(keys.size() - 1));
                curcount += 20;
            }
        } while (precount < targetcount);
        Collections.sort(keys);
        Collections.reverse(keys);
        for (Double key : keys) {
            if (orderforms.size() < targetcount) {
                Orderform orderform = new Orderform();
                orderform.setBuy(true);
                orderform.setPrice(key);
                orderform.setCount(buyorderformMaps.get(key));
                orderforms.add(orderform);
            } else {
                break;
            }
        }
        // 当前卖单
        curcount = 0;
        precount = 0;
        precounts = 0;
        keys.clear();
        Map<Double, Double> sellorderformMaps = new HashMap<Double, Double>();
        do {
            List<Object[]> temps = factory.getCurrentSession().createCriteria(Orderform.class)
                    .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId())).add(Restrictions.eq("buy", false))
                    .setProjection(prolistOrderoform).addOrder(Order.asc("price")).addOrder(Order.desc("id"))
                    .setFirstResult(curcount).setMaxResults(20).setCacheable(true).list();
            for (Object[] temp : temps) {
                Double price = Double.parseDouble(temp[0].toString());
                Double count = Double.parseDouble(temp[1].toString());
                if (sellorderformMaps.containsKey(price)) {
                    sellorderformMaps.put(price, MathHelper.add(sellorderformMaps.get(price), count, 5));
                } else {
                    sellorderformMaps.put(price, count);
                    keys.add(price);
                }
            }
            if (keys.size() == 0)
                break;
            if (keys.size() > 7
                    || keys.size() == precount && sellorderformMaps.get(keys.get(keys.size() - 1)) == precounts) {
                break;
            } else {
                precount = keys.size();
                precounts = sellorderformMaps.get(keys.get(keys.size() - 1));
                curcount += 20;
            }
        } while (precount < targetcount);
        Collections.sort(keys);
        for (Double key : keys) {
            if (orderforms.size() < targetcount * 2) {
                Orderform orderform = new Orderform();
                orderform.setBuy(false);
                orderform.setPrice(key);
                orderform.setCount(sellorderformMaps.get(key));
                orderforms.add(orderform);
            } else {
                break;
            }
        }
        request.setAttribute("orderforms", orderforms);
        // 24小最高价
        Object toppriceObj = factory.getCurrentSession().createCriteria(Traderecord.class)
                .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId()))
                .add(Restrictions.ge("tradetime", tradetimebefore24hourtime)).setProjection(Projections.max("price"))
                .setCacheable(true).uniqueResult();
        request.setAttribute("topprice24hour", toppriceObj == null ? 0 : Double.parseDouble(toppriceObj.toString()));
        // 24小最低价
        Object bottompriceObj = factory.getCurrentSession().createCriteria(Traderecord.class)
                .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId()))
                .add(Restrictions.ge("tradetime", tradetimebefore24hourtime)).setProjection(Projections.min("price"))
                .setCacheable(true).uniqueResult();
        request.setAttribute("bottomprice24hour",
                bottompriceObj == null ? 0 : Double.parseDouble(bottompriceObj.toString()));
        // 24小时成交量
        Object tradecountObj = factory.getCurrentSession().createCriteria(Traderecord.class)
                .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId()))
                .add(Restrictions.ge("tradetime", tradetimebefore24hourtime)).setProjection(Projections.sum("count"))
                .setCacheable(true).uniqueResult();
        request.setAttribute("tradecount24hour",
                tradecountObj == null ? 0 : Double.parseDouble(tradecountObj.toString()));
        // 24小时成交额
        Object trademoneyObj = factory.getCurrentSession().createCriteria(Traderecord.class)
                .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId()))
                .add(Restrictions.ge("tradetime", tradetimebefore24hourtime)).setProjection(Projections.sum("money"))
                .setCacheable(true).uniqueResult();
        request.setAttribute("trademoney24hour",
                trademoneyObj == null ? 0 : Double.parseDouble(trademoneyObj.toString()));
        // 最近成交记录
        List<Traderecord> latelytraderecords = factory.getCurrentSession().createCriteria(Traderecord.class)
                .add(Restrictions.eq("virtualcoin.id", virtualcoin.getId())).addOrder(Order.desc("tradetime"))
                .setMaxResults(20).setCacheable(true).list();
        request.setAttribute("latelytraderecords", latelytraderecords);
        // 交易密码
        request.setAttribute("tradepassword", session.getAttribute("tradepassword"));
        frontpageCommon();
        return succeed();
    }

    // 交易处理
    public void dispose() throws Exception {
        Member member = (Member) session.getAttribute("member");
        int virtualcoinId = Integer.parseInt(request.getParameter("virtualcoinid"));
        Virtualcoin virtualcoin = (Virtualcoin) factory.getCurrentSession().get(Virtualcoin.class, virtualcoinId);
        String callbackurl = "/trade/" + virtualcoin.getEunit().toLowerCase() + ".html";
        int shibiguId = 2; // 什币股ID
        // 买方上级（可能 多个）分销奖励基数集合
        Map<Integer, Double> toPAwardCountMaps = new HashMap<Integer, Double>();
        List<Integer> memberIds = new ArrayList<Integer>();
        Map<Integer, Member> memberMaps = new HashMap<Integer, Member>();
        List<Integer> membervirtualcoinIds = new ArrayList<Integer>();
        Map<Integer, MemberVirtualcoin> membervirtualcoinMaps = new HashMap<Integer, MemberVirtualcoin>();
        MemberAwardsetup memberAwardsetup = (MemberAwardsetup) factory.getCurrentSession().get(MemberAwardsetup.class,
                1);
        Virtualcoin virtualcoinAward = (Virtualcoin) factory.getCurrentSession().load(Virtualcoin.class, shibiguId); // 什币股
        if (member == null) {
            response.sendRedirect("/member/login?callbackurl=" + URLEncoder.encode(callbackurl));
            return;
        }
        if (member.getTradepassword() == null) {
            PageHelper.alertAndRedirect("您还设置交易密码，请前往会员中心密码修改功能里面设置您的交易密码，再回来交易吧！", "/member/");
            return;
        }
        String tradepassword = request.getParameter("tradepassword");
        if (!member.getTradepassword().equals(StringHelper.md5(tradepassword))) {
            PageHelper.alertAndRedirect("交易密码错误，交易失败。", callbackurl);
            return;
        }
        if (!virtualcoin.isTrading()) {
            PageHelper.alertAndRedirect("交易暂未开放，敬请期待。", callbackurl);
            return;
        }
        double price = MathHelper.places(Double.parseDouble(request.getParameter("price")),
                virtualcoin.getPriceinputdecimalsize());
        if (price <= 0) {
            PageHelper.alertAndRedirect("交易价必须大于0", callbackurl);
            return;
        }
        double countPredict = MathHelper.places(Double.parseDouble(request.getParameter("count")),
                virtualcoin.getCountinputdecimalsize()); // 预计交易数量
        if (countPredict < virtualcoin.getCountfloor()) {
            PageHelper.alertAndRedirect("交易数量不能低于交易下限——" + virtualcoin.getCountfloor() + "个", callbackurl);
            return;
        }
        boolean isbuy = Integer.parseInt(request.getParameter("buy")) == 1;
        ProjectionList plMember = Projections.projectionList();
        plMember.add(Projections.property("enabled"));
        plMember.add(Projections.property("sessionId"));
        plMember.add(Projections.property("balanceactive"));
        Object[] memberObjs = (Object[]) factory.getCurrentSession().createCriteria(Member.class)
                .add(Restrictions.eq("id", member.getId())).setProjection(plMember).setMaxResults(1).uniqueResult();
        if (!Boolean.parseBoolean(memberObjs[0].toString())) {
            session.invalidate();
            PageHelper.alertAndRedirect("您的账号已被冻结，如有疑问请联系客服。", callbackurl);
            return;
        } else if (!memberObjs[1].toString().equals(session.getId())) {
            session.invalidate();
            PageHelper.alertAndRedirect("您的账号已在其它地方登录，请确保是否您本人操作，如果非本人操作或许您的密码已泄露，请及时修改您的密码。", callbackurl);
            return;
        }
        if (isbuy) {
            if (Double.parseDouble(memberObjs[2].toString()) < price * countPredict) {
                PageHelper.alertAndRedirect("您的账户CNT余额不足", callbackurl);
                return;
            }
        }
        memberIds.add(member.getId());
        if (memberAwardsetup.getTrade() > 0 && member.getParentId() > 0) {
            memberIds.add(member.getParentId());
            Object parentvirtualcoinObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class)
                    .add(Restrictions.eq("member.id", member.getParentId()))
                    .add(Restrictions.eq("virtualcoin.id", shibiguId)).setProjection(Projections.property("id"))
                    .setMaxResults(1).setCacheable(true).uniqueResult();
            if (parentvirtualcoinObjId == null) {
                Member parent = (Member) factory.getCurrentSession().load(Member.class, member.getParentId());
                MemberVirtualcoin parentvirtualcoin = new MemberVirtualcoin(virtualcoinAward, parent, 0, 0);
                factory.getCurrentSession().save(parentvirtualcoin);
                parentvirtualcoinObjId = parentvirtualcoin.getId();
            }
            membervirtualcoinIds.add(Integer.parseInt(parentvirtualcoinObjId.toString()));
        }
        double coundPredictOriginal = countPredict; // 预计交易数量（原始数量）
        ProjectionList plMembervirtualcoin = Projections.projectionList();
        plMembervirtualcoin.add(Projections.property("id"));
        plMembervirtualcoin.add(Projections.property("countactive"));
        Object[] membervirtualcoinObjs = (Object[]) factory.getCurrentSession().createCriteria(MemberVirtualcoin.class)
                .add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("virtualcoin.id", virtualcoinId))
                .setProjection(plMembervirtualcoin).setMaxResults(1).uniqueResult();
        if (!isbuy) {
            if (membervirtualcoinObjs == null) {
                PageHelper.alertAndRedirect("您暂无" + virtualcoin.getCname(), callbackurl);
                return;
            }
            if (Double.parseDouble(membervirtualcoinObjs[1].toString()) < countPredict) {
                PageHelper.alertAndRedirect("您的" + virtualcoin.getCname() + "余额不足", callbackurl);
                return;
            }
        } else {
            if (membervirtualcoinObjs == null) {
                MemberVirtualcoin membervirtualcoin = new MemberVirtualcoin(virtualcoin, member, 0, 0, null);
                factory.getCurrentSession().save(membervirtualcoin);
                membervirtualcoinObjs = new Object[1];
                membervirtualcoinObjs[0] = membervirtualcoin.getId();
            }
        }
        membervirtualcoinIds.add(Integer.parseInt(membervirtualcoinObjs[0].toString()));
        Criteria criteriaOrderform = factory.getCurrentSession().createCriteria(Orderform.class)
                .add(Restrictions.eq("virtualcoin.id", virtualcoinId)).add(Restrictions.eq("buy", !isbuy));
        if (isbuy)
            criteriaOrderform.add(Restrictions.le("price", price));
        else
            criteriaOrderform.add(Restrictions.ge("price", price));
        List<Integer> orderformIds = criteriaOrderform.setProjection(Projections.property("id")).list();
        if (orderformIds.size() > 0) {
            Criteria criteriaOrderformReal = factory.getCurrentSession().createCriteria(Orderform.class)
                    .add(Restrictions.in("id", orderformIds));
            if (isbuy)
                criteriaOrderformReal.addOrder(Order.asc("price"));
            else
                criteriaOrderformReal.addOrder(Order.desc("price"));
            List<Orderform> orderforms = criteriaOrderformReal.addOrder(Order.asc("id"))
                    .setLockMode(LockMode.PESSIMISTIC_WRITE).list();
            for (Orderform orderform : orderforms) {
                if (countPredict > 0) {
                    int totherMemberId = orderform.getMember().getId();
                    if (!memberIds.contains(totherMemberId)) {
                        memberIds.add(totherMemberId);
                        int totherParentId = (Integer) factory.getCurrentSession().createCriteria(Member.class)
                                .createAlias("orderforms", "orderforms")
                                .add(Restrictions.eq("orderforms.id", orderform.getId()))
                                .setProjection(Projections.property("parentId")).setMaxResults(1).setCacheable(true)
                                .uniqueResult();

                        if (memberAwardsetup.getTrade() > 0 && totherParentId > 0
                                && !memberIds.contains(totherParentId)) {
                            memberIds.add(totherParentId);
                            Object parentvirtualcoinObjId = factory.getCurrentSession()
                                    .createCriteria(MemberVirtualcoin.class)
                                    .add(Restrictions.eq("member.id", totherParentId))
                                    .add(Restrictions.eq("virtualcoin.id", shibiguId))
                                    .setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true)
                                    .uniqueResult();
                            if (parentvirtualcoinObjId == null) {
                                Member parent = (Member) factory.getCurrentSession().load(Member.class, totherParentId);
                                MemberVirtualcoin parentvirtualcoin = new MemberVirtualcoin(virtualcoinAward, parent, 0,
                                        0);
                                factory.getCurrentSession().save(parentvirtualcoin);
                                parentvirtualcoinObjId = parentvirtualcoin.getId();
                            }
                            membervirtualcoinIds.add(Integer.parseInt(parentvirtualcoinObjId.toString()));
                        }

                        Object totherMembervirtualcoinObjId = factory.getCurrentSession()
                                .createCriteria(MemberVirtualcoin.class)
                                .add(Restrictions.eq("member.id", totherMemberId))
                                .add(Restrictions.eq("virtualcoin.id", virtualcoinId))
                                .setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true)
                                .uniqueResult();
                        if (totherMembervirtualcoinObjId == null) {
                            MemberVirtualcoin membervirtualcoinTother = new MemberVirtualcoin(virtualcoin,
                                    orderform.getMember(), 0, 0, null);
                            factory.getCurrentSession().save(membervirtualcoinTother);
                            totherMembervirtualcoinObjId = membervirtualcoinTother.getId();
                        }
                        membervirtualcoinIds.add(Integer.parseInt(totherMembervirtualcoinObjId.toString()));
                    }
                } else {
                    break;
                }
            }
            Collections.sort(memberIds);
            Collections.sort(membervirtualcoinIds);
            List<Member> members = factory.getCurrentSession().createCriteria(Member.class)
                    .add(Restrictions.in("id", memberIds)).setLockMode(LockMode.PESSIMISTIC_WRITE).list();
            for (Member mtmp : members)
                memberMaps.put(mtmp.getId(), mtmp);
            List<MemberVirtualcoin> membervirtualcoins = factory.getCurrentSession()
                    .createCriteria(MemberVirtualcoin.class).add(Restrictions.in("id", membervirtualcoinIds))
                    .setLockMode(LockMode.PESSIMISTIC_WRITE).list();
            for (MemberVirtualcoin mvtmp : membervirtualcoins)
                membervirtualcoinMaps.put(mvtmp.getMember().getId(), mvtmp);
            RmbrecordType rmbrecordTypeMe = (RmbrecordType) factory.getCurrentSession().load(RmbrecordType.class,
                    isbuy ? 3 : 4);
            RmbrecordType rmbrecordTypeTother = (RmbrecordType) factory.getCurrentSession().load(RmbrecordType.class,
                    isbuy ? 4 : 3);
            VcoinrecordType vcoinrecordTypeMe = (VcoinrecordType) factory.getCurrentSession()
                    .load(VcoinrecordType.class, isbuy ? 1 : 2);
            VcoinrecordType vcoinrecordTypeTother = (VcoinrecordType) factory.getCurrentSession()
                    .load(VcoinrecordType.class, isbuy ? 2 : 1);
            // 正式开始交易
            for (Orderform orderform : orderforms) {
                if (countPredict > 0) {
                    double countCurform = 0; // 当前挂单交易数量
                    boolean eatall = false; // 是否全吃
                    Member memberMe = memberMaps.get(member.getId()); // 当前用户
                    Member memberTother = memberMaps.get(orderform.getMember().getId()); // 对方用户
                    MemberVirtualcoin membervirtualcoinMe = membervirtualcoinMaps.get(memberMe.getId()); // 当前用户虚拟币
                    MemberVirtualcoin membervirtualcoinTother = membervirtualcoinMaps.get(memberTother.getId()); // 对方用户虚拟币
                    if (orderform.getCount() > countPredict) {
                        countCurform = countPredict;
                        orderform.setCount(MathHelper.sub(orderform.getCount(), countPredict, 5));
                    } else {
                        countCurform = orderform.getCount();
                        eatall = true;
                    }
                    double money = MathHelper.mul(orderform.getPrice(), countCurform, 2); // 交易金额
                    double moneyfee = MathHelper.mul(money, virtualcoin.getMoneyrate(), 2); // 金额手续费
                    double virtualcoinfee = MathHelper.mul(countCurform, virtualcoin.getVcoinrate(), 5); // 币手续费
                    if (isbuy) {
                        memberMe.setBalanceactive(MathHelper.sub(memberMe.getBalanceactive(), money, 2));
                        membervirtualcoinMe.setCountactive(
                                MathHelper.sub(MathHelper.add(membervirtualcoinMe.getCountactive(), countCurform, 5),
                                        virtualcoinfee, 5));
                        memberTother.setBalanceactive(
                                MathHelper.sub(MathHelper.add(memberTother.getBalanceactive(), money, 2), moneyfee, 2));
                        membervirtualcoinTother.setCountdisable(
                                MathHelper.sub(membervirtualcoinTother.getCountdisable(), countCurform, 5));
                    } else {
                        memberMe.setBalanceactive(
                                MathHelper.sub(MathHelper.add(memberMe.getBalanceactive(), money, 2), moneyfee, 2));
                        membervirtualcoinMe
                                .setCountactive(MathHelper.sub(membervirtualcoinMe.getCountactive(), countCurform, 5));
                        memberTother.setBalancedisable(MathHelper.sub(memberTother.getBalancedisable(), money, 2));
                        membervirtualcoinTother.setCountactive(MathHelper.sub(
                                MathHelper.add(membervirtualcoinTother.getCountactive(), countCurform, 5),
                                virtualcoinfee, 5));
                    }
                    // 交易记录
                    Traderecord traderecord = new Traderecord(isbuy ? memberTother : memberMe, virtualcoin,
                            isbuy ? memberMe : memberTother, isbuy, orderform.getPrice(), countCurform, money, moneyfee,
                            virtualcoinfee, new Date());
                    factory.getCurrentSession().save(traderecord);
                    // 账流水记录
                    Rmbrecord rmbrecordCur = new Rmbrecord(rmbrecordTypeMe, memberMe,
                            isbuy ? -money : MathHelper.sub(money, moneyfee, 2), isbuy ? 0 : moneyfee,
                            memberMe.getBalanceactive(), memberMe.getBalancedisable(), new Date());
                    factory.getCurrentSession().save(rmbrecordCur);
                    Rmbrecord rmbrecordTother = new Rmbrecord(rmbrecordTypeTother, memberTother,
                            isbuy ? MathHelper.sub(money, moneyfee, 2) : -money, isbuy ? moneyfee : 0,
                            memberTother.getBalanceactive(), memberTother.getBalancedisable(), new Date());
                    factory.getCurrentSession().save(rmbrecordTother);
                    // 币流水记录
                    Vcoinrecord vcoinrecordCur = new Vcoinrecord(vcoinrecordTypeMe, virtualcoin, memberMe,
                            isbuy ? MathHelper.sub(countCurform, virtualcoinfee, 5) : -countCurform,
                            isbuy ? virtualcoinfee : 0, new Date());
                    factory.getCurrentSession().save(vcoinrecordCur);
                    Vcoinrecord vcoinrecordTother = new Vcoinrecord(vcoinrecordTypeTother, virtualcoin, memberTother,
                            isbuy ? -countCurform : MathHelper.sub(countCurform, virtualcoinfee, 5),
                            isbuy ? 0 : virtualcoinfee, new Date());
                    factory.getCurrentSession().save(vcoinrecordTother);

                    // 奖励赠送

                    // CNT交易 奖励对方上线赠送CNT
                    int amid = isbuy ? memberTother.getParentId() : memberMe.getParentId();
                    if (amid != 0) {
                        if (!toPAwardCountMaps.containsKey(amid)) {
                            toPAwardCountMaps.put(amid, moneyfee);
                        } else {
                            toPAwardCountMaps.put(amid,
                                    MathHelper.add(toPAwardCountMaps.get(amid), moneyfee, RMB_PRECISION));
                        }
                    }
                    virtualcoin.setPrice(orderform.getPrice()); // 保存当前价
                    if (eatall) {
                        factory.getCurrentSession().delete(orderform);
                    } else {
                        boolean cantrade = orderform.getCount() > MathHelper.mul(virtualcoin.getCountfloor(),
                                virtualcoin.getVcoinrate(), 5);
                        if (!cantrade) {
                            if (isbuy) {
                                membervirtualcoinTother.setCountactive(MathHelper
                                        .add(membervirtualcoinTother.getCountactive(), orderform.getCount(), 5));
                                membervirtualcoinTother.setCountdisable(MathHelper
                                        .sub(membervirtualcoinTother.getCountdisable(), orderform.getCount(), 5));
                            } else {
                                double moneyresidue = MathHelper.mul(orderform.getCount(), orderform.getPrice(), 2); // 剩余金额
                                memberTother.setBalanceactive(
                                        MathHelper.add(memberTother.getBalanceactive(), moneyresidue, 2));
                                memberTother.setBalancedisable(
                                        MathHelper.sub(memberTother.getBalancedisable(), moneyresidue, 2));
                            }
                            factory.getCurrentSession().delete(orderform);
                        }
                    }
                    countPredict = MathHelper.sub(countPredict, countCurform, 5);
                } else {
                    break;
                }
            }
        } else {
            memberMaps.put(member.getId(), (Member) factory.getCurrentSession().load(Member.class, member.getId(),
                    LockMode.PESSIMISTIC_WRITE));
            membervirtualcoinMaps.put(member.getId(),
                    (MemberVirtualcoin) factory.getCurrentSession().load(MemberVirtualcoin.class,
                            Integer.parseInt(membervirtualcoinObjs[0].toString()), LockMode.PESSIMISTIC_WRITE));
        }

        for (Map.Entry<Integer, Double> pams : toPAwardCountMaps.entrySet()) {
            // 发放奖励
            // 奖励上级
            if (MathHelper.mul(pams.getValue(), memberAwardsetup.getTrade(), RMB_PRECISION) > 0.001) {
                Member parent = memberMaps.get(pams.getKey());
                if (parent != null) {
                	// 账流水记录
                    Rmbrecord rmbrecordCur = new Rmbrecord(
                            (RmbrecordType) factory.getCurrentSession().load(RmbrecordType.class, 7), parent,
                            MathHelper.mul(pams.getValue(), memberAwardsetup.getTrade(), RMB_PRECISION), 0,
                            parent.getBalanceactive(), parent.getBalancedisable(), new Date());
                    factory.getCurrentSession().save(rmbrecordCur);
                    parent.setBalanceactive(MathHelper.add(parent.getBalanceactive(),
                            MathHelper.mul(pams.getValue(), memberAwardsetup.getTrade(), RMB_PRECISION), RMB_PRECISION));// 1
				}
            }
        }

        String message = "交易成功";
        if (countPredict > 0) {
            boolean cantrade = countPredict >= virtualcoin.getCountfloor();
            if (cantrade) {
                // 挂剩余单
                Orderform orderform = new Orderform(virtualcoin, member, isbuy, price, countPredict, countPredict,
                        new Date());
                factory.getCurrentSession().save(orderform);
                double money = MathHelper.mul(price, countPredict, 2);
                Member memberMe = memberMaps.get(member.getId());
                if (isbuy) {
                    memberMe.setBalanceactive(MathHelper.sub(memberMe.getBalanceactive(), money, 2));
                    memberMe.setBalancedisable(MathHelper.add(memberMe.getBalancedisable(), money, 2));
                } else {
                    MemberVirtualcoin membervirtualcoinMe = membervirtualcoinMaps.get(memberMe.getId());
                    membervirtualcoinMe
                            .setCountactive(MathHelper.sub(membervirtualcoinMe.getCountactive(), countPredict, 5));
                    membervirtualcoinMe
                            .setCountdisable(MathHelper.add(membervirtualcoinMe.getCountdisable(), countPredict, 5));
                }
                if (countPredict < coundPredictOriginal) {
                    message += "，并对剩余的" + countPredict + "个" + virtualcoin.getCname() + "进行了挂" + (isbuy ? "买" : "卖")
                            + "单";
                    recordMemberLog((isbuy ? "买" : "卖") + "单交易，虚拟币：" + virtualcoin.getCname() + "，价格：" + price
                            + "，并对剩余的" + countPredict + "个" + virtualcoin.getCname() + "进行了挂单");
                } else {
                    message = "挂单成功";
                    recordMemberLog("发起" + (isbuy ? "买" : "卖") + "单，虚拟币：" + virtualcoin.getCname() + "，价格：" + price
                            + "，数量" + countPredict);
                }
            } else {
                message += "，剩余的" + countPredict + "个" + virtualcoin.getCname() + "因低于挂单下限，故无法进行挂单";
                recordMemberLog((isbuy ? "买" : "卖") + "单交易，虚拟币：" + virtualcoin.getCname() + "，价格：" + price + "，剩余的"
                        + countPredict + "个" + virtualcoin.getCname() + "因低于挂单下限，故无法进行挂单");
            }
        }
        session.setAttribute("member", member);
        session.setAttribute("tradepassword", tradepassword);
        PageHelper.alertAndRedirect(message, callbackurl);
    }

    // 撤销挂单
    public void revocation() throws Exception {
        Member member = (Member) session.getAttribute("member");
        int virtualcoinId = Integer.parseInt(request.getParameter("virtualcoinid"));
        Virtualcoin virtualcoin = (Virtualcoin) factory.getCurrentSession().get(Virtualcoin.class, virtualcoinId);
        String callbackurl = "/trade/" + virtualcoin.getEunit().toLowerCase() + ".html";
        int orderformId = Integer.parseInt(request.getParameter("orderformid"));
        if (member == null) {
            response.sendRedirect(
                    "/member/login?callbackurl=" + URLEncoder.encode("/trade/revocation/" + orderformId + ".html"));
            return;
        } else {
            if (!member.isEnabled()) {
                session.invalidate();
                PageHelper.alertAndRedirect("您的账号已被冻结，如有疑问请联系客服。", callbackurl);
                return;
            } else if (!member.getSessionId().equals(session.getId())) {
                session.invalidate();
                PageHelper.alertAndRedirect("您的账号已在其它地方登录，请确保是否您本人操作，如果非本人操作或许您的密码已泄露，请及时修改您的密码。", callbackurl);
                return;
            }
        }
        Object orderformMemberObjId = factory.getCurrentSession().createCriteria(Orderform.class)
                .add(Restrictions.eq("id", orderformId)).setProjection(Projections.property("member.id"))
                .setCacheable(true).uniqueResult();
        if (orderformMemberObjId == null || Integer.parseInt(orderformMemberObjId.toString()) != member.getId()) {
            PageHelper.alertAndRedirect("您的该笔挂单可能已被成交或撤销", callbackurl);
            return;
        }
        Orderform orderform = (Orderform) factory.getCurrentSession().get(Orderform.class, orderformId,
                LockMode.PESSIMISTIC_WRITE);
        if (orderform == null) {
            PageHelper.alertAndRedirect("您的该笔挂单可能已被成交或撤销", callbackurl);
            return;
        }
        member = (Member) factory.getCurrentSession().get(Member.class, member.getId(), LockMode.PESSIMISTIC_WRITE);
        if (orderform.isBuy()) {
            double money = MathHelper.mul(orderform.getPrice(), orderform.getCount(), 2);
            member.setBalanceactive(MathHelper.add(member.getBalanceactive(),
                    member.getBalancedisable() < money ? member.getBalancedisable() : money, 2));
            member.setBalancedisable(
                    member.getBalancedisable() < money ? 0 : MathHelper.sub(member.getBalancedisable(), money, 2));
        } else {
            Object mvObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class)
                    .add(Restrictions.eq("member.id", member.getId()))
                    .add(Restrictions.eq("virtualcoin.id", virtualcoinId)).setProjection(Projections.property("id"))
                    .setMaxResults(1).setCacheable(true).uniqueResult();
            MemberVirtualcoin membervirtualcoin = (MemberVirtualcoin) factory.getCurrentSession()
                    .load(MemberVirtualcoin.class, Integer.parseInt(mvObjId.toString()), LockMode.PESSIMISTIC_WRITE);
            membervirtualcoin.setCountactive(MathHelper.add(membervirtualcoin.getCountactive(),
                    membervirtualcoin.getCountdisable() < orderform.getCount() ? membervirtualcoin.getCountdisable()
                            : orderform.getCount(),
                    5));
            membervirtualcoin.setCountdisable(membervirtualcoin.getCountdisable() < orderform.getCount() ? 0
                    : MathHelper.sub(membervirtualcoin.getCountdisable(), orderform.getCount(), 5));
        }
        factory.getCurrentSession().delete(orderform);
        recordMemberLog("撤销" + (orderform.isBuy() ? "买" : "卖") + "单，虚拟币：" + orderform.getVirtualcoin().getCname()
                + "，价格：" + orderform.getPrice() + "，数量：" + orderform.getCount());
        PageHelper.alertAndRedirect("撤销成功", callbackurl);
    }

}
