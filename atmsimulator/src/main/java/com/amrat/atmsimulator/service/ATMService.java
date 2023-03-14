package com.amrat.atmsimulator.service;

import com.amrat.atmsimulator.entity.Customer;
import com.amrat.atmsimulator.entity.Transaction;
import com.amrat.atmsimulator.exception.ATMTransactionException;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

public interface ATMService {

    @NotNull
    Customer login(@NotNull String loginName) throws ATMTransactionException;

    @NotNull
    Customer getCurrentCustomer() throws ATMTransactionException;

    @NotNull
    List<Transaction> depositAmount(@NotNull BigDecimal amount) throws ATMTransactionException;

    @NotNull
    Transaction withdrawAmount(@NotNull BigDecimal amount) throws ATMTransactionException;

    @NotNull
    List<Transaction> transferAmount(@NotNull String toLoginName, @NotNull BigDecimal amount) throws ATMTransactionException;

    @NotNull
    Customer logoutCustomer() throws ATMTransactionException;

    public String getDebitStatement();

    public String getCreditStatement();
}
