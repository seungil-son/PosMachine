// src/main/java/com/my/pos/model/CashBox.java
package com.my.pos.model;

import java.math.BigDecimal;

public class CashBox {
    private int id;
    private BigDecimal balance;


    public CashBox(int id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    @Override
    public String toString() {
        return "CashBox{id=" + id + ", balance=" + balance + "}";
    }
}
