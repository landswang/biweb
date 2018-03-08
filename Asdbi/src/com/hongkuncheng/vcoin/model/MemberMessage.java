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
@Table(name = "member_message")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MemberMessage implements java.io.Serializable {

	private int id;
	private Member member;
	private String title;
	private String contents;
	private Date sendtime;
	private boolean readed;

	public MemberMessage() {
	}

	public MemberMessage(String title, Date sendtime, boolean readed) {
		this.title = title;
		this.sendtime = sendtime;
		this.readed = readed;
	}

	public MemberMessage(Member member, String title, String contents, Date sendtime, boolean readed) {
		this.member = member;
		this.title = title;
		this.contents = contents == null || contents.isEmpty() ? null : contents;
		this.sendtime = sendtime;
		this.readed = readed;
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
	@JoinColumn(name = "member_id")
	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@Column(name = "title", nullable = false)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "contents", length = 65535)
	public String getContents() {
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents == null || contents.isEmpty() ? null : contents;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sendtime", nullable = false, length = 19)
	public Date getSendtime() {
		return this.sendtime;
	}

	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}

	@Column(name = "readed", nullable = false)
	public boolean isReaded() {
		return this.readed;
	}

	public void setReaded(boolean readed) {
		this.readed = readed;
	}

}
