package com.amrat.atmsimulator.command;

import com.amrat.atmsimulator.entity.Customer;
import com.amrat.atmsimulator.entity.Transaction;
import com.amrat.atmsimulator.exception.ATMTransactionException;
import com.amrat.atmsimulator.service.ATMService;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;


public class DepositCommand extends AbstractCommand {

    public DepositCommand(@NotNull CommandArguments commandArguments) {
        super(commandArguments);
    }

    @Override
    @NotNull
    public String execute(ATMService service) throws ATMTransactionException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Wrong argument count.\n Correct command format 'deposit [amount]'");
        }

        BigDecimal amount = new BigDecimal(args[0]);
        List<Transaction> transactions = service.depositAmount(amount);
        Customer customer = service.getCurrentCustomer();
        return new StringBuilder()
                .append(printTransactionStatement(transactions, customer))
                .append(printBalanceStatement(customer))
                .append(printCreditStatement(service.getCreditStatement()))
                .append(printDebitStatement(service.getDebitStatement()))
                .toString();
    }
}