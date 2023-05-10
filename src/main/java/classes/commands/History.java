package classes.commands;

import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;

/**
 * Command to display the last 15 used commands
 */
public class History extends AbstractCommand {
    public History() {
        this.name = "history";
        this.description = "вывести последние 15 команд без их аргументов";
    }

    @Override
    public void execute(String[] args, CollectionManager collectionManager) throws NotEnoughArgumentsException, WrongArgumentException {
        String[] history = collectionManager.getHistory();
        System.out.print("Последние команды: ");
        for (int i = 0; i < 14; i++) {
            if (history[i] == null) break;
            if (history[i + 1] == null) {
                System.out.print(history[i] + ".");
                break;
            }
            System.out.print(history[i] + ", ");
        }
        if (history[14] != null) System.out.print(history[14] + ".");
        System.out.println();
        collectionManager.addToHistory(this);
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
        execute(args, collectionManager);
    }
}
