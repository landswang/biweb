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
@Table(name = "vcoinrecord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vcoinrecord implements java.io.Serializable {

	private int id;
	private VcoinrecordType vcoinrecordType;
	private Virtualcoin virtualcoin;
	private Member member;
	private double variable;
	private double fee;
	private Date recordtime;

	public Vcoinrecord() {
	}

	public Vcoinrecord(VcoinrecordType vcoinrecordType, Virtualcoin virtualcoin, Member member, double variable,
			double fee, Date recordtime) {
		this.vcoinrecordType = vcoinrecordType;
		this.virtualcoin = virtualcoin;
		this.member = member;
		this.variable = variable;
		this.fee = fee;
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
	public VcoinrecordType getVcoinrecordType() {
		return this.vcoinrecordType;
	}

	public void setVcoinrecordType(VcoinrecordType vcoinrecordType) {
		this.vcoinrecordType = vcoinrecordType;
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

	@Column(name = "variable", nullable = false, precision = 15, scale = 5)
	public double getVariable() {
		return this.variable;
	}

	public void setVariable(double variable) {
		this.variable = variable;
	}

	@Column(name = "fee", nullable = false, precision = 15, scale = 5)
	public double getFee() {
		return this.fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
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
