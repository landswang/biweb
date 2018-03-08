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
@Table(name = "admin_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdminLog implements java.io.Serializable {

	private int id;
	private Admin admin;
	private String operation;
	private Date operationtime;
	private String ipaddress;

	public AdminLog() {
	}

	public AdminLog(Admin admin, String operation, Date operationtime, String ipaddress) {
		this.admin = admin;
		this.operation = operation;
		this.operationtime = operationtime;
		this.ipaddress = ipaddress;
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
	@JoinColumn(name = "admin_id", nullable = false)
	public Admin getAdmin() {
		return this.admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	@Column(name = "operation", nullable = false, length = 4000)
	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "operationtime", nullable = false, length = 19)
	public Date getOperationtime() {
		return this.operationtime;
	}

	public void setOperationtime(Date operationtime) {
		this.operationtime = operationtime;
	}

	@Column(name = "ipaddress", nullable = false)
	public String getIpaddress() {
		return this.ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

}
