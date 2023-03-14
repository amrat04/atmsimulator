package com.amrat.atmsimulator.command;

import com.amrat.atmsimulator.service.ATMService;
import org.jetbrains.annotations.NotNull;

public class ExitCommand extends AbstractCommand {

    public ExitCommand(
            @NotNull CommandArguments commandArguments) {
        super(commandArguments);
    }

    @Override
    @NotNull
    public String execute(ATMService service) {
        if (args.length != 0) {
            throw new IllegalArgumentException("Incorrect Argument");
        }
        return "ATM stopped.";
    }
}
