package com.hongkuncheng.vcoin.model;

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

@Entity
@Table(name = "member_virtualcoin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MemberVirtualcoin implements java.io.Serializable {

	private int id;
	private Virtualcoin virtualcoin;
	private Member member;
	private double countactive;
	private double countdisable;
	private String walletaddress;

	public MemberVirtualcoin() {
	}

	public MemberVirtualcoin(Virtualcoin virtualcoin, Member member, double countactive, double countdisable) {
		this.virtualcoin = virtualcoin;
		this.member = member;
		this.countactive = countactive;
		this.countdisable = countdisable;
	}

	public MemberVirtualcoin(Virtualcoin virtualcoin, Member member, double countactive, double countdisable,
			String walletaddress) {
		this.virtualcoin = virtualcoin;
		this.member = member;
		this.countactive = countactive;
		this.countdisable = countdisable;
		this.walletaddress = walletaddress == null || walletaddress.isEmpty() ? null : walletaddress;
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

	@Column(name = "countactive", nullable = false, precision = 15, scale = 5)
	public double getCountactive() {
		return this.countactive;
	}

	public void setCountactive(double countactive) {
		this.countactive = countactive;
	}

	@Column(name = "countdisable", nullable = false, precision = 15, scale = 5)
	public double getCountdisable() {
		return this.countdisable;
	}

	public void setCountdisable(double countdisable) {
		this.countdisable = countdisable;
	}

	@Column(name = "walletaddress")
	public String getWalletaddress() {
		return this.walletaddress;
	}

	public void setWalletaddress(String walletaddress) {
		this.walletaddress = walletaddress == null || walletaddress.isEmpty() ? null : walletaddress;
	}

}
