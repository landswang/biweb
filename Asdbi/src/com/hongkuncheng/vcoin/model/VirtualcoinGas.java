package com.hongkuncheng.vcoin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "virtualcoin_gas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VirtualcoinGas implements java.io.Serializable {

	private int id;
	private Integer gasQuantity;
	private Integer gasPrice;

	public VirtualcoinGas() {
	}

	public VirtualcoinGas(int id) {
		this.id = id;
	}

	public VirtualcoinGas(int id, Integer gasQuantity, Integer gasPrice) {
		this.id = id;
		this.gasQuantity = gasQuantity;
		this.gasPrice = gasPrice;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "gas_quantity")
	public Integer getGasQuantity() {
		return this.gasQuantity;
	}

	public void setGasQuantity(Integer gasQuantity) {
		this.gasQuantity = gasQuantity;
	}

	@Column(name = "gas_price")
	public Integer getGasPrice() {
		return this.gasPrice;
	}

	public void setGasPrice(Integer gasPrice) {
		this.gasPrice = gasPrice;
	}

}
