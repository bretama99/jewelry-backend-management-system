package com.api.jewelry.io.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "karats")
public class KaratEntity {
 
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 
 @Column(nullable = false)
 private String karat;
 
 @Column(nullable = false, precision = 5, scale = 3)
 private BigDecimal purity;
 
 @Column(nullable = false, precision = 5, scale = 2)
 private BigDecimal buyingDiscount;
 
 @Column(nullable = false, precision = 10, scale = 2)
 private BigDecimal weight;
 
 // Getters and Setters
 public Long getId() {
     return id;
 }
 
 public void setId(Long id) {
     this.id = id;
 }
 
 public String getKarat() {
     return karat;
 }
 
 public void setKarat(String karat) {
     this.karat = karat;
 }
 
 public BigDecimal getPurity() {
     return purity;
 }
 
 public void setPurity(BigDecimal purity) {
     this.purity = purity;
 }
 
 public BigDecimal getBuyingDiscount() {
     return buyingDiscount;
 }
 
 public void setBuyingDiscount(BigDecimal buyingDiscount) {
     this.buyingDiscount = buyingDiscount;
 }
 
 public BigDecimal getWeight() {
     return weight;
 }
 
 public void setWeight(BigDecimal weight) {
     this.weight = weight;
 }
}