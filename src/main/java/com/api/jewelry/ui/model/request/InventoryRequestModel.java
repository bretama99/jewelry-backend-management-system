package com.api.jewelry.ui.model.request;

import org.springframework.web.multipart.MultipartFile;

public class InventoryRequestModel {

	private String productName;
	private String description;
	private Integer categoryId;
	private double grams;
	private double minimumGram;
	private MultipartFile productImage;

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

	public MultipartFile getProductImage() {
		return productImage;
	}

	public void setProductImage(MultipartFile productImage) {
		this.productImage = productImage;
	}

}
