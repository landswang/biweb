package com.hongkuncheng.vcoin.action.index;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.DataHelper;
import com.hongkuncheng.vcoin.model.Article;
import com.hongkuncheng.vcoin.model.ArticleType;

@Controller
@Scope("prototype")
public class IarticleAction extends BaseAction {

	//文章列表
	public String index() {
		int articletypeId = Integer.parseInt(request.getParameter("articletypeid"));
		ArticleType articleType = (ArticleType) factory.getCurrentSession().get(ArticleType.class, articletypeId);
		request.setAttribute("articleType", articleType);
		Criteria criteriaArticle = factory.getCurrentSession().createCriteria(Article.class);
		criteriaArticle.add(Restrictions.eq("articleType.id", articletypeId));
		int pageIndex = request.getParameter("pageindex") == null ? 1 : Integer.parseInt(request.getParameter("pageindex"));
		int datasize = 20;
		List<Article> articles = criteriaArticle.addOrder(Order.desc("publishtime")).setFirstResult((pageIndex - 1) * datasize).setMaxResults(datasize).setCacheable(true).list();
		request.setAttribute("articles", articles);
		request.setAttribute("pageindex", pageIndex);
		int recordcount = articles.size() == 0 ? 0 : Integer.parseInt(criteriaArticle.setProjection(Projections.rowCount()).setFirstResult(0).setCacheable(true).uniqueResult().toString());
		request.setAttribute("recordcount", recordcount);
		int pagecount = DataHelper.calcPageCount(recordcount, datasize);
		request.setAttribute("pagecount", pagecount);
		frontpageCommon();
		return succeed();
	}

	//文章详细
	public String detail() {
		int articleId = Integer.parseInt(request.getParameter("articleid"));
		Article article = (Article) factory.getCurrentSession().get(Article.class, articleId);
		request.setAttribute("article", article);
		frontpageCommon();
		return succeed();
	}
	
}
