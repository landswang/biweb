package com.hongkuncheng.vcoin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "member_awardsetup")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MemberAwardsetup implements java.io.Serializable {

	private int id;
	private double certification;
	private double trade;
	private double parent;

	public MemberAwardsetup() {
	}

	public MemberAwardsetup(int id, double certification, double trade, double parent) {
		this.id = id;
		this.certification = certification;
		this.trade = trade;
		this.parent = parent;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "certification", nullable = false, precision = 15, scale = 5)
	public double getCertification() {
		return this.certification;
	}

	public void setCertification(double certification) {
		this.certification = certification;
	}

	@Column(name = "trade", nullable = false, precision = 15, scale = 5)
	public double getTrade() {
		return this.trade;
	}

	public void setTrade(double trade) {
		this.trade = trade;
	}

	@Column(name = "parent", nullable = false, precision = 15, scale = 5)
	public double getParent() {
		return this.parent;
	}

	public void setParent(double parent) {
		this.parent = parent;
	}

}
