package com.hongkuncheng.vcoin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "imagesize")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Imagesize implements java.io.Serializable {

	private int id;
	private int width;
	private int height;
	private String description;

	public Imagesize() {
	}

	public Imagesize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Imagesize(int width, int height, String description) {
		this.width = width;
		this.height = height;
		this.description = description == null || description.isEmpty() ? null : description;
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

	@Column(name = "width", nullable = false)
	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Column(name = "height", nullable = false)
	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description == null || description.isEmpty() ? null : description;
	}

}
