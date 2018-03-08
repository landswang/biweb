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
@Table(name = "rmbrecord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Rmbrecord implements java.io.Serializable {

	private int id;
	private RmbrecordType rmbrecordType;
	private Member member;
	private double variable;
	private double fee;
	private double balanceactive;
	private double balancedisable;
	private Date recordtime;

	public Rmbrecord() {
	}

	public Rmbrecord(RmbrecordType rmbrecordType, Member member, double variable, double fee,
			double balanceactive, double balancedisable, Date recordtime) {
		this.rmbrecordType = rmbrecordType;
		this.member = member;
		this.variable = variable;
		this.fee = fee;
		this.balanceactive = balanceactive;
		this.balancedisable = balancedisable;
		this.recordtime = recordtime;
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
	@JoinColumn(name = "type_id", nullable = false)
	public RmbrecordType getRmbrecordType() {
		return this.rmbrecordType;
	}

	public void setRmbrecordType(RmbrecordType rmbrecordType) {
		this.rmbrecordType = rmbrecordType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@Column(name = "variable", nullable = false, precision = 15)
	public double getVariable() {
		return this.variable;
	}

	public void setVariable(double variable) {
		this.variable = variable;
	}

	@Column(name = "fee", nullable = false, precision = 15)
	public double getFee() {
		return this.fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	@Column(name = "balanceactive", nullable = false, precision = 15)
	public double getBalanceactive() {
		return this.balanceactive;
	}

	public void setBalanceactive(double balanceactive) {
		this.balanceactive = balanceactive;
	}

	@Column(name = "balancedisable", nullable = false, precision = 15)
	public double getBalancedisable() {
		return this.balancedisable;
	}

	public void setBalancedisable(double balancedisable) {
		this.balancedisable = balancedisable;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "recordtime", nullable = false, length = 19)
	public Date getRecordtime() {
		return this.recordtime;
	}

	public void setRecordtime(Date recordtime) {
		this.recordtime = recordtime;
	}

}
