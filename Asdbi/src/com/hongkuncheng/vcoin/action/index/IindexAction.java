package com.hongkuncheng.vcoin.action.index;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
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
import com.hongkuncheng.vcoin.model.Article;
import com.hongkuncheng.vcoin.model.Traderecord;
import com.hongkuncheng.vcoin.model.Virtualcoin;

@Controller
@Scope("prototype")
public class IindexAction extends BaseAction {

	public String index() {
		Map<Integer, Double> todayincreases = new HashMap<Integer, Double>();
		Map<Integer, Double> virtualMaxprice24hour = new HashMap<Integer, Double>();
		Map<Integer, Double> virtualMinprice24hour = new HashMap<Integer, Double>();
		Map<Integer, Double> virtualTradecount24hour = new HashMap<Integer, Double>();
		Map<Integer, Double> virtualTrademoney24hour = new HashMap<Integer, Double>();
		virtualcoins = factory.getCurrentSession().createCriteria(Virtualcoin.class).add(Restrictions.eq("enabled", true)).addOrder(Order.desc("sort")).addOrder(Order.desc("id")).setCacheable(true).list();
		Date date = new Date();
		Date tradetodaybegintime = DateHelper.convertToDate((date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate());
		Date yesterday = new Date(date.getTime() - Integer.parseInt(ConfigHelper.getValue("tradestatistical.hour")) * 60 * 60 * 1000);
		Date tradetimebefore24hourtime = DateHelper.convertToDatetime((yesterday.getYear() + 1900) + "-" + (yesterday.getMonth() + 1) + "-" + yesterday.getDate() + " " + yesterday.getHours() + ":" + yesterday.getMinutes() + ":0");
		for (Virtualcoin virtualcoin : virtualcoins) {
			//开盘价
			Object openingprice = factory.getCurrentSession().createCriteria(Traderecord.class).add(Restrictions.eq("virtualcoin.id", virtualcoin.getId())).add(Restrictions.le("tradetime", tradetodaybegintime)).addOrder(Order.desc("id")).setProjection(Projections.property("price")).setMaxResults(1).setCacheable(true).uniqueResult();
			//日涨跌
			double increaserate = openingprice == null ? 0 : MathHelper.sub(virtualcoin.getPrice(), Double.parseDouble(openingprice.toString()), 10);
			if (openingprice != null) {
				double openpriceDouble = Double.parseDouble(openingprice.toString());
				if (openpriceDouble > 0) increaserate = MathHelper.round(MathHelper.div(increaserate, openpriceDouble, 10) * 100, 2);
			}
			todayincreases.put(virtualcoin.getId(), increaserate);
			//24小时数据
			ProjectionList prolistDatas = Projections.projectionList();
			prolistDatas.add(Projections.min("price"));
			prolistDatas.add(Projections.max("price"));
			prolistDatas.add(Projections.sum("count"));
			prolistDatas.add(Projections.sum("money"));
			Object[] objdatas = (Object[]) factory.getCurrentSession().createCriteria(Traderecord.class).add(Restrictions.eq("virtualcoin.id", virtualcoin.getId())).add(Restrictions.ge("tradetime", tradetimebefore24hourtime)).setProjection(prolistDatas).setCacheable(true).uniqueResult();
			//24小时最低价
			virtualMinprice24hour.put(virtualcoin.getId(), objdatas[0] == null ? 0 : Double.parseDouble(objdatas[0].toString()));
			//24小时最高价
			virtualMaxprice24hour.put(virtualcoin.getId(), objdatas[1] == null ? 0 : Double.parseDouble(objdatas[1].toString()));
			//24小时成交量
			virtualTradecount24hour.put(virtualcoin.getId(), objdatas[2] == null ? 0 : Double.parseDouble(objdatas[2].toString()));
			//24小时成交额
			virtualTrademoney24hour.put(virtualcoin.getId(), objdatas[3] == null ? 0 : Double.parseDouble(objdatas[3].toString()));
		}
		request.setAttribute("todayincreases", todayincreases);
		request.setAttribute("virtualMaxprice24hour", virtualMaxprice24hour);
		request.setAttribute("virtualMinprice24hour", virtualMinprice24hour);
		request.setAttribute("virtualTradecount24hour", virtualTradecount24hour);
		request.setAttribute("virtualTrademoney24hour", virtualTrademoney24hour);
		//官方公告
		List<Article> noticearticles = factory.getCurrentSession().createCriteria(Article.class).add(Restrictions.eq("articleType.id", 1)).addOrder(Order.desc("publishtime")).setMaxResults(12).setCacheable(true).list();
		request.setAttribute("noticearticles", noticearticles);
		List<Article> hangyearticles = factory.getCurrentSession().createCriteria(Article.class).add(Restrictions.eq("articleType.id", 2)).addOrder(Order.desc("publishtime")).setMaxResults(12).setCacheable(true).list();
		request.setAttribute("hangyearticles", hangyearticles);
		frontpageCommon();
		return succeed();
	}

	public void member(){
		try {
			response.sendRedirect("/member/");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
