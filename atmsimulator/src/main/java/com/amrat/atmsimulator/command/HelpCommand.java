package com.amrat.atmsimulator.command;

import com.amrat.atmsimulator.service.ATMService;
import org.jetbrains.annotations.NotNull;


public class HelpCommand extends AbstractCommand {

    public HelpCommand(@NotNull CommandArguments commandArguments) {
        super(commandArguments);
    }

    @Override
    @NotNull
    public String execute(ATMService service) {
        if (args.length != 0) {
            throw new IllegalArgumentException("Incorrect Argument");
        }
        return "\nlogin [name] - Logs in as this customer and creates the customer if not exist\n" +
                "logout - Logs out of the current customer\n" +
                "deposit [amount] - Deposits this amount to the logged in customer\n" +
                "withdraw [amount] - Withdraws this amount from the logged in customer\n" +
                "transfer [target] [amount] - Transfers this amount from the logged in customer to the target customer\n" +
                "exit - Exit the program\n" +
                "help - Get Help Menu\n";
    }
}
