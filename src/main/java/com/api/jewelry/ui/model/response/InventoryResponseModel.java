package com.api.jewelry.ui.model.response;

import org.springframework.web.multipart.MultipartFile;

public class InventoryResponseModel {

	private String productName;
	private String description;
	private String category;
	private Integer categoryId;
	private double grams;
	private double minimumGram;
	private String productImage;
	private int totalPages;
	private long inventoryId;
	private KaratResponeModel karatResponeModel;
	private PriceResponseModel priceResponseModel;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public KaratResponeModel getKaratResponeModel() {
		return karatResponeModel;
	}
	public void setKaratResponeModel(KaratResponeModel karatResponeModel) {
		this.karatResponeModel = karatResponeModel;
	}
	public PriceResponseModel getPriceResponseModel() {
		return priceResponseModel;
	}
	public void setPriceResponseModel(PriceResponseModel priceResponseModel) {
		this.priceResponseModel = priceResponseModel;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public double getGrams() {
		return grams;
	}
	public void setGrams(double grams) {
		this.grams = grams;
	}
	public double getMinimumGram() {
		return minimumGram;
	}
	public void setMinimumGram(double minimumGram) {
		this.minimumGram = minimumGram;
	}

	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public String getProductImage() {
		return productImage;
	}

	
}
