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
@Table(name = "vote_re_member")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VoteReMember implements java.io.Serializable {

	private int id;
	private Member member;
	private Vote vote;
	private boolean approve;
	private Date votetime;

	public VoteReMember() {
	}

	public VoteReMember(Member member, Vote vote, boolean approve, Date votetime) {
		this.member = member;
		this.vote = vote;
		this.approve = approve;
		this.votetime = votetime;
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
	@JoinColumn(name = "vote_id", nullable = false)
	public Vote getVote() {
		return this.vote;
	}

	public void setVote(Vote vote) {
		this.vote = vote;
	}

	@Column(name = "approve", nullable = false)
	public boolean isApprove() {
		return this.approve;
	}

	public void setApprove(boolean approve) {
		this.approve = approve;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "votetime", nullable = false, length = 19)
	public Date getVotetime() {
		return this.votetime;
	}

	public void setVotetime(Date votetime) {
		this.votetime = votetime;
	}

}
