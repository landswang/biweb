package com.hongkuncheng.vcoin.model;

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

@Entity
@Table(name = "virtualcoin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Virtualcoin implements java.io.Serializable {

	private int id;
	private VirtualcoinType virtualcoinType;
	private String cname;
	private String eunit;
	private String rpcip;
	private int rpcport;
	private String rpcuser;
	private String rpcpassword;
	private String walletaddress;
	private String addressregexp;
	private String contractadres;
	private Integer contractpow;
	private String contractbalanceof;
	private String contracttransfermethod;
	private Integer contractreceiveadresindex;
	private Integer contractamoutindex;
	private String image;
	private String description;
	private double price;
	private int priceinputdecimalsize;
	private int countinputdecimalsize;
	private double countfloor;
	private double vcoinrate;
	private double moneyrate;
	private int moneydecimalsize;
	private double putinfloor;
	private double putinrate;
	private String putinexplain;
	private double extractupper;
	private double extractfloor;
	private double extractrate;
	private double extractfeefloor;
	private int extractdecimalsize;
	private String extractexplain;
	private String txidquerybrowser;
	private int sort;
	private double outcount;
	private boolean enabled;
	private boolean trading;
	private boolean canputin;
	private boolean canextract;
	private List<Vcoinputinrecord> vcoinputinrecords = new ArrayList<Vcoinputinrecord>(0);
	private List<Orderform> orderforms = new ArrayList<Orderform>(0);
	private List<Vcoinextractrecord> vcoinextractrecords = new ArrayList<Vcoinextractrecord>(0);
	private List<Traderecord> traderecords = new ArrayList<Traderecord>(0);
	private List<VirtualcoinBlocks> virtualcoinBlockses = new ArrayList<VirtualcoinBlocks>(0);
	private List<MemberVirtualcoin> memberVirtualcoins = new ArrayList<MemberVirtualcoin>(0);
	private List<Vcoinrecord> vcoinrecords = new ArrayList<Vcoinrecord>(0);
	private List<Pushrecord> pushrecords = new ArrayList<Pushrecord>(0);

	public Virtualcoin() {
	}

	public Virtualcoin(VirtualcoinType virtualcoinType, String cname, String eunit, String rpcip, int rpcport,
			String rpcuser, String rpcpassword, String image, double price, int priceinputdecimalsize,
			int countinputdecimalsize, double countfloor, double vcoinrate, double moneyrate,
			int moneydecimalsize, double putinfloor, double putinrate, double extractupper,
			double extractfloor, double extractrate, double extractfeefloor, int extractdecimalsize,
			int sort, double outcount, boolean enabled, boolean trading, boolean canputin, boolean canextract) {
		this.virtualcoinType = virtualcoinType;
		this.cname = cname;
		this.eunit = eunit;
		this.rpcip = rpcip;
		this.rpcport = rpcport;
		this.rpcuser = rpcuser;
		this.rpcpassword = rpcpassword;
		this.image = image;
		this.price = price;
		this.priceinputdecimalsize = priceinputdecimalsize;
		this.countinputdecimalsize = countinputdecimalsize;
		this.countfloor = countfloor;
		this.vcoinrate = vcoinrate;
		this.moneyrate = moneyrate;
		this.moneydecimalsize = moneydecimalsize;
		this.putinfloor = putinfloor;
		this.putinrate = putinrate;
		this.extractupper = extractupper;
		this.extractfloor = extractfloor;
		this.extractrate = extractrate;
		this.extractfeefloor = extractfeefloor;
		this.extractdecimalsize = extractdecimalsize;
		this.sort = sort;
		this.outcount = outcount;
		this.enabled = enabled;
		this.trading = trading;
		this.canputin = canputin;
		this.canextract = canextract;
	}

	public Virtualcoin(VirtualcoinType virtualcoinType, String cname, String eunit, String rpcip, int rpcport,
			String rpcuser, String rpcpassword, String walletaddress, String addressregexp, String contractadres,
			Integer contractpow, String contractbalanceof, String contracttransfermethod,
			Integer contractreceiveadresindex, Integer contractamoutindex, String image, String description,
			double price, int priceinputdecimalsize, int countinputdecimalsize, double countfloor,
			double vcoinrate, double moneyrate, int moneydecimalsize, double putinfloor,
			double putinrate, String putinexplain, double extractupper, double extractfloor,
			double extractrate, double extractfeefloor, int extractdecimalsize, String extractexplain,
			String txidquerybrowser, int sort, double outcount, boolean enabled, boolean trading, boolean canputin,
			boolean canextract, List<Vcoinputinrecord> vcoinputinrecords, List<Orderform> orderforms,
			List<Vcoinextractrecord> vcoinextractrecords, List<Traderecord> traderecords,
			List<VirtualcoinBlocks> virtualcoinBlockses, List<MemberVirtualcoin> memberVirtualcoins,
			List<Vcoinrecord> vcoinrecords, List<Pushrecord> pushrecords) {
		this.virtualcoinType = virtualcoinType;
		this.cname = cname;
		this.eunit = eunit;
		this.rpcip = rpcip;
		this.rpcport = rpcport;
		this.rpcuser = rpcuser;
		this.rpcpassword = rpcpassword;
		this.walletaddress = walletaddress == null || walletaddress.isEmpty() ? null : walletaddress;
		this.addressregexp = addressregexp == null || addressregexp.isEmpty() ? null : addressregexp;
		this.contractadres = contractadres == null || contractadres.isEmpty() ? null : contractadres;
		this.contractpow = contractpow;
		this.contractbalanceof = contractbalanceof == null || contractbalanceof.isEmpty() ? null : contractbalanceof;
		this.contracttransfermethod = contracttransfermethod == null || contracttransfermethod.isEmpty() ? null : contracttransfermethod;
		this.contractreceiveadresindex = contractreceiveadresindex;
		this.contractamoutindex = contractamoutindex;
		this.image = image;
		this.description = description == null || description.isEmpty() ? null : description;
		this.price = price;
		this.priceinputdecimalsize = priceinputdecimalsize;
		this.countinputdecimalsize = countinputdecimalsize;
		this.countfloor = countfloor;
		this.vcoinrate = vcoinrate;
		this.moneyrate = moneyrate;
		this.moneydecimalsize = moneydecimalsize;
		this.putinfloor = putinfloor;
		this.putinrate = putinrate;
		this.putinexplain = putinexplain == null || putinexplain.isEmpty() ? null : putinexplain;
		this.extractupper = extractupper;
		this.extractfloor = extractfloor;
		this.extractrate = extractrate;
		this.extractfeefloor = extractfeefloor;
		this.extractdecimalsize = extractdecimalsize;
		this.extractexplain = extractexplain == null || extractexplain.isEmpty() ? null : extractexplain;
		this.txidquerybrowser = txidquerybrowser == null || txidquerybrowser.isEmpty() ? null : txidquerybrowser;
		this.sort = sort;
		this.outcount = outcount;
		this.enabled = enabled;
		this.trading = trading;
		this.canputin = canputin;
		this.canextract = canextract;
		this.vcoinputinrecords = vcoinputinrecords;
		this.orderforms = orderforms;
		this.vcoinextractrecords = vcoinextractrecords;
		this.traderecords = traderecords;
		this.virtualcoinBlockses = virtualcoinBlockses;
		this.memberVirtualcoins = memberVirtualcoins;
		this.vcoinrecords = vcoinrecords;
		this.pushrecords = pushrecords;
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
	@JoinColumn(name = "type_id", nullable = false)
	public VirtualcoinType getVirtualcoinType() {
		return this.virtualcoinType;
	}

	public void setVirtualcoinType(VirtualcoinType virtualcoinType) {
		this.virtualcoinType = virtualcoinType;
	}

	@Column(name = "cname", nullable = false)
	public String getCname() {
		return this.cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	@Column(name = "eunit", nullable = false)
	public String getEunit() {
		return this.eunit;
	}

	public void setEunit(String eunit) {
		this.eunit = eunit;
	}

	@Column(name = "rpcip", nullable = false)
	public String getRpcip() {
		return this.rpcip;
	}

	public void setRpcip(String rpcip) {
		this.rpcip = rpcip;
	}

	@Column(name = "rpcport", nullable = false)
	public int getRpcport() {
		return this.rpcport;
	}

	public void setRpcport(int rpcport) {
		this.rpcport = rpcport;
	}

	@Column(name = "rpcuser", nullable = false)
	public String getRpcuser() {
		return this.rpcuser;
	}

	public void setRpcuser(String rpcuser) {
		this.rpcuser = rpcuser;
	}

	@Column(name = "rpcpassword", nullable = false)
	public String getRpcpassword() {
		return this.rpcpassword;
	}

	public void setRpcpassword(String rpcpassword) {
		this.rpcpassword = rpcpassword;
	}

	@Column(name = "walletaddress")
	public String getWalletaddress() {
		return this.walletaddress;
	}

	public void setWalletaddress(String walletaddress) {
		this.walletaddress = walletaddress == null || walletaddress.isEmpty() ? null : walletaddress;
	}

	@Column(name = "addressregexp")
	public String getAddressregexp() {
		return this.addressregexp;
	}

	public void setAddressregexp(String addressregexp) {
		this.addressregexp = addressregexp == null || addressregexp.isEmpty() ? null : addressregexp;
	}

	@Column(name = "contractadres")
	public String getContractadres() {
		return this.contractadres;
	}

	public void setContractadres(String contractadres) {
		this.contractadres = contractadres == null || contractadres.isEmpty() ? null : contractadres;
	}

	@Column(name = "contractpow")
	public Integer getContractpow() {
		return this.contractpow;
	}

	public void setContractpow(Integer contractpow) {
		this.contractpow = contractpow;
	}

	@Column(name = "contractbalanceof")
	public String getContractbalanceof() {
		return this.contractbalanceof;
	}

	public void setContractbalanceof(String contractbalanceof) {
		this.contractbalanceof = contractbalanceof == null || contractbalanceof.isEmpty() ? null : contractbalanceof;
	}

	@Column(name = "contracttransfermethod")
	public String getContracttransfermethod() {
		return this.contracttransfermethod;
	}

	public void setContracttransfermethod(String contracttransfermethod) {
		this.contracttransfermethod = contracttransfermethod == null || contracttransfermethod.isEmpty() ? null : contracttransfermethod;
	}

	@Column(name = "contractreceiveadresindex")
	public Integer getContractreceiveadresindex() {
		return this.contractreceiveadresindex;
	}

	public void setContractreceiveadresindex(Integer contractreceiveadresindex) {
		this.contractreceiveadresindex = contractreceiveadresindex;
	}

	@Column(name = "contractamoutindex")
	public Integer getContractamoutindex() {
		return this.contractamoutindex;
	}

	public void setContractamoutindex(Integer contractamoutindex) {
		this.contractamoutindex = contractamoutindex;
	}

	@Column(name = "image", nullable = false)
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name = "description", length = 65535)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description == null || description.isEmpty() ? null : description;
	}

	@Column(name = "price", nullable = false, precision = 15, scale = 5)
	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Column(name = "priceinputdecimalsize", nullable = false)
	public int getPriceinputdecimalsize() {
		return this.priceinputdecimalsize;
	}

	public void setPriceinputdecimalsize(int priceinputdecimalsize) {
		this.priceinputdecimalsize = priceinputdecimalsize;
	}

	@Column(name = "countinputdecimalsize", nullable = false)
	public int getCountinputdecimalsize() {
		return this.countinputdecimalsize;
	}

	public void setCountinputdecimalsize(int countinputdecimalsize) {
		this.countinputdecimalsize = countinputdecimalsize;
	}

	@Column(name = "countfloor", nullable = false, precision = 15, scale = 5)
	public double getCountfloor() {
		return this.countfloor;
	}

	public void setCountfloor(double countfloor) {
		this.countfloor = countfloor;
	}

	@Column(name = "vcoinrate", nullable = false, precision = 15, scale = 5)
	public double getVcoinrate() {
		return this.vcoinrate;
	}

	public void setVcoinrate(double vcoinrate) {
		this.vcoinrate = vcoinrate;
	}

	@Column(name = "moneyrate", nullable = false, precision = 15, scale = 5)
	public double getMoneyrate() {
		return this.moneyrate;
	}

	public void setMoneyrate(double moneyrate) {
		this.moneyrate = moneyrate;
	}

	@Column(name = "moneydecimalsize", nullable = false)
	public int getMoneydecimalsize() {
		return this.moneydecimalsize;
	}

	public void setMoneydecimalsize(int moneydecimalsize) {
		this.moneydecimalsize = moneydecimalsize;
	}

	@Column(name = "putinfloor", nullable = false, precision = 15, scale = 5)
	public double getPutinfloor() {
		return this.putinfloor;
	}

	public void setPutinfloor(double putinfloor) {
		this.putinfloor = putinfloor;
	}

	@Column(name = "putinrate", nullable = false, precision = 15, scale = 5)
	public double getPutinrate() {
		return this.putinrate;
	}

	public void setPutinrate(double putinrate) {
		this.putinrate = putinrate;
	}

	@Column(name = "putinexplain", length = 2000)
	public String getPutinexplain() {
		return this.putinexplain;
	}

	public void setPutinexplain(String putinexplain) {
		this.putinexplain = putinexplain == null || putinexplain.isEmpty() ? null : putinexplain;
	}

	@Column(name = "extractupper", nullable = false, precision = 15, scale = 5)
	public double getExtractupper() {
		return this.extractupper;
	}

	public void setExtractupper(double extractupper) {
		this.extractupper = extractupper;
	}

	@Column(name = "extractfloor", nullable = false, precision = 15, scale = 5)
	public double getExtractfloor() {
		return this.extractfloor;
	}

	public void setExtractfloor(double extractfloor) {
		this.extractfloor = extractfloor;
	}

	@Column(name = "extractrate", nullable = false, precision = 15, scale = 5)
	public double getExtractrate() {
		return this.extractrate;
	}

	public void setExtractrate(double extractrate) {
		this.extractrate = extractrate;
	}

	@Column(name = "extractfeefloor", nullable = false, precision = 15, scale = 5)
	public double getExtractfeefloor() {
		return this.extractfeefloor;
	}

	public void setExtractfeefloor(double extractfeefloor) {
		this.extractfeefloor = extractfeefloor;
	}

	@Column(name = "extractdecimalsize", nullable = false)
	public int getExtractdecimalsize() {
		return this.extractdecimalsize;
	}

	public void setExtractdecimalsize(int extractdecimalsize) {
		this.extractdecimalsize = extractdecimalsize;
	}

	@Column(name = "extractexplain", length = 2000)
	public String getExtractexplain() {
		return this.extractexplain;
	}

	public void setExtractexplain(String extractexplain) {
		this.extractexplain = extractexplain == null || extractexplain.isEmpty() ? null : extractexplain;
	}

	@Column(name = "txidquerybrowser", length = 2000)
	public String getTxidquerybrowser() {
		return this.txidquerybrowser;
	}

	public void setTxidquerybrowser(String txidquerybrowser) {
		this.txidquerybrowser = txidquerybrowser == null || txidquerybrowser.isEmpty() ? null : txidquerybrowser;
	}

	@Column(name = "sort", nullable = false)
	public int getSort() {
		return this.sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@Column(name = "outcount", nullable = false, precision = 15, scale = 5)
	public double getOutcount() {
		return this.outcount;
	}

	public void setOutcount(double outcount) {
		this.outcount = outcount;
	}

	@Column(name = "enabled", nullable = false)
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name = "trading", nullable = false)
	public boolean isTrading() {
		return this.trading;
	}

	public void setTrading(boolean trading) {
		this.trading = trading;
	}

	@Column(name = "canputin", nullable = false)
	public boolean isCanputin() {
		return this.canputin;
	}

	public void setCanputin(boolean canputin) {
		this.canputin = canputin;
	}

	@Column(name = "canextract", nullable = false)
	public boolean isCanextract() {
		return this.canextract;
	}

	public void setCanextract(boolean canextract) {
		this.canextract = canextract;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "virtualcoin")
	public List<Vcoinputinrecord> getVcoinputinrecords() {
		return this.vcoinputinrecords;
	}

	public void setVcoinputinrecords(List<Vcoinputinrecord> vcoinputinrecords) {
		this.vcoinputinrecords = vcoinputinrecords;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "virtualcoin")
	public List<Orderform> getOrderforms() {
		return this.orderforms;
	}

	public void setOrderforms(List<Orderform> orderforms) {
		this.orderforms = orderforms;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "virtualcoin")
	public List<Vcoinextractrecord> getVcoinextractrecords() {
		return this.vcoinextractrecords;
	}

	public void setVcoinextractrecords(List<Vcoinextractrecord> vcoinextractrecords) {
		this.vcoinextractrecords = vcoinextractrecords;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "virtualcoin")
	public List<Traderecord> getTraderecords() {
		return this.traderecords;
	}

	public void setTraderecords(List<Traderecord> traderecords) {
		this.traderecords = traderecords;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "virtualcoin")
	public List<VirtualcoinBlocks> getVirtualcoinBlockses() {
		return this.virtualcoinBlockses;
	}

	public void setVirtualcoinBlockses(List<VirtualcoinBlocks> virtualcoinBlockses) {
		this.virtualcoinBlockses = virtualcoinBlockses;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "virtualcoin")
	public List<MemberVirtualcoin> getMemberVirtualcoins() {
		return this.memberVirtualcoins;
	}

	public void setMemberVirtualcoins(List<MemberVirtualcoin> memberVirtualcoins) {
		this.memberVirtualcoins = memberVirtualcoins;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "virtualcoin")
	public List<Vcoinrecord> getVcoinrecords() {
		return this.vcoinrecords;
	}

	public void setVcoinrecords(List<Vcoinrecord> vcoinrecords) {
		this.vcoinrecords = vcoinrecords;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "virtualcoin")
	public List<Pushrecord> getPushrecords() {
		return this.pushrecords;
	}

	public void setPushrecords(List<Pushrecord> pushrecords) {
		this.pushrecords = pushrecords;
	}

}
