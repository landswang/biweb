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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "member")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Member implements java.io.Serializable {

	private int id;
	private String mobile;
	private String password;
	private String tradepassword;
	private String face;
	private String openId;
	private String name;
	private String idcard;
	private String idcardpicfront;
	private String idcardpicback;
	private String idcardpiconhand;
	private int idcardpiccheckId;
	private String idcardpiccheckdtcause;
	private Date regtime;
	private double balanceactive;
	private double balancedisable;
	private boolean enabled;
	private String sessionId;
	private int parentId;
	private List<Vcoinextractrecord> vcoinextractrecords = new ArrayList<Vcoinextractrecord>(0);
	private List<MemberMessage> memberMessages = new ArrayList<MemberMessage>(0);
	private List<Vcoinrecord> vcoinrecords = new ArrayList<Vcoinrecord>(0);
	private List<Rmbrecord> rmbrecords = new ArrayList<Rmbrecord>(0);
	private List<VoteReMember> voteReMembers = new ArrayList<VoteReMember>(0);
	private List<MemberVirtualcoin> memberVirtualcoins = new ArrayList<MemberVirtualcoin>(0);
	private List<Pushrecord> pushrecordsForSenderId = new ArrayList<Pushrecord>(0);
	private List<Awardrecord> awardrecords = new ArrayList<Awardrecord>(0);
	private List<Pushrecord> pushrecordsForReceiverId = new ArrayList<Pushrecord>(0);
	private List<Rmbputinrecord> rmbputinrecords = new ArrayList<Rmbputinrecord>(0);
	private List<Vcoinputinrecord> vcoinputinrecords = new ArrayList<Vcoinputinrecord>(0);
	private List<Orderform> orderforms = new ArrayList<Orderform>(0);
	private List<Traderecord> traderecordsForBuymemberId = new ArrayList<Traderecord>(0);
	private List<MemberChangemobile> memberChangemobiles = new ArrayList<MemberChangemobile>(0);
	private List<Repairorder> repairorders = new ArrayList<Repairorder>(0);
	private List<Traderecord> traderecordsForSellmemberId = new ArrayList<Traderecord>(0);
	private List<MemberLog> memberLogs = new ArrayList<MemberLog>(0);
	private List<Vcoinapply> vcoinapplies = new ArrayList<Vcoinapply>(0);
	private List<MemberIp> memberIps = new ArrayList<MemberIp>(0);
	private List<MemberBankcard> memberBankcards = new ArrayList<MemberBankcard>(0);
	private List<Rmbextractrecord> rmbextractrecords = new ArrayList<Rmbextractrecord>(0);

	public Member() {
	}

	public Member(String mobile, String password, int idcardpiccheckId, Date regtime, double balanceactive,
			double balancedisable, boolean enabled, int parentId) {
		this.mobile = mobile;
		this.password = password;
		this.idcardpiccheckId = idcardpiccheckId;
		this.regtime = regtime;
		this.balanceactive = balanceactive;
		this.balancedisable = balancedisable;
		this.enabled = enabled;
		this.parentId = parentId;
	}

	public Member(String mobile, String password, String tradepassword, String face, String openId, String name,
			String idcard, String idcardpicfront, String idcardpicback, String idcardpiconhand, int idcardpiccheckId,
			String idcardpiccheckdtcause, Date regtime, double balanceactive, double balancedisable,
			boolean enabled, String sessionId, int parentId, List<Vcoinextractrecord> vcoinextractrecords,
			List<MemberMessage> memberMessages, List<Vcoinrecord> vcoinrecords, List<Rmbrecord> rmbrecords,
			List<VoteReMember> voteReMembers, List<MemberVirtualcoin> memberVirtualcoins,
			List<Pushrecord> pushrecordsForSenderId, List<Awardrecord> awardrecords,
			List<Pushrecord> pushrecordsForReceiverId, List<Rmbputinrecord> rmbputinrecords,
			List<Vcoinputinrecord> vcoinputinrecords, List<Orderform> orderforms,
			List<Traderecord> traderecordsForBuymemberId, List<MemberChangemobile> memberChangemobiles,
			List<Repairorder> repairorders, List<Traderecord> traderecordsForSellmemberId, List<MemberLog> memberLogs,
			List<Vcoinapply> vcoinapplies, List<MemberIp> memberIps, List<MemberBankcard> memberBankcards,
			List<Rmbextractrecord> rmbextractrecords) {
		this.mobile = mobile;
		this.password = password;
		this.tradepassword = tradepassword == null || tradepassword.isEmpty() ? null : tradepassword;
		this.face = face == null || face.isEmpty() ? null : face;
		this.openId = openId == null || openId.isEmpty() ? null : openId;
		this.name = name == null || name.isEmpty() ? null : name;
		this.idcard = idcard == null || idcard.isEmpty() ? null : idcard;
		this.idcardpicfront = idcardpicfront == null || idcardpicfront.isEmpty() ? null : idcardpicfront;
		this.idcardpicback = idcardpicback == null || idcardpicback.isEmpty() ? null : idcardpicback;
		this.idcardpiconhand = idcardpiconhand == null || idcardpiconhand.isEmpty() ? null : idcardpiconhand;
		this.idcardpiccheckId = idcardpiccheckId;
		this.idcardpiccheckdtcause = idcardpiccheckdtcause == null || idcardpiccheckdtcause.isEmpty() ? null : idcardpiccheckdtcause;
		this.regtime = regtime;
		this.balanceactive = balanceactive;
		this.balancedisable = balancedisable;
		this.enabled = enabled;
		this.sessionId = sessionId == null || sessionId.isEmpty() ? null : sessionId;
		this.parentId = parentId;
		this.vcoinextractrecords = vcoinextractrecords;
		this.memberMessages = memberMessages;
		this.vcoinrecords = vcoinrecords;
		this.rmbrecords = rmbrecords;
		this.voteReMembers = voteReMembers;
		this.memberVirtualcoins = memberVirtualcoins;
		this.pushrecordsForSenderId = pushrecordsForSenderId;
		this.awardrecords = awardrecords;
		this.pushrecordsForReceiverId = pushrecordsForReceiverId;
		this.rmbputinrecords = rmbputinrecords;
		this.vcoinputinrecords = vcoinputinrecords;
		this.orderforms = orderforms;
		this.traderecordsForBuymemberId = traderecordsForBuymemberId;
		this.memberChangemobiles = memberChangemobiles;
		this.repairorders = repairorders;
		this.traderecordsForSellmemberId = traderecordsForSellmemberId;
		this.memberLogs = memberLogs;
		this.vcoinapplies = vcoinapplies;
		this.memberIps = memberIps;
		this.memberBankcards = memberBankcards;
		this.rmbextractrecords = rmbextractrecords;
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

	@Column(name = "mobile", nullable = false)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "tradepassword")
	public String getTradepassword() {
		return this.tradepassword;
	}

	public void setTradepassword(String tradepassword) {
		this.tradepassword = tradepassword == null || tradepassword.isEmpty() ? null : tradepassword;
	}

	@Column(name = "face")
	public String getFace() {
		return this.face;
	}

	public void setFace(String face) {
		this.face = face == null || face.isEmpty() ? null : face;
	}

	@Column(name = "open_id")
	public String getOpenId() {
		return this.openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId == null || openId.isEmpty() ? null : openId;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name == null || name.isEmpty() ? null : name;
	}

	@Column(name = "idcard")
	public String getIdcard() {
		return this.idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard == null || idcard.isEmpty() ? null : idcard;
	}

	@Column(name = "idcardpicfront")
	public String getIdcardpicfront() {
		return this.idcardpicfront;
	}

	public void setIdcardpicfront(String idcardpicfront) {
		this.idcardpicfront = idcardpicfront == null || idcardpicfront.isEmpty() ? null : idcardpicfront;
	}

	@Column(name = "idcardpicback")
	public String getIdcardpicback() {
		return this.idcardpicback;
	}

	public void setIdcardpicback(String idcardpicback) {
		this.idcardpicback = idcardpicback == null || idcardpicback.isEmpty() ? null : idcardpicback;
	}

	@Column(name = "idcardpiconhand")
	public String getIdcardpiconhand() {
		return this.idcardpiconhand;
	}

	public void setIdcardpiconhand(String idcardpiconhand) {
		this.idcardpiconhand = idcardpiconhand == null || idcardpiconhand.isEmpty() ? null : idcardpiconhand;
	}

	@Column(name = "idcardpiccheck_id", nullable = false)
	public int getIdcardpiccheckId() {
		return this.idcardpiccheckId;
	}

	public void setIdcardpiccheckId(int idcardpiccheckId) {
		this.idcardpiccheckId = idcardpiccheckId;
	}

	@Column(name = "idcardpiccheckdtcause")
	public String getIdcardpiccheckdtcause() {
		return this.idcardpiccheckdtcause;
	}

	public void setIdcardpiccheckdtcause(String idcardpiccheckdtcause) {
		this.idcardpiccheckdtcause = idcardpiccheckdtcause == null || idcardpiccheckdtcause.isEmpty() ? null : idcardpiccheckdtcause;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "regtime", nullable = false, length = 19)
	public Date getRegtime() {
		return this.regtime;
	}

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	@Column(name = "balanceactive", nullable = false, precision = 15)
	public double getBalanceactive() {
		return this.balanceactive;
	}

	public void setBalanceactive(double balanceactive) {
		this.balanceactive = balanceactive;
	}

	@Column(name = "balancedisable", nullable = false, precision = 15)
	public double getBalancedisable() {
		return this.balancedisable;
	}

	public void setBalancedisable(double balancedisable) {
		this.balancedisable = balancedisable;
	}

	@Column(name = "enabled", nullable = false)
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name = "session_id")
	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId == null || sessionId.isEmpty() ? null : sessionId;
	}

	@Column(name = "parent_id", nullable = false)
	public int getParentId() {
		return this.parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<Vcoinextractrecord> getVcoinextractrecords() {
		return this.vcoinextractrecords;
	}

	public void setVcoinextractrecords(List<Vcoinextractrecord> vcoinextractrecords) {
		this.vcoinextractrecords = vcoinextractrecords;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<MemberMessage> getMemberMessages() {
		return this.memberMessages;
	}

	public void setMemberMessages(List<MemberMessage> memberMessages) {
		this.memberMessages = memberMessages;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<Vcoinrecord> getVcoinrecords() {
		return this.vcoinrecords;
	}

	public void setVcoinrecords(List<Vcoinrecord> vcoinrecords) {
		this.vcoinrecords = vcoinrecords;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<Rmbrecord> getRmbrecords() {
		return this.rmbrecords;
	}

	public void setRmbrecords(List<Rmbrecord> rmbrecords) {
		this.rmbrecords = rmbrecords;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<VoteReMember> getVoteReMembers() {
		return this.voteReMembers;
	}

	public void setVoteReMembers(List<VoteReMember> voteReMembers) {
		this.voteReMembers = voteReMembers;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<MemberVirtualcoin> getMemberVirtualcoins() {
		return this.memberVirtualcoins;
	}

	public void setMemberVirtualcoins(List<MemberVirtualcoin> memberVirtualcoins) {
		this.memberVirtualcoins = memberVirtualcoins;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "memberBySenderId")
	public List<Pushrecord> getPushrecordsForSenderId() {
		return this.pushrecordsForSenderId;
	}

	public void setPushrecordsForSenderId(List<Pushrecord> pushrecordsForSenderId) {
		this.pushrecordsForSenderId = pushrecordsForSenderId;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<Awardrecord> getAwardrecords() {
		return this.awardrecords;
	}

	public void setAwardrecords(List<Awardrecord> awardrecords) {
		this.awardrecords = awardrecords;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "memberByReceiverId")
	public List<Pushrecord> getPushrecordsForReceiverId() {
		return this.pushrecordsForReceiverId;
	}

	public void setPushrecordsForReceiverId(List<Pushrecord> pushrecordsForReceiverId) {
		this.pushrecordsForReceiverId = pushrecordsForReceiverId;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<Rmbputinrecord> getRmbputinrecords() {
		return this.rmbputinrecords;
	}

	public void setRmbputinrecords(List<Rmbputinrecord> rmbputinrecords) {
		this.rmbputinrecords = rmbputinrecords;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<Vcoinputinrecord> getVcoinputinrecords() {
		return this.vcoinputinrecords;
	}

	public void setVcoinputinrecords(List<Vcoinputinrecord> vcoinputinrecords) {
		this.vcoinputinrecords = vcoinputinrecords;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<Orderform> getOrderforms() {
		return this.orderforms;
	}

	public void setOrderforms(List<Orderform> orderforms) {
		this.orderforms = orderforms;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "memberByBuymemberId")
	public List<Traderecord> getTraderecordsForBuymemberId() {
		return this.traderecordsForBuymemberId;
	}

	public void setTraderecordsForBuymemberId(List<Traderecord> traderecordsForBuymemberId) {
		this.traderecordsForBuymemberId = traderecordsForBuymemberId;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<MemberChangemobile> getMemberChangemobiles() {
		return this.memberChangemobiles;
	}

	public void setMemberChangemobiles(List<MemberChangemobile> memberChangemobiles) {
		this.memberChangemobiles = memberChangemobiles;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<Repairorder> getRepairorders() {
		return this.repairorders;
	}

	public void setRepairorders(List<Repairorder> repairorders) {
		this.repairorders = repairorders;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "memberBySellmemberId")
	public List<Traderecord> getTraderecordsForSellmemberId() {
		return this.traderecordsForSellmemberId;
	}

	public void setTraderecordsForSellmemberId(List<Traderecord> traderecordsForSellmemberId) {
		this.traderecordsForSellmemberId = traderecordsForSellmemberId;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<MemberLog> getMemberLogs() {
		return this.memberLogs;
	}

	public void setMemberLogs(List<MemberLog> memberLogs) {
		this.memberLogs = memberLogs;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<Vcoinapply> getVcoinapplies() {
		return this.vcoinapplies;
	}

	public void setVcoinapplies(List<Vcoinapply> vcoinapplies) {
		this.vcoinapplies = vcoinapplies;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<MemberIp> getMemberIps() {
		return this.memberIps;
	}

	public void setMemberIps(List<MemberIp> memberIps) {
		this.memberIps = memberIps;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<MemberBankcard> getMemberBankcards() {
		return this.memberBankcards;
	}

	public void setMemberBankcards(List<MemberBankcard> memberBankcards) {
		this.memberBankcards = memberBankcards;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public List<Rmbextractrecord> getRmbextractrecords() {
		return this.rmbextractrecords;
	}

	public void setRmbextractrecords(List<Rmbextractrecord> rmbextractrecords) {
		this.rmbextractrecords = rmbextractrecords;
	}

}
