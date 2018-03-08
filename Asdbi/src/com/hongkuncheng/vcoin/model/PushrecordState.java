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
@Table(name = "pushrecord_state")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PushrecordState implements java.io.Serializable {

	private int id;
	private String name;
	private List<Pushrecord> pushrecords = new ArrayList<Pushrecord>(0);

	public PushrecordState() {
	}

	public PushrecordState(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public PushrecordState(int id, String name, List<Pushrecord> pushrecords) {
		this.id = id;
		this.name = name;
		this.pushrecords = pushrecords;
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pushrecordState")
	public List<Pushrecord> getPushrecords() {
		return this.pushrecords;
	}

	public void setPushrecords(List<Pushrecord> pushrecords) {
		this.pushrecords = pushrecords;
	}

}
