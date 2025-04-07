package com.api.jewelry.ui.model.response;


import java.math.BigDecimal;

public class KaratResponeModel {

private Long id;
private String karat;
private BigDecimal purity;
private BigDecimal buyingDiscount;
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