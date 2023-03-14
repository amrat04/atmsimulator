package com.amrat.atmsimulator.controller;

import com.amrat.atmsimulator.command.Command;
import com.amrat.atmsimulator.command.CommandFactory;
import com.amrat.atmsimulator.command.CommandParser;
import com.amrat.atmsimulator.command.ExitCommand;
import com.amrat.atmsimulator.service.ATMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Optional;

@Controller
public class ATMController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CommandFactory commandFactory;

    private final ATMService service;

    @Autowired
    public ATMController(CommandFactory commandFactory, ATMService service) {
        this.commandFactory = commandFactory;
        this.service = service;
    }

    public void doProcess(InputStream in, OutputStream out) {
        log.info("ATM started successfully");

        PrintStream outStream = new PrintStream(out);
        outStream.println("ATM has been started. Inputs are case-sensitive. Use 'help' to see available commands.");
        outStream.println();

        for (CommandParser commandParser = new CommandParser(in, commandFactory); ; ) {
            try {
                if (commandParser.hasNext()) {
                    Optional<Command> commandOptional = commandParser.next();
                    if (commandOptional.isPresent()) {
                        Command command = commandOptional.get();
                        String result = command.execute(service);
                        outStream.println(result);
                        if (command instanceof ExitCommand) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Exception Occurred : ", e);
                outStream.println("Exception occurred: " + e.getMessage());
            }
        }
    }
}