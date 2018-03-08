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
@Table(name = "repairorder_state")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RepairorderState implements java.io.Serializable {

	private int id;
	private String name;
	private List<Repairorder> repairorders = new ArrayList<Repairorder>(0);

	public RepairorderState() {
	}

	public RepairorderState(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public RepairorderState(int id, String name, List<Repairorder> repairorders) {
		this.id = id;
		this.name = name;
		this.repairorders = repairorders;
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "repairorderState")
	public List<Repairorder> getRepairorders() {
		return this.repairorders;
	}

	public void setRepairorders(List<Repairorder> repairorders) {
		this.repairorders = repairorders;
	}

}
