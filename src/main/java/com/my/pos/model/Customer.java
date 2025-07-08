// src/main/java/com/my/pos/model/Customer.java
package com.my.pos.model;

import java.math.BigDecimal;

public class Customer {
    private String customerId;
    private BigDecimal cashBalance;
    private BigDecimal cardBalance;

    public Customer(String customerId,
                    BigDecimal cashBalance,
                    BigDecimal cardBalance) {
        this.customerId   = customerId;
        this.cashBalance  = cashBalance;
        this.cardBalance  = cardBalance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public BigDecimal getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(BigDecimal cashBalance) {
        this.cashBalance = cashBalance;
    }

    public BigDecimal getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(BigDecimal cardBalance) {
        this.cardBalance = cardBalance;
    }
}
