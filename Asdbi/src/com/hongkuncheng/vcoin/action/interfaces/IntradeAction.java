package com.hongkuncheng.vcoin.action.interfaces;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.ConfigHelper;
import com.hongkuncheng.vcoin.helper.DateHelper;
import com.hongkuncheng.vcoin.helper.MathHelper;
import com.hongkuncheng.vcoin.model.Orderform;
import com.hongkuncheng.vcoin.model.Traderecord;
import com.hongkuncheng.vcoin.model.Virtualcoin;

/**
 * 交易接口
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class IntradeAction extends BaseAction {
	
	//主页虚拟币
	public void homevcoin() {
		List<Virtualcoin> virtualcoins = factory.getCurrentSession().createCriteria(Virtualcoin.class).add(Restrictions.eq("enabled", true)).addOrder(Order.desc("sort")).addOrder(Order.desc("id")).setCacheable(true).list();
		JSONArray array = new JSONArray();
		for (Virtualcoin virtualcoin : virtualcoins) {
			JSONObject obj = new JSONObject();
			obj.put("id", virtualcoin.getId());
			obj.put("name", virtualcoin.getCname());
			obj.put("unit", virtualcoin.getEunit());
			obj.put("price", virtualcoin.getPrice());
			Date date = new Date();
			Date tradetodaybegintime = DateHelper.convertToDate((date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate());
			Date yesterday = new Date(date.getTime() - Integer.parseInt(ConfigHelper.getValue("tradestatistical.hour")) * 60 * 60 * 1000);
			Date tradetimebefore24hourtime = DateHelper.convertToDatetime((yesterday.getYear() + 1900) + "-" + (yesterday.getMonth() + 1) + "-" + yesterday.getDate() + " " + yesterday.getHours() + ":" + yesterday.getMinutes() + ":0");
			//开盘价
			Object openingprice = factory.getCurrentSession().createCriteria(Traderecord.class).add(Restrictions.eq("virtualcoin.id", virtualcoin.getId())).add(Restrictions.le("tradetime", tradetodaybegintime)).addOrder(Order.desc("id")).setProjection(Projections.property("price")).setMaxResults(1).setCacheable(true).uniqueResult();
			obj.put("openingprice", openingprice);
			//日涨跌
			double increaserate = openingprice == null ? 0 : MathHelper.sub(virtualcoin.getPrice(), Double.parseDouble(openingprice.toString()), 10);
			if (openingprice != null) {
				double openpriceDouble = Double.parseDouble(openingprice.toString());
				if (openpriceDouble > 0) increaserate = MathHelper.round(MathHelper.div(increaserate, openpriceDouble, 10) * 100, 2);
			}
			obj.put("increaserate", increaserate);
			//24小时数据
			ProjectionList prolistDatas = Projections.projectionList();
			prolistDatas.add(Projections.min("price"));
			prolistDatas.add(Projections.max("price"));
			prolistDatas.add(Projections.sum("count"));
			prolistDatas.add(Projections.sum("money"));
			Object[] objdatas = (Object[]) factory.getCurrentSession().createCriteria(Traderecord.class).add(Restrictions.eq("virtualcoin.id", virtualcoin.getId())).add(Restrictions.ge("tradetime", tradetimebefore24hourtime)).setProjection(prolistDatas).setCacheable(true).uniqueResult();
			//24小时最低价
			obj.put("bottomprice24hour", objdatas[0] == null ? 0 : Double.parseDouble(objdatas[0].toString()));
			//24小时最高价
			obj.put("topprice24hour", objdatas[1] == null ? 0 : Double.parseDouble(objdatas[1].toString()));
			//24小时成交量
			obj.put("tradecount24hour", objdatas[2] == null ? 0 : Double.parseDouble(objdatas[2].toString()));
			//24小时成交额
			obj.put("trademoney24hour", objdatas[3] == null ? 0 : Double.parseDouble(objdatas[3].toString()));
			array.put(obj);
		}
		print(array, "application/json");
	}
	
	//当前虚拟币
	public void currentvcoin() {
		int virtualcoinId = Integer.parseInt(request.getParameter("virtualcoinid"));
		Virtualcoin virtualcoin = (Virtualcoin) factory.getCurrentSession().get(Virtualcoin.class, virtualcoinId);
		if (!virtualcoin.isEnabled()) {
			print("error");
		} else {
			JSONObject obj = new JSONObject();
			obj.put("price", virtualcoin.getPrice());
			Date date = new Date();
			Date tradetodaybegintime = DateHelper.convertToDate((date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate());
			Date yesterday = new Date(date.getTime() - Integer.parseInt(ConfigHelper.getValue("tradestatistical.hour")) * 60 * 60 * 1000);
			Date tradetimebefore24hourtime = DateHelper.convertToDatetime((yesterday.getYear() + 1900) + "-" + (yesterday.getMonth() + 1) + "-" + yesterday.getDate() + " " + yesterday.getHours() + ":" + yesterday.getMinutes() + ":0");
			//今日开盘价
			Object openingprice = factory.getCurrentSession().createCriteria(Traderecord.class).add(Restrictions.eq("virtualcoin.id", virtualcoin.getId())).add(Restrictions.le("tradetime", tradetodaybegintime)).addOrder(Order.desc("id")).setProjection(Projections.property("price")).setMaxResults(1).setCacheable(true).uniqueResult();
			obj.put("openingprice", openingprice == null ? 0 : Double.parseDouble(openingprice.toString()));
			//最佳买价
			Object bestbuyprice = factory.getCurrentSession().createCriteria(Orderform.class).add(Restrictions.eq("virtualcoin.id", virtualcoinId)).add(Restrictions.eq("buy", false)).setProjection(Projections.min("price")).setCacheable(true).uniqueResult();
			obj.put("bestbuyprice", bestbuyprice == null ? 0 : Double.parseDouble(bestbuyprice.toString()));
			//最佳卖价
			Object bestsellprice = factory.getCurrentSession().createCriteria(Orderform.class).add(Restrictions.eq("virtualcoin.id", virtualcoinId)).add(Restrictions.eq("buy", true)).setProjection(Projections.max("price")).setCacheable(true).uniqueResult();
			obj.put("bestsellprice", bestsellprice == null ? 0 : Double.parseDouble(bestsellprice.toString()));
			ProjectionList prolistDatas = Projections.projectionList();
			prolistDatas.add(Projections.min("price"));
			prolistDatas.add(Projections.max("price"));
			prolistDatas.add(Projections.sum("count"));
			prolistDatas.add(Projections.sum("money"));
			Object[] objdatas = (Object[]) factory.getCurrentSession().createCriteria(Traderecord.class).add(Restrictions.eq("virtualcoin.id", virtualcoin.getId())).add(Restrictions.ge("tradetime", tradetimebefore24hourtime)).setProjection(prolistDatas).setCacheable(true).uniqueResult();
			//24小最低价
			obj.put("bottomprice24hour", objdatas[0] == null ? 0 : Double.parseDouble(objdatas[0].toString()));
			//24小最高价
			obj.put("topprice24hour", objdatas[1] == null ? 0 : Double.parseDouble(objdatas[1].toString()));
			//24小时成交量
			obj.put("tradecount24hour", objdatas[2] == null ? 0 : Double.parseDouble(objdatas[2].toString()));
			//24小时成交额
			obj.put("trademoney24hour", objdatas[3] == null ? 0 : Double.parseDouble(objdatas[3].toString()));
			print(obj, "application/json");
		}
	}
	
	//当前买/卖单
	public void currentorderform() {
		int virtualcoinId = Integer.parseInt(request.getParameter("virtualcoinid"));
//		boolean buy = request.getParameter("type").equals("buy");
//		ProjectionList prolistOrderoform = Projections.projectionList();
//		prolistOrderoform.add(Projections.property("price"));
//		prolistOrderoform.add(Projections.property("count"));
//		int curcount = 0;
//		int precount = 0;
//		double precounts = 0;
//		int targetcount = 7;
//		List<Double> keys = new ArrayList<Double>();
//		Map<Double, Double> orderformMaps = new HashMap<Double, Double>();
//		do {
//			Criteria cirteriaObj = factory.getCurrentSession().createCriteria(Orderform.class).add(Restrictions.eq("virtualcoin.id", virtualcoinId));
//			if (buy) cirteriaObj.add(Restrictions.eq("buy", true)).setProjection(prolistOrderoform).addOrder(Order.desc("price"));
//			else cirteriaObj.add(Restrictions.eq("buy", false)).setProjection(prolistOrderoform).addOrder(Order.asc("price"));
//			List<Object[]> temps = cirteriaObj.addOrder(Order.desc("id")).setFirstResult(curcount).setMaxResults(20).setCacheable(true).list();
//			for (Object[] temp : temps) {
//				Double price = Double.parseDouble(temp[0].toString());
//				Double count = Double.parseDouble(temp[1].toString());
//				if (orderformMaps.containsKey(price)) {
//					orderformMaps.put(price, MathHelper.add(orderformMaps.get(price), count, 5));
//				} else {
//					orderformMaps.put(price, count);
//					keys.add(price);
//				}
//			}
//			if (keys.size() > 7 || keys.size() == precount && orderformMaps.get(keys.get(keys.size() - 1)) == precounts) {
//				break;
//			} else {
//				precount = keys.size();
//				precounts = orderformMaps.get(keys.get(keys.size() - 1));
//				curcount += 20;
//			}
//		} while (precount < targetcount);
//		Collections.sort(keys);
//		if (buy) Collections.reverse(keys);
//		JSONArray array = new JSONArray();
//		for (Double key : keys) {
//			if (array.length() < targetcount) {
//				JSONObject obj = new JSONObject();
//				obj.put("price", key);
//				obj.put("count", orderformMaps.get(key));
//				obj.put("total", new BigDecimal(String.valueOf(key)).multiply(new BigDecimal(String.valueOf(orderformMaps.get(key)))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
//				array.put(obj);
//			} else {
//				break;
//			}
//		}
		
		
		JSONArray orderforms = new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		ProjectionList prolistOrderoform = Projections.projectionList();
		prolistOrderoform.add(Projections.property("price"));
		prolistOrderoform.add(Projections.property("count"));
		//当前买单
		int curcount = 0;
		int precount = 0;
		double precounts = 0;
		int targetcount = 7;
		List<Double> keys = new ArrayList<Double>();
		Map<Double, Double> buyorderformMaps = new HashMap<Double, Double>();
		do {
			List<Object[]> temps = factory.getCurrentSession().createCriteria(Orderform.class).add(Restrictions.eq("virtualcoin.id", virtualcoinId)).add(Restrictions.eq("buy", true)).setProjection(prolistOrderoform).addOrder(Order.desc("price")).addOrder(Order.desc("id")).setFirstResult(curcount).setMaxResults(20).list();
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
			if (keys.size() == 0) break;
			if (keys.size() > 7 || keys.size() == precount && buyorderformMaps.get(keys.get(keys.size() - 1)) == precounts) {
				break;
			} else{
				precount = keys.size();
				precounts = buyorderformMaps.get(keys.get(keys.size() - 1));
				curcount += 20;
			}
		} while (precount < targetcount);
		Collections.sort(keys);
		Collections.reverse(keys);
		for (Double key : keys) {
			if (orderforms.length() < targetcount) {
				JSONObject obj = new JSONObject();
				obj.put("buy", true);
				obj.put("price", key);
				obj.put("count", buyorderformMaps.get(key));
				obj.put("total", new BigDecimal(String.valueOf(key)).multiply(new BigDecimal(String.valueOf(buyorderformMaps.get(key)))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				orderforms.put(obj);
			} else {
				break;
			}
		}
		//当前卖单
		curcount = 0;
		precount = 0;
		precounts = 0;
		keys.clear();
		Map<Double, Double> sellorderformMaps = new HashMap<Double, Double>();
		do {
			List<Object[]> temps = factory.getCurrentSession().createCriteria(Orderform.class).add(Restrictions.eq("virtualcoin.id", virtualcoinId)).add(Restrictions.eq("buy", false)).setProjection(prolistOrderoform).addOrder(Order.asc("price")).addOrder(Order.desc("id")).setFirstResult(curcount).setMaxResults(20).setCacheable(true).list();
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
			if (keys.size() == 0) break;
			if (keys.size() > 7 || keys.size() == precount && sellorderformMaps.get(keys.get(keys.size() - 1)) == precounts) {
				break;
			} else{
				precount = keys.size();
				precounts = sellorderformMaps.get(keys.get(keys.size() - 1));
				curcount += 20;
			}
		} while (precount < targetcount);
		Collections.sort(keys);
		for (Double key : keys) {
			if (orderforms.length() < targetcount * 2) {
				JSONObject obj = new JSONObject();
				obj.put("buy", false);
				obj.put("price", key);
				obj.put("count", sellorderformMaps.get(key));
				obj.put("total", new BigDecimal(String.valueOf(key)).multiply(new BigDecimal(String.valueOf(sellorderformMaps.get(key)))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				orderforms.put(obj);
			} else {
				break;
			}
		}
		
		
		print(orderforms, "application/json");
	}
	
	//最近交易记录
	public void latelytraderecord() {
		int virtualcoinId = Integer.parseInt(request.getParameter("virtualcoinid"));
		List<Traderecord> latelytraderecords = factory.getCurrentSession().createCriteria(Traderecord.class).add(Restrictions.eq("virtualcoin.id", virtualcoinId)).addOrder(Order.desc("tradetime")).setMaxResults(20).setCacheable(true).list();
		JSONArray array = new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		for (Traderecord traderecord : latelytraderecords) {
			JSONObject obj = new JSONObject();
			obj.put("tradetime", sdf.format(traderecord.getTradetime()));
			obj.put("type", traderecord.isBuy() ? "买入" : "卖出");
			obj.put("price", traderecord.getPrice());
			obj.put("count", traderecord.getCount());
			obj.put("total", MathHelper.mul(traderecord.getPrice(), traderecord.getCount(), 2));
			array.put(obj);
		}
		print(array, "application/json");
	}
	
}
