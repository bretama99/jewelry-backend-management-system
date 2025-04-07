package com.api.jewelry.ui.model.request;


import java.math.BigDecimal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class KaratRequestModel {

@NotBlank(message = "Karat is required")
private String karat;

@NotNull(message = "Purity is required")
@Min(value = 0, message = "Purity must be greater than 0")
@Max(value = 1, message = "Purity must be less than or equal to 1")
private BigDecimal purity;

@NotNull(message = "Buying discount is required")
@Min(value = 0, message = "Buying discount must be greater than or equal to 0")
@Max(value = 100, message = "Buying discount must be less than or equal to 100")
private BigDecimal buyingDiscount;

@NotNull(message = "Weight is required")
@Positive(message = "Weight must be greater than 0")
private BigDecimal weight;

// Getters and Setters
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