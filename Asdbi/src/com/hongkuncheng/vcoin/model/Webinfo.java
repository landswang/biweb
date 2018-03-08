package com.hongkuncheng.vcoin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "webinfo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Webinfo implements java.io.Serializable {

	private int id;
	private String title;
	private String keywords;
	private String description;

	public Webinfo() {
	}

	public Webinfo(int id, String title) {
		this.id = id;
		this.title = title;
	}

	public Webinfo(int id, String title, String keywords, String description) {
		this.id = id;
		this.title = title;
		this.keywords = keywords == null || keywords.isEmpty() ? null : keywords;
		this.description = description == null || description.isEmpty() ? null : description;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "title", nullable = false)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "keywords", length = 4000)
	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords == null || keywords.isEmpty() ? null : keywords;
	}

	@Column(name = "description", length = 4000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description == null || description.isEmpty() ? null : description;
	}

}
