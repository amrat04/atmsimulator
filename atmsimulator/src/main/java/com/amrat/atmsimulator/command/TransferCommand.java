package com.amrat.atmsimulator.command;

import com.amrat.atmsimulator.entity.Customer;
import com.amrat.atmsimulator.entity.Transaction;
import com.amrat.atmsimulator.exception.ATMTransactionException;
import com.amrat.atmsimulator.service.ATMService;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class TransferCommand extends AbstractCommand {

    public TransferCommand(@NotNull CommandArguments commandArguments) {
        super(commandArguments);
    }

    @Override
    @NotNull
    public String execute(ATMService service) throws ATMTransactionException {
        if (args.length != 2) {
            throw new IllegalArgumentException(
                    "Illegal Argument");
        }
        String toLoginName = args[0];
        BigDecimal amount = new BigDecimal(args[1]);
        List<Transaction> transactions = service.transferAmount(toLoginName, amount);
        Customer customer = service.getCurrentCustomer();

        return new StringBuilder()
                .append(printTransactionStatement(transactions, customer))
                .append(printBalanceStatement(customer))
                .append(printCreditStatement(service.getCreditStatement()))
                .append(printDebitStatement(service.getDebitStatement()))
                .toString();
    }
}
