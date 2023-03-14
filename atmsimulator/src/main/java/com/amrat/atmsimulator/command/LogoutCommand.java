package com.amrat.atmsimulator.command;

import com.amrat.atmsimulator.entity.Customer;
import com.amrat.atmsimulator.exception.ATMTransactionException;
import com.amrat.atmsimulator.service.ATMService;
import org.jetbrains.annotations.NotNull;


public class LogoutCommand extends AbstractCommand {

    public LogoutCommand(@NotNull CommandArguments commandArguments) {
        super(commandArguments);
    }

    @Override
    @NotNull
    public String execute(ATMService service) throws ATMTransactionException {
        if (args.length != 0) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        Customer customer = service.logoutCustomer();
        return new StringBuilder()
                .append("Goodbye, ").append(customer.getCustomerName()).append("!")
                .append("\n").append("\n").append("\n")
                .toString();
    }
}
