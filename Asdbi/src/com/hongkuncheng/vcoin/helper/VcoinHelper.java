package com.hongkuncheng.vcoin.helper;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Resource;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import com.hongkuncheng.vcoin.model.Member;
import com.hongkuncheng.vcoin.model.MemberVirtualcoin;
import com.hongkuncheng.vcoin.model.Vcoinextractrecord;
import com.hongkuncheng.vcoin.model.Virtualcoin;
import com.hongkuncheng.vcoin.model.VirtualcoinGas;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * 虚拟币帮助类
 * @author 洪坤成
 *
 */
@Component
public class VcoinHelper {

	private static SessionFactory factory;
	
	@Resource
	public void setFactory(SessionFactory factory) {
		this.factory = factory;
	}

	public static int ethId = 27;   //以太坊ID
	public static BigDecimal powEighteen = new BigDecimal(String.valueOf(Math.pow(10, 18)));  //以太次方
	
	/**
	 * 创建钱包地址
	 * @param membervirtualcoin
	 */
	public static void createAddress(MemberVirtualcoin membervirtualcoin) {
		Member member = membervirtualcoin.getMember();
		Virtualcoin virtualcoin = membervirtualcoin.getVirtualcoin();
		if (virtualcoin.getRpcport() > 0 && virtualcoin.isEnabled()) {
			switch (virtualcoin.getVirtualcoinType().getId()) {
				case 1:  //比特版
					Map<String, String> headers = new HashMap<String, String>();
					headers.put("Authorization", "Basic " + new String(Base64.encode(new String(virtualcoin.getRpcuser() + ":" + StringHelper.md5(virtualcoin.getRpcpassword())).getBytes())));
					try {
						String json = HttpHelper.post("http://" + virtualcoin.getRpcip() + ":" + virtualcoin.getRpcport(), headers, "{\"jsonrpc\":\"2.0\",\"method\":\"getaccountaddress\",\"params\":[\"" + member.getId() + "\"],\"id\":" + virtualcoin.getId() + "}");
						JSONObject jsonobj = new JSONObject(json);
						membervirtualcoin.setWalletaddress(jsonobj.getString("result"));
					} catch (Exception e) {
						System.out.println("Exception：" + virtualcoin.getCname() + "(" + virtualcoin.getEunit() + ")" + "createAddress连接失败。");
						e.printStackTrace();
					}
					break;
				case 2:  //以太版
					Object address = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).createAlias("virtualcoin", "virtualcoin").createAlias("virtualcoin.virtualcoinType", "virtualcoinType").add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("virtualcoinType.id", 2)).add(Restrictions.isNotNull("walletaddress")).setProjection(Projections.property("walletaddress")).setMaxResults(1).uniqueResult();
					if (address != null) {
						membervirtualcoin.setWalletaddress(address.toString());
					} else {
						if (virtualcoin.getContractadres() == null) {
							//主链
							try {
								String json = HttpHelper.post("http://" + virtualcoin.getRpcip() + ":" + virtualcoin.getRpcport(), "{\"jsonrpc\":\"2.0\",\"method\":\"personal_newAccount\",\"params\":[\"" + StringHelper.md5(ConfigHelper.getValue("app.project.name") + member.getId()) + "\"],\"id\":" + virtualcoin.getId() + "}");
								JSONObject jsonobj = new JSONObject(json);
								membervirtualcoin.setWalletaddress(jsonobj.getString("result"));
							} catch (Exception e) {
								System.out.println("Exception：" + virtualcoin.getCname() + "(" + virtualcoin.getEunit() + ")" + "createAddress连接失败。");
								e.printStackTrace();
							}
						} else {
							//合约
							try {
								String json = HttpHelper.post("http://" + virtualcoin.getRpcip() + ":" + virtualcoin.getRpcport(), "{\"jsonrpc\":\"2.0\",\"method\":\"personal_newAccount\",\"params\":[\"" + StringHelper.md5(ConfigHelper.getValue("app.project.name") + member.getId()) + "\"],\"id\":" + virtualcoin.getId() + "}");
								JSONObject jsonobj = new JSONObject(json);
								membervirtualcoin.setWalletaddress(jsonobj.getString("result"));
								Object mainmvethObjId = factory.getCurrentSession().createCriteria(MemberVirtualcoin.class).add(Restrictions.eq("member.id", member.getId())).add(Restrictions.eq("virtualcoin.id", ethId)).setProjection(Projections.property("id")).setMaxResults(1).setCacheable(true).uniqueResult();
								if (mainmvethObjId == null) {
									//创建主链地址
									Virtualcoin vceth = (Virtualcoin) factory.getCurrentSession().load(Virtualcoin.class, ethId);
									MemberVirtualcoin mainmv = new MemberVirtualcoin(vceth, member, 0, 0, membervirtualcoin.getWalletaddress());
									factory.getCurrentSession().save(mainmv);
								} else {
									//更新主链地址
									MemberVirtualcoin mainmv = (MemberVirtualcoin) factory.getCurrentSession().load(MemberVirtualcoin.class, Integer.parseInt(mainmvethObjId.toString()), LockMode.PESSIMISTIC_WRITE);
									mainmv.setWalletaddress(membervirtualcoin.getWalletaddress());
								}
							} catch (Exception e) {
								System.out.println("Exception：" + virtualcoin.getCname() + "(" + virtualcoin.getEunit() + ")" + "createAddress连接失败。");
								e.printStackTrace();
							}
						}
					}
					break;
				case 3:  //其它版
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("uid", member.getId());
					params.put("account", virtualcoin.getRpcuser());
					params.put("password", virtualcoin.getRpcpassword());
					try {
						membervirtualcoin.setWalletaddress(HttpHelper.post("http://" + virtualcoin.getRpcip() + ":" + virtualcoin.getRpcport() + "/asch/creatAddress", params));
					} catch (Exception e) {
						System.out.println("Exception：" + virtualcoin.getCname() + "(" + virtualcoin.getEunit() + ")" + "createAddress连接失败。");
						e.printStackTrace();
					}
					break;
			}
		}
	}
	
	/**
	 * 发起普通转账
	 * @param vcoinextractrecord
	 * @return TxId
	 */
	public static boolean transferAccounts(Vcoinextractrecord vcoinextractrecord, StringBuffer error) {
		boolean success = false;
		Virtualcoin virtualcoin = vcoinextractrecord.getVirtualcoin();
		if (virtualcoin.getRpcport() > 0) {
			switch (virtualcoin.getVirtualcoinType().getId()) {
				case 1:  //比特版
					Map<String, String> headers = new HashMap<String, String>();
					headers.put("Authorization", "Basic " + new String(Base64.encode(new String(virtualcoin.getRpcuser() + ":" + StringHelper.md5(virtualcoin.getRpcpassword())).getBytes())));
					try {
						String json = HttpHelper.post("http://" + virtualcoin.getRpcip() + ":" + virtualcoin.getRpcport(), headers, "{\"jsonrpc\":\"2.0\",\"method\":\"sendtoaddress\",\"params\":[\"" + vcoinextractrecord.getAddress() + "\"," + vcoinextractrecord.getCountactual() + "],\"id\":" + virtualcoin.getId() + "}");
						if (!json.startsWith("{") || !json.endsWith("}")) {
							System.out.println("Exception：" + virtualcoin.getCname() + "(" + virtualcoin.getEunit() + ")" + "transferAccounts连接失败，返回空数据不是有效的Json格式");
							error.append("返回空数据错误\n" + json);
						} else {
							JSONObject jsonobj = new JSONObject(json);
							if (!jsonobj.get("error").equals(null)) {
								System.out.println("Exception：" + virtualcoin.getCname() + "(" + virtualcoin.getEunit() + ")" + "transferAccounts中personal_sendTransaction接口返回：" + jsonobj);
								error.append(jsonobj);
							} else {
								vcoinextractrecord.setTxid(jsonobj.getString("result"));
								success = true;
							}
						}
					} catch (Exception e) {
						System.out.println("Exception：" + virtualcoin.getCname() + "(" + virtualcoin.getEunit() + ")" + "transferAccounts连接失败。");
						e.printStackTrace();
						error.append(e.getMessage());
					}
					break;
				case 2:  //以太版
					try {
						VirtualcoinGas vcoingas = (VirtualcoinGas) factory.getCurrentSession().get(VirtualcoinGas.class, 1);
						BigDecimal gasQuantity = new BigDecimal(String.valueOf(vcoingas.getGasQuantity()));             //以太油量
						BigDecimal gasPrice = new BigDecimal(String.valueOf(Math.pow(10, 9))).multiply(new BigDecimal(String.valueOf(vcoingas.getGasPrice())));     //以太油价
						BigDecimal countactual = new BigDecimal(String.valueOf(vcoinextractrecord.getCountactual())).setScale(5, BigDecimal.ROUND_FLOOR);
						String params = null;
						if (virtualcoin.getContractadres() == null) {
							//主链
							params = "{\"jsonrpc\":\"2.0\",\"method\":\"personal_sendTransaction\",\"params\":[{\"from\":\"" + virtualcoin.getWalletaddress() + "\",\"to\":\"" + vcoinextractrecord.getAddress() + "\",\"gas\":\"" + DataHelper.encodeHex(gasQuantity) + "\",\"gasPrice\":\"" + DataHelper.encodeHex(gasPrice) + "\",\"value\":\"" + DataHelper.encodeHex(countactual.multiply(powEighteen)) + "\"},\"" + StringHelper.md5(virtualcoin.getRpcpassword()) + "\"],\"id\":" + virtualcoin.getId() + "}";
						} else {
							//合约
							BigDecimal contractpow = new BigDecimal(String.valueOf(Math.pow(10, virtualcoin.getContractpow())));
							String inputdata = virtualcoin.getContracttransfermethod();
							if (virtualcoin.getContractreceiveadresindex() < virtualcoin.getContractamoutindex()) {
								for (int i = 0; i < virtualcoin.getContractreceiveadresindex(); i++) inputdata += insertZero("");
								inputdata += insertZero(vcoinextractrecord.getAddress());
								for (int i = virtualcoin.getContractreceiveadresindex() + 1; i < virtualcoin.getContractamoutindex(); i++) inputdata += insertZero("");
								inputdata += insertZero(DataHelper.encodeHex(countactual.multiply(contractpow)));
							} else {
								for (int i = 0; i < virtualcoin.getContractamoutindex(); i++) inputdata += insertZero("");
								inputdata += insertZero(DataHelper.encodeHex(countactual.multiply(contractpow)));
								for (int i = virtualcoin.getContractamoutindex() + 1; i < virtualcoin.getContractreceiveadresindex(); i++) inputdata += insertZero("");
								inputdata += insertZero(vcoinextractrecord.getAddress());
							}
							params = "{\"jsonrpc\":\"2.0\",\"method\":\"personal_sendTransaction\",\"params\":[{\"from\":\"" + virtualcoin.getWalletaddress() + "\",\"to\":\"" + virtualcoin.getContractadres() + "\",\"gas\":\"" + DataHelper.encodeHex(gasQuantity) + "\",\"gasPrice\":\"" + DataHelper.encodeHex(gasPrice) + "\",\"value\":\"0x0\",\"data\":\"" + inputdata + "\"},\"" + StringHelper.md5(virtualcoin.getRpcpassword()) + "\"],\"id\":" + virtualcoin.getId() + "}";
						}
						String json = HttpHelper.post("http://" + virtualcoin.getRpcip() + ":" + virtualcoin.getRpcport(), params);
						if (!json.startsWith("{") && !json.endsWith("}")) {
							System.out.println("Exception：" + virtualcoin.getCname() + "(" + virtualcoin.getEunit() + ")" + "transferAccounts中personal_sendTransaction接口返回：" + json);
							error.append("获取交易ID失败，提币可能已转出，请前往钱包或区块浏览器核实。");
						} else {
							JSONObject jsonobj = new JSONObject(json);
							if (jsonobj.has("error")) {
								System.out.println("Exception：" + virtualcoin.getCname() + "(" + virtualcoin.getEunit() + ")" + "transferAccounts中personal_sendTransaction接口返回：" + jsonobj);
								error.append(jsonobj);
							} else {
								vcoinextractrecord.setTxid(jsonobj.getString("result"));
								success = true;
							}
						}
					} catch (Exception e) {
						System.out.println("Exception：" + virtualcoin.getCname() + "(" + virtualcoin.getEunit() + ")" + "transferAccounts连接失败。");
						e.printStackTrace();
						error.append(e.getMessage());
					}
					break;
				case 3:  //其它版
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("amount", vcoinextractrecord.getCountactual());
					params.put("sendaddress", vcoinextractrecord.getAddress());
					params.put("account", virtualcoin.getRpcuser());
					params.put("password", virtualcoin.getRpcpassword());
					if(vcoinextractrecord.getRemark() != null) params.put("remark", vcoinextractrecord.getRemark());
					try {
						String json = HttpHelper.post("http://" + virtualcoin.getRpcip() + ":" + virtualcoin.getRpcport() + "/asch/transfer", params);
						if (json.isEmpty()) {
							System.out.println("Exception：" + virtualcoin.getCname() + "(" + virtualcoin.getEunit() + ")" + "transferAccounts连接失败，返回数据为空");
							error.append("返回数据为空");
						} else {
							if (!json.startsWith("{") || !json.endsWith("}")) {
								System.out.println("Exception：" + virtualcoin.getCname() + "(" + virtualcoin.getEunit() + ")" + "transferAccounts连接失败，返回空数据不是有效的Json格式");
								error.append("返回空数据错误\n" + json);
							} else {
								JSONObject jsonobj = new JSONObject(json);
								if (!jsonobj.getBoolean("success")) {
									System.out.println("Exception：" + virtualcoin.getCname() + "(" + virtualcoin.getEunit() + ")" + "transferAccounts连接失败，返回Json：" + json);
									error.append(jsonobj);
								} else {
									vcoinextractrecord.setTxid(jsonobj.getString("transactionId"));
									success = true;
								}
							}
						}
					} catch (Exception e) {
						System.out.println("Exception：" + virtualcoin.getCname() + "(" + virtualcoin.getEunit() + ")" + "transferAccounts连接失败。");
						e.printStackTrace();
						error.append("\n" + e.getMessage());
					}
					break;
			}
		}
		return success;
	}
	

	/**
	 * 补0至64位
	 * @param s
	 * @return
	 */
	public static String insertZero(String s){
		StringBuilder sb = new StringBuilder(s.replaceAll("^0x", ""));
		while (sb.length()<64) {
			sb = sb.insert(0, "0");
		}
		return sb.toString();
	}
}
