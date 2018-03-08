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
@Table(name = "vcoinextractrecord_state")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VcoinextractrecordState implements java.io.Serializable {

	private int id;
	private String name;
	private List<Vcoinextractrecord> vcoinextractrecords = new ArrayList<Vcoinextractrecord>(0);

	public VcoinextractrecordState() {
	}

	public VcoinextractrecordState(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public VcoinextractrecordState(int id, String name, List<Vcoinextractrecord> vcoinextractrecords) {
		this.id = id;
		this.name = name;
		this.vcoinextractrecords = vcoinextractrecords;
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vcoinextractrecordState")
	public List<Vcoinextractrecord> getVcoinextractrecords() {
		return this.vcoinextractrecords;
	}

	public void setVcoinextractrecords(List<Vcoinextractrecord> vcoinextractrecords) {
		this.vcoinextractrecords = vcoinextractrecords;
	}

}
