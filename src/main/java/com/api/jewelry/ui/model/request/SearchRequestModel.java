package com.api.jewelry.ui.model.request;

public class SearchRequestModel {

	private String searchKey;
	private Integer orderTypeId;
	private Integer orderStatusTypeId;
	private String fromDate="";
	private String toDate="";
	private Integer customerPurchasedInventoryId;
	private String customerType;
	
	public Integer getOrderTypeId() {
		return orderTypeId;
	}

	public void setOrderTypeId(Integer orderTypeId) {
		this.orderTypeId = orderTypeId;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public Integer getOrderStatusTypeId() {
		return orderStatusTypeId;
	}

	public void setOrderStatusTypeId(Integer orderStatusTypeId) {
		this.orderStatusTypeId = orderStatusTypeId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getCustomerPurchasedInventoryId() {
		return customerPurchasedInventoryId;
	}

	public void setCustomerPurchasedInventoryId(Integer customerPurchasedInventoryId) {
		this.customerPurchasedInventoryId = customerPurchasedInventoryId;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	
}
