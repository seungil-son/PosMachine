package com.my.pos.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DailySales {
    private LocalDate saleDate;
    private BigDecimal totalSales;

    public DailySales() {}

    public DailySales(LocalDate saleDate, BigDecimal totalSales) {
        this.saleDate = saleDate;
        this.totalSales = totalSales;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }
    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }
    public BigDecimal getTotalSales() {
        return totalSales;
    }
    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    @Override
    public String toString() {
        return "DailySales{" +
                "saleDate=" + saleDate +
                ", totalSales=" + totalSales +
                '}';
    }
}
