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
@Table(name = "vcoinextractrecord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vcoinextractrecord implements java.io.Serializable {

	private int id;
	private Admin admin;
	private Virtualcoin virtualcoin;
	private Member member;
	private VcoinextractrecordState vcoinextractrecordState;
	private String address;
	private double count;
	private double fee;
	private double countactual;
	private String txid;
	private Date extracttime;
	private String remark;
	private String checkdtcause;

	public Vcoinextractrecord() {
	}

	public Vcoinextractrecord(Virtualcoin virtualcoin, Member member, VcoinextractrecordState vcoinextractrecordState,
			String address, double count, double fee, double countactual, Date extracttime) {
		this.virtualcoin = virtualcoin;
		this.member = member;
		this.vcoinextractrecordState = vcoinextractrecordState;
		this.address = address;
		this.count = count;
		this.fee = fee;
		this.countactual = countactual;
		this.extracttime = extracttime;
	}

	public Vcoinextractrecord(Admin admin, Virtualcoin virtualcoin, Member member,
			VcoinextractrecordState vcoinextractrecordState, String address, double count, double fee,
			double countactual, String txid, Date extracttime, String remark, String checkdtcause) {
		this.admin = admin;
		this.virtualcoin = virtualcoin;
		this.member = member;
		this.vcoinextractrecordState = vcoinextractrecordState;
		this.address = address;
		this.count = count;
		this.fee = fee;
		this.countactual = countactual;
		this.txid = txid == null || txid.isEmpty() ? null : txid;
		this.extracttime = extracttime;
		this.remark = remark == null || remark.isEmpty() ? null : remark;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "state_id", nullable = false)
	public VcoinextractrecordState getVcoinextractrecordState() {
		return this.vcoinextractrecordState;
	}

	public void setVcoinextractrecordState(VcoinextractrecordState vcoinextractrecordState) {
		this.vcoinextractrecordState = vcoinextractrecordState;
	}

	@Column(name = "address", nullable = false)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	@Column(name = "countactual", nullable = false, precision = 15, scale = 5)
	public double getCountactual() {
		return this.countactual;
	}

	public void setCountactual(double countactual) {
		this.countactual = countactual;
	}

	@Column(name = "txid")
	public String getTxid() {
		return this.txid;
	}

	public void setTxid(String txid) {
		this.txid = txid == null || txid.isEmpty() ? null : txid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "extracttime", nullable = false, length = 19)
	public Date getExtracttime() {
		return this.extracttime;
	}

	public void setExtracttime(Date extracttime) {
		this.extracttime = extracttime;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null || remark.isEmpty() ? null : remark;
	}

	@Column(name = "checkdtcause")
	public String getCheckdtcause() {
		return this.checkdtcause;
	}

	public void setCheckdtcause(String checkdtcause) {
		this.checkdtcause = checkdtcause == null || checkdtcause.isEmpty() ? null : checkdtcause;
	}

}
