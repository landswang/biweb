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
@Table(name = "vcoinapply")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vcoinapply implements java.io.Serializable {

	private int id;
	private Member member;
	private VcoinapplyState vcoinapplyState;
	private String vcoinname;
	private String teamname;
	private String telphone;
	private String description;
	private Date applytime;
	private String checkdtcause;

	public Vcoinapply() {
	}

	public Vcoinapply(Member member, VcoinapplyState vcoinapplyState, String vcoinname, String teamname,
			String telphone, Date applytime) {
		this.member = member;
		this.vcoinapplyState = vcoinapplyState;
		this.vcoinname = vcoinname;
		this.teamname = teamname;
		this.telphone = telphone;
		this.applytime = applytime;
	}

	public Vcoinapply(Member member, VcoinapplyState vcoinapplyState, String vcoinname, String teamname,
			String telphone, String description, Date applytime, String checkdtcause) {
		this.member = member;
		this.vcoinapplyState = vcoinapplyState;
		this.vcoinname = vcoinname;
		this.teamname = teamname;
		this.telphone = telphone;
		this.description = description == null || description.isEmpty() ? null : description;
		this.applytime = applytime;
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
	@JoinColumn(name = "member_id", nullable = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "state_id", nullable = false)
	public VcoinapplyState getVcoinapplyState() {
		return this.vcoinapplyState;
	}

	public void setVcoinapplyState(VcoinapplyState vcoinapplyState) {
		this.vcoinapplyState = vcoinapplyState;
	}

	@Column(name = "vcoinname", nullable = false)
	public String getVcoinname() {
		return this.vcoinname;
	}

	public void setVcoinname(String vcoinname) {
		this.vcoinname = vcoinname;
	}

	@Column(name = "teamname", nullable = false)
	public String getTeamname() {
		return this.teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	@Column(name = "telphone", nullable = false)
	public String getTelphone() {
		return this.telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	@Column(name = "description", length = 65535)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description == null || description.isEmpty() ? null : description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "applytime", nullable = false, length = 19)
	public Date getApplytime() {
		return this.applytime;
	}

	public void setApplytime(Date applytime) {
		this.applytime = applytime;
	}

	@Column(name = "checkdtcause")
	public String getCheckdtcause() {
		return this.checkdtcause;
	}

	public void setCheckdtcause(String checkdtcause) {
		this.checkdtcause = checkdtcause == null || checkdtcause.isEmpty() ? null : checkdtcause;
	}

}
