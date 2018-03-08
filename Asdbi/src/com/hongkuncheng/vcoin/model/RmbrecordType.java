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
@Table(name = "rmbrecord_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RmbrecordType implements java.io.Serializable {

	private int id;
	private String name;
	private List<Rmbrecord> rmbrecords = new ArrayList<Rmbrecord>(0);

	public RmbrecordType() {
	}

	public RmbrecordType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public RmbrecordType(int id, String name, List<Rmbrecord> rmbrecords) {
		this.id = id;
		this.name = name;
		this.rmbrecords = rmbrecords;
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rmbrecordType")
	public List<Rmbrecord> getRmbrecords() {
		return this.rmbrecords;
	}

	public void setRmbrecords(List<Rmbrecord> rmbrecords) {
		this.rmbrecords = rmbrecords;
	}

}
