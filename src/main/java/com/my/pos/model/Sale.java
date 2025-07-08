package com.my.pos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Sale {
    private int saleId;
    private LocalDateTime saleTime;
    private String empId;
    private int productId;
    private int quantity;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String customerId;

    public Sale() {}

    public Sale(String empId, int productId, int quantity,
                BigDecimal totalAmount, String paymentMethod, String customerId) {
        this.empId = empId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.customerId = customerId;
    }

    public int getSaleId() {
        return saleId;
    }
    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }
    public LocalDateTime getSaleTime() {
        return saleTime;
    }
    public void setSaleTime(LocalDateTime saleTime) {
        this.saleTime = saleTime;
    }
    public String getEmpId() {
        return empId;
    }
    public void setEmpId(String empId) {
        this.empId = empId;
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
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "saleId=" + saleId +
                ", saleTime=" + saleTime +
                ", empId='" + empId + '\'' +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}
