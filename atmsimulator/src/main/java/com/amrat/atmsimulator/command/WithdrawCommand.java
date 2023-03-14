package com.amrat.atmsimulator.command;

import com.amrat.atmsimulator.entity.Customer;
import com.amrat.atmsimulator.exception.ATMTransactionException;
import com.amrat.atmsimulator.service.ATMService;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;


public class WithdrawCommand extends AbstractCommand {

    public WithdrawCommand(@NotNull CommandArguments commandArguments) {
        super(commandArguments);
    }

    @Override
    @NotNull
    public String execute(ATMService service) throws ATMTransactionException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        BigDecimal amount = new BigDecimal(args[0]);
        service.withdrawAmount(amount);
        Customer customer = service.getCurrentCustomer();
        return new StringBuilder()
                .append(printBalanceStatement(customer))
                .append(printCreditStatement(service.getCreditStatement()))
                .append(printDebitStatement(service.getDebitStatement()))
                .toString();
    }
}
