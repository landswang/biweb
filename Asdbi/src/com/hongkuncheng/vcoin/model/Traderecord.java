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
@Table(name = "traderecord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Traderecord implements java.io.Serializable {

	private int id;
	private Member memberBySellmemberId;
	private Virtualcoin virtualcoin;
	private Member memberByBuymemberId;
	private boolean buy;
	private double price;
	private double count;
	private double money;
	private double moneyfee;
	private double virtualcoinfee;
	private Date tradetime;

	public Traderecord() {
	}

	public Traderecord(Member memberBySellmemberId, Virtualcoin virtualcoin, Member memberByBuymemberId, boolean buy,
			double price, double count, double money, double moneyfee, double virtualcoinfee,
			Date tradetime) {
		this.memberBySellmemberId = memberBySellmemberId;
		this.virtualcoin = virtualcoin;
		this.memberByBuymemberId = memberByBuymemberId;
		this.buy = buy;
		this.price = price;
		this.count = count;
		this.money = money;
		this.moneyfee = moneyfee;
		this.virtualcoinfee = virtualcoinfee;
		this.tradetime = tradetime;
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
	@JoinColumn(name = "sellmember_id", nullable = false)
	public Member getMemberBySellmemberId() {
		return this.memberBySellmemberId;
	}

	public void setMemberBySellmemberId(Member memberBySellmemberId) {
		this.memberBySellmemberId = memberBySellmemberId;
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
	@JoinColumn(name = "buymember_id", nullable = false)
	public Member getMemberByBuymemberId() {
		return this.memberByBuymemberId;
	}

	public void setMemberByBuymemberId(Member memberByBuymemberId) {
		this.memberByBuymemberId = memberByBuymemberId;
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

	@Column(name = "money", nullable = false, precision = 15)
	public double getMoney() {
		return this.money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	@Column(name = "moneyfee", nullable = false, precision = 15)
	public double getMoneyfee() {
		return this.moneyfee;
	}

	public void setMoneyfee(double moneyfee) {
		this.moneyfee = moneyfee;
	}

	@Column(name = "virtualcoinfee", nullable = false, precision = 15, scale = 5)
	public double getVirtualcoinfee() {
		return this.virtualcoinfee;
	}

	public void setVirtualcoinfee(double virtualcoinfee) {
		this.virtualcoinfee = virtualcoinfee;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tradetime", nullable = false, length = 19)
	public Date getTradetime() {
		return this.tradetime;
	}

	public void setTradetime(Date tradetime) {
		this.tradetime = tradetime;
	}

}
