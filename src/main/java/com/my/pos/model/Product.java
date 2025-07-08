package com.my.pos.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Product {
    private int productId;
    private String name;
    private String manufacturer;
    private LocalDate expiryDate;
    private boolean adultOnly;
    private BigDecimal price;

    public Product() {}

    public Product(String name, String manufacturer, LocalDate expiryDate,
                   boolean adultOnly, BigDecimal price) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.expiryDate = expiryDate;
        this.adultOnly = adultOnly;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    public boolean isAdultOnly() {
        return adultOnly;
    }
    public void setAdultOnly(boolean adultOnly) {
        this.adultOnly = adultOnly;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", expiryDate=" + expiryDate +
                ", adultOnly=" + adultOnly +
                ", price=" + price +
                '}';
    }
}
