package com.amrat.atmsimulator.entity;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class Account {

    protected BigDecimal balance = new BigDecimal(0);

    public BigDecimal getBalance() {
        return balance;
    }
    public void subtractAmount(@NotNull BigDecimal amount) {
        if (balance.subtract(amount).doubleValue() < 0) {
            throw new IllegalArgumentException("Account balance is not enough");
        }

        balance = balance.subtract(amount);
    }

    public void addAmount(@NotNull BigDecimal amount){
        balance = balance.add(amount);
    }

    public boolean isEmpty() {
        return balance.compareTo(BigDecimal.ZERO) == 0;
    }
}
