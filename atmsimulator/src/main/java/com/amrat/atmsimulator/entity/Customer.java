package com.amrat.atmsimulator.entity;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


@AllArgsConstructor
public class Customer {

    private  String customerName;

    public final Account account = new Account();

    @NotNull
    public String getCustomerName() {
        return customerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerName, customer.customerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerName);
    }

    public BigDecimal getAccountBalance() {
        return account.getBalance().subtract(BigDecimal.ZERO);
    }

}
