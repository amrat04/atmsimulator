package com.amrat.atmsimulator.service;

import com.amrat.atmsimulator.entity.Account;
import com.amrat.atmsimulator.entity.Customer;
import com.amrat.atmsimulator.entity.Transaction;
import com.amrat.atmsimulator.exception.ATMTransactionException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ATMServiceImpl implements ATMService{

    private Customer currentCustomer;

    private final Map<String, Customer> customersMap = new HashMap<>();

    public final LinkedHashMap<Customer, Account> creditAccountMap = new LinkedHashMap<>();

    public final LinkedHashMap<Customer, Account> debitAccountMap = new LinkedHashMap<>();

    @Override
    public @NotNull Customer login(@NotNull String loginUser) throws ATMTransactionException {
        if (currentCustomer == null) {
            if (!customersMap.containsKey(loginUser)) {
                customersMap.putIfAbsent(loginUser, new Customer(loginUser));
            }
            currentCustomer = customersMap.get(loginUser);
            return currentCustomer;
        } else {
            throw new ATMTransactionException("Login name is Incorrect or Empty");
        }
    }

    @Override
    public @NotNull Customer getCurrentCustomer() throws ATMTransactionException {
        if (currentCustomer == null) {
            throw new ATMTransactionException("Login name is Incorrect or Empty");
        } else {
            return currentCustomer;
        }
    }

    @Override
    public @NotNull List<Transaction> depositAmount(@NotNull BigDecimal amount) throws ATMTransactionException {
        if (amount.doubleValue() <= 0) {
            throw new ATMTransactionException(amount + " should be greater than zero");
        }

        if (currentCustomer == null) {
            throw new ATMTransactionException("Login name is Incorrect or Empty");
        }

        List<Transaction> transactions = new ArrayList<>();

        // pay debts
        for (Map.Entry<Customer, Account> entry : new ArrayList<>(creditAccountMap.entrySet())) {
            Customer creditor = entry.getKey();
            Account creditAccount = entry.getValue();

            BigDecimal paymentAmount = creditAccount.getBalance().min(amount);
            if (paymentAmount.doubleValue() > 0) {
                Transaction transaction = payTo(creditor, paymentAmount);
                transactions.add(transaction);
                deposit(transaction.getAmount());
                amount = amount.subtract(transaction.getAmount());
            } else {
                break;
            }
        }

        // do deposit
        if (amount.doubleValue() > 0) {
            currentCustomer.account.addAmount(amount);
            Transaction transaction = new Transaction(null, currentCustomer.getCustomerName(), amount);
            transactions.add(transaction);
        }

        return transactions;
    }

    @Override
    public @NotNull Transaction withdrawAmount(@NotNull BigDecimal amount) throws ATMTransactionException {
        if (amount.doubleValue() <= 0) {
            throw new ATMTransactionException(amount + " should be greater than zero");
        } else if (currentCustomer == null) {
            throw new ATMTransactionException("there is no authenticated customer");
        }
        if (currentCustomer.account.getBalance().subtract(amount).doubleValue() < 0) {
            throw new ATMTransactionException("not enough money");
        } else {
            currentCustomer.account.subtractAmount(amount);
            return new Transaction(currentCustomer.getCustomerName(), null, amount);
        }
    }

    @Override
    public @NotNull List<Transaction> transferAmount(@NotNull String toLoginName, @NotNull BigDecimal amount) throws ATMTransactionException {
        if (amount.doubleValue() <= 0) {
            throw new ATMTransactionException(amount + " should be greater than zero");
        }

        if (currentCustomer == null) {
            throw new ATMTransactionException("there is no authenticated customer");
        }

        if (!customersMap.containsKey(toLoginName)) {
            throw new ATMTransactionException(String.format("no customer with login name %s found", toLoginName));
        }

        if (toLoginName.equals(currentCustomer.getCustomerName())) {
            throw new ATMTransactionException("you can't transfer money to yourself");
        }

        List<Transaction> transactions = new ArrayList<>();

        // owe from destination
        if (isOwedTo(currentCustomer)) {
            payTo(customersMap.get(toLoginName), amount);
            amount = amount.subtract(amount);
        }

        // make transfer to destination
        if (amount.doubleValue() > 0 && currentCustomer.account.getBalance().doubleValue() > 0) {
            BigDecimal transferAmount = currentCustomer.account.getBalance().min(amount);
            customersMap.get(toLoginName).account.addAmount(transferAmount);
            currentCustomer.account.subtractAmount(transferAmount);

            Transaction transaction = new Transaction(currentCustomer.getCustomerName(), toLoginName, transferAmount);
            transactions.add(transaction);

            amount = amount.subtract(transferAmount);
        }

        // owe to destination
        if (amount.doubleValue() > 0) {
            oweTo(customersMap.get(toLoginName), amount);
        }

        return transactions;
    }

    private boolean isOwedTo(@NotNull Customer creditor) {
        return creditAccountMap.containsKey(creditor);
    }
    private void oweTo(@NotNull Customer creditor, @NotNull BigDecimal amount) {
        creditAccountMap.putIfAbsent(creditor, new Account());
        Account creditAccount = creditAccountMap.get(creditor);
        creditAccount.addAmount(amount);

        debitAccountMap.putIfAbsent(currentCustomer, new Account());
        Account debitAccount = debitAccountMap.get(this);
        debitAccount.addAmount(amount);
    }
    @Override
    public @NotNull Customer logoutCustomer() throws ATMTransactionException {
        if (currentCustomer != null) {
            Customer loggedOutCustomer = currentCustomer;
            currentCustomer = null;
            return loggedOutCustomer;
        } else {
            throw new ATMTransactionException("there is no authenticated customer");
        }
    }

    private Transaction payTo(@NotNull Customer creditor, @NotNull BigDecimal amount) {
        creditAccountMap.putIfAbsent(creditor, new Account());
        Account creditAccount = creditAccountMap.get(creditor);
        creditAccount.subtractAmount(amount);

        if (creditAccount.isEmpty()) {
            creditAccountMap.remove(creditor);
        }

        debitAccountMap.putIfAbsent(currentCustomer, new Account());
        Account debitAccount = debitAccountMap.get(this);
        debitAccount.subtractAmount(amount);

        if (debitAccount.isEmpty()) {
            debitAccountMap.remove(this);
        }

        return new Transaction(currentCustomer.getCustomerName(), creditor.getCustomerName(), amount);
    }

    private List<Transaction> deposit(@NotNull BigDecimal amount) {
        List<Transaction> transactions = new ArrayList<>();

        // pay debts
        for (Map.Entry<Customer, Account> entry : new ArrayList<>(creditAccountMap.entrySet())) {
            Customer creditor = entry.getKey();
            Account creditAccount = entry.getValue();

            BigDecimal paymentAmount = creditAccount.getBalance().min(amount);
            if (paymentAmount.doubleValue() > 0) {
                Transaction transaction = payTo(creditor, paymentAmount);
                transactions.add(transaction);
                deposit(transaction.getAmount());
                amount = amount.subtract(transaction.getAmount());
            } else {
                break;
            }
        }

        // do deposit
        if (amount.doubleValue() > 0) {
            currentCustomer.account.addAmount(amount);
            Transaction transaction = new Transaction(null, currentCustomer.getCustomerName(), amount);
            transactions.add(transaction);
        }

        return transactions;
    }

    @NotNull
    public Map<Customer, Account> getCreditAccountMap() {
        return new LinkedHashMap<>(creditAccountMap);
    }

    @NotNull
    @Override
    public String getCreditStatement() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<Customer, Account> entry : getCreditAccountMap().entrySet()) {
            Customer creditor = entry.getKey();
            BigDecimal amount = entry.getValue().getBalance();
            stringBuilder.append("Owed $")
                    .append(amount)
                    .append(" to ")
                    .append(creditor.getCustomerName());
        }

        return stringBuilder.toString();
    }

    @NotNull
    @Override
    public String getDebitStatement() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<Customer, Account> entry : getDebitAccountMap().entrySet()) {
            Customer debtor = entry.getKey();
            BigDecimal amount = entry.getValue().getBalance();
            stringBuilder.append("Owed $")
                    .append(amount)
                    .append(" from ")
                    .append(debtor.getCustomerName());
        }

        return stringBuilder.toString();
    }

    @NotNull
    public Map<Customer, Account> getDebitAccountMap() {
        return new LinkedHashMap<>(debitAccountMap);
    }
}
