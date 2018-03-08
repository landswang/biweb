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
@Table(name = "member_bankcard")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MemberBankcard implements java.io.Serializable {

	private int id;
	private Member member;
	private String bankname;
	private String number;
	private String addprovince;
	private String addcity;
	private String branch;

	public MemberBankcard() {
	}

	public MemberBankcard(Member member, String bankname, String number, String addprovince, String addcity) {
		this.member = member;
		this.bankname = bankname;
		this.number = number;
		this.addprovince = addprovince;
		this.addcity = addcity;
	}

	public MemberBankcard(Member member, String bankname, String number, String addprovince, String addcity,
			String branch) {
		this.member = member;
		this.bankname = bankname;
		this.number = number;
		this.addprovince = addprovince;
		this.addcity = addcity;
		this.branch = branch == null || branch.isEmpty() ? null : branch;
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
	@JoinColumn(name = "member_id", nullable = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@Column(name = "bankname", nullable = false)
	public String getBankname() {
		return this.bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	@Column(name = "number", nullable = false)
	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Column(name = "addprovince", nullable = false)
	public String getAddprovince() {
		return this.addprovince;
	}

	public void setAddprovince(String addprovince) {
		this.addprovince = addprovince;
	}

	@Column(name = "addcity", nullable = false)
	public String getAddcity() {
		return this.addcity;
	}

	public void setAddcity(String addcity) {
		this.addcity = addcity;
	}

	@Column(name = "branch")
	public String getBranch() {
		return this.branch;
	}

	public void setBranch(String branch) {
		this.branch = branch == null || branch.isEmpty() ? null : branch;
	}

}
