package com.api.jewelry.ui.model.response;

import java.time.Instant;

public class PriceResponseModel {

	private long priceId;
	private double price;
	private Long karatId;
	private float weight;
	private KaratResponeModel karatResponeModel;
	
	public KaratResponeModel getKaratResponeModel() {
		return karatResponeModel;
	}

	public void setKaratResponeModel(KaratResponeModel karatResponeModel) {
		this.karatResponeModel = karatResponeModel;
	}

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

	private Instant effectiveDateTime;
	private long inventoryId;
	private String productName;
	private int totalPages;

	public long getPriceId() {
		return priceId;
	}

	public void setPriceId(long priceId) {
		this.priceId = priceId;
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

	public long getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

}
