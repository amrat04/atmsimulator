package com.amrat.atmsimulator.command;

import com.amrat.atmsimulator.entity.Customer;
import com.amrat.atmsimulator.entity.Transaction;
import com.amrat.atmsimulator.exception.ATMTransactionException;
import com.amrat.atmsimulator.service.ATMService;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public interface Command {

    @NotNull
    String execute(ATMService service) throws ATMTransactionException;

    @NotNull
    default String printTransactionStatement(List<Transaction> transactions, Customer customer) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Transaction transaction : transactions) {
            String to = transaction.getTo();
            if (!to.equals(customer.getCustomerName())) {
                stringBuilder.append(transaction.getStatement()).append("\n");
            }
        }

        return stringBuilder.toString();
    }

    @NotNull
    default String printBalanceStatement(Customer customer) {
        return new StringBuilder()
                .append("Your balance is $ ").append(customer.getAccountBalance().setScale(0)).append("\n").toString();
    }

    @NotNull
    default String printCreditStatement(String statement) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!statement.isEmpty())
            stringBuilder.append(statement).append("\n");
        return stringBuilder.toString();
    }

    @NotNull
    default String printDebitStatement(String statement) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!statement.isEmpty()) {
            stringBuilder.append(statement).append("\n");
        }
        return stringBuilder.toString();
    }
}