package com.amrat.atmsimulator.command;

import com.amrat.atmsimulator.entity.Customer;
import com.amrat.atmsimulator.exception.ATMTransactionException;
import com.amrat.atmsimulator.service.ATMService;
import org.jetbrains.annotations.NotNull;


public class LoginCommand extends AbstractCommand {

    public LoginCommand(@NotNull CommandArguments commandArguments) {
        super(commandArguments);
    }

    @Override
    @NotNull
    public String execute(ATMService service) throws ATMTransactionException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        String loginName = args[0];
        Customer customer = service.login(loginName);

        return new StringBuilder()
                .append("Hello, ").append(customer.getCustomerName()).append("!")
                .append("\n")
                .append(printBalanceStatement(customer))
                .append(printCreditStatement(service.getCreditStatement()))
                .append(printDebitStatement(service.getDebitStatement()))
                .toString();
    }
}
