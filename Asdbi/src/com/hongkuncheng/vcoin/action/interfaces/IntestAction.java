package com.hongkuncheng.vcoin.action.interfaces;

import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.model.Admin;

/**
 * 测试接口
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class IntestAction extends BaseAction {

	//检测连接状态
	public void index(){
		if (request.getParameter("callback") != null) {
			JSONObject object = new JSONObject();
			try {
				factory.getCurrentSession().load(Admin.class, 1);
				object.put("result", 1);
			} catch (Exception e) {
				object.put("result", 0);
			}
			print(request.getParameter("callback") + "(" + object + ")");
		} else {
			print("callback is null");
		}
	}
	
}
