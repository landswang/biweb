package com.hongkuncheng.vcoin.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Article implements java.io.Serializable {

	private int id;
	private ArticleType articleType;
	private String title;
	private String contents;
	private String image;
	private Date publishtime;

	public Article() {
	}

	public Article(ArticleType articleType, String title, Date publishtime) {
		this.articleType = articleType;
		this.title = title;
		this.publishtime = publishtime;
	}

	public Article(ArticleType articleType, String title, String contents, String image, Date publishtime) {
		this.articleType = articleType;
		this.title = title;
		this.contents = contents == null || contents.isEmpty() ? null : contents;
		this.image = image == null || image.isEmpty() ? null : image;
		this.publishtime = publishtime;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id", nullable = false)
	public ArticleType getArticleType() {
		return this.articleType;
	}

	public void setArticleType(ArticleType articleType) {
		this.articleType = articleType;
	}

	@Column(name = "title", nullable = false)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "contents", length = 65535)
	public String getContents() {
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents == null || contents.isEmpty() ? null : contents;
	}

	@Column(name = "image")
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image == null || image.isEmpty() ? null : image;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "publishtime", nullable = false, length = 19)
	public Date getPublishtime() {
		return this.publishtime;
	}

	public void setPublishtime(Date publishtime) {
		this.publishtime = publishtime;
	}

}
