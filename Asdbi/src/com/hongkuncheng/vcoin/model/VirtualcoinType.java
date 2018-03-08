package com.hongkuncheng.vcoin.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "virtualcoin_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VirtualcoinType implements java.io.Serializable {

	private int id;
	private String name;
	private List<Virtualcoin> virtualcoins = new ArrayList<Virtualcoin>(0);

	public VirtualcoinType() {
	}

	public VirtualcoinType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public VirtualcoinType(int id, String name, List<Virtualcoin> virtualcoins) {
		this.id = id;
		this.name = name;
		this.virtualcoins = virtualcoins;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "virtualcoinType")
	public List<Virtualcoin> getVirtualcoins() {
		return this.virtualcoins;
	}

	public void setVirtualcoins(List<Virtualcoin> virtualcoins) {
		this.virtualcoins = virtualcoins;
	}

}
