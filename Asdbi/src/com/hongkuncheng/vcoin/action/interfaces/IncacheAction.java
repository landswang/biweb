package com.hongkuncheng.vcoin.action.interfaces;

import java.util.Date;
import java.util.Map;

import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;

/**
 * 缓存接口
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class IncacheAction extends BaseAction {
	
	//清除所有
	public void clearall() {
		Map<String, CollectionMetadata> collectionMaps = factory.getAllCollectionMetadata();
	    for (String collection : collectionMaps.keySet()) factory.evictCollection(collection);
	    Map<String, ClassMetadata> entityMaps = factory.getAllClassMetadata();
	    for (String entity : entityMaps.keySet()) factory.evictEntity(entity);
	    factory.evictQueries();
		print("sucess<br />" + new Date());
	}
	
	//清除对象
	public void clearentity() {
		String clazz = request.getParameter("class");   //如：com.hongkuncheng.exp.model.Member
		int id = request.getParameter("id") == null ? 0 : Integer.parseInt(request.getParameter("id"));
		if (id == 0) factory.evictEntity(clazz);
		else factory.evictEntity(clazz, id);
		print("sucess<br />" + new Date());
	}

	//清除对象集合
	public void clearcollection() {
		String clazz = request.getParameter("class");  //如：com.hongkuncheng.exp.model.Member.articles
		int id = request.getParameter("id") == null ? 0 : Integer.parseInt(request.getParameter("id"));
		if (id == 0) factory.evictCollection(clazz);
		else factory.evictCollection(clazz, id);
		print("sucess<br />" + new Date());
	}
	
}
