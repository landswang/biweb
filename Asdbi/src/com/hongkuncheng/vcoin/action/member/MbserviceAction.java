package com.hongkuncheng.vcoin.action.member;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;

/**
 * 客服中心
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MbserviceAction extends BaseAction {

	//客服中心
	public String index() {
		frontpageCommon();
		return succeed();
	}
	
}
