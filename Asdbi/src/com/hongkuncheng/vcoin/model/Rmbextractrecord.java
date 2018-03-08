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
@Table(name = "rmbextractrecord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Rmbextractrecord implements java.io.Serializable {

	private int id;
	private Admin admin;
	private Member member;
	private RmbextractrecordState rmbextractrecordState;
	private String cardtype;
	private String cardnumber;
	private double money;
	private double moneyactual;
	private Date extracttime;
	private String checkdtcause;

	public Rmbextractrecord() {
	}

	public Rmbextractrecord(Member member, RmbextractrecordState rmbextractrecordState, String cardtype,
			String cardnumber, double money, double moneyactual, Date extracttime) {
		this.member = member;
		this.rmbextractrecordState = rmbextractrecordState;
		this.cardtype = cardtype;
		this.cardnumber = cardnumber;
		this.money = money;
		this.moneyactual = moneyactual;
		this.extracttime = extracttime;
	}

	public Rmbextractrecord(Admin admin, Member member, RmbextractrecordState rmbextractrecordState, String cardtype,
			String cardnumber, double money, double moneyactual, Date extracttime, String checkdtcause) {
		this.admin = admin;
		this.member = member;
		this.rmbextractrecordState = rmbextractrecordState;
		this.cardtype = cardtype;
		this.cardnumber = cardnumber;
		this.money = money;
		this.moneyactual = moneyactual;
		this.extracttime = extracttime;
		this.checkdtcause = checkdtcause == null || checkdtcause.isEmpty() ? null : checkdtcause;
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
	public RmbextractrecordState getRmbextractrecordState() {
		return this.rmbextractrecordState;
	}

	public void setRmbextractrecordState(RmbextractrecordState rmbextractrecordState) {
		this.rmbextractrecordState = rmbextractrecordState;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "extracttime", nullable = false, length = 19)
	public Date getExtracttime() {
		return this.extracttime;
	}

	public void setExtracttime(Date extracttime) {
		this.extracttime = extracttime;
	}

	@Column(name = "checkdtcause")
	public String getCheckdtcause() {
		return this.checkdtcause;
	}

	public void setCheckdtcause(String checkdtcause) {
		this.checkdtcause = checkdtcause == null || checkdtcause.isEmpty() ? null : checkdtcause;
	}

}
