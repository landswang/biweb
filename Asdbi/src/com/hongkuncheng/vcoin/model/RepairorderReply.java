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
@Table(name = "repairorder_reply")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RepairorderReply implements java.io.Serializable {

	private int id;
	private Repairorder repairorder;
	private Admin admin;
	private String contents;
	private Date replytime;

	public RepairorderReply() {
	}

	public RepairorderReply(Repairorder repairorder, String contents, Date replytime) {
		this.repairorder = repairorder;
		this.contents = contents;
		this.replytime = replytime;
	}

	public RepairorderReply(Repairorder repairorder, Admin admin, String contents, Date replytime) {
		this.repairorder = repairorder;
		this.admin = admin;
		this.contents = contents;
		this.replytime = replytime;
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
	@JoinColumn(name = "repairorder_id", nullable = false)
	public Repairorder getRepairorder() {
		return this.repairorder;
	}

	public void setRepairorder(Repairorder repairorder) {
		this.repairorder = repairorder;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id")
	public Admin getAdmin() {
		return this.admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	@Column(name = "contents", nullable = false, length = 65535)
	public String getContents() {
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "replytime", nullable = false, length = 19)
	public Date getReplytime() {
		return this.replytime;
	}

	public void setReplytime(Date replytime) {
		this.replytime = replytime;
	}

}
