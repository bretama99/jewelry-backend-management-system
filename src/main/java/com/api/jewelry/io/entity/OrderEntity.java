package com.api.jewelry.io.entity;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.api.jewelry.model.audit.Audit;

@Entity(name="orders")
public class OrderEntity extends Audit {

	private static final long serialVersionUID = -5037377443545177244L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long orderId;

	@Column(nullable = false)
	private String userId;
	
	@Column(nullable = false,unique = true)
	private String orderNumber;
	
	@Column(nullable = false)
	private double totalPrice;
	
	@Column
	private Instant orderDateTime;
	
	@OneToMany(targetEntity = OrderItemEntity.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "orderId", referencedColumnName = "orderId", nullable = true)
	private List<OrderItemEntity> orderItems;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Instant getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(Instant orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public List<OrderItemEntity> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemEntity> orderItems) {
		this.orderItems = orderItems;
	}
	
	
}
