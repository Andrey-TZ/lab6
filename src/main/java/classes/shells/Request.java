package classes.shells;

import classes.commands.AbstractCommand;

import java.io.Serializable;

public class Request implements Serializable {
    private AbstractCommand command;
    private ArgsShell arguments;
    public Request(AbstractCommand command, ArgsShell arguments){
        this.command = command;
        this.arguments = arguments;
    }

    public AbstractCommand getCommand() {
        return command;
    }

    public ArgsShell getArguments() {
        return arguments;
    }
}
