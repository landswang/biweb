package com.hongkuncheng.vcoin.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "repairorder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Repairorder implements java.io.Serializable {

	private int id;
	private Member member;
	private RepairorderState repairorderState;
	private String title;
	private Date refreshtime;
	private List<RepairorderReply> repairorderReplies = new ArrayList<RepairorderReply>(0);

	public Repairorder() {
	}

	public Repairorder(Member member, RepairorderState repairorderState, String title, Date refreshtime) {
		this.member = member;
		this.repairorderState = repairorderState;
		this.title = title;
		this.refreshtime = refreshtime;
	}

	public Repairorder(Member member, RepairorderState repairorderState, String title, Date refreshtime,
			List<RepairorderReply> repairorderReplies) {
		this.member = member;
		this.repairorderState = repairorderState;
		this.title = title;
		this.refreshtime = refreshtime;
		this.repairorderReplies = repairorderReplies;
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
	public RepairorderState getRepairorderState() {
		return this.repairorderState;
	}

	public void setRepairorderState(RepairorderState repairorderState) {
		this.repairorderState = repairorderState;
	}

	@Column(name = "title", nullable = false)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "refreshtime", nullable = false, length = 19)
	public Date getRefreshtime() {
		return this.refreshtime;
	}

	public void setRefreshtime(Date refreshtime) {
		this.refreshtime = refreshtime;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "repairorder")
	public List<RepairorderReply> getRepairorderReplies() {
		return this.repairorderReplies;
	}

	public void setRepairorderReplies(List<RepairorderReply> repairorderReplies) {
		this.repairorderReplies = repairorderReplies;
	}

}
