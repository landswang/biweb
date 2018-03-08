package com.hongkuncheng.vcoin.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "vote")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vote implements java.io.Serializable {

	private int id;
	private String cname;
	private String ename;
	private int approvecount;
	private int opposecount;
	private boolean puton;
	private boolean voting;
	private List<VoteReMember> voteReMembers = new ArrayList<VoteReMember>(0);

	public Vote() {
	}

	public Vote(String cname, String ename, int approvecount, int opposecount, boolean puton, boolean voting) {
		this.cname = cname;
		this.ename = ename;
		this.approvecount = approvecount;
		this.opposecount = opposecount;
		this.puton = puton;
		this.voting = voting;
	}

	public Vote(String cname, String ename, int approvecount, int opposecount, boolean puton, boolean voting,
			List<VoteReMember> voteReMembers) {
		this.cname = cname;
		this.ename = ename;
		this.approvecount = approvecount;
		this.opposecount = opposecount;
		this.puton = puton;
		this.voting = voting;
		this.voteReMembers = voteReMembers;
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

	@Column(name = "cname", nullable = false)
	public String getCname() {
		return this.cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	@Column(name = "ename", nullable = false)
	public String getEname() {
		return this.ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	@Column(name = "approvecount", nullable = false)
	public int getApprovecount() {
		return this.approvecount;
	}

	public void setApprovecount(int approvecount) {
		this.approvecount = approvecount;
	}

	@Column(name = "opposecount", nullable = false)
	public int getOpposecount() {
		return this.opposecount;
	}

	public void setOpposecount(int opposecount) {
		this.opposecount = opposecount;
	}

	@Column(name = "puton", nullable = false)
	public boolean isPuton() {
		return this.puton;
	}

	public void setPuton(boolean puton) {
		this.puton = puton;
	}

	@Column(name = "voting", nullable = false)
	public boolean isVoting() {
		return this.voting;
	}

	public void setVoting(boolean voting) {
		this.voting = voting;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vote")
	public List<VoteReMember> getVoteReMembers() {
		return this.voteReMembers;
	}

	public void setVoteReMembers(List<VoteReMember> voteReMembers) {
		this.voteReMembers = voteReMembers;
	}

}
