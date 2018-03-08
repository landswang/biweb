package com.hongkuncheng.vcoin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "pushsetup")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pushsetup implements java.io.Serializable {

	private int id;
	private double vcoinrate;
	private double vcoinfeefloor;
	private double moneyrate;
	private double moneyfeefloor;

	public Pushsetup() {
	}

	public Pushsetup(int id, double vcoinrate, double vcoinfeefloor, double moneyrate,
			double moneyfeefloor) {
		this.id = id;
		this.vcoinrate = vcoinrate;
		this.vcoinfeefloor = vcoinfeefloor;
		this.moneyrate = moneyrate;
		this.moneyfeefloor = moneyfeefloor;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "vcoinrate", nullable = false, precision = 15, scale = 5)
	public double getVcoinrate() {
		return this.vcoinrate;
	}

	public void setVcoinrate(double vcoinrate) {
		this.vcoinrate = vcoinrate;
	}

	@Column(name = "vcoinfeefloor", nullable = false, precision = 15, scale = 5)
	public double getVcoinfeefloor() {
		return this.vcoinfeefloor;
	}

	public void setVcoinfeefloor(double vcoinfeefloor) {
		this.vcoinfeefloor = vcoinfeefloor;
	}

	@Column(name = "moneyrate", nullable = false, precision = 15, scale = 5)
	public double getMoneyrate() {
		return this.moneyrate;
	}

	public void setMoneyrate(double moneyrate) {
		this.moneyrate = moneyrate;
	}

	@Column(name = "moneyfeefloor", nullable = false, precision = 15, scale = 5)
	public double getMoneyfeefloor() {
		return this.moneyfeefloor;
	}

	public void setMoneyfeefloor(double moneyfeefloor) {
		this.moneyfeefloor = moneyfeefloor;
	}

}
