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
@Table(name = "admin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Admin implements java.io.Serializable {

	private int id;
	private String account;
	private String password;
	private String mobile;
	private List<RepairorderReply> repairorderReplies = new ArrayList<RepairorderReply>(0);
	private List<AdminIp> adminIps = new ArrayList<AdminIp>(0);
	private List<Vcoinextractrecord> vcoinextractrecords = new ArrayList<Vcoinextractrecord>(0);
	private List<AdminLog> adminLogs = new ArrayList<AdminLog>(0);
	private List<Rmbextractrecord> rmbextractrecords = new ArrayList<Rmbextractrecord>(0);
	private List<Rmbputinrecord> rmbputinrecords = new ArrayList<Rmbputinrecord>(0);

	public Admin() {
	}

	public Admin(String account, String password, String mobile) {
		this.account = account;
		this.password = password;
		this.mobile = mobile;
	}

	public Admin(String account, String password, String mobile, List<RepairorderReply> repairorderReplies,
			List<AdminIp> adminIps, List<Vcoinextractrecord> vcoinextractrecords, List<AdminLog> adminLogs,
			List<Rmbextractrecord> rmbextractrecords, List<Rmbputinrecord> rmbputinrecords) {
		this.account = account;
		this.password = password;
		this.mobile = mobile;
		this.repairorderReplies = repairorderReplies;
		this.adminIps = adminIps;
		this.vcoinextractrecords = vcoinextractrecords;
		this.adminLogs = adminLogs;
		this.rmbextractrecords = rmbextractrecords;
		this.rmbputinrecords = rmbputinrecords;
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

	@Column(name = "account", nullable = false)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "mobile", nullable = false)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admin")
	public List<RepairorderReply> getRepairorderReplies() {
		return this.repairorderReplies;
	}

	public void setRepairorderReplies(List<RepairorderReply> repairorderReplies) {
		this.repairorderReplies = repairorderReplies;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admin")
	public List<AdminIp> getAdminIps() {
		return this.adminIps;
	}

	public void setAdminIps(List<AdminIp> adminIps) {
		this.adminIps = adminIps;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admin")
	public List<Vcoinextractrecord> getVcoinextractrecords() {
		return this.vcoinextractrecords;
	}

	public void setVcoinextractrecords(List<Vcoinextractrecord> vcoinextractrecords) {
		this.vcoinextractrecords = vcoinextractrecords;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admin")
	public List<AdminLog> getAdminLogs() {
		return this.adminLogs;
	}

	public void setAdminLogs(List<AdminLog> adminLogs) {
		this.adminLogs = adminLogs;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admin")
	public List<Rmbextractrecord> getRmbextractrecords() {
		return this.rmbextractrecords;
	}

	public void setRmbextractrecords(List<Rmbextractrecord> rmbextractrecords) {
		this.rmbextractrecords = rmbextractrecords;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admin")
	public List<Rmbputinrecord> getRmbputinrecords() {
		return this.rmbputinrecords;
	}

	public void setRmbputinrecords(List<Rmbputinrecord> rmbputinrecords) {
		this.rmbputinrecords = rmbputinrecords;
	}

}
