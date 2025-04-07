package com.api.jewelry.io.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.web.multipart.MultipartFile;

import com.api.jewelry.model.audit.Audit;

@Entity(name = "inventory")
public class InventoryEntity extends Audit {

	private static final long serialVersionUID = -1130814157378815168L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long inventoryId;

	@Column(nullable = false, length = 250)
	private String productName;

	@Column(nullable = false, length = 150)
	private String description;

	@Column(nullable = true, length = 100)
	private Integer categoryId;

	@Column(length = 250)
	private double grams;

	@Column(nullable = true, length = 100)
	private double minimumGram;

	@Column(nullable = true, length = 100)
	private String productImage;

	@Column
	private boolean isDeleted = false;

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

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
