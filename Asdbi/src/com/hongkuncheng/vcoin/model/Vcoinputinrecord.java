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
@Table(name = "vcoinputinrecord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vcoinputinrecord implements java.io.Serializable {

	private int id;
	private Virtualcoin virtualcoin;
	private Member member;
	private double count;
	private double fee;
	private String txid;
	private Date putintime;

	public Vcoinputinrecord() {
	}

	public Vcoinputinrecord(Virtualcoin virtualcoin, Member member, double count, double fee, String txid,
			Date putintime) {
		this.virtualcoin = virtualcoin;
		this.member = member;
		this.count = count;
		this.fee = fee;
		this.txid = txid;
		this.putintime = putintime;
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

	@Column(name = "count", nullable = false, precision = 15, scale = 5)
	public double getCount() {
		return this.count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	@Column(name = "fee", nullable = false, precision = 15, scale = 5)
	public double getFee() {
		return this.fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	@Column(name = "txid", nullable = false)
	public String getTxid() {
		return this.txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "putintime", nullable = false, length = 19)
	public Date getPutintime() {
		return this.putintime;
	}

	public void setPutintime(Date putintime) {
		this.putintime = putintime;
	}

}
