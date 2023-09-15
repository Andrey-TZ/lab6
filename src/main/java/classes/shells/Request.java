package classes.shells;

import classes.commands.AbstractCommand;
import classes.dataBase.UserData;

import java.io.Serializable;

public class Request implements Serializable {
    private final AbstractCommand command;
    private final ArgsShell arguments;
    private  UserData user;

    public Request(AbstractCommand command, ArgsShell arguments) {
        this.command = command;
        this.arguments = arguments;

    }

    public void setUser(UserData user){
        this.user = user;
    }

    public AbstractCommand getCommand() {
        return command;
    }

    public ArgsShell getArguments() {
        return arguments;
    }

    public UserData getUser(){return user;}
}
