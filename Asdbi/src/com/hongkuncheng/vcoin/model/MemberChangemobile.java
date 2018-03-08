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
@Table(name = "member_changemobile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MemberChangemobile implements java.io.Serializable {

	private int id;
	private Member member;
	private MemberChangemobileState memberChangemobileState;
	private String mobile;
	private String chagemobilepic;
	private Date submittime;
	private String checkdtcause;

	public MemberChangemobile() {
	}

	public MemberChangemobile(Member member, MemberChangemobileState memberChangemobileState, String mobile,
			String chagemobilepic, Date submittime) {
		this.member = member;
		this.memberChangemobileState = memberChangemobileState;
		this.mobile = mobile;
		this.chagemobilepic = chagemobilepic;
		this.submittime = submittime;
	}

	public MemberChangemobile(Member member, MemberChangemobileState memberChangemobileState, String mobile,
			String chagemobilepic, Date submittime, String checkdtcause) {
		this.member = member;
		this.memberChangemobileState = memberChangemobileState;
		this.mobile = mobile;
		this.chagemobilepic = chagemobilepic;
		this.submittime = submittime;
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
	public MemberChangemobileState getMemberChangemobileState() {
		return this.memberChangemobileState;
	}

	public void setMemberChangemobileState(MemberChangemobileState memberChangemobileState) {
		this.memberChangemobileState = memberChangemobileState;
	}

	@Column(name = "mobile", nullable = false)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "chagemobilepic", nullable = false)
	public String getChagemobilepic() {
		return this.chagemobilepic;
	}

	public void setChagemobilepic(String chagemobilepic) {
		this.chagemobilepic = chagemobilepic;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "submittime", nullable = false, length = 19)
	public Date getSubmittime() {
		return this.submittime;
	}

	public void setSubmittime(Date submittime) {
		this.submittime = submittime;
	}

	@Column(name = "checkdtcause")
	public String getCheckdtcause() {
		return this.checkdtcause;
	}

	public void setCheckdtcause(String checkdtcause) {
		this.checkdtcause = checkdtcause == null || checkdtcause.isEmpty() ? null : checkdtcause;
	}

}
