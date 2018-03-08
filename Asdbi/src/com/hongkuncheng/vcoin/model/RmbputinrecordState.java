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
@Table(name = "rmbputinrecord_state")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RmbputinrecordState implements java.io.Serializable {

	private int id;
	private String name;
	private List<Rmbputinrecord> rmbputinrecords = new ArrayList<Rmbputinrecord>(0);

	public RmbputinrecordState() {
	}

	public RmbputinrecordState(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public RmbputinrecordState(int id, String name, List<Rmbputinrecord> rmbputinrecords) {
		this.id = id;
		this.name = name;
		this.rmbputinrecords = rmbputinrecords;
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rmbputinrecordState")
	public List<Rmbputinrecord> getRmbputinrecords() {
		return this.rmbputinrecords;
	}

	public void setRmbputinrecords(List<Rmbputinrecord> rmbputinrecords) {
		this.rmbputinrecords = rmbputinrecords;
	}

}
