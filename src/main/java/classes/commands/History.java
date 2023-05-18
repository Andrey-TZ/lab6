package classes.commands;

import classes.shells.ArgsShell;
import classes.shells.Response;
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
    public Response execute(CollectionManager collectionManager, ArgsShell args) {
        String[] history = collectionManager.getHistory();
        Response response = new Response("Последние команды: ");
        for (int i = 0; i < 14; i++) {
            if (history[i] == null) break;
            if (history[i + 1] == null) {
                response.setData(history[i] + ".");
                break;
            }
            response.setData(history[i] + ", ");
        }
        if (history[14] != null) response.setData(history[14] + ".");
        collectionManager.addToHistory(this);
        return response;
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
    }
}
