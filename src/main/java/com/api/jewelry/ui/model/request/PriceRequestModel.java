package com.api.jewelry.ui.model.request;

import java.time.Instant;

public class PriceRequestModel {

	private double price;
	private long inventoryId;
	private Long karatId;
	private float weight;
	private Instant effectiveDateTime;
	private int totalPages;

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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public Instant getEffectiveDateTime() {
		return effectiveDateTime;
	}

	public void setEffectiveDateTime(Instant effectiveDateTime) {
		this.effectiveDateTime = effectiveDateTime;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

}
