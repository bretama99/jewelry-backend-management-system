package com.api.jewelry.io.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.api.jewelry.model.audit.Audit;

@Entity(name = "price")
public class PriceEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 7018940012827577325L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long priceId;

	@Column(nullable = false, length = 50)
	private long inventoryId;

	@Column(nullable = false, length = 50)
	private double price;

	@Column
	private Long karatId;
	
	@Column
	private float weight;
	
	@Column(nullable = true, length = 50)
	private Instant effectiveDateTime;

	public Long getKaratId() {
		return karatId;
	}

	public void setKaratId(Long karatId) {
		this.karatId = karatId;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public long getPriceId() {
		return priceId;
	}

	public void setPriceId(long priceId) {
		this.priceId = priceId;
	}

	public long getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Instant getEffectiveDateTime() {
		return effectiveDateTime;
	}

	public void setEffectiveDateTime(Instant effectiveDateTime) {
		this.effectiveDateTime = effectiveDateTime;
	}

}
