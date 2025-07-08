// src/main/java/com/my/pos/model/SaleReceipt.java
package com.my.pos.model;

import java.math.BigDecimal;

public class SaleReceipt {
    private final Sale sale;             // 실제 판매 기록
    private final BigDecimal change;     // 고객에게 돌려줄 거스름돈
    private final BigDecimal posBalance; // 결제 후 POS 기기 잔고

    public SaleReceipt(Sale sale, BigDecimal change, BigDecimal posBalance) {
        this.sale       = sale;
        this.change     = change;
        this.posBalance = posBalance;
    }

    public Sale getSale() {
        return sale;
    }

    public BigDecimal getChange() {
        return change;
    }

    public BigDecimal getPosBalance() {
        return posBalance;
    }

    @Override
    public String toString() {
        return "SaleReceipt{" +
                "sale=" + sale +
                ", change=" + change +
                ", posBalance=" + posBalance +
                '}';
    }
}
