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
@Table(name = "rmbputinrecord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Rmbputinrecord implements java.io.Serializable {

	private int id;
	private Admin admin;
	private Member member;
	private RmbputinrecordState rmbputinrecordState;
	private String cardtype;
	private String cardnumber;
	private double money;
	private double moneyactual;
	private String unicode;
	private Date putintime;
	private Date opttime;

	public Rmbputinrecord() {
	}

	public Rmbputinrecord(Member member, RmbputinrecordState rmbputinrecordState, String cardtype, String cardnumber,
			double money, double moneyactual, String unicode, Date putintime) {
		this.member = member;
		this.rmbputinrecordState = rmbputinrecordState;
		this.cardtype = cardtype;
		this.cardnumber = cardnumber;
		this.money = money;
		this.moneyactual = moneyactual;
		this.unicode = unicode;
		this.putintime = putintime;
	}

	public Rmbputinrecord(Admin admin, Member member, RmbputinrecordState rmbputinrecordState, String cardtype,
			String cardnumber, double money, double moneyactual, String unicode, Date putintime, Date opttime) {
		this.admin = admin;
		this.member = member;
		this.rmbputinrecordState = rmbputinrecordState;
		this.cardtype = cardtype;
		this.cardnumber = cardnumber;
		this.money = money;
		this.moneyactual = moneyactual;
		this.unicode = unicode;
		this.putintime = putintime;
		this.opttime = opttime;
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
	@JoinColumn(name = "admin_id")
	public Admin getAdmin() {
		return this.admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "state_id", nullable = false)
	public RmbputinrecordState getRmbputinrecordState() {
		return this.rmbputinrecordState;
	}

	public void setRmbputinrecordState(RmbputinrecordState rmbputinrecordState) {
		this.rmbputinrecordState = rmbputinrecordState;
	}

	@Column(name = "cardtype", nullable = false)
	public String getCardtype() {
		return this.cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	@Column(name = "cardnumber", nullable = false)
	public String getCardnumber() {
		return this.cardnumber;
	}

	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}

	@Column(name = "money", nullable = false, precision = 15)
	public double getMoney() {
		return this.money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	@Column(name = "moneyactual", nullable = false, precision = 15)
	public double getMoneyactual() {
		return this.moneyactual;
	}

	public void setMoneyactual(double moneyactual) {
		this.moneyactual = moneyactual;
	}

	@Column(name = "unicode", nullable = false)
	public String getUnicode() {
		return this.unicode;
	}

	public void setUnicode(String unicode) {
		this.unicode = unicode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "putintime", nullable = false, length = 19)
	public Date getPutintime() {
		return this.putintime;
	}

	public void setPutintime(Date putintime) {
		this.putintime = putintime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "opttime", length = 19)
	public Date getOpttime() {
		return this.opttime;
	}

	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}

}
