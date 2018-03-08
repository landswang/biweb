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
@Table(name = "vcoinapply_state")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VcoinapplyState implements java.io.Serializable {

	private int id;
	private String name;
	private List<Vcoinapply> vcoinapplies = new ArrayList<Vcoinapply>(0);

	public VcoinapplyState() {
	}

	public VcoinapplyState(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public VcoinapplyState(int id, String name, List<Vcoinapply> vcoinapplies) {
		this.id = id;
		this.name = name;
		this.vcoinapplies = vcoinapplies;
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vcoinapplyState")
	public List<Vcoinapply> getVcoinapplies() {
		return this.vcoinapplies;
	}

	public void setVcoinapplies(List<Vcoinapply> vcoinapplies) {
		this.vcoinapplies = vcoinapplies;
	}

}
