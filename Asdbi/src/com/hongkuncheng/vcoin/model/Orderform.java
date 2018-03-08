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
@Table(name = "orderform")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Orderform implements java.io.Serializable {

	private int id;
	private Virtualcoin virtualcoin;
	private Member member;
	private boolean buy;
	private double price;
	private double count;
	private double countoriginal;
	private Date placetime;

	public Orderform() {
	}

	public Orderform(Virtualcoin virtualcoin, Member member, boolean buy, double price, double count,
			double countoriginal, Date placetime) {
		this.virtualcoin = virtualcoin;
		this.member = member;
		this.buy = buy;
		this.price = price;
		this.count = count;
		this.countoriginal = countoriginal;
		this.placetime = placetime;
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
	@JoinColumn(name = "virtualcoin_id", nullable = false)
	public Virtualcoin getVirtualcoin() {
		return this.virtualcoin;
	}

	public void setVirtualcoin(Virtualcoin virtualcoin) {
		this.virtualcoin = virtualcoin;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@Column(name = "buy", nullable = false)
	public boolean isBuy() {
		return this.buy;
	}

	public void setBuy(boolean buy) {
		this.buy = buy;
	}

	@Column(name = "price", nullable = false, precision = 15, scale = 5)
	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Column(name = "count", nullable = false, precision = 15, scale = 5)
	public double getCount() {
		return this.count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	@Column(name = "countoriginal", nullable = false, precision = 15, scale = 5)
	public double getCountoriginal() {
		return this.countoriginal;
	}

	public void setCountoriginal(double countoriginal) {
		this.countoriginal = countoriginal;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "placetime", nullable = false, length = 19)
	public Date getPlacetime() {
		return this.placetime;
	}

	public void setPlacetime(Date placetime) {
		this.placetime = placetime;
	}

}
