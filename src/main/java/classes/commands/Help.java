package classes.commands;

import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;
import java.util.HashMap;

/**
 * Command to display information about all available commands
 */
public class Help extends AbstractCommand {
    private final HashMap<String, AbstractCommand> commands;

    public Help(HashMap<String, AbstractCommand> commands) {
        this.commands = commands;
        this.description = "вывести справку по доступным командам";
        this.name = "help";
    }

    @Override
    public void execute(String[] args, CollectionManager collectionManager) {
        for (String command : this.commands.keySet()) {
            System.out.printf("%-41s - %s\n", commands.get(command).getName(), this.commands.get(command).getDescription());
        }
        collectionManager.addToHistory(this);
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
        execute(args, collectionManager);
    }

}
