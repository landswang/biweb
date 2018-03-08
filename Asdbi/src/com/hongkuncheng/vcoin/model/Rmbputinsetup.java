package com.hongkuncheng.vcoin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "rmbputinsetup")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Rmbputinsetup implements java.io.Serializable {

	private int id;
	private double floor;
	private double price;

	public Rmbputinsetup() {
	}

	public Rmbputinsetup(int id, double floor, double price) {
		this.id = id;
		this.floor = floor;
		this.price = price;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "floor", nullable = false, precision = 15)
	public double getFloor() {
		return this.floor;
	}

	public void setFloor(double floor) {
		this.floor = floor;
	}

	@Column(name = "price", nullable = false, precision = 15, scale = 5)
	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
