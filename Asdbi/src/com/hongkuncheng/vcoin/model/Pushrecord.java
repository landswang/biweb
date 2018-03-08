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
@Table(name = "pushrecord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pushrecord implements java.io.Serializable {

	private int id;
	private Member memberByReceiverId;
	private Member memberBySenderId;
	private Virtualcoin virtualcoin;
	private PushrecordState pushrecordState;
	private double count;
	private double money;
	private Date pushtime;

	public Pushrecord() {
	}

	public Pushrecord(Member memberByReceiverId, Member memberBySenderId, Virtualcoin virtualcoin,
			PushrecordState pushrecordState, double count, double money, Date pushtime) {
		this.memberByReceiverId = memberByReceiverId;
		this.memberBySenderId = memberBySenderId;
		this.virtualcoin = virtualcoin;
		this.pushrecordState = pushrecordState;
		this.count = count;
		this.money = money;
		this.pushtime = pushtime;
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
	@JoinColumn(name = "receiver_id", nullable = false)
	public Member getMemberByReceiverId() {
		return this.memberByReceiverId;
	}

	public void setMemberByReceiverId(Member memberByReceiverId) {
		this.memberByReceiverId = memberByReceiverId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", nullable = false)
	public Member getMemberBySenderId() {
		return this.memberBySenderId;
	}

	public void setMemberBySenderId(Member memberBySenderId) {
		this.memberBySenderId = memberBySenderId;
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
	@JoinColumn(name = "state_id", nullable = false)
	public PushrecordState getPushrecordState() {
		return this.pushrecordState;
	}

	public void setPushrecordState(PushrecordState pushrecordState) {
		this.pushrecordState = pushrecordState;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "pushtime", nullable = false, length = 19)
	public Date getPushtime() {
		return this.pushtime;
	}

	public void setPushtime(Date pushtime) {
		this.pushtime = pushtime;
	}

}
