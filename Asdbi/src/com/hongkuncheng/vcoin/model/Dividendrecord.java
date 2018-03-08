package com.hongkuncheng.vcoin.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "dividendrecord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dividendrecord implements java.io.Serializable {

	private int id;
	private Date tradebegindate;
	private Date tradeenddate;
	private double membervirtualcoinbottom;
	private double virtualcoincounttotal;
	private double dividendmoney;
	private double singleprice;
	private int membercount;
	private Date dividendtime;

	public Dividendrecord() {
	}

	public Dividendrecord(Date tradebegindate, Date tradeenddate, double membervirtualcoinbottom,
			double virtualcoincounttotal, double dividendmoney, double singleprice, int membercount,
			Date dividendtime) {
		this.tradebegindate = tradebegindate;
		this.tradeenddate = tradeenddate;
		this.membervirtualcoinbottom = membervirtualcoinbottom;
		this.virtualcoincounttotal = virtualcoincounttotal;
		this.dividendmoney = dividendmoney;
		this.singleprice = singleprice;
		this.membercount = membercount;
		this.dividendtime = dividendtime;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "tradebegindate", nullable = false, length = 10)
	public Date getTradebegindate() {
		return this.tradebegindate;
	}

	public void setTradebegindate(Date tradebegindate) {
		this.tradebegindate = tradebegindate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "tradeenddate", nullable = false, length = 10)
	public Date getTradeenddate() {
		return this.tradeenddate;
	}

	public void setTradeenddate(Date tradeenddate) {
		this.tradeenddate = tradeenddate;
	}

	@Column(name = "membervirtualcoinbottom", nullable = false, precision = 15, scale = 5)
	public double getMembervirtualcoinbottom() {
		return this.membervirtualcoinbottom;
	}

	public void setMembervirtualcoinbottom(double membervirtualcoinbottom) {
		this.membervirtualcoinbottom = membervirtualcoinbottom;
	}

	@Column(name = "virtualcoincounttotal", nullable = false, precision = 15, scale = 5)
	public double getVirtualcoincounttotal() {
		return this.virtualcoincounttotal;
	}

	public void setVirtualcoincounttotal(double virtualcoincounttotal) {
		this.virtualcoincounttotal = virtualcoincounttotal;
	}

	@Column(name = "dividendmoney", nullable = false, precision = 15, scale = 5)
	public double getDividendmoney() {
		return this.dividendmoney;
	}

	public void setDividendmoney(double dividendmoney) {
		this.dividendmoney = dividendmoney;
	}

	@Column(name = "singleprice", nullable = false, precision = 15, scale = 5)
	public double getSingleprice() {
		return this.singleprice;
	}

	public void setSingleprice(double singleprice) {
		this.singleprice = singleprice;
	}

	@Column(name = "membercount", nullable = false)
	public int getMembercount() {
		return this.membercount;
	}

	public void setMembercount(int membercount) {
		this.membercount = membercount;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dividendtime", nullable = false, length = 19)
	public Date getDividendtime() {
		return this.dividendtime;
	}

	public void setDividendtime(Date dividendtime) {
		this.dividendtime = dividendtime;
	}

}
