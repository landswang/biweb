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
@Table(name = "awardrecord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Awardrecord implements java.io.Serializable {

	private int id;
	private Member member;
	private double count;
	private Date awardtime;

	public Awardrecord() {
	}

	public Awardrecord(Member member, double count, Date awardtime) {
		this.member = member;
		this.count = count;
		this.awardtime = awardtime;
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

	@Column(name = "count", nullable = false, precision = 15, scale = 5)
	public double getCount() {
		return this.count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "awardtime", nullable = false, length = 19)
	public Date getAwardtime() {
		return this.awardtime;
	}

	public void setAwardtime(Date awardtime) {
		this.awardtime = awardtime;
	}

}
