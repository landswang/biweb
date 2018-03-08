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
@Table(name = "member_changemobile_state")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MemberChangemobileState implements java.io.Serializable {

	private int id;
	private String name;
	private List<MemberChangemobile> memberChangemobiles = new ArrayList<MemberChangemobile>(0);

	public MemberChangemobileState() {
	}

	public MemberChangemobileState(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public MemberChangemobileState(int id, String name, List<MemberChangemobile> memberChangemobiles) {
		this.id = id;
		this.name = name;
		this.memberChangemobiles = memberChangemobiles;
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "memberChangemobileState")
	public List<MemberChangemobile> getMemberChangemobiles() {
		return this.memberChangemobiles;
	}

	public void setMemberChangemobiles(List<MemberChangemobile> memberChangemobiles) {
		this.memberChangemobiles = memberChangemobiles;
	}

}
