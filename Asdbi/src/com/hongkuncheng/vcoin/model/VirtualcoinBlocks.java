package com.hongkuncheng.vcoin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "virtualcoin_blocks")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VirtualcoinBlocks implements java.io.Serializable {

	private int id;
	private Virtualcoin virtualcoin;
	private int blocks;

	public VirtualcoinBlocks() {
	}

	public VirtualcoinBlocks(Virtualcoin virtualcoin, int blocks) {
		this.virtualcoin = virtualcoin;
		this.blocks = blocks;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "virtualcoin_id", nullable = false)
	public Virtualcoin getVirtualcoin() {
		return this.virtualcoin;
	}

	public void setVirtualcoin(Virtualcoin virtualcoin) {
		this.virtualcoin = virtualcoin;
	}

	@Column(name = "blocks", nullable = false)
	public int getBlocks() {
		return this.blocks;
	}

	public void setBlocks(int blocks) {
		this.blocks = blocks;
	}

}
