package com.hongkuncheng.vcoin.action.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.LockMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.ConfigHelper;
import com.hongkuncheng.vcoin.helper.HttpHelper;
import com.hongkuncheng.vcoin.helper.MathHelper;
import com.hongkuncheng.vcoin.helper.SystemHelper;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.MemberVirtualcoin;
import com.hongkuncheng.vcoin.model.Rmbrecord;
import com.hongkuncheng.vcoin.model.RmbrecordType;
import com.hongkuncheng.vcoin.model.Vcoinrecord;
import com.hongkuncheng.vcoin.model.VcoinrecordType;
import com.hongkuncheng.vcoin.model.Virtualcoin;

@Controller
@Scope("prototype")
public class IntransferAction extends BaseAction {
	
	//转移处理
	public void index() {
		if (!SystemHelper.getIpAddr(request).equals(ConfigHelper.getValue("asdbi.host"))) {
			System.out.println("ip:" + SystemHelper.getIpAddr(request));
			print("error");
			return;
		}
		String json = HttpHelper.getRequestStream(request);
		System.out.println("json:" + json);
		JSONObject jsonObj = new JSONObject(json);
		int id = jsonObj.getInt("id");
		double balance = jsonObj.getDouble("balance");
		Member member = (Member) factory.getCurrentSession().get(Member.class, id, LockMode.PESSIMISTIC_WRITE);
		if (balance > 0) {
			member.setBalanceactive(MathHelper.add(member.getBalanceactive(), balance, 2));
			RmbrecordType rmbrecordType = (RmbrecordType) factory.getCurrentSession().load(RmbrecordType.class, 6);
			Rmbrecord rmbrecord = new Rmbrecord(rmbrecordType, member, balance, 0, member.getBalanceactive(), member.getBalancedisable(), new Date());
			factory.getCurrentSession().save(rmbrecord);
		}
		JSONArray jsonArray = jsonObj.getJSONArray("virtualcoins");
		List<Integer> keys = new ArrayList<Integer>();
		Map<Integer, Double> mvMaps = new HashMap<Integer, Double>();
		VcoinrecordType vcoinrecordType = (VcoinrecordType) factory.getCurrentSession().get(VcoinrecordType.class, 12);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			int virtualcoinId = obj.getInt("virtualcoinId");
			double count = obj.getDouble("count");
			Object mvObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("virtualcoin.id", virtualcoinId)).setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true).uniqueResult();
			if (mvObjId != null) {
				keys.add(Integer.parseInt(mvObjId.toString()));
				mvMaps.put(Integer.parseInt(mvObjId.toString()), count);
			} else {
				Virtualcoin virtualcoin = (Virtualcoin) factory.getCurrentSession().load(Virtualcoin.class, virtualcoinId);
				MemberVirtualcoin mv = new MemberVirtualcoin(virtualcoin, member, count, 0);
				factory.getCurrentSession().save(mv);
			}
			Virtualcoin virtualcoin = (Virtualcoin) factory.getCurrentSession().load(Virtualcoin.class, virtualcoinId);
			Vcoinrecord vcoinrecord = new Vcoinrecord(vcoinrecordType, virtualcoin, member, count, 0, new Date());
			factory.getCurrentSession().save(vcoinrecord);
		}
		if (keys.size() > 0) {
			Collections.sort(keys);
			List<MemberVirtualcoin> mvs = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.in("id", keys)).setLockMode(LockMode.PESSIMISTIC_WRITE).list();
			for (MemberVirtualcoin mv : mvs) {
				mv.setCountactive(MathHelper.add(mv.getCountactive(), mvMaps.get(mv.getId()), 5));
			}
		}
		print(1);
	}
	
}
