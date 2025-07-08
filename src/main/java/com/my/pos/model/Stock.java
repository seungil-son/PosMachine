package com.my.pos.model;

import java.time.LocalDateTime;

public class Stock {
    private int productId;
    private int quantity;
    private LocalDateTime lastUpdated;

    public Stock() {}

    public Stock(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
